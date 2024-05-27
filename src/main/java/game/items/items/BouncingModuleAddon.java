package game.items.items;

import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.objects.GameObject;
import game.objects.player.PlayerStats;

public class BouncingModuleAddon extends Item {

    private float bonusDamagePercentage = 0;

    public BouncingModuleAddon(){
        super(ItemEnums.BouncingModuleAddon, 1, EffectActivationTypes.PlayerStatsModificationOnCreation, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Also allows missiles to bounce instead of pierce, but this has to be checked in Missile on collision, not from here
        gameObject.modifyBonusDamageMultiplier(bonusDamagePercentage);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    private void recalculateBonus(){
        bonusDamagePercentage = quantity * 0.1f;
    }

}
