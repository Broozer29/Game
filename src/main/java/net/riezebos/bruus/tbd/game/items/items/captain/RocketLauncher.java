package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class RocketLauncher extends Item {
    public static float rocketChance = 0.10f;
    public static float damagePerStack = 1f;

    public RocketLauncher() {
        super(ItemEnums.RocketLauncher, 1, ItemApplicationEnum.ApplyOnSpaceShipCreation);
    }

    @Override
    public void applyEffectToObject (GameObject target) {
        //The drones read the quantity of this item whenever they fire
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

        if(!PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain)){
            return false;
        }

        return true;
    }
}
