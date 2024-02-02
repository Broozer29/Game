package game.items.effects.effecttypes;

import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectInterface;
import game.objects.GameObject;
import game.objects.player.PlayerManager;
import visualobjects.SpriteAnimation;

public class HealPlayerOnDeath implements EffectInterface {

    private boolean healShield;
    private float healAmount;

    private EffectActivationTypes effectActivationType;

    public HealPlayerOnDeath (boolean healShield, float healAmount, EffectActivationTypes effectActivationType) {
        this.healAmount = healAmount;
        this.healShield = healShield;
        this.effectActivationType = effectActivationType;
    }


    @Override
    public void activateEffect (GameObject gameObject) {
        if (gameObject.getCurrentHitpoints() <= 0) {
            if (healShield) {
                PlayerManager.getInstance().getSpaceship().changeShieldHitpoints(healAmount);
            } else {
                PlayerManager.getInstance().getSpaceship().changeHitPoints(healAmount);
            }
        }
    }

    @Override
    public boolean shouldBeRemoved () {
        return false;
    }

    @Override
    public SpriteAnimation getAnimation () {
        return null;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums () {
        return this.effectActivationType;
    }

    @Override
    public void resetDuration () {
        //Shouldnt expire anyway
    }

    @Override
    public void increaseEffectStrength () {
        //Maybe do something later?
    }

    @Override
    public EffectInterface copy () {
        //Shouldn't be used
        return null;
    }
}
