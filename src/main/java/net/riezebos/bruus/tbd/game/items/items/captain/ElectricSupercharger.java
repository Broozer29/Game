package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ElectricSupercharger extends Item {
    public static float buffAmount = 3f;

    public ElectricSupercharger() {
        super(ItemEnums.ElectricSupercharger, 1, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if (quantity >= 1) {
            PlayerStats.getInstance().setHasImprovedElectroShred(true);
        }
        if (gameObject instanceof SpaceShip spaceShip) {
            spaceShip.modifySpecialAttackDamageModifier(quantity * buffAmount);
        }
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain);
    }
}
