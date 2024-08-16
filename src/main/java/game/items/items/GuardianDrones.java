package game.items.items;

import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.gameobjects.GameObject;
import game.gameobjects.player.PlayerStats;

public class GuardianDrones extends Item {

    private int maxAmount = 8;

    public GuardianDrones () {
        super(ItemEnums.GuardianDrone, 1, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().setAmountOfDrones(Math.min(quantity, maxAmount));

    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        applyEffectToObject(null);
    }
}
