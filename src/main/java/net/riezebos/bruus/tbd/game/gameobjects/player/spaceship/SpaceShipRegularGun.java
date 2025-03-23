package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship;

import javafx.animation.Animation;
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
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HomingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import javax.xml.crypto.Data;

public class SpaceShipRegularGun {

    private MissileManager missileManager = MissileManager.getInstance();
    private AudioManager audioManager = AudioManager.getInstance();
    private PlayerStats playerStats = PlayerStats.getInstance();
    private SpecialAttack channeledAttack = null;

    private double lastAttackTime = 0.0;
    private double timeChannelAttackGetsCleared = 0.0;

    private float orangeBarMaxValue = -1;
    private float orangeBarCurrentValue = -1;

    public SpaceShipRegularGun() {

    }

    public void fire(int xCoordinate, int yCoordinate, PlayerPrimaryAttackTypes playerAttackType) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + playerStats.getAttackSpeed()) {
            lastAttackTime = currentTime;  // Update the last attack time

            if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleCommand) != null) {
                for (Drone drone : FriendlyManager.getInstance().getAllPlayerDrones()) {
                    drone.fireAction();
                }
                playMissileAudio(AudioEnums.NewPlayerLaserbeam);
                return; //Don't fire the actual player missile
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
            PlayerStats.getInstance().setMovementSpeed(4);
            AudioManager.getInstance().addAudio(AudioEnums.CarrierSpeedingUp);
            addSwitchingGearAnimation(ImageEnums.ProtossCarrierSwitchFast);
        } else if (carrierFastSwitch) {
            ProtossUtils.getInstance().setAllowedToBuildProtoss(true);
            carrierFastSwitch = false;
            PlayerStats.getInstance().setMovementSpeed(2.5f);
            AudioManager.getInstance().addAudio(AudioEnums.CarrierSlowingDown);
            addSwitchingGearAnimation(ImageEnums.ProtossCarrierSwitchSlow);
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


    private void startFiringFlameThrower(int xCoordinate, int yCoordinate) {
        if (this.channeledAttack == null) {
            PlayerManager playerManager = PlayerManager.getInstance();
            SpaceShip spaceShip = playerManager.getSpaceship();

            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(xCoordinate);
            spriteConfiguration.setyCoordinate(yCoordinate);
            spriteConfiguration.setImageType(ImageEnums.FireFighterFlameThrowerAppearing);

            float damage = playerStats.getNormalAttackDamage();
            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, true);
            SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, false, true);
            SpecialAttack specialAttack = new FlameThrower(spriteAnimationConfiguration, missileConfiguration);
            specialAttack.setCenteredAroundObject(true);
            specialAttack.setScale(0.8f);
            specialAttack.addXOffset((specialAttack.getAnimation().getWidth() / 2) - Math.round((specialAttack.getAnimation().getWidth() * 0.005f)));
            specialAttack.setOwnerOrCreator(PlayerManager.getInstance().getSpaceship());
            spaceShip.addFollowingSpecialAttack(specialAttack);
            this.channeledAttack = specialAttack;
            MissileManager.getInstance().addSpecialAttack(specialAttack);
            AudioManager.getInstance().addAudio(AudioEnums.Firewall);
        }

        orangeBarCurrentValue = -1;
        orangeBarMaxValue = -1;
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
        int movementSpeed = 5;
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
        float damage = playerStats.getNormalAttackDamage();
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
        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        missile.setOwnerOrCreator(spaceship);
        missile.setCenterCoordinates(missile.getCenterXCoordinate(), spaceship.getCenterYCoordinate());
        missile.resetMovementPath();

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