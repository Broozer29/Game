package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

import java.util.Random;

public class WisdomBall extends Item {

    public static float procChance = 0.2f;
    public static float currentBonusChance = 0.0f;

    public WisdomBall() {
        super(ItemEnums.WisdomBall, 1, ItemApplicationEnum.CustomActivation);
    }


    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        //shouldn't do anything here, handled in WisdomBallRollManager
    }

    public boolean shouldActivate(){
        Random random = new Random();
        float chance = random.nextFloat();
        procChance = 0.2f;

        if(chance < (procChance + currentBonusChance)){
            currentBonusChance = 0;
            return true;
        }

        currentBonusChance += 0.175f;
        return false;
    }


    public static void setCurrentBonusChance(float currentBonusChance) {
        WisdomBall.currentBonusChance = currentBonusChance;
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null) {
            return false;
        }

        return true;
    }
}
