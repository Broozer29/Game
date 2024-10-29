package net.riezebos.bruus.tbd.visuals.data.audio.osmediaplayers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class MacOSMediaPlayer {

    // Singleton instance
    private static MacOSMediaPlayer instance = null;

    // ExecutorService for running tasks asynchronously
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // Cached values for playback information
    private volatile double currentSecondsInPlayback = 0.0;
    private volatile double totalSecondsInPlayback = 0.0;
    private boolean isPolling = false;
    private boolean hasStartedMusic = false;
    private boolean isPlaying = false;
    private double currentSeconds = 0;
    private double totalSeconds = 0;

    // Scheduled polling task handle
    private ScheduledFuture<?> pollingTask;

    // Private constructor for Singleton pattern
    private MacOSMediaPlayer () {
    }

    // Static method to get the Singleton instance
    public static MacOSMediaPlayer getInstance () {
        if (instance == null) {
            instance = new MacOSMediaPlayer();
        }
        return instance;
    }

    public void startPlayback () {
        resetPlaybackInfo();  // Reset before starting new playback

        // AppleScript to play the current song and stop after the song finishes
        String script = "tell application \"Music\"\n"
                + "play\n"
                + "set trackDuration to duration of current track\n"
                + "repeat while player position < trackDuration\n"
                + "delay 1\n"
                + "end repeat\n"
                + "pause\n"  // Ensure playback is paused immediately after the song ends
                + "end tell";

        executeAppleScriptAsync(script);  // Execute the AppleScript
        hasStartedMusic = true;
        isPlaying = true;  // Mark as playing
    }

    // Method to stop playback in Apple Music (asynchronous)
    public void stopPlayback () {
        executeAppleScriptAsync("tell application \"Music\" to pause");
    }

    // Method to get the current playback position (in seconds)
    public double getCurrentSecondsInPlayback() {
        String script = "tell application \"Music\"\n"
                + "return player position\n"
                + "end tell";
        return executeAppleScriptWithDoubleResult(script);
    }

    // Method to get the total duration of the currently playing track (in seconds)
    public double getTotalSecondsInPlayback() {
        String script = "tell application \"Music\"\n"
                + "set myTrack to current track\n"
                + "return duration of myTrack\n"
                + "end tell";
        return executeAppleScriptWithDoubleResult(script);
    }


    // Set the current playback position in seconds (asynchronous)
    public void setPlaybackPosition (double seconds) {
        String script = String.format("tell application \"Music\" to set player position to 0");
        executeAppleScriptAsync(script);
    }


    // Method to start polling for playback info
    public void startPolling () {
        if (pollingTask == null || pollingTask.isCancelled() || pollingTask.isDone()) {
            // Schedule polling task to run every second
            pollingTask = scheduler.scheduleAtFixedRate(() -> {
                updatePlaybackInfo();
            }, 0, 500, TimeUnit.MILLISECONDS);
            System.out.println("Started polling Apple Music for playback info.");
        }
    }

    // Method to stop polling for playback info
    public void stopPolling () {
        if (pollingTask != null && !pollingTask.isCancelled()) {
            pollingTask.cancel(true);  // Interrupts the current polling task
            System.out.println("Stopped polling Apple Music for playback info.");
            this.isPolling = false;
            currentSecondsInPlayback = 0;
            totalSecondsInPlayback = 0;
        }
    }


// Polling method to check playback status and update the state
    private void updatePlaybackInfo() {
        currentSeconds = getCurrentSecondsInPlayback();
        totalSeconds = getTotalSecondsInPlayback();

        // If the song has started playing, mark it as started
        if (!hasStartedMusic && currentSeconds > 0) {
            hasStartedMusic = true;
            isPlaying = true;
            System.out.println("Music playback has started.");
        }

        // If the song is close to finishing or has finished, stop playback and update state
        if (currentSeconds >= totalSeconds - 2) {
            stopPlayback();
            stopPolling();
            goToNextSong();
            isPlaying = false;  // Music has finished
            System.out.println("Music playback has finished.");
        }
    }

    // Method to skip to the next song in Apple Music
    public void goToNextSong() {
        executeAppleScriptAsync("tell application \"Music\" to next track");
    }

    // Method to reset playback state
    public void resetPlaybackInfo() {
        stopPolling();  // Stop any existing polling task
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

    // Helper method to execute AppleScript asynchronously
    private void executeAppleScriptAsync(String script) {
        executor.submit(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder("osascript", "-e", script);
                pb.start();  // Fire & forget, no need for waitFor
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Helper method to execute AppleScript and get a double result (synchronous)
    private double executeAppleScriptWithDoubleResult(String script) {
        double result = -1;
        try {
            ProcessBuilder pb = new ProcessBuilder("osascript", "-e", script);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            if ((line = reader.readLine()) != null) {
                result = Double.parseDouble(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public boolean isPolling () {
        return isPolling;
    }

    public void setPlaying (boolean playing) {
        isPlaying = playing;
    }

    public double getCurrentSeconds () {
        return currentSeconds;
    }

    public void setCurrentSeconds (double currentSeconds) {
        this.currentSeconds = currentSeconds;
    }

    public double getTotalSeconds () {
        return totalSeconds;
    }

    public void setTotalSeconds (double totalSeconds) {
        this.totalSeconds = totalSeconds;
    }
}

