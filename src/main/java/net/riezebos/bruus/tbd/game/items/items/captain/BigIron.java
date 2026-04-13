package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class BigIron extends Item {
    public static float maxChargeSeconds = 3;
    public static float amountOfIntervals = 8f;
    public static float interval = 4f / amountOfIntervals;
    public static float damagePerInterval = 0.75f;
    public static float scaleGrowthPerInterval = 0.175f;


    public BigIron() {
        super(ItemEnums.BigIron, 1, ItemApplicationEnum.UponAcquiring);
    }


    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject target) {
        //shouldnt do anything here, place logic in the gun since we need info from there that isnt compatible with interface
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(!PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain)){
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return false;
        }

        return true;
    }
}