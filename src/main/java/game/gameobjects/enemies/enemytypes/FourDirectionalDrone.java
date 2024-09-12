package game.gameobjects.enemies.enemytypes;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyConfiguration;
import game.gameobjects.missiles.*;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.MovementPatternSize;
import game.movement.Point;
import game.movement.pathfinders.DestinationPathFinder;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.StraightLinePathFinder;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourDirectionalDrone extends Enemy {

    private List<SpriteAnimation> chargingUpAnimations = new ArrayList<>();
    private Map<SpriteAnimation, Double> animationAngles = new HashMap<>(); // Map to track angles of charging animations

    public FourDirectionalDrone (SpriteConfiguration spriteConfig, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfig, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfig, 2, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Explosion2);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 10;
        this.allowedToMove = true;
        this.allowedVisualsToRotate = false;
        this.attackSpeed = 0.25f;
        initChargingUpAnimations();
    }


    private void initChargingUpAnimations () {
        double[] initialAngles = {0, 90, 180, 270}; // RIGHT, DOWN, LEFT, UP angles in degrees

        for (double angle : initialAngles) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setScale(1);

            Point position = calculateBulletDestination(angle, this.getWidth() / 2, this.getCenterXCoordinate(), this.getCenterYCoordinate());
            spriteConfiguration.setxCoordinate(position.getX());
            spriteConfiguration.setyCoordinate(position.getY());
            spriteConfiguration.setImageType(ImageEnums.Charging);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
            SpriteAnimation animation = new SpriteAnimation(spriteAnimationConfiguration);

            animation.setCenterCoordinates(position.getX(), position.getY());
            chargingUpAnimations.add(animation);
            animationAngles.put(animation, angle); // Store the angle associated with this animation
        }
    }


    @Override
    public void fireAction () {
        if (this.movementConfiguration.getPathFinder() instanceof DestinationPathFinder) {
            allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
        }


        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (allowedToFire && currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            allowedVisualsToRotate = true;

            for (SpriteAnimation chargingUpAnimation : this.chargingUpAnimations) {
                if (!chargingUpAnimation.isPlaying()) {
                    this.isAttacking = true;
                    chargingUpAnimation.refreshAnimation();
                    AnimationManager.getInstance().addUpperAnimation(chargingUpAnimation);
                }

                if (chargingUpAnimation.getCurrentFrame() >= chargingUpAnimation.getTotalFrames() - 1) {
                    shootMissiles(chargingUpAnimation); // Actually fire the missiles
                    this.isAttacking = false;
                    super.rotateObjectTowardsAngle(increaseRotationAngle(this.rotationAngle), true); // Rotate the object
                    lastAttackTime = currentTime; // Update the last attack time after firing
                }
            }

            adjustChargingAnimationForRotation(); // Rotate the charging animations
        }
    }


    private void shootMissiles (SpriteAnimation chargingUpAnimation) {
        // The charging up attack animation has finished, create and fire the missile
        //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,
                ImageEnums.LaserBullet, 0.5f);


        int movementSpeed = 3;
        //Create missile movement attributes and create a movement configuration
        MissileEnums missileType = MissileEnums.ScoutLaserBullet;
        PathFinder missilePathFinder = new StraightLinePathFinder();
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
        String objectType = "Rotating Drone Missile";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false, true, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);


        //Calculate the angle based on the current chargingAnimation. Because we want to fire from 4 directions, we also need to keep
        //track of the angle that the given chargingAnimation has in this method
        double angleDegrees = animationAngles.get(chargingUpAnimation);
        Point bulletDestination = calculateBulletDestination(angleDegrees, 400, chargingUpAnimation.getCenterXCoordinate(), chargingUpAnimation.getCenterYCoordinate());

        missile.resetMovementPath();

        missile.setCenterCoordinates(chargingUpAnimation.getCenterXCoordinate(), chargingUpAnimation.getCenterYCoordinate());
        missile.getMovementConfiguration().setDestination(bulletDestination); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(chargingUpAnimation.getCenterXCoordinate(), chargingUpAnimation.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(this);

        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private Point calculateBulletDestination (double angleDegrees, int distance, int centerX, int centerY) {
        // Convert the angle from degrees to radians because Math functions use radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the X and Y coordinates
        int targetX = centerX + (int) (Math.cos(angleRadians) * distance);
        int targetY = centerY + (int) (Math.sin(angleRadians) * distance);

        // Return the calculated coordinates as a Point object
        return new Point(targetX, targetY);
    }

    private double increaseRotationAngle (double rotationAngle) {
        double newAngle = rotationAngle + 1;
        if (newAngle > 360) {
            newAngle = 0;
        }

        return newAngle;
    }

    private void adjustChargingAnimationForRotation () {
        double baseAngle = this.rotationAngle; // Use the current rotation angle of the object

        double[] angles = {baseAngle, baseAngle + 90, baseAngle + 180, baseAngle + 270};

        for (int i = 0; i < chargingUpAnimations.size(); i++) {
            double adjustedAngle = angles[i] % 360; // Ensure the angle stays within 0-360 degrees

            Point newPosition = calculateBulletDestination(adjustedAngle, this.getWidth() / 2, this.getCenterXCoordinate(), this.getCenterYCoordinate());
            chargingUpAnimations.get(i).setCenterCoordinates(newPosition.getX(), newPosition.getY());

            animationAngles.put(chargingUpAnimations.get(i), adjustedAngle); // Update the angle in the map
        }
    }

}
