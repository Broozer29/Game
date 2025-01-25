package net.riezebos.bruus.tbd.game.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public abstract class Item implements ItemInterface{

    protected ItemEnums itemEnum;
    protected int quantity;
    protected ItemApplicationEnum applicationMethod;

    public Item(ItemEnums itemEnum, int quantity, ItemApplicationEnum applicationMethod){
        this.itemEnum = itemEnum;
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

    public ItemEnums getItemEnum () {
        return itemEnum;
    }

    public void setItemEnum (ItemEnums itemEnum) {
        this.itemEnum = itemEnum;
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
