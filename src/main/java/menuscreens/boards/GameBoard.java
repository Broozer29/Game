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
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import Data.DataClass;
import gameManagers.AnimationManager;
import gameManagers.EnemyManager;
import gameManagers.FriendlyManager;
import gameManagers.LevelManager;
import gameManagers.MissileManager;
import gameObjectes.Animation;
import gameObjectes.Enemy;
import gameObjectes.Missile;
import java.awt.Image;

public class GameBoard extends JPanel implements ActionListener {

	private Timer timer;
	private boolean ingame;

	private DataClass data = DataClass.getInstance();
	private final int boardWidth = data.getWindowWidth();
	private final int boardHeight = data.getWindowHeight();

	private final int DELAY = 15;
	private AnimationManager animationLoader = AnimationManager.getInstance();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private MissileManager missileManager = MissileManager.getInstance();
	private LevelManager levelManager = LevelManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();

	public GameBoard() {
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
		// Draw friendly spaceship
		if (friendlyManager.getSpaceship().isVisible()) {
			drawImageSelfWritten(g, friendlyManager.getSpaceship().getImage(),
					friendlyManager.getSpaceship().getXCoordinate(), friendlyManager.getSpaceship().getYCoordinate());
		}

		// Draw friendly missiles
		List<Missile> friendlyMissiles = missileManager.getFriendlyMissiles();
		for (Missile missile : friendlyMissiles) {
			if (missile.isVisible()) {
				drawImageSelfWritten(g, missile.getImage(), missile.getXCoordinate(), missile.getYCoordinate());
			}
		}

		// Draw enemy missiles
		List<Missile> enemyMissiles = missileManager.getEnemyMissiles();
		for (Missile missile : enemyMissiles) {
			if (missile.isVisible()) {
				drawImageSelfWritten(g, missile.getImage(), missile.getXCoordinate(), missile.getYCoordinate());
			}
		}

		// Draw enemies
		for (Enemy enemy : enemyManager.getEnemies()) {
			if (enemy.isVisible()) {
				drawImageSelfWritten(g, enemy.getImage(), enemy.getXCoordinate(), enemy.getYCoordinate());
			}
		}

		// Draw animations
		List<Animation> animationList = animationLoader.getAnimations();
		for (int i = 0; i < animationList.size(); i++) {
			drawImageSelfWritten(g, animationList.get(i).getImage(), animationList.get(i).getXCoordinate(),
					animationList.get(i).getYCoordinate());
			animationLoader.updateAnimationList(animationList.get(i));
		}

		// Draw the score/aliens left
		g.setColor(Color.WHITE);
		g.drawString("Aliens left: " + enemyManager.getEnemies().size(), 5, 15);
	}

	private void drawImageSelfWritten(Graphics g, Image image, int xCoordinate, int yCoordinate) {
		g.drawImage(image, xCoordinate, yCoordinate, this);
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