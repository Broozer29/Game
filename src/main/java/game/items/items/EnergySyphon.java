package game.items.items;

import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectIdentifiers;
import game.items.effects.effecttypes.HealPlayerOnDeath;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.objects.GameObject;

public class EnergySyphon extends Item {

    private float barrierAmount;

    public EnergySyphon(){
        super(ItemEnums.EnergySiphon, 1, EffectActivationTypes.CheckEveryGameTick, ItemApplicationEnum.AfterCollision);
        calculateBarrierAmount();
    }


    private void calculateBarrierAmount(){
        this.barrierAmount = 10 * quantity;
    }

    public void increaseQuantityOfItem(int amount){
        this.quantity += amount;
        calculateBarrierAmount();
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        HealPlayerOnDeath healPlayerOnDeath = new HealPlayerOnDeath(true, barrierAmount, EffectActivationTypes.CheckEveryGameTick, EffectIdentifiers.EnergySyphonHealPlayerOnDeath);
        gameObject.addEffect(healPlayerOnDeath);
    }

}
