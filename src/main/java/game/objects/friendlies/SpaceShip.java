package game.objects.friendlies;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.DataClass;
import data.image.ImageRotator;
import game.managers.AnimationManager;
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
	private float attackSpeed;
	private float currentAttackFrame;
	private float shieldRegenDelay;
	private float currentShieldRegenDelayFrame;
	private float maxHitPoints;
	private float maxShieldHitPoints;
	private int movementSpeed;
	private String currentExhaust;
	private Animation exhaustAnimation;
	private float homingRectangleResizeScale;
	private int homingRectangleXCoordinate;
	private int homingRectangleYCoordinate;
	private int homingRectangleWidth;
	private int homingRectangleHeight;

	private MissileManager missileManager = MissileManager.getInstance();
	private AudioManager audioManager = AudioManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private ImageRotator imageRotator = ImageRotator.getInstance();

	private List<Animation> shieldAnimations = new ArrayList<Animation>();

//	private int rotateTestCount = 0;
//	private int rotateTestMaxCoun = 100;
//	private int rotatedtimes = 0;

	public SpaceShip(String shipImage, String exhaustImageType) {
		super(DataClass.getInstance().getWindowWidth() / 10, DataClass.getInstance().getWindowHeight() / 2, 1);
		loadImage(shipImage);
		setExhaustAnimation(exhaustImageType);
		initShip();
	}

	protected void setExhaustAnimation(String imageType) {
		this.exhaustAnimation = new Animation(xCoordinate, yCoordinate, imageType, true, scale);
		currentExhaust = imageType;
//		this.exhaustAnimation.rotateAnimetion("Down");
	}

	// Called when managers need to be reset.
	public void resetSpaceship() {
		initShip();
	}

	private void initShip() {
		directionx = 0;
		directiony = 0;
		this.hitpoints = 150000000;
		this.maxHitPoints = 150000000;
		this.shieldHitpoints = 100;
		this.maxShieldHitPoints = 100;
		movementSpeed = 2;
		attackSpeed = 15;
		currentAttackFrame = 0;
		shieldRegenDelay = 300;
		currentShieldRegenDelayFrame = 0;
		movementSpeed = 2;
	}

	public void addShieldDamageAnimation() {
		cycleShieldAnimations();
		if (shieldAnimations.size() < 10) {
			Animation shieldAnimation = new Animation(this.xCoordinate, this.yCoordinate,
					"Default Player Shield Damage", false, 1);
			AnimationManager animationManager = AnimationManager.getInstance();
			animationManager.addUpperAnimation(shieldAnimation);
			shieldAnimations.add(shieldAnimation);
		}
	}

	private void cycleShieldAnimations() {
		for (int i = 0; i < shieldAnimations.size(); i++) {
			if (!shieldAnimations.get(i).isVisible()) {
				shieldAnimations.remove(i);
			}
		}
	}

	public void takeHitpointDamage(float damage) {
		float shieldPiercingDamage = shieldHitpoints - damage;
		if (shieldPiercingDamage < 0) {
			this.hitpoints += shieldPiercingDamage;
			this.shieldHitpoints = 0;
		} else {
			shieldHitpoints -= damage;
			addShieldDamageAnimation();
		}

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
		homingRectangleResizeScale = (float) 1.5;
		homingRectangleXCoordinate = (int) (xCoordinate - (width * homingRectangleResizeScale));
		homingRectangleYCoordinate = (int) (yCoordinate - (height * homingRectangleResizeScale));
		homingRectangleWidth = (int) (width * (homingRectangleResizeScale * 2));
		homingRectangleHeight = (int) (height * (homingRectangleResizeScale * 2.25));
		if (this.exhaustAnimation != null) {
			this.exhaustAnimation.setX(this.getCenterXCoordinate() - (this.getWidth()));
			this.exhaustAnimation.setY(this.getCenterYCoordinate() - (exhaustAnimation.getHeight() / 2) + 5);
		}

		for (Animation anim : shieldAnimations) {
			anim.setX(xCoordinate);
			anim.setY(yCoordinate);
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

	private void swapExhaust(String exhaustName, float scale, int xOffset) {
		if (!exhaustName.equals(exhaustAnimation.getImageType())) {
			exhaustAnimation.changeImageType(exhaustName);
			exhaustAnimation.setAnimationScale(scale);
			exhaustAnimation.addXOffset(xOffset);
			this.currentExhaust = exhaustName;
		}
	}

	// Move the spaceship in target direction
	public void keyPressed(KeyEvent e) {
		int privyMovementSpeed = movementSpeed;
		if (privyMovementSpeed == 2 && currentExhaust.equals("Default Player Engine Boosted")) {
			privyMovementSpeed = 8;
		} else if (privyMovementSpeed == 8 && currentExhaust.equals("Default Player Engine")) {
			privyMovementSpeed = 2;
		}

		int key = e.getKeyCode();
		
		
		if(key == KeyEvent.VK_SPACE) {
			fire();
		}
		if(key == KeyEvent.VK_A) {
			directionx = -privyMovementSpeed;
		}
		if(key == KeyEvent.VK_D) {
			directionx = privyMovementSpeed;
		}
		if(key == KeyEvent.VK_W) {
			directiony = -privyMovementSpeed;
		}
		if(key == KeyEvent.VK_S) {
			directiony = privyMovementSpeed;
		}
		
		if(key == KeyEvent.VK_SHIFT) {
			movementSpeed = 8;
			swapExhaust("Default Player Engine Boosted", (float) 1, -30);
		}
//		
//		switch (key) {
//		case (KeyEvent.VK_SPACE):
//			fire();
//			break;
//		case (KeyEvent.VK_A):
//			directionx = -privyMovementSpeed;
//			break;
//		case (KeyEvent.VK_D):
//			directionx = privyMovementSpeed;
//			break;
//		case (KeyEvent.VK_W):
//			directiony = -privyMovementSpeed;
//			break;
//		case (KeyEvent.VK_S):
//			directiony = privyMovementSpeed;
//			break;
//		case (KeyEvent.VK_SHIFT):
//			movementSpeed = 8;
//			swapExhaust("Default Player Engine Boosted", (float) 1, -30);
//			break;
//		}
	}

	// Halt movement of spaceship
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if(key == KeyEvent.VK_SPACE) {
			fire();
		}
		if(key == KeyEvent.VK_A) {
			directionx = 0;
		}
		if(key == KeyEvent.VK_D) {
			directionx = 0;
		}
		if(key == KeyEvent.VK_W) {
			directiony = 0;
		}
		if(key == KeyEvent.VK_S) {
			directiony = 0;
		}
		
		if(key == KeyEvent.VK_SHIFT) {
			movementSpeed = 2;
			swapExhaust("Default Player Engine", (float) 1, 0);
		}
		
		
//		switch (key) {
//		case (KeyEvent.VK_A):
//			directionx = 0;
//			break;
//		case (KeyEvent.VK_D):
//			directionx = 0;
//			break;
//		case (KeyEvent.VK_W):
//			directiony = 0;
//			break;
//		case (KeyEvent.VK_S):
//			directiony = 0;
//			break;
//		case (KeyEvent.VK_SHIFT):
//			movementSpeed = 2;
//			swapExhaust("Default Player Engine", (float) 1, 0);
//			break;
//		}

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

	public void setMovementSpeed(int movementSpeed) {
		this.movementSpeed = movementSpeed;
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

	// Called by homing missiles, returns the invisible rectangle around the ship
	// where the missiles lose their target lock
	public Rectangle getHomingRangeBounds() {
		// Casting leads to problems with anything but integers. Fortunately,
		// coordinations and pixels cannot have decimals.

		homingRectangleResizeScale = (float) 1.5;
		homingRectangleXCoordinate = (int) (xCoordinate - (width * homingRectangleResizeScale));
		homingRectangleYCoordinate = (int) (yCoordinate - (height * homingRectangleResizeScale));
		homingRectangleWidth = (int) (width * (homingRectangleResizeScale * 2));
		homingRectangleHeight = (int) (height * (homingRectangleResizeScale * 2.25));
		return new Rectangle(homingRectangleXCoordinate, homingRectangleYCoordinate, homingRectangleWidth,
				homingRectangleHeight);
	}
}
