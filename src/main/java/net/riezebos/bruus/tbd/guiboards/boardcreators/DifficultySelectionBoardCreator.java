package net.riezebos.bruus.tbd.guiboards.boardcreators;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.MiniBossConfig;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.guicomponents.*;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class DifficultySelectionBoardCreator {

    public static MenuCursor createCursor(GUIComponent comp) {
        int initCursorX = comp.getXCoordinate();
        int initCursorY = comp.getCenterYCoordinate();
        float scale = 1 * DataClass.getInstance().getResolutionFactor();
        ImageEnums imageEnums = PlayerStats.getInstance().getSpaceShipImage();
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(initCursorX, initCursorY, scale, imageEnums);
        MenuCursor button = new MenuCursor(spriteConfiguration);
        return button;
    }

    //helper method
    private static SpriteConfiguration createSpriteConfiguration(float xCoordinate, float yCoordinate, float scale, ImageEnums imageType) {
        SpriteConfiguration config = new SpriteConfiguration();
        config.setxCoordinate(xCoordinate);
        config.setyCoordinate(yCoordinate);
        config.setScale(scale);
        config.setImageType(imageType);
        return config;
    }

    public static DisplayOnly createDifficultySelectionBackgroundCard() {
        float xCoordinate = DataClass.getInstance().getWindowWidth() * 0.05f;
        float yCoordinate = DataClass.getInstance().getWindowHeight() * 0.35f;
        int cardWidth = 600;
        int cardHeight = 225;

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                xCoordinate,
                yCoordinate,
                1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);
        return backgroundCard;
    }


    public static DisplayOnly createMiniBossSelectionBackgroundCard() {
        float xCoordinate = DataClass.getInstance().getWindowWidth() * 0.4f;
        float yCoordinate = DataClass.getInstance().getWindowHeight() * 0.35f;
        int cardWidth = 600;
        int cardHeight = 225;

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                xCoordinate,
                yCoordinate,
                1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);
        return backgroundCard;
    }

    public static GUITextCollection createTitleText(GUIComponent backgroundCard, String text) {
        float textX = backgroundCard.getCenterXCoordinate();
        float textY = backgroundCard.getYCoordinate() + Math.round(backgroundCard.getHeight() * 0.15f);
        GUITextCollection textCollection = new GUITextCollection(textX, textY, text);
        textCollection.setScale(1.75f * DataClass.getInstance().getResolutionFactor());
        textCollection.setCenterXCoordinate(backgroundCard.getCenterXCoordinate());
        return textCollection;
    }

    public static MenuButton createPirateSelection(GUIComponent backgroundCard) {
        float y = backgroundCard.getCenterYCoordinate();
        float x0 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.20f;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x0,
                y,
                1.25f, ImageEnums.BlueWings1);
        MenuButton selectEasyDifficulty = new MenuButton(spriteConfiguration);
        selectEasyDifficulty.setLevelDifficulty(LevelDifficulty.Easy);
        selectEasyDifficulty.setMenuFunctionality(MenuFunctionEnums.SelectSongDifficulty);
        selectEasyDifficulty.setCenterCoordinates(x0, y);
        int amount = 1;
        selectEasyDifficulty.setDescriptionOfComponent("Fight space pirates. Adds " + amount + " to the difficulty score. Space pirates have fragile ships and the bounty on their head is worth a few minerals.");
        return selectEasyDifficulty;
    }

    public static MenuButton createZergSelection(GUIComponent backgroundCard) {
        int y = backgroundCard.getCenterYCoordinate();
        float x1 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.45f;
        ImageEnums iconEnum = ImageEnums.BlueWings3;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x1,
                y,
                1.25f, iconEnum);
        MenuButton selectEasyDifficulty = new MenuButton(spriteConfiguration);
        selectEasyDifficulty.setLevelDifficulty(LevelDifficulty.Medium);
        selectEasyDifficulty.setMenuFunctionality(MenuFunctionEnums.SelectSongDifficulty);
        selectEasyDifficulty.setCenterCoordinates(x1, y);

        int amount = 2;
        selectEasyDifficulty.setDescriptionOfComponent("Fight the Zerg. Adds " + amount + " to the difficulty score. Zergs are a considerable foe, you might be overwhelmed before you realize it but many people happily pay minerals for culling their numbers.");
        return selectEasyDifficulty;
    }

    public static MenuButton createRoyalGuardSelection(GUIComponent backgroundCard) {
        int y = backgroundCard.getCenterYCoordinate();
        float x2 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.75f;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x2,
                y,
                1.25f, ImageEnums.BlueWings5);
        MenuButton selectEasyDifficulty = new MenuButton(spriteConfiguration);
        selectEasyDifficulty.setLevelDifficulty(LevelDifficulty.Hard);
        selectEasyDifficulty.setMenuFunctionality(MenuFunctionEnums.SelectSongDifficulty);
        selectEasyDifficulty.setCenterCoordinates(x2, y);

        int amount = 3;

        selectEasyDifficulty.setDescriptionOfComponent("Fight the Royal Guard. Adds " + amount + " to the difficulty score. Braving the Royal Guard is a dangerous task, but the reward is worth it if you are strong and skilled enough.");
        return selectEasyDifficulty;
    }

    public static GUIComponent createNoMiniBossSelection(GUIComponent backgroundCard) {
        float y = backgroundCard.getCenterYCoordinate();
        float x0 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.20f;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x0,
                y,
                1.25f, ImageEnums.BlueWings1);
        MenuButton button = new MenuButton(spriteConfiguration);
        button.setMiniBossConfig(MiniBossConfig.Easy);
        button.setMenuFunctionality(MenuFunctionEnums.SelectSongLength);
        button.setCenterCoordinates(x0, y);
        button.setDescriptionOfComponent("Spawns no minibosses during the next level. Increases the difficulty score by 1");
        return button;
    }


    public static GUIComponent createOneMiniBossSelection(GUIComponent backgroundCard) {
        float y = backgroundCard.getCenterYCoordinate();
        float x1 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.45f;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x1,
                y,
                1.25f, ImageEnums.BlueWings3);
        MenuButton button = new MenuButton(spriteConfiguration);
        button.setMiniBossConfig(MiniBossConfig.Medium);
        button.setMenuFunctionality(MenuFunctionEnums.SelectSongLength);
        button.setCenterCoordinates(x1, y);
        button.setDescriptionOfComponent("Spawns 1 miniboss during the next level. Increases the difficulty score by 2");
        return button;
    }


    public static GUIComponent createTwoMiniBossSelection(GUIComponent backgroundCard) {
        float y = backgroundCard.getCenterYCoordinate();
        float x2 = backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.75f;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                x2,
                y,
                1.25f, ImageEnums.BlueWings5);
        MenuButton button = new MenuButton(spriteConfiguration);
        button.setMiniBossConfig(MiniBossConfig.Hard);
        button.setMenuFunctionality(MenuFunctionEnums.SelectSongLength);
        button.setCenterCoordinates(x2, y);
        button.setDescriptionOfComponent("Spawns 2 minibosses during the next level. increases the difficulty score by 3");
        return button;
    }

    public static DisplayOnly createTotalDifficultyBackgroundCard() {
        float xCoordinate = DataClass.getInstance().getWindowWidth() * 0.5f;
        float yCoordinate = DataClass.getInstance().getWindowHeight() * 0.8f;
        int cardWidth = 500;
        int cardHeight = 250;

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                xCoordinate,
                yCoordinate,
                1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);
        backgroundCard.setCenterCoordinates(xCoordinate, yCoordinate);
        return backgroundCard;
    }

    public static GUITextCollection createNextLevelDifficultyIcon(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int YCoordinate = backgroundCard.getCenterYCoordinate() + Math.round(backgroundCard.getHeight() * 0.35f);
        int difficulty = LevelManager.getInstance().getDifficultyScore();
        ImageEnums  iconEnum = LevelManager.getInstance().getImageEnumByDifficultyScore(difficulty);
        String  string = "NEXT DIFFICULTY: " + difficulty;

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

    public static GUIComponent createDescriptionBoxBackgroundCard() {
        float xCoordinate = DataClass.getInstance().getWindowWidth() * 0.75f;
        float yCoordinate = DataClass.getInstance().getWindowHeight() * 0.32f;
        int cardWidth = 340;
        int cardHeight = 400;

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(
                xCoordinate,
                yCoordinate,
                1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);
        return backgroundCard;
    }

    public static GUIComponent createReturnToMainMenuBackgroundCard() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.0694f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.84f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Wide_Card);
        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);

        int newWidth = Math.round(DataClass.getInstance().getResolutionFactor() * 200);
        int newHeight = Math.round(DataClass.getInstance().getResolutionFactor() * 70);
        backgroundCard.setImageDimensions(newWidth, newHeight);

        return backgroundCard;
    }

    public static GUITextCollection createReturn(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int yCoordinate = backgroundCard.getCenterYCoordinate() - Math.round(DataClass.getInstance().getResolutionFactor() * 10);

        String text = "RETURN";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(1.5f * DataClass.getInstance().getResolutionFactor());
        textCollection.setStartingXCoordinate(xCoordinate - (textCollection.getWidth() / 2));
        textCollection.setMenuFunctionality(MenuFunctionEnums.OpenClassSelectWindow);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Return to class selection.");
        return textCollection;
    }

    public static GUIComponent createStartGameButtonBackground() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.785f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.84f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Wide_Card);
        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);

        int newWidth = Math.round(DataClass.getInstance().getResolutionFactor() * 240);
        int newHeight = Math.round(DataClass.getInstance().getResolutionFactor() * 70);
        backgroundCard.setImageDimensions(newWidth, newHeight);

        return backgroundCard;
    }

    public static GUITextCollection createStartGameButton(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int yCoordinate = backgroundCard.getCenterYCoordinate() - Math.round(DataClass.getInstance().getResolutionFactor() * 10);

        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, "START GAME");
        textCollection.setScale(1.5f * DataClass.getInstance().getResolutionFactor());
        textCollection.setStartingXCoordinate(xCoordinate - (textCollection.getWidth() / 2));
        textCollection.setMenuFunctionality(MenuFunctionEnums.Start_Game);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Start the game.");
        return textCollection;
    }

    public static GUIComponent createChoosDifficultyText() {
        int xCoordinate = DataClass.getInstance().getWindowWidth() / 2;
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.15f);

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(0.8f * DataClass.getInstance().getResolutionFactor());
        spriteConfiguration.setImageType(ImageEnums.ChooseDifficulty);

        GUIComponent titleImage = new DisplayOnly(spriteConfiguration);
        titleImage.setDescriptionOfComponent("Title Image");
        titleImage.setCenterCoordinates(xCoordinate, yCoordinate);
        return titleImage;
    }
}
