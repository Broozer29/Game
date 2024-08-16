package guiboards.boards;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.*;

import VisualAndAudioData.image.ImageEnums;
import controllerInput.ConnectedControllers;
import controllerInput.ControllerInputEnums;
import game.UI.UIObject;
import game.gameobjects.GameObject;
import game.gamestate.GameStatsTracker;
import game.movement.Direction;
import game.level.directors.DirectorManager;
import game.gamestate.SpawningMechanic;
import game.level.LevelManager;
import game.managers.AnimationManager;
import game.UI.GameUICreator;
import game.gameobjects.neutral.ExplosionManager;
import game.managers.OnScreenTextManager;
import game.gameobjects.friendlies.FriendlyObject;
import game.gameobjects.player.PlayerManager;
import game.gameobjects.background.BackgroundManager;
import game.gameobjects.background.BackgroundObject;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyManager;
import game.gameobjects.friendlies.FriendlyManager;
import game.util.OnScreenText;
import game.gameobjects.player.spaceship.SpaceShipSpecialGun;
import game.gameobjects.missiles.specialAttacks.SpecialAttack;
import game.gameobjects.missiles.Missile;
import game.gameobjects.missiles.MissileManager;
import VisualAndAudioData.DataClass;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatusEnums;
import game.gameobjects.player.PlayerStats;
import VisualAndAudioData.audio.AudioDatabase;
import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.audio.AudioPositionCalculator;
import guiboards.BoardManager;
import visualobjects.Sprite;
import visualobjects.SpriteAnimation;

public class GameBoard extends JPanel implements ActionListener {

    private Timer drawTimer;

    private GameStatusEnums lastKnownState = null;

    private DataClass data = DataClass.getInstance();
    private AudioDatabase audioDatabase = AudioDatabase.getInstance();
    private GameStateInfo gameState = GameStateInfo.getInstance();
    private final int boardWidth = data.getWindowWidth();
    private final int boardHeight = data.getWindowHeight();

    private float zoningInAlpha = 1.0f;
    private float zoningOutAlpha = 0.0f;

    private long inputDelay = 0;
    private static final long MOVE_COOLDOWN = 100;

    private boolean hasResetManagersForNextLevel = false;

    private List<String> strings = Arrays.asList("U died lol", "'Get good.' - Sun Tzu", "Veni vidi vici'd",
            "Overconfidence is a slow and insidious killer");
    private int rnd = new Random().nextInt(strings.size());
    private String msg = strings.get(rnd);

    private BoardManager boardManager = BoardManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private MissileManager missileManager = MissileManager.getInstance();
    private LevelManager levelManager = LevelManager.getInstance();
    private PlayerManager playerManager = PlayerManager.getInstance();
    private AudioManager audioManager = AudioManager.getInstance();
    private BackgroundManager backgroundManager = BackgroundManager.getInstance();
    private ExplosionManager explosionManager = ExplosionManager.getInstance();
    private FriendlyManager friendlyManager = FriendlyManager.getInstance();
    private PlayerStats playerStats = PlayerStats.getInstance();
    private GameUICreator uiManager = GameUICreator.getInstance();
    private OnScreenTextManager textManager = OnScreenTextManager.getInstance();
    private AudioPositionCalculator audioPosCalc = AudioPositionCalculator.getInstance();
    private ConnectedControllers controllers = ConnectedControllers.getInstance();


    public GameBoard () {
        animationManager = AnimationManager.getInstance();
        enemyManager = EnemyManager.getInstance();
        missileManager = MissileManager.getInstance();
        levelManager = LevelManager.getInstance();
        playerManager = PlayerManager.getInstance();
        audioManager = AudioManager.getInstance();
        backgroundManager = BackgroundManager.getInstance();
        explosionManager = ExplosionManager.getInstance();
        friendlyManager = FriendlyManager.getInstance();
        playerStats = PlayerStats.getInstance();
        uiManager = GameUICreator.getInstance();
        textManager = OnScreenTextManager.getInstance();
        audioPosCalc = AudioPositionCalculator.getInstance();
        initBoard();
    }

    private void initBoard () {
        drawTimer = new Timer(gameState.getDELAY(), this);
        setDoubleBuffered(true);
    }

    public void startGame () {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        animationManager.resetManager();
        PlayerManager.getInstance().resetManager();
        PlayerManager.getInstance().createSpaceShip();
        uiManager.createGameBoardGUI();
        gameState.setGameState(GameStatusEnums.Zoning_In);
        drawTimer.start();
    }

    // Resets the game
    public void resetGame () {
        animationManager.resetManager();
        enemyManager.resetManager();
        missileManager.resetManager();
        levelManager.resetManager();
        playerManager.resetManager();
        audioManager.resetManager();
        backgroundManager.resetManager();
        explosionManager.resetManager();
        friendlyManager.resetManager();
        uiManager.resetManager();
        textManager.resetManager();
        playerStats.resetPlayerStats();
        gameState.resetGameState();
        this.drawTimer.setDelay(gameState.getDELAY());
        zoningInAlpha = 1.0f;
        zoningOutAlpha = 0.0f;
        inputDelay = 0;

        rnd = new Random().nextInt(strings.size());
        msg = strings.get(rnd);
    }

    private void resetManagersForNextLevel () {
        if (!hasResetManagersForNextLevel) {
            animationManager.resetManager();
            enemyManager.resetManager();
            missileManager.resetManager();
            levelManager = LevelManager.getInstance();
            playerManager.resetManager();
            audioManager.resetManager();
            explosionManager.resetManager();
            friendlyManager.resetManager();
            friendlyManager.resetPortal();
            uiManager.resetManager();
            textManager.resetManager();

            //These should probably to be refactored into osmething new
            hasResetManagersForNextLevel = true;
        }
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawObjects(g2d);
        if (gameState.getGameState() == GameStatusEnums.Dying) {
            playerManager.startDyingScene();
            this.drawTimer.setDelay(75);
        } else if (gameState.getGameState() == GameStatusEnums.Dead) {
            drawEndOfLevelScreen(g2d, false);
        } else if (gameState.getGameState() == GameStatusEnums.Show_Level_Score_Card) {
            drawEndOfLevelScreen(g2d, true);
        } else if (gameState.getGameState() == GameStatusEnums.Transition_To_Next_Stage) {
            drawZoningOut(g2d);
            goToNextLevel();
        } else if (gameState.getGameState() == GameStatusEnums.Zoning_In) {
            drawZoningIn(g2d);
        } else if (gameState.getGameState() == GameStatusEnums.Zoning_Out) {
            drawZoningOut(g2d);
        }
//        else if (gameState.getGameState() == GameStatusEnums.Album_Completed) {
//            drawVictoryScreen(g2d);
//        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void goToNextLevel () {
        if (zoningOutAlpha >= 1) {
            playerManager.getSpaceship().setX(DataClass.getInstance().getWindowWidth() / 10);
            playerManager.getSpaceship().setY(DataClass.getInstance().getWindowHeight() / 2);
            gameState.setGameState(GameStatusEnums.Shopping);
            gameState.setStagesCompleted(gameState.getStagesCompleted() + 1);
            backgroundManager.resetManager();
            zoningInAlpha = 1;
            zoningOutAlpha = 0;
            hasResetManagersForNextLevel = false;
            resetManagersForNextLevel();

            drawTimer.stop();
            BoardManager.getInstance().openShopWindow();
            GameStatsTracker.getInstance().resetStatsForNextRound();
        }
    }

    // Draw the game over screen
    private void drawEndOfLevelScreen (Graphics2D g, boolean hasSurvived) {
        //Create font
        Font font = new Font("Monospaced", Font.PLAIN, 15);
        FontMetrics fm = getFontMetrics(font);
        g.setColor(Color.white);
        g.setFont(font);

        //Draw the background
        GameUICreator gameUICreator = GameUICreator.getInstance();
        UIObject gameOverCard = gameUICreator.getGameOverCard();
        drawImage(g, gameOverCard);



        //Draw the first column of messages

        GameStatsTracker gameStatsTracker = GameStatsTracker.getInstance();
        int firstRowXCoordinate = Math.round(gameOverCard.getXCoordinate() + (gameOverCard.getWidth() * 0.1f));
        int firstRowYCoordinate = Math.round(gameOverCard.getYCoordinate() + (gameOverCard.getHeight() * 0.3f));
        int messageHeight = 24;
        String  msgToDraw = "Enemies killed this level:             " + gameStatsTracker.getEnemiesKilledThisRound();


        if(hasSurvived) {
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Enemies spawned this level:            " + gameStatsTracker.getEnemiesSpawnedThisRound();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Enemies missed this level:             " + gameStatsTracker.getAmountOfEnemiesMissedThisRound();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

//            firstRowYCoordinate += messageHeight;
//            msgToDraw = "Enemy percentage killed: " + gameStatsTracker.getPercentageOfEnemiesKilledThisRound();
//            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Minerals acquired this level:          " + gameStatsTracker.getMineralsGainedThisRound();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Damage taken this round:               " + gameStatsTracker.getDamageTakenThisRound();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Highest hit so far:                    " + gameStatsTracker.getHighestDamageDealt();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);


        } else {
            msgToDraw = "Total enemies killed:                  " + gameStatsTracker.getEnemiesKilled();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Total enemies spawned:                 " + gameStatsTracker.getEnemiesSpawned();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Total spawned enemies killed:          " + gameStatsTracker.getPercentageOfTotalSpawnedEnemiesKilled() + "%";
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Highest damage dealt:                  " + gameStatsTracker.getHighestDamageDealt();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Total damage done:                     " + gameStatsTracker.getTotalDamageDealt();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Total damage taken:                    " + gameStatsTracker.getTotalDamageTaken();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Amount of missiles fired:              " + gameStatsTracker.getShotsFired();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Amount of missiles that hit a target:  " + gameStatsTracker.getShotsHit();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Accuracy:                              " + gameStatsTracker.getAccuracy() + "%";
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);

            firstRowYCoordinate += messageHeight;
            msgToDraw = "Amount of minerals collected:          " + gameStatsTracker.getMineralsAcquired();
            g.drawString(msgToDraw, firstRowXCoordinate, firstRowYCoordinate);
        }



        //Draw the UIObjects
        UIObject grade = gameUICreator.getGradeSC2iconObject();
        UIObject gradeText = gameUICreator.getGradeTextObject();
        UIObject titleText = gameUICreator.getGameOverCardTitle();


        if (hasSurvived) {
            grade.changeImage(gameStatsTracker.getGradeImageTypeByCurrentRoundScore());
            titleText.changeImage(ImageEnums.UILevelComplete);
            msgToDraw = "Enemy percentage killed: " + gameStatsTracker.getPercentageOfEnemiesKilledThisRound() + "%";
            int percentageKilledXCoordinate = gradeText.getXCoordinate() + 10;
            int percentageKilledYCoordinate = grade.getYCoordinate() + Math.round(grade.getHeight() * 1.2f);
            g.drawString(msgToDraw, percentageKilledXCoordinate, percentageKilledYCoordinate);

        } else {
            ImageEnums randomGameOverPeepo = gameUICreator.getRandomGameOverPeepo();
            if(!grade.getImageEnum().equals(randomGameOverPeepo)) {
                int oldXCenter = grade.getCenterXCoordinate();
                int oldYCenter = grade.getCenterYCoordinate();
                grade.changeImage(randomGameOverPeepo);
                grade.setImageDimensions(112, 112);
                grade.rotateImage(Direction.LEFT);
                grade.setCenterCoordinates(oldXCenter, oldYCenter);
            }

            if (!titleText.getImageEnum().equals(ImageEnums.UIYouDied)) {
                int oldXCenter = titleText.getCenterXCoordinate();
                int oldYCenter = titleText.getCenterYCoordinate();
                titleText.changeImage(ImageEnums.UIYouDied);
                titleText.setCenterCoordinates(oldXCenter, oldYCenter);
            }

        }
        drawImage(g, grade);
        drawImage(g, gradeText);
        drawImage(g, titleText);


        //Draw the next level/main menu instructions
        if (inputDelay > MOVE_COOLDOWN) {
            msgToDraw = "Press fire to go back to the main menu";
            int goNextYCoordinate = Math.round(gameOverCard.getYCoordinate() + (gameOverCard.getHeight() * 0.9f));
            g.drawString(msgToDraw, firstRowXCoordinate, goNextYCoordinate);
        }

        //Reset the drawTimer delay to the regular speed because slow-mo death animation slows it
        drawTimer.setDelay(gameState.getDELAY());
    }

    private void drawZoningIn (Graphics2D g2d) {
        if (zoningInAlpha <= 0) {
            return;
        }
        // Set color to black with the current alpha
        g2d.setColor(new Color(0, 0, 0, zoningInAlpha));

        // Draw a rectangle covering the whole screen
        g2d.fillRect(0, 0, boardWidth, boardHeight);

        // Decrease alpha for next time
        zoningInAlpha -= 0.02f; // Adjust this value to control the speed of the fade
    }

    private void drawZoningOut (Graphics2D g2d) {
        // If the black screen is already fully opaque, just return
        if (zoningOutAlpha >= 1) {
            return;
        }

        // Set color to black with the current alpha
        g2d.setColor(new Color(0, 0, 0, zoningOutAlpha));

        // Draw a rectangle covering the whole screen
        g2d.fillRect(0, 0, boardWidth, boardHeight);

        // Increase alpha for next time
        zoningOutAlpha += 0.02f; // Adjust this value to control the speed of the fade
    }

    private void drawObjects (Graphics2D g) {
        // Draws all background objects
        for (BackgroundObject bgObject : backgroundManager.getAllBGO()) {
            drawImage(g, bgObject);
        }

        // Draws lower level animations
        for (SpriteAnimation animation : animationManager.getLowerAnimations()) {
            drawAnimation(g, animation);
        }

        for (Missile missile : missileManager.getMissiles()) {
            if (missile.isVisible()) {
                if (missile.getAnimation() != null) {
                    drawAnimation(g, missile.getAnimation());
                } else {
                    drawImage(g, missile);
                }

                if (missile.isShowHealthBar())
                    drawHealthBars(g, missile);
            }
        }

        // Draw enemies
        for (Enemy enemy : enemyManager.getEnemies()) {
            if (enemy.isVisible()) {
                if (enemy.getAnimation() != null) {
                    drawAnimation(g, enemy.getAnimation());
                } else {
                    drawImage(g, enemy);
                }

                if (enemy.isShowHealthBar())
                    drawHealthBars(g, enemy);
            }
        }

        //Should be handled by the animation manager to allow recentering
//        for (Explosion explosion : explosionManager.getExplosions()) {
//            if (explosion.isVisible()) {
//                drawAnimation(g, explosion.getAnimation());
//            }
//        }

        for (FriendlyObject friendly : friendlyManager.getFriendlyObjects()) {
            if (friendly.isVisible()) {
                drawAnimation(g, friendly.getAnimation());
            }
        }

        // Draw friendly spaceship
        if (playerManager.getSpaceship().isVisible()) {
            drawImage(g, playerManager.getSpaceship());
        }

        for (SpecialAttack specialAttack : missileManager.getSpecialAttacks()) {
            if (specialAttack.isVisible()) {
                if (specialAttack.getAnimation() != null) {
                    drawAnimation(g, specialAttack.getAnimation());
                } else {
                    drawImage(g, specialAttack);
                }
            }
        }

        for (UIObject obj : uiManager.getInformationCards()) {
            drawImage(g, obj);
        }


        drawPlayerHealthBars(g);
        drawSpecialAttackFrame(g);
        drawSongProgressBar(g);

        if (uiManager.getDifficultyWings() != null) {
            UIObject wings = uiManager.getDifficultyWings();
//            Font font = new Font("Helvetica", Font.PLAIN, 10);
//            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString("Difficulty Coefficient: " + levelManager.getCurrentDifficultyCoeff(), wings.getXCoordinate() + (wings.getWidth()), wings.getYCoordinate() + Math.round(wings.getHeight() * 0.25));
            drawImage(g, uiManager.getDifficultyWings());
        }

        // Draws higher level animations
        for (SpriteAnimation animation : animationManager.getUpperAnimations()) {
            drawAnimation(g, animation);
        }

        for (OnScreenText text : textManager.getOnScreenTexts()) {
            drawText(g, text);
        }

        g.setColor(Color.WHITE);
        g.drawString("Difficulty coeff: " + gameState.getDifficultyCoefficient(), 350, DataClass.getInstance().getPlayableWindowMaxHeight() + 25);
        g.drawString("Current stage: " + gameState.getStagesCompleted(), 350, DataClass.getInstance().getPlayableWindowMaxHeight() + 45);
        g.drawString("Enemy level: " + gameState.getMonsterLevel(), 350, DataClass.getInstance().getPlayableWindowMaxHeight() + 65);

//        g.drawString("Enemies spawned: " + levelManager.getEnemiesSpawned(), 550, DataClass.getInstance().getPlayableWindowMaxHeight() + 25);
//        g.drawString("Enemies killed: " + levelManager.getEnemiesKilled(), 550, DataClass.getInstance().getPlayableWindowMaxHeight() + 45);
//        g.drawString("Enemies alive: " + enemyManager.getEnemyCount(), 550, DataClass.getInstance().getPlayableWindowMaxHeight() + 65);

        g.drawString("Player level: " + Math.round(playerStats.getCurrentLevel()), 550, DataClass.getInstance().getPlayableWindowMaxHeight() + 25);
        g.drawString("XP to next level: " + Math.round(playerStats.getXpToNextLevel() - playerStats.getCurrentXP()), 550, DataClass.getInstance().getPlayableWindowMaxHeight() + 45);


    }

    private void drawText (Graphics2D g, OnScreenText text) {
        // Ensure that transparency value is within the appropriate bounds.
        float transparency = Math.max(0, Math.min(1, text.getTransparencyValue()));
        Color originalColor = g.getColor(); // store the original color
        Font originalFont = g.getFont();

        // Set the color with the specified transparency.
        Color colorWithTransparency = new Color(
                text.getColor().getRed(),
                text.getColor().getGreen(),
                text.getColor().getBlue(),
                (int) (transparency * 255) // alpha value must be between 0 and 255
        );

//        g.setColor(new Color(1.0f, 1.0f, 1.0f, transparency)); // White with transparency
        g.setColor(colorWithTransparency);
        g.setFont(new Font("Helvetica", Font.PLAIN, text.getFontSize()));
        // Draw the text at the current coordinates.
        g.drawString(text.getText(), text.getXCoordinate(), text.getYCoordinate());

        // Update the Y coordinate of the text to make it scroll upwards.
        text.setYCoordinate(text.getYCoordinate() - 1);

        // Decrease the transparency for the next draw
        text.setTransparency(transparency - text.getTransparancyStepSize()); // decrease transparency

        g.setColor(originalColor); // restore the original color
        g.setFont(originalFont);
    }

    private void drawImage (Graphics2D g, Sprite sprite) {
        if (sprite.getImage() != null) {
            // Save the original composite
            Composite originalComposite = g.getComposite();

            // Set the alpha composite
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, sprite.getTransparancyAlpha()));

            // Draw the image
            g.drawImage(sprite.getImage(), sprite.getXCoordinate(), sprite.getYCoordinate(), this);

            // Reset to the original composite
            g.setComposite(originalComposite);
        }
    }


    private void drawAnimation (Graphics2D g, SpriteAnimation animation) {
        if (animation.getCurrentFrameImage(false) != null) {
            // Save the original composite
            Composite originalComposite = g.getComposite();
            // Set the alpha composite
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animation.getTransparancyAlpha()));

            g.drawImage(animation.getCurrentFrameImage(true), animation.getXCoordinate(), animation.getYCoordinate(),
                    this);

            // Reset to the original composite
            g.setComposite(originalComposite);
        }
    }

    // Primitive healthbar generator for enemies
    private void drawHealthBars (Graphics2D g, GameObject gameobject) {
        float factor = gameobject.getCurrentHitpoints() / gameobject.getMaxHitPoints();
        int actualAmount = (int) Math.round(gameobject.getHeight() * factor);


        g.setColor(Color.RED);
        g.fillRect((gameobject.getXCoordinate() + gameobject.getWidth() + 10), gameobject.getYCoordinate(), 2, gameobject.getHeight());
        g.setColor(Color.GREEN);
        g.fillRect((gameobject.getXCoordinate() + gameobject.getWidth() + 10), gameobject.getYCoordinate(), 2, actualAmount);
    }

    private void drawPlayerHealthBars (Graphics2D g) {
        float playerHealth = playerManager.getSpaceship().getCurrentHitpoints();
        float playerMaxHealth = playerStats.getMaxHitPoints();
        UIObject healthFrame = uiManager.getHealthFrame();
        UIObject healthBar = uiManager.getHealthBar();

        int healthBarWidth = GameUICreator.getInstance().calculateHealthbarWidth(playerHealth, playerMaxHealth);
        if (healthBarWidth > healthFrame.getWidth()) {
            healthBarWidth = healthFrame.getWidth();
        }

        healthBar.resizeToDimensions(healthBarWidth, healthBar.getHeight());
        drawImage(g, healthBar);
        drawImage(g, healthFrame);

        float playerShields = playerManager.getSpaceship().getCurrentShieldPoints();
        float playerMaxShields = playerStats.getMaxShieldHitPoints();
        UIObject shieldFrame = uiManager.getShieldFrame();
        UIObject shieldBar = uiManager.getShieldBar();

        int shieldBarWidth = GameUICreator.getInstance().calculateHealthbarWidth(playerShields, playerMaxShields);
        if (shieldBarWidth > shieldFrame.getWidth()) {
            shieldBarWidth = shieldFrame.getWidth();
        }

        shieldBar.resizeToDimensions(shieldBarWidth, shieldBar.getHeight());
        drawImage(g, shieldBar);

        // Check if currentShieldPoints exceed maximumShieldPoints
        if (playerShields > playerMaxShields) {
            UIObject overloadingShieldBar = uiManager.getOverloadingShieldBar();
            // Calculate the width for the overloading shield bar: playerMaxShields * 2 as the maximum theoretical width
            int overloadingShieldBarWidth = GameUICreator.getInstance().calculateHealthbarWidth(playerShields - playerMaxShields, playerMaxShields * PlayerStats.getInstance().getMaxOverloadingShieldMultiplier());
            if (overloadingShieldBarWidth > shieldFrame.getWidth()) {
                overloadingShieldBarWidth = shieldFrame.getWidth();
            }
            overloadingShieldBar.resizeToDimensions(overloadingShieldBarWidth, overloadingShieldBar.getHeight());
            drawImage(g, overloadingShieldBar);
        }

        drawImage(g, shieldFrame);
    }

    private void drawSongProgressBar (Graphics2D g) {
        long currentMusicFramePosition = 0;
        long maximumMusicFrames = 1;
        // Get the current and maximum frame positions of the background music
        if(AudioManager.getInstance().getBackgroundMusic() != null){
            currentMusicFramePosition = AudioManager.getInstance().getBackgroundMusic().getFramePosition();
            maximumMusicFrames = AudioManager.getInstance().getBackgroundMusic().getFrameLength();
        }


        UIObject progressBar = uiManager.getProgressBar();
        UIObject progressBarFilling = uiManager.getProgressBarFilling();
        UIObject spaceShipIndicator = uiManager.getProgressBarSpaceShipIndicator();

        // Calculate the width of the progress bar filling based on the current position of the song
        int progressBarWidth = GameUICreator.getInstance().calculateProgressBarFillingWidth(currentMusicFramePosition, maximumMusicFrames);

        // Resize the progress bar filling
        progressBarFilling.resizeToDimensions(progressBarWidth, progressBarFilling.getHeight());

        // Place the spaceship indicator at the edge of the filling
        spaceShipIndicator.setX((progressBarFilling.getXCoordinate() + progressBarFilling.getWidth()) - spaceShipIndicator.getWidth());

        // Draw the progress bar components
        drawImage(g, progressBar);
        drawImage(g, progressBarFilling);
        drawImage(g, spaceShipIndicator);

        if (levelManager.getCurrentLevelSong() != null) {
            g.setColor(Color.white);
            g.drawString("Song: " + levelManager.getCurrentLevelSong().toString(), progressBar.getXCoordinate(), progressBar.getYCoordinate() + progressBar.getHeight());
        }
    }

    private void drawSpecialAttackFrame (Graphics2D g) {
        drawImage(g, uiManager.getSpecialAttackFrame());
        SpaceShipSpecialGun gun = playerManager.getSpaceship().getSpecialGun();
        int charges = gun.getSpecialAttackCharges();

        if (charges > 0) {
            drawAnimation(g, uiManager.getSpecialAttackHighlight());
        } else {
            // Draw the cooldown progress bar only if there are no charges
            double remainingSeconds = gun.getCurrentSpecialAttackFrame();
            double totalCooldown = playerStats.getSpecialAttackSpeed();
            float percentage = (float) (1.0 - remainingSeconds / totalCooldown); // Properly compute the fill percentage
            int barWidth = (int) (uiManager.getSpecialAttackFrame().getWidth() * percentage);

            // Draw the cooldown progress bar
            g.setColor(new Color(160, 160, 160, 160)); // Semi-transparent gray
            g.fillRect(uiManager.getSpecialAttackFrame().getXCoordinate(),
                    uiManager.getSpecialAttackFrame().getYCoordinate(), barWidth,
                    uiManager.getSpecialAttackFrame().getHeight());
        }

        if (charges > 1) {
            g.setColor(Color.green);
            g.drawString(String.valueOf(charges),
                    uiManager.getSpecialAttackFrame().getXCoordinate() + uiManager.getSpecialAttackFrame().getWidth() - 20,
                    uiManager.getSpecialAttackFrame().getYCoordinate() + uiManager.getSpecialAttackFrame().getHeight() - 10
            );

        }
    }


    // Called on every action/input. Essentially the infinite loop that plays
    public void actionPerformed (ActionEvent e) {
        if (gameState.getGameState() == GameStatusEnums.Zoning_In) {
            if (this.zoningInAlpha <= 0.05) {
                levelManager.startLevel();
                zoningInAlpha = 1.0f;
            }
        }

        if (gameState.getGameState() != GameStatusEnums.Dead) {
            if (gameState.getSpawningMechanic() == SpawningMechanic.PreGeneratedLevels && AudioManager.getInstance().getBackgroundMusic() != null) {
                gameState.setMusicSeconds(
                        audioPosCalc.getPlaybackTimeInSeconds(audioManager.getBackgroundMusic().getClip(),
                                audioManager.getBackgroundMusic().getFramePosition()));
//				musicSeconds = AudioManager.getInstance().getBackgroundMusic().getFramePosition();
            }
//			else {
////            Used for playing a level without it's actual track, should be deprecated with director spawning
//				gameState.setMusicSeconds((float) (gameState.getMusicSeconds() + 0.05));
//			}
            playerManager.updateGameTick();
            missileManager.updateGameTick();
            enemyManager.updateGameTick();
            levelManager.updateGameTick();
            animationManager.updateGameTick();
            backgroundManager.updateGameTick();
            audioDatabase.updateGameTick();
            explosionManager.updateGametick();
            friendlyManager.updateGameTick();

//            if(gameState.getGameState().equals(GameStatusEnums.Playing)) {
            gameState.addGameTicks(1);
            DirectorManager.getInstance().updateGameTick();
//            }
            executeControllerInput();
        }

        if (gameState.getGameState() == GameStatusEnums.Dead || gameState.getGameState().equals(GameStatusEnums.Show_Level_Score_Card)) {
            inputDelay++;
        }

        if (lastKnownState == null || lastKnownState != gameState.getGameState()) {
            lastKnownState = gameState.getGameState();
            System.out.println(lastKnownState);
        }


        repaint(0, 0, DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight());
    }

    public Timer getTimer () {
        return drawTimer;
    }

    // Required to read key presses
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased (KeyEvent e) {
            playerManager.getSpaceship().keyReleased(e);
        }

        @Override
        public void keyPressed (KeyEvent e) {

            if(boardManager == null){
                boardManager = BoardManager.getInstance();
            }

            if (gameState.getGameState() == GameStatusEnums.Dead) {
                if (inputDelay >= MOVE_COOLDOWN) {
                    boardManager.gameToMainMenu();
                    drawTimer.stop();
                    inputDelay = 0;
                    GameStatsTracker.getInstance().resetGameStatsTracker();
                }
            }
            if (gameState.getGameState() == GameStatusEnums.Show_Level_Score_Card) {
                if (inputDelay >= MOVE_COOLDOWN) {
                    gameState.setGameState(GameStatusEnums.Transition_To_Next_Stage);
                    inputDelay = 0;
                }
            } else {
                playerManager.getSpaceship().keyPressed(e);
            }
        }

    }

    public void executeControllerInput () {
        if(boardManager == null){
            boardManager = BoardManager.getInstance();
        }

        if (controllers.getFirstController() != null) {
            if (gameState.getGameState() == GameStatusEnums.Dead) {
                controllers.getFirstController().pollController();
                if (controllers.getFirstController().isInputActive(ControllerInputEnums.FIRE)) {
                    boardManager.gameToMainMenu();
                    inputDelay = 0;
                    GameStatsTracker.getInstance().resetGameStatsTracker();
                    drawTimer.stop();
                }

            } else if (gameState.getGameState() == GameStatusEnums.Show_Level_Score_Card) {
                controllers.getFirstController().pollController();
                if (controllers.getFirstController().isInputActive(ControllerInputEnums.FIRE)) {
                    gameState.setGameState(GameStatusEnums.Transition_To_Next_Stage);
                    inputDelay = 0;
                }
            } else {
                playerManager.getSpaceship().update(controllers.getFirstController());
            }
        }

    }
}
