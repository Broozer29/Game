package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

public class DamageModifierEffect implements EffectInterface {


    private float damageModifierAmount = 1.0f;
    private EffectActivationTypes effectTypesEnums;
    private double durationInSeconds;
    private double startTimeInSeconds;

    private SpriteAnimation animation;
    private boolean appliedToObject;
    private EffectIdentifiers effectIdentifier;

    public DamageModifierEffect (float damageModifierAmount, double durationInSeconds, SpriteAnimation animation, EffectIdentifiers effectIdentifier) {
        this.damageModifierAmount = damageModifierAmount;
        this.durationInSeconds = durationInSeconds;
        this.animation = animation;
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.appliedToObject = false;
        this.effectIdentifier = effectIdentifier;
    }

    @Override
    public void activateEffect (GameObject gameObject) {
        if (!appliedToObject) {
            if (gameObject instanceof SpaceShip) {
                PlayerStats.getInstance().modifyBonusDamageMultiplier(damageModifierAmount);
            } else if (gameObject.isFriendly()) {
                gameObject.modifyBonusDamageMultiplier(damageModifierAmount);
            } else {
                gameObject.modifyBonusDamageMultiplier(damageModifierAmount);
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

        if (gameObject instanceof SpaceShip) {
            PlayerStats.getInstance().modifyBonusDamageMultiplier(-damageModifierAmount);
        } else if (gameObject.isFriendly()) {
            gameObject.modifyBonusDamageMultiplier(-damageModifierAmount);
        } else {
            gameObject.modifyBonusDamageMultiplier(-damageModifierAmount);
        }

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
        if (GameStateInfo.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
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
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength (GameObject gameObject) {
        //To be implemented later, if ever
    }

    @Override
    public EffectInterface copy () {
        return new DamageModifierEffect(damageModifierAmount, durationInSeconds, animation.clone(), effectIdentifier);
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
