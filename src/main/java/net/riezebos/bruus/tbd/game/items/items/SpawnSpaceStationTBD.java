package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class SpawnSpaceStationTBD extends Item {

    public static int maxStations = 4;

    public SpawnSpaceStationTBD() {
        super(ItemEnums.SpawnSpaceStationTBD, 1, ItemApplicationEnum.CustomActivation);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
    }


    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return false;
        }
        return true;
    }
}