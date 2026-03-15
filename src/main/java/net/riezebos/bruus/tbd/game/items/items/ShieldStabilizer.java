package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ShieldStabilizer extends Item {
    public static float shieldRegenMultiplier = 0.65f;
    public ShieldStabilizer () {
        super(ItemEnums.ShieldStabilizer, 1, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(gameObject instanceof SpaceShip spaceShip) {
            spaceShip.setContinueShieldRegenThroughDamage(true);
            spaceShip.modifyShieldRegenModifier(-shieldRegenMultiplier);
        }
    }

    @Override
    public boolean isAvailable () {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ShieldStabilizer) == null;
    }
}
