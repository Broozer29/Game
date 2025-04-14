package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

import java.util.ArrayList;
import java.util.List;

public class FocusCrystal extends Item {

    private float damageAmplificationModifier;
    private int distance = Math.round(180);

    private List<GameObject> objectsModified = new ArrayList<>();

    public FocusCrystal () {
        super(ItemEnums.FocusCrystal, 1, ItemApplicationEnum.BeforeCollision);
        calculateDamageAmplificationModifier();
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        calculateDamageAmplificationModifier();
    }

    private void calculateDamageAmplificationModifier(){
        damageAmplificationModifier = (quantity * 0.15f);
    }

    //Not used
    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Not needed
    }



    public void modifyAttackingObject (GameObject attack, GameObject target) {
        //Check if object has an owner fisrt otherwise it crashes
        GameObject ownerOrCreator = attack.getOwnerOrCreator();
        if(ownerOrCreator == null || !ownerOrCreator.equals(PlayerManager.getInstance().getSpaceship())){
            return;
        }

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

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain)){
            return true;
        }
        return false;
    }

}
