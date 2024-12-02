package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

public class AttackSpeedModifierEffect implements EffectInterface {
    private float attackSpeedModifierAmount;
    private EffectActivationTypes effectTypesEnums;
    private double durationInSeconds;
    private double startTimeInSeconds;

    private SpriteAnimation animation;
    private GameObject modifiedObject;
    private boolean appliedToObject;
    private EffectIdentifiers effectIdentifier;

    public AttackSpeedModifierEffect (float attackSpeedModifierAmount, double durationInSeconds, SpriteAnimation animation, EffectIdentifiers effectIdentifier) {
        this.attackSpeedModifierAmount = attackSpeedModifierAmount;
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
            this.modifiedObject = gameObject;

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

    private void removeEffectsBeforeRemovingEffect () {
        float percentageChange = -(attackSpeedModifierAmount * 100);

        if (modifiedObject instanceof SpaceShip) {
            PlayerStats.getInstance().modifyAttackSpeedBonus(percentageChange);
        } else {
            modifiedObject.modifyAttackSpeedBonus(percentageChange);
        }

        if (animation != null) {
            animation.setVisible(false);
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
    public boolean shouldBeRemoved () {
        if (GameStateInfo.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
            removeEffectsBeforeRemovingEffect();
            return true;
        }
        return false;
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
    public void increaseEffectStrength () {
        // To be implemented later, if ever
    }

    @Override
    public EffectInterface copy () {
        return new AttackSpeedModifierEffect(attackSpeedModifierAmount, durationInSeconds, animation.clone(), effectIdentifier);
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }
}
