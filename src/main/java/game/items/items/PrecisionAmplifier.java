package game.items.items;

import game.items.Item;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.managers.OnScreenTextManager;
import game.objects.GameObject;
import game.objects.player.PlayerStats;
import game.util.OnScreenText;

import java.util.Random;

public class PrecisionAmplifier extends Item {

    private float critChance;
    private Random random = new Random();

    public PrecisionAmplifier () {
        super(ItemEnums.PrecisionAmplifier, 1, EffectActivationTypes.DamageModification, ItemApplicationEnum.BeforeCollision);
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
    public void modifyAttackValues(GameObject attack, GameObject target) {
        float roll = random.nextFloat() * 100; // Roll a number between 0 and 100

        // Check if the roll is within the crit chance
        if (roll < critChance) {
            // Successful critical strike, double the attack's damage
            attack.setDamage(attack.getDamage() * PlayerStats.getInstance().getCriticalStrikeDamageMultiplier());
            OnScreenText text = new OnScreenText(target.getXCoordinate(), target.getYCoordinate(), "CRITICAL HIT");
            OnScreenTextManager.getInstance().addTextObject(text);
        }


    }
}
