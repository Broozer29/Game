package net.riezebos.bruus.tbd.game.util;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HomingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLoggerManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
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
    private Map<Point, GameObject> thornsMissileMap = new HashMap<>();

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

    public void addThornsMissile(Point origin, GameObject target) {
        if(target == null || origin == null){
            return; //if this doesnt make sense, simply don't do it
        }
        Missile newMissile = createMissile(origin, target);
        MissileManager.getInstance().addExistingMissile(newMissile);
    }

    private Missile createMissile(Point origin, GameObject target) {
        int movementSpeed = 5;
        MissileCreator missileCreator1 = MissileCreator.getInstance();
        SpriteConfiguration spriteConfiguration = missileCreator1.createMissileSpriteConfig(origin.getX(), origin.getY(),
                ImageEnums.AlienLaserBeamAnimated, 1);


        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL; //Hardcoded, should be dynamic somewhere? Idk not decided how i want to use this behaviour yet
        MovementConfiguration movementConfiguration = missileCreator1.createMissileMovementConfig(
                movementSpeed, movementSpeed, new StraightLinePathFinder(), movementPatternSize, Direction.RIGHT
        );


        boolean isFriendly = true;

        int maxHitPoints = 100;
        int maxShields = 0;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Player Missile";
        float damage = playerStats.getNormalAttackDamage() * 2;
        boolean isExplosive = false;

        MissileConfiguration missileConfiguration = missileCreator1.createMissileConfiguration(MissileEnums.DefaultAnimatedBullet, maxHitPoints, maxShields,
                deathSound, damage, MissileEnums.DefaultAnimatedBullet.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType, MissileEnums.DefaultAnimatedBullet.isUsesBoxCollision(),
                isExplosive, true, false);

        PlayerStats instance = PlayerStats.getInstance();

        //piercing???
//        if (!isExplosive) {
//            missileConfiguration.setPiercesMissiles(instance.getPiercingMissilesAmount() > 0);
//            missileConfiguration.setAmountOfPierces(instance.getPiercingMissilesAmount());
//        }

        Missile missile = missileCreator1.createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        missile.setOwnerOrCreator(spaceship);
        missile.setCenterCoordinates(origin.getX(), origin.getY());
        missile.resetMovementPath();
        missile.getMovementConfiguration().setDestination(new Point(target.getCenterXCoordinate(), target.getCenterYCoordinate()));
        return missile;
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
                dealThornsDamageTo(gameObject, PlayerStats.getInstance().getThornsDamage());
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

    public void dealThornsDamageTo (GameObject target, float thornsDamage) {
        if (this.playerStats.getThornsDamage() <= 0 || target == null || !this.playerStats.isHasThornsEnabled()) {
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

        float damage = ArmorCalculator.calculateDamage(thornsDamage, target);
        target.takeDamage(damage);

        OnScreenTextManager.getInstance().addDamageNumberText(damage, target.getCenterXCoordinate(),
                target.getCenterYCoordinate(), false);
        AnimationManager.getInstance().addUpperAnimation(animation);
    }

    //Currently unused
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
