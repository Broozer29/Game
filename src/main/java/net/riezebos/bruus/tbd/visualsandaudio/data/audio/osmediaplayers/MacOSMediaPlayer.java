package net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers;

import net.riezebos.bruus.tbd.DevTestSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MacOSMediaPlayer {

    // Singleton instance
    private static MacOSMediaPlayer instance = null;

    // ExecutorService for running tasks asynchronously
    private final ExecutorService executor = Executors.newCachedThreadPool();

    // Cached values for playback information
    private boolean hasStartedMusic = false;
    private boolean isPlaying = false;
    private double currentSeconds = 0;
    private double totalSeconds = 0;

    // Private constructor for Singleton pattern
    private MacOSMediaPlayer() {
    }

    // Static method to get the Singleton instance
    public static MacOSMediaPlayer getInstance() {
        if (instance == null) {
            instance = new MacOSMediaPlayer();
        }
        return instance;
    }

    // Apple Music or Spotify, both desktop apps understand the same AppleScript commands used below
    private String getAppName() {
        return DevTestSettings.macOSPlayerUsesSpotify ? "Spotify" : "Music";
    }

    public void startPlayback() {
        resetPlaybackInfo();  // Reset before starting new playback

        String script = "tell application \"" + getAppName() + "\"\n"
                + "play\n"
                + "end tell";


        executeAppleScriptAsync(script);
        hasStartedMusic = true;
        isPlaying = true;
        totalSeconds = getTotalSecondsInPlayback();
    }


    // Method to stop playback in Apple Music or Spotify (asynchronous)
    public void stopPlayback() {
        executeAppleScriptAsync("tell application \"" + getAppName() + "\" to pause");
    }

    public void resumePlayback() {
        executeAppleScriptAsync("tell application \"" + getAppName() + "\" to play");
    }

    // Method to get the current playback position (in seconds)
    public double getCurrentSecondsInPlayback() {
        String script = "tell application \"" + getAppName() + "\"\n"
                + "return player position\n"
                + "end tell";
        return executeAppleScriptWithDoubleResult(script);
    }

    // Method to get the total duration of the currently playing track (in seconds)
    public double getTotalSecondsInPlayback() {
        double duration = executeAppleScriptWithDoubleResult(
                "tell application \"" + getAppName() + "\" to return duration of current track");
        if (DevTestSettings.macOSPlayerUsesSpotify && duration > 0) {
            duration = duration / 1000; // Spotify gives the duration in milliseconds, Apple Music in seconds
        }
        return duration;
    }


    // Set the current playback position in seconds (asynchronous)
    public void setPlaybackPositionTo0() {
        String script = "tell application \"" + getAppName() + "\" to set player position to 0";
        executeAppleScriptAsync(script);
        currentSeconds = 0;
        totalSeconds = 0;
        synchronizePlaybackInfo();
    }

    public void synchronizePlaybackInfo() {
        if (!hasStartedMusic) {
            return;
        }

        // Fetch and update playback info
        double newCurrentSeconds = getCurrentSecondsInPlayback();

        // Ignore invalid results (-1 indicates an error)
        if (newCurrentSeconds < 0) {
            System.err.println("Failed to get playback position. Keeping previous value.");
            return; // Skip this update and keep the last known valid position
        }

        double total = getTotalSecondsInPlayback();
        if(total < 0) {
            System.err.println("Failed to get total track length. Keeping previous value.");
            return; // Skip this update and keep the last known valid position
        }

        currentSeconds = newCurrentSeconds;
        totalSeconds = total;

        // Only stop and skip if we are certain playback has finished
        if (currentSeconds >= 0 && currentSeconds < totalSeconds - 2) {  // Increased buffer to avoid premature stopping
            isPlaying = true;
        } else if (currentSeconds >= totalSeconds - 2) {  // Song has ended
            isPlaying = false;
        }
    }


    // Method to skip to the next song in Apple Music or Spotify
    public void goToNextSong() {
        executeAppleScriptAsync("tell application \"" + getAppName() + "\" to next track");
    }

    // Method to reset playback state
    public void resetPlaybackInfo() {
        hasStartedMusic = false;  // Reset to indicate no song has started yet
        isPlaying = false;        // Reset playing status
        System.out.println("Playback info reset.");
    }

    // Method to check if the music has started
    public boolean hasStartedMusic() {
        return hasStartedMusic;
    }

    // Method to check if music is currently playing
    public boolean isPlaying() {
        return isPlaying;
    }

    private void executeAppleScriptAsync(String script) {
        executor.submit(() -> {
            Process process = null;
            try {
                ProcessBuilder pb = new ProcessBuilder("osascript", "-e", script);
                process = pb.start();

                // Wait for the process to complete
                boolean finished = process.waitFor(2000, TimeUnit.MILLISECONDS);
                if (!finished) {
                    process.destroyForcibly();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (process != null) {
                    process.destroy();
                }
            }
        });
    }


    // Helper method to execute AppleScript and get a double result (synchronous)
    private double executeAppleScriptWithDoubleResult(String script) {
        double result = -1;
        Process process = null;
        try {
            ProcessBuilder pb = new ProcessBuilder("osascript", "-e", script);
            process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                if ((line = reader.readLine()) != null) {
                    result = Double.parseDouble(line.trim().replace(',', '.')); // Some locales write a decimal comma
                }
            } catch (IOException e) {
                return -1;
            }
            boolean finished = process.waitFor(2000, TimeUnit.MILLISECONDS);
            if (!finished) {
                process.destroyForcibly();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();  // Clean up the process
            }
        }
        return result;
    }


    // Asks Apple Music or Spotify what it is playing, to check that it receives our commands. Blocks for up to 3 seconds
    public MediaPlayerConnectionResult testConnection() {
        String appName = getAppName();
        if (!MediaPlayerProcesses.isMacOS()) {
            return new MediaPlayerConnectionResult(false, "Controlling " + appName + " this way only works on macOS");
        }
        // Checked first because any AppleScript command would open the app
        if (!MediaPlayerProcesses.isRunning("/" + appName + ".app/Contents/MacOS/" + appName)) {
            return new MediaPlayerConnectionResult(false, appName + " is not running. Open it and start a playlist");
        }

        String script = "tell application \"" + appName + "\"\n"
                + "set trackName to \"\"\n"
                + "try\n"
                + "set trackName to (artist of current track) & \" - \" & (name of current track)\n"
                + "end try\n"
                + "return (player state as string) & \"|\" & trackName\n"
                + "end tell";
        MediaPlayerProcesses.Output output = MediaPlayerProcesses.run(3000, "osascript", "-e", script);
        if (!output.succeeded()) {
            if (output.stderr().contains("-1743")) {
                return new MediaPlayerConnectionResult(false, "macOS doesn't allow the game to control " + appName
                        + ". Allow it under System Settings > Privacy & Security > Automation");
            }
            return new MediaPlayerConnectionResult(false, appName + " did not answer: " + output.stderr());
        }

        String[] parts = output.stdout().split("\\|", 2);
        String trackName = parts.length > 1 && !parts[1].isEmpty() ? parts[1] : "no song selected";
        return new MediaPlayerConnectionResult(true, "Connected to " + appName + ": " + parts[0] + ", " + trackName);
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

}

