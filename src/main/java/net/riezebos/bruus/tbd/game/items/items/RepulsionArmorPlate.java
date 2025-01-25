package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;

public class RepulsionArmorPlate extends Item {

    private float armorAmount;

    public RepulsionArmorPlate(){
        super(ItemEnums.RepulsionArmorPlate, 1, ItemApplicationEnum.ApplyOnCreation);
        armorAmount = 10;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        gameObject.adjustArmorBonus(this.quantity  * armorAmount);
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return true;
    }
}
