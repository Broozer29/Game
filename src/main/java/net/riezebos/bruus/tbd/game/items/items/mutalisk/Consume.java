package net.riezebos.bruus.tbd.game.items.items.mutalisk;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class Consume extends Item {

    public static int hitpointsBonus = 1;
    public static int killRequirement = 15;

    public Consume () {
        super(ItemEnums.Consume, 1, ItemApplicationEnum.UponAcquiring);
        //Dont add the contract to the helper here, since this will mess with the "isAvailable" method handling
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
    }


    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }


    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Mutalisk);
    }

}
