package game.items.items;

import game.items.Item;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.objects.GameObject;
import game.objects.player.PlayerStats;

public class Battery extends Item {

    public Battery () {
        super(ItemEnums.Battery, 1, EffectActivationTypes.PlayerStatsModificationOnCreation, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().setMaxSpecialAttackCharges(1 + quantity);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        applyEffectToObject(null);
    }

}
