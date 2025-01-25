package net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers;

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

        String script = "tell application \"Music\"\n"
                + "play\n"
                + "set trackDuration to duration of current track\n"
                + "repeat while player position < trackDuration\n"
                + "delay 1\n"
                + "end repeat\n"
                + "pause\n"  // Ensure playback is paused immediately after the song ends
                + "end tell";

        executeAppleScriptAsync(script);
        hasStartedMusic = true;
        isPlaying = true;  // Mark as playing
        totalSeconds = getTotalSecondsInPlayback();
    }

    // Method to stop playback in Apple Music (asynchronous)
    public void stopPlayback () {
        executeAppleScriptAsync("tell application \"Music\" to pause");
    }

    public void resumePlayback () {
        executeAppleScriptAsync("tell application \"Music\" to play");
    }

    // Method to get the current playback position (in seconds)
    public double getCurrentSecondsInPlayback () {
        String script = "tell application \"Music\"\n"
                + "return player position\n"
                + "end tell";
        return executeAppleScriptWithDoubleResult(script);
    }

    // Method to get the total duration of the currently playing track (in seconds)
    public double getTotalSecondsInPlayback() {
        return executeAppleScriptWithDoubleResult(
                "tell application \"Music\" to return duration of current track");
    }


    // Set the current playback position in seconds (asynchronous)
    public void setPlaybackPosition (double seconds) {
        String script = String.format("tell application \"Music\" to set player position to 0");
        executeAppleScriptAsync(script);
    }

    public void synchronizePlaybackInfo() {
        if (!hasStartedMusic) {
            return;
        }

        // Fetch and update playback info
        currentSeconds = getCurrentSecondsInPlayback();
        if (currentSeconds >= 0 && currentSeconds < totalSeconds - 2) {
            isPlaying = true;
        } else {
            isPlaying = false;
            stopPlayback();
            goToNextSong();
            setPlaybackPosition(0);
        }
    }





    // Method to skip to the next song in Apple Music
    public void goToNextSong () {
        executeAppleScriptAsync("tell application \"Music\" to next track");
    }

    // Method to reset playback state
    public void resetPlaybackInfo () {
        hasStartedMusic = false;  // Reset to indicate no song has started yet
        isPlaying = false;        // Reset playing status
        System.out.println("Playback info reset.");
    }

    // Method to check if the music has started
    public boolean hasStartedMusic () {
        return hasStartedMusic;
    }

    // Method to check if music is currently playing
    public boolean isPlaying () {
        return isPlaying;
    }

    private void executeAppleScriptAsync(String script) {
        executor.submit(() -> {
            Process process = null;
            try {
                ProcessBuilder pb = new ProcessBuilder("osascript", "-e", script);
                process = pb.start();

                // Wait for the process to complete
                process.waitFor();
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
    private double executeAppleScriptWithDoubleResult (String script) {
        double result = -1;
        Process process = null;
        try {
            ProcessBuilder pb = new ProcessBuilder("osascript", "-e", script);
            process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                if ((line = reader.readLine()) != null) {
                    result = Double.parseDouble(line.trim());
                }
            }
            process.waitFor();  // Ensure the process finishes before returning the result
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();  // Clean up the process
            }
        }
        return result;
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

