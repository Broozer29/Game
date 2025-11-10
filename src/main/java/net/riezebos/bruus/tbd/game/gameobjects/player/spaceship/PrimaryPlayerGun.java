package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.FlameThrower;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerPrimaryAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.captain.HighVelocityLasers;
import net.riezebos.bruus.tbd.game.items.items.carrier.KineticDynamo;
import net.riezebos.bruus.tbd.game.items.items.firefighter.InfernalPreIgniter;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HomingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
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

    public void fire(int xCoordinate, int yCoordinate, PlayerPrimaryAttackTypes playerAttackType) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + playerStats.getAttackSpeed()) {
            lastAttackTime = currentTime;  // Update the last attack time

            if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleCommand) != null) {
                for (Drone drone : FriendlyManager.getInstance().getAllPlayerDrones()) {
                    drone.fireAction();
                }
            }

            if (playerAttackType.equals(PlayerPrimaryAttackTypes.Laserbeam)) {
                handleRegularMissile(xCoordinate, yCoordinate, playerAttackType);
            } else if (playerAttackType.equals(PlayerPrimaryAttackTypes.Flamethrower)) {
                startFiringFlameThrower(xCoordinate, yCoordinate);
            } else if (playerAttackType.equals(PlayerPrimaryAttackTypes.Carrier)) {
                handleCarrierAttack();
            }

        }
    }

    private boolean carrierFastSwitch = false;

    private void handleCarrierAttack() {
        if (!carrierFastSwitch) {
            ProtossUtils.getInstance().setAllowedToBuildProtoss(false);
            carrierFastSwitch = true;
            PlayerStats.getInstance().setMovementSpeed(PlayerStats.carrierFastSpeed);
            AudioManager.getInstance().addAudio(AudioEnums.ClassCarrierSpeedingUp);
            addSwitchingGearAnimation(ImageEnums.ProtossCarrierSwitchFast);
            updateKineticDynamo(true);
        } else if (carrierFastSwitch) {
            ProtossUtils.getInstance().setAllowedToBuildProtoss(true);
            carrierFastSwitch = false;
            PlayerStats.getInstance().setMovementSpeed(PlayerStats.carrierSlowSpeed);
            AudioManager.getInstance().addAudio(AudioEnums.ClassCarrierSlowingDown);
            addSwitchingGearAnimation(ImageEnums.ProtossCarrierSwitchSlow);
            updateKineticDynamo(false);
        }
    }

    private void updateKineticDynamo(boolean newValue){
        KineticDynamo kineticDynamo = (KineticDynamo) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.KineticDynamo);
        if(kineticDynamo != null){
            if(!newValue){
                kineticDynamo.applyEffectToObject(PlayerManager.getInstance().getSpaceship());
            }
            kineticDynamo.isMovingFast = newValue;
        }

    }


    private void addSwitchingGearAnimation(ImageEnums imageType) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        SpaceShip spaceShip = PlayerManager.getInstance().getSpaceship();
        spriteConfiguration.setxCoordinate(spaceShip.getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(spaceShip.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(imageType);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spriteAnimation.setCenterCoordinates(spaceShip.getCenterXCoordinate(), spaceShip.getCenterYCoordinate());
        spaceShip.addPlayerFollowingAnimation(spriteAnimation);
        AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
    }

    private void handleRegularMissile(int xCoordinate, int yCoordinate, PlayerPrimaryAttackTypes playerAttackType) {
        ImageEnums visualImage = playerStats.getPlayerMissileImage();
        float scale = playerStats.getMissileScale();
        PathFinder pathFinder = new RegularPathFinder();
        fireMissile(xCoordinate, yCoordinate, visualImage, scale, pathFinder, playerAttackType.getCorrespondingMissileEnum());
        playFiringAudio(playerAttackType);

        orangeBarCurrentValue = -1;
        orangeBarMaxValue = -1;
    }


    // Fuel tank mechanics
    private final float FUEL_DEPLETION_RATE = 0.475f;
    private final float FUEL_REGENERATION_RATE = 0.35f;
    private final float FUEL_MINIMUM_REQUIRED = 10f;
    public static float fireFighterBonusDamageRatio = 1.15f;

    private void startFiringFlameThrower(int xCoordinate, int yCoordinate) {
        if (this.channeledAttack == null && orangeBarCurrentValue >= FUEL_MINIMUM_REQUIRED) {


            PlayerManager playerManager = PlayerManager.getInstance();
            SpaceShip spaceShip = playerManager.getSpaceship();

            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(xCoordinate);
            spriteConfiguration.setyCoordinate(yCoordinate);
            spriteConfiguration.setImageType(ImageEnums.FireFighterFlameThrowerAppearing);

            float damage = playerStats.getNormalAttackDamage() * fireFighterBonusDamageRatio;


            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, true);
            SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, false, true);
            SpecialAttack specialAttack = new FlameThrower(spriteAnimationConfiguration, missileConfiguration);
            specialAttack.setCenteredAroundObject(true);
            specialAttack.setScale(0.9f);
            specialAttack.addXOffset((specialAttack.getAnimation().getWidth() / 2) - Math.round((specialAttack.getAnimation().getWidth() * 0.005f)));
            specialAttack.setOwnerOrCreator(PlayerManager.getInstance().getSpaceship());
            spaceShip.addFollowingSpecialAttack(specialAttack);
            this.channeledAttack = specialAttack;
            updateFlameThrowerDamageFromInfernalPreIgniter(this.channeledAttack);
            MissileManager.getInstance().addSpecialAttack(specialAttack);
            AudioManager.getInstance().addAudio(AudioEnums.Firewall);
        }
    }

    private void updateFlameThrowerDamageFromInfernalPreIgniter(SpecialAttack specialAttack){
        //deprecated item effect but still functional, can be reused for a different item now that infernal preigniter has been reworked
//        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.InfernalPreIgniter) != null){
//            specialAttack.setDamage(playerStats.getNormalAttackDamage() * ((this.orangeBarCurrentValue / this.orangeBarMaxValue) * InfernalPreIgniter.maxDamageBonnus));
//        }
    }


    public void stopFiring() {
        if (this.channeledAttack != null && !this.channeledAttack.isDissipating()) {
            this.channeledAttack.startDissipating();
            timeChannelAttackGetsCleared = GameState.getInstance().getGameSeconds();
        }
    }

    private void playFiringAudio(PlayerPrimaryAttackTypes playerAttackType) {
        switch (playerAttackType) {
            case Laserbeam -> playMissileAudio(AudioEnums.NewPlayerLaserbeam);
            case Flamethrower -> playMissileAudio(AudioEnums.Firewall);
        }
    }


    private void fireMissile(int xCoordinate, int yCoordinate, ImageEnums playerMissileType,
                             float missileScale, PathFinder missilePathFinder, MissileEnums attackType) {
        int movementSpeed = 6;
        MissileCreator missileCreator1 = MissileCreator.getInstance();
        SpriteConfiguration spriteConfiguration = missileCreator1.createMissileSpriteConfig(xCoordinate, yCoordinate,
                playerMissileType, missileScale);


        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL; //Hardcoded, should be dynamic somewhere? Idk not decided how i want to use this behaviour yet
        MovementConfiguration movementConfiguration = missileCreator1.createMissileMovementConfig(
                movementSpeed, movementSpeed, missilePathFinder, movementPatternSize, Direction.RIGHT
        );


        boolean isFriendly = true;
        if (missilePathFinder instanceof HomingPathFinder) {
            movementConfiguration.setTargetToChase(((HomingPathFinder) missilePathFinder).getTarget(isFriendly, spriteConfiguration.getxCoordinate(), spriteConfiguration.getyCoordinate()));
        }

        int maxHitPoints = 100;
        int maxShields = 0;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Player Missile";
        float damage = playerStats.getNormalAttackDamage() * 2;
        boolean isExplosive = false;

        switch (attackType) {
            case DefaultRocket -> isExplosive = true;
        }


        MissileConfiguration missileConfiguration = missileCreator1.createMissileConfiguration(attackType, maxHitPoints, maxShields,
                deathSound, damage, attackType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType, attackType.isUsesBoxCollision(),
                isExplosive, true, false);

        PlayerStats instance = PlayerStats.getInstance();
        if (!isExplosive) {
            missileConfiguration.setPiercesMissiles(instance.getPiercingMissilesAmount() > 0);
            missileConfiguration.setAmountOfPierces(instance.getPiercingMissilesAmount());
        }
        Missile missile = missileCreator1.createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.HighVelocityLasers) != null){
            HighVelocityLasers lasers = (HighVelocityLasers) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.HighVelocityLasers);
            lasers.applyEffectToObject(missile);
        }

        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        missile.setOwnerOrCreator(spaceship);
        missile.setCenterCoordinates(missile.getCenterXCoordinate(), spaceship.getCenterYCoordinate());
        missile.resetMovementPath();

        missile.setCanBounce(true);

        GameStatsTracker.getInstance().addShotFired(1);
        this.missileManager.addExistingMissile(missile);

    }

    private void playMissileAudio(AudioEnums audioEnum) {
        this.audioManager.addAudio(audioEnum);
    }

    public void updateFrameCount() {
        if (channeledAttack != null && channeledAttack.isDissipating()) {
            if ((timeChannelAttackGetsCleared + 0.5d) < GameState.getInstance().getGameSeconds()) {
                channeledAttack = null;
            }
        }

        if (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter)) {
            if(orangeBarMaxValue < 0) {
                orangeBarMaxValue = 125 * PlayerStats.getInstance().getFuelCannisterMultiplier();
            }

            if(orangeBarCurrentValue < 0){
                orangeBarCurrentValue = orangeBarMaxValue;
            }

            if(this.channeledAttack == null && orangeBarCurrentValue < orangeBarMaxValue) {
                orangeBarCurrentValue += FUEL_REGENERATION_RATE * PlayerStats.getInstance().getFuelCannisterRegenMultiplier();
                if (orangeBarCurrentValue > orangeBarMaxValue) {
                    orangeBarCurrentValue = orangeBarMaxValue; // Clamp at max value
                }
            }

            if(channeledAttack != null) {
                if (orangeBarCurrentValue <= 0) {
                    // If no fuel is available, prevent firing
                    stopFiring();
                    return;
                }

                // Deplete fuel while firing
                updateFlameThrowerDamageFromInfernalPreIgniter(this.channeledAttack);
                orangeBarCurrentValue -= Math.max(FUEL_DEPLETION_RATE * PlayerStats.getInstance().getFuelCannisterUsageMultiplier() , 0.01f);
                if (orangeBarCurrentValue < 0) {
                    orangeBarCurrentValue = 0; // Clamp at 0
                }
            }
        }
    }

    public float getOrangeBarMaxValue() {
        if (playerStats.getPlayerClass().equals(PlayerClass.Carrier)) {
            return ProtossUtils.getProtossShipBuildTime();
        }
        return orangeBarMaxValue;
    }

    public float getOrangeBarCurrentValue() {
        if (playerStats.getPlayerClass().equals(PlayerClass.Carrier)) {
            return ProtossUtils.getProtossShipBuilderTimer();
        }
        return orangeBarCurrentValue;
    }
}