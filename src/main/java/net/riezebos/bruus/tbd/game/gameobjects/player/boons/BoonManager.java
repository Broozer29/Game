package net.riezebos.bruus.tbd.game.gameobjects.player.boons;

import net.riezebos.bruus.tbd.game.gamestate.save.SaveFile;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class BoonManager {


    private Boon utilityBoon;
    private Boon defensiveBoon;
    private Boon offensiveBoon;
    private static BoonManager instance = new BoonManager();

    private BoonManager() {
        //Reset manager shouldnt be needed, as now it remembers your last selected upgrades
    }

    public static BoonManager getInstance() {
        return instance;
    }

    public void resetManager() {
        if (utilityBoon != null) {
            utilityBoon.setHasAppliedDuringRun(false);
        }
        utilityBoon = null;

        if (defensiveBoon != null) {
            defensiveBoon.setHasAppliedDuringRun(false);
        }
        defensiveBoon = null;

        if (offensiveBoon != null) {
            offensiveBoon.setHasAppliedDuringRun(false);
        }
        offensiveBoon = null;
    }

    public void setUtilityBoon(Boon upgrade) {
        if (this.utilityBoon != null && this.utilityBoon.getBoonType().equals(upgrade.getBoonType())) {
            this.utilityBoon = null; //Allow deselection
        } else {
            AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
            this.utilityBoon = upgrade;
        }
    }

    public void setDefensiveBoon(Boon defensiveBoon) {
        this.defensiveBoon = defensiveBoon;
    }

    public void setOffensiveBoon(Boon offensiveBoon) {
        this.offensiveBoon = offensiveBoon;
    }

    public Boon getUtilityBoon() {
        return utilityBoon;
    }

    public Boon getDefensiveBoon() {
        return defensiveBoon;
    }

    public Boon getOffensiveBoon() {
        return offensiveBoon;
    }

    public void activateBoons(BoonActivationEnums boonActivationEnums) {
        if (utilityBoon != null) {
            utilityBoon.applyUpgrade(boonActivationEnums);
        }
        if (defensiveBoon != null) {
            defensiveBoon.applyUpgrade(boonActivationEnums);
        }
        if (offensiveBoon != null) {
            offensiveBoon.applyUpgrade(boonActivationEnums);
        }
    }

    public void loadInSaveFile(SaveFile saveFile) {
        //Implicitly allows players to load in boons from a save they have not unlocked yet on their own profile, making the decision that this is fine
        this.utilityBoon = saveFile.getSelectedUtilityBoon();
        this.offensiveBoon = saveFile.getSelectedOffenseBoon();
        this.defensiveBoon = saveFile.getSelectedDefenseBoon();
    }

}