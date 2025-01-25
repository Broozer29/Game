package net.riezebos.bruus.tbd.guiboards.boardcreators;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.guicomponents.DisplayOnly;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuCursor;
import net.riezebos.bruus.tbd.guiboards.util.WeaponDescription;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class ClassSelectionBoardCreator {

    public static GUIComponent createSelectShipBackgroundCard () {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.0694f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.3394f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Long_Card);
        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(275, 375);
        return backgroundCard;
    }

    public static GUITextCollection createSelectClassText (GUIComponent backgroundCard) {
        int xCoordinate = Math.round(backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.1f);
        int yCoordinate = Math.round(backgroundCard.getYCoordinate() + backgroundCard.getHeight() * 0.1f);
        ;

        String text = "SELECT CLASS";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(1.5f);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Select class text");

        return textCollection;
    }


    public static GUITextCollection createSelectCaptain (GUIComponent backgroundCard) {
        int xCoordinate = Math.round(backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.2f);
        int yCoordinate = Math.round(backgroundCard.getYCoordinate() + backgroundCard.getHeight() * 0.23f);
        ;

        String text = "CAPTAIN";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(1);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Select captain class");
        textCollection.setMenuFunctionality(MenuFunctionEnums.SelectCaptainClass);

        return textCollection;
    }

    public static GUITextCollection createSelectFireFighter (GUIComponent backgroundCard) {
        int xCoordinate = Math.round(backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.2f);
        int yCoordinate = Math.round(backgroundCard.getYCoordinate() + backgroundCard.getHeight() * 0.315f);
        ;

        String text = "FIREFIGHTER";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(1);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Select FireFighter class");
        textCollection.setMenuFunctionality(MenuFunctionEnums.SelectFireFighterClass);

        return textCollection;
    }

    public static GUIComponent createClassDescriptionBackgroundCard () {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.302f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.3394f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Long_Card);
        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(385, 375);
        return backgroundCard;
    }

    public static GUIComponent createPrimaryWeaponDescriptionBackgroundCard () {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.6161f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.3394f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Wide_Card);
        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(425, 175);
        return backgroundCard;
    }

    public static GUIComponent createPrimaryWeaponDescriptionIcon (GUIComponent backgroundCard, PlayerClass playerClass) {
        int xCoordinate = Math.round(backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.05f);
        int yCoordinate = Math.round(backgroundCard.getYCoordinate() + backgroundCard.getHeight() * 0.4f);

        ImageEnums image = ImageEnums.Starcraft2ConcentratedLaser;

        switch (playerClass) {
            case FireFighter:
                image = ImageEnums.Starcraft2FireBatWeapon;
                break;
            case Captain:
            default:
                image = ImageEnums.Starcraft2ConcentratedLaser;
                break;
        }

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, image);
        GUIComponent primaryWeaponIcon = new DisplayOnly(spriteConfiguration);
        primaryWeaponIcon.setImageDimensions(60, 60);
        primaryWeaponIcon.setCenterYCoordinate(yCoordinate);
        return primaryWeaponIcon;
    }

    public static WeaponDescription createPrimaryWeaponDescription (PlayerClass playerClass) {
        String descriptionTitle = getPrimaryWeaponTitle(playerClass);
        String descriptionText = getPrimaryWeaponDescription(playerClass);

        WeaponDescription weaponDescription = new WeaponDescription(descriptionTitle, descriptionText);
        weaponDescription.setAttackType(playerClass.getPrimaryAttackType());

        return weaponDescription;
    }


    private static String getPrimaryWeaponTitle (PlayerClass playerClass) {
        switch (playerClass) {
            case Captain:
                return "Primary: Laserbeam";
            case FireFighter:
                return "Primary: Flamethrower";
        }
        return "Not implemented yet";
    }

    private static String getPrimaryWeaponDescription (PlayerClass playerClass) {
        switch (playerClass) {
            case Captain:
                return "Shoot a laserbeam dealing 100% damage.";
            case FireFighter:
                return "Hold fire to unleash a flamethrower which deals damage and destroys missiles. Deals 100% damage and applies a damage over time effect.";
        }
        return "Not implemented yet";
    }

    public static GUIComponent createSecondarySkillDescriptionBackgroundCard () {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.6161f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.5694f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Wide_Card);
        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);
        backgroundCard.setImageDimensions(425, 175);
        return backgroundCard;
    }

    public static WeaponDescription createSecondarySkillDescription (PlayerClass playerClass) {
        String descriptionTitle = getSecondarySkillTitle(playerClass);
        String descriptionText = getSecondarySkillDescription(playerClass);

        WeaponDescription weaponDescription = new WeaponDescription(descriptionTitle, descriptionText);
        weaponDescription.setSpecialAttackType(playerClass.getSecondaryAttackType());

        return weaponDescription;
    }

    private static String getSecondarySkillTitle (PlayerClass playerClass) {
        switch (playerClass) {
            case Captain:
                return "Secondary: Electroshred";
            case FireFighter:
                return "Secondary: Fire Shield";
        }
        return "Fires an EMP that constantly deals 10% damage and destroys enemy missiles. Recharges every 3 seconds.";
    }

    private static String getSecondarySkillDescription (PlayerClass playerClass) {
        switch (playerClass) {
            case Captain:
                return "Fires an EMP that constantly deals 10% damage and destroys enemy missiles. Recharges every 3 seconds.";
            case FireFighter:
                return "Creates a ring of fire around you that lasts 4 seconds. Dealing 50% damage and destroys enemy missiles. Recharges every 10 seconds.";
        }
        return "Fires an EMP that constantly deals 10% damage and destroys enemy missiles. Recharges every 3 seconds.";
    }

    public static GUIComponent createSecondaryWeaponDescriptionIcon (GUIComponent backgroundCard, PlayerClass playerClass) {
        int xCoordinate = Math.round(backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.05f);
        int yCoordinate = Math.round(backgroundCard.getYCoordinate() + backgroundCard.getHeight() * 0.4f);

        ImageEnums image = ImageEnums.Starcraft2_Electric_Field;

        switch (playerClass) {
            case FireFighter:
                image = ImageEnums.Starcraft2_Fire_Hardened_Shields;
                break;
            case Captain:
            default:
                image = ImageEnums.Starcraft2_Electric_Field;
                break;
        }

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, image);
        GUIComponent weaponIcon = new DisplayOnly(spriteConfiguration);
        weaponIcon.setImageDimensions(60, 60);
        weaponIcon.setCenterYCoordinate(yCoordinate);
        return weaponIcon;
    }

    public static GUITextCollection createReturnToMainMenu () {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.0694f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.908f);

        String text = "RETURN TO MAIN MENU";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(1.5f);
        textCollection.getComponents().get(0).setDescriptionOfComponent("Return to the main menu");
        textCollection.setMenuFunctionality(MenuFunctionEnums.Return_To_Main_Menu);

        return textCollection;
    }

    public static GUITextCollection createStartGameButton () {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.85f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.908f);

        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, "START GAME");
        textCollection.setScale(1.5f);
        textCollection.setMenuFunctionality(MenuFunctionEnums.Start_Game);

        return textCollection;
    }

    public static MenuCursor createCursor (GUITextCollection textC) {
        int initCursorX = textC.getComponents().get(0).getXCoordinate();
        int initCursorY = textC.getComponents().get(0).getYCoordinate();
        float scale = 1;
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(initCursorX, initCursorY, scale, PlayerStats.getInstance().getSpaceShipImage());
        MenuCursor button = new MenuCursor(spriteConfiguration);
        return button;
    }

    public static GUIComponent createSelectClassImage () {
        int xCoordinate = DataClass.getInstance().getWindowWidth() / 2;
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.15f);

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(0.5f);
        spriteConfiguration.setImageType(ImageEnums.SelectClass);

        GUIComponent titleImage = new DisplayOnly(spriteConfiguration);
        titleImage.setDescriptionOfComponent("Title Image");
        titleImage.setCenterCoordinates(xCoordinate, yCoordinate);
        return titleImage;
    }


    private static SpriteConfiguration createSpriteConfiguration (int xCoordinate, int yCoordinate, float scale, ImageEnums imageType) {
        SpriteConfiguration config = new SpriteConfiguration();
        config.setxCoordinate(xCoordinate);
        config.setyCoordinate(yCoordinate);
        config.setScale(scale);
        config.setImageType(imageType);
        return config;
    }


    public static SpriteAnimation createCursorAnimation (GUIComponent cursor) {
        SpriteConfiguration config = new SpriteConfiguration();
        config.setxCoordinate(cursor.getXCoordinate());
        config.setyCoordinate(cursor.getYCoordinate());
        config.setScale(1);
        config.setImageType(ImageEnums.SelectNewClassAnimation);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(config, 0, false);
        SpriteAnimation anim = new SpriteAnimation(spriteAnimationConfiguration);
        anim.setCenterCoordinates(cursor.getCenterXCoordinate(), cursor.getCenterYCoordinate());
        return anim;
    }

}
