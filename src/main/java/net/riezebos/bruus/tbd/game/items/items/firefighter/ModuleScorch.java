package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;

public class ModuleScorch extends Item {

    private float orbitrangeBonus;
    private boolean shouldApply;

    public ModuleScorch () {
        super(ItemEnums.ModuleScorch, 1, ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
        orbitrangeBonus = 30;
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        //Additional stacks don't really do anything
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if (shouldApply) {
            PlayerStats.getInstance().setDroneType(DroneTypes.FireBall);
            PlayerStats.getInstance().addDroneBonusOrbitRange(this.orbitrangeBonus);
            shouldApply = false;
        }
    }

    private void removeEffect () {
        if (quantity > 0) {
            PlayerStats.getInstance().addDroneBonusDamage(-this.orbitrangeBonus);
        }
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
