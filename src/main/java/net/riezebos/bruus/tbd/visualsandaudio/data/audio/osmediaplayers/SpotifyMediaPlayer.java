package net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers;

import java.util.concurrent.*;

public class SpotifyMediaPlayer {

    // Singleton instance
    private static SpotifyMediaPlayer instance = null;

    // ExecutorService for asynchronous tasks
    private final ExecutorService executor = Executors.newCachedThreadPool();

    // Cached playback data
    private boolean hasStartedMusic = false;
    private boolean isPlaying = false;
    private double currentSeconds = 0; // Playback position
    private double totalSeconds = 0;   // Total track duration

    private String clientId = "CLIENT_ID"; // Replace this with your client ID
    private String clientSecret = "CLIENT_SECRET"; // Replace this with your client secret
    private String spotifyAuthToken;  // Spotify API token (you need to set and refresh this)
    private String deviceId;          // Spotify device ID, represents the machine currently playing audio

    // Private constructor for Singleton pattern
    private SpotifyMediaPlayer() {
        //you can set clientId, clientSecret,deviceId and spotifyAuthToken here hardcoded like below, just replace the strings with the actual values
        clientId = "CLIENT_ID";
        clientSecret = "CLIENT_SECRET";
        spotifyAuthToken = "SPOTIFY_AUTH_TOKEN";
        deviceId = "DEVICE_ID";
    }

    // Static method to get the Singleton instance
    public static SpotifyMediaPlayer getInstance() {
        if (instance == null) {
            instance = new SpotifyMediaPlayer();
        }
        return instance;
    }

    /**
     * Start playback in Spotify.
     * Requires `deviceId` to be set.
     */
    public void startPlayback() {
        if (deviceId == null || deviceId.isEmpty()) {
            System.err.println("Device ID is not set. Please set the deviceId before calling startPlayback.");
            return;
        }
        executeSpotifyCommandAsync("me/player/play?device_id=" + deviceId);
        isPlaying = true;
        hasStartedMusic = true;
    }

    /**
     * Stop playback in Spotify.
     * Requires `deviceId` to be set.
     */
    public void stopPlayback() {
        if (deviceId == null || deviceId.isEmpty()) {
            System.err.println("Device ID is not set. Please set the deviceId before calling stopPlayback.");
            return;
        }
        executeSpotifyCommandAsync("me/player/pause?device_id=" + deviceId);
        isPlaying = false;
    }

    /**
     * Resume playback in Spotify.
     * Requires `deviceId` to be set.
     */
    public void resumePlayback() {
        if (deviceId == null || deviceId.isEmpty()) {
            System.err.println("Device ID is not set. Please set the deviceId before calling resumePlayback.");
            return;
        }
        executeSpotifyCommandAsync("me/player/play?device_id=" + deviceId);
        isPlaying = true;
    }

    /**
     * Get the current playback position (in seconds).
     *
     * @return Current playback position.
     */
    public double getCurrentSecondsInPlayback() {
        return currentSeconds;
    }

    /**
     * Get the total duration of the currently playing track (in seconds).
     *
     * @return Total track duration.
     */
    public double getTotalSecondsInPlayback() {
        return totalSeconds;
    }

    /**
     * Set the current playback position to 0 seconds.
     * Uses the stored `deviceId` to identify where to perform the operation.
     */
    public void setPlaybackPositionTo0() {
        if (deviceId == null || deviceId.isEmpty()) {
            System.err.println("Device ID is not set. Please set the deviceId before calling setPlaybackPositionTo0.");
            return;
        }
        executeSpotifyCommandAsync("me/player/seek?position_ms=0&device_id=" + deviceId);
        currentSeconds = 0;
    }

    /**
     * Skip to the next song in Spotify.
     * Requires `deviceId` to be set.
     */
    public void goToNextSong() {
        if (deviceId == null || deviceId.isEmpty()) {
            System.err.println("Device ID is not set. Please set the deviceId before calling goToNextSong.");
            return;
        }
        executeSpotifyCommandAsync("me/player/next?device_id=" + deviceId);
    }

    /**
     * Reset playback information.
     */
    public void resetPlaybackInfo() {
        isPlaying = false;
        hasStartedMusic = false;
    }

    /**
     * Check if music has started.
     *
     * @return True if music has started, false otherwise.
     */
    public boolean hasStartedMusic() {
        return hasStartedMusic;
    }

    /**
     * Check if music is currently playing.
     *
     * @return True if music is playing, false otherwise.
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Set the device ID of the connected playback device.
     * This must be set before calling playback-related methods.
     *
     * @param deviceId The ID of the Spotify device to use for playback.
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        System.out.println("Device ID has been set to: " + deviceId);
    }

    /**
     * Retrieve the current device ID.
     *
     * @return The current device ID set for this player.
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Synchronize the playback information from Spotify.
     */
    public void synchronizePlaybackInfo() {
        if (!hasStartedMusic) {
            return; //We havent started playback, no point in trying to read info from it
        }

        executor.execute(() -> {
            try {

                // Make a call to Spotify API to fetch playback information
                executeSpotifyCommandAsync("haal de huidige positie in playback op");

                // Only stop and skip if we are certain playback has finished
                if (currentSeconds >= 0 && currentSeconds < totalSeconds - 2) {  // Increased buffer to avoid premature stopping
                    isPlaying = true;
                } else if (currentSeconds >= totalSeconds - 2) {  // Song has ended
                    isPlaying = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Helper method to execute a Spotify command asynchronously with a parameter.
     *
     * @param command The command to execute.
     */
    private void executeSpotifyCommandAsync(String command) {
        executor.execute(() -> {
            try {
                //voer het commando uit
                //Om het commando uit te voeren moet een HTTP request naar Spotify's Web API gemaakt worden
                //Hier heb je de clientId, clientSecret & authentication token voor nodig.
                //Voor het starten/stoppen van playback is de deviceId nodig!
                if (command.equals("haal de huidige positie in playback op")) {
                    currentSeconds = 0; //vervang 0 met de daadwerkelijke waarde die terugkomt
                } else {
                    //voer het commando gewoon uit, hier moet start/stop/resetPlayback uitgevoerd worden
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public double getCurrentSeconds() {
        return currentSeconds;
    }

    public double getTotalSeconds() {
        return totalSeconds;
    }
}