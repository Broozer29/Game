package game.objects.enemies;

import game.managers.MissileManager;

public class Alien extends Enemy {

	public Alien(int x, int y, String direction, int angleModuloDivider) {
		super(x, y, direction, "Alien");
		loadImage("Default Alien Spaceship");
		this.initBoardBlockSpeeds();
		this.angleModuloDivider=angleModuloDivider;
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

	private void initBoardBlockSpeeds() {
		this.boardBlockSpeeds.add(0, 1);
		this.boardBlockSpeeds.add(1, 1);
		this.boardBlockSpeeds.add(2, 1);
		this.boardBlockSpeeds.add(3, 2);
		this.boardBlockSpeeds.add(4, 2);
		this.boardBlockSpeeds.add(5, 2);
		this.boardBlockSpeeds.add(6, 3);
		this.boardBlockSpeeds.add(7, 3);
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
						"Alien Laserbeam", 0, "Left");
				currentAttackSpeedFrameCount = 0;
			}
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}

	}

}
