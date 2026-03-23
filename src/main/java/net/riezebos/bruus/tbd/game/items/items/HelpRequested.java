package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class HelpRequested extends Item {

    public static float additionalKillsRequiredModifier = 2;
    public HelpRequested() {
        super(ItemEnums.HelpRequested, 1, ItemApplicationEnum.UponAcquiring);
    }


    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        Contract.killCountRequired = (int) (Contract.killCountRequired * (1 + additionalKillsRequiredModifier));
        //Logic is applied in shopboardcreator when items are created
    }


    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return false;
        }

        return true;
    }
}
