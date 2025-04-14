package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.StackingInCombatArmorBonus;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class PlatinumSponge extends Item {

    private float armorBonus;

    public PlatinumSponge () {
        super(ItemEnums.PlatinumSponge, 1,  ItemApplicationEnum.ApplyOnCreation);
        calculateArmorBonus();
    }

    private void calculateArmorBonus () {
        armorBonus = quantity * 3;
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        calculateArmorBonus();
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
//        OutOfCombatArmorBonus effect = new OutOfCombatArmorBonus(armorBonus, EffectIdentifiers.PlatinumSpongeArmorBonus, 2);
        StackingInCombatArmorBonus effect = new StackingInCombatArmorBonus(armorBonus, EffectIdentifiers.PlatinumSpongeArmorBonus, 2);
        gameObject.addEffect(effect);
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return true;
    }
}
