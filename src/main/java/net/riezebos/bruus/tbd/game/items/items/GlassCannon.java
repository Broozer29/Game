package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;

public class GlassCannon extends Item {
    public static float modifierBonus = 0.5f;
    public static float damageBonus = 1;
    private boolean shouldApply;

    public GlassCannon(){
        super(ItemEnums.GlassCannon, 1, ItemApplicationEnum.UponAcquiring);
        shouldApply = true;
    }

    //Activate upon purchase, it gets reset when the player dies
    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(shouldApply) {
            PlayerStats.getInstance().modifyMaxHitPointsMultiplier(-modifierBonus);
            PlayerStats.getInstance().addMaxShieldMultiplier(-modifierBonus);
            PlayerStats.getInstance().addMaxOverloadingShieldMultiplier(-modifierBonus);
            PlayerStats.getInstance().modifyBaseDamageMultiplier(damageBonus);
            shouldApply = false;
        }
    }

    public void increaseQuantityOfItem(int amount) {
        //its a relic, shouldnt do anything
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(this.itemEnum.getItemRarity().equals(ItemRarityEnums.Relic) && PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return false;
        }
        return true;
    }
}
