package net.riezebos.bruus.tbd.guiboards.boardcreators;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.LevelLength;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.guicomponents.*;
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
    private int itemWidth;
    private int itemHeight;
    private int horizontalSpacing;
    private int verticalSpacing;
    private int horizontalScreenDistance;
    private int verticalScreenDistance;
    private int imageScale;
    private int textScale;

    private int fourthRowY;
    private ShopManager shopManager;

    public ShopBoardCreator (int boardWidth, int boardHeight, int itemWidth, int itemHeight, int horizontalSpacing, int verticalSpacing
            , int horizontalScreenDistance, int verticalScreenDistance, int imageScale, int textScale, ShopManager shopManager) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
        this.horizontalScreenDistance = horizontalScreenDistance;
        this.verticalScreenDistance = verticalScreenDistance;
        this.shopManager = shopManager;
        this.imageScale = imageScale;
        this.textScale = textScale;
        fourthRowY = 3 * (itemHeight + verticalSpacing) + verticalSpacing;
    }


    public MenuCursor createCursor (GUITextCollection textC) {
        int initCursorX = textC.getComponents().get(0).getXCoordinate();
        int initCursorY = textC.getComponents().get(0).getYCoordinate();
        float scale = textScale;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(initCursorX, initCursorY, scale, PlayerStats.getInstance().getSpaceShipImage());
        MenuCursor button = new MenuCursor(spriteConfiguration);
        return button;
    }


    public GUITextCollection createReturnToMainMenu () {
        int xCoordinate = 100;
        int yCoordinate = boardHeight - 80;
        String text = "RETURN TO MAIN MENU";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setMenuFunctionality(MenuFunctionEnums.Return_To_Main_Menu);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Return to the main menu");

//        float scale = textScale;
//        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate,yCoordinate,scale, ImageEnums.Test_Image);
//        MenuButton button = new MenuButton(spriteConfiguration);
//        button.setDescriptionOfComponent(text);
//        button.setMenuFunctionality(MenuFunctionEnums.Return_To_Main_Menu);

        return textCollection;
    }

    public GUITextCollection createStartNextLevelButton () {
        int xCoordinate = 500;
        int yCoordinate = boardHeight - 80;
        String text = "START NEXT LEVEL";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setMenuFunctionality(MenuFunctionEnums.Start_Game);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Start the next level");
        return textCollection;
    }

    public GUITextCollection createPlayerInventoryButton () {
        int xCoordinate = 780;
        int yCoordinate = boardHeight - 80;
        String text = "VIEW OR HIDE INVENTORY";

        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setMenuFunctionality(MenuFunctionEnums.Open_Inventory);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Opens or hides your inventory, allows you to see your current items.");
        return textCollection;
    }


    public GUITextCollection createMoneyObject (GUIComponent backgroundCard) {
        int moneyX = backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 8;
        int moneyY = backgroundCard.getYCoordinate() + backgroundCard.getHeight() / 7;

        GUITextCollection textCollection = new GUITextCollection(moneyX, moneyY, "MINERALS: " + Math.round(PlayerInventory.getInstance().getCashMoney()));
        GUIComponent lastComponent = textCollection.getComponents().get(textCollection.getComponents().size() - 1);

        int moneyX1 = lastComponent.getXCoordinate() + lastComponent.getWidth();
        int moneyY1 = lastComponent.getYCoordinate();
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(moneyX1, moneyY1, 0.5f, ImageEnums.TopazGem7);
        DisplayOnly moneyImage = new DisplayOnly(spriteConfiguration);

        moneyImage.setYCoordinate(moneyY1 - moneyImage.getHeight() / 2);

        textCollection.addComponentToCollection(moneyImage);
        return textCollection;
    }


    public GUITextCollection createNextLevelDifficultyIcon (GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int YCoordinate = backgroundCard.getCenterYCoordinate();
        int difficulty = 0;
        ImageEnums iconEnum = null;
        String string = "";

        if (LevelManager.getInstance().isNextLevelABossLevel()) {
            iconEnum = ImageEnums.RedWings5;
            difficulty = 6;
            string = "NEXT: BOSS LEVEL";
        } else {
            if(AudioManager.getInstance().isMusicControlledByThirdPartyApp()){
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
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, YCoordinate, textScale, iconEnum);
        DisplayOnly icon = new DisplayOnly(spriteConfiguration);
        icon.setCenterCoordinates(xCoordinate, YCoordinate);


        GUITextCollection textCollection = new GUITextCollection(xCoordinate, YCoordinate, string);
        GUIComponent lastComponent = textCollection.getComponents().get(textCollection.getComponents().size() - 1);
        GUIComponent firstComponent = textCollection.getComponents().get(0);

        int width = (lastComponent.getXCoordinate() + lastComponent.getWidth() - firstComponent.getXCoordinate());
        xCoordinate = backgroundCard.getCenterXCoordinate() - (width / 2);
        textCollection = new GUITextCollection(xCoordinate, YCoordinate + Math.round(backgroundCard.getHeight() * 0.3f), string);

        textCollection.addComponentToCollection(icon);
        return textCollection;
    }

    public GUIComponent createRerollBackgroundCard (GUIComponent nextDifficultyCard) {
        int xCoordinate = nextDifficultyCard.getXCoordinate();
        int yCoordinate = nextDifficultyCard.getYCoordinate() + nextDifficultyCard.getHeight() + 20;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(0.3f);
        spriteConfiguration.setImageType(ImageEnums.Square_Card);

        GUIComponent card = new DisplayOnly(spriteConfiguration);
        return card;
    }

    public GUIComponent createRerollButton (GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int yCoordinate = backgroundCard.getCenterYCoordinate();

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.GUIRefresh);
        GUIComponent refreshButton = new MenuButton(spriteConfiguration);
        refreshButton.setMenuFunctionality(MenuFunctionEnums.RerollShop);

        int backgroundCardDimension = backgroundCard.getWidth() / 4;
        refreshButton.setImageDimensions(backgroundCardDimension, backgroundCardDimension);
        refreshButton.setCenterCoordinates(xCoordinate, yCoordinate);
        refreshButton.setDescriptionOfComponent("Refreshes all items in the shop.");
        return refreshButton;

    }

    public GUITextCollection createRerollCostText (GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getXCoordinate();
        int yCoordinate = backgroundCard.getYCoordinate();
        String rerollCost = String.valueOf(ShopManager.getInstance().getRerollCost());

        if(ShopManager.getInstance().getFreeRefreshessLeft() > 0){
            rerollCost = "FREE";
        }

        String string = "REROLL COST: " + rerollCost;
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, string);
        GUIComponent lastComponent = textCollection.getComponents().get(textCollection.getComponents().size() - 1);
        GUIComponent firstComponent = textCollection.getComponents().get(0);

        //Calculate the new xCoordinate to center the whole collection and then recreate it with the correct xCoordinate
        int width = (lastComponent.getXCoordinate() + lastComponent.getWidth() - firstComponent.getXCoordinate());
        xCoordinate = backgroundCard.getCenterXCoordinate() - (width / 2);
        textCollection = new GUITextCollection(xCoordinate, yCoordinate + Math.round(backgroundCard.getHeight() * 0.2f), string);


        //Add another icon to the end of the collection
        lastComponent = textCollection.getComponents().get(textCollection.getComponents().size() - 1);
        int iconXCoordinate = lastComponent.getXCoordinate() + lastComponent.getWidth();
        int iconYCoordinate = lastComponent.getYCoordinate();
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(iconXCoordinate, iconYCoordinate, 0.4f, ImageEnums.TopazGem7);
        DisplayOnly icon = new DisplayOnly(spriteConfiguration);
        icon.setYCoordinate(icon.getYCoordinate() - (icon.getHeight() / 4));
        textCollection.addComponentToCollection(icon);
        return textCollection;
    }


    private SpriteConfiguration createSpriteConfiguration (int xCoordinate, int yCoordinate, float scale, ImageEnums imageType) {
        SpriteConfiguration config = new SpriteConfiguration();
        config.setxCoordinate(xCoordinate);
        config.setyCoordinate(yCoordinate);
        config.setScale(scale);
        config.setImageType(imageType);
        return config;
    }

    public List<GUIComponent> createNewFirstRowOfItems () {
        List<GUIComponent> firstRow = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int x = (i * (itemWidth + horizontalSpacing)) + horizontalScreenDistance;
            int y = verticalScreenDistance;
            SpriteConfiguration spriteConfiguration = createSpriteConfiguration(x, y, 1, ImageEnums.Invisible);
            ShopItem shopItem = new ShopItem(spriteConfiguration, ItemRarityEnums.Common);

            if (shopManager.getRowsUnlocked() < 1) {
                shopItem.lockItemInShop();
                shopItem.getShopItemInformation().setItemDescription("Play a level of atleast difficulty 2 to unlock this row");
            }
            shopItem.setImageDimensions(80, 80);
            firstRow.add(shopItem);
        }
        return firstRow;
    }

    public List<GUIComponent> createNewSecondRowOfItems () {
        List<GUIComponent> secondRow = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int x = (i * (itemWidth + horizontalSpacing)) + horizontalScreenDistance;
            int y = (1 * (itemHeight + verticalSpacing)) + verticalScreenDistance;
            ItemRarityEnums type = (i == 6 || i == 7) ? ItemRarityEnums.Rare : ItemRarityEnums.Common; // If index is 6 or 7, it's a rare item

            SpriteConfiguration spriteConfiguration = createSpriteConfiguration(x, y, 1, ImageEnums.Invisible);
            ShopItem shopItem = new ShopItem(spriteConfiguration, type);
//            shopItem.getMenuItemInformation().setCost(shopItem.getMenuItemInformation().getCost() * 1.25f);


            if (shopManager.getRowsUnlocked() < 2) {
                shopItem.lockItemInShop();
                shopItem.getShopItemInformation().setItemDescription("Play a level of atleast difficulty 4 to unlock this row");
            }
            shopItem.setImageDimensions(80, 80);
            secondRow.add(shopItem);
        }
        return secondRow;
    }


    public List<GUIComponent> createNewThirdRowOfItems () {
        List<GUIComponent> thirdRow = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int x = (i * (itemWidth + horizontalSpacing)) + horizontalScreenDistance;
            int y = (2 * (itemHeight + verticalSpacing)) + verticalScreenDistance;
            ItemRarityEnums type = (i == 6 || i == 7) ? ItemRarityEnums.Rare : ItemRarityEnums.Common; // If index is 6 or 7, it's a rare item

            SpriteConfiguration spriteConfiguration = createSpriteConfiguration(x, y, 1, ImageEnums.Invisible);
            ShopItem shopItem = new ShopItem(spriteConfiguration, type);
//            shopItem.getMenuItemInformation().setCost(shopItem.getMenuItemInformation().getCost() * 1.5f);


            if (shopManager.getRowsUnlocked() < 3) {
                shopItem.lockItemInShop();
                shopItem.getShopItemInformation().setItemDescription("Play a level of atleast difficulty 6 to unlock this row");
            }
            shopItem.setImageDimensions(80, 80);
            thirdRow.add(shopItem);
        }
        return thirdRow;
    }


    public DisplayOnly createItemRowsBackgroundCard () {
        float widthRatio = 1200 / 1440f;
        float heightRatio = 550f / 875f;
        int cardWidth = (int) (boardWidth * widthRatio);
        int cardHeight = (int) (boardHeight * heightRatio);
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(-20, -20, 1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);
        return backgroundCard;
    }


    public DisplayOnly createSongDifficultyBackgroundCard () {
        float widthRatio = 450 / 1440f;
        float heightRatio = 200f / 875f;
        int cardWidth = (int) (boardWidth * widthRatio);
        int cardHeight = (int) (boardHeight * heightRatio);
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(30, fourthRowY - 20, 1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);
        return backgroundCard;
    }


    public DisplayOnly createSongLengthBackgroundCard () {
        float widthRatio = 450 / 1440f;
        float heightRatio = 200f / 875f;
        float xCoordPaddingRatio = 50 / 1440f;
        int cardWidth = (int) (boardWidth * widthRatio);
        int cardHeight = (int) (boardHeight * heightRatio);
        int xCoordPadding = (int) (boardWidth * xCoordPaddingRatio);
        int xCoord = (3 * (itemWidth + horizontalSpacing)) + (horizontalScreenDistance * 2);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                xCoord - xCoordPadding,
                fourthRowY - 20,
                1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);

        return backgroundCard;
    }

    public DisplayOnly createDescriptionRowsBackgroundCard () {
        float widthRatio = 400 / 1440f;
        float heightRatio = 300 / 875f;
        int cardWidth = (int) (boardWidth * widthRatio);
        int cardHeight = (int) (boardHeight * heightRatio);
        int xCoord = boardWidth / 2 + (boardWidth / 5);


        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                xCoord,
                fourthRowY - 20,
                1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);

        return backgroundCard;
    }

    public DisplayOnly createInventoryBackgroundCard () {
        float widthRatio = 1000 / 1440f;
        float heightRatio = 700 / 875f;

        int cardWidth = (int) (boardWidth * widthRatio);
        int cardHeight = (int) (boardHeight * heightRatio);
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                boardWidth / 6,
                boardHeight / 2 - (boardHeight / 4),
                1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);
        backgroundCard.setXCoordinate(40);
        backgroundCard.setYCoordinate(20);

        return backgroundCard;
    }


    public DisplayOnly createNextLevelDifficultyBackground () {
        int xCoordinate = Math.round(boardWidth - (boardWidth * 0.2f));
        int yCoordinate = 20;

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                xCoordinate,
                yCoordinate,
                0.3f, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);

        return backgroundCard;
    }

    public MenuButton createSelectEasyDifficulty (GUIComponent backgroundCard) {
        int y = fourthRowY + 40;
        int x0 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 6;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x0,
                y,
                1, ImageEnums.BlueWings1);
        MenuButton selectEasyDifficulty = new MenuButton(spriteConfiguration);
        selectEasyDifficulty.setLevelDifficulty(LevelDifficulty.Easy);
        selectEasyDifficulty.setMenuFunctionality(MenuFunctionEnums.SelectSongDifficulty);
        selectEasyDifficulty.setCenterCoordinates(x0, y);
        selectEasyDifficulty.setDescriptionOfComponent("Sets the difficulty of the next level to EASY. Increases the difficulty by 1");
        return selectEasyDifficulty;
    }

    public MenuButton createSelectMediumDifficulty (GUIComponent backgroundCard) {
        int y = fourthRowY + 40;
        int x1 = itemWidth + horizontalSpacing + backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 6;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x1,
                y,
                1, ImageEnums.BlueWings3);
        MenuButton selectEasyDifficulty = new MenuButton(spriteConfiguration);
        selectEasyDifficulty.setLevelDifficulty(LevelDifficulty.Medium);
        selectEasyDifficulty.setMenuFunctionality(MenuFunctionEnums.SelectSongDifficulty);
        selectEasyDifficulty.setCenterCoordinates(x1, y);
        selectEasyDifficulty.setDescriptionOfComponent("Sets the difficulty of the next level to MEDIUM. Increases the difficulty by 2");
        return selectEasyDifficulty;
    }

    public MenuButton createSelectHardDifficulty (GUIComponent backgroundCard) {
        int y = fourthRowY + 40;
        int x2 = 2 * (itemWidth + horizontalSpacing) + backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 6;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x2,
                y,
                1, ImageEnums.BlueWings5);
        MenuButton selectEasyDifficulty = new MenuButton(spriteConfiguration);
        selectEasyDifficulty.setLevelDifficulty(LevelDifficulty.Hard);
        selectEasyDifficulty.setMenuFunctionality(MenuFunctionEnums.SelectSongDifficulty);
        selectEasyDifficulty.setCenterCoordinates(x2, y);
        selectEasyDifficulty.setDescriptionOfComponent("Sets the difficulty of the next level to HARD. Increases the difficulty by 3");
        return selectEasyDifficulty;
    }


    public GUITextCollection createSelectDifficultyText (GUIComponent songDifficultyBackgroundCard, GUIComponent difficultyHard) {
        int textX = songDifficultyBackgroundCard.getXCoordinate() + (songDifficultyBackgroundCard.getWidth() / 3);
        int textY = difficultyHard.getYCoordinate() + difficultyHard.getHeight();
        GUITextCollection textCollection = new GUITextCollection(textX, textY, "SELECT DIFFICULTY");
        return textCollection;
    }


    public GUIComponent createShortSongSelection (GUIComponent backgroundCard) {
        int y = fourthRowY + 40;
        int x0 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 6;
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


    public GUIComponent createMediumSongSelection (GUIComponent backgroundCard) {
        int y = fourthRowY + 40;
        int x1 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 6 + (itemWidth + horizontalSpacing);
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


    public GUIComponent createLongSelection (GUIComponent backgroundCard) {
        int y = fourthRowY + 40;
        int x2 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() / 6 + 2 * (itemWidth + horizontalSpacing);
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


    public GUITextCollection createSongSelectionText (GUIComponent backgroundCard, GUIComponent selectLongMenuButton) {
        int textX = backgroundCard.getXCoordinate() + (backgroundCard.getWidth() / 3);
        int textY = selectLongMenuButton.getYCoordinate() + selectLongMenuButton.getHeight();
        return new GUITextCollection(textX, textY, "SELECT LENGTH");
    }

    public GUIComponent createDescriptionBox () {
        int y = fourthRowY;
        int x = boardWidth / 2 + (boardWidth / 5);
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x,
                y,
                1, ImageEnums.Square_Card);
        return new DisplayOnly(spriteConfiguration);
    }

    public void updateDifficultyIconsToDifficulty (LevelDifficulty currentLevelDifficulty, GUIComponent easy,
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

    public void updateLengthIconsToLength (LevelLength currentLevelLength, GUIComponent shortLength,
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
