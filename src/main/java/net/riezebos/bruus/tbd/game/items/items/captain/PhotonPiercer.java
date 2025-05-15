package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class PhotonPiercer extends Item {

    public static float damageAmplificationModifier = 1;
    public static float hpRequirement = 0.9f;

    public PhotonPiercer () {
        super(ItemEnums.PhotonPiercer, 1, ItemApplicationEnum.BeforeCollision);
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
        if (target.getCurrentHitpoints() >= hpRequirement * target.getMaxHitPoints()) {
            attack.modifyBonusDamageMultiplier(damageAmplificationModifier * quantity);
        }
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain);
    }
}
