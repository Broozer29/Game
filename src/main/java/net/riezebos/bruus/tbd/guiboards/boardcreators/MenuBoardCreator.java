package net.riezebos.bruus.tbd.guiboards.boardcreators;

import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.guicomponents.DisplayOnly;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuCursor;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MenuBoardCreator {

    private static int imageScale = 1;
    private static int textScale = 1;

    public static GUITextCollection createStartGameButton(GUIComponent backgroundCard){
        int xCoordinate = backgroundCard.getXCoordinate() + 120;
        int yCoordinate = backgroundCard.getYCoordinate() + 50;
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, "START GAME");
        textCollection.setMenuFunctionality(MenuFunctionEnums.Start_Game);

        return textCollection;
    }


    public static MenuCursor createMenuCursor(GUIComponent initialSelectedButton){
        int xCoordinate = initialSelectedButton.getXCoordinate();
        int yCoordinate = initialSelectedButton.getCenterYCoordinate();

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(imageScale);
        spriteConfiguration.setImageType(ImageEnums.Player_Spaceship_Model_3);

        MenuCursor menuCursor = new MenuCursor(spriteConfiguration);
        menuCursor.setSelectedMenuTile(initialSelectedButton);
        menuCursor.setDescriptionOfComponent("Menu Cursor");
        menuCursor.setXCoordinate(menuCursor.getXCoordinate() - menuCursor.getxDistanceToKeep());
        menuCursor.setYCoordinate(menuCursor.getYCoordinate() - menuCursor.getHeight() / 2);
        return menuCursor;
    }

    public static GUIComponent createTitleImage(){
        int xCoordinate = DataClass.getInstance().getWindowWidth() / 2;
        int yCoordinate = (DataClass.getInstance().getWindowHeight() / 2) - 300;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(imageScale);
        spriteConfiguration.setImageType(ImageEnums.Title_Image);

        GUIComponent titleImage = new DisplayOnly(spriteConfiguration);
        titleImage.setDescriptionOfComponent("Title Image");
        titleImage.setCenterCoordinates(xCoordinate,yCoordinate);
        return titleImage;
    }

    public static GUIComponent createInputMapping(){
        int xCoordinate = DataClass.getInstance().getWindowWidth();
        int yCoordinate = DataClass.getInstance().getWindowHeight();

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(imageScale);
        spriteConfiguration.setImageType(ImageEnums.InputMapping);

        GUIComponent inputMapping = new DisplayOnly(spriteConfiguration);
        inputMapping.setDescriptionOfComponent("Input mapping image");
        inputMapping.setXCoordinate(Math.round(xCoordinate - (inputMapping.getWidth() * 1.1f)));
        inputMapping.setYCoordinate(Math.round(yCoordinate - (inputMapping.getHeight() * 1.3f)));
        return inputMapping;
    }

    public static List<GUITextCollection> createControlsExplanations(){
        List<GUITextCollection> controlExplanations = new ArrayList<>();
        int xCoordinate = 15;
        int yCoordinate = (DataClass.getInstance().getWindowHeight() / 2) + 300;


        GUITextCollection wasdExplanation = new GUITextCollection(xCoordinate, yCoordinate, "MOVEMENT = WASD OR ARROWS OR JOYSTICK");
        controlExplanations.add(wasdExplanation);

        GUITextCollection attackExplanation = new GUITextCollection(xCoordinate, yCoordinate + 20, "SPACEBAR OR X = NORMAL ATTACK");
        controlExplanations.add(attackExplanation);

        GUITextCollection specialExplanation = new GUITextCollection(xCoordinate, yCoordinate + 40, "ENTER OR Y = SPECIAL ATTACK");
        controlExplanations.add(specialExplanation);


        return controlExplanations;
    }

    public static GUIComponent startGameBackgroundCard(){
        int xCoordinate = 30;
        int yCoordinate = (DataClass.getInstance().getWindowHeight() / 2) - 50;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(imageScale);
        spriteConfiguration.setImageType(ImageEnums.Square_Card);

        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(400,250);
        return backgroundCard;
    }

    public static GUIComponent selectMusicPlayerBackgroundCard(GUIComponent startgameBackgroundCard){
        int xCoordinate = startgameBackgroundCard.getXCoordinate() + Math.round(startgameBackgroundCard.getWidth() * 1.2f);
        int yCoordinate = startgameBackgroundCard.getYCoordinate();

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(imageScale);
        spriteConfiguration.setImageType(ImageEnums.Square_Card);

        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(400,250);
        return backgroundCard;
    }

    public static GUITextCollection openShopButton(GUITextCollection startGameButton){
        GUIComponent component = startGameButton.getComponents().get(0);
        int xCoordinate = component.getXCoordinate();
        int yCoordinate = component.getYCoordinate() + 50;

        GUITextCollection textCollection = new GUITextCollection(xCoordinate,yCoordinate, "OPEN SHOP");
        textCollection.setMenuFunctionality(MenuFunctionEnums.Open_Shop_Window);
        return textCollection;
    }

    public static GUITextCollection foundControllerText(boolean foundController, GUIComponent titleImage){
        GUITextCollection textCollection = null;
        float scale = 1f;
        String text = null;
        int yCoordinate = 0;
        if(foundController){
            int xCoordinate = (DataClass.getInstance().getWindowWidth() / 2) - 100;
            yCoordinate = titleImage.getYCoordinate() + titleImage.getHeight() + 10;
            text = "FOUND A CONTROLLER";
            textCollection = new GUITextCollection(xCoordinate,yCoordinate,text);
        }
        else {
            int xCoordinate = 100;
            yCoordinate = titleImage.getYCoordinate() + titleImage.getHeight() + 10;
            text = "NO CONTROLLER COULD BE FOUND. MAKE SURE THE CONTROLLER IS CONNECTED THEN RESTART";
            textCollection = new GUITextCollection(xCoordinate,yCoordinate,text);

        }

        //Center it under the title image
        textCollection.setScale(scale);
        GUIComponent lastComponent = textCollection.getComponents().get(textCollection.getComponents().size() - 1);
        GUIComponent firstComponent = textCollection.getComponents().get(0);

        int textCollectionWidth = (lastComponent.getXCoordinate() + lastComponent.getWidth() - firstComponent.getXCoordinate());
        int newxCoordinate = titleImage.getCenterXCoordinate() - (textCollectionWidth / 2);
        textCollection = new GUITextCollection(newxCoordinate, yCoordinate, text);
        textCollection.setScale(scale);
        return textCollection;
    }

    public static GUITextCollection selectDefaultLocalMusicPlayer (GUIComponent backgroundCard){
        int xCoordinate = backgroundCard.getXCoordinate() + 80;
        int yCoordinate = backgroundCard.getYCoordinate() + 30;

        GUITextCollection textCollection = new GUITextCollection(xCoordinate,yCoordinate, "USE LOCAL FILES");
        textCollection.setMenuFunctionality(MenuFunctionEnums.SelectDefaultMediaPlayer);
        return textCollection;
    }

    public static GUITextCollection selectMacOSItunesMediaPlayer (GUITextCollection macOsMediaPlayer){
        GUIComponent component = macOsMediaPlayer.getComponents().get(0);
        int xCoordinate = component.getXCoordinate();
        int yCoordinate = component.getYCoordinate() + 50;

        GUITextCollection textCollection = new GUITextCollection(xCoordinate,yCoordinate, "USE ITUNES");
        textCollection.setMenuFunctionality(MenuFunctionEnums.SelectMacOSMediaPlayer);
        return textCollection;
    }

    public static GUITextCollection selectMusicText(GUIComponent backgroundCard){
        int xCoordinate = backgroundCard.getXCoordinate() + 30;
        int yCoordinate = backgroundCard.getYCoordinate() - 30;

        GUITextCollection textCollection = new GUITextCollection(xCoordinate,yCoordinate, "GAME SETTINGS");
        textCollection.setScale(2);
        return textCollection;
    }

}