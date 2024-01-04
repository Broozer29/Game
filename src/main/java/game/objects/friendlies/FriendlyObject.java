package game.objects.friendlies;

import game.movement.MovementConfiguration;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class FriendlyObject extends GameObject {
    protected FriendlyObjectEnums friendlyObjectType;

    public FriendlyObject (SpriteConfiguration spriteConfiguration, FriendlyObjectConfiguration friendlyConfiguration) {
        super(spriteConfiguration);
        this.friendlyObjectType = friendlyConfiguration.getFriendlyType();
        this.attackSpeed = friendlyConfiguration.getAttackSpeedCooldown();
        this.setFriendly(true);
        initMovementConfiguration(friendlyConfiguration);
    }

    public FriendlyObject (SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration friendlyConfiguration) {
        super(spriteAnimationConfiguration);
        this.friendlyObjectType = friendlyConfiguration.getFriendlyType();
        this.attackSpeed = friendlyConfiguration.getAttackSpeedCooldown();
        this.setFriendly(true);
        initMovementConfiguration(friendlyConfiguration);
    }

    protected void initMovementConfiguration (FriendlyObjectConfiguration friendlyObjectConfiguration) {
        PathFinder pathFinder = friendlyObjectConfiguration.getPathFinder();
        movementConfiguration = new MovementConfiguration();
        movementConfiguration.setPathFinder(pathFinder);
        movementConfiguration.setCurrentLocation(currentLocation);
        movementConfiguration.setDestination(pathFinder.calculateInitialEndpoint(currentLocation, movementDirection, false));
        movementConfiguration.setRotation(friendlyObjectConfiguration.getMovementDirection());
        movementConfiguration.setXMovementSpeed(friendlyObjectConfiguration.getxMovementSpeed());
        movementConfiguration.setYMovementSpeed(friendlyObjectConfiguration.getyMovementSpeed());
        movementConfiguration.setStepsTaken(0);
        movementConfiguration.setHasLock(true);
    }

    public void activateObjectAction(){
        //Exists to be overriden
    }
}
