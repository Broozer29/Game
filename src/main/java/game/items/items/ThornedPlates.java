package game.items.items;

import game.gameobjects.GameObject;
import game.gameobjects.player.PlayerStats;
import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;

public class ThornedPlates extends Item {


    private float currentlyAppliedAmount = 0.0f;
    private float buffAmount = 0.10f;

    public ThornedPlates(){
        super(ItemEnums.ThornedPlates, 1, ItemApplicationEnum.ApplyOnCreation);
    }

    private void setCurrentlyAppliedAmount(float amount){
        this.currentlyAppliedAmount = amount;
    }

    private void removeCurrentlyAppliedAmount(){
        PlayerStats.getInstance().modifyThornsDamageRatio(-this.currentlyAppliedAmount);
    }

    private void addAppliedAmount(float amount){
        PlayerStats.getInstance().modifyThornsDamageRatio(amount);
        setCurrentlyAppliedAmount(amount);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        removeCurrentlyAppliedAmount();
        float ratio = quantity * buffAmount;
        addAppliedAmount(ratio);
        setCurrentlyAppliedAmount(ratio);
    }

}
