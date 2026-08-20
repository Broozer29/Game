package net.riezebos.bruus.tbd.discordconnector;

import fun.crashsystem.jdrpc.DiscordIPC;
import fun.crashsystem.jdrpc.activity.Activity;
import fun.crashsystem.jdrpc.activity.ActivityType;

public final class DiscordConnector implements AutoCloseable {

    private static final long APPLICATION_ID = 1538958017670680587L;
    public static DiscordConnector instance = new DiscordConnector();
    private DiscordConnector() {
        connect();
    }

    public static DiscordConnector getInstance() {
        return instance;
    }


    private DiscordIPC discord;
    private boolean connected;

    public boolean connect() {
        return false;

//        if (connected) {
//            return true;
//        }
//
//        try {
//            discord = DiscordIPC.create(APPLICATION_ID);
//            discord.connect();
//
//            connected = discord.isConnected();
//
//            if (connected) {
//                System.out.println("Connected to Discord Rich Presence.");
//            }
//
//            return connected;
//
//        } catch (Exception e) {
//            System.out.println(
//                    "Discord unavailable; continuing without Rich Presence."
//            );
//
//            connected = false;
//            discord = null;
//
//            return false;
//        }
    }

    public void setStatus(String details, String state) {
        if (!isConnected()) {
            return;
        }

        Activity activity = new Activity.Builder()
                .setType(ActivityType.PLAYING)
                .setDetails(details)
                .setState(state)
                .setLargeImage("game_logo", "My Game")
                .build();

        try {
            discord.setActivity(activity);
        } catch (Exception e) {
            connected = false;
        }
    }


    public boolean isConnected() {
        return connected
                && discord != null
                && discord.isConnected();
    }

    /**
     * Clears the presence and shuts down the IPC connection.
     */
    @Override
    public void close() {
        if (discord == null) {
            return;
        }

        try {
            if (discord.isConnected()) {
                discord.clearActivity();
            }
        } catch (Exception ignored) {
        }

        try {
            discord.close();
        } catch (Exception ignored) {
        }

        connected = false;
        discord = null;
    }
}