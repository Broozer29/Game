package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class BarrierBooster extends Item {
    public static float modifierBonus = 0.2f;

    public BarrierBooster(){
        super(ItemEnums.BarrierBooster, 1, ItemApplicationEnum.ApplyOnSpaceShipCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(gameObject instanceof SpaceShip spaceShip) {
            spaceShip.setMaxShieldPoints(spaceShip.getMaxShieldPoints() * (1 + modifierBonus * quantity));
        }
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return true;
    }
}
