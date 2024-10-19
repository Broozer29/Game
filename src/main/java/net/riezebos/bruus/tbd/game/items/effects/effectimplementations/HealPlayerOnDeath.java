package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;

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
