package game.objects.enemies.enemytypes;

import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.missiles.*;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Flamer extends Enemy {

    public Flamer (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
        super(spriteConfiguration, enemyConfiguration);

        //The correct imageenum can be gotten from a method in enemyenums like the BGO enum method
        //Below is sloppy and temporary
        SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
        exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Flamer_Normal_Exhaust);
        this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Flamer_Destroyed_Explosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
    }

    // Called every game tick. If weapon is not on cooldown, fire a shot.
    // Current board block attack is set to 7, this shouldnt be a hardcoded value
    // This function doesn't discern enemy types yet either, should be re-written
    // when new enemies are introduced
    public void fireAction () {
        if (missileManager == null) {
            missileManager = MissileManager.getInstance();
        }
        int xMovementSpeed = 4;
        int yMovementSpeed = 2;
        if (attackSpeedCurrentFrameCount >= attackSpeed) {

            MissileTypeEnums missileType = MissileTypeEnums.FlamerProjectile;

            SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
            missileSpriteConfiguration.setxCoordinate(xCoordinate);
            missileSpriteConfiguration.setyCoordinate(yCoordinate + this.height / 2);
            missileSpriteConfiguration.setScale(this.scale);
            missileSpriteConfiguration.setImageType(missileType.getImageType());

            MissileConfiguration missileConfiguration = new MissileConfiguration(missileType,
                    100, 100, null, missileType.getDeathOrExplosionImageEnum(), isFriendly()
                    , new RegularPathFinder(), this.movementDirection, missileType.getxMovementSpeed(),missileType.getyMovementspeed(), true
                    , missileType.getObjectType(), missileType.getDamage(), MovementPatternSize.SMALL, missileType.isBoxCollision());


            Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration);
            newMissile.setOwnerOrCreator(this);
            newMissile.rotateGameObjectTowards(movementDirection);
            missileManager.addExistingMissile(newMissile);
            attackSpeedCurrentFrameCount = 0;
        }
        if (attackSpeedCurrentFrameCount < attackSpeed) {
            this.attackSpeedCurrentFrameCount++;
        }
    }

}