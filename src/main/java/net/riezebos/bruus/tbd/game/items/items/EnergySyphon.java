package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.HealPlayerOnDeath;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class EnergySyphon extends Item {

    private float barrierAmount;

    public EnergySyphon(){
        super(ItemEnums.EnergySiphon, 1, ItemApplicationEnum.AfterCollision);
        calculateBarrierAmount();
    }


    private void calculateBarrierAmount(){
        this.barrierAmount = 5 * quantity;
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

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return true;
    }

}
