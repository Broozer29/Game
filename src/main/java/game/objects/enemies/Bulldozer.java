package game.objects.enemies;

import data.audio.AudioEnums;
import data.image.ImageEnums;
import game.managers.MissileManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.movement.RegularPathFinder;

public class Bulldozer extends Enemy {
	private PathFinder missilePathFinder;

	public Bulldozer(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder) {
		super(x, y, destination, rotation, EnemyEnums.Bulldozer, scale, pathFinder);
		loadImage(ImageEnums.Bulldozer);
		setExhaustanimation(ImageEnums.Bulldozer_Normal_Exhaust);
		setDeathAnimation(ImageEnums.Bulldozer_Destroyed_Explosion);
		this.exhaustAnimation.setFrameDelay(3);
		this.deathAnimation.setFrameDelay(4);
		this.initBoardBlockSpeeds();
		this.hitPoints = 50;
		this.maxHitPoints = 50;
		this.attackSpeedFrameCount = 250;
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
			missileManager.addEnemyMissile(this.xCoordinate, this.yCoordinate, ImageEnums.Bulldozer_Missile,
					ImageEnums.Bulldozer_Missile_Explosion, Direction.LEFT_UP, this.scale, missilePathFinder,
					xMovementSpeed, yMovementSpeed);
			missileManager.addEnemyMissile(this.xCoordinate, this.yCoordinate, ImageEnums.Bulldozer_Missile,
					ImageEnums.Bulldozer_Missile_Explosion, Direction.LEFT_DOWN, this.scale, missilePathFinder,
					xMovementSpeed, yMovementSpeed);
			missileManager.addEnemyMissile(this.xCoordinate, this.yCoordinate, ImageEnums.Bulldozer_Missile,
					ImageEnums.Bulldozer_Missile_Explosion, Direction.LEFT, this.scale, missilePathFinder,
					xMovementSpeed, yMovementSpeed);
			currentAttackSpeedFrameCount = 0;
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}
	}

}