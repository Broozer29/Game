package net.riezebos.bruus.tbd.game.items.items.firefighter;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class BargainBucket extends Item {
    public BargainBucket () {
        super(ItemEnums.BargainBucket, 1, ItemApplicationEnum.UponAcquiring);
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        //activated after purchase anyway
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerInventory.getInstance().addItem(ItemEnums.StickyOil);
        PlayerInventory.getInstance().addItem(ItemEnums.ScorchingFury);
        PlayerInventory.getInstance().addItem(ItemEnums.EscalatingFlames);

        PlayerInventory.getInstance().removeItemFromInventory(this.itemEnum);
    }

    private void removeEffect () {
        //Not needed
    }


    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter);
    }
}
