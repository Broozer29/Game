package game.items.items;

import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.objects.GameObject;
import game.objects.friendlies.FriendlyCreator;
import game.objects.friendlies.FriendlyManager;
import game.objects.friendlies.FriendlyObject;
import game.objects.player.PlayerManager;
import game.objects.player.PlayerStats;
import game.objects.player.spaceship.SpaceShip;
import game.util.OrbitingObjectsFormatter;

public class GuardianDrones extends Item {

    private int maxAmount = 8;

    public GuardianDrones () {
        super(ItemEnums.GuardianDrone, 1, EffectActivationTypes.PlayerStatsModificationOnCreation, ItemApplicationEnum.ApplyOnCreation);
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
