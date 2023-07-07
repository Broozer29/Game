package game.objects.friendlies.spaceship;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.PlayerStats;
import data.audio.AudioEnums;
import data.image.ImageEnums;
import game.managers.AudioManager;
import game.managers.PlayerManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.RegularPathFinder;
import game.objects.friendlies.spaceship.specialAttacks.ElectroShred;
import game.objects.friendlies.spaceship.specialAttacks.Firewall;
import game.objects.friendlies.spaceship.specialAttacks.SpecialAttack;
import game.objects.missiles.MissileManager;
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
		case Firewall:
			fireFirewall(xCoordinate, yCoordinate, spaceShipWidth, spaceShipHeight);
			break;
		case Rocket_Cluster:
			break;
		}
	}

	//Creates a special attack with an animation that follows the player
	private void fireElectroShred(int xCoordinate, int yCoordinate, int spaceShipWidth, int spaceShipHeight) {
		if (currentSpecialAttackFrame >= playerStats.getSpecialAttackSpeed()) {

			PlayerManager playerManager = PlayerManager.getInstance();
			SpaceShip spaceShip = playerManager.getSpaceship();

			SpriteAnimation specialAttackAnimation = new SpriteAnimation(spaceShip.getCenterXCoordinate(),
					spaceShip.getCenterYCoordinate(), ImageEnums.Player_EMP, false, 2);
			specialAttackAnimation.setFrameDelay(10);
			specialAttackAnimation.setCenterCoordinates(spaceShip.getCenterXCoordinate(), spaceShip.getCenterYCoordinate());
			
			SpecialAttack specialAttack = new ElectroShred(spaceShip.getCenterXCoordinate(),
					spaceShip.getCenterYCoordinate(), 1, specialAttackAnimation, playerStats.getSpecialAttackDamage(),
					true);
			specialAttack.setCenteredAroundPlayer(true);
			try {
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
	private void fireFirewall(int xCoordinate, int yCoordinate, int spaceShipWidth, int spaceShipHeight) {
		if (currentSpecialAttackFrame >= playerStats.getSpecialAttackSpeed()) {

			PlayerManager playerManager = PlayerManager.getInstance();
			SpaceShip spaceShip = playerManager.getSpaceship();

			int centerX = spaceShip.getCenterXCoordinate() + (spaceShip.getWidth() / 2);
			int centerY = spaceShip.getCenterYCoordinate();

			SpriteAnimation invisibleAnimation = new SpriteAnimation(centerX, centerY,
					ImageEnums.Invisible_Animation, true, 1);
			PathFinder pathfinder = new RegularPathFinder();
			int firewallSize = PlayerStats.getInstance().getFirewallSize();

			SpecialAttack firewall = new Firewall(centerX, centerY, 1, invisibleAnimation, PlayerStats.getInstance().getFirewallDamage(),
					true, pathfinder, firewallSize, Direction.RIGHT);

			try {
				AudioManager.getInstance().addAudio(AudioEnums.Firewall);
				MissileManager.getInstance().addSpecialAttack(firewall);
			} catch (UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.currentSpecialAttackFrame = 0;
		}
	}

	public void updateFrameCount() {
		this.currentSpecialAttackFrame++;
	}
	
	public float getCurrentSpecialAttackFrame() {
		return this.currentSpecialAttackFrame;
	}
}