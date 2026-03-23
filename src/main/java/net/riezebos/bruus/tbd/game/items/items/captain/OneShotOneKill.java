package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class OneShotOneKill extends Item {

    public static float damageAmplificationModifier = 2f;
    public static float hpRequirement = 1;

    public OneShotOneKill () {
        super(ItemEnums.OneShotOneKill, 1, ItemApplicationEnum.BeforeCollision);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    //Not used
    @Override
    public void applyEffectToObject (GameObject gameObject) {
    }

    @Override
    public void modifyAttackingObject (GameObject attack, GameObject target) {
        // Check if the current hit points are 90% or more of the maximum hit points
        if (attack instanceof Missile && target.getCurrentHitpoints() >= target.getMaxHitPoints() - 1) { //-1 leeway to guarantee as these are floats, not integers
            attack.modifyBonusDamageMultiplier(damageAmplificationModifier * quantity);

            if(attack instanceof Missile missile){
                missile.setIsACrit(true);
            } else if(attack instanceof SpecialAttack specialAttack){
                specialAttack.setIsACrit(true);
            }
        }
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain);
    }
}
