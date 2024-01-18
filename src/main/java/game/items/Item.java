package game.items;

import game.items.effects.EffectActivationTypes;
import game.objects.GameObject;

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

    }

    @Override
    public void triggerEffectForOneTimeEffects (GameObject gameObject) {

    }

    @Override
    public void modifyAttackValues (GameObject attack, GameObject target){

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
