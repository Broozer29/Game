package net.riezebos.bruus.tbd.game.gameobjects.player;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;

public enum PlayerPrimaryAttackTypes {
    Laserbeam(MissileEnums.PlayerLaserbeam),
    Flamethrower(null),
    Carrier(null);

    private MissileEnums correspondingMissileEnum;

    PlayerPrimaryAttackTypes (MissileEnums correspondingMissileEnum) {
        this.correspondingMissileEnum = correspondingMissileEnum;
    }

    public MissileEnums getCorrespondingMissileEnum () {
        return correspondingMissileEnum;
    }
}
