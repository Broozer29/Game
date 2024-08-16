package game.gameobjects.friendlies.Drones;

import game.gameobjects.GameObject;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyManager;
import game.gameobjects.player.PlayerManager;
import game.gameobjects.player.PlayerStats;
import game.gameobjects.player.spaceship.SpaceShip;
import game.gamestate.GameStateInfo;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.PathFinderEnums;
import game.movement.Point;
import game.movement.MovementPatternSize;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.gameobjects.friendlies.FriendlyObject;
import game.gameobjects.friendlies.FriendlyObjectConfiguration;
import game.gameobjects.missiles.*;
import VisualAndAudioData.image.ImageEnums;
import game.movement.pathfinders.StraightLinePathFinder;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class Drone extends FriendlyObject {

    private double lastAttackTime = 0.0;

    public Drone (SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
    }

    public void activateObjectAction () {
        switch (this.friendlyObjectType) {
            case Missile_Drone:
                fireAction();
                break;
            default:
                break;
        }
    }

    private void fireAction () {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed()) {
            fireMissile();
            lastAttackTime = currentTime; // Update the last attack time after firing
        }
    }


    private void fireMissile () {
        MissileEnums missileType = MissileEnums.PlayerLaserbeam;
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.animation.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.animation.getCenterYCoordinate());
        missileSpriteConfiguration.setImageType(missileType.getImageType());
        missileSpriteConfiguration.setScale(1f);


        float xMovementSpeed = 7.5f;
        float yMovementSpeed = 7.5f;
//        float damage = PlayerStats.getInstance().getBaseDamage() * PlayerStats.getInstance().getDroneDamageRatio();
        float damage = PlayerStats.getInstance().getBaseDamage() * PlayerStats.getInstance().getDroneDamageRatio();
        Direction rotation = Direction.RIGHT;
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        PathFinder pathFinder = selectPathFinder();

        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                xMovementSpeed, yMovementSpeed, pathFinder, movementPatternSize, rotation
        );
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();

        boolean isFriendly = true;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType
                , 100, 100, null, damage, ImageEnums.Impact_Explosion_One, isFriendly, allowedToDealDamage, objectType,
                missileType.isBoxCollision(), false, false, false);


        Missile missile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration, movementConfiguration);

        missile.setOwnerOrCreator(this);
        missile.setObjectType("Drone Missile");
        missile.resetMovementPath();
        adjustPathFinder(missile.getMovementConfiguration().getPathFinder(), missile.getMovementConfiguration(), missile);
        missile.setCenterCoordinates(this.getAnimation().getCenterXCoordinate(), this.getAnimation().getCenterYCoordinate());

        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(animation.getCenterXCoordinate(), animation.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(this);

        MissileManager.getInstance().addExistingMissile(missile);
    }

    private void adjustPathFinder (PathFinder pathFinder, MovementConfiguration movementConfiguration, GameObject missile) {
        if (pathFinder instanceof HomingPathFinder) {
            movementConfiguration.setTargetToChase(((HomingPathFinder) pathFinder).getTarget(true, this.xCoordinate, this.yCoordinate));
        } else if (pathFinder instanceof StraightLinePathFinder) {


            SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
            Enemy closestEnemy = EnemyManager.getInstance().getClosestEnemy(spaceship.getCenterXCoordinate(), spaceship.getCenterYCoordinate());
            if (closestEnemy != null) {
                Point point = new Point(closestEnemy.getCenterXCoordinate(), closestEnemy.getCenterYCoordinate());
                point.setX(point.getX() - missile.getWidth() / 2);
                point.setY(point.getY() - missile.getHeight() / 2);
                movementConfiguration.setDestination(point);
            } else {
                movementConfiguration.setDestination(pathFinder.calculateInitialEndpoint(movementConfiguration.getCurrentLocation(), movementConfiguration.getRotation(), this.isFriendly()));
            }
        }
    }

    private PathFinder selectPathFinder () {
        if (PlayerStats.getInstance().getDronePathFinder().equals(PathFinderEnums.StraightLine)) {
            return new StraightLinePathFinder();
        } else if (PlayerStats.getInstance().getDronePathFinder().equals(PathFinderEnums.Homing)) {
            return new HomingPathFinder();
        }

        return new RegularPathFinder();
    }


}