package game.objects.enemies.enemytypes;

import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyEnums;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileManager;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;

public class Energizer extends Enemy {
	private PathFinder missilePathFinder;

	public Energizer(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder,
			int xMovementSpeed, int yMovementSpeed) {
		super(x, y, destination, rotation, EnemyEnums.Energizer, scale, pathFinder, xMovementSpeed, yMovementSpeed);
		loadImage(ImageEnums.Energizer);
		setExhaustanimation(ImageEnums.Energizer_Normal_Exhaust);
		setDeathAnimation(ImageEnums.Energizer_Destroyed_Explosion);
		this.exhaustAnimation.setFrameDelay(1);
		this.hitPoints = 50;
		this.maxHitPoints = 50;
		this.attackSpeedFrameCount = 300;
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
					yCoordinate + this.height / 2, ImageEnums.Energizer_Missile,
					ImageEnums.Energizer_Missile_Explosion, moveConfig.getRotation(), scale, missilePathFinder, xMovementSpeed,
					yMovementSpeed, (float) 7.5);
			
			newMissile.rotateMissileAnimation(moveConfig.getRotation());
			missileManager.addExistingMissile(newMissile);
			currentAttackSpeedFrameCount = 0;
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}
	}

}