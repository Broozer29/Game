package net.riezebos.bruus.tbd.game.gameobjects.player.boons;

import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;

public interface Boon {
    public void applyUpgrade(BoonActivationEnums boonActivationEnums);
    public boolean isUnlocked();
    public BoonCategories getBoonCategory();
    public String getBoonName();
    public String getBoonDescription();
    public String getBoonUnlockCondition();
    public void upgradeBoon();
    public int getBoonUpgradeCost();
    public boolean canUpgradeFurther();
    public BoonEnums getBoonEnum();
    public int getCurrentLevel();
    public void setHasAppliedDuringRun(boolean hasAppliedDuringRun);
    public MenuFunctionEnums getSelectBoonMenuFunctionEnum();
}
