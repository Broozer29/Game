package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class PiercingMissiles extends Item {
    private boolean shouldApply;

    public PiercingMissiles() {
        super(ItemEnums.PiercingMissiles, 1, ItemApplicationEnum.UponAcquiring);
        shouldApply = true;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if (shouldApply) {
            shouldApply = false;
            PlayerStats.getInstance().modifyPiercingMissilesAmount(this.quantity);
        }
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    public void removeEffect(){
        if(this.quantity > 0){
            PlayerStats.getInstance().modifyPiercingMissilesAmount(-this.quantity);
        }
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain) || PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier);
    }
}
