package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class SideCannons extends Item {

    public static int additionalMissiles = 2;
    public SideCannons () {
        super(ItemEnums.SideCannons, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
    }

    @Override
    public boolean isAvailable () {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain) && PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) == null;
    }
}
