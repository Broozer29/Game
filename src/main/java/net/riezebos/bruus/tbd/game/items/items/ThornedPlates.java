package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

public class ThornedPlates extends Item {


    private float currentlyAppliedAmount = 0.0f;
    private float buffAmount = 0.20f;

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
