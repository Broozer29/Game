package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.carrier.ArbiterMultiTargeting;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProtossArbiter extends Drone {
    private ArrayList<GameObject> targetsToHeal = new ArrayList<>();
    private Map<GameObject, SpriteAnimation> animationsBelongingToTargets = new HashMap<>();
    private int maxTargetsAllowedToHealSimultaneously = 1;
    public static float healingRate = 0.075f;
    private boolean isMovingAroundCarrierDrone = false;


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
        GameObject player = PlayerManager.getInstance().getSpaceship();
        List<GameObject> targetableDrones = FriendlyManager.getInstance().getAllProtossDrones().stream()
                .filter(ship -> ship.getCurrentHitpoints() < ship.getMaxHitPoints())
                .filter(ship -> !(ship instanceof CarrierDrone))
                .collect(Collectors.toList());

        boolean shouldHealPlayer = player.getCurrentHitpoints() < player.getMaxHitPoints();

        // Prioritize healing the player if needed
        if (shouldHealPlayer && !targetsToHeal.contains(player)) {
            if (targetsToHeal.size() < maxTargetsAllowedToHealSimultaneously) {
                targetsToHeal.add(player);
                addAnimationToNewTarget(player);
            }
        }

        // Add additional drones to heal if slots are available
        for (GameObject drone : targetableDrones) {
            if (targetsToHeal.size() >= maxTargetsAllowedToHealSimultaneously) {
                break; // Stop adding if max limit is reached
            }

            if (!targetsToHeal.contains(drone)) {
                targetsToHeal.add(drone);
                addAnimationToNewTarget(drone);
            }
        }

        // Perform healing
        for (GameObject target : new ArrayList<>(targetsToHeal)) { // vermijd ConcurrentModificationException, al kan een iterator pattern ook
            healTarget(target);
            updateAnimationLocation(target);

            if (shouldRemoveTargetFromList(target)) {
                removeTargetFromList(target);
            }
        }
        rotateTowardsFirstTargetOrDestination();
    }

    private void rotateTowardsFirstTargetOrDestination() {
        GameObject targetToRotateTowards = null;

        //  Arbiter ignores itself if there are other targets
        for (GameObject target : targetsToHeal) {
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


    public void triggerOnDeathActions() {
        super.triggerOnDeathActions();
        targetsToHeal.clear();

        for (SpriteAnimation animation : animationsBelongingToTargets.values()) {
            animation.setVisible(false);
        }

        animationsBelongingToTargets.clear();
    }

    private void healTarget(GameObject target) {
        target.takeDamage(-healingRate * PlayerStats.getInstance().getArbiterHealingMultiplier()); //Negative cause it gets reversed in takeDamage
    }


    private void addAnimationToNewTarget(GameObject newTarget) {
        if (!animationsBelongingToTargets.containsKey(newTarget)) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(newTarget.getXCoordinate());
            spriteConfiguration.setyCoordinate(newTarget.getYCoordinate());
            spriteConfiguration.setScale(1);
            spriteConfiguration.setImageType(ImageEnums.Healing);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, true);
            SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
            animationsBelongingToTargets.put(newTarget, spriteAnimation);
            AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
        }
    }


    private boolean shouldRemoveTargetFromList(GameObject target) {
        if (!target.isVisible()
                || target.getCurrentHitpoints() <= 0
                || target.getCurrentHitpoints() >= target.getMaxHitPoints()
                || !FriendlyManager.getInstance().getAllProtossDrones().contains(target)) {  //The target died and cant be retrieved from FriendlyManager in the update loop, thus wont get checked for hitpoints/visibility
            return true;
        } else return false;
    }

    private void updateAnimationLocation(GameObject target) {
        this.animationsBelongingToTargets.get(target).setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
    }

    private void removeTargetFromList(GameObject target) {
        this.animationsBelongingToTargets.get(target).setVisible(false);
        this.targetsToHeal.remove(target);
        this.animationsBelongingToTargets.remove(target);
        AnimationManager.getInstance().getUpperAnimations().remove(animationsBelongingToTargets.get(target)); //Probably be overkill and should do nothing
    }

    private void updateMaxTargetsAvailable(){
        ArbiterMultiTargeting item = (ArbiterMultiTargeting) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ArbiterMultiTargeting);
        if(item != null){
            this.maxTargetsAllowedToHealSimultaneously = item.getQuantity() + 1;
        }
    }

}