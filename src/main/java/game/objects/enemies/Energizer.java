package game.objects.enemies;

import java.util.List;

import data.DataClass;
import data.movement.Trajectory;
import game.managers.MissileManager;

public class Energizer extends Enemy {

	public Energizer(int x, int y, String direction, int angleModuloDivider, float scale) {
		super(x, y, direction, "Energizer", scale);
		loadImage("Energizer");
		setExhaustanimation("Energizer Large Exhaust");
		setDeathAnimation("Energizer Destroyed Explosion");
		this.exhaustAnimation.setFrameDelay(3);
		this.deathAnimation.setFrameDelay(2);
		this.initBoardBlockSpeeds();
		this.angleModuloDivider = angleModuloDivider;
		this.hitPoints = 50;
		this.maxHitPoints = 50;
		this.attackSpeedFrameCount = 100;
		this.movementSpeed = 2;
		this.hasAttack = true;
		this.showHealthBar = true;
		this.deathSound = "Large Ship Destroyed";
		this.trajectory = new Trajectory(direction, totalDistance(), movementSpeed, angleModuloDivider, true);
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
					"Energizer Projectile", "Energizer Projectile Explosion", 0, "Left", "Left", this.scale);
			currentAttackSpeedFrameCount = 0;
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}
	}

}
