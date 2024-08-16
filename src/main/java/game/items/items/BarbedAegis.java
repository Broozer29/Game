package game.items.items;

import game.gameobjects.GameObject;
import game.gameobjects.player.PlayerStats;
import game.items.Item;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;

public class BarbedAegis extends Item {

    public BarbedAegis(){
        super(ItemEnums.BarbedAegis, 1, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().setThornsArmorDamageBonusRatio(this.quantity * 0.25f);
    }
}
