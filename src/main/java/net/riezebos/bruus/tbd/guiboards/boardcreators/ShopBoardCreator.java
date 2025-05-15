package net.riezebos.bruus.tbd.guiboards.boardcreators;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.LevelLength;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.guicomponents.*;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.LevelSongs;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicMediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ShopBoardCreator {
    private int boardWidth;
    private int boardHeight;
    private float horizontalSpacing;
    private float verticalSpacing;
    private float horizontalScreenDistance;
    private float verticalScreenDistance;
    private float imageScale;
    private float objectScale;

    private int fourthRowYCoordinate;
    private ShopManager shopManager;
    public static int shopItemIconDimensions = Math.round(75 * DataClass.getInstance().getResolutionFactor());

    public ShopBoardCreator() {
        this.boardWidth = DataClass.getInstance().getWindowWidth();
        this.boardHeight = DataClass.getInstance().getWindowHeight();
        this.horizontalSpacing = 35 * DataClass.getInstance().getResolutionFactor();
        this.verticalSpacing = 60 * DataClass.getInstance().getResolutionFactor();
        this.horizontalScreenDistance = 75 * DataClass.getInstance().getResolutionFactor();
        this.verticalScreenDistance = 20 * DataClass.getInstance().getResolutionFactor();
        this.shopManager = ShopManager.getInstance();
        this.imageScale = Math.round(1 * DataClass.getInstance().getResolutionFactor());
        this.objectScale = Math.round(1 * DataClass.getInstance().getResolutionFactor());
        fourthRowYCoordinate = Math.round((3.3f * (shopItemIconDimensions + verticalSpacing) + verticalSpacing)
                * DataClass.getInstance().getResolutionFactor());
    }


    public MenuCursor createCursor(GUITextCollection textC) {
        int initCursorX = textC.getComponents().get(0).getXCoordinate();
        int initCursorY = textC.getComponents().get(0).getYCoordinate();
        float scale = objectScale;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(initCursorX, initCursorY, scale, PlayerStats.getInstance().getSpaceShipImage());
        MenuCursor button = new MenuCursor(spriteConfiguration);
        return button;
    }


    public GUITextCollection createReturnToMainMenu(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int yCoordinate = backgroundCard.getCenterYCoordinate() - Math.round(5 * DataClass.getInstance().getResolutionFactor());
        String text = "RETURN TO MAIN MENU";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(objectScale);
        xCoordinate = backgroundCard.getCenterXCoordinate() - (textCollection.getWidth() / 2);
        textCollection.setStartingXCoordinate(xCoordinate);
        textCollection.setMenuFunctionality(MenuFunctionEnums.Return_To_Main_Menu);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Return to the main menu");
        return textCollection;
    }

    public GUITextCollection createStartNextLevelButton(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int yCoordinate = backgroundCard.getCenterYCoordinate() - Math.round(5 * DataClass.getInstance().getResolutionFactor());
        String text = "START NEXT LEVEL";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(objectScale);
        xCoordinate = backgroundCard.getCenterXCoordinate() - (textCollection.getWidth() / 2);
        textCollection.setStartingXCoordinate(xCoordinate);
        textCollection.setMenuFunctionality(MenuFunctionEnums.Start_Game);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Start the next level");
        return textCollection;
    }

    public GUITextCollection createPlayerInventoryButton(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int yCoordinate = backgroundCard.getCenterYCoordinate() - Math.round(5 * DataClass.getInstance().getResolutionFactor());
        String text = "VIEW OR HIDE INVENTORY";

        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(objectScale);
        xCoordinate = backgroundCard.getCenterXCoordinate() - (textCollection.getWidth() / 2);
        textCollection.setStartingXCoordinate(xCoordinate);
        textCollection.setMenuFunctionality(MenuFunctionEnums.Open_Inventory);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Opens or hides your inventory, allows you to see your current items.");
        return textCollection;
    }

    public DisplayOnly createReturnToMainMenuBackgroundCard() {
        int xCoordinate = Math.round(boardWidth * 0.06944f);
        int yCoordinate = boardHeight - Math.round(boardHeight * 0.09f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Wide_Card);
        DisplayOnly displayOnly = new DisplayOnly(spriteConfiguration);
        int width = Math.round(200 * DataClass.getInstance().getResolutionFactor());
        int height = Math.round(50 * DataClass.getInstance().getResolutionFactor());
        displayOnly.setImageDimensions(width, height);
        return displayOnly;
    }

    public DisplayOnly createInventoryButtonBackgroundCard() {
        int xCoordinate = Math.round(boardWidth * 0.3472f);
        int yCoordinate = boardHeight - Math.round(boardHeight * 0.09f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Wide_Card);
        DisplayOnly displayOnly = new DisplayOnly(spriteConfiguration);
        int width = Math.round(220 * DataClass.getInstance().getResolutionFactor());
        int height = Math.round(50 * DataClass.getInstance().getResolutionFactor());
        displayOnly.setImageDimensions(width, height);
        return displayOnly;
    }

    public DisplayOnly createStartNextLevelBackgroundCard() {
        int xCoordinate = Math.round(boardWidth * 0.6416f);
        int yCoordinate = boardHeight - Math.round(boardHeight * 0.09f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Wide_Card);
        DisplayOnly displayOnly = new DisplayOnly(spriteConfiguration);
        int width = Math.round(180 * DataClass.getInstance().getResolutionFactor());
        int height = Math.round(50 * DataClass.getInstance().getResolutionFactor());
        displayOnly.setImageDimensions(width, height);
        return displayOnly;
    }


    public GUITextCollection createMoneyObject(GUIComponent backgroundCard) {
        int startingXCoordinate = backgroundCard.getCenterXCoordinate();
        int moneyY = Math.round(backgroundCard.getYCoordinate() + backgroundCard.getHeight() * 0.85f);

        GUITextCollection textCollection = new GUITextCollection(startingXCoordinate, moneyY, "MINERALS: " + Math.round(PlayerInventory.getInstance().getCashMoney()));
        textCollection.setScale(1.75f * DataClass.getInstance().getResolutionFactor());

        startingXCoordinate = startingXCoordinate - textCollection.getWidth() / 2;
        textCollection.setStartingXCoordinate(startingXCoordinate);

        int moneyX1 = startingXCoordinate + textCollection.getWidth(); //Manually place it at the correct X coordinate
        int moneyY1 = textCollection.getComponents().get(0).getYCoordinate();
        float scale = 0.5f * DataClass.getInstance().getResolutionFactor();

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(moneyX1, moneyY1, scale, ImageEnums.TopazGem7);
        DisplayOnly moneyImage = new DisplayOnly(spriteConfiguration);

//        moneyImage.setYCoordinate(moneyY1 - moneyImage.getHeight() / 2);
        textCollection.addComponentToCollection(moneyImage);

        return textCollection;
    }

    public GUITextCollection createProtossCapacityText(GUIComponent backgroundCard){
        int startingXCoordinate = Math.round(backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.2f);
        int moneyY = Math.round(backgroundCard.getYCoordinate() + backgroundCard.getHeight() * 0.85f);

        int amount = PlayerStats.getInstance().getAmountOfProtossArbiters() + PlayerStats.getInstance().getAmountOfProtossShuttles() + PlayerStats.getInstance().getAmountOfProtossScouts();
        String text = "" + amount + ":" + PlayerStats.getInstance().getMaxAmountOfProtoss();

        GUITextCollection textCollection = new GUITextCollection(startingXCoordinate, moneyY, text);
        textCollection.setScale(1.25f * DataClass.getInstance().getResolutionFactor());

        startingXCoordinate = startingXCoordinate - textCollection.getWidth() / 2;
        textCollection.setStartingXCoordinate(startingXCoordinate);

        int shipX = startingXCoordinate + textCollection.getWidth(); //Manually place it at the correct X coordinate
        int shipY = textCollection.getComponents().get(0).getCenterYCoordinate();
        float scale = 0.275f * DataClass.getInstance().getResolutionFactor();

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(shipX, shipY, scale, ImageEnums.ProtossShipAmountIcon);
        DisplayOnly scoutImage = new DisplayOnly(spriteConfiguration);

        scoutImage.setCenterYCoordinate(shipY);
        textCollection.addComponentToCollection(scoutImage);

        return textCollection;
    }


    public GUITextCollection createNextLevelDifficultyIcon(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int YCoordinate = backgroundCard.getCenterYCoordinate() + Math.round(backgroundCard.getHeight() * 0.3f);
        int difficulty = 0;
        ImageEnums iconEnum = null;
        String string = "";

        if (LevelManager.getInstance().isNextLevelABossLevel()) {
            iconEnum = ImageEnums.RedWings5;
            difficulty = 6;
            string = "NEXT: BOSS LEVEL";
        } else {
            if (AudioManager.getInstance().isMusicControlledByThirdPartyApp()) {
                difficulty = LevelSongs.getDifficultyScoreByDifficultyOnly(LevelManager.getInstance().getCurrentLevelDifficulty());
            } else {
                difficulty = LevelSongs.getDifficultyScore(
                        LevelManager.getInstance().getCurrentLevelDifficulty(),
                        LevelManager.getInstance().getCurrentLevelLength()
                );
            }

            iconEnum = LevelSongs.getImageEnumByDifficultyScore(difficulty);
            string = "NEXT DIFFICULTY: " + difficulty;
        }

        //The difficulty icon
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, YCoordinate, 1.2f * DataClass.getInstance().getResolutionFactor(), iconEnum);
        DisplayOnly icon = new DisplayOnly(spriteConfiguration);
        icon.setCenterCoordinates(xCoordinate, backgroundCard.getCenterYCoordinate() - Math.round(icon.getHeight() * 0.1f));


        GUITextCollection textCollection = new GUITextCollection(xCoordinate, YCoordinate, string);
        textCollection.setScale(1.2f * DataClass.getInstance().getResolutionFactor());
        xCoordinate = backgroundCard.getCenterXCoordinate() - (textCollection.getWidth() / 2);
        textCollection.setStartingXCoordinate(xCoordinate);
        textCollection.addComponentToCollection(icon); //Add the wings seperately
        return textCollection;
    }

    public GUIComponent createRerollBackgroundCard(GUIComponent nextDifficultyCard) {
        int xCoordinate = nextDifficultyCard.getXCoordinate() + nextDifficultyCard.getWidth();
        int yCoordinate = nextDifficultyCard.getYCoordinate();
        float scale = 0.25f * DataClass.getInstance().getResolutionFactor();

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(scale);
        spriteConfiguration.setImageType(ImageEnums.Square_Card);

        GUIComponent backgroundCard = new GUIComponent(spriteConfiguration);
        backgroundCard.setYCoordinate(yCoordinate + Math.round(backgroundCard.getHeight() * 0.1f));
        return backgroundCard;
    }

    public GUIComponent createRerollButton(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int yCoordinate = backgroundCard.getCenterYCoordinate();

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.GUIRefresh);
        GUIComponent refreshButton = new MenuButton(spriteConfiguration);
        refreshButton.setMenuFunctionality(MenuFunctionEnums.RerollShop);

        int rerollButonDimensions = Math.round((backgroundCard.getWidth() / 4) * DataClass.getInstance().getResolutionFactor());
        refreshButton.setImageDimensions(rerollButonDimensions, rerollButonDimensions);
        refreshButton.setCenterCoordinates(xCoordinate, yCoordinate);
        refreshButton.setDescriptionOfComponent("Refreshes all items in the shop, allowing you to purchase new items. Cost is equal to 25% of minerals you entered the shop with.");
        return refreshButton;
    }

    public GUITextCollection createRerollText(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getXCoordinate() + Math.round(backgroundCard.getWidth() * 0.15f);
        int yCoordinate = backgroundCard.getYCoordinate();


        String string = "REROLL SHOP: ";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate + Math.round(backgroundCard.getHeight() * 0.2f), string);
        textCollection.setScale(1.1f * DataClass.getInstance().getResolutionFactor());
        textCollection.setStartingXCoordinate(backgroundCard.getCenterXCoordinate() - (textCollection.getWidth() / 2));
        return textCollection;
    }

    public GUITextCollection createRerollCostText(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getXCoordinate() + Math.round(backgroundCard.getWidth() * 0.5f);
        int yCoordinate = backgroundCard.getYCoordinate() + Math.round(backgroundCard.getHeight() * 0.725f);
        String rerollCost = String.valueOf(ShopManager.getInstance().getRerollCost());

        if (ShopManager.getInstance().getFreeRefreshessLeft() > 0) {
            rerollCost = "FREE";
        }


        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, rerollCost);
        textCollection.setScale(1.1f * DataClass.getInstance().getResolutionFactor());
        textCollection.setStartingXCoordinate(backgroundCard.getCenterXCoordinate() - (textCollection.getWidth() / 2));

        int iconXCoordinate = textCollection.getComponents().get(0).getXCoordinate() + textCollection.getWidth();
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(iconXCoordinate, yCoordinate, 0.4f * DataClass.getInstance().getResolutionFactor(), ImageEnums.TopazGem7);
        DisplayOnly icon = new DisplayOnly(spriteConfiguration);
        icon.setYCoordinate(icon.getYCoordinate() - (icon.getHeight() / 4));
        textCollection.addComponentToCollection(icon);
        return textCollection;
    }


    private SpriteConfiguration createSpriteConfiguration(int xCoordinate, int yCoordinate, float scale, ImageEnums imageType) {
        SpriteConfiguration config = new SpriteConfiguration();
        config.setxCoordinate(xCoordinate);
        config.setyCoordinate(yCoordinate);
        config.setScale(scale);
        config.setImageType(imageType);
        return config;
    }

    private SpriteConfiguration createSpriteConfiguration(float xCoordinate, float yCoordinate, float scale, ImageEnums imageType) {
        SpriteConfiguration config = new SpriteConfiguration();
        config.setxCoordinate(xCoordinate);
        config.setyCoordinate(yCoordinate);
        config.setScale(scale);
        config.setImageType(imageType);
        return config;
    }

    public List<GUIComponent> createNewFirstRowOfItems() {
        List<GUIComponent> firstRow = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            float x = (i * (shopItemIconDimensions + horizontalSpacing)) + horizontalScreenDistance;
            float y = verticalScreenDistance;
            SpriteConfiguration spriteConfiguration = createSpriteConfiguration(x, y, 1, ImageEnums.Invisible);
            ShopItem shopItem = new ShopItem(spriteConfiguration, ItemRarityEnums.Common);

            if (shopManager.getRowsUnlocked() < 1) {
                shopItem.lockItemInShop();
                shopItem.getShopItemInformation().setItemDescription("Previously completed level must be a difficulty of 2 or higher.");
            }
            shopItem.setImageDimensions(shopItemIconDimensions, shopItemIconDimensions);
            shopItem.setColumn(0);
            shopItem.setRow(i);
            firstRow.add(shopItem);
        }
        return firstRow;
    }

    public List<GUIComponent> createNewSecondRowOfItems() {
        List<GUIComponent> secondRow = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            float x = (i * (shopItemIconDimensions + horizontalSpacing)) + horizontalScreenDistance;
            float y = (1 * (shopItemIconDimensions + verticalSpacing)) + verticalScreenDistance;
            ItemRarityEnums type = (i == 7) ? ItemRarityEnums.Rare : ItemRarityEnums.Common; // If index is 7, it's a rare item

            SpriteConfiguration spriteConfiguration = createSpriteConfiguration(x, y, 1, ImageEnums.Invisible);
            ShopItem shopItem = new ShopItem(spriteConfiguration, type);
//            shopItem.getMenuItemInformation().setCost(shopItem.getMenuItemInformation().getCost() * 1.25f);


            if (shopManager.getRowsUnlocked() < 2) {
                shopItem.lockItemInShop();
                shopItem.getShopItemInformation().setItemDescription("Previously completed level must be a difficulty of 4 or higher.");
            }
            shopItem.setImageDimensions(shopItemIconDimensions, shopItemIconDimensions);
            shopItem.setColumn(1);
            shopItem.setRow(i);
            secondRow.add(shopItem);
        }
        return secondRow;
    }


    public List<GUIComponent> createNewThirdRowOfItems() {
        List<GUIComponent> thirdRow = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            float x = (i * (shopItemIconDimensions + horizontalSpacing)) + horizontalScreenDistance;
            float y = (2 * (shopItemIconDimensions + verticalSpacing)) + verticalScreenDistance;
            ItemRarityEnums type = (i == 7) ? ItemRarityEnums.Rare : ItemRarityEnums.Common; // If index is 7, it's a rare item

            SpriteConfiguration spriteConfiguration = createSpriteConfiguration(x, y, 1, ImageEnums.Invisible);
            ShopItem shopItem = new ShopItem(spriteConfiguration, type);

            if (shopManager.getRowsUnlocked() < 3) {
                shopItem.lockItemInShop();
                shopItem.getShopItemInformation().setItemDescription("Previously completed level must be a difficulty of 6.");
            }
            shopItem.setImageDimensions(shopItemIconDimensions, shopItemIconDimensions);
            shopItem.setColumn(2);
            shopItem.setRow(i);
            thirdRow.add(shopItem);
        }
        return thirdRow;
    }


    public DisplayOnly createShopItemsBackground() {
        float widthRatio = 1100 / 1440f;
        float heightRatio = 550f / 875f;
        int cardWidth = Math.round((boardWidth * widthRatio));
        int cardHeight = Math.round((boardHeight * heightRatio));
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(-20, -20, 1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);
        return backgroundCard;
    }

    public GUITextCollection createLevelModifiersText(GUIComponent backgroundCard) {
        int xCoordinate = DataClass.getInstance().getWindowWidth() / 2;
        int yCoordinate = backgroundCard.getYCoordinate() - Math.round(30 * DataClass.getInstance().getResolutionFactor());
        String text = "NEXT LEVEL SETTINGS";

        GUITextCollection guiTextCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        guiTextCollection.setScale(1.2f * DataClass.getInstance().getResolutionFactor());
        xCoordinate = xCoordinate - guiTextCollection.getWidth() / 2;
        guiTextCollection.setStartingXCoordinate(xCoordinate);

        return guiTextCollection;
    }


    public DisplayOnly createSongDifficultyBackgroundCard() {
        float widthRatio = 425 / 1440f;
        float heightRatio = 150f / 875f;
        int cardWidth = Math.round((boardWidth * widthRatio));
        int cardHeight = Math.round((boardHeight * heightRatio));
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(30, fourthRowYCoordinate - 20, 1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);
        return backgroundCard;
    }


    public DisplayOnly createSongLengthBackgroundCard() {
        float widthRatio = 425 / 1440f;
        float heightRatio = 150f / 875f;
        float xCoordPaddingRatio = 50 / 1440f;
        int cardWidth = Math.round((boardWidth * widthRatio));
        int cardHeight = Math.round((boardHeight * heightRatio));
        int xCoordPadding = Math.round((boardWidth * xCoordPaddingRatio));
        float xCoord = (3 * (shopItemIconDimensions + horizontalSpacing)) + (horizontalScreenDistance * 2) * DataClass.getInstance().getResolutionFactor();

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                xCoord - xCoordPadding,
                Math.round((fourthRowYCoordinate - 20)),
                1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);

        return backgroundCard;
    }

    public DisplayOnly createDescriptionRowsBackgroundCard(GUIComponent rerollBackgroundCard) {
        float widthRatio = 350 / 1440f;
        float heightRatio = 300 / 875f;
        int cardWidth = Math.round((boardWidth * widthRatio));
        int cardHeight = Math.round((boardHeight * heightRatio));
        int xCoord = rerollBackgroundCard.getXCoordinate();
        int yCoord = rerollBackgroundCard.getYCoordinate() + rerollBackgroundCard.getHeight();


        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                xCoord,
                yCoord,
                1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);

        return backgroundCard;
    }

    public DisplayOnly createInventoryBackgroundCard() {
        float widthRatio = 850 / 1440f;
        float heightRatio = 700 / 875f;

        int cardWidth = Math.round((boardWidth * widthRatio));
        int cardHeight = Math.round((boardHeight * heightRatio));
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                Math.round((boardWidth / 6) * DataClass.getInstance().getResolutionFactor()),
                Math.round((boardHeight / 2 - (boardHeight / 4))),
                1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);
        backgroundCard.setXCoordinate(Math.round(40 * DataClass.getInstance().getResolutionFactor()));
        backgroundCard.setYCoordinate(Math.round(20 * DataClass.getInstance().getResolutionFactor()));

        return backgroundCard;
    }


    public DisplayOnly createShowNextLevelDifficultyBackground(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getXCoordinate() + backgroundCard.getWidth() + Math.round(10 * DataClass.getInstance().getResolutionFactor());
        int yCoordinate = fourthRowYCoordinate - Math.round(15 * DataClass.getInstance().getResolutionFactor());

        float widthRatio = 300 / 1440f;
        float heightRatio = 175 / 875f;

        int cardWidth = Math.round((boardWidth * widthRatio));
        int cardHeight = Math.round((boardHeight * heightRatio));

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                xCoordinate,
                yCoordinate,
                1,
                ImageEnums.Square_Card);
        DisplayOnly nextLevelDifficultyBackgroundCard = new DisplayOnly(spriteConfiguration);
        nextLevelDifficultyBackgroundCard.setImageDimensions(cardWidth, cardHeight);
        return nextLevelDifficultyBackgroundCard;
    }

    public MenuButton createSelectEasyDifficulty(GUIComponent backgroundCard) {
        int y = fourthRowYCoordinate + 40;
        int x0 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 6;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x0,
                y,
                1, ImageEnums.BlueWings1);
        MenuButton selectEasyDifficulty = new MenuButton(spriteConfiguration);
        selectEasyDifficulty.setLevelDifficulty(LevelDifficulty.Easy);
        selectEasyDifficulty.setMenuFunctionality(MenuFunctionEnums.SelectSongDifficulty);
        selectEasyDifficulty.setCenterCoordinates(x0, y);
        int amount = 1;
        if (AudioManager.getInstance().isMusicControlledByThirdPartyApp()) {
            amount = 2;
        }

        selectEasyDifficulty.setDescriptionOfComponent("Sets the difficulty to " + amount);
        return selectEasyDifficulty;
    }

    public MenuButton createSelectMediumDifficulty(GUIComponent backgroundCard) {
        float y = fourthRowYCoordinate + 40;
        float x1 = shopItemIconDimensions + horizontalSpacing + backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 6;
        ImageEnums iconEnum = ImageEnums.BlueWings3;
        if (LevelManager.getInstance().isNextLevelABossLevel()) {
            iconEnum = ImageEnums.RedWings5;
        }

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x1,
                y,
                1, iconEnum);
        MenuButton selectEasyDifficulty = new MenuButton(spriteConfiguration);
        selectEasyDifficulty.setLevelDifficulty(LevelDifficulty.Medium);
        selectEasyDifficulty.setMenuFunctionality(MenuFunctionEnums.SelectSongDifficulty);
        selectEasyDifficulty.setCenterCoordinates(x1, y);

        int amount = 2;
        if (AudioManager.getInstance().isMusicControlledByThirdPartyApp()) {
            amount *= 2;
        }

        selectEasyDifficulty.setDescriptionOfComponent("Sets the difficulty to " + amount +
                ". Enemies gain strength quicker and are 25% stronger");
        return selectEasyDifficulty;
    }

    public MenuButton createSelectHardDifficulty(GUIComponent backgroundCard) {
        float y = fourthRowYCoordinate + 40;
        float x2 = 2 * (shopItemIconDimensions + horizontalSpacing) + backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 6;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x2,
                y,
                1, ImageEnums.BlueWings5);
        MenuButton selectEasyDifficulty = new MenuButton(spriteConfiguration);
        selectEasyDifficulty.setLevelDifficulty(LevelDifficulty.Hard);
        selectEasyDifficulty.setMenuFunctionality(MenuFunctionEnums.SelectSongDifficulty);
        selectEasyDifficulty.setCenterCoordinates(x2, y);

        int amount = 3;
        if (AudioManager.getInstance().isMusicControlledByThirdPartyApp()) {
            amount *= 2;
        }

        selectEasyDifficulty.setDescriptionOfComponent("Sets the difficulty to " + amount
                + ". Enemies gain strength considerably quicker and are 50% stronger.");
        return selectEasyDifficulty;
    }


    public GUITextCollection createSelectDifficultyText(GUIComponent songDifficultyBackgroundCard, GUIComponent difficultyHard) {
        int textX = songDifficultyBackgroundCard.getXCoordinate() + (songDifficultyBackgroundCard.getWidth() / 3);
        int textY = difficultyHard.getYCoordinate() + difficultyHard.getHeight();
        GUITextCollection textCollection = new GUITextCollection(textX, textY, "SELECT DIFFICULTY");
        return textCollection;
    }


    public GUIComponent createShortSongSelection(GUIComponent backgroundCard) {
        float y = fourthRowYCoordinate + 40;
        float x0 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 6;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x0,
                y,
                1, ImageEnums.BlueWings1);
        MenuButton button = new MenuButton(spriteConfiguration);
        button.setLevelLength(LevelLength.Short);
        button.setMenuFunctionality(MenuFunctionEnums.SelectSongLength);
        button.setCenterCoordinates(x0, y);
        button.setDescriptionOfComponent("Sets the length of the next song between 0 and 3 minutes, increases the difficulty score by 1");
        return button;
    }


    public GUIComponent createMediumSongSelection(GUIComponent backgroundCard) {
        float y = fourthRowYCoordinate + 40;
        float x1 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 6 + (shopItemIconDimensions + horizontalSpacing);
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x1,
                y,
                1, ImageEnums.BlueWings3);
        MenuButton button = new MenuButton(spriteConfiguration);
        button.setLevelLength(LevelLength.Medium);
        button.setMenuFunctionality(MenuFunctionEnums.SelectSongLength);
        button.setCenterCoordinates(x1, y);
        button.setDescriptionOfComponent("Sets the length of the next song between 3 and 5 minutes, increases the difficulty score by 2");
        return button;
    }


    public GUIComponent createLongSelection(GUIComponent backgroundCard) {
        float y = fourthRowYCoordinate + 40;
        float x2 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 6 + 2 * (shopItemIconDimensions + horizontalSpacing);
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x2,
                y,
                1, ImageEnums.BlueWings5);
        MenuButton button = new MenuButton(spriteConfiguration);
        button.setLevelLength(LevelLength.Long);
        button.setMenuFunctionality(MenuFunctionEnums.SelectSongLength);
        button.setCenterCoordinates(x2, y);
        button.setDescriptionOfComponent("Sets the length of the next song to 5 or more minutes, increases the difficulty score by 3");
        return button;
    }


    public GUITextCollection createSongSelectionText(GUIComponent backgroundCard, GUIComponent selectLongMenuButton) {
        float textX = backgroundCard.getXCoordinate() + (backgroundCard.getWidth() / 3);
        float textY = selectLongMenuButton.getYCoordinate() + selectLongMenuButton.getHeight();
        return new GUITextCollection(textX, textY, "WORK IN PROGRESS");
    }

    public GUIComponent createDescriptionBox(GUIComponent backgroundCard) {
        float y = backgroundCard.getYCoordinate();
        float x = backgroundCard.getXCoordinate();
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x,
                y,
                backgroundCard.getScale(), ImageEnums.Square_Card);
        return new DisplayOnly(spriteConfiguration);
    }

    public void updateDifficultyIconsToDifficulty(LevelDifficulty currentLevelDifficulty, GUIComponent easy,
                                                  GUIComponent medium, GUIComponent hard) {
        if (currentLevelDifficulty.equals(LevelDifficulty.Easy)) {
            easy.setNewImage(ImageEnums.YellowWings1);
            medium.setNewImage(ImageEnums.BlueWings3);
            hard.setNewImage(ImageEnums.BlueWings5);
        } else if (currentLevelDifficulty.equals(LevelDifficulty.Medium)) {
            easy.setNewImage(ImageEnums.BlueWings1);
            medium.setNewImage(ImageEnums.YellowWings3);
            hard.setNewImage(ImageEnums.BlueWings5);
        } else if (currentLevelDifficulty.equals(LevelDifficulty.Hard)) {
            easy.setNewImage(ImageEnums.BlueWings1);
            medium.setNewImage(ImageEnums.BlueWings3);
            hard.setNewImage(ImageEnums.YellowWings5);
        }
    }

    public void updateLengthIconsToLength(LevelLength currentLevelLength, GUIComponent shortLength,
                                          GUIComponent mediumMediumLength, GUIComponent longLength) {
        if (AudioManager.getInstance().getMusicMediaPlayer().equals(MusicMediaPlayer.Default)) {
            if (currentLevelLength.equals(LevelLength.Short)) {
                shortLength.setNewImage(ImageEnums.YellowWings1);
                mediumMediumLength.setNewImage(ImageEnums.BlueWings3);
                longLength.setNewImage(ImageEnums.BlueWings5);
            } else if (currentLevelLength.equals(LevelLength.Medium)) {
                shortLength.setNewImage(ImageEnums.BlueWings1);
                mediumMediumLength.setNewImage(ImageEnums.YellowWings3);
                longLength.setNewImage(ImageEnums.BlueWings5);
            } else if (currentLevelLength.equals(LevelLength.Long)) {
                shortLength.setNewImage(ImageEnums.BlueWings1);
                mediumMediumLength.setNewImage(ImageEnums.BlueWings3);
                longLength.setNewImage(ImageEnums.YellowWings5);
            }
        }
    }


}
