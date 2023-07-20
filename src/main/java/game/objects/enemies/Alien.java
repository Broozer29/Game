package game.objects.enemies;

import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileManager;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;


//Deprecated enemy, trying to use this one will probably cause crashes lol
public class Alien extends Enemy {

	private PathFinder missilePathFinder;

	public Alien(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder) {
		super(x, y, destination, rotation, EnemyEnums.Alien, scale, pathFinder);
		loadImage(ImageEnums.Alien);
		this.initBoardBlockSpeeds();
		this.hitPoints = 35;
		this.maxHitPoints = 35;
		this.attackSpeedFrameCount = 150;
		this.XMovementSpeed = 1;
		this.YMovementSpeed = 1;
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
				missileManager.addExistingMissile(MissileCreator.getInstance().createEnemyMissile(
						xCoordinate, yCoordinate + calculateRandomWeaponHeightOffset()
						, ImageEnums.Alien_Laserbeam, ImageEnums.Impact_Explosion_One, rotation, 
						scale, missilePathFinder, XMovementSpeed, XMovementSpeed, (float) 7.5));
				currentAttackSpeedFrameCount = 0;
			}
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}

	}

}