package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class EnemyProtossPulsingDrone extends Enemy {
    private static final int ANGLE_INCREMENT = 10;

    public EnemyProtossPulsingDrone(SpriteAnimationConfiguration spriteConfig, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfig, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfig.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 8;
        this.allowedToMove = true;
        this.allowedVisualsToRotate = false;
        this.attackSpeed = 1.5f;
        this.detonateOnCollision = false;
        this.knockbackStrength = 9;
        this.allowedToFire = true;
        this.baseArmor = 999;
    }

    @Override
    public void fireAction() {
        this.allowedToMove = false; //Manually overwrite this cause I don't want these to move

        if(this.ownerOrCreator != null && this.ownerOrCreator.getCurrentHitpoints() <= 0){
            this.takeDamage(99999);
        }


        double currentTime = GameState.getInstance().getGameSeconds();
        if (allowedToFire && currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            for (int angle = 0; angle < (360 - ANGLE_INCREMENT); angle += ANGLE_INCREMENT) {
                // Directly call shootMissiles using current angle
                shootMissiles(angle);
            }
            this.lastAttackTime = GameState.getInstance().getGameSeconds();
        }
    }


    private void shootMissiles(double angleDegrees) {
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,
                ImageEnums.LaserBullet, 0.75f);


        int movementSpeed = 3;
        MissileEnums missileType = MissileEnums.DefaultLaserBullet;
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
        String objectType = "Enemy Protoss Beacon";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false, true, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);


        //Calculate the angle based on the current chargingAnimation. Because we want to fire from 4 directions, we also need to keep
        //track of the angle that the given chargingAnimation has in this method
        Point bulletOrigin = calculateBulletDestination(angleDegrees, 1, this.getCenterXCoordinate(), this.getCenterYCoordinate());
        Point bulletDestination = calculateBulletDestination(angleDegrees, 400, this.getCenterXCoordinate(), this.getCenterYCoordinate());

        missile.resetMovementPath();

        missile.setCenterCoordinates(bulletOrigin.getX(), bulletOrigin.getY());
        missile.getMovementConfiguration().setDestination(bulletDestination); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(bulletOrigin.getX(), bulletOrigin.getY());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(this);

        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private Point calculateBulletDestination(double angleDegrees, int distance, int centerX, int centerY) {
        // Convert the angle from degrees to radians because Math functions use radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the X and Y coordinates
        int targetX = centerX + (int) (Math.cos(angleRadians) * distance);
        int targetY = centerY + (int) (Math.sin(angleRadians) * distance);

        // Return the calculated coordinates as a Point object
        return new Point(targetX, targetY);
    }

}
