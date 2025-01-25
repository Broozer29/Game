package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;

public class EntanglingFlames extends Item {

    public EntanglingFlames() {
        super(ItemEnums.EntanglingFlames, 1, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Doesn't do anything, the Damage over time effect should check if this exists and then handle it
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public boolean isAvailable () {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter);
    }
}
