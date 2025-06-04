package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class EscalatingFlames extends Item {
    public EscalatingFlames() {
        super(ItemEnums.EscalatingFlames, 1, ItemApplicationEnum.UponAcquiring);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(this.quantity > 0) {
            PlayerStats.getInstance().modifyMaxIgniteStacks(this.quantity);
        }
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    private void removeEffect () {
        if(this.quantity > 0) {
            PlayerStats.getInstance().modifyMaxIgniteStacks(-this.quantity);
        }
    }


    @Override
    public boolean isAvailable () {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter);
    }
}