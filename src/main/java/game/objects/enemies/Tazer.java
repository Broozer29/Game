package game.objects.enemies;

import data.audio.AudioEnums;
import data.image.enums.EnemyEnums;
import data.image.enums.ImageEnums;
import game.managers.MissileManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.movement.RegularPathFinder;

public class Tazer extends Enemy {

	private PathFinder missilePathFinder;
	public Tazer(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder) {
		super(x, y, destination, rotation, EnemyEnums.Tazer, scale, pathFinder);
		loadImage(ImageEnums.Tazer);
		setExhaustanimation(ImageEnums.Tazer_Normal_Exhaust);
		setDeathAnimation(ImageEnums.Tazer_Destroyed_Explosion);
		this.exhaustAnimation.setFrameDelay(3);
		this.deathAnimation.setFrameDelay(2);
		this.initBoardBlockSpeeds();
		this.hitPoints = 50;
		this.maxHitPoints = 50;
		this.attackSpeedFrameCount = 100;
		this.movementSpeed = 2;
		this.hasAttack = true;
		this.showHealthBar = true;
		this.deathSound = AudioEnums.Large_Ship_Destroyed;
		this.setVisible(true);
		this.setRotation(rotation);
		this.missilePathFinder = new RegularPathFinder();
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
					ImageEnums.Tazer_Missile, ImageEnums.Tazer_Missile_Explosion, rotation, this.scale, missilePathFinder);
			currentAttackSpeedFrameCount = 0;
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}
	}
}
