package game.UI;

import VisualAndAudioData.DataClass;
import VisualAndAudioData.audio.enums.LevelSongs;
import game.managers.AnimationManager;
import game.objects.player.PlayerStats;
import VisualAndAudioData.image.ImageEnums;
import game.spawner.LevelManager;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class GameUIManager {

    private int healthBarWidth = 200;
    private int healthBarHeight = 15;
    private int healthFrameWidth = 200;
    private int healthFrameHeight = 23;
    private UIObject healthFrame;
    private UIObject healthBar;

    private UIObject shieldFrame;
    private UIObject shieldBar;

    private UIObject overloadingShieldBar;

    private UIObject specialAttackFrame;
    private SpriteAnimation specialAttackHighlight;

    private UIObject difficultyWings;

    private List<UIObject> informationCards = new ArrayList<UIObject>();


    private static GameUIManager instance = new GameUIManager();

    private GameUIManager () {

    }

    public static GameUIManager getInstance () {
        return instance;
    }

    public void createGameBoardGUI () {
        resetManager();
    }

    //Called by LevelManager each time a level is generated
    public void createDifficultyWings(){
        ImageEnums wingsImageEnum = LevelSongs.getImageEnumByDifficultyScore(LevelManager.getInstance().getCurrentDifficultyCoeff());
        difficultyWings = new UIObject(createUIConfiguration(1050, DataClass.getInstance().getPlayableWindowMaxHeight(), 1f, wingsImageEnum));
        difficultyWings.setCenterCoordinates(1200, (DataClass.getInstance().getPlayableWindowMaxHeight() + (difficultyWings.getHeight() / 2) + 10));

    }

    private void createInformationCards () {
        this.informationCards.clear();

        UIObject topInfoCard = new UIObject(createUIConfiguration(0, 0, 1, ImageEnums.InformationCard));
        topInfoCard.setImageDimensions(DataClass.getInstance().getInformationCardWidth(), DataClass.getInstance().getInformationCardHeight());

        UIObject botInfoCard = new UIObject(createUIConfiguration(0, DataClass.getInstance().getPlayableWindowMaxHeight(), 1, ImageEnums.InformationCard));
        botInfoCard.setImageDimensions(DataClass.getInstance().getInformationCardWidth(), DataClass.getInstance().getInformationCardHeight());

//        this.informationCards.add(topInfoCard);
        this.informationCards.add(botInfoCard);
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

        specialAttackFrame = new UIObject(createUIConfiguration(healthBarWidth + 10 + 50, DataClass.getInstance().getPlayableWindowMaxHeight() + 10, 1, frameType));
        specialAttackHighlight = AnimationManager.getInstance().createAnimation(150, specialAttackFrame.getYCoordinate(), ImageEnums.Highlight, true,1);
//        specialAttackHighlight.setImageDimensions(specialAttackFrame.getWidth(), specialAttackFrame.getHeight());
        specialAttackHighlight.setAnimationScale(0.9142f);

        specialAttackHighlight.setX(specialAttackFrame.getXCoordinate() - 2);
        specialAttackHighlight.setY(specialAttackFrame.getYCoordinate() - 2);

    }

    private void createHealthBar () {
        int xCoordinate = 10;
        int yCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight() + 15;
        healthFrame = new UIObject(createUIConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Frame));
        healthFrame.resizeToDimensions(healthFrameWidth, healthFrameHeight);
        healthBar = new UIObject(createUIConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Red_Filling));
        healthBar.resizeToDimensions(healthBarWidth, healthBarHeight);
    }

    private void createShieldBar () {
        int xCoordinate = 10;
        int yCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight() + 35;
        shieldFrame = new UIObject(createUIConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Frame));
        shieldFrame.resizeToDimensions(healthFrameWidth, healthFrameHeight);
        shieldBar = new UIObject(createUIConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Blue_Filling));
        shieldBar.resizeToDimensions(healthBarWidth, healthBarHeight);
        overloadingShieldBar = new UIObject(createUIConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.Gold_Filling));
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
        createSpecialAttackUIObjects();
        createInformationCards();
    }

    public UIObject getSpecialAttackFrame () {
        return specialAttackFrame;
    }

    public SpriteAnimation getSpecialAttackHighlight () {
        return specialAttackHighlight;
    }

    public List<UIObject> getInformationCards () {
        return informationCards;
    }

    public UIObject getDifficultyWings () {
        return difficultyWings;
    }

}