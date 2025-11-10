package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.spaceships.AlienBomb;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.carrier.ArbiterDamage;
import net.riezebos.bruus.tbd.game.items.items.carrier.ArbiterMultiTargeting;
import net.riezebos.bruus.tbd.game.items.items.carrier.EmergencyRepairs;
import net.riezebos.bruus.tbd.game.items.items.carrier.VengeanceProtocol;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.*;
import java.util.stream.Collectors;

public class ProtossArbiter extends Drone {
    private ArrayList<GameObject> targets = new ArrayList<>();
    private Map<GameObject, SpriteAnimation> animationsBelongingToTargets = new HashMap<>();
    private int maxTargetsAllowedSimultaneously = 1;
    public static float healingRate = 0.135f;
    private boolean isMovingAroundCarrierDrone = false;
    private boolean canAcquireTarget = true;

    public ProtossArbiter(SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
        super.isProtoss = true;
        this.maxHitPoints = PlayerStats.getInstance().getProtossShipBaseHealth();
        this.currentHitpoints = maxHitPoints;
        this.baseArmor = PlayerStats.getInstance().getProtossShipBaseArmor();
        super.initProtossDeathExplosion();
        this.movementConfiguration.setXMovementSpeed(2.5f);
        this.movementConfiguration.setYMovementSpeed(2.5f);
        super.droneType = DroneTypes.ProtossArbiter;
        super.deathSound = AudioEnums.ProtossShipDeath;
        super.appliesOnHitEffects = true;
        updateMaxTargetsAvailable();
    }

    public void activateObject() {
        if (this.getCurrentLocation().equals(this.movementConfiguration.getDestination())) {
            this.movementConfiguration.resetMovementPath();
            this.movementConfiguration.setCurrentLocation(new Point(this.getXCoordinate(), this.getYCoordinate()));
            this.setAllowedVisualsToRotate(true);
            this.movementConfiguration.setDestination(ProtossUtils.getRandomPoint());
            this.isMovingAroundCarrierDrone = ProtossUtils.carrierDroneIsPresent();
        }

        if (!ProtossUtils.carrierDroneIsPresent() && this.isMovingAroundCarrierDrone) {
            immediatlyReturnToCarrier();
        }

        fireAction();
    }

    private void immediatlyReturnToCarrier() {
        this.movementConfiguration.resetMovementPath();
        this.movementConfiguration.setCurrentLocation(new Point(this.getXCoordinate(), this.getYCoordinate()));
        this.setAllowedVisualsToRotate(true);
        this.movementConfiguration.setDestination(ProtossUtils.getRandomPoint());
        this.isMovingAroundCarrierDrone = ProtossUtils.carrierDroneIsPresent();
    }


    public void fireAction() {
        // Acquire player and targetable drones
        if (!canAcquireTarget) {
            return;
        }

        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ArbiterDamage) != null) {
            handleDamage();
        } else {
            handleHealing();
        }

        rotateTowardsFirstTargetOrDestination();
    }

    private void handleHealing() {
        // Perform healing
        for (GameObject target : new ArrayList<>(targets)) { // vermijd ConcurrentModificationException, al kan een iterator pattern ook
            healTarget(target);
            updateAnimationLocation(target);

            if (shouldRemoveTargetFromHealingList(target)) {
                removeTargetFromList(target);
            }
        }

        if (targets.size() >= maxTargetsAllowedSimultaneously) {
            return; // Stop adding if max limit is reached
        }

        GameObject player = PlayerManager.getInstance().getSpaceship();
        List<GameObject> targetableDrones = FriendlyManager.getInstance().getAllProtossDrones().stream()
                .filter(ship -> ship.getCurrentHitpoints() < ship.getMaxHitPoints())
                .filter(ship -> !(ship instanceof CarrierDrone))
                .collect(Collectors.toList());

        boolean shouldHealPlayer = player.getCurrentHitpoints() < player.getMaxHitPoints();

        // Prioritize healing the player if needed
        if (shouldHealPlayer && !targets.contains(player)) {
            if (targets.size() < maxTargetsAllowedSimultaneously) {
                targets.add(player);
                addAnimationToNewTarget(player, true);
            }
        }

        // Add additional drones to heal if slots are available
        for (GameObject drone : targetableDrones) {
            if (targets.size() >= maxTargetsAllowedSimultaneously) {
                break; // Stop adding if max limit is reached
            }
            if (!targets.contains(drone)) {
                targets.add(drone);
                addAnimationToNewTarget(drone, true);
            }
        }
    }

    private void handleDamage() {
        for (GameObject target : new ArrayList<>(targets)) { // vermijd ConcurrentModificationException, al kan een iterator pattern ook
            damageTarget(target);
            updateAnimationLocation(target);

            if (shouldRemoveTargetFromDamageList(target)) {
                removeTargetFromList(target);
            }
        }

        if (targets.size() >= maxTargetsAllowedSimultaneously) {
            return; // Stop adding if max limit is reached
        }

        List<GameObject> targetableEnemies = EnemyManager.getInstance().getEnemies().stream()
                .filter(enemy -> !(enemy instanceof AlienBomb) && enemy.getCurrentHitpoints() > 0 && enemy.isVisible() && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy))
                .collect(Collectors.toList());
        Collections.shuffle(targetableEnemies); //randomly select an enemy from the pool

        for (GameObject enemy : targetableEnemies) {
            if (targets.size() >= maxTargetsAllowedSimultaneously) {
                break; // Stop adding if max limit is reached
            }
            if (!targets.contains(enemy)) {
                targets.add(enemy);
                addAnimationToNewTarget(enemy, false);
            }
        }
    }

    private void rotateTowardsFirstTargetOrDestination() {
        GameObject targetToRotateTowards = null;

        //  Arbiter ignores itself if there are other targets
        for (GameObject target : targets) {
            if (target != this) { // Skip itself if there's another target
                targetToRotateTowards = target;
                break;
            }
        }

        if (targetToRotateTowards != null) {
            // Rotate towards the first valid healing target
            this.setAllowedVisualsToRotate(true);
            this.rotateObjectTowardsPoint(new Point(targetToRotateTowards.getCenterXCoordinate(), targetToRotateTowards.getCenterYCoordinate()), true);
            this.setAllowedVisualsToRotate(false);
        } else {
            // Rotate towards the movement destination
            this.setAllowedVisualsToRotate(true);
            this.rotateObjectTowardsDestination(true);
            this.setAllowedVisualsToRotate(false);
        }
    }


    @Override
    public void triggerOnDeathActions() {
        super.triggerOnDeathActions();
        this.canAcquireTarget = false;

        for (SpriteAnimation animation : animationsBelongingToTargets.values()) {
            animation.setVisible(false);
            animation.setInfiniteLoop(false);
            animation.setCenterCoordinates(-500, -500);
            AnimationManager.getInstance().getUpperAnimations().remove(animation);
        }


        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EmergencyRepairs) != null) {
            EmergencyRepairs emergencyRepairs = (EmergencyRepairs) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EmergencyRepairs);
            emergencyRepairs.applyEffectToObject(PlayerManager.getInstance().getSpaceship());
        }

        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.VengeanceProtocol) != null) {
            VengeanceProtocol vengeanceProtocol = (VengeanceProtocol) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.VengeanceProtocol);
            vengeanceProtocol.applyEffectToObject(this);
        }

        targets.clear();
        animationsBelongingToTargets.clear();
    }

    private void healTarget(GameObject target) {
        target.takeDamage(-healingRate * PlayerStats.getInstance().getArbiterHealingMultiplier()); //Negative cause it gets reversed in takeDamage
    }

    private void damageTarget(GameObject target) {
        target.takeDamage((healingRate * PlayerStats.getInstance().getArbiterHealingMultiplier()) * ArbiterDamage.damageIncreaseMultiplier);
    }

    private void addAnimationToNewTarget(GameObject newTarget, boolean isHealing) {
        if (!animationsBelongingToTargets.containsKey(newTarget)) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(newTarget.getXCoordinate());
            spriteConfiguration.setyCoordinate(newTarget.getYCoordinate());
            spriteConfiguration.setScale(1);

            if (isHealing) {
                spriteConfiguration.setImageType(ImageEnums.Healing);
            } else {
                spriteConfiguration.setImageType(ImageEnums.ReverseHealing);
            }

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, true);
            SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
            animationsBelongingToTargets.put(newTarget, spriteAnimation);
            AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
        }
    }


    private boolean shouldRemoveTargetFromHealingList(GameObject target) {
        if (!target.isVisible()
                || target.getCurrentHitpoints() <= 0
                || target.getCurrentHitpoints() >= target.getMaxHitPoints()
                || !FriendlyManager.getInstance().getAllProtossDrones().contains(target)) {  //The target died and cant be retrieved from FriendlyManager in the update loop, thus wont get checked for hitpoints/visibility
            return true;
        } else return false;
    }

    private boolean shouldRemoveTargetFromDamageList(GameObject target) {
        if (!target.isVisible()
                || target.getCurrentHitpoints() <= 0
                || !EnemyManager.getInstance().getEnemies().contains(target)) {  //The target died and cant be retrieved from EnemyManager in the update loop, thus wont get checked for hitpoints/visibility
            return true;
        } else return false;
    }

    private void updateAnimationLocation(GameObject target) {
        this.animationsBelongingToTargets.get(target).setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
    }

    private void removeTargetFromList(GameObject target) {
        this.animationsBelongingToTargets.get(target).setVisible(false);
        this.targets.remove(target);
        this.animationsBelongingToTargets.remove(target);
        AnimationManager.getInstance().getUpperAnimations().remove(animationsBelongingToTargets.get(target)); //Probably be overkill and should do nothing
    }

    private void updateMaxTargetsAvailable() {
        ArbiterMultiTargeting item = (ArbiterMultiTargeting) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ArbiterMultiTargeting);
        if (item != null) {
            this.maxTargetsAllowedSimultaneously = item.getQuantity() + 1;
        }
    }

}