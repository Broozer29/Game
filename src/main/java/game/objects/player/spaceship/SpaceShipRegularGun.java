package game.objects.player.spaceship;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.gamestate.GameStateInfo;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.missiles.*;
import game.objects.player.PlayerManager;
import game.objects.player.BoostsUpgradesAndBuffsSettings;
import game.objects.player.PlayerStats;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class SpaceShipRegularGun {

    private MissileManager missileManager = MissileManager.getInstance();
    private AudioManager audioManager = AudioManager.getInstance();
    private PlayerManager friendlyManager = PlayerManager.getInstance();
    private PlayerStats playerStats = PlayerStats.getInstance();
    private BoostsUpgradesAndBuffsSettings powerUpEffects = BoostsUpgradesAndBuffsSettings.getInstance();
    private MissileCreator missileCreator = MissileCreator.getInstance();

    private float currentAttackFrame;
    private double lastAttackTime = 0.0;

    public SpaceShipRegularGun () {

    }

    public void fire (int xCoordinate, int yCoordinate, MissileEnums playerAttackType) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();

        if (currentTime >= lastAttackTime + playerStats.getAttackSpeed()) {
            lastAttackTime = currentTime;  // Update the last attack time
            int xMovementSpeed = 5;
            int yMovementSpeed = 5;

            ImageEnums visualImage = playerStats.getPlayerMissileImage();
            ImageEnums impactType = playerAttackType.getDeathOrExplosionImageEnum();
            float scale = playerStats.getMissileScale();
            PathFinder pathFinder = new RegularPathFinder();

            fireMissile(xCoordinate, yCoordinate, visualImage, scale, pathFinder, playerAttackType);

            switch (playerAttackType) {
                case PlayerLaserbeam -> playMissileAudio(AudioEnums.Player_Laserbeam);
                case Rocket1 -> playMissileAudio(AudioEnums.Rocket_Launcher);
            }
        }
    }


    private void fireMissile (int xCoordinate, int yCoordinate, ImageEnums playerMissileType,
                              float missileScale, PathFinder missilePathFinder, MissileEnums attackType) {

//        attackType = MissileEnums.OrbitCenter;
//        playerMissileType = attackType.getImageType();
//        missileScale = 0.4f;

        MissileCreator missileCreator1 = MissileCreator.getInstance();
        SpriteConfiguration spriteConfiguration = missileCreator1.createMissileSpriteConfig(xCoordinate, yCoordinate,
                playerMissileType, missileScale);

        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL; //Hardcoded, should be dynamic somewhere? Idk not decided how i want to use this behaviour yet
        MovementConfiguration movementConfiguration = missileCreator1.createMissileMovementConfig(
                attackType.getxMovementSpeed(), attackType.getyMovementSpeed(), missilePathFinder, movementPatternSize, Direction.RIGHT
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
            case Rocket1 -> isExplosive = true;
        }



        MissileConfiguration missileConfiguration = missileCreator1.createMissileConfiguration(attackType, maxHitPoints, maxShields,
                deathSound, damage, attackType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType, attackType.isBoxCollision(),
                isExplosive, true);

        PlayerStats instance = PlayerStats.getInstance();
        if(!isExplosive) {
            missileConfiguration.setPiercesMissiles(instance.getPiercingMissilesAmount() > 0);
            missileConfiguration.setAmountOfPierces(instance.getPiercingMissilesAmount());
        }
        Missile missile = missileCreator1.createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        missile.setOwnerOrCreator(spaceship);
        missile.setCenterCoordinates(missile.getCenterXCoordinate(), spaceship.getCenterYCoordinate());
        missile.resetMovementPath();
        this.missileManager.addExistingMissile(missile);

    }

    private void playMissileAudio (AudioEnums audioEnum) {
        try {
            this.audioManager.addAudio(audioEnum);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

//    public void updateFrameCount () {
//        this.currentAttackFrame++;
//    }

}