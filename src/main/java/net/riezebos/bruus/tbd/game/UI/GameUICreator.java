package net.riezebos.bruus.tbd.game.UI;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.LevelSongs;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUICreator {
    Random random = new Random();

    private int healthBarWidth = Math.round(200 * DataClass.getInstance().getResolutionFactor());
    private int healthBarHeight = Math.round(15 * DataClass.getInstance().getResolutionFactor());
    private int healthFrameWidth = Math.round(200 * DataClass.getInstance().getResolutionFactor());
    private int healthFrameHeight = Math.round(23 * DataClass.getInstance().getResolutionFactor());
    private UIObject healthFrame;
    private UIObject healthBar;

    private UIObject shieldFrame;
    private UIObject shieldBar;

    private UIObject overloadingShieldBar;

    private UIObject specialAttackFrame;
    private SpriteAnimation specialAttackHighlight;

    private UIObject difficultyWings;

    private UIObject progressBarFrame;
    private UIObject progressBarFilling;
    private UIObject progressBarSpaceShipIndicator;
    private UIObject gameOverCard;
    private UIObject gradeSC2iconObject;
    private UIObject gradeTextObject;
    private UIObject gameOverCardTitle;
    private UIObject damageOverlay;
    private UIObject mineralIcon;

    private List<UIObject> informationCards = new ArrayList<>();

    private List<ImageEnums> gameOverPeepos = new ArrayList<>();


    private static GameUICreator instance = new GameUICreator();

    private GameUICreator () {
        initGameOverPeepos();
        createDamageOverlay();
    }

    public static GameUICreator getInstance () {
        return instance;
    }

    private void initGameOverPeepos() {
        gameOverPeepos.add(ImageEnums.peepoDeepFriedSadge);
        gameOverPeepos.add(ImageEnums.peepoFeelsCringeMan);
        gameOverPeepos.add(ImageEnums.peepoFeelsRetardedMan);
        gameOverPeepos.add(ImageEnums.peepoHmmm);
        gameOverPeepos.add(ImageEnums.peepoLookingDown);
        gameOverPeepos.add(ImageEnums.peepoMonkaHmmm);
        gameOverPeepos.add(ImageEnums.peepoMonkaLaugh);
        gameOverPeepos.add(ImageEnums.peepoPauseChamp);
        gameOverPeepos.add(ImageEnums.peepoClown);
        gameOverPeepos.add(ImageEnums.peepoCringe);
        gameOverPeepos.add(ImageEnums.peepoLaugh);
        gameOverPeepos.add(ImageEnums.peepoLyingSadge);
        gameOverPeepos.add(ImageEnums.peepoOkay);
        gameOverPeepos.add(ImageEnums.peepoSad);
        gameOverPeepos.add(ImageEnums.peepoSad2);
        gameOverPeepos.add(ImageEnums.peepoShrug);
        gameOverPeepos.add(ImageEnums.peepoSmadge);
        gameOverPeepos.add(ImageEnums.peepoSmokedge);
        gameOverPeepos.add(ImageEnums.peepoSmug);
        gameOverPeepos.add(ImageEnums.peepoStare);
        gameOverPeepos.add(ImageEnums.peepoUhm);
        gameOverPeepos.add(ImageEnums.peepoAngy);
        gameOverPeepos.add(ImageEnums.peepoBruh);
        gameOverPeepos.add(ImageEnums.peepoCoffee);
        gameOverPeepos.add(ImageEnums.peepoConfused);
        gameOverPeepos.add(ImageEnums.peepoGottem);
        gameOverPeepos.add(ImageEnums.peepoHands);
        gameOverPeepos.add(ImageEnums.peepoLaugh2);
        gameOverPeepos.add(ImageEnums.peepoPointLaugh);
        gameOverPeepos.add(ImageEnums.peepoW);
        gameOverPeepos.add(ImageEnums.peepoSadClown);
        gameOverPeepos.add(ImageEnums.peepoSadge);
        gameOverPeepos.add(ImageEnums.peepoSadgeCry);
        gameOverPeepos.add(ImageEnums.peepoShruge);
        gameOverPeepos.add(ImageEnums.peepoSkillIssue);
    }


    public void createGameBoardGUI () {
        resetManager();
    }

    //Called by LevelManager each time a level is generated
    public void createDifficultyWings(boolean isBossLevel, int currentLevelDifficultyScore){
        ImageEnums wingsImageEnum = null;
        if(isBossLevel){
            wingsImageEnum = ImageEnums.RedWings5;
        } else {
            wingsImageEnum = LevelSongs.getImageEnumByDifficultyScore(currentLevelDifficultyScore);
        }

        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.60305f);
//        int yCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight() + 20;


        float scale = 1 * DataClass.getInstance().getResolutionFactor();
        difficultyWings = new UIObject(createUIConfiguration(0, DataClass.getInstance().getPlayableWindowMaxHeight() + 20, scale, wingsImageEnum));
        difficultyWings.setCenterCoordinates(xCoordinate,
                (DataClass.getInstance().getPlayableWindowMaxHeight() + (difficultyWings.getHeight() / 2))
        );
    }

    public GUIComponent createEmeraldObtainedIcon(int xCoordinate, int yCoordinate){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(1 * DataClass.getInstance().getResolutionFactor());
        spriteConfiguration.setImageType(ImageEnums.EmeraldGem5);

        GUIComponent emeraldObtainedIcon = new GUIComponent(spriteConfiguration);
        emeraldObtainedIcon.setCenterCoordinates(xCoordinate, yCoordinate);
        emeraldObtainedIcon.setTransparancyAlpha(true, 1, -0.005f);
        return emeraldObtainedIcon;
    }

    private void createInformationCards () {
        this.informationCards.clear();
        UIObject botInfoCard = new UIObject(createUIConfiguration(0, DataClass.getInstance().getPlayableWindowMaxHeight(), 1, ImageEnums.InformationCard));
        botInfoCard.setImageDimensions(DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getInformationCardHeight());
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
            case FlameShield:
                frameType = ImageEnums.Starcraft2_Fire_Hardened_Shields;
                break;
            case PlaceCarrierDrone:
                frameType = ImageEnums.CarrierPlaceDroneIcon;
                break;
            default:
                frameType = ImageEnums.Test_Image;
                break;

        }

        specialAttackFrame = new UIObject(createUIConfiguration(healthBarWidth + 60, DataClass.getInstance().getPlayableWindowMaxHeight() + 10, 1, frameType));
        specialAttackHighlight = AnimationManager.getInstance().createAnimation(150, specialAttackFrame.getYCoordinate(), ImageEnums.Highlight, true,1);
        specialAttackHighlight.setAnimationScale(0.9142f);

        specialAttackHighlight.setXCoordinate(specialAttackFrame.getXCoordinate() - 2);
        specialAttackHighlight.setYCoordinate(specialAttackFrame.getYCoordinate() - 2);

    }

    private void createDamageOverlay(){
        int xCoordinate = 0;
        int yCoordinate = 0;

        int width = DataClass.getInstance().getWindowWidth();
        int height = DataClass.getInstance().getPlayableWindowMaxHeight();
        damageOverlay = new UIObject(createUIConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.UIDamageOverlay));
        damageOverlay.resizeToDimensions(width, height);

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

    private void createProgressBar(){
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.35305f);
        int yCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight() + 20;

        float progressBarScale = 1 * DataClass.getInstance().getResolutionFactor();
        float progressBarFillingScale = 0.95f * DataClass.getInstance().getResolutionFactor();
        float progressBarSpaceShipIndicatorScale = 0.3f * DataClass.getInstance().getResolutionFactor();

        progressBarFrame = new UIObject((createUIConfiguration(xCoordinate, yCoordinate, progressBarScale, ImageEnums.ProgressBar)));
        progressBarFilling = new UIObject((createUIConfiguration(xCoordinate + 5, yCoordinate, progressBarFillingScale, ImageEnums.ProgressBarFilling)));
        progressBarFilling.setCenterYCoordinate(progressBarFrame.getCenterYCoordinate());

        progressBarSpaceShipIndicator = new UIObject(createUIConfiguration(xCoordinate,yCoordinate, progressBarSpaceShipIndicatorScale, ImageEnums.Player_Spaceship_Model_3));
        progressBarSpaceShipIndicator.setCenterYCoordinate(progressBarFilling.getCenterYCoordinate());
    }

    private SpriteConfiguration createUIConfiguration (int xCoordinate, int yCoordinate, float scale, ImageEnums imageType) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setScale(scale);
        spriteConfiguration.setImageType(imageType);
        return spriteConfiguration;
    }

    public int calculateHealthbarWidth (float currentHitpoints, float maximumHitpoints) {
        // Calculate the percentage of currentHitpoints out of maximumHitpoints
        double percentage = (double) currentHitpoints / maximumHitpoints * 100;
        // Calculate what this percentage is of thirdNumber
        int width = (int) Math.ceil((percentage / 100) * getHealthBarWidth());

        if (width > getHealthBarWidth()) {
            width = getHealthBarWidth();
        } else if (width < 1) {
            width = 1;
        }

        return width;
    }

    private void createGameOverCard(){
        int centerScreenX = DataClass.getInstance().getWindowWidth() / 2;
        int centerScreenY = DataClass.getInstance().getWindowHeight() / 2;

        float gameOverCardScale = 0.75f * DataClass.getInstance().getResolutionFactor();
        gameOverCard = new UIObject((createUIConfiguration(0, 0, gameOverCardScale, ImageEnums.Wide_Card)));
        gameOverCard.setCenterCoordinates(centerScreenX, centerScreenY - (centerScreenY / 5));


        float gradeScale = 1 * DataClass.getInstance().getResolutionFactor();
        int gradeObjectX = Math.round(gameOverCard.getCenterXCoordinate() + (gameOverCard.getWidth() * 0.3f));
        int gradeObjectY = gameOverCard.getCenterYCoordinate();
        gradeSC2iconObject = new UIObject((createUIConfiguration(gradeObjectX, gradeObjectY, gradeScale, ImageEnums.GradeBronze)));
        gradeSC2iconObject.setCenterCoordinates(gradeObjectX, gradeObjectY);


        int gradeTitleCardX = gradeSC2iconObject.getCenterXCoordinate();
        int gradeTitleCardY = gradeSC2iconObject.getCenterYCoordinate();
        float gradeTextScale = 0.3f * DataClass.getInstance().getResolutionFactor();
        gradeTextObject = new UIObject((createUIConfiguration(gradeTitleCardX, gradeTitleCardY, gradeTextScale, ImageEnums.UIScoreTextCard)));
        gradeTitleCardY = gradeSC2iconObject.getYCoordinate() - gradeTextObject.getHeight();
        gradeTextObject.setCenterCoordinates(gradeTitleCardX, gradeTitleCardY);


        int titleCardX = gameOverCard.getCenterXCoordinate();
        int titleCardY = gameOverCard.getYCoordinate() + Math.round(gameOverCard.getHeight() * 0.15f);
        float gameOverCardTitleScale = 0.35f * DataClass.getInstance().getResolutionFactor();
        gameOverCardTitle = new UIObject(createUIConfiguration(titleCardX, titleCardY, gameOverCardTitleScale, ImageEnums.UILevelComplete));
        gameOverCardTitle.setCenterCoordinates(titleCardX, titleCardY);
    }

    private void createMineralIcon(){
        int xCoordinate = Math.round(DataClass.getInstance().getWindowWidth() * 0.265f);
        int yCoordinate = Math.round(DataClass.getInstance().getPlayableWindowMaxHeight() + ((DataClass.getInstance().getInformationCardHeight() / 2) * 0.9f));


        float scale = 0.5f * DataClass.getInstance().getResolutionFactor();
        mineralIcon = new UIObject(createUIConfiguration(xCoordinate, yCoordinate, scale, ImageEnums.TopazGem7));
        mineralIcon.setCenterCoordinates(xCoordinate, yCoordinate);
    }


    private int gameOverPeepoRandomNumber = -100;
    public ImageEnums getRandomGameOverPeepo(){
        if(gameOverPeepoRandomNumber < 0) {
            gameOverPeepoRandomNumber = random.nextInt(0, gameOverPeepos.size() - 1);
        }
        return gameOverPeepos.get(gameOverPeepoRandomNumber);
    }

    public int calculateProgressBarFillingWidth(double currentFrame, double maxFrames) {
        if (currentFrame < 0) {
            return 1;
        }
        return Math.min(progressBarFrame.getWidth(), (int) (currentFrame / maxFrames * progressBarFrame.getWidth()));
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
        createProgressBar();
        createGameOverCard();
        createMineralIcon();
        gameOverPeepoRandomNumber = -100;
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

    public UIObject getProgressBarFrame() {
        return progressBarFrame;
    }

    public UIObject getProgressBarFilling () {
        return progressBarFilling;
    }

    public UIObject getProgressBarSpaceShipIndicator () {
        return progressBarSpaceShipIndicator;
    }

    public UIObject getGameOverCard () {
        return gameOverCard;
    }

    public UIObject getGradeSC2iconObject () {
        return gradeSC2iconObject;
    }

    public UIObject getGradeTextObject () {
        return gradeTextObject;
    }

    public UIObject getGameOverCardTitle () {
        return gameOverCardTitle;
    }

    public UIObject getDamageOverlay () {
        return damageOverlay;
    }

    public UIObject getMineralIcon () {
        return mineralIcon;
    }
}