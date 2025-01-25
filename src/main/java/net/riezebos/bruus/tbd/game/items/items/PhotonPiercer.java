package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;

public class PhotonPiercer extends Item {

    private float damageAmplificationModifier;

    public PhotonPiercer () {
        super(ItemEnums.PhotonPiercer, 1, ItemApplicationEnum.BeforeCollision);
        calculateDamageAmplificationModifier();
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        calculateDamageAmplificationModifier();
    }

    private void calculateDamageAmplificationModifier(){
        damageAmplificationModifier = (quantity * 1f);
    }

    //Not used
    @Override
    public void applyEffectToObject (GameObject gameObject) {
    }

    @Override
    public void modifyAttackingObject (GameObject attack, GameObject target) {
        // Check if the current hit points are 90% or more of the maximum hit points
        if (target.getCurrentHitpoints() >= 0.9 * target.getMaxHitPoints()) {
            attack.modifyBonusDamageMultiplier(damageAmplificationModifier);
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
