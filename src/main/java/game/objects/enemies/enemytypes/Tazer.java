package game.objects.enemies.enemytypes;

import VisualAndAudioData.audio.enums.AudioEnums;
import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.pathfinderconfigs.MovementPatternSize;
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

public class Tazer extends Enemy {

    public Tazer (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

//        SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
//        exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Tazer_Normal_Exhaust);
//        this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Tazer_Destroyed_Explosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

        attackSpeed = 20;
    }


    public void fireAction () {
        // Check if the attack cooldown has been reached
        if (attackSpeedCurrentFrameCount >= attackSpeed) {
            // Check if the charging animation is not already playing
            if (WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
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
        //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate,
                yCoordinate, ImageEnums.Tazer_Missile, this.scale);


        //Create missile movement attributes and create a movement configuration
        MissileTypeEnums missileType = MissileTypeEnums.TazerProjectile;
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
        String objectType = "Tazer Missile";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, missileType.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(this);
        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.getAnimation().setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());

        this.missileManager.addExistingMissile(missile);
    }
}