package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes;

import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

public enum DroneTypes {
    Missile(ImageEnums.Drone, 1),
    ElectroShred(ImageEnums.Drone, 1),
    FireBall(ImageEnums.Drone, 1),
    ProtossScout(ImageEnums.ProtossScout, 4),
    ProtossArbiter(ImageEnums.ProtossArbiter , 3),
    ProtossShuttle(ImageEnums.ProtossShuttle , 3),
    CarrierDrone(ImageEnums.ProtossCarrierBeacon, 1); //movement speed shouldnt matter, its not supposed to move

    DroneTypes(ImageEnums image, int movementSpeed) {
        this.correspondingImageEnum = image;
        this.movementSpeed = movementSpeed;
    }

    private ImageEnums correspondingImageEnum;
    private int movementSpeed;

    public ImageEnums getCorrespondingImageEnum() {
        return correspondingImageEnum;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }
}
