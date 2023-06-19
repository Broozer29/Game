package game.objects.enemies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.DataClass;
import data.audio.AudioEnums;
import data.image.enums.EnemyEnums;
import data.image.enums.ImageEnums;
import game.managers.AnimationManager;
import game.managers.AudioManager;
import game.managers.MissileManager;
import game.movement.Direction;
import game.movement.Path;
import game.movement.PathFinder;
import game.movement.Point;
import image.objects.Sprite;
import image.objects.SpriteAnimation;

public class Enemy extends Sprite {

	protected MissileManager missileManager = MissileManager.getInstance();
	protected AnimationManager animationManager = AnimationManager.getInstance();
	protected AudioManager audioManager = AudioManager.getInstance();
	protected Random random = new Random();

	// Enemy combat stats
	protected float hitPoints;
	protected float maxHitPoints;
	protected float attackSpeedFrameCount;
	protected float currentAttackSpeedFrameCount = 0;
	protected boolean hasAttack;

	// Enemy new movement:
	private Point currentLocation;
	private Point destination;
	private PathFinder pathFinder;
	private Path currentPath;
	protected int XMovementSpeed;
	protected int YMovementSpeed;
	private int lastUsedXMovementSpeed;
	private int lastUsedYMovementSpeed;
	protected int currentBoardBlock;

	// Enemy miscellanious attributes
	private boolean isFriendly = false;
	protected Direction rotation;
	protected EnemyEnums enemyType;
	protected AudioEnums deathSound;
	protected boolean showHealthBar;
	protected List<Integer> boardBlockSpeeds = new ArrayList<Integer>();
	protected SpriteAnimation exhaustAnimation = null;
	protected SpriteAnimation deathAnimation;

	public Enemy(int x, int y, Point destination, Direction rotation, EnemyEnums enemyType, float scale,
			PathFinder pathFinder) {
		super(x, y, scale);
		this.enemyType = enemyType;
		this.currentLocation = new Point(x, y);
		this.destination = destination;
		this.rotation = rotation;
		this.currentBoardBlock = 8;
		this.pathFinder = pathFinder;
	}

	protected void setExhaustanimation(ImageEnums imageType) {
		this.exhaustAnimation = new SpriteAnimation(xCoordinate, yCoordinate, imageType, true, scale);
	}

	protected void setDeathAnimation(ImageEnums imageType) {
		this.deathAnimation = new SpriteAnimation(xCoordinate, yCoordinate, imageType, false, scale);
	}

	// Called when there is collision between friendly missile and enemy
	public void takeDamage(float damageTaken) {
		if (animationManager == null || audioManager == null) {
			animationManager = AnimationManager.getInstance();
			audioManager = AudioManager.getInstance();
		}

		this.hitPoints -= damageTaken;
		if (this.hitPoints <= 0) {
			this.deathAnimation.setX(this.getCenterXCoordinate() - (deathAnimation.getWidth() / 2));
			this.deathAnimation.setY(this.getCenterYCoordinate() - (deathAnimation.getHeight() / 2));
			animationManager.addDestroyedExplosion(deathAnimation);
			try {
				audioManager.addAudio(deathSound);
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
			this.setVisible(false);
		}
	}

	boolean changedMovementSpeed = false;

	public void updateBoardBlock() {
		int boardBlockSize = DataClass.getInstance().getWindowWidth() / 8;
		if (xCoordinate >= 0 && xCoordinate <= (boardBlockSize * 1)) {
			this.XMovementSpeed = boardBlockSpeeds.get(0);
			changedMovementSpeed = true;
		} else if (xCoordinate >= (boardBlockSize * 1) && xCoordinate <= (boardBlockSize * 2)) {
			this.XMovementSpeed = boardBlockSpeeds.get(1);
			changedMovementSpeed = true;
		} else if (xCoordinate >= (boardBlockSize * 2) && xCoordinate <= (boardBlockSize * 3)) {
			this.XMovementSpeed = boardBlockSpeeds.get(2);
			changedMovementSpeed = true;
		} else if (xCoordinate >= (boardBlockSize * 3) && xCoordinate <= (boardBlockSize * 4)) {
			this.XMovementSpeed = boardBlockSpeeds.get(3);
			changedMovementSpeed = true;
		} else if (xCoordinate >= (boardBlockSize * 4) && xCoordinate <= (boardBlockSize * 5)) {
			this.XMovementSpeed = boardBlockSpeeds.get(4);
			changedMovementSpeed = true;
		} else if (xCoordinate >= (boardBlockSize * 5) && xCoordinate <= (boardBlockSize * 6)) {
			this.XMovementSpeed = boardBlockSpeeds.get(5);
			changedMovementSpeed = true;
		} else if (xCoordinate >= (boardBlockSize * 6) && xCoordinate <= (boardBlockSize * 7)) {
			this.XMovementSpeed = boardBlockSpeeds.get(6);
			changedMovementSpeed = true;
		} else if (xCoordinate >= (boardBlockSize * 7) && xCoordinate <= (boardBlockSize * 8)) {
			this.XMovementSpeed = boardBlockSpeeds.get(7);
			changedMovementSpeed = true;
		} else if (xCoordinate > boardBlockSize * 8) {
			changedMovementSpeed = true;
			this.XMovementSpeed = boardBlockSpeeds.get(7);
		}

	}

	public void move() {
		if (currentPath == null || currentPath.getWaypoints().isEmpty() || XMovementSpeed != lastUsedXMovementSpeed
				|| YMovementSpeed != lastUsedYMovementSpeed || pathFinder.shouldRecalculatePath(currentPath)) {
			// calculate a new path if necessary
			currentPath = pathFinder.findPath(currentLocation, destination, XMovementSpeed, YMovementSpeed, rotation, isFriendly);
			lastUsedXMovementSpeed = XMovementSpeed;
			lastUsedYMovementSpeed = YMovementSpeed;
		}

		currentPath.updateCurrentLocation(currentLocation);
		// get the next point from the path
		Point nextPoint = currentPath.getWaypoints().get(0);

		// move towards the next point
		currentLocation = nextPoint;
		this.xCoordinate = nextPoint.getX();
		this.yCoordinate = nextPoint.getY();

		// if reached the next point, remove it from the path
		if (currentLocation.equals(nextPoint)) {
			currentPath.getWaypoints().remove(0);
		}

		if (this.exhaustAnimation != null) {
			this.exhaustAnimation.setX(this.getCenterXCoordinate() + (this.getWidth() / 2));
			this.exhaustAnimation.setY(this.getCenterYCoordinate() - (exhaustAnimation.getHeight() / 2));
		}

		switch (rotation) {
		case UP:
			if (yCoordinate <= 0) {
				this.setVisible(false);
			}
			break;
		case DOWN:
			if (yCoordinate >= DataClass.getInstance().getWindowHeight()) {
				this.setVisible(false);
			}
			break;
		case LEFT:
			if (xCoordinate < 0) {
				this.setVisible(false);
			}
			break;
		case RIGHT:
			if (xCoordinate > DataClass.getInstance().getWindowWidth()) {
				this.setVisible(false);
			}
			break;
		case LEFT_DOWN:
			if (xCoordinate < 0 || yCoordinate >= DataClass.getInstance().getWindowHeight()) {
				this.setVisible(false);
			}
			break;
		case LEFT_UP:
			if (xCoordinate < 0 || yCoordinate <= 0) {
				this.setVisible(false);
			}
			break;
		case NONE:
			this.setVisible(false);
			break;
		case RIGHT_DOWN:
			if (xCoordinate > DataClass.getInstance().getWindowWidth()
					|| yCoordinate >= DataClass.getInstance().getWindowHeight()) {
				this.setVisible(false);
			}
			break;
		case RIGHT_UP:
			if (xCoordinate > DataClass.getInstance().getWindowWidth() || yCoordinate <= 0) {
				this.setVisible(false);
			}
			break;
		}

	}

	// Random offset for the origin of the missile the enemy shoots
	protected int calculateRandomWeaponHeightOffset() {
		int upOrDown = random.nextInt((1 - 0) + 1) + 1;
		int yOffset = random.nextInt(((this.getHeight() / 2) - 0) + 1) + 0;
		if (upOrDown == 1) {
			return yOffset;
		} else {
			return -yOffset;
		}
	}

	public float getCurrentHitpoints() {
		return this.hitPoints;
	}

	public float getMaxHitpoints() {
		return this.maxHitPoints;
	}

	public int getXMovementSpeed() {
		return this.XMovementSpeed;
	}

	public int getYMovementSpeed() {
		return this.XMovementSpeed;
	}

	public boolean showhealthBar() {
		return this.showHealthBar;
	}

	public boolean getHasAttack() {
		return this.hasAttack;
	}

	public int getBoardBlockNumber() {
		return this.currentBoardBlock;
	}

	public SpriteAnimation getExhaustAnimation() {
		return this.exhaustAnimation;
	}

	public EnemyEnums getEnemyType() {
		return this.enemyType;
	}

	public SpriteAnimation getDestroyedAnimation() {
		return this.deathAnimation;
	}

	protected void setRotation(Direction rotation) {
		this.rotation = rotation;
		rotateImage(rotation);
	}

	public void fireAction() {
		// This could contain default behaviour but SHOULD be overriden by specific enemytype
		// classes.

	}

}
