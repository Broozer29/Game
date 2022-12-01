package game.objects.enemies;

import java.util.List;

import game.managers.MissileManager;

public class Alien extends Enemy {

	public Alien(int x, int y, String direction) {
		super(x, y, direction);
		loadImage("Default Alien Spaceship");
		this.hitPoints = 35;
		this.maxHitPoints = 35;
		this.attackSpeedFrameCount = 150;
		this.movementSpeed = 1;
		this.hasAttack = true;
		this.deathSound = "Alien Spaceship Destroyed";
		this.showHealthBar = true;
		this.trajectory.setEnemyTrajectoryType(this);
		this.setVisible(true);
		this.setRotation(direction);
	}

	// Called every loop to move the enemy
	public void move() {
		List<Integer> newCoordsList = trajectory.getPathCoordinates(xCoordinate, yCoordinate);
		xCoordinate = newCoordsList.get(0);
		yCoordinate = newCoordsList.get(1);

		if (xCoordinate < 0) {
			this.setVisible(false);
		}

	}
	

	public void updateBoardBlock(int boardBlockNumber) {
		if (boardBlockNumber != this.currentBoardBlock) {
			this.currentBoardBlock = boardBlockNumber;
			this.updateMovementSpeed();
			this.trajectory.updateMovementSpeed(movementSpeed);
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

	// Call the corresponding boardBlockSpeed modification depending on the current
	// boardBlock

	private void updateMovementSpeed() {
		switch (currentBoardBlock) {
		case (0):
			this.movementSpeed = 1;
			break;
		case (1):
			this.movementSpeed = 1;
			break;
		case (2):
			this.movementSpeed = 1;
			break;
		case (3):
			this.movementSpeed = 1;
			break;
		case (4):
			this.movementSpeed = 1;
			break;
		case (5):
			this.movementSpeed = 1;
			break;
		case (6):
			this.movementSpeed = 2;
			break;
		case (7):
			this.movementSpeed = 3;
			break;
		case (8):
			this.movementSpeed = 3;
			break;
		}
	}

}
