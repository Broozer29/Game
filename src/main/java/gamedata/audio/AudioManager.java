package gamedata.audio;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {

	private static AudioManager instance = new AudioManager();
	private CustomAudioClip backGroundMusic = null;
	private AudioDatabase audioDatabase = AudioDatabase.getInstance();

	private AudioManager() {

	}

//Resets the manager
	public void resetManager() {
		// Removing or placing the line below somewhere else completely bricks level
		// transitioning, idfk why tho
		backGroundMusic = null;
		audioDatabase.resetSongs();
	}
	
	public static AudioManager getInstance() {
		return instance;
	}

	public void addAudio(AudioEnums audioType) throws UnsupportedAudioFileException, IOException {
		playAudio(audioType);
	}

	// Play singular audios
	private void playAudio(AudioEnums audioType) throws UnsupportedAudioFileException, IOException {
		if (audioType != null) {
			CustomAudioClip clip = audioDatabase.getAudioClip(audioType);
			if (clip != null) {
				clip.startClip();
			}
		}

	}

	// Play the background music
	public void playMusicAudio(AudioEnums audioType) throws UnsupportedAudioFileException, IOException {
		backGroundMusic = audioDatabase.getAudioClip(audioType);
		if (!(backGroundMusic == null)) {
			backGroundMusic.startClip();
			System.out.println("I tried to start a soundtrack in AudioManager line 49");
		}
	}
	
	public void stopMusicAudio() {
		if (backGroundMusic != null) {
			backGroundMusic.stopClip();
			backGroundMusic.setFramePosition(0);
			backGroundMusic = null;
		}
	}

	public CustomAudioClip getBackgroundMusic() {
		return this.backGroundMusic;
	}

}