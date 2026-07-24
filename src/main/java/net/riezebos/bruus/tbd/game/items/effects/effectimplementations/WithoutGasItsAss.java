package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.List;

public class WithoutGasItsAss implements EffectInterface {
    private float attackSpeedModifierAmountPerStack;
    private EffectActivationTypes effectTypesEnums;
    private double durationInSeconds;
    private double startTimeInSeconds;

    private EffectIdentifiers effectIdentifier;

    public WithoutGasItsAss(double durationInSeconds, EffectIdentifiers effectIdentifier) {
        this.attackSpeedModifierAmountPerStack = attackSpeedModifierAmountPerStack; //Expects a float value! A value if 0.1 is a 10% attack speed increase
        this.durationInSeconds = durationInSeconds;
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds(); //default safety net but should be overwritten by activateEffect
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.effectIdentifier = effectIdentifier;
    }

    @Override
    public void activateEffect(GameObject gameObject) {
    }

    @Override
    public boolean shouldBeRemoved(GameObject gameObject) {
        if (GameState.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
            return true;
        } else return false;
    }

    @Override
    public List<SpriteAnimation> getAnimations() {
        return null;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums() {
        return effectTypesEnums;
    }

    @Override
    public void resetDuration() {
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength(GameObject gameObject) {
    }

    @Override
    public EffectInterface copy() {
        return new WithoutGasItsAss(durationInSeconds, effectIdentifier);
    }

    @Override
    public EffectIdentifiers getEffectIdentifier() {
        return effectIdentifier;
    }

    @Override
    public void removeEffect(GameObject gameObject) {
    }
}
