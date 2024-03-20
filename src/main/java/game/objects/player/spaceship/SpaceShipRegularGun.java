package game.objects.player.spaceship;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

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

    public SpaceShipRegularGun () {

    }

    public void fire (int xCoordinate, int yCoordinate, MissileTypeEnums playerAttackType) {
        if (missileManager == null || friendlyManager == null || audioManager == null) {
            missileManager = MissileManager.getInstance();
            friendlyManager = PlayerManager.getInstance();
            audioManager = AudioManager.getInstance();
        }

        if (currentAttackFrame >= playerStats.getAttackSpeed()) {
            this.currentAttackFrame = 0;
            int xMovementSpeed = 5;
            int yMovementSpeed = 5;

            ImageEnums visualImage = playerStats.getPlayerMissileImage();
            ImageEnums impactType = playerStats.getPlayerMissileImpactImage();
            float scale = playerStats.getMissileScale();
            PathFinder pathFinder = new RegularPathFinder();

            fireMissile(xCoordinate, yCoordinate, visualImage, impactType, Direction.RIGHT, scale, pathFinder, xMovementSpeed, yMovementSpeed, playerAttackType);

            switch (playerAttackType){
                case DefaultPlayerLaserbeam -> playMissileAudio(AudioEnums.Player_Laserbeam);
                case Rocket1 -> playMissileAudio(AudioEnums.Rocket_Launcher);
            }
        }
    }


    private void fireMissile (int xCoordinate, int yCoordinate, ImageEnums playerMissileType,
                              ImageEnums playerMissileImpactType, Direction direction, float missileScale, PathFinder missilePathFinder,
                              int xMovementspeed, int yMovementspeed, MissileTypeEnums attackType) {

        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,
                playerMissileType, missileScale);


        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL; //Hardcoded, should be dynamic somewhere? Idk not decided how i want to use this behaviour yet
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                xMovementspeed, yMovementspeed, missilePathFinder, movementPatternSize, direction
        );


        boolean isFriendly = true;
        if (missilePathFinder instanceof HomingPathFinder) {
            movementConfiguration.setTargetToChase(((HomingPathFinder) missilePathFinder).getTarget(isFriendly, spriteConfiguration.getxCoordinate(), spriteConfiguration.getyCoordinate()));
        }

        int maxHitPoints = 100;
        int maxShields = 100;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Player Missile";
        float damage = playerStats.getNormalAttackDamage();


        switch (attackType){
            case DefaultPlayerLaserbeam -> damage = playerStats.getNormalAttackDamage();
            case Rocket1 -> damage = playerStats.getNormalAttackDamage() * 2f;
        }

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(attackType, maxHitPoints, maxShields,
                deathSound, damage, playerMissileImpactType, isFriendly, allowedToDealDamage, objectType, attackType.isBoxCollision());

        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(PlayerManager.getInstance().getSpaceship());
        this.missileManager.addExistingMissile(missile);

    }

    private void playMissileAudio (AudioEnums audioEnum) {
        try {
            this.audioManager.addAudio(audioEnum);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void updateFrameCount () {
        this.currentAttackFrame++;
    }

}