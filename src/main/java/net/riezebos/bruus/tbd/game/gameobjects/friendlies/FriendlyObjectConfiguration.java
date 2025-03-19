package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;

public class FriendlyObjectConfiguration {

    private FriendlyObjectEnums friendlyType;
    private float attackSpeedCooldown;
    private boolean boxCollision;

    public FriendlyObjectConfiguration (FriendlyObjectEnums friendlyType, float attackSpeedCooldown, boolean boxCollision) {
        this.friendlyType = friendlyType;
        this.attackSpeedCooldown = attackSpeedCooldown;
        this.boxCollision = boxCollision;
    }


    public FriendlyObjectEnums getFriendlyType () {
        return friendlyType;
    }

    public void setFriendlyType (FriendlyObjectEnums friendlyType) {
        this.friendlyType = friendlyType;
    }

    public float getAttackSpeedCooldown () {
        return attackSpeedCooldown;
    }

    public void setAttackSpeedCooldown (float attackSpeedCooldown) {
        this.attackSpeedCooldown = attackSpeedCooldown;
    }

    public boolean isBoxCollision () {
        return boxCollision;
    }

    public void setBoxCollision (boolean boxCollision) {
        this.boxCollision = boxCollision;
    }

}
