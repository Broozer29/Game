package net.riezebos.bruus.tbd.game.items.items.mutalisk;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class FragmentationSacs  extends Item {

    public static int bonusBileBits = 3;

    public FragmentationSacs(){
        super(ItemEnums.MutaliskAttackSpeed, 1,  ItemApplicationEnum.ApplyOnSpaceShipCreation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject){
        if(gameObject instanceof SpaceShip spaceShip){
            spaceShip.addBonusBileBits(this.quantity * bonusBileBits);
        }
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Mutalisk);
    }

}
