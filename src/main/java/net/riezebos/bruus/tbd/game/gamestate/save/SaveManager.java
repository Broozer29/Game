package net.riezebos.bruus.tbd.game.gamestate.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonManager;

import java.io.File;
import java.io.IOException;

public class SaveManager {

    private static final SaveManager instance = new SaveManager();
    private static final String SAVE_FILE_PATH = "savefile.json"; // File name for saving

    private SaveManager() {
    }

    public static SaveManager getInstance() {
        return instance;
    }

    /**
     * Exports the current game state into a JSON save file.
     */
    public void exportCurrentSave() {
        ObjectMapper objectMapper = new ObjectMapper();
        SaveFile saveFile = new SaveFile();

        try {
            objectMapper.writeValue(new File(SAVE_FILE_PATH), saveFile);
            System.out.println("Game successfully saved to " + SAVE_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving game!");
        }
    }

    /**
     * Loads the game state from the save file.
     */
    public void loadSaveFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (!doesSaveFileExist()) {
                System.out.println("No save file found!");
                return;
            }

            SaveFile saveFile = objectMapper.readValue(new File(SAVE_FILE_PATH), SaveFile.class);
            PlayerStats.getInstance().loadInSaveFile(saveFile);
            GameState.getInstance().loadInSaveFile(saveFile);
            BoonManager.getInstance().loadInSaveFile(saveFile);
            System.out.println("Game successfully loaded!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading save file!");
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

    /**
     * Deletes the save file if it exists.
     */
    public void deleteSaveFile() {
        File file = new File(SAVE_FILE_PATH);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Save file deleted successfully.");
            } else {
                System.err.println("Failed to delete save file.");
            }
        } else {
            System.out.println("No save file found to delete.");
        }
    }
}
