package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ScorchingFury extends Item {

    private boolean shouldApply;
    public static float bonusDamageMultiplier = 0.15f;

    public ScorchingFury(){
        super(ItemEnums.ScorchingFury, 1,  ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject){
        if(shouldApply) {
            PlayerStats.getInstance().modifyIgniteItemDamageMultiplier(this.quantity * bonusDamageMultiplier);
            shouldApply = false;
        }
    }

    private void removeEffect(){
        if(quantity > 0) {
            PlayerStats.getInstance().modifyIgniteItemDamageMultiplier(-(this.quantity * bonusDamageMultiplier));
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
