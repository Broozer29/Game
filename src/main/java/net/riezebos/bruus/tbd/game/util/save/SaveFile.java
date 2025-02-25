package net.riezebos.bruus.tbd.game.util.save;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class SaveFile {

    private PlayerClass playerclass = PlayerStats.getInstance().getPlayerClass();
    private int playerLevel = PlayerStats.getInstance().getCurrentLevel();
    private float playerXP = PlayerStats.getInstance().getCurrentXP();

    private Map<ItemEnums, Integer> items;

    private float difficultyCoefficient = GameStateInfo.getInstance().getDifficultyCoefficient();
    private int stagesCompleted;
    private int bossesDefeated;
    private long gameTicksExecuted;
    private float money;

    public SaveFile() {
        playerclass = PlayerStats.getInstance().getPlayerClass();
        playerLevel = PlayerStats.getInstance().getCurrentLevel();
        playerXP = PlayerStats.getInstance().getCurrentXP();
        stagesCompleted = GameStateInfo.getInstance().getStagesCompleted();
        bossesDefeated = GameStateInfo.getInstance().getBossesDefeated();
        gameTicksExecuted = GameStateInfo.getInstance().getGameTicksExecuted();
        money = PlayerInventory.getInstance().getCashMoney();

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
}
