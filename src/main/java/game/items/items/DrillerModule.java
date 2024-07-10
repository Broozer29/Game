package game.items.items;

import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.gameobjects.GameObject;
import game.gameobjects.player.PlayerStats;

public class DrillerModule extends Item {

    public DrillerModule(){
        super(ItemEnums.DrillerModule, 1, EffectActivationTypes.PlayerStatsModificationOnCreation, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().addPiercingMissilesAmount(this.quantity);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        applyEffectToObject(null);
    }
}
