package game.objects.friendlies;

import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.DataClass;
import data.ImageRotator;
import game.managers.AudioManager;
import game.managers.FriendlyManager;
import game.managers.MissileManager;
import image.objects.Animation;
import image.objects.Sprite;

public class SpaceShip extends Sprite {

	private int directionx;
	private int directiony;
	private float hitpoints;
	private float shieldHitpoints;
	private float attackSpeed = 1;
	private float currentAttackFrame = 0;
	private float shieldRegenDelay = 300;
	private float currentShieldRegenDelayFrame = 0;
	private float maxHitPoints;
	private float maxShieldHitPoints;
	private Animation exhaustAnimation;
	private MissileManager missileManager = MissileManager.getInstance();
	private AudioManager audioManager = AudioManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private ImageRotator imageRotator = ImageRotator.getInstance();

//	private int rotateTestCount = 0;
//	private int rotateTestMaxCoun = 100;
//	private int rotatedtimes = 0;

	public SpaceShip(String shipImage, String exhaustImageType) {
		super(DataClass.getInstance().getWindowWidth() / 10, DataClass.getInstance().getWindowHeight() / 2, 1);
		loadImage(shipImage);
		setExhaustAnimation(exhaustImageType);
		setShipHealth();
	}

	protected void setExhaustAnimation(String imageType) {
		this.exhaustAnimation = new Animation(xCoordinate, yCoordinate, imageType, true, scale);
//		this.exhaustAnimation.rotateAnimetion("Down");
	}

	// Called when managers need to be reset.
	public void resetSpaceship() {
		directionx = 0;
		directiony = 0;
		this.hitpoints = 150000000;
		this.maxHitPoints = 150;
		this.shieldHitpoints = 100;
		this.maxShieldHitPoints = 100;
		attackSpeed = 15;
		currentAttackFrame = 0;
		shieldRegenDelay = 300;
		currentShieldRegenDelayFrame = 0;
	}

	private void setShipHealth() {
		this.hitpoints = 150;
		this.maxHitPoints = 150;
		this.shieldHitpoints = 100;
		this.maxShieldHitPoints = 100;
	}

	public void takeHitpointDamage(float damage) {
		float shieldPiercingDamage = shieldHitpoints - damage;
		if (shieldPiercingDamage < 0) {
			this.hitpoints += shieldPiercingDamage;
			this.shieldHitpoints = 0;
		} else
			shieldHitpoints -= damage;
		this.currentShieldRegenDelayFrame = 0;

	}

	public void updateGameTick() {
		this.currentAttackFrame++;
		this.currentShieldRegenDelayFrame++;

		if (currentShieldRegenDelayFrame >= shieldRegenDelay) {
			if (shieldHitpoints < maxShieldHitPoints) {
				repairShields((float) 0.4);
			}
		}
	}

	// Moves the spaceship
	public void move() {
		xCoordinate += directionx;
		yCoordinate += directiony;
		if (this.exhaustAnimation != null) {
			this.exhaustAnimation.setX(this.getCenterXCoordinate() - (this.getWidth()));
			this.exhaustAnimation.setY(this.getCenterYCoordinate() - (exhaustAnimation.getHeight() / 2) + 5);
		}
	}

	// Launch a missile from the center point of the spaceship
	public void fire() {
		if (missileManager == null || friendlyManager == null || audioManager == null) {
			missileManager = MissileManager.getInstance();
			friendlyManager = FriendlyManager.getInstance();
			audioManager = AudioManager.getInstance();
		}

		if (currentAttackFrame >= attackSpeed) {
			try {
				this.audioManager.firePlayerMissile();
				this.missileManager.firePlayerMissile(xCoordinate + width, yCoordinate + (height / 2) - 5,
						friendlyManager.getPlayerMissileType(), "Impact Explosion One", 0, "Right", 1);
				this.currentAttackFrame = 0;
				
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

	public float getShieldHitpoints() {
		return this.shieldHitpoints;
	}

	public void repairHealth(float healAmount) {
		this.hitpoints += healAmount;
	}

	public void repairShields(float healAmount) {
		this.shieldHitpoints += healAmount;
	}

	public float getMaxHitpoints() {
		return this.maxHitPoints;
	}

	public float getMaxShieldHitpoints() {
		return this.maxShieldHitPoints;
	}

	public Animation getExhaustAnimation() {
		return this.exhaustAnimation;
	}
}
