package game.objects.enemies.enemytypes;

import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.missiles.*;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;
import visual.objects.SpriteAnimation;

public class Seeker extends Enemy {

	private PathFinder missilePathFinder;

	public Seeker(SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
		super(spriteConfiguration, enemyConfiguration);

		SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
		exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Seeker_Normal_Exhaust);
		this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

		SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
		destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Seeker_Destroyed_Explosion);
		this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
	}


	@Override
	public void fireAction() {
		if (missileManager == null) {
			missileManager = MissileManager.getInstance();
		}
		int xMovementSpeed = 3;
		int yMovementSpeed = 3;


		if (attackSpeedCurrentFrameCount >= attackSpeed) {
			SpriteConfiguration missileSpriteConfiguration = this.spriteConfiguration;
			missileSpriteConfiguration.setyCoordinate(yCoordinate + this.height / 2);
			missileSpriteConfiguration.setImageType(ImageEnums.Seeker_Missile);

			MissileConfiguration missileConfiguration = new MissileConfiguration(MissileTypeEnums.SeekerProjectile,
					100, 100, null, ImageEnums.Seeker_Missile_Explosion, isFriendly()
					, new RegularPathFinder(), movementDirection, xMovementSpeed,yMovementSpeed, true
					, "Bomba Missile", (float) 7.5);


			Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration);

			newMissile.rotateGameObject(movementDirection);
			missileManager.addExistingMissile(newMissile);
			attackSpeedCurrentFrameCount = 0;
		}
		if (attackSpeedCurrentFrameCount < attackSpeed) {
			this.attackSpeedCurrentFrameCount++;
		}
	}

}