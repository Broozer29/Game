package game.objects.player.spaceship;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.movement.Direction;
import game.movement.pathfinders.PathFinder;
import game.objects.missiles.*;
import game.objects.player.PlayerManager;
import game.objects.player.BoostsUpgradesAndBuffsSettings;
import game.objects.player.PlayerStats;
import VisualAndAudioData.audio.AudioEnums;
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

            ImageEnums visualImage = playerStats.getPlayerMissileType();
            ImageEnums impactType = playerStats.getPlayerMissileImpactType();
            float scale = playerStats.getMissileScale();
            PathFinder pathFinder = playerStats.getMissilePathFinder();

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

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setImageType(playerMissileType);
        spriteConfiguration.setScale(missileScale);

        MissileConfiguration missileConfiguration = new MissileConfiguration(attackType,
                1000000, 100000, null, playerMissileImpactType, true, missilePathFinder
                , direction, xMovementspeed, yMovementspeed, true, "Player Missile", playerStats.getNormalAttackDamage());
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration);

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