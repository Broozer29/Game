package game.objects.enemies.enemytypes;

import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.missiles.*;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Tazer extends Enemy {

    public Tazer (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
        super(spriteConfiguration, enemyConfiguration);

        SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
        exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Tazer_Normal_Exhaust);
        this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Tazer_Destroyed_Explosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
    }


    public void fireAction () {
        if (missileManager == null) {
            missileManager = MissileManager.getInstance();
        }
        if (attackSpeedCurrentFrameCount >= attackSpeed) {
            MissileTypeEnums missileType = MissileTypeEnums.TazerProjectile;

            SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
            missileSpriteConfiguration.setxCoordinate(xCoordinate);
            missileSpriteConfiguration.setyCoordinate(yCoordinate + this.height / 2);
            missileSpriteConfiguration.setScale(this.scale);
            missileSpriteConfiguration.setImageType(missileType.getImageType());

            MissileConfiguration missileConfiguration = new MissileConfiguration(missileType,
                    100, 100, null, missileType.getDeathOrExplosionImageEnum(), isFriendly()
                    , new RegularPathFinder(), this.movementDirection, missileType.getxMovementSpeed(), missileType.getyMovementspeed(), true
                    , missileType.getObjectType(), missileType.getDamage(), MovementPatternSize.SMALL, missileType.isBoxCollision());


            Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration);
            newMissile.setOwnerOrCreator(this);
            newMissile.rotateGameObject(movementDirection);
            missileManager.addExistingMissile(newMissile);
            attackSpeedCurrentFrameCount = 0;
        }
        if (attackSpeedCurrentFrameCount < attackSpeed) {
            this.attackSpeedCurrentFrameCount++;
        }
    }
}