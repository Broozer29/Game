package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.FlameThrower;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class InfernalPreIgniter extends Item {

    public static float scalingFactor = 0.005f;
    public InfernalPreIgniter () {
        super(ItemEnums.InfernalPreIgniter, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(gameObject instanceof FlameThrower flameThrower){
            if(flameThrower.getOwnerOrCreator().equals(PlayerManager.getInstance().getSpaceship())){
                flameThrower.setDamage(flameThrower.getDamage() * (1 + scalingFactor * quantity));
            }
        }
    }

    private void removeEffect () {
        //shouldnt be needed as flamethrowers dissipate and have to be recreated, but a reason might envelop later on
    }


    @Override
    public boolean isAvailable(){
        boolean isAvailable = false;
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter)){
            isAvailable = true;
        }

        return isAvailable;
    }
}
