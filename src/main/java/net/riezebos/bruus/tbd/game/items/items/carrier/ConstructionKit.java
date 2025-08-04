package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ConstructionKit extends Item {

    private boolean shouldApply;
    public static float additionalConstructionSpeed = 0.4f;

    public ConstructionKit() {
        super(ItemEnums.ConstructionKit, 1, ItemApplicationEnum.UponAcquiring);
        shouldApply = true;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if (shouldApply) {
            PlayerStats.getInstance().modifyConstructionBonusSpeedModifier(this.quantity * additionalConstructionSpeed);
            shouldApply = false;
        }
    }

    private void removeEffect() {
        if (quantity > 0) {
            PlayerStats.getInstance().modifyConstructionBonusSpeedModifier(-(this.quantity * additionalConstructionSpeed));
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
