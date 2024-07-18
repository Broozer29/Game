package guiboards.boardcreators;

import VisualAndAudioData.image.ImageEnums;
import game.items.PlayerInventory;
import game.managers.ShopManager;
import game.spawner.enums.LevelDifficulty;
import game.spawner.enums.LevelLength;
import guiboards.MenuCursor;
import guiboards.MenuObjectCollection;
import guiboards.MenuObjectPart;
import guiboards.boardEnums.MenuFunctionEnums;
import guiboards.boardEnums.MenuObjectEnums;

import java.awt.*;
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

    public MenuCursor createCursor(MenuObjectCollection selectedTile){
        int initCursorX = selectedTile.getXCoordinate();
        int initCursorY = selectedTile.getYCoordinate();

        MenuCursor menuCursor = new MenuCursor(initCursorX, initCursorY, imageScale);
        menuCursor.setXCoordinate(selectedTile.getXCoordinate() - (menuCursor.getxDistanceToKeep()));
        return menuCursor;
    }

    public MenuObjectCollection createReturnToMainMenu(){
        return new MenuObjectCollection(100, boardHeight - 80, textScale, "RETURN TO MAIN MENU", MenuObjectEnums.Text,
                MenuFunctionEnums.Return_To_Main_Menu);
    }

    public MenuObjectCollection createStartNextLevelButton(){
        return new MenuObjectCollection(500, boardHeight - 80, textScale, "START NEXT LEVEL", MenuObjectEnums.Text,
                MenuFunctionEnums.Start_Game);
    }

    public MenuObjectCollection createPlayerInventoryButton(){
        return new MenuObjectCollection(800, boardHeight - 80, textScale
                , "OPEN INVENTORY", MenuObjectEnums.Text, MenuFunctionEnums.Open_Inventory);
    }

    public MenuObjectCollection createMoneyObject(MenuObjectCollection nextLevelDifficultyBackground){
        int moneyX = nextLevelDifficultyBackground.getXCoordinate() + nextLevelDifficultyBackground.getMenuImages().get(0).getWidth() / 8;
        int moneyY = nextLevelDifficultyBackground.getYCoordinate() + nextLevelDifficultyBackground.getMenuImages().get(0).getHeight() / 7;
        MenuObjectCollection moneyObjectCollection = new MenuObjectCollection(moneyX, moneyY, textScale, "MONEY: " + PlayerInventory.getInstance().getCashMoney(),
                MenuObjectEnums.Text, MenuFunctionEnums.NONE);
        moneyObjectCollection.setNewImage(ImageEnums.TopazGem7);
        MenuObjectPart moneyImage = moneyObjectCollection.getMenuImages().get(0);
        moneyImage.setImageDimensions(moneyImage.getWidth() / 2, moneyImage.getHeight() / 2);

        MenuObjectPart lastLetter = moneyObjectCollection.getMenuTextImages().get((moneyObjectCollection.getMenuTextImages().size() - 1));
        moneyImage.setX(lastLetter.getXCoordinate() + lastLetter.getWidth());
        moneyImage.setY(moneyImage.getYCoordinate() - (moneyImage.getHeight() / 3));

        return moneyObjectCollection;
    }

    public MenuObjectCollection createNextLevelDifficultyIcon(MenuObjectCollection nextLevelDifficultyBackground){
        MenuObjectPart moneyBackgroundCard = nextLevelDifficultyBackground.getMenuImages().get(0);
        int diffiX = moneyBackgroundCard.getCenterXCoordinate();
        int diffiY = moneyBackgroundCard.getCenterYCoordinate() ;
        MenuObjectCollection icon = new MenuObjectCollection(diffiX, diffiY, textScale, "NEXT:", MenuObjectEnums.Text, MenuFunctionEnums.NONE);



        return icon;
    }

    public MenuObjectCollection createDifficultyObject(MenuObjectCollection nextLevelDifficultyBackground){
        int diffiX = nextLevelDifficultyBackground.getXCoordinate() + nextLevelDifficultyBackground.getMenuImages().get(0).getWidth() / 8;
        int diffiY = nextLevelDifficultyBackground.getYCoordinate() + nextLevelDifficultyBackground.getMenuImages().get(0).getHeight() / 3;
        return new MenuObjectCollection(diffiX, diffiY, textScale, "NEXT:", MenuObjectEnums.Text, MenuFunctionEnums.NONE);
    }

    public List<MenuObjectCollection> createFirstRowOfItems() {
        List<MenuObjectCollection> firstRow = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int x = (i * (itemWidth + horizontalSpacing)) + horizontalScreenDistance;
            int y = verticalScreenDistance;
            MenuObjectCollection item = new MenuObjectCollection(x, y, imageScale, "Item " + (i + 1), MenuObjectEnums.CommonItem, MenuFunctionEnums.PurchaseItem);
            if (shopManager.getRowsUnlocked() < 1) {
                item.lockItemInShop();
                item.getMenuItemInformation().setItemDescription("Play a level of atleast difficulty 2 to unlock this row");
            }
            firstRow.add(item);
        }
        return firstRow;
    }

    public List<MenuObjectCollection> createSecondRowOfItems() {
        List<MenuObjectCollection> secondRow = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int x = (i * (itemWidth + horizontalSpacing)) + horizontalScreenDistance;
            int y = (1 * (itemHeight + verticalSpacing)) + verticalScreenDistance;
            MenuObjectEnums type = (i == 6 || i == 7) ? MenuObjectEnums.RareItem : MenuObjectEnums.CommonItem; // If index is 6 or 7, it's a rare item
            MenuObjectCollection item = new MenuObjectCollection(x, y, imageScale, "Item " + (i + 11), type, MenuFunctionEnums.PurchaseItem);
            item.getMenuItemInformation().setCost(item.getMenuItemInformation().getCost() * 1.25f);
            if (shopManager.getRowsUnlocked() < 2) {
                item.lockItemInShop();
                item.getMenuItemInformation().setItemDescription("Play a level of atleast difficulty 4 to unlock this row");
            }
            secondRow.add(item);
        }
        return secondRow;
    }

    public List<MenuObjectCollection> createThirdRowOfItems() {
        List<MenuObjectCollection> thirdRow = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int x = (i * (itemWidth + horizontalSpacing)) + horizontalScreenDistance;
            int y = (2 * (itemHeight + verticalSpacing)) + verticalScreenDistance;
            MenuObjectEnums type = (i == 6 || i == 7) ? MenuObjectEnums.RareItem : MenuObjectEnums.CommonItem; // If index is 6 or 7, it's a rare item
            MenuObjectCollection item = new MenuObjectCollection(x, y, imageScale, "Item " + (i + 21), type, MenuFunctionEnums.PurchaseItem);
            item.getMenuItemInformation().setCost(item.getMenuItemInformation().getCost() * 1.5f);
            if (shopManager.getRowsUnlocked() < 3) {
                item.lockItemInShop();
                item.getMenuItemInformation().setItemDescription("Play a level of atleast difficulty 6 to unlock this row");
            }
            thirdRow.add(item);
        }
        return thirdRow;
    }

    public MenuObjectCollection createItemRowsBackgroundCard() {
        float widthRatio = 1200 / 1440f;
        float heightRatio = 550f / 875f;
        int cardWidth = (int) (boardWidth * widthRatio);
        int cardHeight = (int) (boardHeight * heightRatio);
        MenuObjectCollection itemRowsBackgroundCard = new MenuObjectCollection(-20, -20, 1, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);
        itemRowsBackgroundCard.getMenuImages().get(0).setImageDimensions(cardWidth, cardHeight);
        return itemRowsBackgroundCard;
    }

    public MenuObjectCollection createSongDifficultyBackgroundCard() {
        float widthRatio = 450 / 1440f;
        float heightRatio = 200f / 875f;
        int cardWidth = (int) (boardWidth * widthRatio);
        int cardHeight = (int) (boardHeight * heightRatio);
        MenuObjectCollection songDifficultyBackgroundCard = new MenuObjectCollection(30, fourthRowY - 20, 1, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);
        songDifficultyBackgroundCard.getMenuImages().get(0).setImageDimensions(cardWidth, cardHeight);
        return songDifficultyBackgroundCard;
    }

    public MenuObjectCollection createSongLengthBackgroundCard() {
        float widthRatio = 450 / 1440f;
        float heightRatio = 200f / 875f;
        float xCoordPaddingRatio = 50 / 1440f;
        int cardWidth = (int) (boardWidth * widthRatio);
        int cardHeight = (int) (boardHeight * heightRatio);
        int xCoordPadding = (int) (boardWidth * xCoordPaddingRatio);
        int xCoord = (3 * (itemWidth + horizontalSpacing)) + (horizontalScreenDistance * 2);
        MenuObjectCollection songLengthBackgroundCard = new MenuObjectCollection(xCoord - xCoordPadding, fourthRowY - 20, 1, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);
        songLengthBackgroundCard.getMenuImages().get(0).setImageDimensions(cardWidth, cardHeight);
        return songLengthBackgroundCard;
    }

    public MenuObjectCollection createDescriptionRowsBackgroundCard() {
        float widthRatio = 400 / 1440f;
        float heightRatio = 300 / 875f;
        int cardWidth = (int) (boardWidth * widthRatio);
        int cardHeight = (int) (boardHeight * heightRatio);
        int xCoord = boardWidth / 2 + (boardWidth / 5);
        MenuObjectCollection descriptionRowsBackgroundCard = new MenuObjectCollection(xCoord, fourthRowY - 20, 1, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);
        descriptionRowsBackgroundCard.getMenuImages().get(0).setImageDimensions(cardWidth, cardHeight);
        return descriptionRowsBackgroundCard;
    }

    public MenuObjectCollection createInventoryBackgroundCard() {
        float widthRatio = 800 / 1440f;
        float heightRatio = 500 / 875f;
        int cardWidth = (int) (boardWidth * widthRatio);
        int cardHeight = (int) (boardHeight * heightRatio);
        MenuObjectCollection inventoryBackgroundCard = new MenuObjectCollection(boardWidth / 6, boardHeight / 2 - (boardHeight / 4), 1, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);
        inventoryBackgroundCard.getMenuImages().get(0).setImageDimensions(cardWidth, cardHeight);
        return inventoryBackgroundCard;
    }

    public MenuObjectCollection createNextLevelDifficultyBackground (){
        int xCoordinate = Math.round(boardWidth - (boardWidth * 0.2f));
        int yCoordinate = 20;


        MenuObjectCollection backgroundCard = new MenuObjectCollection(
                xCoordinate, yCoordinate, 0.3f, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);


        return backgroundCard;
    }

    public MenuObjectCollection createSelectEasyDifficulty(MenuObjectCollection songDifficultyBackgroundCard){
        int y = fourthRowY + 40;
        int x0 = songDifficultyBackgroundCard.getXCoordinate() + songDifficultyBackgroundCard.getMenuImages().get(0).getWidth() / 6;
        MenuObjectCollection difficultyEasy = new MenuObjectCollection(x0, fourthRowY + 40, imageScale, LevelDifficulty.Easy.toString().toUpperCase(), MenuObjectEnums.Song_Difficulty_Selector, MenuFunctionEnums.SelectSongDifficulty);
        difficultyEasy.setLevelDifficulty(LevelDifficulty.Easy);
        difficultyEasy.setNewImage(ImageEnums.BlueWings1);
        difficultyEasy.setCenterCoordinates(x0, y);
        difficultyEasy.setText("Sets the difficulty of the next level to EASY, increases the difficulty score by 1");
        return difficultyEasy;
    }

    public MenuObjectCollection createSelectMediumDifficulty(MenuObjectCollection songDifficultyBackgroundCard){
        int y = fourthRowY + 40;
        int x1 = itemWidth + horizontalSpacing + songDifficultyBackgroundCard.getXCoordinate() + songDifficultyBackgroundCard.getMenuImages().get(0).getWidth() / 6;
        MenuObjectCollection difficultyMedium = new MenuObjectCollection(x1, fourthRowY + 40, imageScale, LevelDifficulty.Medium.toString().toUpperCase(), MenuObjectEnums.Song_Difficulty_Selector, MenuFunctionEnums.SelectSongDifficulty);
        difficultyMedium.setLevelDifficulty(LevelDifficulty.Medium);
        difficultyMedium.setNewImage(ImageEnums.BlueWings3);
        difficultyMedium.setCenterCoordinates(x1, y);
        difficultyMedium.setText("Sets the difficulty of the next level to MEDIUM, increases the difficulty score by 2");
        return difficultyMedium;
    }

    public MenuObjectCollection createSelectHardDifficulty(MenuObjectCollection songDifficultyBackgroundCard){
        int y = fourthRowY + 40;
        int x2 = 2 * (itemWidth + horizontalSpacing) + songDifficultyBackgroundCard.getXCoordinate() + songDifficultyBackgroundCard.getMenuImages().get(0).getWidth() / 6;
        MenuObjectCollection difficultyHard = new MenuObjectCollection(x2, fourthRowY + 40, imageScale, LevelDifficulty.Hard.toString().toUpperCase(), MenuObjectEnums.Song_Difficulty_Selector, MenuFunctionEnums.SelectSongDifficulty);
        difficultyHard.setLevelDifficulty(LevelDifficulty.Hard);
        difficultyHard.setNewImage(ImageEnums.BlueWings5);
        difficultyHard.setCenterCoordinates(x2, y);
        difficultyHard.setText("Sets the difficulty of the next level to HARD, increases the difficulty score by 3");
        return difficultyHard;
    }

    public MenuObjectCollection createSelectDifficultyText(MenuObjectCollection songDifficultyBackgroundCard, MenuObjectCollection difficultyHard){
        int textX = songDifficultyBackgroundCard.getXCoordinate() + (songDifficultyBackgroundCard.getMenuImages().get(0).getWidth() / 3);
        int textY = difficultyHard.getYCoordinate() + difficultyHard.getMenuImages().get(0).getHeight();
       return new MenuObjectCollection(textX, textY, imageScale, "SELECT DIFFICULTY", MenuObjectEnums.Text, MenuFunctionEnums.NONE);
    }

    public MenuObjectCollection createShortSongSelection(MenuObjectCollection songLengthBackgroundCard){
        int y = fourthRowY + 40;
        int x0 = songLengthBackgroundCard.getXCoordinate() + songLengthBackgroundCard.getMenuImages().get(0).getWidth() / 6;
        MenuObjectCollection songLengthShort = new MenuObjectCollection(x0, fourthRowY + 20, imageScale, LevelLength.Short.toString().toUpperCase(), MenuObjectEnums.Song_Length_Selector, MenuFunctionEnums.SelectSongLength);
        songLengthShort.setLevelLength(LevelLength.Short);
        songLengthShort.setNewImage(ImageEnums.YellowWings1);
        songLengthShort.setCenterCoordinates(x0, y);
        songLengthShort.setText("Sets the length of the next song between 0 and 3 minutes, increases the difficulty score by 1");
        return songLengthShort;
    }

    public MenuObjectCollection createMediumSongSelection(MenuObjectCollection songLengthBackgroundCard){
        int y = fourthRowY + 40;
        int x1 = songLengthBackgroundCard.getXCoordinate() + songLengthBackgroundCard.getMenuImages().get(0).getWidth() / 6 + (itemWidth + horizontalSpacing);
        MenuObjectCollection songLengthMedium = new MenuObjectCollection(x1, fourthRowY + 20, imageScale, LevelLength.Medium.toString().toUpperCase(), MenuObjectEnums.Song_Length_Selector, MenuFunctionEnums.SelectSongLength);
        songLengthMedium.setLevelLength(LevelLength.Medium);
        songLengthMedium.setNewImage(ImageEnums.YellowWings3);
        songLengthMedium.setCenterCoordinates(x1, y);
        songLengthMedium.setText("Sets the length of the next song between 3 and 5 minutes, increases the difficulty score by 2");
        return songLengthMedium;
    }

    public MenuObjectCollection createLongSongSelection(MenuObjectCollection songLengthBackgroundCard){
        int y = fourthRowY + 40;
        int x2 = songLengthBackgroundCard.getXCoordinate() + songLengthBackgroundCard.getMenuImages().get(0).getWidth() / 6 + 2 * (itemWidth + horizontalSpacing);
        MenuObjectCollection songLengthLong = new MenuObjectCollection(x2, fourthRowY + 20, imageScale, LevelLength.Long.toString().toUpperCase(), MenuObjectEnums.Song_Length_Selector, MenuFunctionEnums.SelectSongLength);
        songLengthLong.setLevelLength(LevelLength.Long);
        songLengthLong.setNewImage(ImageEnums.YellowWings5);
        songLengthLong.setCenterCoordinates(x2, y);
        songLengthLong.setText("Sets the length of the next song between 5 or more minutes, increases the difficulty score by 3");
        return songLengthLong;
    }

    public MenuObjectCollection createSongSelectionText(MenuObjectCollection songLengthBackgroundCard, MenuObjectCollection songLengthLong){
        int textX = songLengthBackgroundCard.getXCoordinate() + (songLengthBackgroundCard.getMenuImages().get(0).getWidth() / 3);
        int textY = songLengthLong.getYCoordinate() + songLengthLong.getMenuImages().get(0).getHeight();
        return new MenuObjectCollection(textX, textY, imageScale, "SELECT LENGTH", MenuObjectEnums.Text, MenuFunctionEnums.NONE);
    }

    public MenuObjectCollection createDescriptionBox(){
        int y = fourthRowY;
        int x = boardWidth / 2 + (boardWidth / 5);
        return new MenuObjectCollection(x, y, imageScale, "", MenuObjectEnums.Text, MenuFunctionEnums.NONE);
    }

}
