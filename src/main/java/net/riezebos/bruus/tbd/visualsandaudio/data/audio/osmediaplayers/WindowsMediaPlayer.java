package net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//Controls the Spotify desktop app on Windows through the Windows media controls, the same ones the media keys use.
//No Spotify account or API key is involved. One PowerShell process stays open in the background, because starting
//PowerShell takes about a second: the game writes one command per line to it and reads one line back.
public class WindowsMediaPlayer {

    // Singleton instance
    private static WindowsMediaPlayer instance = null;

    private static final long STARTUP_TIMEOUT_MILLIS = 10000;
    private static final long COMMAND_TIMEOUT_MILLIS = 4000;

    // Runs one command at a time, in the order they were given, so a restart always happens before the play after it
    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "WindowsMediaPlayer");
        thread.setDaemon(true);
        return thread;
    });

    // Cached values for playback information, updated by every answer from PowerShell
    private volatile boolean hasStartedMusic = false;
    private volatile boolean isPlaying = false;
    private volatile double currentSeconds = 0;
    private volatile double totalSeconds = 0;
    private volatile String playbackStatus = "";
    private volatile String trackTitle = "";
    private volatile boolean canChangePosition = false;
    private volatile String lastError = "";

    // Only touched from the executor thread
    private Process powerShell;
    private BufferedWriter commandWriter;
    private BufferedReader responseReader;

    // Commands: play, pause, next, restart, status. Every command answers with one line:
    // OK|<status>|<position seconds>|<duration seconds>|<can change position>|<artist - title>, NO_SESSION or ERROR|<message>
    private static final String POWERSHELL_SCRIPT = """
            $ErrorActionPreference = 'Stop'
            [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding $false
            try {
                Add-Type -AssemblyName System.Runtime.WindowsRuntime
                $asTaskMethod = [System.WindowsRuntimeSystemExtensions].GetMethods() | Where-Object { $_.Name -eq 'AsTask' -and $_.GetParameters().Count -eq 1 -and $_.GetParameters()[0].ParameterType.Name -eq 'IAsyncOperation`1' } | Select-Object -First 1
                $null = [Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager,Windows.Media.Control,ContentType=WindowsRuntime]
                $null = [Windows.Media.Control.GlobalSystemMediaTransportControlsSessionMediaProperties,Windows.Media.Control,ContentType=WindowsRuntime]
                function Await($operation, [Type]$resultType) {
                    $task = $asTaskMethod.MakeGenericMethod($resultType).Invoke($null, @($operation))
                    if (-not $task.Wait(1500)) { throw 'Windows took too long to answer' }
                    return $task.Result
                }
                function Get-Position($session) {
                    $timeline = $session.GetTimelineProperties()
                    $position = $timeline.Position.TotalSeconds
                    $duration = ($timeline.EndTime - $timeline.StartTime).TotalSeconds
                    $sinceUpdate = ([DateTimeOffset]::Now - $timeline.LastUpdatedTime).TotalSeconds
                    if ($session.GetPlaybackInfo().PlaybackStatus -eq 'Playing' -and $sinceUpdate -gt 0 -and $sinceUpdate -lt $duration) {
                        $position += $sinceUpdate
                    }
                    return $position
                }
                $manager = Await ([Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager]::RequestAsync()) ([Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager])
            } catch {
                [Console]::Out.WriteLine('ERROR|' + ($_.Exception.Message -replace '[\\r\\n]+', ' '))
                exit 1
            }
            $culture = [System.Globalization.CultureInfo]::InvariantCulture
            [Console]::Out.WriteLine('READY')
            while ($null -ne ($command = [Console]::In.ReadLine())) {
                try {
                    $session = $manager.GetSessions() | Where-Object { $_.SourceAppUserModelId -like '*Spotify*' } | Select-Object -First 1
                    if ($null -eq $session) {
                        [Console]::Out.WriteLine('NO_SESSION')
                        continue
                    }
                    if ($command -eq 'play') {
                        $null = Await ($session.TryPlayAsync()) ([bool])
                    } elseif ($command -eq 'pause') {
                        $null = Await ($session.TryPauseAsync()) ([bool])
                    } elseif ($command -eq 'next') {
                        $null = Await ($session.TrySkipNextAsync()) ([bool])
                    } elseif ($command -eq 'restart') {
                        $moved = Await ($session.TryChangePlaybackPositionAsync(0)) ([bool])
                        if (-not $moved -and (Get-Position $session) -gt 3) {
                            $null = Await ($session.TrySkipPreviousAsync()) ([bool])
                        }
                    }
                    $playbackInfo = $session.GetPlaybackInfo()
                    $timeline = $session.GetTimelineProperties()
                    $position = [double](Get-Position $session)
                    $duration = [double]($timeline.EndTime - $timeline.StartTime).TotalSeconds
                    $properties = Await ($session.TryGetMediaPropertiesAsync()) ([Windows.Media.Control.GlobalSystemMediaTransportControlsSessionMediaProperties])
                    $title = $properties.Title
                    if ($properties.Artist) { $title = $properties.Artist + ' - ' + $title }
                    $title = $title -replace '[\\r\\n]+', ' '
                    [Console]::Out.WriteLine('OK|' + $playbackInfo.PlaybackStatus + '|' + $position.ToString($culture) + '|' + $duration.ToString($culture) + '|' + $playbackInfo.Controls.IsPlaybackPositionEnabled + '|' + $title)
                } catch {
                    [Console]::Out.WriteLine('ERROR|' + ($_.Exception.Message -replace '[\\r\\n]+', ' '))
                }
            }
            """;

    // Private constructor for Singleton pattern
    private WindowsMediaPlayer() {
    }

    // Static method to get the Singleton instance
    public static WindowsMediaPlayer getInstance() {
        if (instance == null) {
            instance = new WindowsMediaPlayer();
        }
        return instance;
    }

    //Starts PowerShell in the background, so the first song of a run doesn't wait for it
    public void warmUp() {
        if (MediaPlayerProcesses.isWindows()) {
            executor.execute(this::ensurePowerShellIsRunning);
        }
    }

    public void startPlayback() {
        resetPlaybackInfo();  // Reset before starting new playback
        sendCommandAndWait("play"); //Waits, so the song length is known when AudioManager predicts the end of the level
        hasStartedMusic = true;
        isPlaying = true;
    }

    public void stopPlayback() {
        sendCommandAsync("pause");
    }

    public void resumePlayback() {
        sendCommandAsync("play");
    }

    // Jumps to the start of the song. If Spotify doesn't allow that, "previous track" restarts it instead
    public void setPlaybackPositionTo0() {
        sendCommandAsync("restart");
        currentSeconds = 0;
    }

    public void synchronizePlaybackInfo() {
        if (!hasStartedMusic) {
            return;
        }

        String response = sendCommandAndWait("status");
        if (response == null || !response.startsWith("OK|") || totalSeconds <= 0) {
            System.err.println("Failed to get playback info from Spotify. Keeping previous values.");
            return;
        }

        // Only stop and skip if we are certain playback has finished
        if (currentSeconds >= 0 && currentSeconds < totalSeconds - 2) {  // Increased buffer to avoid premature stopping
            isPlaying = true;
        } else if (currentSeconds >= totalSeconds - 2) {  // Song has ended
            isPlaying = false;
        }
    }

    public void goToNextSong() {
        sendCommandAsync("next");
    }

    public void resetPlaybackInfo() {
        hasStartedMusic = false;
        isPlaying = false;
    }

    public boolean hasStartedMusic() {
        return hasStartedMusic;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public double getCurrentSeconds() {
        return currentSeconds;
    }

    public void resetCurrentSeconds() {
        currentSeconds = 0;
    }

    public double getTotalSeconds() {
        return totalSeconds;
    }

    // Asks Spotify what it is playing, to check that it receives our commands. Blocks for up to 14 seconds the first
    // time (PowerShell has to start), so call it off the game loop
    public MediaPlayerConnectionResult testConnection() {
        if (!MediaPlayerProcesses.isWindows()) {
            return new MediaPlayerConnectionResult(false, "Controlling Spotify this way only works on Windows");
        }

        String response = sendCommandAndWait("status");
        if (response == null) {
            return new MediaPlayerConnectionResult(false, "Could not reach the Windows media controls: " + lastError);
        }
        if (response.equals("NO_SESSION")) {
            if (!MediaPlayerProcesses.isRunning("\\Spotify.exe")) {
                return new MediaPlayerConnectionResult(false, "Spotify is not running. Open the Spotify app and start a playlist");
            }
            return new MediaPlayerConnectionResult(false, "Spotify is open, but Windows doesn't list it as playing media yet. Play a song in Spotify, then test again");
        }
        if (response.startsWith("ERROR|")) {
            return new MediaPlayerConnectionResult(false, "The Windows media controls gave an error: " + response.substring("ERROR|".length()));
        }
        if (!response.startsWith("OK|")) {
            return new MediaPlayerConnectionResult(false, "Unexpected answer from PowerShell: " + response);
        }

        String state = String.format("%s, %s (%.0f of %.0f seconds)", playbackStatus,
                trackTitle.isEmpty() ? "no song selected" : trackTitle, currentSeconds, totalSeconds);
        if (totalSeconds <= 0) {
            return new MediaPlayerConnectionResult(false, "Spotify answers (" + state + ") but doesn't report the song length, so levels can't end with the song");
        }

        String message = "Connected to Spotify: " + state;
        if (!canChangePosition) {
            message += ". Spotify doesn't allow jumping to a position, so songs are restarted with 'previous track'";
        }
        return new MediaPlayerConnectionResult(true, message);
    }

    private void sendCommandAsync(String command) {
        executor.execute(() -> sendCommand(command));
    }

    // Sends the command after the ones already waiting and returns the answer, or null when there was none in time
    private String sendCommandAndWait(String command) {
        try {
            return executor.submit(() -> sendCommand(command))
                    .get(STARTUP_TIMEOUT_MILLIS + COMMAND_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            lastError = "No answer in time";
            return null;
        }
    }

    // Runs on the executor thread only
    private String sendCommand(String command) {
        if (!ensurePowerShellIsRunning()) {
            return null;
        }
        try {
            commandWriter.write(command);
            commandWriter.newLine();
            commandWriter.flush();

            String response = readLine(COMMAND_TIMEOUT_MILLIS);
            if (response == null) {
                lastError = "PowerShell did not answer '" + command + "' within " + COMMAND_TIMEOUT_MILLIS + " ms";
                stopPowerShell(); //A fresh one starts with the next command
                return null;
            }
            if (response.startsWith("OK|")) {
                readStatus(response);
            }
            return response;
        } catch (IOException e) {
            lastError = e.getMessage();
            stopPowerShell();
            return null;
        }
    }

    private void readStatus(String response) {
        String[] parts = response.split("\\|", 6);
        if (parts.length < 6) {
            return;
        }
        try {
            currentSeconds = Double.parseDouble(parts[2]);
            totalSeconds = Double.parseDouble(parts[3]);
        } catch (NumberFormatException e) {
            return;
        }
        playbackStatus = parts[1];
        canChangePosition = Boolean.parseBoolean(parts[4]);
        trackTitle = parts[5];
    }

    private boolean ensurePowerShellIsRunning() {
        if (powerShell != null && powerShell.isAlive()) {
            return true;
        }
        if (!MediaPlayerProcesses.isWindows()) {
            lastError = "Not running on Windows";
            return false;
        }

        try {
            // Windows PowerShell 5.1 (powershell.exe), not PowerShell 7: only 5.1 can load the Windows media classes
            String encodedScript = Base64.getEncoder().encodeToString(POWERSHELL_SCRIPT.getBytes(StandardCharsets.UTF_16LE));
            ProcessBuilder pb = new ProcessBuilder("powershell.exe", "-NoProfile", "-NonInteractive", "-EncodedCommand", encodedScript);
            pb.redirectError(ProcessBuilder.Redirect.DISCARD); //The script reports its own errors on stdout
            powerShell = pb.start();
            commandWriter = new BufferedWriter(new OutputStreamWriter(powerShell.getOutputStream(), StandardCharsets.UTF_8));
            responseReader = new BufferedReader(new InputStreamReader(powerShell.getInputStream(), StandardCharsets.UTF_8));

            String firstLine = readLine(STARTUP_TIMEOUT_MILLIS);
            if (!"READY".equals(firstLine)) {
                lastError = firstLine == null ? "PowerShell did not start within " + STARTUP_TIMEOUT_MILLIS + " ms" : firstLine;
                stopPowerShell();
                return false;
            }
            return true;
        } catch (IOException e) {
            lastError = "Could not start PowerShell: " + e.getMessage();
            stopPowerShell();
            return false;
        }
    }

    // Reads one line, or returns null when none arrives before the timeout or PowerShell stopped
    private String readLine(long timeoutMillis) throws IOException {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < deadline) {
            if (responseReader.ready()) {
                String line = responseReader.readLine();
                return line == null ? null : line.replace("﻿", "").trim();
            }
            if (!powerShell.isAlive()) {
                return null;
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return null;
    }

    private void stopPowerShell() {
        if (powerShell != null) {
            powerShell.destroyForcibly();
            powerShell = null;
        }
    }
}
