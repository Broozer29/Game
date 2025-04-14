package net.riezebos.bruus.tbd.game.items.effects.util;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.Random;

public class EffectAnimationHelper {

    public static void applyRandomOffset(GameObject target, SpriteAnimation animation) {
        // Reset any previous offsets
        animation.resetOffset();

        Random random = new Random();

        // Get animation dimensions and target (enemy) dimensions
        int enemyWidth = target.getWidth();
        int enemyHeight = target.getHeight();

        // Center the animation on the enemy first
        int animationCenterX = target.getCenterXCoordinate();
        int animationCenterY = target.getCenterYCoordinate();
        animation.setCenterCoordinates(animationCenterX, animationCenterY);

        // Determine the 50/50 chance for offset direction (increase or decrease)
        boolean increaseXOffset = random.nextBoolean();
        boolean increaseYOffset = random.nextBoolean();


        // Calculate the maximum possible offsets based on 25% of the enemy's dimensions
        int maxXOffset = (int) (enemyWidth * 0.25); // 25% of the enemy's width
        int maxYOffset = (int) (enemyHeight * 0.25); // 25% of the enemy's height

        if(target.getSpriteConfiguration().getImageType().equals(ImageEnums.SpaceStationBoss) ||
                target.getSpriteConfiguration().getImageType().equals(ImageEnums.RedBoss)){
            maxXOffset = (int) (enemyWidth * 0.1);
            maxYOffset = (int) (enemyHeight * 0.1);
        }

        // Determine the actual offsets (random amount within the allowed range)
        int xOffset = random.nextInt(maxXOffset + 1); // Random value between 0 and maxXOffset
        int yOffset = random.nextInt(maxYOffset + 1); // Random value between 0 and maxYOffset

        // Adjust offsets based on the increase/decrease direction
        if (!increaseXOffset) {
            xOffset = -xOffset; // Decrease the X offset if false
        }
        if (!increaseYOffset) {
            yOffset = -yOffset; // Decrease the Y offset if false
        }

        // Apply the calculated offsets to the animation's position
        animation.addXOffset(xOffset);
        animation.addYOffset(yOffset);
    }

    public static void scaleAnimation (GameObject target, SpriteAnimation animation) {
        animation.cropAnimation();
        // Retrieve animation dimensions
        int animationWidth = animation.getWidth();
        int animationHeight = animation.getHeight();

        // Retrieve target dimensions
        int enemyWidth = target.getWidth();
        int enemyHeight = target.getHeight();
        float maxScaleFactor = getMaxScaleByAnimEnum(animation.getImageEnum());

        // Calculate the maximum allowed dimensions
        int maxAllowedWidth = (int) (enemyWidth * maxScaleFactor);
        int maxAllowedHeight = (int) (enemyHeight * maxScaleFactor);

        // Calculate the current scale of the animation
        float currentScale = animation.getScale();


        // Determine if scaling adjustment is needed
        if (animationWidth > maxAllowedWidth || animationHeight > maxAllowedHeight) {
            // Calculate the necessary scale factors to fit within the target dimensions
            float widthScaleFactor = maxAllowedWidth / (float) animationWidth;
            float heightScaleFactor = maxAllowedHeight / (float) animationHeight;

            // Choose the larger scale factor to ensure the animation fits within both dimensions
            float newScaleFactor = Math.max(widthScaleFactor, heightScaleFactor);
            if(newScaleFactor < 0.4f){
                newScaleFactor = 0.4f;
            }

            // Apply the new scale, adjusting based on the current scale
            animation.setAnimationScale(currentScale * newScaleFactor);
        }
    }


    private static float getMaxScaleByAnimEnum(ImageEnums imageEnums){
        switch(imageEnums){
            case FreezeEffect:
                return 0.6f;
            case PlasmaCoatedDebuff:
                return 0.3f;
            default:
                return 0.25f;
        }
    }
}
