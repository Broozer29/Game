package game.items.effects.effectimplementations;

import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectIdentifiers;
import game.items.effects.EffectInterface;
import game.gameobjects.GameObject;
import game.gameobjects.player.PlayerManager;
import visualobjects.SpriteAnimation;

public class HealPlayerOnDeath implements EffectInterface {

    private boolean healShield;
    private float healAmount;
    private EffectIdentifiers effectIdentifier;

    private EffectActivationTypes effectActivationType;

    public HealPlayerOnDeath (boolean healShield, float healAmount, EffectActivationTypes effectActivationType, EffectIdentifiers effectIdentifier) {
        this.healAmount = healAmount;
        this.healShield = healShield;
        this.effectActivationType = effectActivationType;
        this.effectIdentifier = effectIdentifier;
    }


    @Override
    public void activateEffect (GameObject gameObject) {
        if (gameObject.getCurrentHitpoints() <= 0) {
            if (healShield) {
                PlayerManager.getInstance().getSpaceship().repairShields(healAmount);
            } else {
                PlayerManager.getInstance().getSpaceship().repairHealth(healAmount);
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

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }
}
