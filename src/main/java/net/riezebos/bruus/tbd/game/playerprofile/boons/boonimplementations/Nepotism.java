package net.riezebos.bruus.tbd.game.playerprofile.boons.boonimplementations;

import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.playerprofile.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.playerprofile.profile.PlayerProfile;
import net.riezebos.bruus.tbd.game.playerprofile.profile.PlayerProfileManager;
import net.riezebos.bruus.tbd.game.playerprofile.boons.StartOfGameBoon;
import net.riezebos.bruus.tbd.game.playerprofile.boons.UpgradeCategories;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class Nepotism implements StartOfGameBoon {

    private int upgradeLevel;
    private static final int unlockRequirement = 0;
    private static Nepotism instance = new Nepotism();
    private UpgradeCategories category = UpgradeCategories.Utility;
    private int mineralQuantity = 75;
    private int upgradeCost = 1;
    private boolean isUnlocked = true;

    private Nepotism() {
    }

    public static Nepotism getInstance() {
        return instance;
    }

    @Override
    public void applyUpgrade() {
        upgradeLevel = PlayerProfileManager.getInstance().getLoadedProfile().getNepotismLevel(); //Reupdate it to make sure we are synced
        PlayerInventory.getInstance().addMinerals(upgradeLevel * mineralQuantity);
    }


    @Override
    public boolean isUnlocked() {
        PlayerProfile profile = PlayerProfileManager.getInstance().getLoadedProfile();
        if (profile.getEnemyKilledCounter() >= unlockRequirement) { //Should always be true for this specific upgrade
            this.upgradeLevel = profile.getNepotismLevel();
            this.isUnlocked = true;
            return isUnlocked;
        }
        return isUnlocked;
    }

    @Override
    public UpgradeCategories getUpgradeCategory() {
        return this.category;
    }

    @Override
    public String getUpgradeName() {
        return "NEPOTISM";
    }

    @Override
    public String getUpgradeDescription() {
        return "Start the game with " + this.upgradeLevel * this.mineralQuantity + " minerals";
    }

    @Override
    public String getUpgradeUnlockCondition() {
        if(isUnlocked){
            return "";
        }

        return "Always unlocked, this message should be invisible";
    }


    @Override
    public void upgrade() {
        if(canUpgradeFurther() && PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds() >= upgradeCost) {
            upgradeLevel += 1;
            PlayerProfileManager.getInstance().getLoadedProfile().setNepotismLevel(upgradeLevel);
            PlayerProfileManager.getInstance().getLoadedProfile().addEmeralds(-upgradeCost);
            PlayerProfileManager.getInstance().exportCurrentProfile();
            AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
        }
    }

    @Override
    public int getUpgradeCost() {
        return upgradeCost;
    }

    @Override
    public boolean canUpgradeFurther() {
        return upgradeLevel < 5;
    }

    @Override
    public BoonEnums getBoonType() {
        return BoonEnums.NEPOTISM;
    }

    @Override
    public int getCurrentLevel() {
        return upgradeLevel;
    }
}
