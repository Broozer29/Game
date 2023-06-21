package game.objects.friendlies.friendlyobjects;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.TemporaryGameSettings;
import data.PlayerStats;
import data.audio.AudioEnums;
import data.image.enums.ImageEnums;
import game.managers.AudioManager;
import game.managers.FriendlyManager;
import game.managers.MissileManager;
import game.movement.Direction;
import game.movement.HomingPathFinder;
import game.movement.PathFinder;

public class SpaceShipRegularGun {
	// Implement "fire()" behaviour here

	private MissileManager missileManager = MissileManager.getInstance();
	private AudioManager audioManager = AudioManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private PlayerStats playerStats = PlayerStats.getInstance();
	private TemporaryGameSettings powerUpEffects = TemporaryGameSettings.getInstance();

	private float currentAttackFrame;

	public SpaceShipRegularGun() {

	}

	public void fire(int xCoordinate, int yCoordinate, int spaceShipWidth, int spaceShipHeight) {
		if (missileManager == null || friendlyManager == null || audioManager == null) {
			missileManager = MissileManager.getInstance();
			friendlyManager = FriendlyManager.getInstance();
			audioManager = AudioManager.getInstance();
		}

		if (currentAttackFrame >= playerStats.getAttackSpeed()) {
			this.currentAttackFrame = 0;
			
	        int x = xCoordinate + spaceShipWidth;
	        int y = yCoordinate + (spaceShipHeight / 2) - 5;
	        ImageEnums type = playerStats.getPlayerMissileType();
	        ImageEnums imageType = playerStats.getPlayerMissileType();
	        float scale = playerStats.getMissileScale();
	        PathFinder pathFinder = playerStats.getMissilePathFinder();
//			PathFinder pathFinder = new HomingPathFinder();
			if (powerUpEffects.getTripleShotActive()) {
				this.fireMissile(x, y, type, imageType, Direction.RIGHT_UP, scale, pathFinder);
				this.fireMissile(x, y, type, imageType, Direction.RIGHT, scale, pathFinder);
				this.fireMissile(x, y, type, imageType, Direction.RIGHT_DOWN, scale, pathFinder);
				playMissileAudio(AudioEnums.Player_Laserbeam);

			} else if (powerUpEffects.getDoubleShotActive()) {
				this.fireMissile(x, y, type, imageType, Direction.RIGHT_UP, scale, pathFinder);
				this.fireMissile(x, y, type, imageType, Direction.RIGHT, scale, pathFinder);
				playMissileAudio(AudioEnums.Player_Laserbeam);

			} else {
				this.fireMissile(x, y, type, imageType, Direction.RIGHT, scale, pathFinder);
				playMissileAudio(AudioEnums.Player_Laserbeam);

			}
		}
	}

	private void fireMissile(int xCoordinate, int yCoordinate, ImageEnums playerMissileType, ImageEnums playerMissileImpactType, Direction direction, 
			float missileScale, PathFinder missilePathFinder) {
		this.missileManager.addFriendlyMissile(xCoordinate, yCoordinate, playerMissileType, playerMissileImpactType, direction, missileScale, missilePathFinder);
		
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
