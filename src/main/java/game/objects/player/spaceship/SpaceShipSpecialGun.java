package game.objects.player.spaceship;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.objects.player.PlayerManager;
import game.objects.player.PlayerSpecialAttackTypes;
import game.objects.player.specialAttacks.ElectroShred;
import game.objects.player.specialAttacks.SpecialAttack;
import game.objects.missiles.MissileConfiguration;
import game.objects.missiles.MissileManager;
import game.objects.player.PlayerStats;
import VisualAndAudioData.audio.AudioEnums;
import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class SpaceShipSpecialGun {
    private PlayerStats playerStats = PlayerStats.getInstance();

    private float currentSpecialAttackFrame;
    private int specialAttackCharges;


    public SpaceShipSpecialGun () {
        specialAttackCharges = 0;
    }

    public void fire (int xCoordinate, int yCoordinate, int spaceShipWidth, int spaceShipHeight,
                      PlayerSpecialAttackTypes attackType) {
        if (specialAttackCharges > 0 || currentSpecialAttackFrame >= playerStats.getSpecialAttackSpeed()) {
            switch (attackType) {
                case EMP:
                    fireElectroShred(xCoordinate, yCoordinate);
                    break;
                case Rocket_Cluster:
                    break;
            }
            if (specialAttackCharges > 0) {
                specialAttackCharges--; // Use a charge if available
            } else {
                this.currentSpecialAttackFrame = 0; // Reset frame count if no charges
            }
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

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 4, false);
        MissileConfiguration missileConfiguration = new MissileConfiguration();
        missileConfiguration.setDamage(1.5f);
        missileConfiguration.setBoxCollision(true);


        SpriteAnimation specialAttackAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        specialAttackAnimation.setAnimationScale(2);

        SpecialAttack specialAttack = new ElectroShred(spriteAnimationConfiguration, missileConfiguration);
        specialAttack.setCenteredAroundPlayer(true);
        specialAttack.setScale(2);
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
        if (this.specialAttackCharges < playerStats.getMaxSpecialAttackCharges()) {
            this.currentSpecialAttackFrame++;

            if (this.currentSpecialAttackFrame >= playerStats.getSpecialAttackSpeed()) {
                this.currentSpecialAttackFrame = 0; // Reset the frame count
                this.specialAttackCharges++; // Increment the charge count
            }
        }
    }

    public int getSpecialAttackCharges () {
        return this.specialAttackCharges;
    }

    public float getCurrentSpecialAttackFrame () {
        return this.currentSpecialAttackFrame;
    }
}