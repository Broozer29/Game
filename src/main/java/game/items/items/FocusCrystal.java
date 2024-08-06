package game.items.items;

import game.items.Item;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.gameobjects.GameObject;

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
        damageAmplificationModifier = (quantity * 0.20f);
    }

    //Not used
    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Not needed
    }



    public void applyEffectToObject (GameObject attack, GameObject target) {

        GameObject ownerOrCreator = attack.getOwnerOrCreator();
        int shooterXCoordinate = ownerOrCreator.getCenterXCoordinate();
        int shooterYCoordinate = ownerOrCreator.getCenterYCoordinate();

        int targetXCoordinate = target.getCenterXCoordinate();
        int targetYCoordinate = target.getCenterYCoordinate();

        if (Math.abs(shooterXCoordinate - targetXCoordinate) < distance &&
                Math.abs(shooterYCoordinate - targetYCoordinate) < distance) {
            // Amplify the attack damage
            attack.modifyBonusDamageMultiplier(damageAmplificationModifier);
        }


    }

    public int getDistance () {
        return distance;
    }

}
