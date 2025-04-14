package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ProtossThorns extends Item {
    public static float thornsBonusDamageRatio = 0.2f;

    public ProtossThorns() {
        super(ItemEnums.ProtossThorns, 1, ItemApplicationEnum.UponAcquiring);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if (quantity > 0) {
            PlayerStats.getInstance().modifyProtossShipThornsDamageRatio((quantity * thornsBonusDamageRatio));
        }
    }

    @Override
    public void increaseQuantityOfItem(int amount) {
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    private void removeEffect() {
        if (quantity > 0) {
            PlayerStats.getInstance().modifyProtossShipThornsDamageRatio(-(quantity * thornsBonusDamageRatio));
        }
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier);
    }
}