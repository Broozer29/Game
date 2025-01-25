package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class CorrosiveOil extends Item {


    public CorrosiveOil(){
        super(ItemEnums.CorrosiveOil, 1,  ItemApplicationEnum.UponAcquiring);
    }
    @Override
    public void applyEffectToObject(GameObject gameObject){
        //Handled in the Damage over time class
    }

    private void removeEffect(){
        //it's permanent
    }

    public int getArmorReductionPerStack (){
        return quantity;
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
//        removeEffect();
//        applyEffectToObject(null);
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter);
    }
}
