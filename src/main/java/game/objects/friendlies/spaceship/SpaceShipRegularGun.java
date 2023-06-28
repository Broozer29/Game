package game.objects.friendlies.spaceship;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.TemporaryGameSettings;
import data.PlayerStats;
import data.audio.AudioEnums;
import data.image.ImageEnums;
import game.managers.AudioManager;
import game.managers.PlayerManager;
import game.managers.MissileManager;
import game.movement.Direction;
import game.movement.HomingPathFinder;
import game.movement.PathFinder;
import game.movement.Point;

public class SpaceShipRegularGun {
	// Implement "fire()" behaviour here

	private MissileManager missileManager = MissileManager.getInstance();
	private AudioManager audioManager = AudioManager.getInstance();
	private PlayerManager friendlyManager = PlayerManager.getInstance();
	private PlayerStats playerStats = PlayerStats.getInstance();
	private TemporaryGameSettings powerUpEffects = TemporaryGameSettings.getInstance();

	private float currentAttackFrame;

	public SpaceShipRegularGun() {

	}

	public void fire(int xCoordinate, int yCoordinate, int spaceShipWidth, int spaceShipHeight,
			PlayerAttackTypes playerAttackType) {
		if (missileManager == null || friendlyManager == null || audioManager == null) {
			missileManager = MissileManager.getInstance();
			friendlyManager = PlayerManager.getInstance();
			audioManager = AudioManager.getInstance();
		}

		switch (playerAttackType) {
		case Flamethrower:
			fireFlameThrower(xCoordinate, yCoordinate, spaceShipWidth, spaceShipHeight);
		case Laserbeam:
			fireLaserBeam(xCoordinate, yCoordinate, spaceShipWidth, spaceShipHeight);
		case Rocket:
			break;
		case Shotgun:
			break;
		default:
			break;
		}

	}

	private void fireFlameThrower(int xCoordinate, int yCoordinate, int spaceShipWidth, int spaceShipHeight) {
		if (currentAttackFrame >= playerStats.getAttackSpeed()) {
			this.currentAttackFrame = 0;
			int xMovementSpeed = 3;
			int yMovementSpeed = 3;
			
			int x = xCoordinate + spaceShipWidth;
			int y = yCoordinate + (spaceShipHeight / 2) - 5;
			ImageEnums type = playerStats.getPlayerMissileType();
			ImageEnums impactType = playerStats.getPlayerMissileImpactType();
			float scale = playerStats.getMissileScale();
			PathFinder pathFinder = playerStats.getMissilePathFinder();
			PlayerAttackTypes attackType = PlayerAttackTypes.Flamethrower;
			
			this.fireMissile(x, y, type, impactType, Direction.RIGHT, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType);
//			playMissileAudio(AudioEnums.Player_Laserbeam);
		}
	}

	private void fireLaserBeam(int xCoordinate, int yCoordinate, int spaceShipWidth, int spaceShipHeight) {
		if (currentAttackFrame >= playerStats.getAttackSpeed()) {
			this.currentAttackFrame = 0;

			int xMovementSpeed = 5;
			int yMovementSpeed = 2;
			
			int x = xCoordinate + spaceShipWidth;
			int y = yCoordinate + (spaceShipHeight / 2) - 5;
			ImageEnums type = playerStats.getPlayerMissileType();
			ImageEnums impactType = playerStats.getPlayerMissileImpactType();
			float scale = playerStats.getMissileScale();
			PathFinder pathFinder = playerStats.getMissilePathFinder();
			PlayerAttackTypes attackType = PlayerAttackTypes.Laserbeam;
			if (powerUpEffects.getTripleShotActive()) {
				this.fireMissile(x, y, type, impactType, Direction.RIGHT_UP, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType);
				this.fireMissile(x, y, type, impactType, Direction.RIGHT, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType);
				this.fireMissile(x, y, type, impactType, Direction.RIGHT_DOWN, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType);
				playMissileAudio(AudioEnums.Player_Laserbeam);

			} else if (powerUpEffects.getDoubleShotActive()) {
				this.fireMissile(x, y, type, impactType, Direction.RIGHT_UP, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType);
				this.fireMissile(x, y, type, impactType, Direction.RIGHT, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType);
				playMissileAudio(AudioEnums.Player_Laserbeam);

			} else {
				this.fireMissile(x, y, type, impactType, Direction.RIGHT, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType);
				playMissileAudio(AudioEnums.Player_Laserbeam);

			}
		}
	}

	private void fireMissile(int xCoordinate, int yCoordinate, ImageEnums playerMissileType,
			ImageEnums playerMissileImpactType, Direction direction, float missileScale, PathFinder missilePathFinder,
			int xMovementspeed, int yMovementspeed, PlayerAttackTypes attackType) {
		this.missileManager.addFriendlyMissile(xCoordinate, yCoordinate, playerMissileType, playerMissileImpactType,
				direction, missileScale, missilePathFinder, xMovementspeed, yMovementspeed, attackType);

	}

	private void playMissileAudio(AudioEnums audioEnum) {
		try {
			this.audioManager.addAudio(audioEnum);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}

	public void updateFrameCount() {
		this.currentAttackFrame++;
	}
	
}