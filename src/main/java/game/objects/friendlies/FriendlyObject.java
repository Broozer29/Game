package game.objects.friendlies;

import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class FriendlyObject extends GameObject {
    protected FriendlyObjectEnums friendlyObjectType;
    protected boolean permanentFriendlyObject;


    public FriendlyObject (SpriteConfiguration spriteConfiguration, FriendlyObjectConfiguration friendlyConfiguration) {
        super(spriteConfiguration);
        this.friendlyObjectType = friendlyConfiguration.getFriendlyType();
        this.attackSpeed = friendlyConfiguration.getAttackSpeedCooldown();
        this.setFriendly(true);
        this.boxCollision = friendlyConfiguration.isBoxCollision();
        this.permanentFriendlyObject = friendlyConfiguration.isPermanentObject();
        initMovementConfiguration(friendlyConfiguration);
    }

    public FriendlyObject (SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration friendlyConfiguration) {
        super(spriteAnimationConfiguration);
        this.friendlyObjectType = friendlyConfiguration.getFriendlyType();
        this.attackSpeed = friendlyConfiguration.getAttackSpeedCooldown();
        this.setFriendly(true);
        this.boxCollision = friendlyConfiguration.isBoxCollision();
        this.permanentFriendlyObject = friendlyConfiguration.isPermanentObject();
        initMovementConfiguration(friendlyConfiguration);
    }

    protected void initMovementConfiguration (FriendlyObjectConfiguration friendlyObjectConfiguration) {
        PathFinder pathFinder = friendlyObjectConfiguration.getPathFinder();
        movementConfiguration = new MovementConfiguration();
        movementConfiguration.setPathFinder(pathFinder);
        movementConfiguration.setCurrentLocation(new Point(this.xCoordinate, this.yCoordinate));
        movementConfiguration.setDestination(pathFinder.calculateInitialEndpoint(currentLocation, movementDirection, false));
        movementConfiguration.setRotation(friendlyObjectConfiguration.getMovementDirection());
        movementConfiguration.setXMovementSpeed(friendlyObjectConfiguration.getxMovementSpeed());
        movementConfiguration.setYMovementSpeed(friendlyObjectConfiguration.getyMovementSpeed());
        movementConfiguration.setStepsTaken(0);
        movementConfiguration.setHasLock(true);

        movementConfiguration.setDiamondWidth(friendlyObjectConfiguration.getMovementPatternSize().getDiamondWidth());
        movementConfiguration.setDiamondHeight(friendlyObjectConfiguration.getMovementPatternSize().getDiamondHeight());

        movementConfiguration.setStepsBeforeBounceInOtherDirection(friendlyObjectConfiguration.getMovementPatternSize().getStepsBeforeBounceInOtherDirection());

        movementConfiguration.setAngleStep(0.1);
        movementConfiguration.setCurveDistance(1);
        movementConfiguration.setRadius(5);
        movementConfiguration.setRadiusIncrement(friendlyObjectConfiguration.getMovementPatternSize().getRadiusIncrement());


        movementConfiguration.setPrimaryDirectionStepAmount(friendlyObjectConfiguration.getMovementPatternSize().getPrimaryDirectionStepAmount());
        movementConfiguration.setFirstDiagonalDirectionStepAmount(friendlyObjectConfiguration.getMovementPatternSize().getSecondaryDirectionStepAmount());
        movementConfiguration.setSecondDiagonalDirectionStepAmount(friendlyObjectConfiguration.getMovementPatternSize().getSecondaryDirectionStepAmount());

    }

    public void activateObjectAction () {
        //Exists to be overriden
    }

    public FriendlyObjectEnums getFriendlyObjectType () {
        return friendlyObjectType;
    }

    public void setFriendlyObjectType (FriendlyObjectEnums friendlyObjectType) {
        this.friendlyObjectType = friendlyObjectType;
    }

    public boolean isPermanentFriendlyObject () {
        return permanentFriendlyObject;
    }

    public void setPermanentFriendlyObject (boolean permanentFriendlyObject) {
        this.permanentFriendlyObject = permanentFriendlyObject;
    }
}
