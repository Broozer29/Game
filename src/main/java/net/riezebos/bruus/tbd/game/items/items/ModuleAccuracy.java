package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ModuleAccuracy extends Item {
    public static float damageBonus = 0.25f;
    private boolean shouldApply;

    public ModuleAccuracy () {
        super(ItemEnums.ModuleAccuracy, 1, ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }


    @Override
    public void increaseQuantityOfItem (int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(shouldApply) {
            PlayerStats.getInstance().addDroneBonusDamage(this.damageBonus * quantity);
            shouldApply = false;
        }
    }

    private void removeEffect(){
        if(quantity > 0){
            PlayerStats.getInstance().addDroneBonusDamage(-this.damageBonus * quantity);
        }
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)){
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleScorch) != null){
            return false;
        }

        return true;
    }
}
