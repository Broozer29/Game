package net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility;

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

    private int upgradeCost = 5;
    private static ClubAccess instance = new ClubAccess();
    private boolean hasAppliedDuringRun = false;
    private int maxLevel = 3;

    private ClubAccess() {

    }

    public static ClubAccess getInstance() {
        return instance;
    }


    @Override
    public void applyUpgrade(BoonActivationEnums activation) {
        if(!hasAppliedDuringRun && activation.equals(BoonActivationEnums.Start_of_Level)) {
            for(int i = 0; i < getCurrentLevel(); i++){
                PlayerInventory.getInstance().addItem(ItemEnums.VIPTicket);
            }
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
        return "Grants " + PlayerProfileManager.getInstance().getLoadedProfile().getClubAccessLevel() + " VIP Tickets upon starting the game.";
    }

    @Override
    public String getBoonUnlockCondition() {
        return "Obtain 5 VIP Tickets in a single run to unlock this boon.";
    }

    @Override
    public void upgradeBoon() {
        if(canUpgradeFurther() && PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds() >= upgradeCost) {
            int currentLevel = PlayerProfileManager.getInstance().getLoadedProfile().getClubAccessLevel();
            PlayerProfileManager.getInstance().getLoadedProfile().setClubAccessLevel(currentLevel + 1);
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
        return PlayerProfileManager.getInstance().getLoadedProfile().getClubAccessLevel() < maxLevel
                && PlayerProfileManager.getInstance().getLoadedProfile().getClubAccessLevel() > 0;
    }

    @Override
    public BoonEnums getBoonType() {
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
