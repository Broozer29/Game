package game.items.items;

import game.gameobjects.GameObject;
import game.gameobjects.player.PlayerStats;
import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.movement.PathFinderEnums;

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
