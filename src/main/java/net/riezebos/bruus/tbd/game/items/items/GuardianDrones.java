package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;

public class GuardianDrones extends Item {


    public GuardianDrones () {
        super(ItemEnums.GuardianDrone, 1, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().setAmountOfDrones(Math.min(quantity, PlayerStats.getInstance().getMaximumAmountOfDrones()));

    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum).getQuantity() < PlayerStats.getInstance().getMaximumAmountOfDrones();
        }

        return true;
    }
}
