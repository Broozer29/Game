package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class FuelCannister extends Item {

    public static float bonusFuelMultiplier = 0.25f;

    public FuelCannister() {
        super(ItemEnums.FuelCannister, 1, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if(gameObject instanceof SpaceShip spaceShip){
            spaceShip.modifyFuelCannisterMaxCapacityModifier(this.quantity * bonusFuelMultiplier);
            spaceShip.modifyFuelCannisterRegenMultiplier(this.quantity * bonusFuelMultiplier);
        }

    }

    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter);
    }

}
