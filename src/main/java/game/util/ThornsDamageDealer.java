package game.util;

import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.GameObject;
import game.gameobjects.player.PlayerManager;
import game.gameobjects.player.PlayerStats;
import game.items.Item;
import game.items.PlayerInventory;
import game.items.enums.ItemApplicationEnum;
import game.items.items.ThornedPlates;
import game.managers.AnimationManager;
import game.managers.OnScreenTextManager;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.List;
import java.util.Random;

public class ThornsDamageDealer {

    private static ThornsDamageDealer instance = new ThornsDamageDealer();
    private PlayerStats playerStats = PlayerStats.getInstance();

    private ThornsDamageDealer () {
        playerStats = PlayerStats.getInstance();
    }

    public static ThornsDamageDealer getInstance () {
        return instance;
    }

    public void dealThornsDamageTo (GameObject target) {
        if (this.playerStats.getThornsDamage() <= 0 || target == null) {
            return;
        }

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(target.getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(target.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.ThornsDamage);

        SpriteAnimationConfiguration animConfig = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        SpriteAnimation animation = new SpriteAnimation(animConfig);
        scaleAnimationToTarget(target, animation);
        animation.setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());

        float damage = ArmorCalculator.calculateDamage(playerStats.getThornsDamage(), target);
        target.takeDamage(damage);

        OnScreenTextManager.getInstance().addDamageNumberText(damage, target.getCenterXCoordinate(),
                target.getCenterYCoordinate(), false);
        AnimationManager.getInstance().addUpperAnimation(animation);


        if (playerStats.getChanceForThornsToApplyOnHitEffects() > 0.0) {
            applyOnHitEffects(target);
        }
    }

    private void applyOnHitEffects (GameObject target) {
        float roll = new Random().nextFloat(); // Roll a number between 0.0 and 1.0
        if (roll < playerStats.getChanceForThornsToApplyOnHitEffects()) {
            List<Item> onHitItems = PlayerInventory.getInstance().getItemsByApplicationMethod(ItemApplicationEnum.AfterCollision);
            for (Item item : onHitItems) {
                item.applyEffectToObject(target);
                item.applyEffectToObject(PlayerManager.getInstance().getSpaceship(), target);
            }
        }
    }


    private void scaleAnimationToTarget (GameObject target, SpriteAnimation animation) {
        animation.cropAnimation();
        // Retrieve animation dimensions
        int animationWidth = animation.getWidth();
        int animationHeight = animation.getHeight();

        // Retrieve target dimensions
        int enemyWidth = target.getWidth();
        int enemyHeight = target.getHeight();

        // Calculate the maximum allowed dimensions
        int maxAllowedWidth = (int) (enemyWidth * 0.9);
        int maxAllowedHeight = (int) (enemyHeight * 0.9);

        // Calculate the current scale of the animation
        float currentScale = animation.getScale();


        // Determine if scaling adjustment is needed
        if (animationWidth > maxAllowedWidth || animationHeight > maxAllowedHeight) {
            // Calculate the necessary scale factors to fit within the target dimensions
            float widthScaleFactor = maxAllowedWidth / (float) animationWidth;
            float heightScaleFactor = maxAllowedHeight / (float) animationHeight;

            // Choose the larger scale factor to ensure the animation fits within both dimensions
            float newScaleFactor = Math.max(widthScaleFactor, heightScaleFactor);

            // Apply the new scale, adjusting based on the current scale
            animation.setAnimationScale(currentScale * newScaleFactor);
        }
    }
}
