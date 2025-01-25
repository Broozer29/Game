package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class FriendlyObject extends GameObject {
    protected FriendlyObjectEnums friendlyObjectType;

    //This class is obsolete at this point but may be required later on
    public FriendlyObject (SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration friendlyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration);
        this.friendlyObjectType = friendlyConfiguration.getFriendlyType();
        this.attackSpeed = friendlyConfiguration.getAttackSpeedCooldown();
        this.setFriendly(true);
        this.boxCollision = friendlyConfiguration.isBoxCollision();
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
    }


    public void activateObject () {
        //Exists to be overriden
    }
}
