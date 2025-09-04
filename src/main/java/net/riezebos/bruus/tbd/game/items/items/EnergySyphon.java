package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.HealPlayerOnDeath;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class EnergySyphon extends Item {

    public static float barrierAmount = 3;

    public EnergySyphon() {
        super(ItemEnums.EnergySiphon, 1, ItemApplicationEnum.AfterCollision);
    }


    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        //Heals the player on the death of the object
        HealPlayerOnDeath healPlayerOnDeath = new HealPlayerOnDeath(true, barrierAmount * quantity, EffectActivationTypes.OnObjectDeath, EffectIdentifiers.EnergySyphonHealPlayerOnDeath);
        gameObject.addEffect(healPlayerOnDeath);
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return true;
    }

}
