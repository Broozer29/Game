package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.items.util.ContractCounter;
import net.riezebos.bruus.tbd.game.items.items.util.ContractHelper;

public class Contract extends Item{

    public Contract () {
        super(ItemEnums.Contract, 1, ItemApplicationEnum.UponAcquiring);
        //Dont add the contract to the helper here, since this will mess with the "isAvailable" method handling
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        ContractCounter contractCounter = new ContractCounter();
        ContractHelper.getInstance().addContract(contractCounter);
    }


    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
//        applyEffectToObject(null);
    }

    //Only needed for the Contract item so far
    public void subtractItem(int amount){
        this.quantity -= amount;
        if(this.quantity <= 0){
            PlayerInventory.getInstance().removeItemFromInventory(this.itemEnum);
        }
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return true;
    }

}
