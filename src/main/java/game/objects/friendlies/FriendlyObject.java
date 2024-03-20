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


    public FriendlyObject (SpriteConfiguration spriteConfiguration, FriendlyObjectConfiguration friendlyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, movementConfiguration);
        this.friendlyObjectType = friendlyConfiguration.getFriendlyType();
        this.attackSpeed = friendlyConfiguration.getAttackSpeedCooldown();
        this.setFriendly(true);
        this.boxCollision = friendlyConfiguration.isBoxCollision();
        this.permanentFriendlyObject = friendlyConfiguration.isPermanentObject();
    }

    public FriendlyObject (SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration friendlyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, movementConfiguration);
        this.friendlyObjectType = friendlyConfiguration.getFriendlyType();
        this.attackSpeed = friendlyConfiguration.getAttackSpeedCooldown();
        this.setFriendly(true);
        this.boxCollision = friendlyConfiguration.isBoxCollision();
        this.permanentFriendlyObject = friendlyConfiguration.isPermanentObject();
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
