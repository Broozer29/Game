package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class BouncingModuleAddon extends Item {

    public static float bonusDamagePercentage = 0.25f;

    public BouncingModuleAddon () {
        super(ItemEnums.BouncingModuleAddon, 1, ItemApplicationEnum.BeforeCollision);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
    }

    @Override
    public void modifyAttackingObject (GameObject applier, GameObject target) {
        if (applier instanceof Missile missile) {
            if (missile.isPiercesThroughObjects() && !missile.hasCollidedBeforeWith(target) && missile.getAmountOfPiercesLeft() < missile.getMaximumAmountOfPierces()) {
                applier.modifyBonusDamageMultiplier(quantity * bonusDamagePercentage);
            }
        }
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isAvailable(){
        boolean isAvailable = false;
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain) && PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) == null){
            isAvailable = true;
        }

        return isAvailable;
    }
}
