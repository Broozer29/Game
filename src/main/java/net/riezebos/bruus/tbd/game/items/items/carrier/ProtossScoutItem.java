package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ProtossScoutItem extends Item {

    public ProtossScoutItem(){
        super(ItemEnums.ProtossScout, 1,  ItemApplicationEnum.ApplyOnCreation);
    }


    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().setAmountOfProtossScouts(PlayerStats.getInstance().getCarrierStartingScouts() + quantity);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        if(!PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)){
            return false;
        }

        return ProtossUtils.canHostMoreProtoss();
    }
}
