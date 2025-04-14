package net.riezebos.bruus.tbd.game.gamestate;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.items.items.Contract;
import net.riezebos.bruus.tbd.game.items.items.util.ContractCounter;
import net.riezebos.bruus.tbd.game.items.items.util.ContractHelper;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.LevelLength;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.boardcreators.BoonSelectionBoardCreator;
import net.riezebos.bruus.tbd.guiboards.boardcreators.ShopBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class ShopManager {

    private int amountOfFreeItems;
    private int rowsUnlocked = 1;
    private int rerollCost;
    private int freeRerollsLeft = 0;

    private int lastLevelDifficultyScore;
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


    public int getLastLevelDifficultyScore () {
        return lastLevelDifficultyScore;
    }

    public void setLastLevelDifficultyScore (int lastLevelDifficultyScore) {
        this.lastLevelDifficultyScore = lastLevelDifficultyScore;
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
        } else if (difficulty == 6) {
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
        int yOffset = 0;
        int bountyHunterCounter = 0;
        for(ContractCounter contractCounter : ContractHelper.getInstance().getFinishedContracts()){
            ItemRarityEnums itemRarity = ItemRarityEnums.getRandomRareItemSlot();
            ItemEnums itemToAdd = getRandomAvailableItemByRarity(itemRarity);
            PlayerInventory.getInstance().addItem(itemToAdd);
            AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
            ContractHelper.getInstance().removeContract(contractCounter);
            BoardManager.getInstance().getShopBoard().addContractAnimation(getContractAnimation(itemToAdd, yOffset));
            yOffset += ShopBoardCreator.shopItemIconDimensions + 5;

            if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.Contract) != null){
                Contract contract = (Contract) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.Contract);
                contract.subtractItem(1);
            }
            bountyHunterCounter += 1;

            if(bountyHunterCounter >= 3 && PlayerProfileManager.getInstance().getLoadedProfile().getBountyHunterLevel() <= 0){
                PlayerProfileManager.getInstance().getLoadedProfile().setBountyHunterLevel(1);
                PlayerProfileManager.getInstance().exportCurrentProfile();
                BoardManager.getInstance().getShopBoard().addContractAnimation(BoonSelectionBoardCreator.createBoonUnlockComponent(BoonEnums.BOUNTY_HUNTER));
            }
        }
    }


    //helper method for creating an animation for the contract completing
    private GUIComponent getContractAnimation(ItemEnums itemEnums, int yOffset){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(DataClass.getInstance().getWindowWidth() / 2);
        spriteConfiguration.setyCoordinate(DataClass.getInstance().getWindowHeight() / 2);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(itemEnums.getItemIcon());

        GUIComponent itemSprite = new GUIComponent(spriteConfiguration);
        itemSprite.setImageDimensions(ShopBoardCreator.shopItemIconDimensions, ShopBoardCreator.shopItemIconDimensions);
        itemSprite.setTransparancyAlpha(true, 1, -0.005f);
        itemSprite.setCenterCoordinates(DataClass.getInstance().getWindowWidth() / 2,
                (DataClass.getInstance().getWindowHeight() / 2) - yOffset);
        return itemSprite;
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
