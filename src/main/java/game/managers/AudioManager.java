package game.managers;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {

	private static AudioManager instance = new AudioManager();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private Clip backGroundMusic = null;
	private AudioInputStream backGroundMusicInputStream = null;

	private AudioManager() {

	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		backGroundMusic.stop();
		backGroundMusic.close();
		backGroundMusic = null;
		backGroundMusicInputStream = null;

	}

	public static AudioManager getInstance() {
		return instance;
	}

	// Wss niet meer nodig
//	public void addAudioToPlayList(String audioType) throws UnsupportedAudioFileException, IOException {
//		playAudio(audioType);
//	}

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

	// Voegt background muziek toe en loopt het.
	public void playBackgroundMusic(String audioType) throws UnsupportedAudioFileException, IOException {
		if (backGroundMusic == null) {
			playMusicAudio(audioType);
		}
	}

	// Play singular audios
	private void playAudio(String audioType) throws UnsupportedAudioFileException, IOException {
		Clip clip;
		AudioInputStream playerMissileAudio = null;

		switch (audioType) {
		case ("Player Laserbeam"):
			playerMissileAudio = AudioSystem
					.getAudioInputStream(new File("src/resources/audio/laserbeam1.wav").getAbsoluteFile());
		}

		try {
			clip = AudioSystem.getClip();
			clip.open(playerMissileAudio);

			// Adjusts volume
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-4);

			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	// Play the background music
	private void playMusicAudio(String audioType) throws UnsupportedAudioFileException, IOException {
		switch (audioType) {
		case ("defaultmusic"):
			backGroundMusicInputStream = AudioSystem
					.getAudioInputStream(new File("src/resources/audio/music/defaultmusic.wav").getAbsoluteFile());
		}

		try {
			backGroundMusic = AudioSystem.getClip();
			backGroundMusic.open(backGroundMusicInputStream);
			backGroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

}
