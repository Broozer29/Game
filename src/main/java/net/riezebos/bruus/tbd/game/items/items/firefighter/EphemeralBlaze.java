package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class EphemeralBlaze extends Item {

    private boolean shouldApply;
    public static float primaryDamagePerIgniteStack = 0.1f; //1%
    public static float igniteDamageReduction = 0.15f; //25%

    public EphemeralBlaze(){
        super(ItemEnums.EphemeralBlaze, 1,  ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject){
        if(shouldApply) {
            PlayerStats.getInstance().modifyIgniteItemDamageMultiplier(-(this.quantity * igniteDamageReduction));
            shouldApply = false;
        }
    }

    private void removeEffect(){
        if(quantity > 0) {
            PlayerStats.getInstance().modifyIgniteItemDamageMultiplier(this.quantity * igniteDamageReduction);
        }
    }

    public float modifyDamage(float damage, int igniteStacks) {
        if(igniteStacks == 0){
            return damage;
        }

        return damage * (1 + (igniteStacks * ((1 + this.quantity) * primaryDamagePerIgniteStack)));
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