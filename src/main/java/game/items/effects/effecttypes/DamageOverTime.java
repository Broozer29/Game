package game.items.effects.effecttypes;

import game.gamestate.GameStateInfo;
import game.items.PlayerInventory;
import game.items.effects.EffectIdentifiers;
import game.items.effects.EffectInterface;
import game.items.effects.EffectActivationTypes;
import game.managers.AnimationManager;
import game.managers.OnScreenTextManager;
import game.objects.GameObject;
import game.objects.player.PlayerManager;
import visualobjects.SpriteAnimation;

import java.util.Objects;
import java.util.Random;

public class DamageOverTime implements EffectInterface {

    private EffectActivationTypes effectTypesEnums;
    private float damage;
    private double durationInSeconds;
    private double startTimeInSeconds;
    private int dotStacks;
    private boolean offsetApplied = false;
    private boolean scaledToTarget = false;

    private SpriteAnimation animation;
    private EffectIdentifiers effectIdentifier;

    public DamageOverTime (float damage, double durationInSeconds, SpriteAnimation spriteAnimation, EffectIdentifiers effectIdentifier) {
        this.damage = damage;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.dotStacks = 1;
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
        this.animation = spriteAnimation;
        this.effectIdentifier = effectIdentifier;
    }

    public void updateDamage (float damage) {
        this.damage = damage;
    }

    @Override
    public void activateEffect (GameObject target) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (animation != null) {
            if (!scaledToTarget) {
                scaleAnimation(target);
                scaledToTarget = true;
            }
            if (!offsetApplied) {
                applyRandomOffset(target);
//                offsetApplied = true;
            }
        }
        if (currentTime - startTimeInSeconds < durationInSeconds) {
            target.takeDamage(this.damage * dotStacks);
        } else {
            this.dotStacks = 0;
        }
    }

    //Scales animations if needed, should be added to specific animations & enemies
    private void scaleAnimation (GameObject target) {
        animation.cropAnimation();
        // Retrieve animation dimensions
        int animationWidth = this.animation.getWidth();
        int animationHeight = this.animation.getHeight();

        // Retrieve target dimensions
        int enemyWidth = target.getWidth();
        int enemyHeight = target.getHeight();

        // If target has its own animation, use those dimensions instead
        if (target.getAnimation() != null) {
            enemyWidth = target.getAnimation().getWidth();
            enemyHeight = target.getAnimation().getHeight();
        }

        // Calculate the maximum allowed dimensions
        int maxAllowedWidth = (int) (enemyWidth * 0.6);
        int maxAllowedHeight = (int) (enemyHeight * 0.6);

        // Calculate the current scale of the animation
        float currentScale = this.animation.getScale();


        // Determine if scaling adjustment is needed
        if (animationWidth > maxAllowedWidth || animationHeight > maxAllowedHeight) {
            // Calculate the necessary scale factors to fit within the target dimensions
            float widthScaleFactor = maxAllowedWidth / (float) animationWidth;
            float heightScaleFactor = maxAllowedHeight / (float) animationHeight;

            // Choose the larger scale factor to ensure the animation fits within both dimensions
            float newScaleFactor = Math.max(widthScaleFactor, heightScaleFactor);

            // Apply the new scale, adjusting based on the current scale
            this.animation.setAnimationScale(currentScale * newScaleFactor);
        }
    }

    private void applyRandomOffset (GameObject target) {
        animation.resetOffset();

        Random random = new Random();

        // Get the animation dimensions and enemy dimensions
        int animationWidth = this.animation.getWidth();
        int animationHeight = this.animation.getHeight();
        int enemyWidth = target.getWidth();
        int enemyHeight = target.getHeight();

        // Center animation on the enemy first
        int animationCenterX = target.getCenterXCoordinate();
        int animationCenterY = target.getCenterYCoordinate();

        animation.setCenterCoordinates(animationCenterX, animationCenterY);

        // Calculate max X offset to ensure animation stays within the enemy's width
        int maxXOffset = (int) Math.round((enemyWidth * 0.8) - animationWidth / 2);
        int xOffset = (maxXOffset > 0) ? random.nextInt(maxXOffset * 2 + 1) - maxXOffset : 0;

        // For Y, allow animation's origin to potentially start from below the enemy's base
        int minYOffset = (int) (enemyHeight * 0.65);  // Start from 45% of the enemy's height for the base of the animation

        // Adjust the range above this base point where the animation can extend, slightly reducing it
        int maxYOffset = minYOffset + (int) (animationHeight * 0.25);  // Extend the bottom of the animation slightly less beyond the base

        int yOffset = minYOffset + random.nextInt(maxYOffset + 1) - animationHeight;

        // Apply the calculated offsets to the animation's coordinates
        animation.addXOffset(xOffset);
        animation.addYOffset(yOffset);
    }


    @Override
    public boolean shouldBeRemoved () {
        if (GameStateInfo.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
            if (animation != null) {
                animation.setVisible(false);
            }
            return true;
        } else return false;

    }

    @Override
    public void resetDuration () {
        // Reset the start time to the current game time
//        OnScreenTextManager.getInstance().addText("Refreshed " + this.effectIdentifier, PlayerManager.getInstance().getSpaceship().getXCoordinate(),
//                PlayerManager.getInstance().getSpaceship().getYCoordinate());
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength () {
        this.dotStacks += 1;
    }

    @Override
    public EffectInterface copy () {
        DamageOverTime copiedEffect = new DamageOverTime(this.damage, this.durationInSeconds, this.animation.clone(), effectIdentifier);
        // Copy other necessary fields
        copiedEffect.dotStacks = this.dotStacks;
        // Note: startTimeInSeconds may need special handling depending on desired behavior
        return copiedEffect;
    }

    public SpriteAnimation getAnimation () {
        return animation;
    }

    public void setAnimation (SpriteAnimation animation) {
        this.animation = animation;
    }

    public EffectActivationTypes getEffectTypesEnums () {
        return effectTypesEnums;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DamageOverTime that = (DamageOverTime) o;
        return Float.compare(damage, that.damage) == 0 && Double.compare(durationInSeconds, that.durationInSeconds) == 0 && Double.compare(startTimeInSeconds, that.startTimeInSeconds) == 0 && dotStacks == that.dotStacks && offsetApplied == that.offsetApplied && effectTypesEnums == that.effectTypesEnums && Objects.equals(animation, that.animation);
    }

    @Override
    public int hashCode () {
        return Objects.hash(effectTypesEnums);
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }
}
