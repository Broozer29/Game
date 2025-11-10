package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.ItemInterface;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class AimAssist extends Item {
    public static float protossAttackRangeBonus = 0.20f;

    public AimAssist() {
        super(ItemEnums.AimAssist, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
    }

    public float getAttackRangeBonus(){
        return this.quantity * protossAttackRangeBonus;
    }

    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier);
    }
}
