package game.objects.friendlies.Drones;

import game.managers.ExplosionManager;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.GameObject;
import game.objects.enemies.EnemyConfiguration;
import game.objects.missiles.*;
import game.objects.neutral.Explosion;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class GuardianDrone extends GameObject {

    private DroneEnums guardianType;
    private int attackSpeedCooldown;
    private int attackFrameCount;

    public GuardianDrone (SpriteAnimationConfiguration spriteAnimationConfiguration, DroneConfiguration droneConfiguration) {
        super(spriteAnimationConfiguration);
        this.attackSpeedCooldown = droneConfiguration.getAttackSpeedCooldown();
        this.guardianType = droneConfiguration.getDroneType();
        initMovementConfiguration(droneConfiguration);
    }

    private void initMovementConfiguration (DroneConfiguration droneConfiguration) {
        PathFinder pathFinder = droneConfiguration.getPathFinder();
        movementConfiguration = new MovementConfiguration();
        movementConfiguration.setPathFinder(pathFinder);
        movementConfiguration.setCurrentLocation(currentLocation);
        movementConfiguration.setDestination(pathFinder.calculateInitialEndpoint(currentLocation, movementDirection, false));
        movementConfiguration.setRotation(droneConfiguration.getMovementDirection());
        movementConfiguration.setXMovementSpeed(droneConfiguration.getxMovementSpeed());
        movementConfiguration.setYMovementSpeed(droneConfiguration.getyMovementSpeed());
        movementConfiguration.setStepsTaken(0);
        movementConfiguration.setHasLock(true);
    }

    public void activateGuardianDrone () {
        switch (guardianType) {
            case Absorbtion_Guardian_Bot:
                break;
            case Explosion_Guardian_Bot:
                createExplosion();
                break;
            case Missile_Guardian_Bot:
                fireMissile();
                break;
            default:
                break;
        }
    }

    //Untested
    private void createExplosion () {
    }


    //Should be used for all fireXMissile methods
    private void fireMissile () {
        if (attackFrameCount >= attackSpeedCooldown) {
            PathFinder pathFinder = new RegularPathFinder();

            int xMovementSpeed = 5;
            int yMovementSpeed = 2;

            SpriteConfiguration missileSpriteConfiguration = this.spriteConfiguration;
            missileSpriteConfiguration.setyCoordinate(yCoordinate + this.height / 2);
            missileSpriteConfiguration.setImageType(ImageEnums.Player_Laserbeam);

            MissileConfiguration missileConfiguration = new MissileConfiguration(MissileTypeEnums.DefaultPlayerLaserbeam,
                    100, 100, null, null, isFriendly()
                    , pathFinder, Direction.RIGHT, xMovementSpeed, yMovementSpeed, true
                    , "Bomba Missile", (float) 7.5);

            if (pathFinder instanceof HomingPathFinder) {
                missileConfiguration.setTargetToChase(((HomingPathFinder) pathFinder).getTarget(true));
            }

            Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration);
            MissileManager.getInstance().addExistingMissile(newMissile);
            attackFrameCount = 0;

        } else {
            attackFrameCount++;
        }
    }

}