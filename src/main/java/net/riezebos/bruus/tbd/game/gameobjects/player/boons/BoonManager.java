package net.riezebos.bruus.tbd.game.gameobjects.player.boons;

import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.defensive.ThickHide;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility.*;
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
//        utilityBoon = null;

        if (defensiveBoon != null) {
            defensiveBoon.setHasAppliedDuringRun(false);
        }
//        defensiveBoon = null;

        if (offensiveBoon != null) {
            offensiveBoon.setHasAppliedDuringRun(false);
        }
//        offensiveBoon = null;
    }

    public void setUtilityBoon(Boon upgrade) {
        if (this.utilityBoon != null && this.utilityBoon.getBoonEnum().equals(upgrade.getBoonEnum())) {
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
        setUtilityBoon(saveFile.getSelectedUtilityBoon());
        setDefensiveBoon(saveFile.getSelectedDefenseBoon());
        setOffensiveBoon(saveFile.getSelectedOffenseBoon());
    }

    private void setUtilityBoon(BoonEnums boonEnum) {
        if (boonEnum == null) {
            return;
        }

        switch (boonEnum) {
            case COMPOUND_WEALTH:
                utilityBoon = CompoundWealth.getInstance();
                break;
            case BOUNTY_HUNTER:
                utilityBoon = BountyHunter.getInstance();
                break;
            case CLUB_ACCESS:
                utilityBoon = ClubAccess.getInstance();
                break;
            case NEPOTISM:
                utilityBoon = Nepotism.getInstance();
                break;
            case TREASURE_HUNTER:
                utilityBoon = TreasureHunter.getInstance();
                break;
        }
    }

    private void setDefensiveBoon(BoonEnums boonEnum) {
        if (boonEnum == null) {
            return;
        }

        switch (boonEnum){
            case THICK_HIDE:
                defensiveBoon = ThickHide.getInstance();
                break;
        }
    }

    private void setOffensiveBoon(BoonEnums boonEnum) {
    }
}