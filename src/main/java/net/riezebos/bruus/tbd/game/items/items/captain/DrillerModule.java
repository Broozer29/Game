package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;

public class DrillerModule extends Item {
    private boolean shouldApply;

    public DrillerModule(){
        super(ItemEnums.DrillerModule, 1, ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(shouldApply){
            PlayerStats.getInstance().addPiercingMissilesAmount(this.quantity);
            shouldApply = false;
        }
    }

    private void removeEffect(){
        if(quantity > 0){
            PlayerStats.getInstance().addPiercingMissilesAmount(-this.quantity);
        }
    }

    public void increaseQuantityOfItem(int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain);
    }
}
