package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

public class BarrierSupersizer extends Item {
    private float modifierBonus;

    public BarrierSupersizer(){
        super(ItemEnums.BarrierSuperSizer, 1, ItemApplicationEnum.ApplyOnCreation);
        calculateModifierBonusAmount();
    }

    private void calculateModifierBonusAmount(){
        this.modifierBonus = quantity * 0.1f;
    }

    //Increases the maximum shield and overloaded shield by 10% and 20% per item
    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().addMaxShieldMultiplier(modifierBonus);
        PlayerStats.getInstance().addMaxOverloadingShieldMultiplier(modifierBonus * 2);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        calculateModifierBonusAmount();
        applyEffectToObject(null);
    }
}
