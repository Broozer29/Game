package net.riezebos.bruus.tbd.game.gameobjects.friendlies.Drones;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.game.movement.*;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

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
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setImageType(missileType.getImageType());
        missileSpriteConfiguration.setScale(1f);


        float xMovementSpeed = 7.5f;
        float yMovementSpeed = 7.5f;
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
                missileType.isUsesBoxCollision(), false, false, false);


        Missile missile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration, movementConfiguration);

        missile.setOwnerOrCreator(this);
        missile.setObjectType("Drone Missile");

        missile.resetMovementPath();
        adjustStraightLinePathFinder(missile.getMovementConfiguration(), missile);
        missile.setCenterCoordinates(this.getAnimation().getCenterXCoordinate(), this.getAnimation().getCenterYCoordinate());

        missile.rotateObjectTowardsDestination(true);
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(this);

        MissileManager.getInstance().addExistingMissile(missile);
    }

    private void adjustStraightLinePathFinder (MovementConfiguration movementConfiguration, GameObject missile) {
        if (allowedToAimAtTarget()) {
            SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
            Enemy closestEnemy = EnemyManager.getInstance().getClosestEnemy(spaceship.getCenterXCoordinate(), spaceship.getCenterYCoordinate());
            if (closestEnemy != null) {
                Point point = new Point(closestEnemy.getCenterXCoordinate(), closestEnemy.getCenterYCoordinate());
                point.setX(point.getX() - missile.getWidth() / 2);
                point.setY(point.getY() - missile.getHeight() / 2);
                movementConfiguration.setDestination(point);
            }
        }
    }

    private boolean allowedToAimAtTarget () {
        return PlayerInventory.getInstance().getItems().containsKey(ItemEnums.ModuleAccuracy);
    }

    private PathFinder selectPathFinder () {
//        if (PlayerStats.getInstance().getDronePathFinder().equals(PathFinderEnums.StraightLine)) {
//            return new StraightLinePathFinder();
//        }
        return new StraightLinePathFinder();
    }


}