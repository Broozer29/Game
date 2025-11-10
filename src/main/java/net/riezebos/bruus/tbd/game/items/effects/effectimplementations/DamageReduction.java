package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.util.EffectAnimationHelper;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class DamageReduction implements EffectInterface {

    private double durationInSeconds;
    private double startTimeInSeconds;
    private boolean scaledToTarget = false;

    private List<SpriteAnimation> animationList = new ArrayList<>();
    private EffectIdentifiers effectIdentifier;
    private EffectActivationTypes effectTypesEnums;
    private float damageReductionRatio;
    private boolean hasApplied = false;

    public DamageReduction(double durationInSeconds, float damageReductionRatio, SpriteAnimation spriteAnimation) {
        this.effectIdentifier = EffectIdentifiers.DamageReduction;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        if(spriteAnimation != null) {
            this.animationList.add(spriteAnimation);
        }
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
        this.damageReductionRatio = damageReductionRatio;
    }

    @Override
    public void activateEffect(GameObject target) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (this.animationList.size() > 0 && this.animationList.get(0) != null) {
            if (!scaledToTarget) {
                EffectAnimationHelper.scaleAnimation(target, this.animationList.get(0));
                scaledToTarget = true;
                this.animationList.get(0).setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
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
    public List<SpriteAnimation> getAnimations() {
        return animationList;
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
        SpriteAnimation animation = null;
        if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
            animation = this.animationList.get(0);
        }
        SpriteAnimation clonedAnimation = (animation != null) ? animation.clone() : null;

        DamageReduction copiedEffect = new DamageReduction(this.durationInSeconds, this.damageReductionRatio, clonedAnimation);
        // Copy other necessary fields
        copiedEffect.startTimeInSeconds = this.startTimeInSeconds;
        copiedEffect.effectIdentifier = this.effectIdentifier;
        // Note: startTimeInSeconds may need special handling depending on desired behavior
        return copiedEffect;
    }

    @Override
    public void removeEffect(GameObject gameObject) {
        if (this.animationList.size() > 0 && this.animationList.get(0) != null) {
            this.animationList.get(0).setInfiniteLoop(false);
            this.animationList.get(0).setVisible(false);
        }

        deleteEffect(gameObject);
        this.animationList.clear();
    }
}
