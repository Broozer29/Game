package net.riezebos.bruus.tbd.game.items.items.mutalisk;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class InstantViralEruption extends Item {
    public static float hitpointsPerUse = 0.5f;

    public InstantViralEruption() {
        super(ItemEnums.InstantViralEruption, 1, ItemApplicationEnum.CustomActivation);
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
