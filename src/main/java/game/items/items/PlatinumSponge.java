package game.items.items;

import game.items.Item;
import game.items.effects.EffectIdentifiers;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.items.effects.effectimplementations.OutOfCombatArmorBonus;
import game.gameobjects.GameObject;

public class PlatinumSponge extends Item {

    float armorBonus;

    public PlatinumSponge () {
        super(ItemEnums.PlatinumSponge, 1, EffectActivationTypes.CheckEveryGameTick, ItemApplicationEnum.ApplyOnCreation);
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
