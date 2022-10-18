package menuscreens.boards;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import Data.DataClass;
import gameManagers.EnemyManager;
import gameManagers.GlobalAnimationStorage;
import gameObjectes.Alien;
import gameObjectes.Animation;
import gameObjectes.Enemy;
import gameObjectes.Missile;
import gameObjectes.SpaceShip;

public class GameBoard extends JPanel implements ActionListener {

	private Timer timer;
	private SpaceShip spaceship;
	private boolean ingame;
	private final int startingXPosition = 40;
	private final int startingYPosition = 60;
	private DataClass data = DataClass.getInstance();
	private final int boardWidth = data.getWindowWidth();;
	private final int boardHeight = data.getWindowHeight();;
	private final int DELAY = 15;
	private GlobalAnimationStorage animationLoader = GlobalAnimationStorage.getInstance();
	private EnemyManager enemyManager = EnemyManager.getInstance();

	public GameBoard() {
		initBoard();
	}

	private void initBoard() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		ingame = true;

		setPreferredSize(new Dimension(boardWidth, boardHeight));

		spaceship = new SpaceShip(startingXPosition, startingYPosition);

		enemyManager.startLevel();

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
		if (spaceship.isVisible()) {
			g.drawImage(spaceship.getImage(), spaceship.getXCoordinate(), spaceship.getYCoordinate(), this);
		}

		// Draw missiles
		List<Missile> ms = spaceship.getMissiles();
		for (Missile missile : ms) {
			if (missile.isVisible()) {
				g.drawImage(missile.getImage(), missile.getXCoordinate(), missile.getYCoordinate(), this);
			}
		}

		// Draw aliens
		for (Enemy enemy : enemyManager.getEnemies()) {
			if (enemy.isVisible()) {
				g.drawImage(enemy.getImage(), enemy.getXCoordinate(), enemy.getYCoordinate(), this);
			}
		}

		List<Animation> animationList = animationLoader.getAnimations();
		for (int i = 0; i < animationList.size(); i++) {
			g.drawImage(animationList.get(i).getImage(), animationList.get(i).getXCoordinate(),
					animationList.get(i).getYCoordinate(), this);
			animationLoader.updateAnimationList(animationList.get(i));
		}

		// Draw the score/aliens left
		g.setColor(Color.WHITE);
		g.drawString("Aliens left: " + enemyManager.getEnemies().size(), 5, 15);
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
		updateShip();
		updateMissiles();
		updateEnemies();
		checkCollisions();
		updateLevel();
		repaint();
	}

	private void updateLevel() {
		if (enemyManager.getEnemies().size() <= 0) {
			enemyManager.levelUp();
		}
	}

	// Checks wether the game is still running, stops the timer is not.
	private void inGame() {
		if (!ingame) {
			timer.stop();
		}
	}

	private void updateShip() {
		if (spaceship.isVisible()) {
			spaceship.move();
		}
	}

	private void updateEnemies() {
		List<Enemy> enemies = enemyManager.getEnemies();
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			if (enemy.isVisible()) {
				enemy.move();
			} else {
				enemyManager.removeEnemy(enemy);
			}
		}
	}

	// Updates the missiles list. DO NOT ITERATE OVER "missiles
	private void updateMissiles() {
		List<Missile> missiles = spaceship.getMissiles();
		for (int i = 0; i < missiles.size(); i++) {
			Missile missile = missiles.get(i);

			if (missile.isVisible()) {
				missile.move();
			} else {
				missiles.remove(i);
			}
		}
	}

	public void checkCollisions() {
		Rectangle spaceshipBounds = spaceship.getBounds();

		for (Enemy enemy : enemyManager.getEnemies()) {
			Rectangle alienBounds = enemy.getBounds();
			if (spaceshipBounds.intersects(alienBounds)) {
				spaceship.setVisible(false);
				enemy.setVisible(false);
				ingame = false;
			}
		}

		List<Missile> ms = spaceship.getMissiles();
		for (Missile m : ms) {
			Rectangle r1 = m.getBounds();
			for (Enemy enemy : enemyManager.getEnemies()) {
				Rectangle r2 = enemy.getBounds();
				if (r1.intersects(r2)) {
					enemy.takeDamage(m.getMissileDamage());
					animationLoader.addAnimation(
							new Animation(m.getXCoordinate(), m.getYCoordinate(), "Impact Explosion One"));
					m.setVisible(false);
				}
			}
		}
	}

	// Required to read key presses
	private class TAdapter extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			spaceship.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			spaceship.keyPressed(e);
		}
	}
}