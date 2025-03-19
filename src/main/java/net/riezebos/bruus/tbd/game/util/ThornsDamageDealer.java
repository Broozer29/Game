package net.riezebos.bruus.tbd.game.util;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLoggerManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.*;

public class ThornsDamageDealer {

    private static ThornsDamageDealer instance = new ThornsDamageDealer();
    private PlayerStats playerStats = PlayerStats.getInstance();
    private Map<GameObject, Integer> thornsApplyMap = new HashMap<>();
    private double lastThornsActivationTime = 0;
    private Random random;
    private PerformanceLogger performanceLogger = null;

    private ThornsDamageDealer () {
        playerStats = PlayerStats.getInstance();
        this.random = new Random();
        this.performanceLogger = new PerformanceLogger("Thorns Damage Dealer");
    }

    public static ThornsDamageDealer getInstance () {
        return instance;
    }


    public void resetThornsDamageDealer () {
        lastThornsActivationTime = 0;
        thornsApplyMap.clear();
        performanceLogger.reset();
    }


    public void updateGameTick () {
        //This method will be acticated every gametick, hence the "activateDelayedThornsAttacks()"
//        PerformanceLoggerManager.timeAndLog(performanceLogger, "Total", () -> {
            PerformanceLoggerManager.timeAndLog(performanceLogger, "Activate Delayed Thorn Attacks", this::activateDelayedThornsAttacks);
//            activateDelayedThornsAttacks();
//        });
    }


    public void addDelayedThornsDamageToObject (GameObject gameObject, int amountOftimes) {
        //Adds the gameObject and corresponding amountOfTimes if an object is NOT found
        //If the object IS found, combine the existing and new value by using Integers "sum" which adds 2 integers and returns the result
        thornsApplyMap.merge(gameObject, amountOftimes, Integer::sum);
    }

    private void activateDelayedThornsAttacks () {
        double currentTime = GameState.getInstance().getGameSeconds();

        // Ensure 0.1 second has passed since the last activation
        if (thornsApplyMap.isEmpty() || currentTime - lastThornsActivationTime < 0.15) {
            return;
        }

        Iterator<Map.Entry<GameObject, Integer>> iterator = thornsApplyMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<GameObject, Integer> entry = iterator.next();
            GameObject gameObject = entry.getKey();
            int remainingAttacks = entry.getValue();

            if (gameObject.isVisible() && gameObject.getCurrentHitpoints() > 0) {
                dealThornsDamageTo(gameObject);
                remainingAttacks--;

                if (remainingAttacks > 0) {
                    entry.setValue(remainingAttacks);
                } else {
                    iterator.remove();
                }
            } else {
                iterator.remove();
            }
        }

        // Update the last activation time
        lastThornsActivationTime = currentTime;
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
    }

    private void applyOnHitEffects (GameObject target) {
        float roll = random.nextFloat(); // Roll a number between 0.0 and 1.0
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

    public PerformanceLogger getPerformanceLogger () {
        return this.performanceLogger;
    }
}
