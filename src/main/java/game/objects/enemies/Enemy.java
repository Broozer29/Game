package game.objects.enemies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.managers.AnimationManager;
import game.managers.AudioManager;
import game.movement.Direction;
import game.movement.Path;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.missiles.MissileManager;
import gamedata.DataClass;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

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
	protected int lastBoardBlock;
	protected SpriteAnimation exhaustAnimation = null;
	protected SpriteAnimation deathAnimation;

	public Enemy(int x, int y, Point destination, Direction rotation, EnemyEnums enemyType, float scale,
			PathFinder pathFinder) {
		super(x, y, scale);
		this.enemyType = enemyType;
		this.currentLocation = new Point(x, y);
		this.destination = destination;
		this.rotation = rotation;
		updateCurrentBoardBlock();
		this.lastBoardBlock = currentBoardBlock;
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

	public void updateBoardBlockSpeed() {
		if(currentBoardBlock != lastBoardBlock) {
			this.XMovementSpeed = boardBlockSpeeds.get(currentBoardBlock);
			lastBoardBlock = currentBoardBlock;
		}
	}
	
	
	public void move() {
		if (currentPath == null || currentPath.getWaypoints().isEmpty() || XMovementSpeed != lastUsedXMovementSpeed
				|| YMovementSpeed != lastUsedYMovementSpeed || pathFinder.shouldRecalculatePath(currentPath)) {
			// calculate a new path if necessary
//			System.out.println("ik doe path vinden");
			currentPath = pathFinder.findPath(currentLocation, destination, XMovementSpeed, YMovementSpeed, rotation,
					isFriendly);
			lastUsedXMovementSpeed = XMovementSpeed;
			lastUsedYMovementSpeed = YMovementSpeed;
		}
//		System.out.println(currentPath);
		currentPath.updateCurrentLocation(currentLocation);
		// get the next point from the path

		// move towards the next point
		currentLocation = currentPath.getWaypoints().get(0);
		this.xCoordinate = currentPath.getWaypoints().get(0).getX();
		this.yCoordinate = currentPath.getWaypoints().get(0).getY();

		// if reached the next point, remove it from the path
		if (currentLocation.equals(currentPath.getWaypoints().get(0))) {
			currentPath.getWaypoints().remove(0);
		}

		if (this.exhaustAnimation != null) {
			this.exhaustAnimation.setX(this.getCenterXCoordinate() + (this.getWidth() / 2));
			this.exhaustAnimation.setY(this.getCenterYCoordinate() - (exhaustAnimation.getHeight() / 2));
		}

		bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
		updateCurrentBoardBlock();
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