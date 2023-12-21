package menuscreens.boards;

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
import controllerInput.ControllerInput;
import controllerInput.ControllerInputReader;
import game.UI.UIObject;
import game.levels.LevelManager;
import game.managers.AnimationManager;
import game.managers.GameUIManager;
import game.managers.ExplosionManager;
import game.managers.OnScreenTextManager;
import game.managers.PlayerManager;
import game.managers.TimerManager;
import game.objects.background.BackgroundManager;
import game.objects.background.BackgroundObject;
import game.objects.friendlies.Drones.GuardianDrone;
import game.objects.neutral.Explosion;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.objects.friendlies.FriendlyManager;
import game.objects.friendlies.powerups.PowerUp;
import game.objects.friendlies.powerups.OnScreenText;
import game.objects.friendlies.powerups.PowerUpManager;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.friendlies.spaceship.PlayerSpecialAttackTypes;
import game.objects.friendlies.spaceship.SpaceShipSpecialGun;
import game.objects.friendlies.spaceship.specialAttacks.SpecialAttack;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileManager;
import game.playerpresets.GunPreset;
import game.playerpresets.SpecialGunPreset;
import gamedata.BoostsUpgradesAndBuffsSettings;
import gamedata.DataClass;
import gamedata.GameStateInfo;
import gamedata.GameStatusEnums;
import gamedata.PlayerStats;
import gamedata.audio.AudioDatabase;
import gamedata.audio.AudioManager;
import gamedata.audio.AudioPositionCalculator;
import menuscreens.BoardManager;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

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
	private FriendlyManager friendlyMover = FriendlyManager.getInstance();
	private PlayerStats playerStats = PlayerStats.getInstance();
	private GameUIManager uiManager = GameUIManager.getInstance();
	private PowerUpManager powerUpManager = PowerUpManager.getInstance();
	private OnScreenTextManager textManager = OnScreenTextManager.getInstance();
	private BoostsUpgradesAndBuffsSettings tempSettings = BoostsUpgradesAndBuffsSettings.getInstance();
	private AudioPositionCalculator audioPosCalc = AudioPositionCalculator.getInstance();

	private ConnectedControllers controllers = ConnectedControllers.getInstance();

	public GameBoard() {
		animationManager = AnimationManager.getInstance();
		enemyManager = EnemyManager.getInstance();
		missileManager = MissileManager.getInstance();
		levelManager = LevelManager.getInstance();
		playerManager = PlayerManager.getInstance();
		audioManager = AudioManager.getInstance();
		backgroundManager = BackgroundManager.getInstance();
		timerManager = TimerManager.getInstance();
		explosionManager = ExplosionManager.getInstance();
		friendlyMover = FriendlyManager.getInstance();
		playerStats = PlayerStats.getInstance();
		uiManager = GameUIManager.getInstance();
		powerUpManager = PowerUpManager.getInstance();
		textManager = OnScreenTextManager.getInstance();
		audioPosCalc = AudioPositionCalculator.getInstance();
		tempSettings = BoostsUpgradesAndBuffsSettings.getInstance();
		initBoard();
	}

	private void initBoard() {
		drawTimer = new Timer(gameState.getDELAY(), this);
	}

	public void startGame() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		animationManager.resetManager();
		PlayerManager.getInstance().getSpaceship().resetSpaceship();
		// Dit moet uit een "out-of-game state manager" gehaald worden
		if (playerStats.getNormalGunPreset() == null) {
			GunPreset preset = new GunPreset(PlayerAttackTypes.Laserbeam);
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
	public void resetGame() {
		animationManager.resetManager();
		enemyManager.resetManager();
		missileManager.resetManager();
		levelManager.resetManager();
		playerManager.resetManager();
		audioManager.resetManager();
		backgroundManager.resetManager();
		timerManager.resetManager();
		explosionManager.resetManager();
		friendlyMover.resetManager();
		uiManager.resetManager();
		powerUpManager.resetManager();
		textManager.resetManager();
		playerStats.resetPlayerStats();
		tempSettings.resetGameSettings();
		this.drawTimer.setDelay(gameState.getDELAY());
		zoningInAlpha = 1.0f;
		zoningOutAlpha = 0.0f;
		currentWaitingTime = 0;
		

		rnd = new Random().nextInt(strings.size());
		msg = strings.get(rnd);

		if (playerStats.getNormalGunPreset() == null) {
			GunPreset preset = new GunPreset(PlayerAttackTypes.Laserbeam);
			playerStats.setNormalGunPreset(preset);
		}
		playerStats.getNormalGunPreset().loadPreset();

		if (playerStats.getSpecialGunPreset() == null) {
			SpecialGunPreset preset = new SpecialGunPreset(PlayerSpecialAttackTypes.EMP);
			playerStats.setSpecialGunPreset(preset);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
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
			playerManager.resetSpaceshipForNextLevel();
			friendlyMover.resetManagerForNextLevel();
			powerUpManager.resetManager();
			goToNextLevel();
		} else if (gameState.getGameState() == GameStatusEnums.Zoning_In) {
			drawZoningIn(g2d);
		} else if (gameState.getGameState() == GameStatusEnums.Zoning_Out) {
			drawZoningOut(g2d);
		} else if (gameState.getGameState() == GameStatusEnums.Album_Completed) {
			drawVictoryScreen(g2d);
		} 

		Toolkit.getDefaultToolkit().sync();
	}

	private void goToNextLevel() {
		if (zoningOutAlpha >= 1) {
			playerManager.getSpaceship().setX(DataClass.getInstance().getWindowWidth() / 10);
			playerManager.getSpaceship().setY(DataClass.getInstance().getWindowHeight() / 2);
			gameState.setGameState(GameStatusEnums.Zoning_In);
		}
	}

	// Draw the game over screen
	private void drawGameOver(Graphics2D g) {

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

	private void drawVictoryScreen(Graphics2D g) {
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

	private void drawZoningIn(Graphics2D g2d) {
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

	private void drawZoningOut(Graphics2D g2d) {
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

	private void drawObjects(Graphics2D g) {
		// Draws all background objects
		for (BackgroundObject bgObject : backgroundManager.getAllBGO()) {
			drawImage(g, bgObject);
		}

		// Draws lower level animations
		for (SpriteAnimation animation : animationManager.getLowerAnimations()) {
			drawAnimation(g, animation);
		}

		for (Missile missile : missileManager.getMissiles()){
			if(missile.isVisible()){
				if (missile.getAnimation() != null) {
					drawAnimation(g, missile.getAnimation());
				} else {
					drawImage(g, missile);
				}
			}
		}

//		// Draw enemy missiles with an animation
//		for (Missile missile : missileManager.getEnemyMissiles()) {
//			if (missile.isVisible()) {
//				if (missile.getAnimation() != null) {
//					drawAnimation(g, missile.getAnimation());
//				} else {
//					drawImage(g, missile);
//				}
//			}
//		}
//
//		// Draw friendly missiles
//		for (Missile missile : missileManager.getFriendlyMissiles()) {
//			if (missile.isVisible()) {
//				if (missile.getAnimation() != null) {
//					drawAnimation(g, missile.getAnimation());
//				} else {
//					drawImage(g, missile);
//				}
//			}
//		}

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

		for (Explosion explosion : explosionManager.getExplosions()) {
			if (explosion.isVisible()) {
				drawAnimation(g, explosion.getAnimation());
			}
		}

		for (GuardianDrone friendly : friendlyMover.getGuardianDroneList()) {
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

		drawPlayerHealthBars(g);
		drawSpecialAttackFrame(g);

		// Draws higher level animations
		for (SpriteAnimation animation : animationManager.getUpperAnimations()) {
			drawAnimation(g, animation);
		}

		for (OnScreenText text : textManager.getPowerUpTexts()) {
			drawText(g, text);
		}

		// Draw the score/aliens left
		g.setColor(Color.WHITE);
		g.drawString("Enemies left: " + enemyManager.getEnemyCount(), 5, 15);

		g.drawString("Music time: " + gameState.getMusicSeconds(), 100, 15);
		if (audioManager.getBackgroundMusic() != null) {
			g.drawString("Music frame: " + audioManager.getBackgroundMusic().getFramePosition(), 300, 15);
		}
	}

	private void drawText(Graphics2D g, OnScreenText text) {
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

	private void drawImage(Graphics2D g, Sprite sprite) {
		if (sprite.getImage() != null) {
			// Save the original composite
			Composite originalComposite = g.getComposite();

			System.out.println("Kom ik hier " + sprite.getSpriteConfiguration().getImageType());
			// Set the alpha composite
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, sprite.getTransparancyAlpha()));

			// Draw the image
			g.drawImage(sprite.getImage(), sprite.getXCoordinate(), sprite.getYCoordinate(), this);

			// Reset to the original composite
			g.setComposite(originalComposite);
		}
	}

	private void drawAnimation(Graphics2D g, SpriteAnimation animation) {
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
	private void drawHealthBars(Graphics2D g, Enemy enemy) {
		float factor = enemy.getCurrentHitpoints() / enemy.getMaxHitPoints();
		int actualAmount = (int) Math.round(enemy.getHeight() * factor);

		g.setColor(Color.RED);
		g.fillRect((enemy.getXCoordinate() + enemy.getWidth() + 10), enemy.getYCoordinate(), 2, enemy.getHeight());
		g.setColor(Color.GREEN);
		g.fillRect((enemy.getXCoordinate() + enemy.getWidth() + 10), enemy.getYCoordinate(), 2, actualAmount);
	}

	private void drawPlayerHealthBars(Graphics2D g) {
		float playerHealth = playerStats.getHitpoints();
		float playerMaxHealth = playerStats.getMaxHitPoints();

		UIObject healthBar = uiManager.getHealthBar();
		int healthBarWidth = calculateHealthbarWidth(playerHealth, playerMaxHealth, healthBar.getWidth());
		healthBar.resizeToDimensions(healthBarWidth, healthBar.getHeight());
		drawImage(g, healthBar);

		UIObject healthFrame = uiManager.getHealthFrame();
		drawImage(g, healthFrame);

		float playerShields = playerStats.getShieldHitpoints();
		float playerMaxShields = playerStats.getMaxShieldHitPoints();

		UIObject shieldBar = uiManager.getShieldBar();

		int shieldBarWidth = calculateHealthbarWidth(playerShields, playerMaxShields, shieldBar.getWidth());
		shieldBar.resizeToDimensions(shieldBarWidth, shieldBar.getHeight());
		drawImage(g, shieldBar);

		UIObject shieldFrame = uiManager.getShieldFrame();
		drawImage(g, shieldFrame);
	}

	private void drawSpecialAttackFrame(Graphics2D g) {
		drawImage(g, uiManager.getSpecialAttackFrame());

		for (SpaceShipSpecialGun gun : playerManager.getSpaceship().getSpecialGuns()) {
			if (gun.getCurrentSpecialAttackFrame() >= playerStats.getSpecialAttackSpeed()) {
				drawAnimation(g, uiManager.getSpecialAttackHighlight());
			} else {
				float percentage = (float) gun.getCurrentSpecialAttackFrame() / playerStats.getSpecialAttackSpeed();

				int barWidth = (int) (uiManager.getSpecialAttackFrame().getWidth() * percentage);

				// Draw the cooldown progress bar
				g.setColor(new Color(160, 160, 160, 160)); // Semi-transparent gray
				g.fillRect(uiManager.getSpecialAttackFrame().getXCoordinate(),
						uiManager.getSpecialAttackFrame().getYCoordinate(), barWidth,
						uiManager.getSpecialAttackFrame().getHeight());
			}
		}

	}

	public int calculateHealthbarWidth(float currentHitpoints, float maximumHitpoints, int healthBarSize) {
		// Calculate the percentage of currentHitpoints out of maximumHitpoints
		double percentage = (double) currentHitpoints / maximumHitpoints * 100;
		// Calculate what this percentage is of thirdNumber
		int width = (int) Math.ceil(percentage / 100 * healthBarSize);

		if (width > uiManager.getHealthBarWidth()) {
			width = uiManager.getHealthBarWidth();
		} else if (width < 1) {
			width = 1;
		}
		return width;
	}

	// Called on every action/input. Essentially the infinite loop that plays
	public void actionPerformed(ActionEvent e) {
		if (gameState.getGameState() == GameStatusEnums.Zoning_In) {
			if (this.zoningInAlpha <= 0.05) {
				levelManager.startLevel();
				zoningInAlpha = 1.0f;
				
				System.out.println("Skipping time in the level on on GameBoard 517");
//				int seconds = 300;
//				long framePosition = AudioPositionCalculator.getInstance()
//						.getFrameLengthForTime(audioManager.getBackgroundMusic().getClip(), seconds);
//				audioManager.getBackgroundMusic().setFramePosition((int) framePosition);

			}
		}

		if (gameState.getGameState() != GameStatusEnums.Dead) {
			if (AudioManager.getInstance().getBackgroundMusic() != null) {
				gameState.setMusicSeconds(
						audioPosCalc.getPlaybackTimeInSeconds(audioManager.getBackgroundMusic().getClip(),
								audioManager.getBackgroundMusic().getFramePosition()));
//				musicSeconds = AudioManager.getInstance().getBackgroundMusic().getFramePosition();
			} else {
				gameState.setMusicSeconds((float) (gameState.getMusicSeconds() + 0.05));
			}
			playerManager.updateGameTick();
			missileManager.updateGameTick();
			enemyManager.updateGameTick();
			levelManager.updateGameTick();
			animationManager.updateGameTick();
			backgroundManager.updateGameTick();
			timerManager.updateGameTick(gameState.getMusicSeconds());
			audioDatabase.updateGameTick();
			explosionManager.updateGametick();
			friendlyMover.updateGameTick();
			powerUpManager.updateGameTick();
			executeControllerInput();
		}

		if (gameState.getGameState() == GameStatusEnums.Album_Completed || 
				gameState.getGameState() == GameStatusEnums.Dead) {
			currentWaitingTime++;
		}
		
		if(lastKnownState == null || lastKnownState != gameState.getGameState()) {
			lastKnownState = gameState.getGameState();
			System.out.println(lastKnownState);
		}
		
		
		repaint();
	}

	// Required to read key presses
	private class TAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			playerManager.getSpaceship().keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (gameState.getGameState() == GameStatusEnums.Dead
					|| gameState.getGameState() == GameStatusEnums.Album_Completed) {
				if (currentWaitingTime >= MOVE_COOLDOWN) {
					boardManager.gameToMainMenu();
					drawTimer.stop();
				}
			} else {
				playerManager.getSpaceship().keyPressed(e);
			}
		}
	}

	public void executeControllerInput() {
		if (controllers.getFirstController() != null) {
			ControllerInputReader inputReader = controllers.getControllerInputReader(controllers.getFirstController());
			if (gameState.getGameState() == GameStatusEnums.Dead
					|| gameState.getGameState() == GameStatusEnums.Album_Completed) {
				inputReader.pollController();
				if (inputReader.isInputActive(ControllerInput.FIRE)) {
					boardManager.gameToMainMenu();
					drawTimer.stop();
				}

			} else {
				playerManager.getSpaceship().update(inputReader);
			}
		}
	}
}
