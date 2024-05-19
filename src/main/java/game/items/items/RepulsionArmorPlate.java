package game.items.items;

import game.items.Item;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.objects.GameObject;

public class RepulsionArmorPlate extends Item {

    private float armorAmount;

    public RepulsionArmorPlate(){
        super(ItemEnums.RepulsionArmorPlate, 1, EffectActivationTypes.PlayerStatsModificationOnCreation, ItemApplicationEnum.ApplyOnCreation);
        armorAmount = 10;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        gameObject.adjustArmorBonus(this.quantity  * armorAmount);
    }
}
