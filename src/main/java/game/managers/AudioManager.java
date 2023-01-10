package game.managers;

import java.io.IOException;

import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

import data.AudioDatabase;
import data.CustomAudioClip;

public class AudioManager {

	private static AudioManager instance = new AudioManager();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private CustomAudioClip backGroundMusic = null;
	private CustomAudioClip backGroundMusicInputStream = null;
	private AudioDatabase audioDatabase = AudioDatabase.getInstance();

	private AudioManager() {

	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		if (backGroundMusic != null) {
			backGroundMusic.stopClip();
			backGroundMusic.closeclip();
			backGroundMusic = null;
			backGroundMusicInputStream = null;
		}

	}

	public static AudioManager getInstance() {
		return instance;
	}

	public void addAudio(String audioType) throws UnsupportedAudioFileException, IOException {
		playAudio(audioType);
	}

	// Voeg een playermissile audio toe op basis van de missile type
	public void firePlayerMissile() throws UnsupportedAudioFileException, IOException {
		if (friendlyManager == null) {
			friendlyManager = FriendlyManager.getInstance();
		}

		switch (friendlyManager.getPlayerMissileType()) {
		case ("Player Laserbeam"):
			playAudio("Player Laserbeam");
		}
	}

	// Play singular audios
	private void playAudio(String audioType) throws UnsupportedAudioFileException, IOException {
		if (audioType != null) {
//			audioDatabase.getAudioClip(audioType).start();
			
			CustomAudioClip clip = audioDatabase.getAudioClip(audioType);
			if (clip != null) {
				// Adjusts volume
				
//				FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//				switch (audioType) {
//				case ("Player Laserbeam"):
//					volume.setValue(-6);
//					break;
//				case ("Destroyed Explosion"):
//					volume.setValue(-2);
//					break;
//				case ("Alien Spaceship Destroyed"):
//					break;
//				case ("Alien Bomb Impact"):
//					volume.setValue(-2);
//				}
				clip.startClip();;
			}
		}

	}

	// Play the background music
	public void playMusicAudio(String audioType) throws UnsupportedAudioFileException, IOException {
		if (backGroundMusic == null) {
			backGroundMusic = audioDatabase.getAudioClip(audioType);

			if (!(backGroundMusicInputStream == null)) {
				backGroundMusicInputStream.startClip();
			}
		}
	}

}
