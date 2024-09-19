package game.UI;

import VisualAndAudioData.DataClass;
import VisualAndAudioData.audio.enums.LevelSongs;
import game.managers.AnimationManager;
import game.gameobjects.player.PlayerStats;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUICreator {

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

    private UIObject progressBar;
    private UIObject progressBarFilling;
    private UIObject progressBarSpaceShipIndicator;
    private UIObject gameOverCard;
    private UIObject gradeSC2iconObject;
    private UIObject gradeTextObject;
    private UIObject gameOverCardTitle;

    private List<UIObject> informationCards = new ArrayList<UIObject>();

    private List<ImageEnums> gameOverPeepos = new ArrayList();


    private static GameUICreator instance = new GameUICreator();

    private GameUICreator () {
        initGameOverPeepos();
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
        difficultyWings = new UIObject(createUIConfiguration(800, DataClass.getInstance().getPlayableWindowMaxHeight() + 20, 1f, wingsImageEnum));
        difficultyWings.setCenterCoordinates(800, (DataClass.getInstance().getPlayableWindowMaxHeight() + (difficultyWings.getHeight() / 2) + 10));

    }

    private void createInformationCards () {
        this.informationCards.clear();
//        UIObject topInfoCard = new UIObject(createUIConfiguration(0, 0, 1, ImageEnums.InformationCard));
//        topInfoCard.setImageDimensions(DataClass.getInstance().getInformationCardWidth(), DataClass.getInstance().getInformationCardHeight());
//        this.informationCards.add(topInfoCard);

        UIObject botInfoCard = new UIObject(createUIConfiguration(0, DataClass.getInstance().getPlayableWindowMaxHeight(), 1, ImageEnums.InformationCard));
        botInfoCard.setImageDimensions(DataClass.getInstance().getInformationCardWidth(), DataClass.getInstance().getInformationCardHeight());
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

        specialAttackHighlight.setXCoordinate(specialAttackFrame.getXCoordinate() - 2);
        specialAttackHighlight.setYCoordinate(specialAttackFrame.getYCoordinate() - 2);

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
        int xCoordinate = 1100;
        int yCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight() + 20;

        progressBar = new UIObject((createUIConfiguration(xCoordinate, yCoordinate, 1, ImageEnums.ProgressBar)));
        progressBarFilling = new UIObject((createUIConfiguration(xCoordinate + 5, yCoordinate, 0.95f, ImageEnums.ProgressBarFilling)));
        progressBarFilling.setCenterYCoordinate(progressBar.getCenterYCoordinate());

        progressBarSpaceShipIndicator = new UIObject(createUIConfiguration(xCoordinate,yCoordinate, 0.3f, ImageEnums.Player_Spaceship_Model_3));
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
        gameOverCard = new UIObject((createUIConfiguration(0, 0, 0.75f, ImageEnums.Wide_Card)));
        gameOverCard.setCenterCoordinates(centerScreenX, centerScreenY - (centerScreenY / 5));


        int gradeObjectX = Math.round(gameOverCard.getCenterXCoordinate() + (gameOverCard.getWidth() * 0.3f));
        int gradeObjectY = Math.round(gameOverCard.getCenterYCoordinate());
        gradeSC2iconObject = new UIObject((createUIConfiguration(gradeObjectX, gradeObjectY, 1, ImageEnums.GradeBronze)));
        gradeSC2iconObject.setCenterCoordinates(gradeObjectX, gradeObjectY);


        int gradeTitleCardX = gradeSC2iconObject.getCenterXCoordinate();
        int gradeTitleCardY = gradeSC2iconObject.getCenterYCoordinate();
        gradeTextObject = new UIObject((createUIConfiguration(gradeTitleCardX, gradeTitleCardY, 0.3f, ImageEnums.UIScoreTextCard)));
        gradeTitleCardY = gradeSC2iconObject.getYCoordinate() - gradeTextObject.getHeight();
        gradeTextObject.setCenterCoordinates(gradeTitleCardX, gradeTitleCardY);


        int titleCardX = gameOverCard.getCenterXCoordinate();
        int titleCardY = gameOverCard.getYCoordinate() + Math.round(gameOverCard.getHeight() * 0.15f);
        gameOverCardTitle = new UIObject(createUIConfiguration(titleCardX, titleCardY, 0.25f, ImageEnums.UILevelComplete));
        gameOverCardTitle.setCenterCoordinates(titleCardX, titleCardY);
    }


    private int gameOverPeepoRandomNumber = -100;
    public ImageEnums getRandomGameOverPeepo(){
        if(gameOverPeepoRandomNumber < 0) {
            Random random = new Random();
            gameOverPeepoRandomNumber = random.nextInt(0, gameOverPeepos.size() - 1);
        }
        return gameOverPeepos.get(gameOverPeepoRandomNumber);
    }

    public int calculateProgressBarFillingWidth(long currentFrame, long maxFrames) {
        return (int) ((double) currentFrame / maxFrames * (progressBar.getWidth() - 10));
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

    public UIObject getProgressBar () {
        return progressBar;
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
}