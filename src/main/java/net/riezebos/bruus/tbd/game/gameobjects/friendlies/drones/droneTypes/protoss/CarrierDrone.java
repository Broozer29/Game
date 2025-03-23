package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class CarrierDrone extends Drone {
    public CarrierDrone(SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
        this.setAllowedVisualsToRotate(false);
        this.setAllowedToMove(false);
        this.droneType = DroneTypes.CarrierDrone;
        this.maxHitPoints = 100;
        this.currentHitpoints = maxHitPoints;
        this.isProtoss = true;
    }

    public void activateObject () {
        //determined by items
    }

    public void fireAction (){
        //to do
    }
}
