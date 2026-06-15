package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class LeechingLasers extends Item {
    public static int healAmount = 2;

    public LeechingLasers() {
        super(ItemEnums.LeechingLasers, 1, ItemApplicationEnum.AfterCollision);
    }


    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public void modifyAttackingObject (GameObject applier, GameObject target){
        if(applier.getOwnerOrCreator() instanceof SpaceShip ship && applier.isFriendly()){
            ship.heal(healAmount, false);
        }
        //Should be used to modify the applier, depending on certain conditions
    };

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(!PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain)){
            return false;
        }
        return true;
    }
}