package menuscreens.boards;

import java.awt.Color;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

import data.BoostsUpgradesAndBuffsSettings;
import data.ConnectedControllers;
import data.DataClass;
import data.GameStateInfo;
import data.GameStatusEnums;
import data.PlayerStats;
import data.audio.AudioDatabase;
import data.audio.AudioPositionCalculator;
import game.UI.UIObject;
import game.levels.LevelSpawnerManager;
import game.managers.AnimationManager;
import game.managers.AudioManager;
import game.managers.CustomUIManager;
import game.managers.ExplosionManager;
import game.managers.OnScreenTextManager;
import game.managers.PlayerManager;
import game.managers.TimerManager;
import game.objects.BackgroundManager;
import game.objects.BackgroundObject;
import game.objects.Explosion;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.objects.friendlies.FriendlyManager;
import game.objects.friendlies.FriendlyObject;
import game.objects.friendlies.powerups.PowerUp;
import game.objects.friendlies.powerups.PowerUpAcquiredText;
import game.objects.friendlies.powerups.PowerUpManager;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.friendlies.spaceship.PlayerSpecialAttackTypes;
import game.objects.friendlies.spaceship.SpaceShipSpecialGun;
import game.objects.friendlies.spaceship.specialAttacks.SpecialAttack;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileManager;
import game.playerpresets.GunPreset;
import game.playerpresets.SpecialGunPreset;
import menuscreens.BoardManager;
import net.java.games.input.Component;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class GameBoard extends JPanel implements ActionListener {

	private Timer timer;

	private DataClass data = DataClass.getInstance();
	private AudioDatabase audioDatabase = AudioDatabase.getInstance();
	private GameStateInfo gameState = GameStateInfo.getInstance();
	private final int boardWidth = data.getWindowWidth();
	private final int boardHeight = data.getWindowHeight();

	private BoardManager boardManager = BoardManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private MissileManager missileManager = MissileManager.getInstance();
	private LevelSpawnerManager levelManager = LevelSpawnerManager.getInstance();
	private PlayerManager playerManager = PlayerManager.getInstance();
	private AudioManager audioManager = AudioManager.getInstance();
	private BackgroundManager backgroundManager = BackgroundManager.getInstance();
	private TimerManager timerManager = TimerManager.getInstance();
	private ExplosionManager explosionManager = ExplosionManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private PlayerStats playerStats = PlayerStats.getInstance();
	private CustomUIManager uiManager = CustomUIManager.getInstance();
	private PowerUpManager powerUpManager = PowerUpManager.getInstance();
	private OnScreenTextManager textManager = OnScreenTextManager.getInstance();
	private BoostsUpgradesAndBuffsSettings tempSettings = BoostsUpgradesAndBuffsSettings.getInstance();
	private AudioPositionCalculator audioPosCalc = AudioPositionCalculator.getInstance();
	private ConnectedControllers controllers = ConnectedControllers.getInstance();

	public GameBoard() {
		animationManager = AnimationManager.getInstance();
		enemyManager = EnemyManager.getInstance();
		missileManager = MissileManager.getInstance();
		levelManager = LevelSpawnerManager.getInstance();
		playerManager = PlayerManager.getInstance();
		audioManager = AudioManager.getInstance();
		backgroundManager = BackgroundManager.getInstance();
		timerManager = TimerManager.getInstance();
		explosionManager = ExplosionManager.getInstance();
		friendlyManager = FriendlyManager.getInstance();
		playerStats = PlayerStats.getInstance();
		uiManager = CustomUIManager.getInstance();
		powerUpManager = PowerUpManager.getInstance();
		textManager = OnScreenTextManager.getInstance();
		audioPosCalc = AudioPositionCalculator.getInstance();
		tempSettings = BoostsUpgradesAndBuffsSettings.getInstance();
		initBoard();
	}

	private void initBoard() {
		timer = new Timer(gameState.getDELAY(), this);
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

		gameState.setGameState(GameStatusEnums.Playing);
		uiManager.createGameBoardGUI();
		levelManager.startLevel();
		timer.start();

		// The cursed ling
		System.out.println("Skipping time in the level on on GameBoard 142");

//		long framePosition = AudioPositionCalculator.getInstance().getFrameLengthForTime(audioManager.getBackgroundMusic().getClip(), 220);
//		audioManager.getBackgroundMusic().setFramePosition((int) framePosition);
	}

	// Resets the game
	// This needs to be reworked in seperate (startManagers) and (endManagers)
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
		friendlyManager.resetManager();
		uiManager.resetManager();
		powerUpManager.resetManager();
		textManager.resetManager();
		playerStats.resetPlayerStats();
		tempSettings.resetGameSettings();
		this.timer.setDelay(gameState.getDELAY());

		// Dit moet uit een "out-of-game state manager" gehaald worden
		playerStats.getNormalGunPreset().loadPreset();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		if (gameState.getGameState() == GameStatusEnums.Playing) {
			drawObjects(g2d);
		} else if (gameState.getGameState() == GameStatusEnums.Dying) {
			playerManager.startDyingScene();
			this.timer.setDelay(75);
			drawObjects(g2d);
		} else if (gameState.getGameState() == GameStatusEnums.Dead) {
			drawGameOver(g2d);
		}
		Toolkit.getDefaultToolkit().sync();
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

		// Draw friendly spaceship
		if (playerManager.getSpaceship().isVisible()) {
			drawImage(g, playerManager.getSpaceship());
		}

		// Draw enemy missiles with an animation
		for (Missile missile : missileManager.getEnemyMissiles()) {
			if (missile.isVisible()) {
				if (missile.getAnimation() != null) {
					drawAnimation(g, missile.getAnimation());
				} else {
					drawImage(g, missile);
				}
			}
		}

		// Draw friendly missiles
		for (Missile missile : missileManager.getFriendlyMissiles()) {
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
				drawImage(g, enemy);
				if (enemy.showhealthBar())
					drawHealthBars(g, enemy);
			}
		}

		for (Explosion explosion : explosionManager.getExplosions()) {
			if (explosion.isVisible()) {
				drawAnimation(g, explosion.getAnimation());
			}
		}

		for (FriendlyObject friendly : friendlyManager.getActiveFriendlyObjects()) {
			if (friendly.isVisible()) {
				drawAnimation(g, friendly.getAnimation());
			}
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

		for (PowerUpAcquiredText text : textManager.getPowerUpTexts()) {
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

	private void drawText(Graphics2D g, PowerUpAcquiredText text) {
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
			g.drawImage(sprite.getImage(), sprite.getXCoordinate(), sprite.getYCoordinate(), this);
		}
	}

	private void drawAnimation(Graphics2D g, SpriteAnimation animation) {
		if (animation.getCurrentFrame() != null) {
			g.drawImage(animation.getCurrentFrame(), animation.getXCoordinate(), animation.getYCoordinate(), this);
		}
	}

	// Primitive healthbar generator for enemies
	private void drawHealthBars(Graphics2D g, Enemy enemy) {
		float factor = enemy.getCurrentHitpoints() / enemy.getMaxHitpoints();
		int actualAmount = (int) Math.round(enemy.getHeight() * factor);

		g.setColor(Color.RED);
		g.fillRect((enemy.getXCoordinate() + enemy.getWidth() + 10), enemy.getYCoordinate(), 5, enemy.getHeight());
		g.setColor(Color.GREEN);
		g.fillRect((enemy.getXCoordinate() + enemy.getWidth() + 10), enemy.getYCoordinate(), 5, actualAmount);
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

	// Draw the game over screen
	private void drawGameOver(Graphics2D g) {
		String msg = "Lmao xd";
		Font font = new Font("Helvetica", Font.BOLD, 140);
		FontMetrics fm = getFontMetrics(font);
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString(msg, (boardWidth - fm.stringWidth(msg)) / 2, boardHeight / 2);

		if (boardManager == null) {
			boardManager = BoardManager.getInstance();
		}
		timer.stop();

	}

	// Called on every action/input. Essentially the infinite loop that plays
	public void actionPerformed(ActionEvent e) {
		if (gameState.getGameState() == GameStatusEnums.Playing || gameState.getGameState() == GameStatusEnums.Dying) {

			if (AudioManager.getInstance().getBackgroundMusic() != null) {
				gameState.setMusicSeconds(
						audioPosCalc.getPlaybackTimeInSeconds(audioManager.getBackgroundMusic().getClip(),
								audioManager.getBackgroundMusic().getFramePosition()));
//				musicSeconds = AudioManager.getInstance().getBackgroundMusic().getFramePosition();
			} else {
				gameState.setMusicSeconds((float) (gameState.getMusicSeconds() + 0.05
						));
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
			friendlyManager.updateGameTick();
			powerUpManager.updateGameTick();
			readControllerState();
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
			playerManager.getSpaceship().keyPressed(e);

			if (gameState.getGameState() == GameStatusEnums.Dead) {
				boardManager.gameToMainMenu();
			}
		}
	}

	private final Set<Integer> simulatedPressedKeys = new HashSet<>();

	private final Map<String, Float> pressedButtons = new HashMap<>();

	public void readControllerState() {
		if (controllers.getFirstController() != null) {
			controllers.getFirstController().poll();
			Component[] components = controllers.getFirstController().getComponents();

			for (int i = 0; i < components.length; i++) {
				Component component = components[i];
				String componentName = component.getName();
				float componentValue = component.getPollData();

				// Handle button presses
				if (componentName.equals("0") || componentName.equals("1") || componentName.equals("3")
						|| componentName.equals("x") || componentName.equals("z") || componentName.equals("y")
						|| componentName.equals("rz")) {
					if (Math.abs(componentValue) > 0.5) {
						pressedButtons.put(componentName, componentValue);
					} else {
						pressedButtons.remove(componentName);
					}
				}
			}

			// Handle left joystick horizontal movement
			if (pressedButtons.containsKey("x")) {
				if (pressedButtons.get("x") > 0 && !simulatedPressedKeys.contains(KeyEvent.VK_D)) {
					simulateKeyPress(KeyEvent.VK_D);
				} else if (pressedButtons.get("x") < 0 && !simulatedPressedKeys.contains(KeyEvent.VK_A)) {
					simulateKeyPress(KeyEvent.VK_A);
				}
			} else if (!pressedButtons.containsKey("x")) {
				if (simulatedPressedKeys.contains(KeyEvent.VK_D)) {
					simulateKeyRelease(KeyEvent.VK_D);
				}
				if (simulatedPressedKeys.contains(KeyEvent.VK_A)) {
					simulateKeyRelease(KeyEvent.VK_A);
				}
			}

			// Handle left joystick vertical movement
			if (pressedButtons.containsKey("y")) {
				if (pressedButtons.get("y") > 0 && !simulatedPressedKeys.contains(KeyEvent.VK_S)) {
					simulateKeyPress(KeyEvent.VK_S);
				} else if (pressedButtons.get("y") < 0 && !simulatedPressedKeys.contains(KeyEvent.VK_W)) {
					simulateKeyPress(KeyEvent.VK_W);
				}
			} else if (!pressedButtons.containsKey("y")) {
				if (simulatedPressedKeys.contains(KeyEvent.VK_S)) {
					simulateKeyRelease(KeyEvent.VK_S);
				}
				if (simulatedPressedKeys.contains(KeyEvent.VK_W)) {
					simulateKeyRelease(KeyEvent.VK_W);
				}
			}

			// Fire
			if (pressedButtons.containsKey("0")) {
				simulateKeyPress(KeyEvent.VK_SPACE);
			}
		}
		if (!pressedButtons.containsKey("0") && simulatedPressedKeys.contains(KeyEvent.VK_SPACE)) {
			simulateKeyRelease(KeyEvent.VK_SPACE);
		}

		// Special attack
		if (pressedButtons.containsKey("1")) {
			simulateKeyPress(KeyEvent.VK_ENTER);
		}
		if (!pressedButtons.containsKey("1") && simulatedPressedKeys.contains(KeyEvent.VK_ENTER)) {
			simulateKeyRelease(KeyEvent.VK_ENTER);
		}
	}

	private void simulateKeyPress(int keyCode) {
		KeyEvent keyEvent = new KeyEvent(this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode,
				KeyEvent.CHAR_UNDEFINED);
		playerManager.getSpaceship().keyPressed(keyEvent);
		simulatedPressedKeys.add(keyCode);
	}

	private void simulateKeyRelease(int keyCode) {
		KeyEvent keyEvent = new KeyEvent(this, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keyCode,
				KeyEvent.CHAR_UNDEFINED);
		playerManager.getSpaceship().keyReleased(keyEvent);
		simulatedPressedKeys.remove(keyCode);
	}

}
