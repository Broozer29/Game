package net.riezebos.bruus.tbd.game.items.items.mutalisk;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class BileTravelPlaceholder extends Item {
    public static float bonusTravelRange = 2;
    public static float timeBetweenExplosion = 0.35f;

    public BileTravelPlaceholder () {
        super(ItemEnums.BileTravelRange, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //should be called on each enemy kill
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
