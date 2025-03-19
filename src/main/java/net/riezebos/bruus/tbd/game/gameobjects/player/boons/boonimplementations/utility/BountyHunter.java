package net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.Boon;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonCategories;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class BountyHunter implements Boon {

    private boolean hasAppliedThisRun = false;
    private static BountyHunter instance = new BountyHunter();
    private float modifierAmount = 0.025f;
    private int upgradeCost = 1;
    private int maxLevel = 5;


    private BountyHunter() {

    }

    public static BountyHunter getInstance() {
        return instance;
    }


    @Override
    public void applyUpgrade(BoonActivationEnums boonActivationEnums) {
        if(boonActivationEnums.equals(BoonActivationEnums.Start_of_Level) && !hasAppliedThisRun) {
            PlayerStats.getInstance().addMineralModifier(this.getCurrentLevel() * modifierAmount);
            hasAppliedThisRun = true;
        }
    }

    @Override
    public boolean isUnlocked() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getBountyHunterLevel() > 0;
    }

    @Override
    public BoonCategories getBoonCategory() {
        return BoonCategories.Utility;
    }

    @Override
    public String getBoonName() {
        return "Bounty Hunter";
    }

    @Override
    public String getBoonDescription() {
        return "Enemies grant an additional " + (this.getCurrentLevel() * modifierAmount) * 100 + "% minerals upon death.";
    }

    @Override
    public String getBoonUnlockCondition() {
        return "Complete 3 contracts at once to unlock this boon.";
    }

    @Override
    public void upgradeBoon() {
        if(canUpgradeFurther() && PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds() >= upgradeCost) {
            int currentLevel = PlayerProfileManager.getInstance().getLoadedProfile().getBountyHunterLevel();
            PlayerProfileManager.getInstance().getLoadedProfile().setBountyHunterLevel(currentLevel + 1);
            PlayerProfileManager.getInstance().getLoadedProfile().addEmeralds(-upgradeCost);
            PlayerProfileManager.getInstance().exportCurrentProfile();
            AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
        }
    }

    @Override
    public int getBoonUpgradeCost() {
        return upgradeCost;
    }

    @Override
    public boolean canUpgradeFurther() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getBountyHunterLevel() < maxLevel
                && PlayerProfileManager.getInstance().getLoadedProfile().getBountyHunterLevel() > 0;
    }

    @Override
    public BoonEnums getBoonType() {
        return BoonEnums.BOUNTY_HUNTER;
    }

    @Override
    public int getCurrentLevel() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getBountyHunterLevel();
    }

    @Override
    public void setHasAppliedDuringRun(boolean hasAppliedDuringRun) {
        this.hasAppliedThisRun = hasAppliedDuringRun;
    }

    @Override
    public MenuFunctionEnums getSelectBoonMenuFunctionEnum() {
        return MenuFunctionEnums.SelectBountyHunter;
    }
}
