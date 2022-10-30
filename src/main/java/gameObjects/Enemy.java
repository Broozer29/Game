package gameObjects;

import java.util.Random;

import Data.DataClass;
import gameManagers.MissileManager;
import imageObjects.Sprite;

public class Enemy extends Sprite {

	private float hitPoints;
	private float maxHitPoints;
	private String enemyType;
	private float attackSpeedFrameCount;
	private float currentAttackSpeedFrameCount = 0;
	private Random random = new Random();
	private int movementSpeed;
	private int currentBoardBlock;

	public Enemy(int x, int y, String enemyType, int currentBoardBlock) {
		super(x, y);
		this.enemyType = enemyType;
		this.currentBoardBlock = currentBoardBlock;
		initEnemy();
	}

	private void initEnemy() {
		if (this.enemyType.equals("Alien")) {
			this.hitPoints = 35;
			this.maxHitPoints = 35;
			this.attackSpeedFrameCount = 150;
			loadImage("Alien");
			this.movementSpeed = 1;
		}
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
		case ("Alien"):
			this.movementSpeed = 1;
			break;
		}
	}

	// Enemy speeds for block 1
	private void boardBlockOneSpeed() {
		switch (this.enemyType) {
		case ("Alien"):
			this.movementSpeed = 1;
			break;
		}
	}

	// Enemy speeds for block 2
	private void boardBlockTwoSpeed() {
		switch (this.enemyType) {
		case ("Alien"):
			this.movementSpeed = 1;
			break;
		}
	}

	// Enemy speeds for block 3
	private void boardBlockThreeSpeed() {
		switch (this.enemyType) {
		case ("Alien"):
			this.movementSpeed = 1;
			break;
		}
	}

	// Enemy speeds for block 4
	private void boardBlockFourSpeed() {
		switch (this.enemyType) {
		case ("Alien"):
			this.movementSpeed = 1;
			break;
		}
	}

	// Enemy speeds for block 5
	private void boardBlockFiveSpeed() {
		switch (this.enemyType) {
		case ("Alien"):
			this.movementSpeed = 1;
			break;
		}
	}

	// Enemy speeds for block 6
	private void boardBlockSixSpeed() {
		switch (this.enemyType) {
		case ("Alien"):
			this.movementSpeed = 2;
			break;
		}
	}

	// Enemy speeds for block 7
	private void boardBlockSevenSpeed() {
		switch (this.enemyType) {
		case ("Alien"):
			this.movementSpeed = 3;
			break;
		}
	}

	// Enemy speeds for block 8
	private void boardBlockEightSpeed() {
		switch (this.enemyType) {
		case ("Alien"):
			this.movementSpeed = 3;
			break;
		}
	}

	// Called when there is collision between friendly missile and enemy
	public void takeDamage(float damageTaken) {
		this.hitPoints -= damageTaken;
		if (this.hitPoints <= 0) {
			this.setVisible(false);
		}
	}

	// Called every game tick
	public void move() {
		if (xCoordinate < 0) {
			xCoordinate = DataClass.getInstance().getWindowWidth();
		}
		xCoordinate -= movementSpeed;
	}
	
	public float getCurrentHitpoints() {
		return this.hitPoints;
	}
	
	public float getMaxHitpoints() {
		return this.maxHitPoints;
	}

	// Called every game tick. If weapon is not on cooldown, fire a shot.
	// Current board block attack is set to 7, this shouldnt be a hardcoded value
	// This function doesn't discern enemy types yet either, should be re-written when new enemies are introduced
	public void fireAction() {
		if (currentBoardBlock < 7) {
			if (currentAttackSpeedFrameCount == attackSpeedFrameCount) {
				MissileManager missileManager = MissileManager.getInstance();

				missileManager.addEnemyMissile(this.xCoordinate, this.yCoordinate + calculateRandomWeaponHeightOffset(),
						"AlienDefault");
				currentAttackSpeedFrameCount = 0;
			}
			if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
				this.currentAttackSpeedFrameCount++;
			}
		}

	}

}
