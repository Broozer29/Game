package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.OutOfCombatArmorBonus;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

public class PlatinumSponge extends Item {

    float armorBonus;

    public PlatinumSponge () {
        super(ItemEnums.PlatinumSponge, 1,  ItemApplicationEnum.ApplyOnCreation);
        calculateArmorBonus();
    }

    private void calculateArmorBonus () {
        armorBonus = quantity * 100;
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        calculateArmorBonus();
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        OutOfCombatArmorBonus effect = new OutOfCombatArmorBonus(armorBonus, EffectIdentifiers.PlatinumSpongeArmorBonus);
        gameObject.addEffect(effect);
    }

}
