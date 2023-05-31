package game.objects.enemies;

import data.movement.Direction;
import data.movement.PathFinder;
import data.movement.Point;
import game.managers.MissileManager;

public class Tazer extends Enemy {

	public Tazer(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder) {
		super(x, y, destination, rotation, "Tazer", scale, pathFinder);
		loadImage("Tazer");
		setExhaustanimation("Tazer Large Exhaust");
		setDeathAnimation("Tazer Destroyed Explosion");
		this.exhaustAnimation.setFrameDelay(3);
		this.deathAnimation.setFrameDelay(2);
		this.initBoardBlockSpeeds();
		this.hitPoints = 50;
		this.maxHitPoints = 50;
		this.attackSpeedFrameCount = 100;
		this.movementSpeed = 2;
		this.hasAttack = true;
		this.showHealthBar = true;
		this.deathSound = "Large Ship Destroyed";
		this.setVisible(true);
		this.setRotation(rotation);
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

	// Called every game tick. If weapon is not on cooldown, fire a shot.
	// Current board block attack is set to 7, this shouldnt be a hardcoded value
	// This function doesn't discern enemy types yet either, should be re-written
	// when new enemies are introduced
	public void fireAction() {
		if (missileManager == null) {
			missileManager = MissileManager.getInstance();
		}

		if (currentAttackSpeedFrameCount >= attackSpeedFrameCount) {
			missileManager.addEnemyMissile(this.xCoordinate, this.yCoordinate + calculateRandomWeaponHeightOffset(),
					"Tazer Projectile", "Tazer Projectile Explosion", rotation, this.scale);
			currentAttackSpeedFrameCount = 0;
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}
	}
}
