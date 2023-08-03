package game.objects.enemies;

import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.movement.RegularPathFinder;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileManager;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;

public class Tazer extends Enemy {

	private PathFinder missilePathFinder;
	public Tazer(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed) {
		super(x, y, destination, rotation, EnemyEnums.Tazer, scale, pathFinder, xMovementSpeed, yMovementSpeed);
		loadImage(ImageEnums.Tazer);
		setExhaustanimation(ImageEnums.Tazer_Normal_Exhaust);
		setDeathAnimation(ImageEnums.Tazer_Destroyed_Explosion);
		this.exhaustAnimation.setFrameDelay(1);
		this.hitPoints = 50;
		this.maxHitPoints = 50;
		this.attackSpeedFrameCount = 250;
		this.hasAttack = true;
		this.showHealthBar = true;
		this.deathSound = AudioEnums.Large_Ship_Destroyed;
		this.setVisible(true);
		this.setRotation(rotation);
		this.deathAnimation.rotateAnimetion(rotation);
		this.missilePathFinder = new RegularPathFinder();
	}


	// Called every game tick. If weapon is not on cooldown, fire a shot.
	// Current board block attack is set to 7, this shouldnt be a hardcoded value
	// This function doesn't discern enemy types yet either, should be re-written
	// when new enemies are introduced
	public void fireAction() {
		if (missileManager == null) {
			missileManager = MissileManager.getInstance();
		}
		int xMovementSpeed = 4;
		int yMovementSpeed = 2;
		if (currentAttackSpeedFrameCount >= attackSpeedFrameCount) {
			Missile newMissile = MissileCreator.getInstance().createEnemyMissile(xCoordinate,
					yCoordinate + +this.height / 2, ImageEnums.Tazer_Missile,
					ImageEnums.Tazer_Missile_Explosion, rotation, scale, missilePathFinder, xMovementSpeed,
					yMovementSpeed, (float) 7.5);
			
			newMissile.rotateMissileAnimation(rotation);
			missileManager.addExistingMissile(newMissile);
			currentAttackSpeedFrameCount = 0;
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}
	}
}