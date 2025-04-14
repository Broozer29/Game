package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.util.EffectAnimationHelper;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

public class DamageReduction implements EffectInterface {

    private double durationInSeconds;
    private double startTimeInSeconds;
    private boolean scaledToTarget = false;

    private SpriteAnimation animation;
    private EffectIdentifiers effectIdentifier;
    private EffectActivationTypes effectTypesEnums;
    private float damageReductionRatio;
    private boolean hasApplied = false;

    public DamageReduction(double durationInSeconds, float damageReductionRatio, SpriteAnimation spriteAnimation) {
        this.effectIdentifier = EffectIdentifiers.DamageReduction;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.animation = spriteAnimation;
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
        this.damageReductionRatio = damageReductionRatio;
    }

    @Override
    public void activateEffect(GameObject target) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (animation != null) {
            if (!scaledToTarget) {
                EffectAnimationHelper.scaleAnimation(target, animation);
                scaledToTarget = true;
                animation.setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
            }
        }

        if(!hasApplied) {
            target.modifyDamageReductionMultiplier(-this.damageReductionRatio);
            hasApplied = true;
        }
    }


    @Override
    public boolean shouldBeRemoved(GameObject gameObject) {
        if (GameState.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
            return true;
        } else return false;

    }

    private void deleteEffect(GameObject gameObject) {
        if (gameObject != null && hasApplied) {
            gameObject.modifyDamageReductionMultiplier(this.damageReductionRatio);
        }
    }

    @Override
    public SpriteAnimation getAnimation() {
        return animation;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums() {
        return this.effectTypesEnums;
    }

    @Override
    public void resetDuration() {
        // Reset the start time to the current game time
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength(GameObject gameObject) {
        // does nothing
    }

    @Override
    public EffectIdentifiers getEffectIdentifier() {
        return effectIdentifier;
    }

    @Override
    public EffectInterface copy() {
        DamageReduction copiedEffect = new DamageReduction(this.durationInSeconds, this.damageReductionRatio, this.animation);
        // Copy other necessary fields
        copiedEffect.startTimeInSeconds = this.startTimeInSeconds;
        copiedEffect.effectIdentifier = this.effectIdentifier;
        // Note: startTimeInSeconds may need special handling depending on desired behavior
        return copiedEffect;
    }

    @Override
    public void removeEffect(GameObject gameObject) {
        if (animation != null) {
            animation.setInfiniteLoop(false);
            animation.setVisible(false);
        }

        deleteEffect(gameObject);
        animation = null;
    }
}
