package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class FuelCannister extends Item {

    private boolean shouldApply;
    private float bonusDamageMultiplier = 0.25f;

    public FuelCannister(){
        super(ItemEnums.FuelCannister, 1,  ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject){
        if(shouldApply) {
            PlayerStats.getInstance().modifyFuelCannisterMultiplier(this.quantity * bonusDamageMultiplier);
            PlayerStats.getInstance().modifyFuelCannisterRegenMultiplier(this.quantity * bonusDamageMultiplier);
            shouldApply = false;
        }
    }

    private void removeEffect(){
        if(quantity > 0) {
            PlayerStats.getInstance().modifyFuelCannisterMultiplier(this.quantity * bonusDamageMultiplier);
            PlayerStats.getInstance().modifyFuelCannisterRegenMultiplier(this.quantity * bonusDamageMultiplier);
        }
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter);
    }

}
