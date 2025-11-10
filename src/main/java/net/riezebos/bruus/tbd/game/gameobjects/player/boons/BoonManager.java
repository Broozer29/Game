package net.riezebos.bruus.tbd.game.gameobjects.player.boons;

import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility.*;
import net.riezebos.bruus.tbd.game.gamestate.save.SaveFile;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class BoonManager {


    private Boon utilityBoon;
    private static BoonManager instance = new BoonManager();

    private BoonManager() {
    }

    public static BoonManager getInstance() {
        return instance;
    }

    public void resetManager() {
        if (utilityBoon != null) {
            utilityBoon.setHasAppliedDuringRun(false);
        }
        this.utilityBoon = null;
    }

    public void setUtilityBoon(Boon upgrade) {
        if (this.utilityBoon != null && this.utilityBoon.getBoonEnum().equals(upgrade.getBoonEnum())) {
            this.utilityBoon = null; //Allow deselection
        } else {
            AudioManager.getInstance().addAudio(AudioEnums.GenericSelect);
            this.utilityBoon = upgrade;
        }
    }


    public Boon getActiveBoon() {
        return utilityBoon;
    }
    public void activateBoons(BoonActivationEnums boonActivationEnums) {
        if (utilityBoon != null) {
            utilityBoon.applyUpgrade(boonActivationEnums);
        }
    }

    public void loadInSaveFile(SaveFile saveFile) {
        setUtilityBoon(saveFile.getSelectedBoon());
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

}