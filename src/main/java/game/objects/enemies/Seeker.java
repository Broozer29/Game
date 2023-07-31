package game.objects.enemies;

import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.movement.RegularPathFinder;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileManager;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;

public class Seeker extends Enemy {

	private PathFinder missilePathFinder;

	public Seeker(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed) {
		super(x, y, destination, rotation, EnemyEnums.Seeker, scale, pathFinder, xMovementSpeed, yMovementSpeed);
		loadImage(ImageEnums.Seeker);
		setExhaustanimation(ImageEnums.Seeker_Normal_Exhaust);
		setDeathAnimation(ImageEnums.Seeker_Destroyed_Explosion);
		this.exhaustAnimation.setFrameDelay(3);
		this.deathAnimation.setFrameDelay(4);
		this.hitPoints = 50;
		this.maxHitPoints = 50;
		this.attackSpeedFrameCount = 200;
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
		int xMovementSpeed = 3;
		int yMovementSpeed = 5;
		
		// Hier een missile maken, en na het maken een target toeveogen aan de missile. De missile kan dan zijn target geven aan path. 
		//PAth kan vervolgens zijn target tracken en constant de nextStep() naar de target teruggeven.
		if (currentAttackSpeedFrameCount >= attackSpeedFrameCount) {
			missileManager.addExistingMissile(MissileCreator.getInstance().createEnemyMissile(
					xCoordinate, yCoordinate + + this.height / 2
					, ImageEnums.Seeker_Missile, ImageEnums.Seeker_Missile_Explosion, rotation, 
					scale, missilePathFinder, xMovementSpeed, yMovementSpeed, (float) 7.5));
			currentAttackSpeedFrameCount = 0;
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}
	}

}