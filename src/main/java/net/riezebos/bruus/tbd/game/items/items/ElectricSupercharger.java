package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

public class ElectricSupercharger extends Item {
    private float buffAmount = 0.2f;
    private float currentlyAppliedAmount = 0;
    private boolean shouldApply;
    public ElectricSupercharger () {
        super(ItemEnums.ElectricSupercharger, 1, ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        removeCurrentlyAppliedAmount();
        this.quantity += amount;
        shouldApply = true;
        applyEffectToObject(null);
    }

    private void setCurrentlyAppliedAmount(float amount){
        this.currentlyAppliedAmount = amount;
    }

    private void removeCurrentlyAppliedAmount(){
        if(quantity > 0) {
            PlayerStats.getInstance().modifySpecialBonusDamageMultiplier(-this.currentlyAppliedAmount);
        }
    }

    private void addAppliedAmount(float amount){
            PlayerStats.getInstance().modifySpecialBonusDamageMultiplier(amount);
            setCurrentlyAppliedAmount(amount);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(shouldApply) {
            if (quantity >= 1) {
                PlayerStats.getInstance().setHasImprovedElectroShred(true);
            }
            float ratio = quantity * buffAmount;
            addAppliedAmount(ratio);
            setCurrentlyAppliedAmount(ratio);
            shouldApply = false;
        }
    }
}
