package game.items.items;

import game.gameobjects.GameObject;
import game.gameobjects.player.PlayerStats;
import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;

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
