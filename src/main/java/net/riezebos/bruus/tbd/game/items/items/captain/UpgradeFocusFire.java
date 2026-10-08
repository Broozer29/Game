package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class UpgradeFocusFire extends Item {

    public UpgradeFocusFire() {
        super(ItemEnums.UpgradeFocusFire, 1, ItemApplicationEnum.AfterCollision);
    }


    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject applier, GameObject target) {
        if(applier.getOwnerOrCreator() instanceof SpaceShip spaceShip && applier instanceof Missile){
            FriendlyManager.getInstance().getAllPlayerDrones(spaceShip).forEach(drone -> drone.fireAction(target));
        }
    }


    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)){
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleScorch) != null){
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleAccuracy) != null){
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleCommand) != null){
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return false;
        }

        if(GameState.getInstance().getStagesCompleted() == 0){
            return false;
        }

        return true;
    }
}