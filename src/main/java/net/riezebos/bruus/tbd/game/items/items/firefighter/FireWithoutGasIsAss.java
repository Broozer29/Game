package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class FireWithoutGasIsAss  extends Item {

    public static float reduction = 0.4f;
    public static float increase = 5;

    public FireWithoutGasIsAss() {
        super(ItemEnums.FireWithoutGasIsAss, 1, ItemApplicationEnum.UponAcquiring);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //checked in damageovertime to ensure that its a total reduction instead of flat -0.5 modifier
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    private void removeEffect () {
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter) && PlayerInventory.getInstance().getItemFromInventoryIfExists(this.getItemEnum()) == null);
    }
}