package game.util;

import game.objects.GameObject;

public class ArmorCalculator {

    public static float calculateDamage(float damageAmount, GameObject gameObject) {
        float armor = gameObject.getBaseArmor() + gameObject.getArmorBonus();
        float damageMultiplier;

        if (armor >= 0) {
            damageMultiplier = 100f / (100f + armor);
        } else {
            damageMultiplier = 2f - 100f / (100f - armor);
        }

        return damageAmount * damageMultiplier;
    }
}
