package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ShieldStabilizer extends Item {
    public ShieldStabilizer () {
        super(ItemEnums.ShieldStabilizer, 1, ItemApplicationEnum.UponAcquiring);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().setContinueShieldRegenThroughDamage(true);
        PlayerStats.getInstance().modifyShieldRegenMultiplier(-0.5f);
    }

    @Override
    public boolean isAvailable () {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        boolean available = false;
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ShieldStabilizer) == null) {
            available = true;
        }

        return available;
    }
}
