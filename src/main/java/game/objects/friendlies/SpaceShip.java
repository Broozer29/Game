package game.objects.friendlies;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.DataClass;
import game.managers.AnimationManager;
import game.managers.AudioManager;
import game.managers.FriendlyManager;
import game.managers.FriendlyObjectManager;
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
	private float specialAttackSpeed;
	private float currentSpecialAttackFrame;
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

	private String defaultEngineType = "Default Player Engine";
	private String boostedEngineType = "Default Player Engine Boosted";
	private String playerFireswirl = "Player EMP";

	private MissileManager missileManager = MissileManager.getInstance();
	private AudioManager audioManager = AudioManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();

	private List<Animation> playerFollowingAnimations = new ArrayList<Animation>();
	private List<FriendlyObject> playerFollowingObjects = new ArrayList<FriendlyObject>();

	public SpaceShip(String shipImage, String exhaustImageType) {
		super(DataClass.getInstance().getWindowWidth() / 10, DataClass.getInstance().getWindowHeight() / 2, 1);
		loadImage(shipImage);
		setExhaustAnimation(exhaustImageType);
		initShip();
	}

	protected void setExhaustAnimation(String imageType) {
		this.exhaustAnimation = new Animation(xCoordinate, yCoordinate, imageType, true, scale);
		currentExhaust = imageType;
	}

	// Called when managers need to be reset.
	public void resetSpaceship() {
		initShip();
	}

	private void initShip() {
		directionx = 0;
		directiony = 0;
		this.hitpoints = 150000;
		this.maxHitPoints = 150000;
		this.shieldHitpoints = 10000;
		this.maxShieldHitPoints = 10000;
		movementSpeed = 2;
		attackSpeed = 15;
		currentAttackFrame = 15;
		shieldRegenDelay = 300;
		currentShieldRegenDelayFrame = 0;
		movementSpeed = 4;
		specialAttackSpeed = 100;
		currentSpecialAttackFrame = 100;
	}

	public void addShieldDamageAnimation() {
		if (playerFollowingAnimations.size() < 10) {
			Animation shieldAnimation = new Animation(this.xCoordinate, this.yCoordinate,
					"Default Player Shield Damage", false, 1);

			shieldAnimation.setFrameDelay(2);

//			int xDist = Math.abs(shieldAnimation.getWidth() / 8);
//			int yDist = Math.abs(this.getCenterYCoordinate() - shieldAnimation.getCenterYCoordinate());

			int yDist = 30;
			int xDist = 10;
			shieldAnimation.addXOffset(-xDist);
			shieldAnimation.addYOffset(-yDist);
			playerFollowingAnimations.add(shieldAnimation);

			shieldAnimation.setX(this.xCoordinate);
			shieldAnimation.setY(this.yCoordinate);
			AnimationManager.getInstance().addUpperAnimation(shieldAnimation);

		}

	}

	private void cycleFollowingAnimations() {
		for (int i = 0; i < playerFollowingAnimations.size(); i++) {
			if (!playerFollowingAnimations.get(i).isVisible()) {
				playerFollowingAnimations.remove(i);
			}
		}
		for (int i = 0; i < playerFollowingObjects.size(); i++) {
			if (!playerFollowingObjects.get(i).isVisible()) {
				playerFollowingObjects.remove(i);
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
		this.currentSpecialAttackFrame++;
		cycleFollowingAnimations();

		if (currentShieldRegenDelayFrame >= shieldRegenDelay) {
			if (shieldHitpoints < maxShieldHitPoints) {
				repairShields((float) 0.4);
			}
		}
	}

	// Moves the spaceship
	public void move() {
		for (Animation anim : playerFollowingAnimations) {
			anim.setX(xCoordinate);
			anim.setY(yCoordinate);
		}

		for (FriendlyObject friendly : playerFollowingObjects) {
			friendly.setX(xCoordinate);
			friendly.setY(yCoordinate);
			friendly.getAnimation().setX(xCoordinate);
			friendly.getAnimation().setY(yCoordinate);
		}

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

	}

	// Launch a missile from the center point of the spaceship
	private void fire() {
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

	private void fireSpecialAttack() {
		if (currentSpecialAttackFrame >= specialAttackSpeed) {
			FriendlyObject specialAttack = new FriendlyObject(this.getCenterXCoordinate(), this.getCenterYCoordinate(), 1);
			Animation specialAttackAnimation = new Animation(this.getCenterXCoordinate(), this.getCenterYCoordinate(), playerFireswirl, false, 1);
			specialAttack.setAnimation(specialAttackAnimation);
			specialAttack.getAnimation().setFrameDelay(3);
			specialAttack.setDamage(30);

			specialAttack.addXOffset(-(specialAttackAnimation.getWidth() / 2));
			specialAttack.addYOffset(-(specialAttackAnimation.getHeight() / 2));

			specialAttackAnimation.addXOffset(-(specialAttackAnimation.getWidth() / 2));
			specialAttackAnimation.addYOffset(-(specialAttackAnimation.getHeight() / 2));

			playerFollowingObjects.add(specialAttack);
			FriendlyObjectManager.getInstance().addActiveFriendlyObject(specialAttack);
			this.currentSpecialAttackFrame = 0;
		}
	}

	private void swapExhaust(String newEngineType) {
		if (!newEngineType.equals(currentExhaust)) {
			float scale = 1;
			int xOffset = 0;
			if (currentExhaust.contains("Boosted")) {
				scale = 1;
				xOffset = 0;
			} else {
				scale = 1;
				xOffset = -30;
			}
			exhaustAnimation.changeImagetype(newEngineType);
			exhaustAnimation.setAnimationScale(scale);
			exhaustAnimation.addXOffset(xOffset);
			this.currentExhaust = newEngineType;
		}
	}

	private final Set<Integer> pressedKeys = new HashSet<>();

	public synchronized void keyPressed(KeyEvent e) {
		pressedKeys.add(e.getKeyCode());
		if (!pressedKeys.isEmpty()) {
			for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
				switch (it.next()) {
				case (KeyEvent.VK_SPACE):
					fire();
					break;
				case (KeyEvent.VK_A):
					directionx = -movementSpeed;
					break;
				case (KeyEvent.VK_D):
					directionx = movementSpeed;
					break;
				case (KeyEvent.VK_W):
					directiony = -movementSpeed;
					break;
				case (KeyEvent.VK_S):
					directiony = movementSpeed;
					break;
				case (KeyEvent.VK_ENTER):
					fireSpecialAttack();
					break;
				case (KeyEvent.VK_SHIFT):
					movementSpeed = 8;
					swapExhaust(boostedEngineType);
					break;
				}
			}
		}
	}

	public synchronized void keyReleased(KeyEvent e) {
		pressedKeys.remove(e.getKeyCode());
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_SPACE) {
			fire();
		}
		if (key == KeyEvent.VK_A) {
			directionx = 0;
		}
		if (key == KeyEvent.VK_D) {
			directionx = 0;
		}
		if (key == KeyEvent.VK_W) {
			directiony = 0;
		}
		if (key == KeyEvent.VK_S) {
			directiony = 0;
		}
		if (key == KeyEvent.VK_SHIFT) {
			movementSpeed = 4;
			swapExhaust(defaultEngineType);
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
