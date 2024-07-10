package game.items.items;

import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.gameobjects.GameObject;
import game.gameobjects.player.PlayerStats;

public class BarrierSupersizer extends Item {
    private float modifierBonus;

    public BarrierSupersizer(){
        super(ItemEnums.BarrierSuperSizer, 1, EffectActivationTypes.PlayerStatsModificationOnCreation, ItemApplicationEnum.ApplyOnCreation);
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
