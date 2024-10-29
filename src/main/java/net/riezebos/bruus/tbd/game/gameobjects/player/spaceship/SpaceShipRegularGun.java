package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HomingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.visuals.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visuals.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visuals.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class SpaceShipRegularGun {

    private MissileManager missileManager = MissileManager.getInstance();
    private AudioManager audioManager = AudioManager.getInstance();
    private PlayerManager friendlyManager = PlayerManager.getInstance();
    private PlayerStats playerStats = PlayerStats.getInstance();
    private MissileCreator missileCreator = MissileCreator.getInstance();

    private float currentAttackFrame;
    private double lastAttackTime = 0.0;

    public SpaceShipRegularGun () {

    }

    public void fire (int xCoordinate, int yCoordinate, MissileEnums playerAttackType) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();

        if (currentTime >= lastAttackTime + playerStats.getAttackSpeed()) {
            lastAttackTime = currentTime;  // Update the last attack time

            ImageEnums visualImage = playerStats.getPlayerMissileImage();
            float scale = playerStats.getMissileScale();
            PathFinder pathFinder = new RegularPathFinder();

            fireMissile(xCoordinate, yCoordinate, visualImage, scale, pathFinder, playerAttackType);

            switch (playerAttackType) {
                case PlayerLaserbeam -> playMissileAudio(AudioEnums.NewPlayerLaserbeam);
                case DefaultRocket -> playMissileAudio(AudioEnums.Rocket_Launcher);
            }
        }
    }


    private void fireMissile (int xCoordinate, int yCoordinate, ImageEnums playerMissileType,
                              float missileScale, PathFinder missilePathFinder, MissileEnums attackType) {

//        attackType = MissileEnums.OrbitCenter;
//        playerMissileType = ImageEnums.ThornsDamage;
//        missileScale = 0.4f;

        int movementSpeed = 5;

        MissileCreator missileCreator1 = MissileCreator.getInstance();
        SpriteConfiguration spriteConfiguration = missileCreator1.createMissileSpriteConfig(xCoordinate, yCoordinate,
                playerMissileType, missileScale);



        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL; //Hardcoded, should be dynamic somewhere? Idk not decided how i want to use this behaviour yet
        MovementConfiguration movementConfiguration = missileCreator1.createMissileMovementConfig(
                movementSpeed,movementSpeed, missilePathFinder, movementPatternSize, Direction.RIGHT
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
        if(!isExplosive) {
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