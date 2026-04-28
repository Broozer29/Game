package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ModuleElectrify extends Item {
    public static float cooldown = 1.5f;

    public ModuleElectrify () {
        super(ItemEnums.ModuleElectrify, 1, ItemApplicationEnum.ApplyOnSpaceShipCreation);
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        //Additional stacks don't really do anything
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(gameObject instanceof SpaceShip spaceShip) {
            for(Drone drone : FriendlyManager.getInstance().getAllPlayerDrones(spaceShip)){
                drone.spawnElectroShredAttack();
            }
        }
    }


    public float getDroneSpecialScale () {
        return Math.max(0, quantity * 0.25f);
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(GameState.getInstance().getStagesCompleted() == 0){
            return false;
        }

        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain) && PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) == null){
            return true;
        }

        return false;
    }
}
