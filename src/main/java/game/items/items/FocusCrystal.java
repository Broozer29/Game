package game.items.items;

import game.items.Item;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.gameobjects.GameObject;

public class FocusCrystal extends Item {

    private float damageAmplificationModifier;
    private int distance = 180;

    public FocusCrystal () {
        super(ItemEnums.FocusCrystal, 1, ItemApplicationEnum.BeforeCollision);
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



    public void applyEffectToObject(GameObject attack, GameObject target) {
        GameObject ownerOrCreator = attack.getOwnerOrCreator();
        int shooterXCoordinate = ownerOrCreator.getCenterXCoordinate();
        int shooterYCoordinate = ownerOrCreator.getCenterYCoordinate();

        // Get target's bounding box
        int targetXMin = target.getXCoordinate(); // Left edge
        int targetYMin = target.getYCoordinate(); // Top edge
        int targetXMax = targetXMin + target.getWidth(); // Right edge
        int targetYMax = targetYMin + target.getHeight(); // Bottom edge

        // Define the square range bounds
        int rangeMinX = shooterXCoordinate - distance;
        int rangeMaxX = shooterXCoordinate + distance;
        int rangeMinY = shooterYCoordinate - distance;
        int rangeMaxY = shooterYCoordinate + distance;

        // Check if the target's bounding box intersects with the square range
        boolean isWithinRange = (targetXMax >= rangeMinX && targetXMin <= rangeMaxX) &&
                (targetYMax >= rangeMinY && targetYMin <= rangeMaxY);

        if (isWithinRange) {
            // Amplify the attack damage
            attack.modifyBonusDamageMultiplier(damageAmplificationModifier);
        }
    }



    public int getDistance () {
        return distance;
    }

}
