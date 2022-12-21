package menuscreens.boards;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

import data.AudioDatabase;
import data.DataClass;
import game.managers.AnimationManager;
import game.managers.AudioManager;
import game.managers.BackgroundManager;
import game.managers.EnemyManager;
import game.managers.ExplosionManager;
import game.managers.FriendlyManager;
import game.managers.LevelManager;
import game.managers.MissileManager;
import game.managers.TimerManager;
import game.objects.Explosion;
import game.objects.enemies.Enemy;
import game.objects.missiles.Missile;
import image.objects.Animation;
import image.objects.BackgroundObject;
import image.objects.Sprite;
import menuscreens.BoardManager;

public class GameBoard extends JPanel implements ActionListener {

	private Timer timer;
	private boolean ingame;
//	private String currentMusic = "defaultmusic";
	private String currentMusic = "";

	private DataClass data = DataClass.getInstance();
	private AudioDatabase audioDatabase = AudioDatabase.getInstance();
	private final int boardWidth = data.getWindowWidth();
	private final int boardHeight = data.getWindowHeight();

	private final int DELAY = 15;
	private BoardManager boardManager = BoardManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private MissileManager missileManager = MissileManager.getInstance();
	private LevelManager levelManager = LevelManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private AudioManager audioManager = AudioManager.getInstance();
	private BackgroundManager backgroundManager = BackgroundManager.getInstance();
	private TimerManager timerManager = TimerManager.getInstance();
	private ExplosionManager explosionManager = ExplosionManager.getInstance();

	public GameBoard() {
		animationManager = AnimationManager.getInstance();
		enemyManager = EnemyManager.getInstance();
		missileManager = MissileManager.getInstance();
		levelManager = LevelManager.getInstance();
		friendlyManager = FriendlyManager.getInstance();
		audioManager = AudioManager.getInstance();
		backgroundManager = BackgroundManager.getInstance();
		timerManager = TimerManager.getInstance();
		explosionManager = ExplosionManager.getInstance();
		initBoard();
	}

	private void initBoard() {
		addKeyListener(new TAdapter());
		timer = new Timer(DELAY, this);
	}

	public void startGame() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		ingame = true;
		levelManager.startLevel();

		timer.start();
		try {
			audioManager.playMusicAudio(currentMusic);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}

	// Resets the game
	public void resetGame() {
		animationManager.resetManager();
		enemyManager.resetManager();
		missileManager.resetManager();
		levelManager.resetManager();
		friendlyManager.resetManager();
		audioManager.resetManager();
		backgroundManager.resetManager();
		timerManager.resetManager();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (ingame) {
			drawObjects(g);
		} else {
			drawGameOver(g);
		}
		Toolkit.getDefaultToolkit().sync();
	}

	private void drawObjects(Graphics g) {

		// Draws all background objects
		for (BackgroundObject bgObject : backgroundManager.getAllBGO()) {
			drawImage(g, bgObject);
		}

		// Draws lower level animations
		for (Animation animation : animationManager.getLowerAnimations()) {
			drawAnimation(g, animation);
		}

		// Draw friendly spaceship
		if (friendlyManager.getSpaceship().isVisible()) {
			drawImage(g, friendlyManager.getSpaceship());
		}

		// Draw friendly missiles
		for (Missile missile : missileManager.getFriendlyMissiles()) {
			if (missile.isVisible()) {
				drawImage(g, missile);
			}
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

		drawPlayerHealthBars(g);

		// Draws higher level animations
		for (Animation animation : animationManager.getUpperAnimations()) {
			drawAnimation(g, animation);
		}

		// Draw the score/aliens left
		g.setColor(Color.WHITE);
		g.drawString("Aliens left: " + enemyManager.getDefaultAlienSpaceshipCount(), 5, 15);
		g.drawString("Bombs present: " + enemyManager.getAlienBombCount(), 5, 25);
	}

	private void drawImage(Graphics g, Sprite sprite) {
		if (sprite.getImage() != null) {
			g.drawImage(sprite.getImage(), sprite.getXCoordinate(), sprite.getYCoordinate(), this);
		}
	}

	private void drawAnimation(Graphics g, Animation animation) {
		if (animation.getCurrentFrame() != null) {
			g.drawImage(animation.getCurrentFrame(), animation.getXCoordinate(), animation.getYCoordinate(), this);
		}
	}

	// Primitive healthbar generator for enemies
	private void drawHealthBars(Graphics g, Enemy enemy) {
		float factor = enemy.getCurrentHitpoints() / enemy.getMaxHitpoints();
		int actualAmount = (int) Math.round(enemy.getHeight() * factor);

		g.setColor(Color.RED);
		g.fillRect((enemy.getXCoordinate() + enemy.getWidth() + 10), enemy.getYCoordinate(), 5, enemy.getHeight());
		g.setColor(Color.GREEN);
		g.fillRect((enemy.getXCoordinate() + enemy.getWidth() + 10), enemy.getYCoordinate(), 5, actualAmount);
	}

	private void drawPlayerHealthBars(Graphics g) {
		float playerHealth = friendlyManager.getSpaceship().getHitpoints();
		float playerMaxHealth = friendlyManager.getSpaceship().getMaxHitpoints();

		float playerHealthFactor = playerHealth / playerMaxHealth;
		int actualHealthAmount = (int) Math.round(200 * playerHealthFactor);

		g.setColor(Color.RED);
		g.fillRect(10, 30, 200, 15);
		g.setColor(Color.GREEN);
		g.fillRect(10, 30, actualHealthAmount, 15);

		float playerShields = friendlyManager.getSpaceship().getShieldHitpoints();
		float playerMaxShields = friendlyManager.getSpaceship().getMaxShieldHitpoints();

		float playerShieldFactor = playerShields / playerMaxShields;
		int actualShieldAmount = (int) Math.round(200 * playerShieldFactor);

		g.setColor(Color.BLUE);
		g.fillRect(10, 50, 200, 15);
		g.setColor(Color.CYAN);
		g.fillRect(10, 50, actualShieldAmount, 15);

	}

	// Draw the game over screen
	private void drawGameOver(Graphics g) {
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
			friendlyManager.updateGameTick();
			missileManager.updateGameTick();
			enemyManager.updateGameTick();
			levelManager.updateGameTick();
			animationManager.updateGameTick();
			backgroundManager.updateGameTick();
			timerManager.updateGameTick();
			audioDatabase.updateGameTick();
			explosionManager.updateGametick();
		}

		repaint();
	}

	// Checks wether the game is still running, stops the timer is not.
	private void inGame() {
		ingame = friendlyManager.getPlayerStatus();
		if (!ingame) {
			timer.stop();
		}
	}

	// Required to read key presses
	private class TAdapter extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			friendlyManager.getSpaceship().keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			friendlyManager.getSpaceship().keyPressed(e);
		}
	}

}