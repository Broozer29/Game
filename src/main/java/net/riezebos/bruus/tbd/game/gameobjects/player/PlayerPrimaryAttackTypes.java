package net.riezebos.bruus.tbd.game.gameobjects.player;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;

public enum PlayerPrimaryAttackTypes {
    //todo deze correspondingMissileEnums is beetje waardeloos als alles op null staat niet?

    Laserbeam(MissileEnums.PlayerLaserbeam),
    Flamethrower(null),
    Carrier(null),
    Mutalisk(null);

    private MissileEnums correspondingMissileEnum;

    PlayerPrimaryAttackTypes(MissileEnums correspondingMissileEnum) {
        this.correspondingMissileEnum = correspondingMissileEnum;
    }

    public MissileEnums getCorrespondingMissileEnum() {
        return correspondingMissileEnum;
    }
}
