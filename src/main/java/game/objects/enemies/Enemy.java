package game.objects.enemies;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.managers.AnimationManager;
import game.managers.AudioManager;
import game.movement.Direction;
import game.movement.HomingPathFinder;
import game.movement.OrbitPathFinder;
import game.movement.Path;
import game.movement.PathFinder;
import game.movement.Point;
import game.movement.RegularPathFinder;
import game.movement.pathfinderconfigs.HomingPathFinderConfig;
import game.movement.pathfinderconfigs.OrbitPathFinderConfig;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.RegularPathFinderConfig;
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
	protected int xMovementSpeed;
	protected int yMovementSpeed;
	private int lastUsedXMovementSpeed;
	private int lastUsedYMovementSpeed;
	protected int currentBoardBlock;

	// Enemy miscellanious attributes
	private boolean isFriendly = false;
	private boolean gotANewPathFinder = false;
	protected Direction rotation;
	protected EnemyEnums enemyType;
	protected AudioEnums deathSound;
	protected boolean showHealthBar;
	protected int lastBoardBlock;
	protected SpriteAnimation exhaustAnimation = null;
	protected SpriteAnimation deathAnimation;
	protected SpriteAnimation animation = null;
	
	protected List<Enemy> followingEnemies = new LinkedList<Enemy>();
	private int lastKnownTargetX;
	private int lastKnownTargetY;

	public Enemy(int x, int y, Point destination, Direction rotation, EnemyEnums enemyType, float scale,
			PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed) {
		super(x, y, scale);
		this.enemyType = enemyType;
		this.currentLocation = new Point(x, y);
		this.destination = destination;
		this.rotation = rotation;
		updateCurrentBoardBlock();
		this.lastBoardBlock = currentBoardBlock;
		this.pathFinder = pathFinder;
		this.xMovementSpeed = xMovementSpeed;
		this.yMovementSpeed = yMovementSpeed;
	}

	protected void setExhaustanimation(ImageEnums imageType) {
		this.exhaustAnimation = new SpriteAnimation(xCoordinate, yCoordinate, imageType, true, scale);
	}

	protected void setDeathAnimation(ImageEnums imageType) {
		this.deathAnimation = new SpriteAnimation(xCoordinate, yCoordinate, imageType, false, scale);
	}
	
	protected void setAnimation(ImageEnums imageType) {
		this.animation = new SpriteAnimation(xCoordinate, yCoordinate, imageType, false, scale);
	}

	// Called when there is collision between friendly missile and enemy
	public void takeDamage(float damageTaken) {
		if (animationManager == null || audioManager == null) {
			animationManager = AnimationManager.getInstance();
			audioManager = AudioManager.getInstance();
		}

		this.hitPoints -= damageTaken;
		if (this.hitPoints <= 0) {
//			this.deathAnimation.setX(this.getCenterXCoordinate() - (deathAnimation.getWidth() / 2));
//			this.deathAnimation.setY(this.getCenterYCoordinate() - (deathAnimation.getHeight() / 2));
			this.deathAnimation.setOriginCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
			animationManager.addDestroyedExplosion(deathAnimation);
			
			for(Enemy enemy : followingEnemies) {
				enemy.takeDamage(99999);
			}
			
			try {
				audioManager.addAudio(deathSound);
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
			this.setVisible(false);
		}
	}

	public void move() {
		if (currentPath == null || currentPath.getWaypoints().isEmpty() || xMovementSpeed != lastUsedXMovementSpeed
				|| yMovementSpeed != lastUsedYMovementSpeed || pathFinder.shouldRecalculatePath(currentPath) || gotANewPathFinder) {
			// calculate a new path if necessary
			PathFinderConfig config = getConfigByPathFinder(pathFinder);
			currentPath = pathFinder.findPath(config);
			lastUsedXMovementSpeed = xMovementSpeed;
			lastUsedYMovementSpeed = yMovementSpeed;
			gotANewPathFinder = false;
		}
		currentPath.updateCurrentLocation(currentLocation);
		// get the next point from the path
		
		
		if (pathFinder instanceof OrbitPathFinder) {
			// Check if the target's position has changed
			Sprite target = ((OrbitPathFinder) pathFinder).getTarget();

			if (target.getCenterXCoordinate() != lastKnownTargetX
					|| target.getCenterYCoordinate() != lastKnownTargetY) {

				int deltaX = 0;
				int deltaY = 0;

				if (lastKnownTargetX == 0 || lastKnownTargetY == 0) {
					deltaX = 0;
					deltaY = 0;
				} else {
					deltaX = target.getCenterXCoordinate() - lastKnownTargetX;
					deltaY = target.getCenterYCoordinate() - lastKnownTargetY;
				}

				// Update the last known position
				lastKnownTargetX = target.getCenterXCoordinate();
				lastKnownTargetY = target.getCenterYCoordinate();

				// Adjust the orbit path based on the deltas
				((OrbitPathFinder) pathFinder).adjustPathForTargetMovement(currentPath, deltaX, deltaY);
			}
		}
		

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
	
	//Used for movement initialization or recalculation
	private PathFinderConfig getConfigByPathFinder(PathFinder pathFinder) {
		PathFinderConfig config = null;
		if (pathFinder instanceof OrbitPathFinder) {
			config = new OrbitPathFinderConfig(currentLocation, rotation, isFriendly);
		} else if (pathFinder instanceof RegularPathFinder) {
			config = new RegularPathFinderConfig(currentLocation, destination, xMovementSpeed, yMovementSpeed, isFriendly, rotation);
		} else if (pathFinder instanceof HomingPathFinder) {
			config = new HomingPathFinderConfig(currentLocation, rotation, true, isFriendly);
		}
		return config;
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
		return this.xMovementSpeed;
	}

	public int getYMovementSpeed() {
		return this.xMovementSpeed;
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
	
	public SpriteAnimation getAnimation() {
		return this.animation;
	}

	public void fireAction() {
		// This could contain default behaviour but SHOULD be overriden by specific enemytype
		// classes.

	}
	
	public PathFinder getPathFinder() {
		return this.pathFinder;
	}
	
	public void setPathFinder(PathFinder pathFinder) {
		this.pathFinder = pathFinder;
		gotANewPathFinder = true;
	}

}