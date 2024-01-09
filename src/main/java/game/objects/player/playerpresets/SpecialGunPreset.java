package game.objects.player.playerpresets;

import game.objects.player.PlayerSpecialAttackTypes;
import game.objects.player.PlayerStats;

public class SpecialGunPreset {

    private PlayerSpecialAttackTypes specialAttackType;

    public SpecialGunPreset (PlayerSpecialAttackTypes specialAttackType) {
        this.specialAttackType = specialAttackType;
    }

    public void loadPreset () {
        switch (specialAttackType) {
            case EMP:
                initEMP();
                break;
            case Rocket_Cluster:
                break;
            default:
                break;
        }
    }


    private void initEMP () {
        PlayerStats.getInstance().setPlayerSpecialAttackType(PlayerSpecialAttackTypes.EMP);
    }
}
