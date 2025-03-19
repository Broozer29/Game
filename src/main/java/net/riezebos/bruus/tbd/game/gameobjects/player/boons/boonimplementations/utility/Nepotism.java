package net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility;

import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.Boon;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonCategories;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class Nepotism implements Boon {

    private int upgradeLevel;
    private static final int unlockRequirement = 0;
    private static Nepotism instance = new Nepotism();
    private BoonCategories category = BoonCategories.Utility;
    private int mineralQuantity = 75;
    private boolean isUnlocked = true;

    private Nepotism() {
    }

    public static Boon getInstance() {
        return instance;
    }


    @Override
    public void applyUpgrade(BoonActivationEnums activation) {
        if (activation.equals(BoonActivationEnums.Start_of_Level) && GameState.getInstance().getStagesCompleted() == 0) {
                upgradeLevel = Math.max(1, PlayerProfileManager.getInstance().getLoadedProfile().getNepotismLevel()); //Reupdate it to make sure we are synced
                PlayerInventory.getInstance().addMinerals(upgradeLevel * mineralQuantity);
            }

    }


    @Override
    public boolean isUnlocked() {
        upgradeLevel = Math.max(1, PlayerProfileManager.getInstance().getLoadedProfile().getNepotismLevel());
        return true;
    }

    @Override
    public BoonCategories getBoonCategory() {
        return this.category;
    }

    @Override
    public String getBoonName() {
        return "Nepotism";
    }

    @Override
    public String getBoonDescription() {
        return "Start the game with " + this.upgradeLevel * this.mineralQuantity + " minerals";
    }

    @Override
    public String getBoonUnlockCondition() {
        if (isUnlocked) {
            return "";
        }

        return "Always unlocked, this message should be invisible";
    }


    @Override
    public void upgradeBoon() {
        if (canUpgradeFurther() && PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds() >= getBoonUpgradeCost()) {
            upgradeLevel += 1;
            PlayerProfileManager.getInstance().getLoadedProfile().setNepotismLevel(upgradeLevel);
            PlayerProfileManager.getInstance().getLoadedProfile().addEmeralds(-getBoonUpgradeCost());
            PlayerProfileManager.getInstance().exportCurrentProfile();
            AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
        }
    }

    @Override
    public int getBoonUpgradeCost() {
        return 1;
    }

    @Override
    public boolean canUpgradeFurther() {
        return upgradeLevel < 5 && upgradeLevel > 0;
    }

    @Override
    public BoonEnums getBoonType() {
        return BoonEnums.NEPOTISM;
    }

    @Override
    public int getCurrentLevel() {
        upgradeLevel = Math.max(1, PlayerProfileManager.getInstance().getLoadedProfile().getNepotismLevel());
        return upgradeLevel;
    }

    @Override
    public void setHasAppliedDuringRun(boolean hasAppliedDuringRun) {
        //Doesnt need an impl, checks itself in applyToObject
    }

    @Override
    public MenuFunctionEnums getSelectBoonMenuFunctionEnum() {
        return MenuFunctionEnums.SelectNepotism;
    }
}
