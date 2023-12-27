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
            case Firewall:
                initFirewall();
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

    private void initFirewall () {
        PlayerStats.getInstance().setPlayerSpecialAttackType(PlayerSpecialAttackTypes.Firewall);
        PlayerStats.getInstance().setFirewallSize(3);
        PlayerStats.getInstance().setFirewallDamage((float) 0.5);
        PlayerStats.getInstance().setFirewallSpeed(3);
        PlayerStats.getInstance().setSpecialAttackSpeed(5);
    }

}
