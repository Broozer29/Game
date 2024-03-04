package game.objects.enemies.enemytypes;

import game.managers.AnimationManager;
import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.BouncingPathFinder;
import game.movement.pathfinders.StraightLinePathFinder;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.missiles.*;
import VisualAndAudioData.image.ImageEnums;
import game.objects.player.PlayerManager;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Seeker extends Enemy {


    public Seeker (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
        super(spriteConfiguration, enemyConfiguration);

//        SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
//        exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Seeker_Normal_Exhaust);
//        this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Seeker_Destroyed_Explosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

//        this.attackSpeed = 20;
//		this.attackSpeed= 9999999;
//		this.attackSpeedCurrentFrameCount= 9999999;
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
        // The charging up attack animation has finished, create and fire the missile
        MissileTypeEnums missileType = MissileTypeEnums.SeekerProjectile;

        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(chargingUpAttackAnimation.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(chargingUpAttackAnimation.getCenterYCoordinate());
        missileSpriteConfiguration.setScale(this.scale);
        missileSpriteConfiguration.setImageType(missileType.getImageType());

        MissileConfiguration missileConfiguration = new MissileConfiguration(missileType,
                100, 100, null, missileType.getDeathOrExplosionImageEnum(), isFriendly(),
                new StraightLinePathFinder(), Direction.RIGHT, missileType.getxMovementSpeed(), missileType.getyMovementSpeed(), true,
                missileType.getObjectType(), missileType.getDamage(), MovementPatternSize.SMALL, missileType.isBoxCollision());

        Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration);

        Point spaceShipCenter = new Point(PlayerManager.getInstance().getSpaceship().getCenterXCoordinate(),
                PlayerManager.getInstance().getSpaceship().getCenterYCoordinate());
        newMissile.getMovementConfiguration().setDestination(spaceShipCenter);

        newMissile.rotateGameObjectTowards(newMissile.getMovementConfiguration().getDestination().getX(), newMissile.getMovementConfiguration().getDestination().getY());
        newMissile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        newMissile.getAnimation().setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());


        newMissile.setOwnerOrCreator(this);
        MissileManager.getInstance().addExistingMissile(newMissile);


    }


}