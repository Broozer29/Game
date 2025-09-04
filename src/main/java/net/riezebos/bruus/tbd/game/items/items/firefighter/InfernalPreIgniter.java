package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class InfernalPreIgniter extends Item {

    public static float maxDamageBonnus = 4;
    public InfernalPreIgniter () {
        super(ItemEnums.InfernalPreIgniter, 1, ItemApplicationEnum.UponAcquiring);
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        //Additional stacks don't really do anything
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
    }

    private void removeEffect () {
    }


    @Override
    public boolean isAvailable(){
        boolean isAvailable = false;
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter) && PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) == null){
            isAvailable = true;
        }

        return isAvailable;
    }
}
