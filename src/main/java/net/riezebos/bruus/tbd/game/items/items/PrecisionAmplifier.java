package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

import java.util.Random;

public class PrecisionAmplifier extends Item {

    private float critChance;
    private Random random = new Random();

    public PrecisionAmplifier () {
        super(ItemEnums.PrecisionAmplifier, 1,  ItemApplicationEnum.BeforeCollision);
        calculateCritChance();
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        calculateCritChance();
    }

    private void calculateCritChance(){
        this.critChance = this.quantity * 10;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Not needed as critical strikes are no buffs/debuffs
    }

    @Override
    public void modifyAttackingObject (GameObject attack, GameObject target) {
        float roll = random.nextFloat() * 100; // Roll a number between 0 and 100

        // Check if the roll is within the crit chance
        if (roll < critChance) {
            // Successful critical strike, double the attack's damage
            attack.setACrit(true);
//            OnScreenText text = new OnScreenText(target.getXCoordinate(), target.getYCoordinate(), "CRITICAL HIT");
//            OnScreenTextManager.getInstance().addTextObject(text);
        }


    }
}
