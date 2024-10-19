package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

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
