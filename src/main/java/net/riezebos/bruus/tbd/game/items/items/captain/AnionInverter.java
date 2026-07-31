package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class AnionInverter extends Item {
    public static float damageModifier = 0.75f;
    public static float cooldownModifier = -0.6f;
    public static float scaleBonus = 0.2f;

    public AnionInverter() {
        super(ItemEnums.AnionInverter, 1, ItemApplicationEnum.ApplyOnSpaceShipCreation);
    }


    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject target) {
        if(target instanceof SpaceShip spaceShip){
            spaceShip.modifySpecialAttackDamageModifier(damageModifier);
            spaceShip.modifySpecialAttackRechargeCooldown(cooldownModifier);
        }
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(!PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain)){
            return false;
        }
        
        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null || PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.AnionInverter) != null){
            return false;
        }

        return true;
    }
}