package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class HangarBayUpgrade extends Item {

    private boolean shouldApply;
    public static int additionalShipsPerItem = 6;

    public HangarBayUpgrade() {
        super(ItemEnums.HangarBayUpgrade, 1, ItemApplicationEnum.UponAcquiring);
        shouldApply = true;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if (shouldApply) {
            PlayerStats.getInstance().modifyMaxAmountOfProtoss(this.quantity * additionalShipsPerItem);
            shouldApply = false;
        }
    }

    private void removeEffect() {
        if (quantity > 0) {
            PlayerStats.getInstance().modifyMaxAmountOfProtoss(-(this.quantity * additionalShipsPerItem));
        }
    }

    @Override
    public void increaseQuantityOfItem(int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier);
    }
}