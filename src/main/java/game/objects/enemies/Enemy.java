package game.objects.enemies;

import java.util.Random;

import data.movement.Trajectory;
import game.managers.AnimationManager;
import game.managers.MissileManager;
import image.objects.Sprite;

public class Enemy extends Sprite {

	protected MissileManager missileManager = MissileManager.getInstance();
	protected AnimationManager animationManager = AnimationManager.getInstance();
	protected float hitPoints;
	protected float maxHitPoints;
	protected String enemyType;
	protected float attackSpeedFrameCount;
	protected float currentAttackSpeedFrameCount = 0;
	protected Random random = new Random();
	protected int movementSpeed;
	protected int currentBoardBlock;
	protected String rotation;
	protected boolean hasAttack;
	protected String direction;
	protected Trajectory trajectory = new Trajectory();

	public Enemy(int x, int y, String enemyType, String direction) {
		super(x, y);
		loadImage(enemyType);
		this.enemyType = enemyType;
		this.direction = direction;
		initEnemy();
	}

	private void initEnemy() {
		this.currentBoardBlock = 8;
		this.setRotation(direction);
		this.setVisible(true);
	}

	// Called when there is collision between friendly missile and enemy
	public void takeDamage(float damageTaken) {
		if (animationManager == null) {
			animationManager = AnimationManager.getInstance();
		}
		this.hitPoints -= damageTaken;
		if (this.hitPoints <= 0) {
			animationManager.addDestroyedExplosion(xCoordinate, yCoordinate);
			this.setVisible(false);
		}
	}

	public float getCurrentHitpoints() {
		return this.hitPoints;
	}

	public float getMaxHitpoints() {
		return this.maxHitPoints;
	}

	public String getEnemyType() {
		return this.enemyType;
	}

	public String getEnemyDirection() {
		return this.direction;
	}

	public int getMovementSpeed() {
		return this.movementSpeed;
	}

	public boolean getHasAttack() {
		return this.hasAttack;
	}

	public int getBoardBlockNumber() {
		return this.currentBoardBlock;
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
		switch (rotation) {
		case ("Up"):
			rotateImage(Math.PI * 1.5);
			break;
		case ("Down"):
			rotateImage(Math.PI * 0.5);
			break;
		case ("Left"):
			rotateImage(0);
			break;
		case ("Right"):
			rotateImage(Math.PI);
			break;
		}
	}
}
