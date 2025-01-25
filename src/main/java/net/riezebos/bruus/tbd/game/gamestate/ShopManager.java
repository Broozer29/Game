package net.riezebos.bruus.tbd.game.gamestate;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.items.items.Contract;
import net.riezebos.bruus.tbd.game.items.items.util.ContractCounter;
import net.riezebos.bruus.tbd.game.items.items.util.ContractHelper;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.LevelLength;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class ShopManager {

    private int amountOfFreeItems;
    private int rowsUnlocked = 1;
    private int rerollCost;
    private int freeRerollsLeft = 0;

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
        if (PlayerInventory.getInstance() == null) {
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
        if (rerollCost < 1) {
            rerollCost = 1;
        }
    }

    public void resetFreeRerolls () {
        this.freeRerollsLeft = PlayerStats.getInstance().getAmountOfFreeRerolls();
    }

    public void spendFreeReroll () {
        this.freeRerollsLeft -= 1;
    }

    public int getFreeRefreshessLeft () {
        return freeRerollsLeft;
    }

    public int getRerollCost () {
        if(rerollCost < 1) {
            calculateRerollCost();
        }
        return rerollCost;
    }

    public void activateFinishedContracts(){
        for(ContractCounter contractCounter : ContractHelper.getInstance().getFinishedContracts()){
            ItemRarityEnums itemRarity = ItemRarityEnums.getRandomRareItemSlot();
            ItemEnums itemToAdd = getRandomAvailableItemByRarity(itemRarity);
            PlayerInventory.getInstance().addItem(itemToAdd);
            AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
            ContractHelper.getInstance().removeContract(contractCounter);


            if(PlayerInventory.getInstance().getItemByName(ItemEnums.Contract) != null){
                Contract contract = (Contract) PlayerInventory.getInstance().getItemByName(ItemEnums.Contract);
                contract.subtractItem(1);
            }

        }
    }

    private ItemEnums getRandomAvailableItemByRarity(ItemRarityEnums category) {
        int MAX_ATTEMPTS = 50;
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            ItemEnums randomItem = ItemEnums.getRandomItemByRarity(category);

            Item tempItem = PlayerInventory.getInstance().createItemFromEnum(randomItem);

            if (tempItem != null && tempItem.isAvailable()) {
                return randomItem;
            }

            attempts++;
        }

        // If no item was found after MAX_ATTEMPTS, return an overclock
        OnScreenTextManager.getInstance().addText("COULDNT FIND A RANDOM ITEM TO ADD",
                DataClass.getInstance().getWindowWidth() / 2,
                DataClass.getInstance().getWindowHeight() / 2);
        return ItemEnums.Overclock;
    }

    public void setRerollCost (int rerollCost) {
        this.rerollCost = rerollCost;
    }
}
