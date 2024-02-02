package game.managers;

import game.UI.UIObject;
import game.objects.player.PlayerStats;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class GameUIManager {

    private int healthBarWidth = 120;
    private int healthBarHeight = 20;
    private int healthFrameWidth = 120;
    private int healthFrameHeight = 15;
    private UIObject healthFrame;
    private UIObject healthBar;

    private UIObject shieldFrame;
    private UIObject shieldBar;

    private UIObject overloadingShieldBar;

    private UIObject specialAttackFrame;
    private SpriteAnimation specialAttackHighlight;


    private static GameUIManager instance = new GameUIManager();

    private GameUIManager () {

    }

    public static GameUIManager getInstance () {
        return instance;
    }

    public void createGameBoardGUI () {
        createHealthBar();
        createShieldBar();
        createSpecialAttackUIObjects();
    }

    private void createSpecialAttackUIObjects () {
        ImageEnums frameType = null;
        switch (PlayerStats.getInstance().getPlayerSpecialAttackType()) {
            case EMP:
                frameType = ImageEnums.Starcraft2_Electric_Field;
                break;
            case Rocket_Cluster:
                frameType = ImageEnums.Starcraft2_Dual_Rockets;
                break;
            default:
                break;

        }

        specialAttackFrame = new UIObject(createUIConfiguration(150, 20, (float) 0.7, frameType));
        specialAttackHighlight = AnimationManager.getInstance().createAnimation(50, 30, ImageEnums.Highlight, true, (float) 0.7);
//		specialAttackHighlight = new SpriteAnimation(50, 30, ImageEnums.Highlight, true, (float) 0.7);
        specialAttackHighlight.setImageDimensions(specialAttackFrame.getWidth(), specialAttackFrame.getHeight());
        specialAttackHighlight.setX(specialAttackFrame.getXCoordinate() - 2);
        specialAttackHighlight.setY(specialAttackFrame.getYCoordinate() - 2);
    }

    private void createHealthBar () {
        healthFrame = new UIObject(createUIConfiguration(0, 30, 1, ImageEnums.Frame));
        healthFrame.resizeToDimensions(healthFrameWidth, healthFrameHeight);
        healthBar = new UIObject(createUIConfiguration(10, 30, 1, ImageEnums.Red_Filling));
        healthBar.resizeToDimensions(healthBarWidth, healthBarHeight);
    }

    private void createShieldBar () {
        shieldFrame = new UIObject(createUIConfiguration(0, 60, 1, ImageEnums.Frame));
        shieldFrame.resizeToDimensions(healthFrameWidth, healthFrameHeight);
        shieldBar = new UIObject(createUIConfiguration(10, 60, 1, ImageEnums.Blue_Filling));
        shieldBar.resizeToDimensions(healthBarWidth, healthBarHeight);
        overloadingShieldBar = new UIObject(createUIConfiguration(10, 60, 1, ImageEnums.Gold_Filling));
        overloadingShieldBar.resizeToDimensions(healthBarWidth, healthBarHeight);
    }


    private SpriteConfiguration createUIConfiguration (int xCoordinate, int yCoordinate, float scale, ImageEnums imageType) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(scale);
        spriteConfiguration.setImageType(imageType);
        return spriteConfiguration;
    }

    public int getHealthBarWidth () {
        return healthBarWidth;
    }

    public int getHealthBarHeight () {
        return healthBarHeight;
    }

    public UIObject getHealthFrame () {
        return healthFrame;
    }

    public UIObject getHealthBar () {
        return healthBar;
    }

    public UIObject getShieldFrame () {
        return shieldFrame;
    }

    public UIObject getShieldBar () {
        return shieldBar;
    }

    public UIObject getOverloadingShieldBar () {
        return overloadingShieldBar;
    }

    public void resetManager () {
        createHealthBar();
        createShieldBar();
    }

    public UIObject getSpecialAttackFrame () {
        return specialAttackFrame;
    }

    public SpriteAnimation getSpecialAttackHighlight () {
        return specialAttackHighlight;
    }

}