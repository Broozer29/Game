package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.ElectroShred;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.FireShield;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerSpecialAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.carrier.InverseRetrieval;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class SpaceShipSpecialGun {
    private PlayerStats playerStats = PlayerStats.getInstance();

    private int specialAttackCharges;
    private double lastSecondsSpecialAttackUsed = 0.0;
    private double lastSecondsSpecialAttackChargeGained = 0.0;
    private double secondsUntilNextSpecialAttackCharge = 0.0;

    public SpaceShipSpecialGun() {
        specialAttackCharges = 1;
    }

    public void fire(int xCoordinate, int yCoordinate, PlayerSpecialAttackTypes attackType) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (specialAttackCharges > 0 && currentTime >= (lastSecondsSpecialAttackUsed + 0.15)) {
            switch (attackType) {
                case EMP:
                    fireElectroShred(xCoordinate, yCoordinate);
                    break;
                case Rocket_Cluster:
                    break;
                case FlameShield:
                    fireFlameShield(xCoordinate, yCoordinate);
                    break;
                case PlaceCarrierDrone:
                    handleCarrierSpecialAttack();
                    break;
            }
            if (specialAttackCharges > 0) {
                specialAttackCharges--;
                lastSecondsSpecialAttackUsed = currentTime;
                // Reset the charge gain timer only if there's room for more charges
                if ((specialAttackCharges <= 0 && playerStats.getMaxSpecialAttackCharges() == 1) ||
                        (specialAttackCharges + 1 == playerStats.getMaxSpecialAttackCharges())) {
                    lastSecondsSpecialAttackChargeGained = currentTime; // Start new charge cooldown
                }
            }
        }
    }

    private void handleCarrierSpecialAttack() {
        Drone carrierBeacon = FriendlyCreator.getCarrierBeacon();
        if (carrierBeacon != null) {
            carrierBeacon.setVisible(false);
            placeAnimationAtCarrierBeacon(carrierBeacon);
            handleInverseRetrieval(carrierBeacon);
        } else {
            GameObject player = PlayerManager.getInstance().getSpaceship();
            carrierBeacon = FriendlyCreator.createCarrierBeacon();
            int xCoordinate = Math.round(player.getXCoordinate() + player.getWidth() + (carrierBeacon.getWidth() * 0.6f));
            carrierBeacon.setCenterCoordinates(xCoordinate,
                    player.getCenterYCoordinate());
            placeAnimationAtCarrierBeacon(carrierBeacon);
            FriendlyManager.getInstance().addDrone(carrierBeacon);
        }
    }

    private void handleInverseRetrieval(GameObject carrierBeacon){
        InverseRetrieval item = (InverseRetrieval) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.InverseRetrieval);
        if(item != null){
            item.applyEffectToObject(carrierBeacon);
        }
    }

    private void placeAnimationAtCarrierBeacon(GameObject carrierDrone) {
        SpriteAnimation spriteAnimation = new SpriteAnimation(createConfig(carrierDrone.getCenterXCoordinate(), carrierDrone.getCenterYCoordinate(),
                ImageEnums.SelectNewClassAnimation, 1, false));
        spriteAnimation.setFrameDelay(1);
        spriteAnimation.setCenterCoordinates(carrierDrone.getCenterXCoordinate(), carrierDrone.getCenterYCoordinate());
        AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
    }


    private SpriteAnimationConfiguration createConfig(int xCoordinate, int yCoordinate, ImageEnums imageEnums, float scale, boolean loop) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setImageType(imageEnums);
        spriteConfiguration.setScale(scale);

        return new SpriteAnimationConfiguration(spriteConfiguration, 2, loop);

    }

    private void fireFlameShield(int xCoordinate, int yCoordinate) {
        float damage = PlayerStats.getInstance().getSpecialDamage();
        SpriteAnimationConfiguration spriteAnimationConfiguration = createConfig(xCoordinate, yCoordinate, ImageEnums.FireFighterFireShieldAppearing, 1, true);
        SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, true, true);
        SpecialAttack specialAttack = new FireShield(spriteAnimationConfiguration, missileConfiguration);
        specialAttack.setCenteredAroundObject(true);
        specialAttack.setScale(1.1f);
        specialAttack.setOwnerOrCreator(PlayerManager.getInstance().getSpaceship());
        AudioManager.getInstance().addAudio(AudioEnums.Firewall);
        PlayerManager.getInstance().getSpaceship().addFollowingSpecialAttack(specialAttack);
        MissileManager.getInstance().addSpecialAttack(specialAttack);
    }

    //Creates a special attack with an animation that follows the player
    private void fireElectroShred(int xCoordinate, int yCoordinate) {
        float damage = playerStats.getSpecialDamage();
        SpriteAnimationConfiguration spriteAnimationConfiguration = createConfig(xCoordinate, yCoordinate, ImageEnums.Electroshred, 1, false);
        spriteAnimationConfiguration.setFrameDelay(3);

        if (playerStats.isHasImprovedElectroShred()) {
            spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.ElectroShredImproved);
        }

        SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, true, true);
        SpecialAttack specialAttack = new ElectroShred(spriteAnimationConfiguration, missileConfiguration);
        specialAttack.setCenteredAroundObject(true);
        specialAttack.setScale(1.5f);
        specialAttack.setOwnerOrCreator(PlayerManager.getInstance().getSpaceship());
        AudioManager.getInstance().addAudio(AudioEnums.Default_EMP);
        PlayerManager.getInstance().getSpaceship().addFollowingSpecialAttack(specialAttack);
        MissileManager.getInstance().addSpecialAttack(specialAttack);

    }

    public void updateFrameCount() {
        double currentTime = GameState.getInstance().getGameSeconds();
        // Gain a new charge if enough time has passed since the last charge was gained and there is a slot available
        if (specialAttackCharges < playerStats.getMaxSpecialAttackCharges()) {
            if (currentTime >= lastSecondsSpecialAttackChargeGained + playerStats.getSpecialAttackCooldown()) {
                lastSecondsSpecialAttackChargeGained = currentTime;
                specialAttackCharges++;
                AudioManager.getInstance().addAudio(AudioEnums.SpecialAttackFinishedCharging);
            }
        }

        // Calculate time until next charge
        if (specialAttackCharges < playerStats.getMaxSpecialAttackCharges()) {
            secondsUntilNextSpecialAttackCharge = playerStats.getSpecialAttackCooldown() - (currentTime - lastSecondsSpecialAttackChargeGained);
        } else {
            secondsUntilNextSpecialAttackCharge = 0; // No charging if full
        }


    }

    public int getSpecialAttackCharges() {
        return this.specialAttackCharges;
    }

    public double getCurrentSpecialAttackFrame() {
        return secondsUntilNextSpecialAttackCharge;
    }
}