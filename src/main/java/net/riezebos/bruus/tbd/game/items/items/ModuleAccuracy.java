package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;

public class ModuleAccuracy extends Item {
    private float damageBonus;
    private boolean shouldApply;

    public ModuleAccuracy () {
        super(ItemEnums.ModuleAccuracy, 1, ItemApplicationEnum.ApplyOnCreation);
        calculateDamageBonus();
        shouldApply = true;
    }

    private void calculateDamageBonus(){
        damageBonus = this.quantity * 0.2f;
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        calculateDamageBonus();
        applyEffectToObject(null);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(shouldApply) {
            PlayerStats.getInstance().addDroneBonusDamage(this.damageBonus);
            shouldApply = false;
        }
    }

    private void removeEffect(){
        if(quantity > 0){
            PlayerStats.getInstance().addDroneBonusDamage(-this.damageBonus);
        }
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return true;
    }
}
