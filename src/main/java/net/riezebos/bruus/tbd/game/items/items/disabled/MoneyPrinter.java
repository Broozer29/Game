package net.riezebos.bruus.tbd.game.items.items.disabled;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.GainGoldOnDeath;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

import java.util.Random;

public class MoneyPrinter extends Item {

    private float moneyGainAmount;
    private Random rand;
    private float procChance;


    public MoneyPrinter(){
        super(ItemEnums.MoneyPrinter, 1, ItemApplicationEnum.AfterCollision);
        calculateMoneyGainAmount();
        procChance = 0.1f;
//        procChance = 1f;
        rand = new Random();
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Grants the player a small chance on hit to gain gold when the enemy dies
        float chance = rand.nextFloat(); // Generates a float between 0.0 (inclusive) and 1.0 (exclusive)
        if (chance <= procChance) {
            GainGoldOnDeath gainGoldOnDeath = new GainGoldOnDeath(EffectActivationTypes.OnObjectDeath, EffectIdentifiers.MoneyPrinterGoldOnDeath, moneyGainAmount);
            gameObject.addEffect(gainGoldOnDeath);
        }

    }

    private void calculateMoneyGainAmount(){
        this.moneyGainAmount = quantity * 2f;
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        calculateMoneyGainAmount();
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        return true;
    }
}
