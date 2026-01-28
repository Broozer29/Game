package net.riezebos.bruus.tbd.game.gameobjects.player.boons;

import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;

public interface Boon {
    void applyUpgrade(BoonActivationEnums boonActivationEnums);

    boolean isUnlocked();

    BoonCategories getBoonCategory();

    String getBoonName();

    String getBoonDescription();

    String getBoonUnlockCondition();

    void upgradeBoon();

    int getBoonUpgradeCost();

    boolean canUpgradeFurther();

    BoonEnums getBoonEnum();

    int getCurrentLevel();

    void setHasAppliedDuringRun(boolean hasAppliedDuringRun);

    MenuFunctionEnums getSelectBoonMenuFunctionEnum();
}
