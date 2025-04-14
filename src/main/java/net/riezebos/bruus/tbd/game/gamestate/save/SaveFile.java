package net.riezebos.bruus.tbd.game.gamestate.save;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameMode;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonManager;

import java.util.HashMap;
import java.util.Map;

public class SaveFile {

    private PlayerClass playerclass;
    private int playerLevel;
    private float playerXP;
    private GameMode gameMode;

    private Map<ItemEnums, Integer> items;

    private float difficultyCoefficient = GameState.getInstance().getDifficultyCoefficient();
    private int stagesCompleted;
    private int bossesDefeated;
    private long gameTicksExecuted;
    private float money;

    private BoonEnums selectedUtilityBoon;
    private BoonEnums selectedOffenseBoon;
    private BoonEnums selectedDefenseBoon;


    public SaveFile() {
        gameMode = GameState.getInstance().getGameMode();
        playerclass = PlayerStats.getInstance().getPlayerClass();
        playerLevel = PlayerStats.getInstance().getCurrentLevel();
        playerXP = PlayerStats.getInstance().getCurrentXP();
        stagesCompleted = GameState.getInstance().getStagesCompleted();
        bossesDefeated = GameState.getInstance().getBossesDefeated();
        gameTicksExecuted = GameState.getInstance().getGameTicksExecuted();
        money = PlayerInventory.getInstance().getCashMoney();


        selectedUtilityBoon = BoonManager.getInstance().getUtilityBoon() != null ? BoonManager.getInstance().getUtilityBoon().getBoonEnum() : null;
        selectedDefenseBoon = BoonManager.getInstance().getDefensiveBoon() != null ? BoonManager.getInstance().getDefensiveBoon().getBoonEnum() : null;
        selectedOffenseBoon = BoonManager.getInstance().getOffensiveBoon() != null ? BoonManager.getInstance().getOffensiveBoon().getBoonEnum() : null;

        this.items = new HashMap<>();
        for (Map.Entry<ItemEnums, Item> entry : PlayerInventory.getInstance().getItems().entrySet()) {
            this.items.put(entry.getKey(), entry.getValue().getQuantity());
        }
    }


    public PlayerClass getPlayerclass() {
        return playerclass;
    }

    public void setPlayerclass(PlayerClass playerclass) {
        this.playerclass = playerclass;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public float getPlayerXP() {
        return playerXP;
    }

    public void setPlayerXP(float playerXP) {
        this.playerXP = playerXP;
    }


    public float getDifficultyCoefficient() {
        return difficultyCoefficient;
    }

    public void setDifficultyCoefficient(float difficultyCoefficient) {
        this.difficultyCoefficient = difficultyCoefficient;
    }

    public int getStagesCompleted() {
        return stagesCompleted;
    }

    public void setStagesCompleted(int stagesCompleted) {
        this.stagesCompleted = stagesCompleted;
    }

    public int getBossesDefeated() {
        return bossesDefeated;
    }

    public void setBossesDefeated(int bossesDefeated) {
        this.bossesDefeated = bossesDefeated;
    }

    public long getGameTicksExecuted() {
        return gameTicksExecuted;
    }

    public void setGameTicksExecuted(long gameTicksExecuted) {
        this.gameTicksExecuted = gameTicksExecuted;
    }

    public Map<ItemEnums, Integer> getItems() {
        return items;
    }

    public void setItems(Map<ItemEnums, Integer> items) {
        this.items = items;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public BoonEnums getSelectedUtilityBoon() {
        return selectedUtilityBoon;
    }

    public void setSelectedUtilityBoon(BoonEnums selectedUtilityBoon) {
        this.selectedUtilityBoon = selectedUtilityBoon;
    }

    public BoonEnums getSelectedOffenseBoon() {
        return selectedOffenseBoon;
    }

    public void setSelectedOffenseBoon(BoonEnums selectedOffenseBoon) {
        this.selectedOffenseBoon = selectedOffenseBoon;
    }

    public BoonEnums getSelectedDefenseBoon() {
        return selectedDefenseBoon;
    }

    public void setSelectedDefenseBoon(BoonEnums selectedDefenseBoon) {
        this.selectedDefenseBoon = selectedDefenseBoon;
    }

    public GameMode getGameModes() {
        return gameMode;
    }

    public void setGameModes(GameMode gameMode) {
        this.gameMode = gameMode;
    }
}
