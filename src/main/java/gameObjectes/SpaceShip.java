package gameObjectes;

import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import Data.DataClass;
import gameManagers.AudioManager;
import gameManagers.FriendlyManager;
import gameManagers.MissileManager;

public class SpaceShip extends Sprite {

	private int directionx;
	private int directiony;
	private float hitpoints;
	private float attackSpeed = 15;
	private float currentAttackFrame = 0;
	private MissileManager missileManager = MissileManager.getInstance();
	private AudioManager audioManager = AudioManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();

	public SpaceShip() {
		super(DataClass.getInstance().getWindowWidth() / 10, DataClass.getInstance().getWindowHeight() / 2);
		initSpaceShip();
	}

	private void initSpaceShip() {
		loadImage("spaceship");
		getImageDimensions();
		setShipHealth();
	}

	private void setShipHealth() {
		this.hitpoints = 150;
	}

	public void takeHitpointDamage(float damage) {
		this.hitpoints -= damage;
	}
	
	public void updateAttackCooldown() {
		this.currentAttackFrame++;
	}

	//Moves the spaceship
	public void move() {
		xCoordinate += directionx;
		yCoordinate += directiony;
	}

	// Launch a missile from the center point of the spaceship
	public void fire() {
		if (missileManager == null || friendlyManager == null) {
			missileManager = MissileManager.getInstance();
			friendlyManager = FriendlyManager.getInstance();
		}

		if (currentAttackFrame >= attackSpeed) {
			try {
				this.audioManager.firePlayerMissile();
				this.missileManager.addFriendlyMissile(new Missile(xCoordinate + width, yCoordinate + height / 2,
						friendlyManager.getPlayerMissileType()));
				currentAttackFrame = 0;
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Move the spaceship in target direction
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case (KeyEvent.VK_SPACE):
			fire();
			break;
		case (KeyEvent.VK_A):
			directionx = -2;
			break;
		case (KeyEvent.VK_D):
			directionx = 2;
			break;
		case (KeyEvent.VK_W):
			directiony = -2;
			break;
		case (KeyEvent.VK_S):
			directiony = 2;
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

	public float getHitpoints() {
		return this.hitpoints;
	}
}
