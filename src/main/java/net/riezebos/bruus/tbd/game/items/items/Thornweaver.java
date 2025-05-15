package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class Thornweaver extends Item {
    private boolean shouldApply;
    public static float collisionDamageReduction = 0.9f;
    public Thornweaver(){
        super(ItemEnums.Thornweaver, 1, ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(shouldApply) {
            PlayerStats.getInstance().setHasThornsEnabled(true);
            PlayerStats.getInstance().modifyCollisionDamageReduction(collisionDamageReduction);
            shouldApply = false;
        }
    }

    private void removeEffect(){
        if(quantity > 0) {
            PlayerStats.getInstance().setHasThornsEnabled(false);
            PlayerStats.getInstance().modifyCollisionDamageReduction(-collisionDamageReduction);
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
        return PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.Thornweaver) == null;
    }
}
