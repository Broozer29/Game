package game.items.items;

import game.items.Item;
import game.items.ItemApplicationEnum;
import game.items.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.movement.Point;
import game.objects.GameObject;

public class FocusCrystal extends Item {

    private float damageAmplificationModifier;
    private int distance = 200;

    public FocusCrystal () {
        super(ItemEnums.FocusCrystal, 1, EffectActivationTypes.DamageModification, ItemApplicationEnum.BeforeCollision);
        calculateDamageAmplificationModifier();
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        calculateDamageAmplificationModifier();
    }

    private void calculateDamageAmplificationModifier(){
        damageAmplificationModifier = 1 + (quantity * 0.20f);
    }

    //Not used
    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Not needed
    }

    @Override
    public void modifyAttackValues (GameObject attack, GameObject target) {

        int shooterXCoordinate = attack.getOwnerOrCreator().getCenterXCoordinate();
        int shooterYCoordinate = attack.getOwnerOrCreator().getCenterYCoordinate();

        int targetXCoordinate = target.getCenterXCoordinate();
        int targetYCoordinate = target.getCenterYCoordinate();

        if (Math.abs(shooterXCoordinate - targetXCoordinate) < distance &&
                Math.abs(shooterYCoordinate - targetYCoordinate) < distance) {
            // Amplify the attack damage
            attack.setDamage(attack.getDamage() * damageAmplificationModifier);
        }
    }

    public int getDistance () {
        return distance;
    }

}
