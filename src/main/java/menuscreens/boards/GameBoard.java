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

import javax.swing.JPanel;
import javax.swing.Timer;

import data.DataClass;
import data.PlayerStats;
import data.TemporaryGameSettings;
import data.audio.AudioDatabase;
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
import game.objects.friendlies.spaceship.SpaceShipSpecialGun;
import game.objects.friendlies.spaceship.specialAttacks.SpecialAttack;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileManager;
import game.playerpresets.LaserbeamPreset;
import game.playerpresets.PlayerPreset;
import game.playerpresets.RocketPreset;
import menuscreens.BoardManager;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class GameBoard extends JPanel implements ActionListener {

	private Timer timer;
	private boolean ingame;

	private DataClass data = DataClass.getInstance();
	private AudioDatabase audioDatabase = AudioDatabase.getInstance();
	private final int boardWidth = data.getWindowWidth();
	private final int boardHeight = data.getWindowHeight();

	private final int DELAY = 15;

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
	private TemporaryGameSettings tempSettings = TemporaryGameSettings.getInstance();

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
		initBoard();
	}

	private void initBoard() {
		timer = new Timer(DELAY, this);
	}

	public void startGame() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(boardWidth, boardHeight));

		// Dit moet uit een "out-of-game state manager" gehaald worden
		if (playerStats.getPreset() == null) {
			PlayerPreset preset = new LaserbeamPreset();
			playerStats.setPreset(preset);
		}
		playerStats.getPreset().loadPreset();

		ingame = true;
		uiManager.createGameBoardGUI();
		levelManager.startLevel();
		timer.start();
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

		// Dit moet uit een "out-of-game state manager" gehaald worden
		playerStats.getPreset().loadPreset();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		if (ingame) {
			drawObjects(g2d);
		} else {
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

		// Draw enemy missiles
		for (Missile missile : missileManager.getEnemyMissiles()) {
			if (missile.isVisible() && missile.getAnimation() == null) {
				drawImage(g, missile);
			}
		}

		// Draw enemy missiles with an animation
		for (Missile missile : missileManager.getEnemyMissiles()) {
			if (missile.isVisible() && missile.getAnimation() != null) {
				drawAnimation(g, missile.getAnimation());
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
		                    uiManager.getSpecialAttackFrame().getYCoordinate(),
		                    barWidth,
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
		String msg = "Game Over";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics fm = getFontMetrics(small);
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg, (boardWidth - fm.stringWidth(msg)) / 2, boardHeight / 2);

		if (boardManager == null) {
			boardManager = BoardManager.getInstance();
		}
		timer.stop();
		boardManager.gameToMainMenu();
	}

	// Called on every action/input. Essentially the infinite loop that plays
	public void actionPerformed(ActionEvent e) {
		inGame();
		if (ingame) {
			playerManager.updateGameTick();
			missileManager.updateGameTick();
			enemyManager.updateGameTick();
			levelManager.updateGameTick();
			animationManager.updateGameTick();
			backgroundManager.updateGameTick();
			timerManager.updateGameTick();
			audioDatabase.updateGameTick();
			explosionManager.updateGametick();
			friendlyManager.updateGameTick();
			powerUpManager.updateGameTick();
		}

		repaint();
	}

	// Checks wether the game is still running, stops the timer is not.
	private void inGame() {
		ingame = playerManager.getPlayerStatus();
		if (!ingame) {
			timer.stop();
		}
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
		}
	}

}