package game.objects.enemies.enemytypes;

import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.missiles.*;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;
import visual.objects.SpriteAnimation;

public class Energizer extends Enemy {

	public Energizer(SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
		super(spriteConfiguration, enemyConfiguration);
		SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
		exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Energizer_Normal_Exhaust);
		this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

		SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
		destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Energizer_Destroyed_Explosion);
		this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
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
		if (attackSpeedCurrentFrameCount >= attackSpeed) {
			SpriteConfiguration missileSpriteConfiguration = this.spriteConfiguration;
			missileSpriteConfiguration.setyCoordinate(yCoordinate + this.height / 2);
			missileSpriteConfiguration.setImageType(ImageEnums.Energizer_Missile);

			MissileConfiguration missileConfiguration = new MissileConfiguration(MissileTypeEnums.EnergizerProjectile,
					100, 100, null, ImageEnums.Energizer_Missile_Explosion, isFriendly()
					, new RegularPathFinder(), movementDirection, xMovementSpeed,yMovementSpeed, true
					, "Bomba Missile", (float) 7.5);


			Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration);


			newMissile.rotateGameObject(movementConfiguration.getRotation());
			missileManager.addExistingMissile(newMissile);
			attackSpeedCurrentFrameCount = 0;
		}
		if (attackSpeedCurrentFrameCount < attackSpeed) {
			this.attackSpeedCurrentFrameCount++;
		}
	}

}