package game.items.items;

import game.gameobjects.GameObject;
import game.gameobjects.player.PlayerStats;
import game.items.Item;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;

public class Thornweaver extends Item {
    private float buffAmount = 0.1f; //Chance to apply on-hit effects
    public Thornweaver(){
        super(ItemEnums.Thornweaver, 1, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().setAttacksApplyThorns(true);
        PlayerStats.getInstance().setChanceForThornsToApplyOnHitEffects(quantity * buffAmount);
    }

}
