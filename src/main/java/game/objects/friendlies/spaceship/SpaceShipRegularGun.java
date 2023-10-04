package game.objects.friendlies.spaceship;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.managers.PlayerManager;
import game.movement.Direction;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileManager;
import gamedata.BoostsUpgradesAndBuffsSettings;
import gamedata.PlayerStats;
import gamedata.audio.AudioEnums;
import gamedata.audio.AudioManager;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;

public class SpaceShipRegularGun {
	// Implement "fire()" behaviour here

	private MissileManager missileManager = MissileManager.getInstance();
	private AudioManager audioManager = AudioManager.getInstance();
	private PlayerManager friendlyManager = PlayerManager.getInstance();
	private PlayerStats playerStats = PlayerStats.getInstance();
	private BoostsUpgradesAndBuffsSettings powerUpEffects = BoostsUpgradesAndBuffsSettings.getInstance();
	private MissileCreator missileCreator = MissileCreator.getInstance();

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
			break;
		case Laserbeam:
			fireLaserBeam(xCoordinate, yCoordinate, spaceShipWidth, spaceShipHeight);
			break;
		case Rocket:
			fireRocket(xCoordinate, yCoordinate, spaceShipWidth, spaceShipHeight);
			break;
		case Shotgun:
			break;
		default:
			break;
		}

	}

	private void fireRocket(int xCoordinate, int yCoordinate, int spaceShipWidth, int spaceShipHeight) {
		if (currentAttackFrame >= playerStats.getAttackSpeed()) {
			this.currentAttackFrame = 0;
			int xMovementSpeed = 5;
			int yMovementSpeed = 5;
			
			int x = xCoordinate + spaceShipWidth;
			int y = yCoordinate + (spaceShipHeight / 2) - 5;
			
			ImageEnums type = playerStats.getPlayerMissileType();
			ImageEnums impactType = playerStats.getPlayerMissileImpactType();
			float scale = playerStats.getMissileScale();
			PathFinder pathFinder = playerStats.getMissilePathFinder();
			PlayerAttackTypes attackType = PlayerAttackTypes.Rocket;
			
			this.fireMissile(x, y, type, impactType, Direction.RIGHT, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType, null);
			playMissileAudio(AudioEnums.Rocket_Launcher);
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
			
			this.fireMissile(x, y, type, impactType, Direction.RIGHT, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType, null);
			playMissileAudio(AudioEnums.Flamethrower);
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
//			PathFinder pathFinder = new HomingPathFinder();
			Sprite target = null;
			
			
			if(pathFinder instanceof HomingPathFinder) {
				target = ((HomingPathFinder) pathFinder).getTarget(true);
			}
			
			PlayerAttackTypes attackType = PlayerAttackTypes.Laserbeam;
			if (powerUpEffects.getTripleShotActive()) {
				this.fireMissile(x, y, type, impactType, Direction.RIGHT_UP, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType, target);
				this.fireMissile(x, y, type, impactType, Direction.RIGHT, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType, target);
				this.fireMissile(x, y, type, impactType, Direction.RIGHT_DOWN, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType, target);
				playMissileAudio(AudioEnums.Player_Laserbeam);

			} else if (powerUpEffects.getDoubleShotActive()) {
				this.fireMissile(x, y, type, impactType, Direction.RIGHT_UP, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType, target);
				this.fireMissile(x, y, type, impactType, Direction.RIGHT, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType, target);
				playMissileAudio(AudioEnums.Player_Laserbeam);
			} else {
				this.fireMissile(x, y, type, impactType, Direction.RIGHT, scale, pathFinder, xMovementSpeed, yMovementSpeed, attackType, target);
				playMissileAudio(AudioEnums.Player_Laserbeam);
			}
		}
	}

	private void fireMissile(int xCoordinate, int yCoordinate, ImageEnums playerMissileType,
			ImageEnums playerMissileImpactType, Direction direction, float missileScale, PathFinder missilePathFinder,
			int xMovementspeed, int yMovementspeed, PlayerAttackTypes attackType, Sprite target) {
		Missile missile = MissileCreator.getInstance().createFriendlyMissile(xCoordinate, yCoordinate, playerMissileType, playerMissileImpactType,
				direction, missileScale, missilePathFinder, xMovementspeed, yMovementspeed, attackType);
		
		if(target != null) {
			missile.setTarget(target);
		}
		

		this.missileManager.addExistingMissile(missile);

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