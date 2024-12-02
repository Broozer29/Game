package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

public class ThornedPlates extends Item {


    private float buffAmount = 0.20f;
    private boolean shouldApply;

    public ThornedPlates () {
        super(ItemEnums.ThornedPlates, 1, ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }


    private void removeEffect () {
        PlayerStats.getInstance().modifyThornsDamageRatio(-(quantity * buffAmount));
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if (shouldApply) {
            shouldApply = false;
            PlayerStats.getInstance().modifyThornsDamageRatio(quantity * buffAmount);
        }
    }

}
