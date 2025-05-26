package net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility;

import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.Boon;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonCategories;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class CompoundWealth implements Boon {
    boolean hasApplied = false;
    public static int mineralUnlockRequirement = 3000;
    private static CompoundWealth instance = new CompoundWealth();

    private CompoundWealth() {

    }

    public static CompoundWealth getInstance() {
        return instance;
    }

    @Override
    public void applyUpgrade(BoonActivationEnums activation) {
        if (activation.equals(BoonActivationEnums.Whenever_Entering_Shop)) {
            PlayerInventory.getInstance().addMinerals(PlayerInventory.getInstance().getCashMoney() * (this.getCurrentLevel() * 0.1f));
        }
    }

    @Override
    public boolean isUnlocked() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getCompoundWealthLevel() > 0;
    }

    @Override
    public BoonCategories getBoonCategory() {
        return BoonCategories.Utility;
    }

    @Override
    public String getBoonName() {
        return "Compound Wealth";
    }

    @Override
    public String getBoonDescription() {
        return "Whenever you enter the shop, gain minerals equal to " + (PlayerProfileManager.getInstance().getLoadedProfile().getCompoundWealthLevel() * 100f) + "% of your minerals.";
    }

    @Override
    public String getBoonUnlockCondition() {
        return "Enter the shop with atleast " + mineralUnlockRequirement + " minerals to unlock this boon.";
    }

    @Override
    public void upgradeBoon() {
        if (canUpgradeFurther() && PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds() >= getBoonUpgradeCost()) {
            PlayerProfileManager.getInstance().getLoadedProfile().setCompoundWealthLevel(PlayerProfileManager.getInstance().getLoadedProfile().getCompoundWealthLevel() + 1);
            PlayerProfileManager.getInstance().getLoadedProfile().addEmeralds(-getBoonUpgradeCost());
            PlayerProfileManager.getInstance().exportCurrentProfile();
            AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
        }else if(canUpgradeFurther() && PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds() < getBoonUpgradeCost()){
            AudioManager.getInstance().addAudio(AudioEnums.GenericError);
        }
    }

    @Override
    public int getBoonUpgradeCost() {
        return 2;
    }

    @Override
    public boolean canUpgradeFurther() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getCompoundWealthLevel() < 3
                && PlayerProfileManager.getInstance().getLoadedProfile().getCompoundWealthLevel() > 0;
    }

    @Override
    public BoonEnums getBoonEnum() {
        return BoonEnums.COMPOUND_WEALTH;
    }

    @Override
    public int getCurrentLevel() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getCompoundWealthLevel();
    }

    @Override
    public void setHasAppliedDuringRun(boolean hasAppliedDuringRun) {
        this.hasApplied = hasAppliedDuringRun;
    }

    @Override
    public MenuFunctionEnums getSelectBoonMenuFunctionEnum() {
        return MenuFunctionEnums.SelectCompoundWealth;
    }

}
