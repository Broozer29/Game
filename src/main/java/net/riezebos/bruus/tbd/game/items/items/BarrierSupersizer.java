package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class BarrierSupersizer extends Item {
    public static float modifierBonus = 0.2f;
    private boolean shouldApply;

    public BarrierSupersizer(){
        super(ItemEnums.BarrierSuperSizer, 1, ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }

    //Increases the maximum shield and overloaded shield by 10% and 20% per item
    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(shouldApply) {
            PlayerStats.getInstance().addMaxShieldMultiplier(modifierBonus * quantity);
            PlayerStats.getInstance().addMaxOverloadingShieldMultiplier(modifierBonus * quantity * 2);
            shouldApply = false;
        }
    }

    private void removeEffect(){
        if(quantity > 0) {
            PlayerStats.getInstance().addMaxShieldMultiplier(-modifierBonus * quantity);
            PlayerStats.getInstance().addMaxOverloadingShieldMultiplier(-modifierBonus * quantity * 2);
        }
    }

    public void increaseQuantityOfItem(int amount) {
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
