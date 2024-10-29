package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;

public class ModifyMovementSpeedEffect implements EffectInterface {
    private float movementSpeedModifieramount = 1.0f;
    private EffectActivationTypes effectTypesEnums;
    private double durationInSeconds;
    private double startTimeInSeconds;

    private SpriteAnimation animation;
    private GameObject modifiedObject;
    private boolean appliedToObject;
    private EffectIdentifiers effectIdentifier;

    public ModifyMovementSpeedEffect (float movementSpeedModifierAmount, double durationInSeconds, SpriteAnimation animation, EffectIdentifiers effectIdentifier) {
        this.movementSpeedModifieramount = movementSpeedModifierAmount;
        this.durationInSeconds = durationInSeconds;
        this.animation = animation;
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.appliedToObject = false;
        this.effectIdentifier = effectIdentifier;
    }

    @Override
    public void activateEffect(GameObject gameObject) {
        if (!appliedToObject) {
            this.modifiedObject = gameObject;

            if (gameObject.isFriendly() || gameObject instanceof SpaceShip) {
                // Player
                PlayerStats.getInstance().modifyMovementSpeedModifier(movementSpeedModifieramount);
            } else {
                // Enemy
                gameObject.modifyMovementSpeedModifier(movementSpeedModifieramount);
            }
            appliedToObject = true;
        }

        if (animation != null) {
            centerAnimation(gameObject);
        }
    }

    private void removeEffectsBeforeRemovingEffect() {
        if (modifiedObject instanceof SpaceShip) {
            // Correctly subtract the originally applied modifier
            PlayerStats.getInstance().modifyMovementSpeedModifier(-movementSpeedModifieramount);
        } else {
            // Correctly subtract the originally applied modifier
            modifiedObject.modifyMovementSpeedModifier(-movementSpeedModifieramount);
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
            removeEffectsBeforeRemovingEffect(); //Assumes the effect is immediatly removed if this is true (currently is)
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
    public void increaseEffectStrength () {
        //To be implemented later, if ever
    }

    @Override
    public EffectInterface copy () {
        AttackSpeedModifierEffect copiedEffect = new AttackSpeedModifierEffect(movementSpeedModifieramount, durationInSeconds, animation.clone(), effectIdentifier);
        return copiedEffect;
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

}
