package net.riezebos.bruus.tbd.game.playerprofile.profile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class PlayerProfileManager {

    private static PlayerProfileManager instance = new PlayerProfileManager();
    private PlayerProfile loadedProfile;
    private static final String SAVE_FILE_PATH = "playerprofile.json";

    private PlayerProfileManager(){
        loadProfile();
    }

    public static PlayerProfileManager getInstance(){
        return instance;
    }


    /**
     * Exports the current game state into a JSON save file.
     */
    public void exportCurrentProfile() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File(SAVE_FILE_PATH), this.loadedProfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the game state from the save file.
     */
    public void loadProfile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (!doesSaveFileExist()) {
                loadedProfile = new PlayerProfile();
                return;
            }

            PlayerProfile saveFile = objectMapper.readValue(new File(SAVE_FILE_PATH), PlayerProfile.class);
            this.loadedProfile = saveFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the save file exists.
     * @return true if the save file exists, false otherwise.
     */
    public boolean doesSaveFileExist() {
        File file = new File(SAVE_FILE_PATH);
        return file.exists();
    }


    public PlayerProfile getLoadedProfile() {
        return loadedProfile;
    }

}
