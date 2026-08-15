package net.riezebos.bruus.tbd.game.items.items.mutalisk;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ThickHide extends Item {
    public static float maxHitpointsModifier = 0.2f;

    public ThickHide(){
        super(ItemEnums.ThickHide, 1,  ItemApplicationEnum.ApplyOnSpaceShipCreation);
    }


    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerManager.getInstance().getAllSpaceShips().forEach(spaceShip -> spaceShip.setMaxHitpointsModifier(spaceShip.getMaxHitpointsModifier() + (quantity * maxHitpointsModifier)));
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        if(!PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)){
            return false;
        }

        return ProtossUtils.canHostMoreProtossToRollItems();
    }
}
