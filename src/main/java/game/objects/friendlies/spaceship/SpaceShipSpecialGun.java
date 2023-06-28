package game.objects.friendlies.spaceship;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.PlayerStats;
import data.audio.AudioEnums;
import data.image.ImageEnums;
import game.managers.AudioManager;
import game.managers.MissileManager;
import game.managers.PlayerManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.RegularPathFinder;
import game.objects.friendlies.spaceship.specialAttacks.ElectroShred;
import game.objects.friendlies.spaceship.specialAttacks.Firewall;
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

	private void fireElectroShred(int xCoordinate, int yCoordinate, int spaceShipWidth, int spaceShipHeight) {
		if (currentSpecialAttackFrame >= playerStats.getSpecialAttackSpeed()) {

			PlayerManager playerManager = PlayerManager.getInstance();
			SpaceShip spaceShip = playerManager.getSpaceship();

			SpriteAnimation specialAttackAnimation = new SpriteAnimation(spaceShip.getCenterXCoordinate(),
					spaceShip.getCenterYCoordinate(), ImageEnums.Player_EMP, false, 1);
			specialAttackAnimation.setFrameDelay(4);

			SpecialAttack specialAttack = new ElectroShred(spaceShip.getCenterXCoordinate(),
					spaceShip.getCenterYCoordinate(), 1, specialAttackAnimation, playerStats.getSpecialAttackDamage(),
					true);
			specialAttack.addXOffset(-(specialAttackAnimation.getWidth() / 4));
			specialAttack.addYOffset(-(specialAttackAnimation.getHeight() / 2));

			specialAttackAnimation.addXOffset(-(specialAttackAnimation.getWidth() / 4));
			specialAttackAnimation.addYOffset(-(specialAttackAnimation.getHeight() / 2));

			try {
				AudioManager.getInstance().addAudio(AudioEnums.Default_EMP);
				spaceShip.playerFollowingSpecialAttacks.add(specialAttack);
				MissileManager.getInstance().addSpecialAttack(specialAttack);

			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
			this.currentSpecialAttackFrame = 0;
		}
	}

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

			MissileManager.getInstance().addSpecialAttack(firewall);
			this.currentSpecialAttackFrame = 0;
		}
	}

	public void updateFrameCount() {
		this.currentSpecialAttackFrame++;
	}
}