package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.FlameThrower;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerPrimaryAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.captain.BigIron;
import net.riezebos.bruus.tbd.game.items.items.captain.HighVelocityLasers;
import net.riezebos.bruus.tbd.game.items.items.carrier.KineticDynamo;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.CustomAudioClip;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class PrimaryPlayerGun {

    private MissileManager missileManager = MissileManager.getInstance();
    private AudioManager audioManager = AudioManager.getInstance();
    private PlayerStats playerStats = PlayerStats.getInstance();
    private SpecialAttack channeledAttack = null;

    private double lastAttackTime = 0.0;
    private double timeChannelAttackGetsCleared = 0.0;

    private float orangeBarMaxValue = -10;
    private float orangeBarCurrentValue = -10;

    public PrimaryPlayerGun() {

    }

    public void fire(int xCoordinate, int yCoordinate, PlayerPrimaryAttackTypes playerAttackType, SpaceShip owner) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + owner.getAttackSpeed()) {
            lastAttackTime = currentTime;  // Update the last attack time

            if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleCommand) != null) {
                for (Drone drone : FriendlyManager.getInstance().getAllPlayerDrones(owner)) {
                    drone.fireAction();
                }
            }

            if (playerAttackType.equals(PlayerPrimaryAttackTypes.Laserbeam)) {
                handleRegularMissile(xCoordinate, yCoordinate, playerAttackType, owner);
            } else if (playerAttackType.equals(PlayerPrimaryAttackTypes.Flamethrower)) {
                startFiringFlameThrower(xCoordinate, yCoordinate, owner);
            } else if (playerAttackType.equals(PlayerPrimaryAttackTypes.Carrier)) {
                handleCarrierAttack(owner);
            }

        }
    }

    private boolean carrierFastSwitch = false;

    private void handleCarrierAttack(SpaceShip owner) {
        if (!carrierFastSwitch) {
            owner.isAllowedToBuildProtoss = false;
            carrierFastSwitch = true;
            owner.modifyMovementSpeedModifier(0.6f);
            AudioManager.getInstance().addAudio(AudioEnums.ClassCarrierSpeedingUp);
            addSwitchingGearAnimation(ImageEnums.ProtossCarrierSwitchFast, owner);
            updateKineticDynamo(true, owner);
        } else if (carrierFastSwitch) {
            owner.isAllowedToBuildProtoss = true;
            carrierFastSwitch = false;
            owner.modifyMovementSpeedModifier(-0.6f);
            AudioManager.getInstance().addAudio(AudioEnums.ClassCarrierSlowingDown);
            addSwitchingGearAnimation(ImageEnums.ProtossCarrierSwitchSlow, owner);
            updateKineticDynamo(false, owner);
        }
    }

    private void updateKineticDynamo(boolean newValue, GameObject owner) {
        KineticDynamo kineticDynamo = (KineticDynamo) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.KineticDynamo);
        if (kineticDynamo != null) {
            if (!newValue) {
                kineticDynamo.applyEffectToObject(owner);
            }
            kineticDynamo.isMovingFast = newValue;
        }

    }


    private void addSwitchingGearAnimation(ImageEnums imageType, SpaceShip owner) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(owner.getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(owner.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(imageType);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spriteAnimation.setCenterCoordinates(owner.getCenterXCoordinate(), owner.getCenterYCoordinate());
        owner.addPlayerFollowingAnimation(spriteAnimation);
        AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
    }

    private void handleRegularMissile(int xCoordinate, int yCoordinate, PlayerPrimaryAttackTypes playerAttackType, SpaceShip owner) {
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.BigIron) != null) {
            handleChargingLaserbeam(owner);
        } else {
            ImageEnums visualImage = playerStats.getPlayerMissileImage();
            PathFinder pathFinder = new RegularPathFinder();
            fireMissile(xCoordinate, yCoordinate, visualImage, 1, pathFinder, playerAttackType.getCorrespondingMissileEnum(), owner);
            playFiringAudio(playerAttackType);
        }
        orangeBarCurrentValue = -1;
        orangeBarMaxValue = -1;
    }

    private float totalScaleBonus = 0f;
    private float totalDamageBonus = 0f;
    private double secondsStartedCharging = 0.0d;
    private boolean isCharging = false;
    private SpriteAnimation chargingAnimation = this.initBigIronChargingAnimation();
    private CustomAudioClip chargingAudioClip = AudioDatabase.getInstance().getAudioClip(AudioEnums.ChargingBigIronLaserbeam);

    private void handleChargingLaserbeam(SpaceShip owner) {
        if (!isCharging) {
            isCharging = true;
            secondsStartedCharging = GameState.getInstance().getGameSeconds();
            chargingAnimation = this.initBigIronChargingAnimation(); //reset animation
            owner.addPlayerFollowingAnimation(chargingAnimation);
            AnimationManager.getInstance().addUpperAnimation(chargingAnimation);
            chargingAudioClip = AudioDatabase.getInstance().getAudioClip(AudioEnums.ChargingBigIronLaserbeam);
            chargingAudioClip.setLoop(true);
            totalScaleBonus = 0f;
            totalDamageBonus = 0f;
        }
        if (isCharging) {
            double timeCharged = GameState.getInstance().getGameSeconds() - secondsStartedCharging;
            int intervals = (int) (timeCharged / BigIron.interval);
            totalScaleBonus = intervals * (BigIron.scaleGrowthPerInterval + 0.025f); //klein beetje extra scaling op de charge anim
            totalDamageBonus = intervals * (BigIron.damagePerInterval * (1 + owner.getAttackSpeedModifier()));
            chargingAnimation.setAnimationScale(1 + totalScaleBonus);

            if(timeCharged >= 2 && !chargingAudioClip.isRunning()) {
                chargingAudioClip.startClip();
                chargingAudioClip.setLoop(true);
            }
//            chargingAnimation.setCenterCoordinates(owner.getCenterXCoordinate(), owner.getCenterYCoordinate()); //recenter the animation, might not be need
        }
        if (isCharging && secondsStartedCharging + BigIron.maxChargeSeconds < GameState.getInstance().getGameSeconds()) {
            releaseChargingLaserbeam(owner);
        }
    }

    public void releaseChargingLaserbeam(SpaceShip owner) {
        //release it
        if (chargingAnimation != null) {
            chargingAnimation.setVisible(false);
        }
        if(chargingAudioClip != null){
            chargingAudioClip.setLoop(false);
            chargingAudioClip.stopClip();
        }

        if(!isCharging){
            return; //if we werent charging, dont fire anything
        }
        ImageEnums visualImage = playerStats.getPlayerMissileImage();
        PathFinder pathFinder = new RegularPathFinder();
        fireMissile(owner.getXCoordinate() + owner.getWidth(), owner.getCenterYCoordinate(), visualImage, 1 * (1 + totalScaleBonus), pathFinder, PlayerPrimaryAttackTypes.Laserbeam.getCorrespondingMissileEnum(), owner);
        playFiringAudio(PlayerPrimaryAttackTypes.Laserbeam);
        totalScaleBonus = 0f;
        totalDamageBonus = 0f;
        lastAttackTime = GameState.getInstance().getGameSeconds();
        isCharging = false;
    }


    // Fuel tank mechanics
    private final float FUEL_DEPLETION_RATE = 0.475f;
    private final float FUEL_REGENERATION_RATE = 0.35f;
    private final float FUEL_MINIMUM_REQUIRED = 10f;
    public static float fireFighterBonusDamageRatio = 1.15f;

    private void startFiringFlameThrower(int xCoordinate, int yCoordinate, SpaceShip owner) {
        if (this.channeledAttack == null && orangeBarCurrentValue >= FUEL_MINIMUM_REQUIRED) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(xCoordinate);
            spriteConfiguration.setyCoordinate(yCoordinate);
            spriteConfiguration.setImageType(ImageEnums.FireFighterFlameThrowerAppearing);

            float damage = owner.getDamage() * fireFighterBonusDamageRatio;


            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, true);
            SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, false, true);
            SpecialAttack specialAttack = new FlameThrower(spriteAnimationConfiguration, missileConfiguration);
            specialAttack.setCenteredAroundObject(true);
            specialAttack.setScale(0.9f);
            specialAttack.addXOffset((specialAttack.getAnimation().getWidth() / 2) - Math.round((specialAttack.getAnimation().getWidth() * 0.005f)));
            specialAttack.setOwnerOrCreator(owner);
            owner.addFollowingSpecialAttack(specialAttack);
            this.channeledAttack = specialAttack;
            updateFlameThrowerDamageFromInfernalPreIgniter(this.channeledAttack);
            MissileManager.getInstance().addSpecialAttack(specialAttack);
            AudioManager.getInstance().addAudio(AudioEnums.Firewall);
        }
    }

    private void updateFlameThrowerDamageFromInfernalPreIgniter(SpecialAttack specialAttack) {
        //deprecated item effect but still functional, can be reused for a different item now that infernal preigniter has been reworked
//        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.InfernalPreIgniter) != null){
//            specialAttack.setDamage(playerStats.getNormalAttackDamage() * ((this.orangeBarCurrentValue / this.orangeBarMaxValue) * InfernalPreIgniter.maxDamageBonnus));
//        }
    }


    public void stopFiring(SpaceShip owner) {
        if (this.channeledAttack != null && !this.channeledAttack.isDissipating()) {
            this.channeledAttack.startDissipating();
            timeChannelAttackGetsCleared = GameState.getInstance().getGameSeconds();
        }
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.BigIron) != null) {
            releaseChargingLaserbeam(owner);
        }
    }

    private void playFiringAudio(PlayerPrimaryAttackTypes playerAttackType) {
        switch (playerAttackType) {
            case Laserbeam -> playMissileAudio(AudioEnums.NewPlayerLaserbeam);
            case Flamethrower -> playMissileAudio(AudioEnums.Firewall);
        }
    }


    private void fireMissile(int xCoordinate, int yCoordinate, ImageEnums playerMissileType,
                             float missileScale, PathFinder missilePathFinder, MissileEnums attackType, SpaceShip owner) {
        int movementSpeed = 6;
        MissileCreator missileCreator1 = MissileCreator.getInstance();
        SpriteConfiguration spriteConfiguration = missileCreator1.createMissileSpriteConfig(xCoordinate, yCoordinate,
                playerMissileType, missileScale);


        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL; //Hardcoded, should be dynamic somewhere? Idk not decided how i want to use this behaviour yet
        MovementConfiguration movementConfiguration = missileCreator1.createMissileMovementConfig(
                movementSpeed, movementSpeed, missilePathFinder, movementPatternSize, Direction.RIGHT
        );


        boolean isFriendly = true;

        int maxHitPoints = 100;
        int maxShields = 0;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Player Missile";
        float damage = owner.getDamage() * 2;


        damage *= 1 + (totalDamageBonus); //bigiron dmg bonus
        boolean isExplosive = false;


//        damage = 2000;
        MissileConfiguration missileConfiguration = missileCreator1.createMissileConfiguration(attackType, maxHitPoints, maxShields,
                deathSound, damage, attackType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType, attackType.isUsesBoxCollision(),
                isExplosive, true, false);

        PlayerStats instance = PlayerStats.getInstance();
        if (!isExplosive) {
            missileConfiguration.setPiercesMissiles(instance.getPiercingMissilesAmount() > 0);
            missileConfiguration.setAmountOfPierces(instance.getPiercingMissilesAmount());
        }
        Missile missile = missileCreator1.createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.HighVelocityLasers) != null) {
            HighVelocityLasers lasers = (HighVelocityLasers) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.HighVelocityLasers);
            lasers.applyEffectToObject(missile);
        }

        missile.setOwnerOrCreator(owner);
        missile.setCenterCoordinates(missile.getCenterXCoordinate(), owner.getCenterYCoordinate());
        missile.resetMovementPath();

        missile.setCanBounce(true);

        this.missileManager.addExistingMissile(missile);

    }

    private void playMissileAudio(AudioEnums audioEnum) {
        this.audioManager.addAudio(audioEnum);
    }

    public void updateFrameCount(SpaceShip owner) {
        if (channeledAttack != null && channeledAttack.isDissipating()) {
            if ((timeChannelAttackGetsCleared + 0.5d) < GameState.getInstance().getGameSeconds()) {
                channeledAttack = null;
            }
        }

        if (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter)) {
            if (orangeBarMaxValue < 0) {
                orangeBarMaxValue = 125 * owner.getFuelCannisterMaxCapacityModifier();
            }

            if (orangeBarCurrentValue < 0) {
                orangeBarCurrentValue = orangeBarMaxValue;
            }

            if (this.channeledAttack == null && orangeBarCurrentValue < orangeBarMaxValue) {
                orangeBarCurrentValue += FUEL_REGENERATION_RATE * owner.getFuelCannisterRegenModifier();
                if (orangeBarCurrentValue > orangeBarMaxValue) {
                    orangeBarCurrentValue = orangeBarMaxValue; // Clamp at max value
                }
            }

            if (channeledAttack != null) {
                if (orangeBarCurrentValue <= 0) {
                    // If no fuel is available, prevent firing
                    stopFiring(owner);
                    return;
                }

                // Deplete fuel while firing
                updateFlameThrowerDamageFromInfernalPreIgniter(this.channeledAttack);
                orangeBarCurrentValue -= Math.max(FUEL_DEPLETION_RATE * owner.getFuelCannisterUsageModifier(), 0.01f);
                if (orangeBarCurrentValue < 0) {
                    orangeBarCurrentValue = 0; // Clamp at 0
                }
            }
        }
    }

    public float getOrangeBarMaxValue(SpaceShip owner) {
        if (playerStats.getPlayerClass().equals(PlayerClass.Carrier)) {
            return owner.getProtossShipBuildTime();
        }
        return orangeBarMaxValue;
    }

    public float getOrangeBarCurrentValue(SpaceShip owner) {
        if (playerStats.getPlayerClass().equals(PlayerClass.Carrier)) {
            return owner.getProtossShipBuilderTimer();
        }
        return orangeBarCurrentValue;
    }


    private SpriteAnimation initBigIronChargingAnimation() {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(-300);
        spriteConfiguration.setyCoordinate(-300);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.Charging);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, true);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spriteAnimation.addXOffset(60);
        return spriteAnimation;
    }
}