package game.items.items;

import game.items.Item;
import game.items.ItemApplicationEnum;
import game.items.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.objects.GameObject;

public class PhotonPiercer extends Item {

    private float damageAmplificationModifier;

    public PhotonPiercer () {
        super(ItemEnums.PhotonPiercer, 1, EffectActivationTypes.DamageModification, ItemApplicationEnum.BeforeCollision);
        calculateDamageAmplificationModifier();
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        calculateDamageAmplificationModifier();
    }

    private void calculateDamageAmplificationModifier(){
        damageAmplificationModifier = 1 + (quantity * 0.75f);
    }

    //Not used
    @Override
    public void applyEffectToObject (GameObject gameObject) {
    }

    @Override
    public void modifyAttackValues (GameObject attack, GameObject target) {
        // Check if the current hit points are 90% or more of the maximum hit points
        if (target.getCurrentHitpoints() >= 0.9 * target.getMaxHitPoints()) {
            attack.setDamage(attack.getDamage() * damageAmplificationModifier);
        }
    }

}
