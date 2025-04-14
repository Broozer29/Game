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

public class TreasureHunter implements Boon {
    private static TreasureHunter instance = new TreasureHunter();
    public static int chanceShiftAmount = 3;
    private int maxLevel = 5;
    private boolean hasAppliedThisRun = false;
    private int upgradeCost = 2;

    private TreasureHunter() {

    }

    public static TreasureHunter getInstance() {
        return instance;
    }


    @Override
    public void applyUpgrade(BoonActivationEnums boonActivationEnums) {
        if(!hasAppliedThisRun && boonActivationEnums.equals(BoonActivationEnums.Start_of_Level)){
            PlayerStats.getInstance().modifyRelicChanceModifier(PlayerProfileManager.getInstance().getLoadedProfile().getTreasureHunterLevel() * chanceShiftAmount);
            hasAppliedThisRun = true;
        }
    }

    @Override
    public boolean isUnlocked() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getTreasureHunterLevel() > 0;
    }

    @Override
    public BoonCategories getBoonCategory() {
        return BoonCategories.Utility;
    }

    @Override
    public String getBoonName() {
        return "Treasure Hunter";
    }

    @Override
    public String getBoonDescription() {
        return "Increases the chance for Relic items to appear in the shop by " + (PlayerProfileManager.getInstance().getLoadedProfile().getTreasureHunterLevel() * chanceShiftAmount) + " %.";
    }

    @Override
    public String getBoonUnlockCondition() {
        return "Obtain 3 relics during a single run.";
    }

    @Override
    public void upgradeBoon() {
        if(canUpgradeFurther() && PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds() >= upgradeCost) {
            int currentLevel = PlayerProfileManager.getInstance().getLoadedProfile().getTreasureHunterLevel();
            PlayerProfileManager.getInstance().getLoadedProfile().setTreasureHunterLevel(currentLevel + 1);
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
        return PlayerProfileManager.getInstance().getLoadedProfile().getTreasureHunterLevel() < maxLevel
                && PlayerProfileManager.getInstance().getLoadedProfile().getTreasureHunterLevel() > 0;
    }

    @Override
    public BoonEnums getBoonEnum() {
        return BoonEnums.TREASURE_HUNTER;
    }

    @Override
    public int getCurrentLevel() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getTreasureHunterLevel();
    }

    @Override
    public void setHasAppliedDuringRun(boolean hasAppliedDuringRun) {
        this.hasAppliedThisRun = hasAppliedDuringRun;
    }

    @Override
    public MenuFunctionEnums getSelectBoonMenuFunctionEnum() {
        return MenuFunctionEnums.SelectTreasureHunter;
    }
}
