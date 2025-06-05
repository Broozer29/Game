package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

import java.util.Random;

public class ReflectiveShielding extends Item {
    public static float buffAmount = 2f;
    public static float procChance = 0.25f;
    public static float procChanceIncrease = 0.05f;
    private boolean shouldApply;
    private static Random random = new Random();

    public ReflectiveShielding() {
        super(ItemEnums.ReflectiveShielding, 1, ItemApplicationEnum.ApplyOnCreation);
        shouldApply = true;
    }


    private void removeEffect () {
        PlayerStats.getInstance().setHasThornsEnabled(false);
        PlayerStats.getInstance().modifyThornsDamageRatio(-(quantity * buffAmount));
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if (shouldApply) {
            shouldApply = false;
            PlayerStats.getInstance().setHasThornsEnabled(true);
            PlayerStats.getInstance().modifyThornsDamageRatio(quantity * buffAmount);
        }

    }

    public boolean attemptToReflectMissile(GameObject gameObject){
        if(gameObject instanceof Missile missile){
            if(missile.isExplosive()){
                return false;
            }
            else {
                return random.nextFloat() < (procChance + (procChanceIncrease * quantity));
            }
        }
        return false; //if it aint a non-explosive missile, we cant reflect it

    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return true;
    }
}
