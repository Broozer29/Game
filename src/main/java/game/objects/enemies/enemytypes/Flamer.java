package game.objects.enemies.enemytypes;

import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.missiles.*;
import VisualAndAudioData.image.ImageEnums;
import game.objects.player.specialAttacks.SpecialAttack;
import game.objects.player.specialAttacks.SpecialAttackConfiguration;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Flamer extends Enemy {

    public Flamer (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

//        SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
//        exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Flamer_Normal_Exhaust);
//        this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Flamer_Destroyed_Explosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

//        this.attackSpeed = 150;
    }

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
//        MissileTypeEnums missileType = MissileTypeEnums.FlamerProjectile;
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(-85);
        missileSpriteConfiguration.setyCoordinate(-85);
        missileSpriteConfiguration.setScale(1);
        missileSpriteConfiguration.setImageType(ImageEnums.Player_EMP);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(missileSpriteConfiguration, 2, false);
//        SpriteAnimation anim = new SpriteAnimation(spriteAnimationConfiguration);
//        anim.setX(-85);
//        anim.setY(-85);
//        AnimationManager.getInstance().addUpperAnimation(anim);


        SpecialAttackConfiguration specialAttackConfiguration = new SpecialAttackConfiguration(1, false, true, false, false);
        SpecialAttack specialAttack = new SpecialAttack(spriteAnimationConfiguration, specialAttackConfiguration);
        specialAttack.setOwnerOrCreator(this);
        specialAttack.setObjectType("Flamer Special Attack");


        specialAttack.setObjectToCenterAround(this);
        specialAttack.setCenteredAroundObject(true);

        specialAttack.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        specialAttack.getAnimation().setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        this.objectsFollowingThis.add(specialAttack);

        MissileManager.getInstance().addSpecialAttack(specialAttack);
    }

}