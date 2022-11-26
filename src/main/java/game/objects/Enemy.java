package game.objects;

import java.util.List;
import java.util.Random;

import data.DataClass;
import data.movement.Trajectory;
import game.managers.AnimationManager;
import game.managers.MissileManager;
import image.objects.Sprite;

public class Enemy extends Sprite {

	private MissileManager missileManager = MissileManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private float hitPoints;
	private float maxHitPoints;
	private String enemyType;
	private float attackSpeedFrameCount;
	private float currentAttackSpeedFrameCount = 0;
	private Random random = new Random();
	private int movementSpeed;
	private int currentBoardBlock;
	private String rotation;
	private boolean hasAttack;
	private String direction;
	private Trajectory trajectory = new Trajectory();

	public Enemy(int x, int y, String enemyType, int currentBoardBlock, String direction) {
		super(x, y);
		this.enemyType = enemyType;
		this.currentBoardBlock = currentBoardBlock;
		this.direction = direction;
		initEnemy();
	}

	private void initEnemy() {
		if (this.enemyType.equals("Default Alien Spaceship")) {
			this.hitPoints = 35;
			this.maxHitPoints = 35;
			this.attackSpeedFrameCount = 150;
			loadImage("Default Alien Spaceship");
			this.movementSpeed = 1;
			this.hasAttack = true;

		}
		if (this.enemyType.equals("Alien Bomb")) {
			this.hitPoints = 10;
			this.maxHitPoints = 10;
			this.attackSpeedFrameCount = 999999;
			loadImage("Alien Bomb");
			this.movementSpeed = 1;
			this.hasAttack = false;
		}
		this.setRotation(direction);
		this.trajectory.setTrajectoryType(this);
	}

	// Random offset for the origin of the missile the enemy shoots
	private int calculateRandomWeaponHeightOffset() {
		int upOrDown = random.nextInt((1 - 0) + 1) + 1;
		int yOffset = random.nextInt(((this.getHeight() / 2) - 0) + 1) + 0;
		if (upOrDown == 1) {
			return yOffset;
		} else {
			return -yOffset;
		}
	}

	public void updateBoardBlock(int boardBlockNumber) {
		if (boardBlockNumber != this.currentBoardBlock) {
			this.currentBoardBlock = boardBlockNumber;
			this.updateMovementSpeed();
			this.trajectory.updateMovementSpeed(movementSpeed);
		}
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

	// Called every loop to move the enemy
	public void move() {
		List<Integer> newCoordsList = trajectory.getPathCoordinates(xCoordinate, yCoordinate);
		xCoordinate = newCoordsList.get(0);
		yCoordinate = newCoordsList.get(1);

		switch (enemyType) {
		case ("Default Alien Spaceship"):
			if (xCoordinate < 0) {
				this.setVisible(false);
			}
			break;
		case ("Alien Bomb"):
			if (direction.equals("Up")) {
				if (yCoordinate <= 0) {
					this.setVisible(false);
				}
			} else if (direction.equals("Down")) {
				if (yCoordinate >= DataClass.getInstance().getWindowHeight()) {
					this.setVisible(false);
				}
			}
			break;
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

	// Call the corresponding boardBlockSpeed modification depending on the current
	// boardBlock

	private void updateMovementSpeed() {
		switch (currentBoardBlock) {
		case (0):
			boardBlockZeroSpeed();
			break;
		case (1):
			boardBlockOneSpeed();
			break;
		case (2):
			boardBlockTwoSpeed();
			break;
		case (3):
			boardBlockThreeSpeed();
			break;
		case (4):
			boardBlockFourSpeed();
			break;
		case (5):
			boardBlockFiveSpeed();
			break;
		case (6):
			boardBlockSixSpeed();
			break;
		case (7):
			boardBlockSevenSpeed();
			break;
		case (8):
			boardBlockEightSpeed();
			break;
		}
	}

	// Enemy speeds for block 0
	private void boardBlockZeroSpeed() {
		switch (this.enemyType) {
		case ("Default Alien Spaceship"):
			this.movementSpeed = 1;
			break;
		}
	}

	// Enemy speeds for block 1
	private void boardBlockOneSpeed() {
		switch (this.enemyType) {
		case ("Default Alien Spaceship"):
			this.movementSpeed = 1;
			break;
		}
	}

	// Enemy speeds for block 2
	private void boardBlockTwoSpeed() {
		switch (this.enemyType) {
		case ("Default Alien Spaceship"):
			this.movementSpeed = 1;
			break;
		}
	}

	// Enemy speeds for block 3
	private void boardBlockThreeSpeed() {
		switch (this.enemyType) {
		case ("Default Alien Spaceship"):
			this.movementSpeed = 1;
			break;
		}
	}

	// Enemy speeds for block 4
	private void boardBlockFourSpeed() {
		switch (this.enemyType) {
		case ("Default Alien Spaceship"):
			this.movementSpeed = 1;
			break;
		}
	}

	// Enemy speeds for block 5
	private void boardBlockFiveSpeed() {
		switch (this.enemyType) {
		case ("Default Alien Spaceship"):
			this.movementSpeed = 1;
			break;
		}
	}

	// Enemy speeds for block 6
	private void boardBlockSixSpeed() {
		switch (this.enemyType) {
		case ("Default Alien Spaceship"):
			this.movementSpeed = 2;
			break;
		}
	}

	// Enemy speeds for block 7
	private void boardBlockSevenSpeed() {
		switch (this.enemyType) {
		case ("Default Alien Spaceship"):
			this.movementSpeed = 3;
			break;
		}
	}

	// Enemy speeds for block 8
	private void boardBlockEightSpeed() {
		switch (this.enemyType) {
		case ("Default Alien Spaceship"):
			this.movementSpeed = 3;
			break;
		}
	}

	// Called every game tick. If weapon is not on cooldown, fire a shot.
	// Current board block attack is set to 7, this shouldnt be a hardcoded value
	// This function doesn't discern enemy types yet either, should be re-written
	// when new enemies are introduced
	public void fireAction() {
		if (missileManager == null) {
			missileManager = MissileManager.getInstance();
		}
		if (currentAttackSpeedFrameCount >= attackSpeedFrameCount) {
			if (currentBoardBlock < 7) {
				missileManager.addEnemyMissile(this.xCoordinate, this.yCoordinate + calculateRandomWeaponHeightOffset(),
						"Alien Laserbeam");
				currentAttackSpeedFrameCount = 0;
			}
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}

	}
}
