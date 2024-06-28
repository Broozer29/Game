package game.items.effects.effectimplementations;

import game.gamestate.GameStateInfo;
import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectIdentifiers;
import game.items.effects.EffectInterface;
import game.objects.GameObject;
import game.objects.player.PlayerStats;
import game.objects.player.spaceship.SpaceShip;
import visualobjects.SpriteAnimation;

public class AttackSpeedModifierEffect implements EffectInterface {
    private float attackSpeedModifierAmount = 1.0f;
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

            float percentageChange;

            if (attackSpeedModifierAmount <= 1) {
                // Expected to increase speed: use positive
                percentageChange = -(1 - attackSpeedModifierAmount) * 100;
            } else {
                // Expected to decrease speed: use negative
                percentageChange = (attackSpeedModifierAmount - 1) * 100;
            }


            if (gameObject.isFriendly() || gameObject instanceof SpaceShip) {
                //Player
                PlayerStats.getInstance().modifyAttackSpeedBonus(percentageChange);
//                OnScreenTextManager.getInstance().addText(PlayerManager.getInstance().getSpaceship().getAttackSpeed() + "",
//                        gameObject.getXCoordinate(), gameObject.getYCoordinate());
            } else {
                //Enemy
//                float oldValue = gameObject.getAttackSpeed();
                gameObject.modifyAttackSpeedBonus(percentageChange);
//                OnScreenTextManager.getInstance().addText(gameObject.getAttackSpeed() + " / " + oldValue,
//                        gameObject.getXCoordinate(), gameObject.getYCoordinate());
            }
            appliedToObject = true;
        }

        if (animation != null) {
            centerAnimation(gameObject);
        }

    }

    private void removeEffectsBeforeRemovingEffect () {
        float percentageChange;

        if (attackSpeedModifierAmount <= 1) {
            // Reverse the decrease: Positive adjustment (since original was negative)
            percentageChange = (1 - attackSpeedModifierAmount) * 100;
        } else {
            // Reverse the increase: Negative adjustment (since original was positive)
            percentageChange = -(attackSpeedModifierAmount - 1) * 100;
        }

        if (modifiedObject instanceof SpaceShip) {
            PlayerStats.getInstance().modifyAttackSpeedBonus(percentageChange);
//            OnScreenTextManager.getInstance().addText(PlayerManager.getInstance().getSpaceship().getAttackSpeed() + "",
//                    modifiedObject.getXCoordinate(), modifiedObject.getYCoordinate());
        } else {
            modifiedObject.modifyAttackSpeedBonus(percentageChange);
//            OnScreenTextManager.getInstance().addText(modifiedObject.getAttackSpeed() + "",
//                    modifiedObject.getXCoordinate(), modifiedObject.getYCoordinate());
        }

        if(animation != null){
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
        AttackSpeedModifierEffect copiedEffect = new AttackSpeedModifierEffect(attackSpeedModifierAmount, durationInSeconds, animation.clone(), effectIdentifier);
        return copiedEffect;
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }
}
