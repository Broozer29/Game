package game.objects.enemies.enemytypes;

import game.movement.Point;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.StraightLinePathFinder;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.missiles.*;
import VisualAndAudioData.image.ImageEnums;
import game.objects.player.PlayerManager;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Seeker extends Enemy {


	public Seeker(SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
		super(spriteConfiguration, enemyConfiguration);

		SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
		exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Seeker_Normal_Exhaust);
		this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

		SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
		destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Seeker_Destroyed_Explosion);
		this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

//		this.attackSpeed = 20;
		this.attackSpeed= 9999999;
		this.attackSpeedCurrentFrameCount= 9999999;
	}


	@Override
	public void fireAction() {
		if (missileManager == null) {

			missileManager = MissileManager.getInstance();
		}


		if (attackSpeedCurrentFrameCount >= attackSpeed) {
			MissileTypeEnums missileType = MissileTypeEnums.SeekerProjectile;

			SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
			missileSpriteConfiguration.setxCoordinate(xCoordinate);
			missileSpriteConfiguration.setyCoordinate(yCoordinate + this.height / 2);
			missileSpriteConfiguration.setScale(this.scale);
			missileSpriteConfiguration.setImageType(missileType.getImageType());

			MissileConfiguration missileConfiguration = new MissileConfiguration(missileType,
					100, 100, null, missileType.getDeathOrExplosionImageEnum(), isFriendly()
					, new HomingPathFinder(), this.movementDirection, missileType.getxMovementSpeed(),missileType.getyMovementspeed(), true
					, missileType.getObjectType(), missileType.getDamage(), MovementPatternSize.SMALL, missileType.isBoxCollision());


			Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration);

			System.out.println(newMissile.getCurrentLocation());
//			Point spaceShipCenter = new Point(PlayerManager.getInstance().getSpaceship().getCenterXCoordinate(),
//					PlayerManager.getInstance().getSpaceship().getCenterYCoordinate());
//
//			newMissile.getMovementConfiguration().setDestination(spaceShipCenter);
			newMissile.setOwnerOrCreator(this);
			missileManager.addExistingMissile(newMissile);
			attackSpeedCurrentFrameCount = 0;
		}
		if (attackSpeedCurrentFrameCount < attackSpeed) {
			this.attackSpeedCurrentFrameCount++;
		}
	}

}