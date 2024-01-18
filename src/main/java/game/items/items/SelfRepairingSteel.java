package game.items.items;

import game.items.Item;
import game.items.ItemApplicationEnum;
import game.items.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.items.effects.effecttypes.PassiveHealthRegeneration;
import game.objects.GameObject;

public class SelfRepairingSteel extends Item {
    private float repairAmount;

    public SelfRepairingSteel(){
        super(ItemEnums.SelfRepairingSteel, 1, EffectActivationTypes.HealthRegeneration, ItemApplicationEnum.ApplyOnCreation);
        calculateHealingAmount();
    }

    private void calculateHealingAmount(){
        repairAmount = quantity / 2f;
    }

    public void increaseQuantityOfItem(int amount){
        this.quantity += amount;
        calculateHealingAmount();
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PassiveHealthRegeneration healthRegeneration = new PassiveHealthRegeneration(repairAmount);
        gameObject.addEffect(healthRegeneration);
    }
}
