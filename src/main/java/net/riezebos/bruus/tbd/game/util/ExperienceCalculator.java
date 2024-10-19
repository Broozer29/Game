package net.riezebos.bruus.tbd.game.util;

public class ExperienceCalculator {


    public static float getNextLevelXPRequired(float currentXpToNextLevel){
        return currentXpToNextLevel*1.55f;
    }

    public static float getNextLevelHitPoints(float currentHP){
        return currentHP*1.1f;
    }

    public static float getNextLevelShieldPoints(float currentShield){
        return currentShield*1.1f;
    }

    public static float getNextLevelBaseDamage(float currentDamage){
        return currentDamage*1.04f;
    }

}
