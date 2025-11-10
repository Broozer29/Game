package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class EternaBurn extends Item {

    private boolean shouldApply;
    public static float fuelUsagereduction = 0.15f; //15%
    public static float igniteDamageReduction = 0.25f; //25%

    public EternaBurn(){
        super(ItemEnums.EternaFlame, 1,  ItemApplicationEnum.UponAcquiring);
        shouldApply = true;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject){
        if(shouldApply) {
            PlayerStats.getInstance().modifyFuelCannisterUsageMultiplier(-(this.quantity * fuelUsagereduction));
            PlayerStats.getInstance().modifyIgniteItemDamageMultiplier(-(this.quantity * igniteDamageReduction));
            shouldApply = false;
        }
    }

    private void removeEffect(){
        if(quantity > 0) {
            PlayerStats.getInstance().modifyFuelCannisterUsageMultiplier(this.quantity * fuelUsagereduction);
            PlayerStats.getInstance().modifyIgniteItemDamageMultiplier(this.quantity * igniteDamageReduction);
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