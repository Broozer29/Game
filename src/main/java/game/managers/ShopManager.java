package game.managers;

import game.gameobjects.player.PlayerStats;
import game.items.PlayerInventory;
import game.level.enums.LevelDifficulty;
import game.level.enums.LevelLength;

public class ShopManager {

    private int amountOfFreeItems;
    private int rowsUnlocked = 1;
    private int rerollCost;

    private int lastLevelDifficultyCoeff;
    private LevelLength lastLevelLength;
    private LevelDifficulty lastLevelDifficulty;

    private static ShopManager instance = new ShopManager();

    private ShopManager () {

    }

    public static ShopManager getInstance () {
        return instance;
    }

    public int getAmountOfFreeItems () {
        return amountOfFreeItems;
    }

    public void setAmountOfFreeItems (int amountOfFreeItems) {
        this.amountOfFreeItems = amountOfFreeItems;
    }

    public int getRowsUnlocked () {
        return rowsUnlocked;
    }

    public void setRowsUnlocked (int rowsUnlocked) {
        this.rowsUnlocked = rowsUnlocked;
    }

    public int getLastLevelDifficultyCoeff () {
        return lastLevelDifficultyCoeff;
    }

    public void setLastLevelDifficultyCoeff (int lastLevelDifficultyCoeff) {
        this.lastLevelDifficultyCoeff = lastLevelDifficultyCoeff;
    }

    public LevelLength getLastLevelLength () {
        return lastLevelLength;
    }

    public void setLastLevelLength (LevelLength lastLevelLength) {
        this.lastLevelLength = lastLevelLength;
    }

    public LevelDifficulty getLastLevelDifficulty () {
        return lastLevelDifficulty;
    }

    public void setLastLevelDifficulty (LevelDifficulty lastLevelDifficulty) {
        this.lastLevelDifficulty = lastLevelDifficulty;
    }

    public void setRowsUnlockedByDifficulty (int difficulty) {
        if (difficulty == 2 || difficulty == 3) {
            rowsUnlocked = 1;
        } else if (difficulty == 4 || difficulty == 5) {
            rowsUnlocked = 2;
        } else if (difficulty == 5 || difficulty == 6) {
            rowsUnlocked = 3;
        }
    }

    public void calculateRerollCost () {
        if(PlayerInventory.getInstance() == null){
            return; //It's not initialized yet, don't use it
        }

        rerollCost = Math.round(PlayerInventory.getInstance().getCashMoney() * 0.25f);
        float discount = PlayerStats.getInstance().getShopRerollDiscount();
        if (discount > 100) {
            discount = 100;
        }

        // Calculate the discounted cost
        float discountedCost = rerollCost * (1 - discount / 100);
        rerollCost = Math.round(discountedCost);
        if(rerollCost < 1){
            rerollCost = 1;
        }
    }

    public int getRerollCost () {
        if(rerollCost == 0){
            calculateRerollCost();
        }
        return rerollCost;
    }

    public void setRerollCost (int rerollCost) {
        this.rerollCost = rerollCost;
    }
}
