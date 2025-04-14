package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

public class ModifyMovementSpeedEffect implements EffectInterface {
    private float movementSpeedModifieramount = 1.0f;
    private float movementSpeedMofifierAmountPerStack;
    private EffectActivationTypes effectTypesEnums;
    private double durationInSeconds;
    private double startTimeInSeconds;

    private SpriteAnimation animation;
    private boolean appliedToObject;
    private EffectIdentifiers effectIdentifier;
    private int amountOfStacks;

    public ModifyMovementSpeedEffect (float movementSpeedModifierAmountPerStack, double durationInSeconds, SpriteAnimation animation, EffectIdentifiers effectIdentifier) {
        this.movementSpeedModifieramount = movementSpeedModifierAmountPerStack;
        this.movementSpeedMofifierAmountPerStack = movementSpeedModifierAmountPerStack;
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
        if (gameObject == null) {
            return;
        }

        if (!appliedToObject) {
            if (gameObject instanceof SpaceShip) {
                // Player
                PlayerStats.getInstance().modifyMovementSpeedModifier(movementSpeedModifieramount);
            } else {
                // Enemy
                gameObject.modifyMovementSpeedModifier(movementSpeedModifieramount);
            }
            appliedToObject = true;
            this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
        }

        if (animation != null) {
            centerAnimation(gameObject);
        }
    }

    private void removeEffectsBeforeRemovingEffect (GameObject gameObject) {
        if(gameObject == null){
            return;
        }

        if (gameObject instanceof SpaceShip) {
            // Correctly subtract the originally applied modifier
            PlayerStats.getInstance().modifyMovementSpeedModifier(-movementSpeedModifieramount);
        } else {
            // Correctly subtract the originally applied modifier
            gameObject.modifyMovementSpeedModifier(-movementSpeedModifieramount);
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
        if (this.effectIdentifier.equals(EffectIdentifiers.DevourerMoveSpeedDebuff)) {
            int maxStacks = 4;
            if (amountOfStacks < maxStacks) {
                amountOfStacks += 1;
            }
            removeEffectsBeforeRemovingEffect(gameObject);
            movementSpeedModifieramount = movementSpeedMofifierAmountPerStack * amountOfStacks;
            activateEffect(gameObject);

            switch (amountOfStacks) {
                case 1:
                    animation.changeImagetype(ImageEnums.DevourerDebuffStage1);
                    break;
                case 2:
                    animation.changeImagetype(ImageEnums.DevourerDebuffStage2);
                    break;
                case 3:
                    animation.changeImagetype(ImageEnums.DevourerDebuffStage3);
                    break;
                case 4:
                    animation.changeImagetype(ImageEnums.DevourerDebuffStage4);
                    break;
            }

        }
    }

    @Override
    public EffectInterface copy () {
        return new ModifyMovementSpeedEffect(movementSpeedMofifierAmountPerStack, durationInSeconds, animation.clone(), effectIdentifier);
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    @Override
    public void removeEffect (GameObject gameObject) {
        if(animation != null){
            animation.setInfiniteLoop(false);
            animation.setVisible(false);
        }
        removeEffectsBeforeRemovingEffect(gameObject);
        animation = null;
    }

}
