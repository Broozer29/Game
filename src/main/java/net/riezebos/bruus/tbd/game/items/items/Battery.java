package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class Battery extends Item {

    public static float cooldownReduction = 0.05f;
    public Battery () {
        super(ItemEnums.Battery, 1, ItemApplicationEnum.UponAcquiring);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().setMaxSpecialAttackCharges(1 + quantity);
        PlayerStats.getInstance().modifySpecialAttackRechargeCooldown(-(this.quantity * cooldownReduction));
    }

    public void increaseQuantityOfItem(int amount) {
        removeEffect();
        this.quantity += amount;
    }

    private void removeEffect(){
        if(this.quantity > 0){
            PlayerStats.getInstance().modifySpecialAttackRechargeCooldown(this.quantity * cooldownReduction);
        }
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)){
            return false;
        }
        return true;
    }

}
