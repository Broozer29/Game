package game.objects.enemies;

import data.audio.AudioEnums;
import data.image.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.movement.RegularPathFinder;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileManager;

public class Flamer extends Enemy {
	private PathFinder missilePathFinder;

	public Flamer(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder) {
		super(x, y, destination, rotation, EnemyEnums.Flamer, scale, pathFinder);
		loadImage(ImageEnums.Flamer);
		setExhaustanimation(ImageEnums.Flamer_Normal_Exhaust);
		setDeathAnimation(ImageEnums.Flamer_Destroyed_Explosion);
		this.exhaustAnimation.setFrameDelay(3);
		this.deathAnimation.setFrameDelay(4);
		this.initBoardBlockSpeeds();
		this.hitPoints = 50;
		this.maxHitPoints = 50;
		this.attackSpeedFrameCount = 200;
		this.XMovementSpeed = 2;
		this.YMovementSpeed = 1;
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
		int xMovementSpeed = 5;
		int yMovementSpeed = 2;
		if (currentAttackSpeedFrameCount >= attackSpeedFrameCount) {
			missileManager.addExistingMissile(MissileCreator.getInstance().createEnemyMissile(
					xCoordinate, yCoordinate + + this.height / 2
					, ImageEnums.Flamer_Missile, ImageEnums.Flamer_Missile_Explosion, rotation, 
					scale, missilePathFinder, xMovementSpeed, yMovementSpeed, (float) 7.5));
			currentAttackSpeedFrameCount = 0;
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}
	}

}