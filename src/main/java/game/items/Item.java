package game.items;

import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.gameobjects.GameObject;

public abstract class Item implements ItemInterface{

    protected ItemEnums itemName;
    protected int quantity;
    protected EffectActivationTypes effectType;
    protected ItemApplicationEnum applicationMethod;

    public Item (ItemEnums itemEnum, int quantity, EffectActivationTypes effectType, ItemApplicationEnum applicationMethod) {
        this.itemName = itemEnum;
        this.quantity = quantity;
        this.effectType = effectType;
        this.applicationMethod = applicationMethod;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Applies an effect to an object. It often implements the effect interface.
    }

    @Override
    public void applyEffectToObject (GameObject applier, GameObject target){
        //Modifies the ATTACK damage done to TARGET
    };

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

    public EffectActivationTypes getEffectType () {
        return effectType;
    }

    public void setEffectType (EffectActivationTypes effectType) {
        this.effectType = effectType;
    }

    public ItemApplicationEnum getApplicationMethod () {
        return applicationMethod;
    }

    public void setApplicationMethod (ItemApplicationEnum applicationMethod) {
        this.applicationMethod = applicationMethod;
    }


}
