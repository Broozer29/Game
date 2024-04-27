package game.objects.enemies.enemytypes;

import VisualAndAudioData.audio.enums.AudioEnums;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.HoverPathFinder;
import game.movement.pathfinders.PathFinder;
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

	public Energizer(SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, enemyConfiguration, movementConfiguration);
//		SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
//		exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Energizer_Normal_Exhaust);
//		this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

		SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, false);
		destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Energizer_Destroyed_Explosion);
		this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

		this.damage = MissileTypeEnums.BarrierProjectile.getDamage();
//		allowedToFire = true;
//		attackSpeed = 20;
	}

	@Override
	public void fireAction () {

		if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder){
			allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
		}

		double currentTime = GameStateInfo.getInstance().getGameSeconds();
		if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
		&& allowedToFire) {
			if (!chargingUpAttackAnimation.isPlaying()) {
				chargingUpAttackAnimation.refreshAnimation();
				AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
			}

			if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
				shootMissile();
				lastAttackTime = currentTime; // Update the last attack time after firing
			}
		}
	}

	private void shootMissile () {
		//Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
		SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,ImageEnums.BarrierProjectile
				,1);


		//Create missile movement attributes and create a movement configuration
		MissileTypeEnums missileType = MissileTypeEnums.BarrierProjectile;
		PathFinder missilePathFinder = new RegularPathFinder();
		MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
		MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
				missileType.getxMovementSpeed(), missileType.getyMovementSpeed(), missilePathFinder, movementPatternSize, this.movementRotation
		);


		//Create remaining missile attributes and a missile configuration
		boolean isFriendly = false;
		int maxHitPoints = 100;
		int maxShields = 100;
		AudioEnums deathSound = null;
		boolean allowedToDealDamage = true;
		String objectType = "Barrier Projectile";

		MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
				deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType, false);


		//Create the missile and finalize the creation process, then add it to the manager and consequently the game
		Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
		missile.getDestructionAnimation().setFrameDelay(1);
		missile.getDestructionAnimation().setAnimationScale(0.3f);
		missile.setOwnerOrCreator(this);
		missile.getAnimation().setAnimationScale(0.3f);
		missile.setAllowedVisualsToRotate(false);
		missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
		missile.resetMovementPath();


		this.missileManager.addExistingMissile(missile);
	}


}