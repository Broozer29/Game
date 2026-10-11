package net.riezebos.bruus.tbd.guiboards.boardcreators;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.guicomponents.DisplayOnly;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuCursor;
import net.riezebos.bruus.tbd.guiboards.guicomponents.ResizableCard;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class MenuBoardCreator {

    private static float resolutionFactor = Math.min(1 * DataClass.getInstance().getResolutionFactor(), DataClass.maxResolutionFactor) ;

    public static GUITextCollection createStartGameButton(GUIComponent backgroundCard){
        int xCoordinate = backgroundCard.getXCoordinate() + 120;
        int yCoordinate = backgroundCard.getYCoordinate() + 50;
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, "SELECT CLASS");
        textCollection.setScale(resolutionFactor);
        textCollection.setMenuFunctionality(MenuFunctionEnums.OpenClassSelectWindow);

        textCollection.setCenterXCoordinate(backgroundCard.getCenterXCoordinate());
        return textCollection;
    }


    public static MenuCursor createMenuCursor(GUIComponent initialSelectedButton){
        int xCoordinate = initialSelectedButton.getXCoordinate();
        int yCoordinate = initialSelectedButton.getCenterYCoordinate();

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(resolutionFactor);
        spriteConfiguration.setImageType(PlayerStats.getInstance().getSpaceShipImage());

        MenuCursor menuCursor = new MenuCursor(spriteConfiguration);
        menuCursor.setSelectedMenuTile(initialSelectedButton);
        menuCursor.setDescriptionOfComponent("Menu Cursor");
        menuCursor.setXCoordinate(menuCursor.getXCoordinate() - menuCursor.getxDistanceToKeep());
        menuCursor.setCenterYCoordinate(menuCursor.getCenterYCoordinate());
        return menuCursor;
    }

    public static GUIComponent createTitleImage(){
        int xCoordinate = DataClass.getInstance().getWindowWidth() / 2;
        int yCoordinate = (DataClass.getInstance().getWindowHeight() / 2) - 300;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(resolutionFactor);
        spriteConfiguration.setImageType(ImageEnums.Title_Image);

        GUIComponent titleImage = new DisplayOnly(spriteConfiguration);
        titleImage.setDescriptionOfComponent("Title Image");
        titleImage.setCenterCoordinates(xCoordinate,yCoordinate);
        return titleImage;
    }

    //The card is as high as the buttons need, 50 per button plus 50 at the top and the bottom, plus 50 for the two gaps between the button groups, so the border stays the same thickness
    public static ResizableCard startGameBackgroundCard(int buttonCount){
        int xCoordinate = DataClass.getInstance().getWindowWidth() / 2;
        int yCoordinate = (DataClass.getInstance().getWindowHeight() / 2) - 50;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.Square_Card);

        int newWidth = Math.round(250 * resolutionFactor);
        //The buttons start 50 below the top and are 50 apart (unscaled), so the same 50 below the last button keeps both margins even
        int newHeight = 50 + (buttonCount - 1) * 50 + Math.round(14 * resolutionFactor) + 50 + 50;
        ResizableCard backgroundCard = new ResizableCard(spriteConfiguration, newWidth, newHeight);

        backgroundCard.setCenterXCoordinate(xCoordinate);

        return backgroundCard;
    }

    public static GUIComponent selectMusicPlayerBackgroundCard(GUIComponent startgameBackgroundCard){
        int xCoordinate = startgameBackgroundCard.getXCoordinate() + Math.round(startgameBackgroundCard.getWidth() * 1.2f);
        int yCoordinate = startgameBackgroundCard.getYCoordinate();

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(resolutionFactor);
        spriteConfiguration.setImageType(ImageEnums.Square_Card);

        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);
        int newWidth = Math.round(400 * resolutionFactor);
        int newHeight = Math.round(250 * resolutionFactor);

        backgroundCard.setImageDimensions(newWidth,newHeight);
        return backgroundCard;
    }

    public static GUITextCollection openShopButton(GUITextCollection startGameButton){
        GUIComponent component = startGameButton.getComponents().get(0);
        int xCoordinate = component.getXCoordinate();
        int yCoordinate = component.getYCoordinate() + 50;

        GUITextCollection textCollection = new GUITextCollection(xCoordinate,yCoordinate, "OPEN SHOP");
        textCollection.setScale(resolutionFactor);
        textCollection.setMenuFunctionality(MenuFunctionEnums.Open_Shop_Window);
        return textCollection;
    }

    public static GUITextCollection continueSaveFileButton(GUITextCollection openShopButton){
        GUIComponent component = openShopButton.getComponents().get(0);
        int xCoordinate = component.getXCoordinate();
        int yCoordinate = component.getYCoordinate() + 50;

        GUITextCollection textCollection = new GUITextCollection(xCoordinate,yCoordinate, "CONTINUE RUN");
        textCollection.setScale(resolutionFactor);
        textCollection.setMenuFunctionality(MenuFunctionEnums.ContinueSaveFile);
        return textCollection;
    }

    public static GUITextCollection settingsButton(GUITextCollection previousButton){
        GUIComponent component = previousButton.getComponents().get(0);
        int xCoordinate = component.getXCoordinate();
        int yCoordinate = component.getYCoordinate() + 75; // 50 plus a gap of 25 before this group

        GUITextCollection textCollection = new GUITextCollection(xCoordinate,yCoordinate, "SETTINGS");
        textCollection.setScale(resolutionFactor);
        textCollection.setMenuFunctionality(MenuFunctionEnums.Open_Settings);
        return textCollection;
    }

    public static GUITextCollection testingButton(GUITextCollection openShopButton){
        GUIComponent component = openShopButton.getComponents().get(0);
        int xCoordinate = component.getXCoordinate();
        int yCoordinate = component.getYCoordinate() + 75; // 50 plus a gap of 25 before this group

        GUITextCollection textCollection = new GUITextCollection(xCoordinate,yCoordinate, "CLOSE GAME");
        textCollection.setScale(resolutionFactor);
        textCollection.setMenuFunctionality(MenuFunctionEnums.CloseGame);
        return textCollection;
    }

    public static GUITextCollection selectDefaultLocalMusicPlayer (GUIComponent backgroundCard){
        int xCoordinate = backgroundCard.getXCoordinate() + 80;
        int yCoordinate = backgroundCard.getYCoordinate() + 30;

        GUITextCollection textCollection = new GUITextCollection(xCoordinate,yCoordinate, "USE LOCAL FILES");
        textCollection.setScale(resolutionFactor);
        textCollection.setMenuFunctionality(MenuFunctionEnums.SelectDefaultMediaPlayer);
        return textCollection;
    }

    public static GUITextCollection selectMacOSItunesMediaPlayer (GUITextCollection macOsMediaPlayer){
        GUIComponent component = macOsMediaPlayer.getComponents().get(0);
        int xCoordinate = component.getXCoordinate();
        int yCoordinate = component.getYCoordinate() + 50;

        GUITextCollection textCollection = new GUITextCollection(xCoordinate,yCoordinate, "USE ITUNES");
        textCollection.setScale(resolutionFactor);
        textCollection.setMenuFunctionality(MenuFunctionEnums.SelectMacOSMediaPlayer);
        return textCollection;
    }

    public static GUITextCollection selectMusicText(GUIComponent backgroundCard){
        int xCoordinate = backgroundCard.getXCoordinate() + 30;
        int yCoordinate = backgroundCard.getYCoordinate() - 30;

        GUITextCollection textCollection = new GUITextCollection(xCoordinate,yCoordinate, "GAME SETTINGS");
        textCollection.setScale(resolutionFactor * 2);
        return textCollection;
    }
}
