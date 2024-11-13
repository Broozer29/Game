package net.riezebos.bruus.tbd.game.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

public abstract class Item implements ItemInterface{

    protected ItemEnums itemName;
    protected int quantity;
    protected ItemApplicationEnum applicationMethod;

    public Item(ItemEnums itemEnum, int quantity, ItemApplicationEnum applicationMethod){
        this.itemName = itemEnum;
        this.quantity = quantity;
        this.applicationMethod = applicationMethod;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Applies an effect to an object. It often implements the effect interface.
    }

    @Override
    public void modifyAttackingObject (GameObject applier, GameObject target){
        //Should be used to modify the applier, depending on certain conditions
    };

    public void applyEffectToObject (GameObject applier, GameObject target) {
        //Applies an effect to an object, with the applier provided for certain conditions
    }

    public ItemEnums getItemName () {
        return itemName;
    }

    public void setItemName (ItemEnums itemName) {
        this.itemName = itemName;
    }

    public int getQuantity () {
        return quantity;
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }


    public ItemApplicationEnum getApplicationMethod () {
        return applicationMethod;
    }

    public void setApplicationMethod (ItemApplicationEnum applicationMethod) {
        this.applicationMethod = applicationMethod;
    }


}
