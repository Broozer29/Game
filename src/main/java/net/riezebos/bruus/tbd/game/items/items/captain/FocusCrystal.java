package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class FocusCrystal extends Item {

    public static float damageAmplificationModifier = 0.15f;
    private int distance = Math.round(180);

    public FocusCrystal () {
        super(ItemEnums.FocusCrystal, 1, ItemApplicationEnum.BeforeCollision);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    //Not used
    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Not needed
    }

    private boolean isValidOwner(GameObject attack, GameObject target) {
        GameObject ownerOrCreator = attack.getOwnerOrCreator();
        if (ownerOrCreator == null) {
            return false;
        }
        if (ownerOrCreator instanceof SpaceShip) {
            return true;
        }
        return (ownerOrCreator instanceof Drone drone && drone.isProtoss());
    }

    public void modifyAttackingObject (GameObject attack, GameObject target) {
        if(!isValidOwner(attack, target)){
            return;
        }

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
            attack.modifyBonusDamageMultiplier(damageAmplificationModifier * quantity);
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
