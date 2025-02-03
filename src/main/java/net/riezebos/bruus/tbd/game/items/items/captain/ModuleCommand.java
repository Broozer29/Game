package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;

public class ModuleCommand extends Item {

    public ModuleCommand () {
        super(ItemEnums.ModuleCommand, 1, ItemApplicationEnum.UponAcquiring);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().setMaximumAmountOfDrones(12);
    }

    @Override
    public boolean isAvailable () {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain) && PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) == null;
    }
}
