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

import javax.swing.JPanel;
import javax.swing.Timer;

import controllerInput.ConnectedControllers;
import controllerInput.ControllerInputEnums;
import game.UI.UIObject;
import game.spawner.directors.DirectorManager;
import game.gamestate.SpawningMechanic;
import game.spawner.LevelManager;
import game.managers.AnimationManager;
import game.UI.GameUIManager;
import game.objects.neutral.ExplosionManager;
import game.managers.OnScreenTextManager;
import game.objects.friendlies.FriendlyObject;
import game.objects.missiles.MissileTypeEnums;
import game.objects.player.PlayerManager;
import game.managers.TimerManager;
import game.objects.background.BackgroundManager;
import game.objects.background.BackgroundObject;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.objects.friendlies.FriendlyManager;
import game.objects.powerups.PowerUp;
import game.util.OnScreenText;
import game.objects.powerups.PowerUpManager;
import game.objects.player.PlayerSpecialAttackTypes;
import game.objects.player.spaceship.SpaceShipSpecialGun;
import game.objects.player.specialAttacks.SpecialAttack;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileManager;
import game.objects.player.playerpresets.GunPreset;
import game.objects.player.playerpresets.SpecialGunPreset;
import game.objects.player.BoostsUpgradesAndBuffsSettings;
import VisualAndAudioData.DataClass;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatusEnums;
import game.objects.player.PlayerStats;
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

    private long currentWaitingTime = 0;
    private static final long MOVE_COOLDOWN = 200;

    private boolean resetManagersForNextLevel = false;

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
    private TimerManager timerManager = TimerManager.getInstance();
    private ExplosionManager explosionManager = ExplosionManager.getInstance();
    private FriendlyManager friendlyManager = FriendlyManager.getInstance();
    private PlayerStats playerStats = PlayerStats.getInstance();
    private GameUIManager uiManager = GameUIManager.getInstance();
    private PowerUpManager powerUpManager = PowerUpManager.getInstance();
    private OnScreenTextManager textManager = OnScreenTextManager.getInstance();
    private BoostsUpgradesAndBuffsSettings tempSettings = BoostsUpgradesAndBuffsSettings.getInstance();
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
        timerManager = TimerManager.getInstance();
        explosionManager = ExplosionManager.getInstance();
        friendlyManager = FriendlyManager.getInstance();
        playerStats = PlayerStats.getInstance();
        uiManager = GameUIManager.getInstance();
        powerUpManager = PowerUpManager.getInstance();
        textManager = OnScreenTextManager.getInstance();
        audioPosCalc = AudioPositionCalculator.getInstance();
        tempSettings = BoostsUpgradesAndBuffsSettings.getInstance();
        initBoard();
    }

    private void initBoard () {
        drawTimer = new Timer(gameState.getDELAY(), this);
    }

    public void startGame () {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        animationManager.resetManager();
        PlayerManager.getInstance().getSpaceship().resetSpaceship();

        // Dit moet uit een "out-of-game state manager" gehaald worden
        if (playerStats.getNormalGunPreset() == null) {
            GunPreset preset = new GunPreset(MissileTypeEnums.DefaultPlayerLaserbeam);
            playerStats.setNormalGunPreset(preset);
        }
        playerStats.getNormalGunPreset().loadPreset();

        // Dit moet uit een "out-of-game state manager" gehaald worden
        if (playerStats.getSpecialGunPreset() == null) {
            SpecialGunPreset preset = new SpecialGunPreset(PlayerSpecialAttackTypes.EMP);
            playerStats.setSpecialGunPreset(preset);
        }
        playerStats.getSpecialGunPreset().loadPreset();
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
        timerManager.resetManager();
        explosionManager.resetManager();
        friendlyManager.resetManager();
        uiManager.resetManager();
        powerUpManager.resetManager();
        textManager.resetManager();
        playerStats.resetPlayerStats();
        tempSettings.resetGameSettings();
        gameState.resetGameState();
        this.drawTimer.setDelay(gameState.getDELAY());
        zoningInAlpha = 1.0f;
        zoningOutAlpha = 0.0f;
        currentWaitingTime = 0;

        rnd = new Random().nextInt(strings.size());
        msg = strings.get(rnd);

        if (playerStats.getNormalGunPreset() == null) {
            GunPreset preset = new GunPreset(MissileTypeEnums.DefaultPlayerLaserbeam);
            playerStats.setNormalGunPreset(preset);
        }
        playerStats.getNormalGunPreset().loadPreset();

        if (playerStats.getSpecialGunPreset() == null) {
            SpecialGunPreset preset = new SpecialGunPreset(PlayerSpecialAttackTypes.EMP);
            playerStats.setSpecialGunPreset(preset);
        }
    }

    private void resetManagersForNextLevel () {
        if (!resetManagersForNextLevel) {
            animationManager.resetManager();
            enemyManager.resetManager();
            missileManager.resetManager();
            levelManager = LevelManager.getInstance();
            playerManager.resetManager();
            audioManager.resetManager();
            timerManager.resetManager();
            explosionManager.resetManager();
            friendlyManager.resetManager();
            friendlyManager.resetPortal();
            uiManager.resetManager();
            powerUpManager.resetManager();
            textManager.resetManager();

            //These should probably to be refactored into osmething new
            tempSettings = BoostsUpgradesAndBuffsSettings.getInstance();
            resetManagersForNextLevel = true;
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
            drawGameOver(g2d);
        } else if (gameState.getGameState() == GameStatusEnums.Transitioning_To_Next_Level) {
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
            resetManagersForNextLevel = false;
            resetManagersForNextLevel();

            drawTimer.stop();
            BoardManager.getInstance().openShopWindow();
        }
    }

    // Draw the game over screen
    private void drawGameOver (Graphics2D g) {

        Font font = new Font("Helvetica", Font.BOLD, 25);
        FontMetrics fm = getFontMetrics(font);
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (boardWidth - fm.stringWidth(msg)) / 2, boardHeight / 2);

        if (boardManager == null) {
            boardManager = BoardManager.getInstance();
        }
        drawTimer.setDelay(gameState.getDELAY());
    }

    private void drawVictoryScreen (Graphics2D g) {
        String msg = "Yoooo you beat the level";
        Font font = new Font("Helvetica", Font.BOLD, 25);
        FontMetrics fm = getFontMetrics(font);
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (boardWidth - fm.stringWidth(msg)) / 2, boardHeight / 2);

        if (boardManager == null) {
            boardManager = BoardManager.getInstance();
        }
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

        for (PowerUp powerUp : powerUpManager.getPowerUpsOnTheField()) {
            if (powerUp.isVisible()) {
                drawImage(g, powerUp);
            }
        }

        for(UIObject obj : uiManager.getInformationCards()){
            drawImage(g, obj);
        }


        drawPlayerHealthBars(g);
        drawSpecialAttackFrame(g);

        if(uiManager.getDifficultyWings() != null) {
            drawImage(g, uiManager.getDifficultyWings());
        }

        // Draws higher level animations
        for (SpriteAnimation animation : animationManager.getUpperAnimations()) {
            drawAnimation(g, animation);
        }

        for (OnScreenText text : textManager.getOnScreenTexts()) {
            drawText(g, text);
        }

        // Draw the score/aliens left
        g.setColor(Color.WHITE);
        g.drawString("Enemies alive: " + enemyManager.getEnemyCount(), 650, DataClass.getInstance().getPlayableWindowMaxHeight() + 65);

        g.drawString("Difficulty coeff: " + gameState.getDifficultyCoefficient(), 450, DataClass.getInstance().getPlayableWindowMaxHeight() + 25);
        g.drawString("Current stage: " + gameState.getStagesCompleted(), 450, DataClass.getInstance().getPlayableWindowMaxHeight() + 45);
        g.drawString("Enemy level: " + gameState.getMonsterLevel(), 450, DataClass.getInstance().getPlayableWindowMaxHeight() + 65);

        g.drawString("Enemies spawned: " + levelManager.getEnemiesSpawned(), 650, DataClass.getInstance().getPlayableWindowMaxHeight() + 25);
        g.drawString("Enemies killed: " + levelManager.getEnemiesKilled(), 650, DataClass.getInstance().getPlayableWindowMaxHeight() + 45);
        g.drawString("Player level: " + playerStats.getCurrentLevel(), 950, DataClass.getInstance().getPlayableWindowMaxHeight() + 25);
        g.drawString("XP to next level: " + (playerStats.getXpToNextLevel() - playerStats.getCurrentXP()), 950, DataClass.getInstance().getPlayableWindowMaxHeight() + 45);

    }

    private void drawText (Graphics2D g, OnScreenText text) {
        // Ensure that transparency value is within the appropriate bounds.
        float transparency = Math.max(0, Math.min(1, text.getTransparencyValue()));
        Color originalColor = g.getColor(); // store the original color

        // Set the color with the specified transparency.
        g.setColor(new Color(1.0f, 1.0f, 1.0f, transparency)); // White with transparency

        // Draw the text at the current coordinates.
        g.drawString(text.getText(), text.getXCoordinate(), text.getYCoordinate());

        // Update the Y coordinate of the text to make it scroll upwards.
        text.setYCoordinate(text.getYCoordinate() - 1);

        // Decrease the transparency for the next draw
        text.setTransparency(transparency - 0.01f); // decrease transparency

        g.setColor(originalColor); // restore the original color
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
    private void drawHealthBars (Graphics2D g, Enemy enemy) {
        float factor = enemy.getCurrentHitpoints() / enemy.getMaxHitPoints();
        int actualAmount = (int) Math.round(enemy.getHeight() * factor);

        g.setColor(Color.RED);
        g.fillRect((enemy.getXCoordinate() + enemy.getWidth() + 10), enemy.getYCoordinate(), 2, enemy.getHeight());
        g.setColor(Color.GREEN);
        g.fillRect((enemy.getXCoordinate() + enemy.getWidth() + 10), enemy.getYCoordinate(), 2, actualAmount);
    }

    private void drawPlayerHealthBars (Graphics2D g) {
        float playerHealth = playerManager.getSpaceship().getCurrentHitpoints();
        float playerMaxHealth = playerStats.getMaxHitPoints();
        UIObject healthFrame = uiManager.getHealthFrame();
        UIObject healthBar = uiManager.getHealthBar();

        int healthBarWidth = calculateHealthbarWidth(playerHealth, playerMaxHealth);
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

        int shieldBarWidth = calculateHealthbarWidth(playerShields, playerMaxShields);
        if (shieldBarWidth > shieldFrame.getWidth()) {
            shieldBarWidth = shieldFrame.getWidth();
        }

        shieldBar.resizeToDimensions(shieldBarWidth, shieldBar.getHeight());
        drawImage(g, shieldBar);

        // Check if currentShieldPoints exceed maximumShieldPoints
        if (playerShields > playerMaxShields) {
            UIObject overloadingShieldBar = uiManager.getOverloadingShieldBar();
            // Calculate the width for the overloading shield bar: playerMaxShields * 2 as the maximum theoretical width
            int overloadingShieldBarWidth = calculateHealthbarWidth(playerShields - playerMaxShields, playerMaxShields * PlayerStats.getInstance().getMaxOverloadingShieldMultiplier());
            if (overloadingShieldBarWidth > shieldFrame.getWidth()) {
                overloadingShieldBarWidth = shieldFrame.getWidth();
            }
            overloadingShieldBar.resizeToDimensions(overloadingShieldBarWidth, overloadingShieldBar.getHeight());
            drawImage(g, overloadingShieldBar);
        }

        drawImage(g, shieldFrame);
    }


    private void drawSpecialAttackFrame (Graphics2D g) {
        drawImage(g, uiManager.getSpecialAttackFrame());
        SpaceShipSpecialGun gun = playerManager.getSpaceship().getSpecialGun();
        int charges = gun.getSpecialAttackCharges();
        if (charges > 0) {
            drawAnimation(g, uiManager.getSpecialAttackHighlight());
        } else {
            // Only draw the charging rectangle if there are no charges
            float percentage = (float) gun.getCurrentSpecialAttackFrame() / playerStats.getSpecialAttackSpeed();
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


    public int calculateHealthbarWidth (float currentHitpoints, float maximumHitpoints) {
        // Calculate the percentage of currentHitpoints out of maximumHitpoints
        double percentage = (double) currentHitpoints / maximumHitpoints * 100;
        // Calculate what this percentage is of thirdNumber
        int width = (int) Math.ceil((percentage / 100) * uiManager.getHealthBarWidth());

        if (width > uiManager.getHealthBarWidth()) {
            width = uiManager.getHealthBarWidth();
        } else if (width < 1) {
            width = 1;
        }

        return width;
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
            timerManager.updateGameTick(gameState.getMusicSeconds());
            audioDatabase.updateGameTick();
            explosionManager.updateGametick();
            friendlyManager.updateGameTick();
            powerUpManager.updateGameTick();
            gameState.addGameTicks(1);
            DirectorManager.getInstance().updateGameTick();
            executeControllerInput();
        }

        if (gameState.getGameState() == GameStatusEnums.Dead) {
            currentWaitingTime++;
        }

        if (lastKnownState == null || lastKnownState != gameState.getGameState()) {
            lastKnownState = gameState.getGameState();
            System.out.println(lastKnownState);
        }


        repaint();
    }

    // Required to read key presses
    private class TAdapter extends KeyAdapter {


        @Override
        public void keyReleased (KeyEvent e) {
            playerManager.getSpaceship().keyReleased(e);
        }

        @Override
        public void keyPressed (KeyEvent e) {
            if (gameState.getGameState() == GameStatusEnums.Dead) {
                if (currentWaitingTime >= MOVE_COOLDOWN) {
                    boardManager.gameToMainMenu();
                    drawTimer.stop();
                }
            } else {
                playerManager.getSpaceship().keyPressed(e);
            }
        }

    }

    public void executeControllerInput () {
        if (controllers.getFirstController() != null) {
            if (gameState.getGameState() == GameStatusEnums.Dead) {
                controllers.getFirstController().pollController();
                if (controllers.getFirstController().isInputActive(ControllerInputEnums.FIRE)) {
                    boardManager.gameToMainMenu();
                    drawTimer.stop();
                }

            } else {
                playerManager.getSpaceship().update(controllers.getFirstController());
            }
        }

    }
}
