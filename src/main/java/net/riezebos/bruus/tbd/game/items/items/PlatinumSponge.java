package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class PlatinumSponge extends Item {
    private boolean shouldApply;
    public static float damageReduction = 1;

    public PlatinumSponge () {
        super(ItemEnums.PlatinumSponge, 1,  ItemApplicationEnum.UponAcquiring);
        shouldApply = true;
    }


    public void increaseQuantityOfItem (int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    private void removeEffect() {
        if(this.quantity > 0){
            PlayerStats.getInstance().modifyFlatDamageReduction(-(this.quantity * damageReduction));
        }
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if (shouldApply) {
            PlayerStats.getInstance().modifyFlatDamageReduction(this.quantity * damageReduction);
            shouldApply = false;
        }
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return true;
    }
}
