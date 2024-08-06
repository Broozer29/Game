package game.items.effects.effectimplementations;

import game.gameobjects.GameObject;
import game.gameobjects.enemies.Enemy;
import game.gamestate.GameStateInfo;
import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectIdentifiers;
import game.items.effects.EffectInterface;
import visualobjects.SpriteAnimation;

import java.util.Random;

public class FreezeEffect implements EffectInterface {

    private double durationInSeconds;
    private double startTimeInSeconds;


    private boolean offsetApplied = false;
    private boolean scaledToTarget = false;

    private SpriteAnimation animation;
    private EffectIdentifiers effectIdentifier;
    private EffectActivationTypes effectTypesEnums;
    private GameObject target;

    public FreezeEffect(double durationInSeconds, SpriteAnimation spriteAnimation){
        this.effectIdentifier = EffectIdentifiers.ElectricDestabilizerFreeze;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.animation = spriteAnimation;
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
    }
    @Override
    public void activateEffect (GameObject target) {
        this.target = target;
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (animation != null) {
            if (!scaledToTarget) {
                scaleAnimation(target);
                scaledToTarget = true;
            }
            if (!offsetApplied) {
                applyRandomOffset(target);
                offsetApplied = true;
            }
        }
//
        if (currentTime - startTimeInSeconds < durationInSeconds) {
            target.setAllowedToMove(false);
            if(target instanceof Enemy){
                ((Enemy) target).setAllowedToFire(false);
            }
        }
    }

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
            target.setAllowedToMove(true);

            if(target instanceof Enemy){
                ((Enemy) target).setAllowedToFire(true);
            }
            return true;
        } else return false;

    }

    @Override
    public SpriteAnimation getAnimation () {
        return animation;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums () {
        return this.effectTypesEnums;
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
//does nothing
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    @Override
    public EffectInterface copy () {
        FreezeEffect copiedEffect = new FreezeEffect(this.durationInSeconds, this.animation);
        // Copy other necessary fields
        copiedEffect.startTimeInSeconds = this.startTimeInSeconds;
        copiedEffect.effectIdentifier = this.effectIdentifier;
        // Note: startTimeInSeconds may need special handling depending on desired behavior
        return copiedEffect;
    }

    public void setEffectIdentifier (EffectIdentifiers effectIdentifier) {
        this.effectIdentifier = effectIdentifier;
    }

    public void setEffectTypesEnums (EffectActivationTypes effectTypesEnums) {
        this.effectTypesEnums = effectTypesEnums;
    }


}
