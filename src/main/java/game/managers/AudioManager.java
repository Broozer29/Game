package game.managers;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import data.AudioDatabase;

public class AudioManager {

	private static AudioManager instance = new AudioManager();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private Clip backGroundMusic = null;
	private AudioInputStream backGroundMusicInputStream = null;
	private AudioDatabase audioDatabase = AudioDatabase.getInstance();

	private AudioManager() {

	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		if (backGroundMusic != null) {
			backGroundMusic.stop();
			backGroundMusic.close();
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
		Clip clip = null;
		
		switch (audioType) {
		case ("Player Laserbeam"):
			clip = audioDatabase.getLaserBeam();
			break;
		case ("Destroyed Explosion"):
			clip = audioDatabase.getDestroyedExplosion();
			break;
		case ("Alien Spaceship Destroyed"):
			clip = audioDatabase.getDefaultAlienExplosion();
			break;
		case("Alien Bomb Impact"):
			clip = audioDatabase.getAlienBombImpact();
			break;
		}

		if (clip != null) {
			// Adjusts volume
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			switch(audioType) {
			case ("Player Laserbeam"):
				volume.setValue(-6);
				break;
			case ("Destroyed Explosion"):
				volume.setValue(-2);
				break;
			case ("Alien Spaceship Destroyed"):
				break;
			case("Alien Bomb Impact"):
				volume.setValue(-2);
			}

			clip.start();
		}

	}

	// Play the background music
	public void playMusicAudio(String audioType) throws UnsupportedAudioFileException, IOException {
		if (backGroundMusic == null) {
			switch (audioType) {
//			case ("defaultmusic"):
//				backGroundMusicInputStream = audioDatabase.getDefaultMusic();
//			case ("Furi - Make this right"):
//				backGroundMusicInputStream = audioDatabase.getFuriMakeThisRight();
//			case ("Furi - Wisdom of rage"):
//				backGroundMusicInputStream = audioDatabase.getFuriWisdomOfRage();
//			case ("Furi - My only chance"):
//				backGroundMusicInputStream = audioDatabase.getFuriMyOnlyChance();
			}

			if (!(backGroundMusicInputStream == null)) {
				try {
					backGroundMusic = AudioSystem.getClip();
					backGroundMusic.open(backGroundMusicInputStream);
					backGroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
