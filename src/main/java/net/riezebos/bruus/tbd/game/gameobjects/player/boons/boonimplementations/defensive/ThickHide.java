package net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.defensive;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.Boon;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonCategories;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility.BountyHunter;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class ThickHide implements Boon {

    private static ThickHide instance = new ThickHide();
    private float modifierAmount = 2;
    private int upgradeCost = 1;
    private int maxLevel = 5;


    private ThickHide() {

    }

    public static ThickHide getInstance() {
        return instance;
    }


    @Override
    public void applyUpgrade(BoonActivationEnums boonActivationEnums) {
        if(boonActivationEnums.equals(BoonActivationEnums.LevelingUp)){
            PlayerStats.getInstance().addBaseArmor(modifierAmount);
        }
    }

    @Override
    public boolean isUnlocked() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getThickHideLevel() > 0;
    }

    @Override
    public BoonCategories getBoonCategory() {
        return BoonCategories.Utility;
    }

    @Override
    public String getBoonName() {
        return "Thick Hide";
    }

    @Override
    public String getBoonDescription() {
        return "Gain " + (this.getCurrentLevel() * modifierAmount) + " armor instead of base damage when leveling up.";
    }

    @Override
    public String getBoonUnlockCondition() {
        return "Placeholder";
    }

    @Override
    public void upgradeBoon() {
        if(canUpgradeFurther() && PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds() >= upgradeCost) {
            int currentLevel = PlayerProfileManager.getInstance().getLoadedProfile().getThickHideLevel();
            PlayerProfileManager.getInstance().getLoadedProfile().setThickHideLevel(currentLevel + 1);
            PlayerProfileManager.getInstance().getLoadedProfile().addEmeralds(-upgradeCost);
            PlayerProfileManager.getInstance().exportCurrentProfile();
            AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
        }else if(canUpgradeFurther() && PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds() < upgradeCost){
            AudioManager.getInstance().addAudio(AudioEnums.GenericError);
        }
    }

    @Override
    public int getBoonUpgradeCost() {
        return upgradeCost;
    }

    @Override
    public boolean canUpgradeFurther() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getThickHideLevel() < maxLevel
                && PlayerProfileManager.getInstance().getLoadedProfile().getThickHideLevel() > 0;
    }

    @Override
    public BoonEnums getBoonEnum() {
        return BoonEnums.THICK_HIDE;
    }

    @Override
    public int getCurrentLevel() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getThickHideLevel();
    }

    @Override
    public void setHasAppliedDuringRun(boolean hasAppliedDuringRun) {
        //not needed for this one
    }

    @Override
    public MenuFunctionEnums getSelectBoonMenuFunctionEnum() {
        return MenuFunctionEnums.SelectThickHide;
    }
}
