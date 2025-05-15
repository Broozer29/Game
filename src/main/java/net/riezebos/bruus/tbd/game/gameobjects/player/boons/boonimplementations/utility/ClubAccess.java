package net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.Boon;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonCategories;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class ClubAccess implements Boon {

    private int upgradeCost = 3;
    private static ClubAccess instance = new ClubAccess();
    private boolean hasAppliedDuringRun = false;
    private int maxLevel = 5;
    private float discountPerStack = 5;

    private ClubAccess() {

    }

    public static ClubAccess getInstance() {
        return instance;
    }


    @Override
    public void applyUpgrade(BoonActivationEnums activation) {
        if (!hasAppliedDuringRun && activation.equals(BoonActivationEnums.Start_of_Level)) {
            PlayerStats.getInstance().setShopRerollDiscount(
                    Math.round(PlayerProfileManager.getInstance().getLoadedProfile().getClubAccessLevel() * this.discountPerStack)
            );
            hasAppliedDuringRun = true;
        }
    }

    @Override
    public boolean isUnlocked() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getClubAccessLevel() > 0;
    }

    @Override
    public BoonCategories getBoonCategory() {
        return BoonCategories.Utility;
    }

    @Override
    public String getBoonName() {
        return "CLUB ACCESS";
    }

    @Override
    public String getBoonDescription() {
        return "Grants " + Math.round(PlayerProfileManager.getInstance().getLoadedProfile().getClubAccessLevel() * this.discountPerStack) + "% discount on refreshing the shop.";
    }

    @Override
    public String getBoonUnlockCondition() {
        return "Obtain 3 VIP Tickets in a single run to unlock this boon.";
    }

    @Override
    public void upgradeBoon() {
        if (canUpgradeFurther() && PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds() >= upgradeCost) {
            int currentLevel = PlayerProfileManager.getInstance().getLoadedProfile().getClubAccessLevel();
            PlayerProfileManager.getInstance().getLoadedProfile().setClubAccessLevel(currentLevel + 1);
            PlayerProfileManager.getInstance().getLoadedProfile().addEmeralds(-upgradeCost);
            PlayerProfileManager.getInstance().exportCurrentProfile();
            AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
        } else if (canUpgradeFurther() && PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds() < upgradeCost) {
            AudioManager.getInstance().addAudio(AudioEnums.GenericError);
        }
    }

    @Override
    public int getBoonUpgradeCost() {
        return upgradeCost;
    }

    @Override
    public boolean canUpgradeFurther() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getClubAccessLevel() < maxLevel
                && PlayerProfileManager.getInstance().getLoadedProfile().getClubAccessLevel() > 0;
    }

    @Override
    public BoonEnums getBoonEnum() {
        return BoonEnums.CLUB_ACCESS;
    }

    @Override
    public int getCurrentLevel() {
        return PlayerProfileManager.getInstance().getLoadedProfile().getClubAccessLevel();
    }

    @Override
    public void setHasAppliedDuringRun(boolean hasAppliedDuringRun) {
        this.hasAppliedDuringRun = hasAppliedDuringRun;
    }

    @Override
    public MenuFunctionEnums getSelectBoonMenuFunctionEnum() {
        return MenuFunctionEnums.SelectClubAccess;
    }

}
