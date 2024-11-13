package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

public class BouncingModuleAddon extends Item {

    private float bonusDamagePercentage = 0;

    public BouncingModuleAddon () {
        super(ItemEnums.BouncingModuleAddon, 1, ItemApplicationEnum.BeforeCollision);
        recalculateBonus();
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
    }

    @Override
    public void modifyAttackingObject (GameObject applier, GameObject target) {
        if (applier instanceof Missile missile) {
            if (missile.isPiercesThroughObjects() && !missile.hasCollidedBeforeWith(target) && missile.getAmountOfPiercesLeft() < missile.getMaximumAmountOfPierces()) {
                applier.modifyBonusDamageMultiplier(bonusDamagePercentage);
            }
        }
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        recalculateBonus();
    }

    private void recalculateBonus () {
        bonusDamagePercentage = quantity * 0.25f;
    }

}
