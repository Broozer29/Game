package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ModuleAccuracy extends Item {
    public static float damageBonus = 0.35f;

    public ModuleAccuracy() {
        super(ItemEnums.ModuleAccuracy, 1, ItemApplicationEnum.ApplyOnSpaceShipCreation);
    }


    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //doesnt do anything, the existence of the item is checked
    }


    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)){
            return false;
        }
        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Mutalisk)){
            return false;
        }


        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleScorch) != null){
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleFocusFire) != null){
            return false;
        }

        if(GameState.getInstance().getStagesCompleted() == 0){
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return false;
        }

        return true;
    }
}
