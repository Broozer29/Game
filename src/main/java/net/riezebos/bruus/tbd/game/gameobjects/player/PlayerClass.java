package net.riezebos.bruus.tbd.game.gameobjects.player;

public enum PlayerClass {
    Captain(PlayerPrimaryAttackTypes.Laserbeam, PlayerSpecialAttackTypes.EMP),
    FireFighter(PlayerPrimaryAttackTypes.Flamethrower, PlayerSpecialAttackTypes.FlameShield),
    Carrier(PlayerPrimaryAttackTypes.Carrier, PlayerSpecialAttackTypes.Carrier);

    private PlayerPrimaryAttackTypes primaryAttackType;
    private PlayerSpecialAttackTypes secondaryAttackType;

    PlayerClass (PlayerPrimaryAttackTypes primaryAttackType, PlayerSpecialAttackTypes secondaryAttackType) {
        this.primaryAttackType = primaryAttackType;
        this.secondaryAttackType = secondaryAttackType;
    }

    public PlayerSpecialAttackTypes getSecondaryAttackType () {
        return secondaryAttackType;
    }

    public PlayerPrimaryAttackTypes getPrimaryAttackType () {
        return primaryAttackType;
    }
}
