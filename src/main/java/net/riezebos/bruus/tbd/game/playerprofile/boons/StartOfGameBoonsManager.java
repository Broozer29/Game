package net.riezebos.bruus.tbd.game.playerprofile.boons;

import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class StartOfGameBoonsManager {


    private StartOfGameBoon utilityUpgrade;
    private StartOfGameBoon defensiveUpgrade;
    private StartOfGameBoon offensiveUpgrade;
    private static StartOfGameBoonsManager instance = new StartOfGameBoonsManager();

    private StartOfGameBoonsManager() {
        //Reset manager shouldnt be needed, as now it remembers your last selected upgrades
    }

    public static StartOfGameBoonsManager getInstance() {
        return instance;
    }

    public void setUtilityUpgrade(StartOfGameBoon upgrade) {
        if (this.utilityUpgrade != null && this.utilityUpgrade.getBoonType().equals(upgrade.getBoonType())) {
            this.utilityUpgrade = null; //Allow deselection
        } else {
            AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
            this.utilityUpgrade = upgrade;
        }
    }

    public void setDefensiveUpgrade(StartOfGameBoon defensiveUpgrade) {
        this.defensiveUpgrade = defensiveUpgrade;
    }

    public void setOffensiveUpgrade(StartOfGameBoon offensiveUpgrade) {
        this.offensiveUpgrade = offensiveUpgrade;
    }

    public StartOfGameBoon getUtilityUpgrade() {
        return utilityUpgrade;
    }

    public StartOfGameBoon getDefensiveUpgrade() {
        return defensiveUpgrade;
    }

    public StartOfGameBoon getOffensiveUpgrade() {
        return offensiveUpgrade;
    }

    public void activateStartOfRunUpgrades() {
        if (utilityUpgrade != null) {
            utilityUpgrade.applyUpgrade();
        }
        if (defensiveUpgrade != null) {
            defensiveUpgrade.applyUpgrade();
        }
        if (offensiveUpgrade != null) {
            offensiveUpgrade.applyUpgrade();
        }
    }
}
