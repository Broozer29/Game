package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class CriticalOverloadCapacitor extends Item {
    public static float damageMultiplier = 0.5f;

    public CriticalOverloadCapacitor () {
        super(ItemEnums.CriticalOverloadCapacitor, 1, ItemApplicationEnum.ApplyOnSpaceShipCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(gameObject instanceof SpaceShip spaceShip){
            spaceShip.modifyCritDamageModifier(this.quantity * damageMultiplier);
        }
    }


    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Mutalisk)){
            return false;
        }


        return true;
    }

}

