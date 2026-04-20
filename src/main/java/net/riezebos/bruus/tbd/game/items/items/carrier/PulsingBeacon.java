package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class PulsingBeacon extends Item {

    public static float damageModifier = 3f;
    public static float damageBonusPerCast = 0.2f;
    public static float cooldown = 1f;

    public PulsingBeacon() {
        super(ItemEnums.PulsingBeacon, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        //handled in CarrierBeacon class
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

        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier);
    }
}
