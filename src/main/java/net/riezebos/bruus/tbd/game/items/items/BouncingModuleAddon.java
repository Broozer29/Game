package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

public class BouncingModuleAddon extends Item {

    private float bonusDamagePercentage = 0;

    public BouncingModuleAddon(){
        super(ItemEnums.BouncingModuleAddon, 1, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Also allows missiles to bounce instead of pierce, but this has to be checked in Missile on collision, not from here
        gameObject.modifyBonusDamageMultiplier(bonusDamagePercentage);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    private void recalculateBonus(){
        bonusDamagePercentage = quantity * 0.1f;
    }

}
