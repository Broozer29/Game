package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

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
