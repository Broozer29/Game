package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class RingOfFire extends Item {
    public static float projectileDamage = 1f;
    public static float projectileCooldown = 0.5f;

    public RingOfFire() {
        super(ItemEnums.RingOfFire, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        //handled in the FireShield class
    }

    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter) && PlayerInventory.getInstance().getItemFromInventoryIfExists(this.getItemEnum()) == null);
    }
}
