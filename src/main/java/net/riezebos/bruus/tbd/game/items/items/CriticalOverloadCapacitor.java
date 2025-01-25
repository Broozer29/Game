package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;

public class CriticalOverloadCapacitor extends Item {
    private boolean shouldApply;

    public CriticalOverloadCapacitor () {
        super(ItemEnums.CriticalOverloadCapacitor, 1, ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if (shouldApply) {
            PlayerStats.getInstance().addCriticalStrikeDamageMultiplier(this.quantity);
            shouldApply = false;
        }
    }

    private void removeEffect () {
        if (this.quantity > 0) {
            PlayerStats.getInstance().addCriticalStrikeDamageMultiplier(-this.quantity);
        }
    }

    public void increaseQuantityOfItem (int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return true;
    }

}

