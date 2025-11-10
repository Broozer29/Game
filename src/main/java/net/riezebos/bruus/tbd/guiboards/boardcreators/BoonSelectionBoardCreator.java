package net.riezebos.bruus.tbd.guiboards.boardcreators;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.Boon;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.defensive.ThickHide;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility.*;
import net.riezebos.bruus.tbd.game.gamestate.GameMode;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.guicomponents.*;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class BoonSelectionBoardCreator {

    private static int columnWidth = 300;
    private static float boonTextScale = 1.2f;
    private static float yDistanceBetweenButtons = 1.8f;
    private static float upgradeBoonButtonWidthScale = 0.8f;


    public static GUIComponent createSelectUpgradesTitleCard() {
        int xCoordinate = DataClass.getInstance().getWindowWidth() / 2;
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.1f);

        DisplayOnly titleCard = new DisplayOnly(createSpriteConfig(xCoordinate, yCoordinate, 0.5f, ImageEnums.SelectBoons));
        titleCard.setCenterCoordinates(xCoordinate, yCoordinate);
        return titleCard;
    }

    public static MenuCursor createCursor(GUIComponent guiComponent) {
        int initCursorX = guiComponent.getXCoordinate();
        int initCursorY = guiComponent.getYCoordinate();
        float scale = DataClass.getInstance().getResolutionFactor();
        SpriteConfiguration spriteConfiguration = createSpriteConfig(initCursorX, initCursorY, scale, PlayerStats.getInstance().getSpaceShipImage());
        MenuCursor button = new MenuCursor(spriteConfiguration);
        return button;
    }

    public static GUITextCollection createEmeraldText(){
        int xCoordinate = DataClass.getInstance().getWindowWidth() / 2;
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.75f);
        String rerollCost = "EMERALDS AVAILABLE: " + PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds();

        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, rerollCost);
        textCollection.setScale(2f * DataClass.getInstance().getResolutionFactor());
        textCollection.setCenterXCoordinate(xCoordinate);

        int iconXCoordinate = textCollection.getComponents().get(0).getXCoordinate() + textCollection.getWidth();
        SpriteConfiguration spriteConfiguration = createSpriteConfig(iconXCoordinate, yCoordinate, 0.75f * DataClass.getInstance().getResolutionFactor(), ImageEnums.EmeraldGem5);
        DisplayOnly icon = new DisplayOnly(spriteConfiguration);
        icon.setCenterYCoordinate(yCoordinate + textCollection.getHeight() / 2);
//        icon.setYCoordinate(icon.getYCoordinate() - (icon.getHeight() / 4));
        textCollection.addComponentToCollection(icon);
        return textCollection;
    }

    public static GUIComponent createFirstColumnBackgroundCard() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.05f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.25f);
        ImageEnums imageEnums = ImageEnums.Long_Card;
        float scale = 1 * DataClass.getInstance().getResolutionFactor();

        SpriteConfiguration spriteconfiguration = createSpriteConfig(xCoordinate, yCoordinate, scale, imageEnums);
        GUIComponent guicomponent = new GUIComponent(spriteconfiguration);
        int width = Math.round(columnWidth * DataClass.getInstance().getResolutionFactor());
        int height = Math.round(400 * DataClass.getInstance().getResolutionFactor());

        guicomponent.setImageDimensions(width, height);
        return guicomponent;
    }

    public static GUITextCollection createFirstColumnTitle(GUIComponent backgroundCard) {
        int yCoordinate = Math.round(backgroundCard.getYCoordinate() - 20 * DataClass.getInstance().getResolutionFactor());
        GUITextCollection guiTextCollection = new GUITextCollection(0, yCoordinate, "BOONS");
        guiTextCollection.setScale(1.7f * DataClass.getInstance().getResolutionFactor());
        guiTextCollection.setCenterXCoordinate(backgroundCard.getCenterXCoordinate());
        return guiTextCollection;
    }


    public static GUITextCollection createThirdColumnTitle(GUIComponent backgroundCard) {
        int yCoordinate = Math.round(backgroundCard.getYCoordinate() - 20 * DataClass.getInstance().getResolutionFactor());
        GUITextCollection guiTextCollection = new GUITextCollection(0, yCoordinate, "GAME MODIFIERS");
        guiTextCollection.setScale(1.7f * DataClass.getInstance().getResolutionFactor());
        guiTextCollection.setCenterXCoordinate(backgroundCard.getCenterXCoordinate());
        return guiTextCollection;
    }



    public static GUITextCollection createFifthColumnTitle(GUIComponent backgroundCard) {
        int yCoordinate = Math.round(backgroundCard.getYCoordinate() - 20 * DataClass.getInstance().getResolutionFactor());
        GUITextCollection guiTextCollection = new GUITextCollection(0, yCoordinate, "OFFENSE");
        guiTextCollection.setScale(1.7f * DataClass.getInstance().getResolutionFactor());
        guiTextCollection.setCenterXCoordinate(backgroundCard.getCenterXCoordinate());
        return guiTextCollection;
    }

    public static GUIComponent createThirdColumnBackgroundCard() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.275f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.25f);
        ImageEnums imageEnums = ImageEnums.Long_Card;
        float scale = 1 * DataClass.getInstance().getResolutionFactor();

        SpriteConfiguration spriteconfiguration = createSpriteConfig(xCoordinate, yCoordinate, scale, imageEnums);
        GUIComponent guicomponent = new GUIComponent(spriteconfiguration);
        int width = Math.round(columnWidth * DataClass.getInstance().getResolutionFactor());
        int height = Math.round(400 * DataClass.getInstance().getResolutionFactor());

        guicomponent.setImageDimensions(width, height);
        return guicomponent;
    }

    public static GUIComponent createFifthColumnBackgroundCard() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.5f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.25f);
        ImageEnums imageEnums = ImageEnums.Long_Card;
        float scale = 1 * DataClass.getInstance().getResolutionFactor();

        SpriteConfiguration spriteconfiguration = createSpriteConfig(xCoordinate, yCoordinate, scale, imageEnums);
        GUIComponent guicomponent = new GUIComponent(spriteconfiguration);
        int width = Math.round(columnWidth * DataClass.getInstance().getResolutionFactor());
        int height = Math.round(400 * DataClass.getInstance().getResolutionFactor());

        guicomponent.setImageDimensions(width, height);
        return guicomponent;
    }

    public static GUIComponent createDescriptionBackgroundCard() {
        float widthRatio = 350 / 1440f;
        float heightRatio = 300 / 875f;
        int cardWidth = Math.round((DataClass.getInstance().getWindowWidth() * widthRatio));
        int cardHeight = Math.round((DataClass.getInstance().getWindowHeight() * heightRatio));
        int xCoord = Math.round(DataClass.getInstance().getWindowWidth() * 0.75f);
        int yCoord = Math.round(DataClass.getInstance().getWindowHeight() * 0.25f);

        SpriteConfiguration spriteConfiguration = createSpriteConfig(
                xCoord,
                yCoord,
                1, ImageEnums.Square_Card);
        DisplayOnly backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(cardWidth, cardHeight);

        return backgroundCard;
    }

    public static GUIComponent createDescriptionComponent(GUIComponent backgroundCard) {
        int y = backgroundCard.getYCoordinate();
        int x = backgroundCard.getXCoordinate();
        SpriteConfiguration spriteConfiguration = createSpriteConfig(
                x,
                y,
                backgroundCard.getScale(), ImageEnums.Square_Card);
        return new DisplayOnly(spriteConfiguration);
    }

    public static GUIComponent createReturnToSelectionBackgroundCard() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.0694f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.84f);
        ImageEnums imageEnums = ImageEnums.Wide_Card;
        float scale = 1 * DataClass.getInstance().getResolutionFactor();

        SpriteConfiguration spriteconfiguration = createSpriteConfig(xCoordinate, yCoordinate, scale, imageEnums);
        GUIComponent guicomponent = new GUIComponent(spriteconfiguration);
        int width = Math.round(300 * DataClass.getInstance().getResolutionFactor());
        int height = Math.round(70 * DataClass.getInstance().getResolutionFactor());

        guicomponent.setImageDimensions(width, height);
        return guicomponent;
    }

    public static GUIComponent createStartGameBackgroundCard() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.785f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.84f);
        ImageEnums imageEnums = ImageEnums.Wide_Card;
        float scale = 1 * DataClass.getInstance().getResolutionFactor();

        SpriteConfiguration spriteconfiguration = createSpriteConfig(xCoordinate, yCoordinate, scale, imageEnums);
        GUIComponent guicomponent = new GUIComponent(spriteconfiguration);
        int width = Math.round(150 * DataClass.getInstance().getResolutionFactor());
        int height = Math.round(70 * DataClass.getInstance().getResolutionFactor());

        guicomponent.setImageDimensions(width, height);
        return guicomponent;
    }

    public static GUITextCollection createReturnToClassSelectionButton(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int yCoordinate = backgroundCard.getCenterYCoordinate() - Math.round(5 * DataClass.getInstance().getResolutionFactor());
        String text = "RETURN TO CLASS SELECTION";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(DataClass.getInstance().getResolutionFactor());
        xCoordinate = backgroundCard.getCenterXCoordinate() - (textCollection.getWidth() / 2);
        textCollection.setStartingXCoordinate(xCoordinate);
        textCollection.setMenuFunctionality(MenuFunctionEnums.OpenClassSelectWindow);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Return to class selection");
        return textCollection;
    }

    public static GUITextCollection createStartGameButton(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int yCoordinate = backgroundCard.getCenterYCoordinate() - Math.round(5 * DataClass.getInstance().getResolutionFactor());
        String text = "START GAME";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(DataClass.getInstance().getResolutionFactor());
        xCoordinate = backgroundCard.getCenterXCoordinate() - (textCollection.getWidth() / 2);
        textCollection.setStartingXCoordinate(xCoordinate);
        textCollection.setMenuFunctionality(MenuFunctionEnums.Start_Game);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Start the game");
        return textCollection;
    }

    //Helper method
    private static SpriteConfiguration createSpriteConfig(int xCoordinate, int yCoordinate, float scale, ImageEnums imageEnums) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(scale);
        spriteConfiguration.setImageType(imageEnums);
        return spriteConfiguration;
    }



    public static GUITextCollection createSelectDefaultButton(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getXCoordinate() + Math.round(backgroundCard.getWidth() * 0.1f);
        int yCoordinate = backgroundCard.getYCoordinate() + Math.round(backgroundCard.getHeight() * 0.1f);

        String text = "NORMAL";

        GUITextCollection selectDefaultButton = new GUITextCollection(xCoordinate, yCoordinate, text);
        selectDefaultButton.setScale(boonTextScale * DataClass.getInstance().getResolutionFactor());
        selectDefaultButton.setMenuFunctionality(MenuFunctionEnums.SelectDefaultGameMode);
        return selectDefaultButton;
    }

    public static GUITextCollection createOtherGameModeButton(GUIComponent button, GameMode gameMode) {
        int xCoordinate = button.getXCoordinate();
        int yCoordinate = button.getYCoordinate() + Math.round(button.getHeight() * yDistanceBetweenButtons);

        String text = gameMode.getName().toUpperCase();

        GUITextCollection selectBoonButton = new GUITextCollection(xCoordinate, yCoordinate, text);
        selectBoonButton.setScale(boonTextScale * DataClass.getInstance().getResolutionFactor());
        switch (gameMode){
            case Default -> selectBoonButton.setMenuFunctionality(MenuFunctionEnums.SelectDefaultGameMode);
            case ManMode -> selectBoonButton.setMenuFunctionality(MenuFunctionEnums.SelectManModeGameMode);
            case MonoCultural -> selectBoonButton.setMenuFunctionality(MenuFunctionEnums.SelectMonoCulturalGameMode);
            case DoubleTrouble -> selectBoonButton.setMenuFunctionality(MenuFunctionEnums.SelectDoubleTroubleGameMode);
            case Formatted -> selectBoonButton.setMenuFunctionality(MenuFunctionEnums.SelectFormattedGameMode);
        }
        return selectBoonButton;
    }



    public static GUITextCollection createNepotismSelectionButton(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getXCoordinate() + Math.round(backgroundCard.getWidth() * 0.1f);
        int yCoordinate = backgroundCard.getYCoordinate() + Math.round(backgroundCard.getHeight() * 0.1f);

        String text = "LOCKED";

        if (Nepotism.getInstance().isUnlocked()) {
            text = Nepotism.getInstance().getBoonName().toUpperCase();
        }

        GUITextCollection nepotismSelectionButton = new GUITextCollection(xCoordinate, yCoordinate, text);
        nepotismSelectionButton.setScale(boonTextScale * DataClass.getInstance().getResolutionFactor());
        nepotismSelectionButton.setMenuFunctionality(MenuFunctionEnums.SelectNepotism);
        return nepotismSelectionButton;
    }

    public static MenuButton createUpgradeBoonButton(GUIComponent backgroundCard, GUITextCollection nepotismSelectionButton, MenuFunctionEnums menuFunction) {
        GUIComponent lastComponent = nepotismSelectionButton.getComponents().get(nepotismSelectionButton.getComponents().size() - 1);
        int xCoordinate = backgroundCard.getXCoordinate() + Math.round(backgroundCard.getWidth() * upgradeBoonButtonWidthScale);
        int yCoordinate = lastComponent.getCenterYCoordinate();

        ImageEnums imageType = ImageEnums.UpGrey;
        Boon boon = getBoonByMenuFunctionality(menuFunction);
        if (boon.getBoonUpgradeCost() <= PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds() &&
                boon.canUpgradeFurther() && boon.isUnlocked()) {
            imageType = ImageEnums.UpOrange;
        }
        float scale = 0.8f * DataClass.getInstance().getResolutionFactor();

        SpriteConfiguration spriteConfiguration = createSpriteConfig(xCoordinate, yCoordinate, scale, imageType);
        MenuButton upgradeButton = new MenuButton(spriteConfiguration);
        upgradeButton.setCenterYCoordinate(yCoordinate);
        upgradeButton.setMenuFunctionality(menuFunction);
        return upgradeButton;
    }


    //Apart from the first boon since the placement of the first button is not based on other buttons
    public static GUITextCollection createOtherBoonButtons(GUIComponent button, Boon boon) {
        int xCoordinate = button.getXCoordinate();
        int yCoordinate = button.getYCoordinate() + Math.round(button.getHeight() * yDistanceBetweenButtons);

        String text = "LOCKED";

        if (boon.isUnlocked()) {
            text = boon.getBoonName().toUpperCase();
        }

        GUITextCollection selectBoonButton = new GUITextCollection(xCoordinate, yCoordinate, text);
        selectBoonButton.setScale(boonTextScale * DataClass.getInstance().getResolutionFactor());
        selectBoonButton.setMenuFunctionality(boon.getSelectBoonMenuFunctionEnum());
        return selectBoonButton;
    }

    public static GUITextCollection createWorkInProgressText(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getXCoordinate() + Math.round(backgroundCard.getWidth() * 0.15f);
        int yCoordinate = backgroundCard.getYCoordinate() + Math.round(backgroundCard.getHeight() * 0.1f);

        String text = "GAME MODIFIERS";

        GUITextCollection nepotismSelectionButton = new GUITextCollection(xCoordinate, yCoordinate, text);
        nepotismSelectionButton.setScale(boonTextScale * DataClass.getInstance().getResolutionFactor());
        return nepotismSelectionButton;
    }



    public static GUIComponent createUpgradeSelectedCheckmark() {
        int xCoordinate = 0;
        int yCoordinate = 0;
        ImageEnums imageType = ImageEnums.UpgradeSelectedCheck;
        float scale = 0.5f * DataClass.getInstance().getResolutionFactor();

        return new GUIComponent(createSpriteConfig(xCoordinate, yCoordinate, scale, imageType));
    }

    public static Boon getBoonByMenuFunctionality(MenuFunctionEnums menuFunctionality) {
        switch (menuFunctionality) {
            case SelectNepotism, UpgradeNepotism -> {
                return Nepotism.getInstance();
            }
            case SelectClubAccess, UpgradeClubAccess -> {
                return ClubAccess.getInstance();
            }
            case SelectBountyHunter, UpgradeBountyHunter -> {
                return BountyHunter.getInstance();
            }
            case SelectCompoundWealth, UpgradeCompoundWealth -> {
                return CompoundWealth.getInstance();
            }
            case SelectTreasureHunter, UpgradeTreasureHunter -> {
                return TreasureHunter.getInstance();
            }
            case SelectThickHide, UpgradeThickHide -> {
                return ThickHide.getInstance();
            }

        }
        return null;
    }

    public static GameMode getGameModeByMenuFunctionality(MenuFunctionEnums menuFunctionality) {
        switch (menuFunctionality){
            case SelectDefaultGameMode -> {return GameMode.Default;}
            case SelectManModeGameMode -> {return GameMode.ManMode;}
            case SelectMonoCulturalGameMode -> {return GameMode.MonoCultural;}
            case SelectDoubleTroubleGameMode -> {return GameMode.DoubleTrouble;}
            case SelectFormattedGameMode -> {return GameMode.Formatted;}
            default -> {return null;}
        }
    }



}
