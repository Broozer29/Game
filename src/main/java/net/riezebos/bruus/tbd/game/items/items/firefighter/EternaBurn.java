package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class EternaBurn extends Item {

    public static float fuelUsagereduction = 0.15f; //15%
    public static float igniteDamageReduction = 0.25f; //25%

    public EternaBurn(){
        super(ItemEnums.EternaFlame, 1,  ItemApplicationEnum.UponAcquiring);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if (gameObject instanceof SpaceShip spaceship) {
            spaceship.modifyFuelCannisterUsageMultiplier(-(this.quantity * fuelUsagereduction));
            spaceship.modifyIgniteDamageModifier(-(this.quantity * igniteDamageReduction));
        }
    }


    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter);
    }

}