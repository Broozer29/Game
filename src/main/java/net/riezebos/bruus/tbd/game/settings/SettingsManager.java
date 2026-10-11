package net.riezebos.bruus.tbd.game.settings;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class SettingsManager {

    private static SettingsManager instance = new SettingsManager();
    private static final String SETTINGS_FILE_PATH = "settings.json";
    //Read by the controller thread and the game loop as well as the one that loads them
    private volatile Settings settings; //What the game uses right now, including changes that are not saved yet
    private volatile Settings savedSettings; //What settings.json holds

    private SettingsManager() {
    }

    public static SettingsManager getInstance() {
        return instance;
    }

    //The settings in use, loaded from the file the first time they are asked for
    public Settings getSettings() {
        if (settings == null) {
            loadSettings();
        }
        return settings;
    }

    public Settings getSavedSettings() {
        if (savedSettings == null) {
            loadSettings();
        }
        return savedSettings;
    }

    //A missing file gives the defaults without writing one; an unreadable file gives the defaults and a log line
    private synchronized void loadSettings() {
        if (settings != null) {
            return;
        }
        Settings loaded = null;
        File file = new File(SETTINGS_FILE_PATH);
        if (file.exists()) {
            try {
                loaded = new ObjectMapper().readValue(file, Settings.class);
                if (loaded == null) {
                    System.out.println("Could not read " + SETTINGS_FILE_PATH + ": the file is empty, using the default settings");
                }
            } catch (IOException e) {
                System.out.println("Could not read " + SETTINGS_FILE_PATH + ", using the default settings: " + e.getMessage());
            }
        }
        if (loaded == null) {
            loaded = new Settings();
        }
        //The setters clamp, but Jackson sets the fields through them, so a value outside 0-100 is already clamped here
        //Invalid bindings fall back to their defaults
        loaded.getInputBindings().repair();
        this.savedSettings = loaded.copyOf();
        this.settings = loaded;
    }

    //Writes the settings in use to settings.json; the saved copy only changes when the write worked
    public void saveSettings() {
        try {
            new ObjectMapper().writeValue(new File(SETTINGS_FILE_PATH), getSettings());
            this.savedSettings = getSettings().copyOf();
        } catch (IOException e) {
            System.out.println("Could not write " + SETTINGS_FILE_PATH + ": " + e.getMessage());
        }
    }

    //Puts the saved values back in use
    public void discardChanges() {
        this.settings = getSavedSettings().copyOf();
    }

    public boolean hasUnsavedChanges() {
        return !getSettings().hasSameValuesAs(getSavedSettings());
    }
}
