package game.gameobjects.friendlies;

import game.movement.MovementConfiguration;
import game.gameobjects.GameObject;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class FriendlyObject extends GameObject {
    protected FriendlyObjectEnums friendlyObjectType;
    protected boolean permanentFriendlyObject;


    public FriendlyObject (SpriteConfiguration spriteConfiguration, FriendlyObjectConfiguration friendlyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration);
        this.friendlyObjectType = friendlyConfiguration.getFriendlyType();
        this.attackSpeed = friendlyConfiguration.getAttackSpeedCooldown();
        this.setFriendly(true);
        this.boxCollision = friendlyConfiguration.isBoxCollision();
        this.permanentFriendlyObject = friendlyConfiguration.isPermanentObject();
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
    }

    public FriendlyObject (SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration friendlyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration);
        this.friendlyObjectType = friendlyConfiguration.getFriendlyType();
        this.attackSpeed = friendlyConfiguration.getAttackSpeedCooldown();
        this.setFriendly(true);
        this.boxCollision = friendlyConfiguration.isBoxCollision();
        this.permanentFriendlyObject = friendlyConfiguration.isPermanentObject();
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
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
