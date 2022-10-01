package gameObjectes;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SpaceShip extends Sprite {

	private int directionx;
	private int directiony;
	private List<Missile> missiles;

	public SpaceShip(int xCoordinate, int yCoordinate) {
		super(xCoordinate, yCoordinate);
		initSpaceShip();
	}

	private void initSpaceShip() {
		missiles = new ArrayList<Missile>();
		loadImage("src/resources/images/spaceship.png");
		getImageDimensions();
	}

	public void move() {
		xCoordinate += directionx;
		yCoordinate += directiony;
	}

	public List<Missile> getMissiles() {
		return missiles;
	}

	// Launch a missile from the center point of the spaceship
	public void fire() {
		missiles.add(new Missile(xCoordinate + width, yCoordinate + height / 2));
	}

	// Move the spaceship in target direction
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case (KeyEvent.VK_SPACE):
			fire();
			break;
		case (KeyEvent.VK_A):
			directionx = -1;
			break;
		case (KeyEvent.VK_D):
			directionx = 1;
			break;
		case (KeyEvent.VK_W):
			directiony = -1;
			break;
		case (KeyEvent.VK_S):
			directiony = 1;
			break;
		}
	}

	// Halt movement of spaceship
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case (KeyEvent.VK_A):
			directionx = 0;
			break;
		case (KeyEvent.VK_D):
			directionx = 0;
			break;
		case (KeyEvent.VK_W):
			directiony = 0;
			break;
		case (KeyEvent.VK_S):
			directiony = 0;
			break;
		}

	}
}
