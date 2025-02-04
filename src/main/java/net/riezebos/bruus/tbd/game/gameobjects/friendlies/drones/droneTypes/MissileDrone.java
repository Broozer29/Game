package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class MissileDrone extends Drone {

    public MissileDrone (SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
    }

    @Override
    public void activateObject () {
        PlayerInventory playerInventory = PlayerInventory.getInstance();
        if (playerInventory.getItemFromInventoryIfExists(ItemEnums.ModuleCommand) != null) {
            return;
        }

        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed()) {
            fireAction();
            lastAttackTime = currentTime;
        }
    }

    @Override
    public void fireAction () {
        MissileEnums missileType = MissileEnums.PlayerLaserbeam;
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setImageType(missileType.getImageType());
        missileSpriteConfiguration.setScale(1f);

        float xMovementSpeed = 7.5f;
        float yMovementSpeed = 7.5f;
        float damage = PlayerStats.getInstance().getDroneDamage();
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
        return new StraightLinePathFinder();
    }

}
