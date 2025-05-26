package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.StackingInCombatArmorBonus;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class PlatinumSponge extends Item {

    public static float armorBonus = 3;
    public static float maxArmorBonus = 20;

    public PlatinumSponge () {
        super(ItemEnums.PlatinumSponge, 1,  ItemApplicationEnum.ApplyOnCreation);
    }


    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        float actualArmorBonus = Math.min(armorBonus * quantity, maxArmorBonus * quantity);
        StackingInCombatArmorBonus effect = new StackingInCombatArmorBonus(actualArmorBonus, EffectIdentifiers.PlatinumSpongeArmorBonus, 2);
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
