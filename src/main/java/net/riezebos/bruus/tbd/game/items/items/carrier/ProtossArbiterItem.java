package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ProtossArbiterItem extends Item {

    public static float healingBonusMultiplier = 0.2f;
    private boolean shouldApply;

    public ProtossArbiterItem(){
        super(ItemEnums.ProtossArbiter, 1,  ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().setAmountOfProtossArbiters(1);

        if(shouldApply) {
            PlayerStats.getInstance().modifyArbiterHealingMultiplier(this.quantity * healingBonusMultiplier);
            shouldApply = false;
        }
    }

    public void increaseQuantityOfItem(int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    private void removeEffect(){
        if(quantity > 0) {
            PlayerStats.getInstance().modifyArbiterHealingMultiplier(-(this.quantity * healingBonusMultiplier));
            PlayerStats.getInstance().setAmountOfProtossArbiters(0);
        }
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        if(!PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)){
            return false;
        }

        return true;
    }
}
