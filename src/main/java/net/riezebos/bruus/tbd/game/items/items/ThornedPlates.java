package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ThornedPlates extends Item {
    public static float buffAmount = 0.20f;
    public static int armorAmount = 7;
    private boolean shouldApply;

    public ThornedPlates () {
        super(ItemEnums.ThornedPlates, 1, ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }


    private void removeEffect () {
        PlayerStats.getInstance().setHasThornsEnabled(false);
        PlayerStats.getInstance().modifyThornsDamageRatio(-(quantity * buffAmount));
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if (shouldApply) {
            shouldApply = false;
            PlayerStats.getInstance().setHasThornsEnabled(true);
            PlayerStats.getInstance().modifyThornsDamageRatio(quantity * buffAmount);
        }

        //Armor bonus applied to the spaceship that gets re-created at the start of the round, thus outside of the if-statement
        if(gameObject != null) {
            gameObject.adjustArmorBonus(this.quantity * armorAmount);
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
