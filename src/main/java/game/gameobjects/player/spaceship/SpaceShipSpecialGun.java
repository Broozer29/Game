package game.gameobjects.player.spaceship;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.gamestate.GameStateInfo;
import game.gameobjects.player.PlayerManager;
import game.gameobjects.player.PlayerSpecialAttackTypes;
import game.gameobjects.missiles.specialAttacks.ElectroShred;
import game.gameobjects.missiles.specialAttacks.SpecialAttack;
import game.gameobjects.missiles.MissileManager;
import game.gameobjects.player.PlayerStats;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class SpaceShipSpecialGun {
    private PlayerStats playerStats = PlayerStats.getInstance();

    private int specialAttackCharges;
    private double lastSecondsSpecialAttackUsed = 0.0;
    private double lastSecondsSpecialAttackChargeGained = 0.0;
    private double secondsUntilNextSpecialAttackCharge = 0.0;
    private boolean allowedToFire = false;


    public SpaceShipSpecialGun () {
        specialAttackCharges = 0;
    }

    public void fire(int xCoordinate, int yCoordinate,PlayerSpecialAttackTypes attackType) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (specialAttackCharges > 0 && currentTime >= (lastSecondsSpecialAttackUsed + 0.15) && allowedToFire) {
            switch (attackType) {
                case EMP:
                    fireElectroShred(xCoordinate, yCoordinate);
                    break;
                case Rocket_Cluster:
                    break;
            }
            if (specialAttackCharges > 0) {
                specialAttackCharges--;
                lastSecondsSpecialAttackUsed = currentTime;
                // Reset the charge gain timer only if there's room for more charges
                if (specialAttackCharges < playerStats.getMaxSpecialAttackCharges()) {
                    lastSecondsSpecialAttackChargeGained = currentTime; // Start new charge cooldown
                }
            }
            allowedToFire = false;
        }
    }

    //Creates a special attack with an animation that follows the player
    private void fireElectroShred (int xCoordinate, int yCoordinate) {
        PlayerManager playerManager = PlayerManager.getInstance();
        SpaceShip spaceShip = playerManager.getSpaceship();

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setImageType(ImageEnums.Player_EMP);

        float damage = playerStats.getSpecialDamage();

        if(playerStats.isHasImprovedElectroShred()){
            spriteConfiguration.setImageType(ImageEnums.ElectroShredImproved);
        }

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 6, false);
        SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true);

        SpriteAnimation specialAttackAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        specialAttackAnimation.setAnimationScale(1.5f);

        SpecialAttack specialAttack = new ElectroShred(spriteAnimationConfiguration, missileConfiguration);
        specialAttack.setCenteredAroundObject(true);
        specialAttack.setScale(1.5f);
        specialAttack.setOwnerOrCreator(PlayerManager.getInstance().getSpaceship());
//			specialAttackAnimation.setCenterCoordinates(spaceShip.getCenterXCoordinate(), spaceShip.getCenterYCoordinate());
//			specialAttack.setCenterCoordinates(spaceShip.getCenterXCoordinate(), spaceShip.getCenterYCoordinate());

        try {
            AudioManager.getInstance().addAudio(AudioEnums.Default_EMP);
            spaceShip.addFollowingSpecialAttack(specialAttack);
            MissileManager.getInstance().addSpecialAttack(specialAttack);

        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void updateFrameCount() {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();

        // Allow firing if enough time has passed since the last shot
        if (currentTime >= lastSecondsSpecialAttackUsed + playerStats.getSpecialAttackSpeed()) {
            allowedToFire = true;
        }

        // Gain a new charge if enough time has passed since the last charge was gained and there is a slot available
        if (specialAttackCharges < playerStats.getMaxSpecialAttackCharges()) {
            if (currentTime >= lastSecondsSpecialAttackChargeGained + playerStats.getSpecialAttackSpeed()) {
                lastSecondsSpecialAttackChargeGained = currentTime;
                specialAttackCharges++;
            }
        }

        // Calculate time until next charge
        if (specialAttackCharges < playerStats.getMaxSpecialAttackCharges()) {
            secondsUntilNextSpecialAttackCharge = playerStats.getSpecialAttackSpeed() - (currentTime - lastSecondsSpecialAttackChargeGained);
        } else {
            secondsUntilNextSpecialAttackCharge = 0; // No charging if full
        }
    }

    public int getSpecialAttackCharges () {
        return this.specialAttackCharges;
    }

    public double getCurrentSpecialAttackFrame() {
        return secondsUntilNextSpecialAttackCharge;
    }
}