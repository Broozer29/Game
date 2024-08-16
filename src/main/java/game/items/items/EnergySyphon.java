package game.items.items;

import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectIdentifiers;
import game.items.effects.effectimplementations.HealPlayerOnDeath;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.gameobjects.GameObject;

public class EnergySyphon extends Item {

    private float barrierAmount;

    public EnergySyphon(){
        super(ItemEnums.EnergySiphon, 1, ItemApplicationEnum.AfterCollision);
        calculateBarrierAmount();
    }


    private void calculateBarrierAmount(){
        this.barrierAmount = 25 * quantity;
    }

    public void increaseQuantityOfItem(int amount){
        this.quantity += amount;
        calculateBarrierAmount();
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Heals the player on the death of the object
        HealPlayerOnDeath healPlayerOnDeath = new HealPlayerOnDeath(true, barrierAmount, EffectActivationTypes.OnObjectDeath, EffectIdentifiers.EnergySyphonHealPlayerOnDeath);
        gameObject.addEffect(healPlayerOnDeath);
    }

}
