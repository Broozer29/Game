package menuscreens.boards;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

import data.DataClass;
import game.managers.AnimationManager;
import game.managers.AudioManager;
import game.managers.BackgroundManager;
import game.managers.EnemyManager;
import game.managers.FriendlyManager;
import game.managers.LevelManager;
import game.managers.MissileManager;
import game.objects.Enemy;
import game.objects.Missile;
import image.objects.Animation;
import image.objects.BackgroundObject;
import image.objects.Sprite;

public class GameBoard extends JPanel implements ActionListener {

	private Timer timer;
	private boolean ingame;

	private DataClass data = DataClass.getInstance();
	private final int boardWidth = data.getWindowWidth();
	private final int boardHeight = data.getWindowHeight();

	private final int DELAY = 15;
	private AnimationManager animationManager = AnimationManager.getInstance();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private MissileManager missileManager = MissileManager.getInstance();
	private LevelManager levelManager = LevelManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private AudioManager audioManager = AudioManager.getInstance();
	private BackgroundManager backgroundManager = BackgroundManager.getInstance();

	public GameBoard() {
		animationManager = AnimationManager.getInstance();
		enemyManager = EnemyManager.getInstance();
		missileManager = MissileManager.getInstance();
		levelManager = LevelManager.getInstance();
		friendlyManager = FriendlyManager.getInstance();
		audioManager = AudioManager.getInstance();
		backgroundManager = BackgroundManager.getInstance();
		initBoard();
	}

	private void initBoard() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		ingame = true;

		setPreferredSize(new Dimension(boardWidth, boardHeight));
		levelManager.startLevel();

		timer = new Timer(DELAY, this);
		timer.start();
		try {
			audioManager.playBackgroundMusic("defaultmusic");
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
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
		
		// Draws low level stars
		for(BackgroundObject bgObject : backgroundManager.getLevelOneStars()) {
			drawImage(g, bgObject);
		}

		// Draws low level planets
		for (BackgroundObject bgObject : backgroundManager.getLevelOnePlanets()) {
			drawImage(g, bgObject);
		}
		
		// Draws medium level stars
		for(BackgroundObject bgObject : backgroundManager.getLevelTwoStars()) {
			drawImage(g, bgObject);
		}

		// Draws medium level planets
		for (BackgroundObject bgObject : backgroundManager.getLevelTwoPlanets()) {
			drawImage(g, bgObject);
		}
		
		// Draws high level stars
		for(BackgroundObject bgObject : backgroundManager.getLevelThreeStars()) {
			drawImage(g, bgObject);
		}

		// Draws high level planets
		for (BackgroundObject bgObject : backgroundManager.getLevelThreePlanets()) {
			drawImage(g, bgObject);
		}

		// Draws lower level animations
		for (Animation animation : animationManager.getLowerAnimations()) {
			drawImage(g, animation);
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
			if (missile.isVisible()) {
				drawImage(g, missile);
			}
		}

		// Draw enemies
		for (Enemy enemy : enemyManager.getEnemies()) {
			if (enemy.isVisible()) {
				drawImage(g, enemy);
				drawHealthBars(g, enemy);
			}
		}

		// Draws higher level animations
		for (Animation animation : animationManager.getUpperAnimations()) {
			drawImage(g, animation);
		}

		// Draw the score/aliens left
		g.setColor(Color.WHITE);
		g.drawString("Aliens left: " + enemyManager.getEnemies().size(), 5, 15);
	}

	private void drawImage(Graphics g, Sprite sprite) {
		g.drawImage(sprite.getImage(), sprite.getXCoordinate(), sprite.getYCoordinate(), this);

	}
	
	//Primitive healthbar generator for enemies
	private void drawHealthBars(Graphics g, Enemy enemy) {
		float factor = enemy.getCurrentHitpoints() / enemy.getMaxHitpoints();
		int actualAmount = (int) Math.round(enemy.getHeight() * factor);
		
		g.setColor(Color.RED);
		g.fillRect((enemy.getXCoordinate() + enemy.getWidth() + 10), enemy.getYCoordinate(), 5, enemy.getHeight());
		g.setColor(Color.GREEN);
		g.fillRect((enemy.getXCoordinate() + enemy.getWidth() + 10), enemy.getYCoordinate(), 5, actualAmount);
	}

	// Draw the game over screen
	private void drawGameOver(Graphics g) {
		String msg = "Game Over";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics fm = getFontMetrics(small);
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg, (boardWidth - fm.stringWidth(msg)) / 2, boardHeight / 2);
	}

	// Called on every action/input. Essentially the infinite loop that plays
	public void actionPerformed(ActionEvent e) {
		inGame();
		friendlyManager.updateGameTick();
		missileManager.updateGameTick();
		enemyManager.updateGameTick();
		levelManager.updateGameTick();
		animationManager.updateGameTick();
		backgroundManager.updateGameTick();
//		backgroundManager.testWhatIGot();

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