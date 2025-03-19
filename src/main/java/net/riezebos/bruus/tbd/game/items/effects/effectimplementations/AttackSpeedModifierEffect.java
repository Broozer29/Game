package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

public class AttackSpeedModifierEffect implements EffectInterface {
    private float attackSpeedModifierAmount;
    private float attackSpeedModifierAmountPerStack;
    private EffectActivationTypes effectTypesEnums;
    private double durationInSeconds;
    private double startTimeInSeconds;

    private SpriteAnimation animation;
    private boolean appliedToObject;
    private EffectIdentifiers effectIdentifier;
    private int amountOfStacks;

    public AttackSpeedModifierEffect (float attackSpeedModifierAmountPerStack, double durationInSeconds, SpriteAnimation animation, EffectIdentifiers effectIdentifier) {
        this.attackSpeedModifierAmountPerStack = attackSpeedModifierAmountPerStack;
        this.attackSpeedModifierAmount = attackSpeedModifierAmountPerStack;
        this.durationInSeconds = durationInSeconds;
        this.animation = animation;
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.appliedToObject = false;
        this.effectIdentifier = effectIdentifier;
        this.amountOfStacks = 1;
    }

    @Override
    public void activateEffect (GameObject gameObject) {
        if (!appliedToObject) {
            float percentageChange = attackSpeedModifierAmount * 100;

            if (gameObject.isFriendly() || gameObject instanceof SpaceShip) {
                // Player
                PlayerStats.getInstance().modifyAttackSpeedBonus(percentageChange);
            } else {
                // Enemy
                gameObject.modifyAttackSpeedBonus(percentageChange);
            }
            appliedToObject = true;
        }

        if (animation != null) {
            centerAnimation(gameObject);
        }
    }

    private void removeEffectsBeforeRemovingEffect (GameObject gameObject) {
        if (gameObject == null) {
            return;
        }

        float percentageChange = -(attackSpeedModifierAmount * 100);
        if (gameObject instanceof SpaceShip) {
            PlayerStats.getInstance().modifyAttackSpeedBonus(percentageChange);
        } else {
            gameObject.modifyAttackSpeedBonus(percentageChange);
        }

        appliedToObject = false;
    }

    private void centerAnimation (GameObject object) {
        if (object.getAnimation() != null) {
            SpriteAnimation objectVisuals = object.getAnimation();
            animation.setCenterCoordinates(objectVisuals.getCenterXCoordinate(), objectVisuals.getCenterYCoordinate());
        } else {
            animation.setCenterCoordinates(object.getCenterXCoordinate(), object.getCenterYCoordinate());
        }
    }

    @Override
    public boolean shouldBeRemoved (GameObject gameObject) {
        if (GameState.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
            return true;
        } else return false;
    }

    @Override
    public SpriteAnimation getAnimation () {
        return animation;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums () {
        return effectTypesEnums;
    }

    @Override
    public void resetDuration () {
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength (GameObject gameObject) {
        if (this.effectIdentifier.equals(EffectIdentifiers.DevourerAttackSpeedDebuff)) {
            int maxStacks = 4;
            if (amountOfStacks < maxStacks) {
                amountOfStacks += 1;
            }
            removeEffectsBeforeRemovingEffect(gameObject);
            attackSpeedModifierAmount = attackSpeedModifierAmountPerStack * amountOfStacks;
            activateEffect(gameObject);
        }
    }

    @Override
    public EffectInterface copy () {
        return new AttackSpeedModifierEffect(attackSpeedModifierAmountPerStack, durationInSeconds, animation.clone(), effectIdentifier);
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    @Override
    public void removeEffect (GameObject gameObject) {
        if (animation != null) {
            animation.setInfiniteLoop(false);
            animation.setVisible(false);
        }
        removeEffectsBeforeRemovingEffect(gameObject);
        animation = null;
    }
}
