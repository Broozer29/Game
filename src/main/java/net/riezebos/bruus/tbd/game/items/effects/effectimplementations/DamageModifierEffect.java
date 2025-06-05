package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class DamageModifierEffect implements EffectInterface {


    private float damageModifierAmount = 1.0f;
    private EffectActivationTypes effectTypesEnums;
    private double durationInSeconds;
    private double startTimeInSeconds;

    private List<SpriteAnimation> animationList = new ArrayList<>();
    private boolean appliedToObject;
    private EffectIdentifiers effectIdentifier;

    public DamageModifierEffect (float damageModifierAmount, double durationInSeconds, SpriteAnimation animation, EffectIdentifiers effectIdentifier) {
        this.damageModifierAmount = damageModifierAmount;
        this.durationInSeconds = durationInSeconds;
        this.animationList.add(animation);
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.appliedToObject = false;
        this.effectIdentifier = effectIdentifier;
    }

    @Override
    public void activateEffect (GameObject gameObject) {
        if (!appliedToObject) {
            if (gameObject instanceof SpaceShip) {
                PlayerStats.getInstance().modifyBonusDamageMultiplier(damageModifierAmount);
            } else {
                gameObject.modifyBonusDamageMultiplier(damageModifierAmount);
            }
            appliedToObject = true;
        }

        if (this.animationList.get(0) != null) {
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
            this.animationList.get(0).setCenterCoordinates(objectVisuals.getCenterXCoordinate(), objectVisuals.getCenterYCoordinate());
        } else {
            this.animationList.get(0).setCenterCoordinates(object.getCenterXCoordinate(), object.getCenterYCoordinate());
        }


    }

    @Override
    public boolean shouldBeRemoved (GameObject gameObject) {
        if (GameState.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
            return true;
        } else return false;
    }

    @Override
    public List<SpriteAnimation> getAnimations() {
        return this.animationList;
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
        //To be implemented later, if ever
    }

    @Override
    public EffectInterface copy () {
        return new DamageModifierEffect(damageModifierAmount, durationInSeconds, this.animationList.get(0).clone(), effectIdentifier);
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    @Override
    public void removeEffect (GameObject gameObject) {
        if (this.animationList.get(0) != null) {
            this.animationList.get(0).setInfiniteLoop(false);
            this.animationList.get(0).setVisible(false);
        }
        removeEffectsBeforeRemovingEffect(gameObject);
        this.animationList.clear();;
    }
}
