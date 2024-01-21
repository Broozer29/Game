package game.items.items;

import game.items.Item;
import game.items.ItemApplicationEnum;
import game.items.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.items.effects.effecttypes.OutOfCombatArmorBonus;
import game.objects.GameObject;

public class PlatinumSponge extends Item {

    float armorBonus;

    public PlatinumSponge () {
        super(ItemEnums.PlatinumSponge, 1, EffectActivationTypes.OutOfCombatArmorBonus, ItemApplicationEnum.ApplyOnCreation);
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
        OutOfCombatArmorBonus effect = new OutOfCombatArmorBonus(armorBonus);
        gameObject.addEffect(effect);
    }
}
