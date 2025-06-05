package net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers.spotify;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class SpotifyWebAPIClient {

    // Spotify API credentials (configured during setup)
    private String clientId = "YOUR_CLIENT_ID";
    private String clientSecret = "YOUR_CLIENT_SECRET";
    private String refreshToken = "YOUR_REFRESH_TOKEN"; // Setup and store once

    // Access token that is refreshed each session
    private String accessToken = null;

    /**
     * Constructor
     * - Loads access token automatically by refreshing on application startup.
     */
    public SpotifyWebAPIClient() {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalStateException("Refresh token is not configured. Please set it up first.");
        }

        refreshAccessToken(); // Automatically refresh access token on startup
    }

    /**
     * Refreshes the access token using the stored refresh token.
     */
    public void refreshAccessToken() {
        try {
            URL url = new URL("https://accounts.spotify.com/api/token");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            String body = "grant_type=refresh_token" +
                    "&refresh_token=" + refreshToken +
                    "&client_id=" + clientId +
                    "&client_secret=" + clientSecret;

            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Parse and store the access token from the response
                String response = new BufferedReader(new InputStreamReader(connection.getInputStream()))
                        .lines()
                        .reduce("", (accumulator, actual) -> accumulator + actual);

                JSONObject responseObject = new JSONObject(response);
                accessToken = responseObject.getString("access_token");

                System.out.println("Access token refreshed successfully!");
            } else {
                System.err.println("Failed to refresh access token. HTTP response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetch the list of devices and return the currently active device ID.
     * @return The device ID of the active device, or null if no active device is found.
     */
    public String getDeviceId() {
        try {
            URL url = new URL("https://api.spotify.com/v1/me/player/devices");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                String response = new BufferedReader(new InputStreamReader(connection.getInputStream()))
                        .lines()
                        .reduce("", (accumulator, actual) -> accumulator + actual);

                JSONObject responseObject = new JSONObject(response);

                // Iterate devices to find an active one
                for (Object deviceObj : responseObject.getJSONArray("devices")) {
                    JSONObject device = (JSONObject) deviceObj;
                    if (device.getBoolean("is_active")) {
                        System.out.println("Active device found: " + device.getString("name"));
                        return device.getString("id");
                    }
                }

                System.out.println("No active device found.");
            } else {
                System.err.println("Failed to fetch devices. HTTP response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Start playback on the given device.
     * @param deviceId The ID of the device where playback should start.
     */
    public void startPlayback(String deviceId) {
        try {
            URL url = new URL("https://api.spotify.com/v1/me/player/play?device_id=" + deviceId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == 204) {
                System.out.println("Playback started successfully!");
            } else {
                System.err.println("Failed to start playback. HTTP response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Example: Test the connection and start playback
     */
    public void testConnectAndPlay() {
        // Fetch the active device ID
        String deviceId = getDeviceId();
        if (deviceId != null) {
            // Start playback on the active device
            startPlayback(deviceId);
        } else {
            System.err.println("Unable to find an active Spotify device to start playback.");
        }
    }

    public static void main(String[] args) {
        // Initialize the client (automatically refreshes token on startup)
        SpotifyWebAPIClient client = new SpotifyWebAPIClient();

        // Test connectivity and playback
        client.testConnectAndPlay();
    }
}