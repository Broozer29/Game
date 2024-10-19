package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.game.movement.PathFinderEnums;

public class ModuleAccuracy extends Item {
    float damageBonus;

    public ModuleAccuracy () {
        super(ItemEnums.ModuleAccuracy, 1, ItemApplicationEnum.ApplyOnCreation);
        calculateDamageBonus();
    }

    private void calculateDamageBonus(){
        damageBonus = this.quantity * 0.2f;
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        calculateDamageBonus();
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().addDroneBonusDamage(this.damageBonus);
        PlayerStats.getInstance().setDroneStraightLinePathFinder(PathFinderEnums.StraightLine);
    }
}
