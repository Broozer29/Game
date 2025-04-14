package net.riezebos.bruus.tbd.guiboards.boardcreators;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.guicomponents.DisplayOnly;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuCursor;
import net.riezebos.bruus.tbd.guiboards.util.ClassDescription;
import net.riezebos.bruus.tbd.guiboards.util.WeaponDescription;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class ClassSelectionBoardCreator {

    public static GUIComponent createSelectShipBackgroundCard() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.0694f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.3394f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Long_Card);
        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);

        int newWidth = Math.round(DataClass.getInstance().getResolutionFactor() * 275);
        int newHeight = Math.round(DataClass.getInstance().getResolutionFactor() * 375);

        backgroundCard.setImageDimensions(newWidth, newHeight);
        return backgroundCard;
    }

    public static GUITextCollection createSelectClassText(GUIComponent backgroundCard) {
        int xCoordinate = Math.round(backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.1f);
        int yCoordinate = Math.round(backgroundCard.getYCoordinate() + backgroundCard.getHeight() * 0.1f);

        String text = "SELECT CLASS";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(1.5f * DataClass.getInstance().getResolutionFactor());
        textCollection.getComponents().get(0).setDescriptionOfComponent("Select class text");

        return textCollection;
    }


    public static GUITextCollection createSelectCaptain(GUIComponent backgroundCard) {
        int xCoordinate = Math.round(backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.2f);
        int yCoordinate = Math.round(backgroundCard.getYCoordinate() + backgroundCard.getHeight() * 0.23f);

        String text = "CAPTAIN";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(1 * DataClass.getInstance().getResolutionFactor());
        textCollection.getComponents().get(0).setDescriptionOfComponent("Select captain class");
        textCollection.setMenuFunctionality(MenuFunctionEnums.SelectCaptainClass);

        return textCollection;
    }

    public static GUITextCollection createSelectFireFighter(GUIComponent backgroundCard) {
        int xCoordinate = Math.round(backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.2f);
        int yCoordinate = Math.round(backgroundCard.getYCoordinate() + backgroundCard.getHeight() * 0.315f);

        String text = "FIREFIGHTER";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(1 * DataClass.getInstance().getResolutionFactor());
        textCollection.getComponents().get(0).setDescriptionOfComponent("Select FireFighter class");
        textCollection.setMenuFunctionality(MenuFunctionEnums.SelectFireFighterClass);

        return textCollection;
    }

    public static GUITextCollection createSelectCarrier(GUIComponent backgroundCard) {
        int xCoordinate = Math.round(backgroundCard.getXCoordinate() + backgroundCard.getWidth() * 0.2f);
        int yCoordinate = Math.round(backgroundCard.getYCoordinate() + backgroundCard.getHeight() * 0.4f);


        String text = "CARRIER";
        if (!PlayerProfileManager.getInstance().getLoadedProfile().isCarrierUnlocked()) {
            text = "LOCKED";
        }

        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(1 * DataClass.getInstance().getResolutionFactor());
        textCollection.getComponents().get(0).setDescriptionOfComponent("Select Carrier class");

        if (PlayerProfileManager.getInstance().getLoadedProfile().isCarrierUnlocked()) {
            textCollection.setMenuFunctionality(MenuFunctionEnums.SelectCarrierClass);
        }

        return textCollection;
    }

    public static GUIComponent createClassDescriptionBackgroundCard() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.302f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.3394f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Long_Card);
        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);
        int newWidth = Math.round(DataClass.getInstance().getResolutionFactor() * 385);
        int newHeight = Math.round(DataClass.getInstance().getResolutionFactor() * 375);

        backgroundCard.setImageDimensions(newWidth, newHeight);
        return backgroundCard;
    }

    public static GUIComponent createPrimaryWeaponDescriptionBackgroundCard() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.6161f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.3394f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Wide_Card);
        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);

        int newWidth = Math.round(DataClass.getInstance().getResolutionFactor() * 425);
        int newHeight = Math.round(DataClass.getInstance().getResolutionFactor() * 175);
        backgroundCard.setImageDimensions(newWidth, newHeight);
        return backgroundCard;
    }

    public static GUIComponent createPrimaryWeaponDescriptionIcon(GUIComponent backgroundCard, PlayerClass playerClass) {
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

        int newWidth = Math.round(DataClass.getInstance().getResolutionFactor() * 60);
        int newHeight = Math.round(DataClass.getInstance().getResolutionFactor() * 60);
        primaryWeaponIcon.setImageDimensions(newWidth, newHeight);
        primaryWeaponIcon.setCenterYCoordinate(yCoordinate);
        return primaryWeaponIcon;
    }

    public static WeaponDescription createPrimaryWeaponDescription(PlayerClass playerClass) {
        String descriptionTitle = getPrimaryWeaponTitle(playerClass);
        String descriptionText = getPrimaryWeaponDescription(playerClass);

        WeaponDescription weaponDescription = new WeaponDescription(descriptionTitle, descriptionText);
        weaponDescription.setAttackType(playerClass.getPrimaryAttackType());

        return weaponDescription;
    }


    private static String getPrimaryWeaponTitle(PlayerClass playerClass) {
        switch (playerClass) {
            case Captain:
                return "Primary: Laserbeam";
            case FireFighter:
                return "Primary: Flamethrower";
            case Carrier:
                if (PlayerProfileManager.getInstance().getLoadedProfile().isCarrierUnlocked()) {
                    return "Primary: Switch gears";
                }

                return "Locked";
        }
        return "Placeholder";
    }

    private static String getPrimaryWeaponDescription(PlayerClass playerClass) {
        switch (playerClass) {
            case Captain:
                return "Shoot a laserbeam dealing 100% damage.";
            case FireFighter:
                return "Hold fire to unleash a flamethrower which deals damage and destroys missiles. Deals 100% damage and applies Ignite.";
            case Carrier:
                if (PlayerProfileManager.getInstance().getLoadedProfile().isCarrierUnlocked()) {
                    return "Switch between fast and slow movement. While moving fast, you do not build Protoss ships.";
                }

                return ClassDescription.getInstance(PlayerClass.Carrier).getUnlockCondition();
        }
        return "Placeholder";
    }

    public static GUIComponent createSecondarySkillDescriptionBackgroundCard() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.6161f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.5694f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Wide_Card);
        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);

        int newWidth = Math.round(DataClass.getInstance().getResolutionFactor() * 425);
        int newHeight = Math.round(DataClass.getInstance().getResolutionFactor() * 175);
        backgroundCard.setImageDimensions(newWidth, newHeight);
        return backgroundCard;
    }

    public static WeaponDescription createSecondarySkillDescription(PlayerClass playerClass) {
        String descriptionTitle = getSecondarySkillTitle(playerClass);
        String descriptionText = getSecondarySkillDescription(playerClass);

        WeaponDescription weaponDescription = new WeaponDescription(descriptionTitle, descriptionText);
        weaponDescription.setSpecialAttackType(playerClass.getSecondaryAttackType());

        return weaponDescription;
    }

    private static String getSecondarySkillTitle(PlayerClass playerClass) {
        switch (playerClass) {
            case Captain:
                return "Secondary: Electroshred";
            case FireFighter:
                return "Secondary: Fire Shield";
            case Carrier:
                if (PlayerProfileManager.getInstance().getLoadedProfile().isCarrierUnlocked()) {
                    return "Secondary: Protoss Beacon";
                }
                return "Locked";
        }
        return "Placeholder";
    }

    public static boolean hasUnlockedClass(PlayerClass playerClass) {
        switch (playerClass) {
            case Carrier -> {
                return PlayerProfileManager.getInstance().getLoadedProfile().isCarrierUnlocked();
            }
            default -> {
                return true;
            }
        }
    }

    private static String getSecondarySkillDescription(PlayerClass playerClass) {
        switch (playerClass) {
            case Captain:
                return "Fires an EMP that constantly deals 10% damage and destroys enemy missiles. Recharges every 3 seconds.";
            case FireFighter:
                return "Creates a ring of fire around you that lasts 4 seconds. Dealing 50% damage, applies Ignite and destroys enemy missiles. Recharges every 10 seconds.";
            case Carrier:
                if (PlayerProfileManager.getInstance().getLoadedProfile().isCarrierUnlocked()) {
                    return "Places a beacon in front of you. Protoss ships will prioritize hovering around the beacon over the Carrier.";
                }
                return ClassDescription.getInstance(PlayerClass.Carrier).getUnlockCondition();
        }
        return "Placeholder";
    }

    public static GUIComponent createSecondaryWeaponDescriptionIcon(GUIComponent backgroundCard, PlayerClass playerClass) {
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

        int newWidth = Math.round(DataClass.getInstance().getResolutionFactor() * 60);
        int newHeight = Math.round(DataClass.getInstance().getResolutionFactor() * 60);
        weaponIcon.setImageDimensions(newWidth, newHeight);
        weaponIcon.setCenterYCoordinate(yCoordinate);
        return weaponIcon;
    }

    public static GUIComponent createReturnToMainMenuBackgroundCard() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.0694f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.84f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Wide_Card);
        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);

        int newWidth = Math.round(DataClass.getInstance().getResolutionFactor() * 340);
        int newHeight = Math.round(DataClass.getInstance().getResolutionFactor() * 70);
        backgroundCard.setImageDimensions(newWidth, newHeight);

        return backgroundCard;
    }

    public static GUITextCollection createReturnToMainMenu(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int yCoordinate = backgroundCard.getCenterYCoordinate() - Math.round(DataClass.getInstance().getResolutionFactor() * 10);

        String text = "RETURN TO MAIN MENU";
        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, text);
        textCollection.setScale(1.5f * DataClass.getInstance().getResolutionFactor());
        textCollection.setStartingXCoordinate(xCoordinate - (textCollection.getWidth() / 2));
        textCollection.setMenuFunctionality(MenuFunctionEnums.Return_To_Main_Menu);
        return textCollection;
    }

    public static GUIComponent createBoonButtonBackgroundCard() {
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.785f);
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.84f);

        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Wide_Card);
        GUIComponent backgroundCard = new DisplayOnly(spriteConfiguration);

        int newWidth = Math.round(DataClass.getInstance().getResolutionFactor() * 240);
        int newHeight = Math.round(DataClass.getInstance().getResolutionFactor() * 70);
        backgroundCard.setImageDimensions(newWidth, newHeight);

        return backgroundCard;
    }

    public static GUITextCollection createBoonSelectionButton(GUIComponent backgroundCard) {
        int xCoordinate = backgroundCard.getCenterXCoordinate();
        int yCoordinate = backgroundCard.getCenterYCoordinate() - Math.round(DataClass.getInstance().getResolutionFactor() * 10);

        GUITextCollection textCollection = new GUITextCollection(xCoordinate, yCoordinate, "SELECT BOONS");
        textCollection.setScale(1.5f * DataClass.getInstance().getResolutionFactor());
        textCollection.setStartingXCoordinate(xCoordinate - (textCollection.getWidth() / 2));
        textCollection.setMenuFunctionality(MenuFunctionEnums.OpenBoonSelectionBoard);

        return textCollection;
    }

    public static MenuCursor createCursor(GUITextCollection textC) {
        int initCursorX = textC.getComponents().get(0).getXCoordinate();
        int initCursorY = textC.getComponents().get(0).getYCoordinate();
        float scale = 1 * DataClass.getInstance().getResolutionFactor();
        ImageEnums imageEnums = PlayerStats.getInstance().getSpaceShipImage();
        SpriteConfiguration spriteConfiguration = createSpriteConfiguration(initCursorX, initCursorY, scale, imageEnums);
        MenuCursor button = new MenuCursor(spriteConfiguration);
        return button;
    }

    public static GUIComponent createSelectClassImage() {
        int xCoordinate = DataClass.getInstance().getWindowWidth() / 2;
        int yCoordinate = Math.round(DataClass.getInstance().getWindowHeight() * 0.15f);

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(0.5f * DataClass.getInstance().getResolutionFactor());
        spriteConfiguration.setImageType(ImageEnums.SelectClass);

        GUIComponent titleImage = new DisplayOnly(spriteConfiguration);
        titleImage.setDescriptionOfComponent("Title Image");
        titleImage.setCenterCoordinates(xCoordinate, yCoordinate);
        return titleImage;
    }


    private static SpriteConfiguration createSpriteConfiguration(int xCoordinate, int yCoordinate, float scale, ImageEnums imageType) {
        SpriteConfiguration config = new SpriteConfiguration();
        config.setxCoordinate(xCoordinate);
        config.setyCoordinate(yCoordinate);
        config.setScale(scale);
        config.setImageType(imageType);
        return config;
    }


    public static SpriteAnimation createCursorAnimation(GUIComponent cursor) {
        SpriteConfiguration config = new SpriteConfiguration();
        config.setxCoordinate(cursor.getXCoordinate());
        config.setyCoordinate(cursor.getYCoordinate());
        config.setScale(1 * DataClass.getInstance().getResolutionFactor());
        config.setImageType(ImageEnums.SelectNewClassAnimation);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(config, 0, false);
        SpriteAnimation anim = new SpriteAnimation(spriteAnimationConfiguration);
        anim.setCenterCoordinates(cursor.getCenterXCoordinate(), cursor.getCenterYCoordinate());
        return anim;
    }

}
