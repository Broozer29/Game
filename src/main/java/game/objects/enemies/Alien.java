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

	public Alien(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed) {
		super(x, y, destination, rotation, EnemyEnums.Alien, scale, pathFinder, xMovementSpeed,yMovementSpeed);
		loadImage(ImageEnums.Alien);
		this.hitPoints = 35;
		this.maxHitPoints = 35;
		this.attackSpeedFrameCount = 150;
		this.hasAttack = true;
		this.deathSound = AudioEnums.Alien_Spaceship_Destroyed;
		this.showHealthBar = true;
		this.setVisible(true);
		this.setRotation(rotation);
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
						, ImageEnums.Alien_Laserbeam, ImageEnums.Impact_Explosion_One, moveConfig.getRotation(), 
						scale, missilePathFinder, 3, 3, (float) 7.5));
				currentAttackSpeedFrameCount = 0;
			}
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}

	}

}