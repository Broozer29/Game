package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

public class ElectricSupercharger extends Item {
    private float buffAmount = 0.2f;
    private float currentlyAppliedAmount = 0;
    public ElectricSupercharger () {
        super(ItemEnums.ElectricSupercharger, 1, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += 1;
    }

    private void setCurrentlyAppliedAmount(float amount){
        this.currentlyAppliedAmount = amount;
    }

    private void removeCurrentlyAppliedAmount(){
        PlayerStats.getInstance().modifySpecialBonusDamageMultiplier(-this.currentlyAppliedAmount);
    }

    private void addAppliedAmount(float amount){
        PlayerStats.getInstance().modifySpecialBonusDamageMultiplier(amount);
        setCurrentlyAppliedAmount(amount);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(quantity >= 1){
            PlayerStats.getInstance().setHasImprovedElectroShred(true);
        }

        removeCurrentlyAppliedAmount();
        float ratio = quantity * buffAmount;
        addAppliedAmount(ratio);
        setCurrentlyAppliedAmount(ratio);
    }
}
