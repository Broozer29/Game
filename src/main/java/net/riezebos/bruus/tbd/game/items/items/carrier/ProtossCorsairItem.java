package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ProtossCorsairItem extends Item {
    public static int maxDamageBase = 100;
    public static int maxDamageIncrease = 50;

    public ProtossCorsairItem(){
        super(ItemEnums.ProtossCorsair, 1,  ItemApplicationEnum.ApplyOnCreation);
    }
    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerManager.getInstance().getAllSpaceShips().forEach(spaceShip -> spaceShip.setCorsairCount(1));
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        applyEffectToObject(null);
    }

    public float getMaxDamage(GameObject target) {
        return Math.min(target.getMaxHitPoints() * 0.5f, (maxDamageBase + this.quantity * maxDamageIncrease));
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
