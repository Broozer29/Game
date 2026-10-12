package net.riezebos.bruus.tbd.game.gamestate.save;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.gamestate.shop.ShopContents;
import net.riezebos.bruus.tbd.game.gamestate.shop.ShopSlot;
import net.riezebos.bruus.tbd.game.level.LevelManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

            // The shop is read apart from the rest, so a shop that can't be read (or names an item that no longer exists) doesn't stop the run from loading
            JsonNode rootNode = objectMapper.readTree(new File(SAVE_FILE_PATH));
            if (!(rootNode instanceof ObjectNode root)) {
                throw new IOException("Save file is empty or not a JSON object");
            }
            JsonNode shopNode = root.remove("shopContents");
            SaveFile saveFile = objectMapper.treeToValue(root, SaveFile.class);
            PlayerStats.getInstance().loadInSaveFile(saveFile);
            GameState.getInstance().loadInSaveFile(saveFile);
            BoonManager.getInstance().loadInSaveFile(saveFile);
            LevelManager.getInstance().loadInSaveFile(saveFile);
            GameStatsTracker.getInstance().loadInSaveFile(saveFile);

            ShopManager.getInstance().setLastLevelDifficultyScore(saveFile.getLastKnownDifficultyScore());
            ShopManager.getInstance().setLastLevelDifficulty(saveFile.getLastKnownLevelDifficulty());
            ShopManager.getInstance().setLastMiniBossConfig(saveFile.getLastKnownMiniBossConfig());
            ShopManager.getInstance().setContents(readShopContents(objectMapper, shopNode));


            System.out.println("Game successfully loaded!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading save file!");
        }
    }

    /**
     * Reads the shop from its part of the save file.
     * @return the shop, or null when it is missing or can't be read, in which case a fresh shop is rolled.
     */
    private ShopContents readShopContents(ObjectMapper objectMapper, JsonNode shopNode) {
        if (shopNode == null || shopNode.isNull()) {
            return null;
        }

        ShopContents contents;
        try {
            contents = objectMapper.treeToValue(shopNode, ShopContents.class);
        } catch (Exception e) {
            System.err.println("Shop in the save file can't be read, a new shop is rolled (" + e.getClass().getSimpleName() + "): " + e.getMessage());
            return null;
        }

        if (contents == null || contents.getRows() == null || contents.getRows().size() != ShopContents.ROW_COUNT) {
            System.err.println("Shop in the save file is rejected, a new shop is rolled: it does not have " + ShopContents.ROW_COUNT + " rows");
            return null;
        }
        for (List<ShopSlot> row : contents.getRows()) {
            if (row == null || row.size() != ShopContents.SLOTS_PER_ROW) {
                System.err.println("Shop in the save file is rejected, a new shop is rolled: a row does not have " + ShopContents.SLOTS_PER_ROW + " slots");
                return null;
            }
            for (ShopSlot slot : row) {
                if (slot == null || slot.getItem() == null || slot.getRarity() == null || slot.getStatus() == null) {
                    System.err.println("Shop in the save file is rejected, a new shop is rolled: a slot is missing its item, rarity or status");
                    return null;
                }
            }
        }
        return contents;
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
