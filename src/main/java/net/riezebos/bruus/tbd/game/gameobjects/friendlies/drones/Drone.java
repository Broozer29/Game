package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.movement.*;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.Random;

public abstract class Drone extends FriendlyObject {
    protected Random random = new Random();
    protected double lastAttackTime = 0.0;
    protected DroneTypes droneType;

    public Drone (SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
    }

    public void activateObject () {
        //Must be overriden
    }

    public void fireAction () {
        //Must be overriden
    }

}