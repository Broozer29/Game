package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class MutaliskHealthRegenBonusEffect implements EffectInterface {

    private EffectIdentifiers effectIdentifier;
    private EffectActivationTypes effectActivationType;
    private List<SpriteAnimation> animationList = new ArrayList<>();
    private float healingBonusModifier = 0f;
    private boolean hasApplied = false;
    private float duration = 0;
    private double gameSecondsStarted = 0;

    public MutaliskHealthRegenBonusEffect(float healingModifier, float duration) {
        this.effectActivationType = EffectActivationTypes.CheckEveryGameTick;
        this.effectIdentifier = EffectIdentifiers.MutaliskIncreasedPassiveHealing;
        this.healingBonusModifier = healingModifier;
        this.duration = duration;
    }


    @Override
    public void activateEffect(GameObject gameObject) {
        if (gameObject instanceof SpaceShip spaceShip && !hasApplied) {
            //doesnt do anything here, passivehealthregen handles the actual implementation
            gameSecondsStarted = GameState.getInstance().getGameSeconds();
            hasApplied = true;
        }
    }

    @Override
    public boolean shouldBeRemoved(GameObject gameObject) {
        return GameState.getInstance().getGameSeconds() > gameSecondsStarted + duration;
    }

    @Override
    public List<SpriteAnimation> getAnimations() {
        return animationList;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums() {
        return this.effectActivationType;
    }

    @Override
    public void resetDuration() {
        this.gameSecondsStarted = GameState.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength(GameObject gameObject) {
        //Maybe do something later?
    }

    @Override
    public EffectInterface copy() {
        //Shouldn't be used
        return null;
    }

    @Override
    public EffectIdentifiers getEffectIdentifier() {
        return effectIdentifier;
    }

    @Override
    public void removeEffect(GameObject gameObject) {
        if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
            animationList.get(0).setInfiniteLoop(false);
            animationList.get(0).setVisible(false);
        }
        animationList.clear();
    }

    public float getHealingBonusModifier() {
        return healingBonusModifier;
    }

    public void setHealingBonusModifier(float healingBonusModifier) {
        this.healingBonusModifier = healingBonusModifier;
    }
}

