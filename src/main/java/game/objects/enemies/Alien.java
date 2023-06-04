package game.objects.enemies;


import data.audio.AudioEnums;
import data.image.enums.EnemyEnums;
import data.image.enums.ImageEnums;
import game.managers.MissileManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;

public class Alien extends Enemy {

	private PathFinder missilePathFinder;
	public Alien(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder) {
		super(x, y, destination, rotation, EnemyEnums.Alien, scale, pathFinder);
		loadImage(ImageEnums.Alien);
		this.initBoardBlockSpeeds();
		this.hitPoints = 35;
		this.maxHitPoints = 35;
		this.attackSpeedFrameCount = 150;
		this.movementSpeed = 1;
		this.hasAttack = true;
		this.deathSound = AudioEnums.Alien_Spaceship_Destroyed;
		this.showHealthBar = true;
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
			if (currentBoardBlock < 7) {
				missileManager.addEnemyMissile(this.xCoordinate, this.yCoordinate + calculateRandomWeaponHeightOffset(),
						ImageEnums.Alien_Laserbeam, ImageEnums.Impact_Explosion_One, Direction.LEFT, this.scale, missilePathFinder);
				currentAttackSpeedFrameCount = 0;
			}
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}

	}

}
