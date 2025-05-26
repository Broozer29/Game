package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class FuelCannister extends Item {

    private boolean shouldApply;
    public static float bonusFuelMultiplier = 0.25f;

    public FuelCannister(){
        super(ItemEnums.FuelCannister, 1,  ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject){
        if(shouldApply) {
            PlayerStats.getInstance().modifyFuelCannisterMultiplier(this.quantity * bonusFuelMultiplier);
            PlayerStats.getInstance().modifyFuelCannisterRegenMultiplier(this.quantity * bonusFuelMultiplier);
            shouldApply = false;
        }
    }

    private void removeEffect(){
        if(quantity > 0) {
            PlayerStats.getInstance().modifyFuelCannisterMultiplier(this.quantity * bonusFuelMultiplier);
            PlayerStats.getInstance().modifyFuelCannisterRegenMultiplier(this.quantity * bonusFuelMultiplier);
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
