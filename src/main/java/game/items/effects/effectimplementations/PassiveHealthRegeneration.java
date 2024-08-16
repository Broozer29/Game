package game.items.effects.effectimplementations;

import game.gamestate.GameStateInfo;
import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectIdentifiers;
import game.items.effects.EffectInterface;
import game.managers.AnimationManager;
import game.gameobjects.GameObject;
import visualobjects.SpriteAnimation;

public class PassiveHealthRegeneration implements EffectInterface {

    private EffectActivationTypes effectActivationTypes;
    private float healingAmount;
    private double lastTimeDamageTaken;
    private EffectIdentifiers effectIdentifier;

    private SpriteAnimation animation;

    public PassiveHealthRegeneration (float healingAmount, EffectIdentifiers effectIdentifier) {
        this.healingAmount = healingAmount;
        this.lastTimeDamageTaken = GameStateInfo.getInstance().getGameSeconds();
        this.effectActivationTypes = EffectActivationTypes.CheckEveryGameTick;
        this.effectIdentifier = effectIdentifier;
    }

    @Override
    public void activateEffect (GameObject gameObject) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime - gameObject.getLastGameSecondDamageTaken() > 3 && // More than 3 seconds have passed
                gameObject.getCurrentHitpoints() < gameObject.getMaxHitPoints()) {

            if (animation != null) {
                showHealingAnimation();
                centerHealingAnimation(gameObject);
            }

            gameObject.takeDamage(-healingAmount); // Apply healing
        } else {
            if (animation != null) {
                hideHealingAnimation();
            }
        }
    }

    private void centerHealingAnimation (GameObject gameObject) {
        animation.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
    }

    private void showHealingAnimation () {
        if (!AnimationManager.getInstance().getUpperAnimations().contains(animation)) {
            AnimationManager.getInstance().addUpperAnimation(animation);
        }
    }

    private void hideHealingAnimation () {
        if (AnimationManager.getInstance().getUpperAnimations().contains(animation)) {
            AnimationManager.getInstance().getUpperAnimations().remove(animation);
            animation.refreshAnimation();
        }
    }

    @Override
    public boolean shouldBeRemoved () {
        return false;
    }

    @Override
    public SpriteAnimation getAnimation () {
        //Maybe implemented later, possibly not
        return null;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums () {
        return effectActivationTypes;
    }

    @Override
    public void resetDuration () {
        //Considering it should always be active but activate when in combat, this is not needed
    }

    @Override
    public void increaseEffectStrength () {
        //Not needed, the "healingAmount" already factors this in
    }

    @Override
    public EffectInterface copy () {
        return null;
    }

    public void setAnimation (SpriteAnimation spriteAnimation) {
        this.animation = spriteAnimation;
    }
    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

}
