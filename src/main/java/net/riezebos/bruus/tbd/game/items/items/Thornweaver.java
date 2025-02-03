package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;

public class Thornweaver extends Item {
    private boolean shouldApply;
    public Thornweaver(){
        super(ItemEnums.Thornweaver, 1, ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(shouldApply) {
            PlayerStats.getInstance().modifyCollisionDamageReduction(0.5f);
            shouldApply = false;
        }
    }

    private void removeEffect(){
        if(quantity > 0) {
            PlayerStats.getInstance().modifyAttackSpeedBonus(-0.5f);
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
        boolean available = false;
        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.Thornweaver) == null){
            available = true;
        }
        return available;
    }
}
