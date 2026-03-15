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

    public void fire(int xCoordinate, int yCoordinate, PlayerSpecialAttackTypes attackType, SpaceShip owner) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (specialAttackCharges > 0 && currentTime >= (lastSecondsSpecialAttackUsed + 0.15)) {
            switch (attackType) {
                case EMP:
                    fireElectroShred(xCoordinate, yCoordinate, owner);
                    break;
                case Rocket_Cluster:
                    break;
                case FlameShield:
                    fireFlameShield(xCoordinate, yCoordinate, owner);
                    break;
                case PlaceCarrierDrone:
                    handleCarrierSpecialAttack(owner);
                    break;
            }
            if (specialAttackCharges > 0) {
                specialAttackCharges--;
                lastSecondsSpecialAttackUsed = currentTime;
                // Reset the charge gain timer only if there's room for more charges
                if ((specialAttackCharges <= 0 && owner.getMaxSpecialAttackCharges() == 1) ||
                        (specialAttackCharges + 1 == owner.getMaxSpecialAttackCharges())) {
                    lastSecondsSpecialAttackChargeGained = currentTime; // Start new charge cooldown
                }
            }
        }
    }

    private void handleCarrierSpecialAttack(SpaceShip owner) {
        Drone carrierBeacon = FriendlyCreator.getCarrierBeacon(owner);
        if (carrierBeacon != null) {
            carrierBeacon.setVisible(false);
            placeAnimationAtCarrierBeacon(carrierBeacon);
            handleInverseRetrieval(carrierBeacon);
        } else {
            carrierBeacon = FriendlyCreator.createCarrierBeacon(owner);
            int xCoordinate = Math.round(owner.getXCoordinate() + owner.getWidth() + (carrierBeacon.getWidth() * 0.6f));
            carrierBeacon.setCenterCoordinates(xCoordinate, owner.getCenterYCoordinate());
            placeAnimationAtCarrierBeacon(carrierBeacon);
            FriendlyManager.getInstance().placeCarrierBeacon(carrierBeacon);
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

    private void fireFlameShield(int xCoordinate, int yCoordinate, SpaceShip owner) {
        float damage = owner.getSpecialAttackDamage();
        SpriteAnimationConfiguration spriteAnimationConfiguration = createConfig(xCoordinate, yCoordinate, ImageEnums.FireFighterFireShieldAppearing, 1, true);
        SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, true, true);
        SpecialAttack specialAttack = new FireShield(spriteAnimationConfiguration, missileConfiguration);
        specialAttack.setCenteredAroundObject(true);
        specialAttack.setScale(1.1f);
        specialAttack.setOwnerOrCreator(owner);
        AudioManager.getInstance().addAudio(AudioEnums.Firewall);
        owner.addFollowingSpecialAttack(specialAttack);
        MissileManager.getInstance().addSpecialAttack(specialAttack);
    }

    //Creates a special attack with an animation that follows the player
    private void fireElectroShred(int xCoordinate, int yCoordinate, SpaceShip owner) {
        float damage = owner.getSpecialAttackDamage() * 1.5f;
        SpriteAnimationConfiguration spriteAnimationConfiguration = createConfig(xCoordinate, yCoordinate, ImageEnums.Electroshred, 1, false);
        spriteAnimationConfiguration.setFrameDelay(3);

        if (playerStats.isHasImprovedElectroShred()) {
            spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.ElectroShredImproved);
        }

        SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, true, true);
        SpecialAttack specialAttack = new ElectroShred(spriteAnimationConfiguration, missileConfiguration);
        specialAttack.setCenteredAroundObject(true);
        specialAttack.setScale(1.5f);
        specialAttack.setOwnerOrCreator(owner);
        AudioManager.getInstance().addAudio(AudioEnums.Default_EMP);
        owner.addFollowingSpecialAttack(specialAttack);
        MissileManager.getInstance().addSpecialAttack(specialAttack);

    }

    public void updateFrameCount(SpaceShip owner) {
        double currentTime = GameState.getInstance().getGameSeconds();
        // Gain a new charge if enough time has passed since the last charge was gained and there is a slot available
        if (specialAttackCharges < owner.getMaxSpecialAttackCharges()) {
            if (currentTime >= lastSecondsSpecialAttackChargeGained + playerStats.getSpecialAttackCooldown()) {
                lastSecondsSpecialAttackChargeGained = currentTime;
                specialAttackCharges++;
                AudioManager.getInstance().addAudio(AudioEnums.SpecialAttackFinishedCharging);
            }
        }

        // Calculate time until next charge
        if (specialAttackCharges < owner.getMaxSpecialAttackCharges()) {
            secondsUntilNextSpecialAttackCharge = playerStats.getSpecialAttackCooldown() - (currentTime - lastSecondsSpecialAttackChargeGained);
        } else {
            secondsUntilNextSpecialAttackCharge = 0; // No charging if full
        }


    }

    public int getSpecialAttackCharges() {
        return this.specialAttackCharges;
    }

    public double getSecondsUntilNextAttackCharge() {
        return secondsUntilNextSpecialAttackCharge;
    }

}