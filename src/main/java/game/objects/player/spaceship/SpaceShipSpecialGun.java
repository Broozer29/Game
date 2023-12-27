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
import gamedata.audio.AudioEnums;
import gamedata.audio.AudioManager;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;
import visual.objects.SpriteAnimation;

public class SpaceShipSpecialGun {
	private PlayerStats playerStats = PlayerStats.getInstance();

	private float currentSpecialAttackFrame;

	public SpaceShipSpecialGun() {

	}

	public void fire(int xCoordinate, int yCoordinate, int spaceShipWidth, int spaceShipHeight,
			PlayerSpecialAttackTypes attackType) {
		switch (attackType) {
		case EMP:
			fireElectroShred(xCoordinate, yCoordinate, spaceShipWidth, spaceShipHeight);
			break;
//		case Firewall:
//			fireFirewall(xCoordinate, yCoordinate, spaceShipWidth, spaceShipHeight);
//			break;
		case Rocket_Cluster:
			break;
		}
	}

	//Creates a special attack with an animation that follows the player
	private void fireElectroShred(int xCoordinate, int yCoordinate, int spaceShipWidth, int spaceShipHeight) {
		if (currentSpecialAttackFrame >= playerStats.getSpecialAttackSpeed()) {

			PlayerManager playerManager = PlayerManager.getInstance();
			SpaceShip spaceShip = playerManager.getSpaceship();

			SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
			spriteConfiguration.setxCoordinate(spaceShip.getCenterXCoordinate());
			spriteConfiguration.setyCoordinate(spaceShip.getCenterYCoordinate());
			spriteConfiguration.setImageType(ImageEnums.Player_EMP);

			SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 4, false);
			MissileConfiguration missileConfiguration = new MissileConfiguration();
			missileConfiguration.setDamage(1.5f);

			SpriteAnimation specialAttackAnimation = new SpriteAnimation(spriteAnimationConfiguration);
			specialAttackAnimation.setCenterCoordinates(spaceShip.getCenterXCoordinate(), spaceShip.getCenterYCoordinate());

			SpecialAttack specialAttack = new ElectroShred(spriteAnimationConfiguration, missileConfiguration);
			specialAttack.setCenteredAroundPlayer(true);
			try {
//				specialAttackAnimation.updateCurrentBoardBlock();
				AudioManager.getInstance().addAudio(AudioEnums.Default_EMP);
				spaceShip.addFollowingSpecialAttack(specialAttack);
				MissileManager.getInstance().addSpecialAttack(specialAttack);
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
			this.currentSpecialAttackFrame = 0;
		}
	}


	//Creates missiles and adds it to the missile manager
//	private void fireFirewall(int xCoordinate, int yCoordinate, int spaceShipWidth, int spaceShipHeight) {
//		if (currentSpecialAttackFrame >= playerStats.getSpecialAttackSpeed()) {
//
//			PlayerManager playerManager = PlayerManager.getInstance();
//			SpaceShip spaceShip = playerManager.getSpaceship();
//
//			int centerX = spaceShip.getCenterXCoordinate() + (spaceShip.getWidth() / 2);
//			int centerY = spaceShip.getCenterYCoordinate();
//
//			SpriteAnimation invisibleAnimation = new SpriteAnimation(centerX, centerY,
//					ImageEnums.Invisible_Animation, true, 1);
//			PathFinder pathfinder = new RegularPathFinder();
//			int firewallSize = PlayerStats.getInstance().getFirewallSize();
//
//			SpecialAttack firewall = new Firewall(centerX, centerY, 1, invisibleAnimation, PlayerStats.getInstance().getFirewallDamage(),
//					true, pathfinder, firewallSize, Direction.RIGHT);
//
//			try {
//				AudioManager.getInstance().addAudio(AudioEnums.Firewall);
//				MissileManager.getInstance().addSpecialAttack(firewall);
//			} catch (UnsupportedAudioFileException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			this.currentSpecialAttackFrame = 0;
//		}
//	}

	public void updateFrameCount() {
		this.currentSpecialAttackFrame++;
	}
	
	public float getCurrentSpecialAttackFrame() {
		return this.currentSpecialAttackFrame;
	}
}