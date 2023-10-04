package game.objects.enemies;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.managers.AnimationManager;
import game.movement.Direction;
import game.movement.HomingPathFinder;
import game.movement.MovementConfiguration;
import game.movement.OrbitPathFinder;
import game.movement.PathFinder;
import game.movement.Point;
import game.movement.RegularPathFinder;
import game.movement.SpriteMover;
import game.movement.SpriteRemover;
import game.movement.pathfinderconfigs.HomingPathFinderConfig;
import game.movement.pathfinderconfigs.OrbitPathFinderConfig;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.RegularPathFinderConfig;
import game.objects.missiles.MissileManager;
import gamedata.DataClass;
import gamedata.audio.AudioEnums;
import gamedata.audio.AudioManager;
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
	protected MovementConfiguration moveConfig;
	
	// Enemy miscellanious attributes
	private boolean isFriendly = false;
	protected EnemyEnums enemyType;
	protected AudioEnums deathSound;
	protected boolean showHealthBar;
	protected int lastBoardBlock;
	protected SpriteAnimation exhaustAnimation = null;
	protected SpriteAnimation deathAnimation;
	protected SpriteAnimation animation = null;

	protected List<Enemy> followingEnemies = new LinkedList<Enemy>();

	public Enemy(int x, int y, Point destination, Direction rotation, EnemyEnums enemyType, float scale,
			PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed) {
		super(x, y, scale);
		this.enemyType = enemyType;
		this.currentLocation = new Point(x, y);
		moveConfig = new MovementConfiguration();
		updateCurrentBoardBlock();
		this.lastBoardBlock = currentBoardBlock;
		initMovementConfiguration(currentLocation, destination, pathFinder, rotation, xMovementSpeed, yMovementSpeed);
		this.setIsFriendly(isFriendly);
	}
	
	private void initMovementConfiguration(Point currentLocation, Point destination, PathFinder pathFinder,
			Direction rotation, int xMovementSpeed, int yMovementSpeed) {
		moveConfig = new MovementConfiguration();
		moveConfig.setPathFinder(pathFinder);
		moveConfig.setCurrentLocation(currentLocation);
		moveConfig.setDestination(destination);
		moveConfig.setRotation(rotation);
		moveConfig.setXMovementSpeed(xMovementSpeed);
		moveConfig.setYMovementSpeed(yMovementSpeed);
		moveConfig.setStepsTaken(0);
		moveConfig.setHasLock(true);
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
			this.deathAnimation.setOriginCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
			animationManager.addDestroyedExplosion(deathAnimation);

			for (Enemy enemy : followingEnemies) {
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
		SpriteMover.getInstance().moveSprite(this, moveConfig);
		if (this.animation != null) {
			if (moveConfig.getCurrentPath().getWaypoints().size() > 0) {
				animation.setX(moveConfig.getCurrentPath().getWaypoints().get(0).getX());
				animation.setY(moveConfig.getCurrentPath().getWaypoints().get(0).getY());
				animation.updateCurrentBoardBlock();
			}
		}
		
		bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
		updateCurrentBoardBlock();
		updateAnimationCoordinates();
		updateVisibility();
	}
	
	//Remove the enemy if it's out of bounds, and remove it's animation if it has one
	private void updateVisibility() {
		if(SpriteRemover.getInstance().shouldRemoveVisibility(this, moveConfig)) {
			this.visible = false;
		}
		
		if (!this.isVisible() && this.animation != null) {
			this.animation.setVisible(false);
		}
		
	}

	// Sets the animations (the graphics of enemy) to align with the missiles
	// coordinates
	private void updateAnimationCoordinates() {
		if (animation != null) {
			animation.setX(xCoordinate);
			animation.setY(yCoordinate);
			animation.setAnimationBounds(animation.getXCoordinate(), animation.getYCoordinate());
			animation.updateCurrentBoardBlock();
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
		return moveConfig.getXMovementSpeed();
	}

	public int getYMovementSpeed() {
		return moveConfig.getYMovementSpeed();
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
		moveConfig.setRotation(rotation);
		rotateImage(rotation);
		if(animation != null) {
			animation.rotateAnimetion(rotation);
		}
	}

	public SpriteAnimation getAnimation() {
		return this.animation;
	}

	public void fireAction() {
		// This could contain default behaviour but SHOULD be overriden by specific enemytype
		// classes.

	}

	public PathFinder getPathFinder() {
		return moveConfig.getPathFinder();
	}


}