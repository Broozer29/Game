package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.spaceships;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileCreator;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class Energizer extends Enemy {

	public Energizer(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, enemyConfiguration, movementConfiguration);
		SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
		destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
		this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

		this.damage = 15;
		this.attackSpeed = 7f;
//		allowedToFire = true;
		this.detonateOnCollision = false;
		this.knockbackStrength = 8;
	}

	@Override
	public void fireAction () {

		if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder){
			allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
		}

		double currentTime = GameState.getInstance().getGameSeconds();
		if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
		&& allowedToFire) {
			updateChargingAttackAnimationCoordination();
			if (!chargingUpAttackAnimation.isPlaying()) {
				this.isAttacking = true;
				chargingUpAttackAnimation.refreshAnimation();
				AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
			}

			if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
				shootMissile();
				this.isAttacking = false;
				lastAttackTime = currentTime; // Update the last attack time after firing
			}
		}
	}

	private void shootMissile () {
		//Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
		SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,ImageEnums.BarrierProjectile
				,1);

		int movementSpeed = 3;

		//Create missile movement attributes and create a movement configuration
		MissileEnums missileType = MissileEnums.BarrierProjectile;
		PathFinder missilePathFinder = new RegularPathFinder();
		MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
		MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
				movementSpeed, movementSpeed, missilePathFinder, movementPatternSize, this.movementRotation
		);


		//Create remaining missile attributes and a missile configuration
		boolean isFriendly = false;
		int maxHitPoints = 100;
		int maxShields = 100;
		AudioEnums deathSound = null;
		boolean allowedToDealDamage = true;
		String objectType = "Barrier Projectile";

		MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
				deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType, false,
				false, true, false);


		//Create the missile and finalize the creation process, then add it to the manager and consequently the game
		Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
		missile.getDestructionAnimation().setFrameDelay(1);
		missile.getDestructionAnimation().setAnimationScale(0.3f);
		missile.setOwnerOrCreator(this);
		missile.getAnimation().setAnimationScale(0.3f);
		missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
		missile.rotateObjectTowardsDestination(true);
		missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
		missile.setAllowedVisualsToRotate(false);
		missile.resetMovementPath();
		this.missileManager.addExistingMissile(missile);
	}


}