package game.objects.enemies.enemytypes;

import game.managers.AnimationManager;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.missiles.*;
import VisualAndAudioData.image.ImageEnums;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Energizer extends Enemy {

	public Energizer(SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
		super(spriteConfiguration, enemyConfiguration);
//		SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
//		exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Energizer_Normal_Exhaust);
//		this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

		SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
		destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Energizer_Destroyed_Explosion);
		this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
	}

	@Override
	public void fireAction () {
		// Check if the attack cooldown has been reached
		if (attackSpeedCurrentFrameCount >= attackSpeed) {
			// Check if the charging animation is not already playing
			if(WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
				if (!chargingUpAttackAnimation.isPlaying()) {
					// Start charging animation
					chargingUpAttackAnimation.refreshAnimation(); // Refreshes the animation
					AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation); // Adds the animation for displaying
				}

				// Check if the charging animation has finished
				if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
					shootMissile();
					// Reset attack speed frame count after firing the missile
					attackSpeedCurrentFrameCount = 0;
				}
			}



		} else {
			// If not yet ready to attack, increase the attack speed frame count
			attackSpeedCurrentFrameCount++;
		}
	}

	private void shootMissile () {


		MissileTypeEnums missileType = MissileTypeEnums.EnergizerProjectile;

		SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
		missileSpriteConfiguration.setxCoordinate(xCoordinate);
		missileSpriteConfiguration.setyCoordinate(yCoordinate + this.height / 2);
		missileSpriteConfiguration.setScale(this.scale);
		missileSpriteConfiguration.setImageType(missileType.getImageType());

		MissileConfiguration missileConfiguration = new MissileConfiguration(missileType,
				100, 100, null, missileType.getDeathOrExplosionImageEnum(), isFriendly()
				, new RegularPathFinder(), this.movementDirection, missileType.getxMovementSpeed(),missileType.getyMovementSpeed(), true
				, missileType.getObjectType(), missileType.getDamage(), MovementPatternSize.SMALL, missileType.isBoxCollision());



		Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration);
		newMissile.setOwnerOrCreator(this);

		newMissile.rotateGameObjectTowards(newMissile.getMovementConfiguration().getDestination().getX(), newMissile.getMovementConfiguration().getDestination().getY());
		newMissile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
		newMissile.getAnimation().setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());


//		newMissile.rotateGameObjectTowards(movementConfiguration.getRotation());
		MissileManager.getInstance().addExistingMissile(newMissile);

	}


}