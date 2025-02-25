package net.riezebos.bruus.tbd.game.playerprofile.boons;

import net.riezebos.bruus.tbd.game.playerprofile.profile.PlayerProfile;

public interface StartOfGameBoon {
    public void applyUpgrade();
    public boolean isUnlocked();
    public UpgradeCategories getUpgradeCategory();
    public String getUpgradeName();
    public String getUpgradeDescription();
    public String getUpgradeUnlockCondition();
    public void upgrade();
    public int getUpgradeCost();
    public boolean canUpgradeFurther();
    public BoonEnums getBoonType();
    public int getCurrentLevel();
}
