package game.managers;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.audio.AudioDatabase;
import data.audio.AudioEnums;
import data.audio.CustomAudioClip;

public class AudioManager {

	private static AudioManager instance = new AudioManager();
	private CustomAudioClip backGroundMusic = null;
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
		}

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
		if (backGroundMusic == null) {
			backGroundMusic = audioDatabase.getAudioClip(audioType);
			if (!(backGroundMusic == null)) {
				backGroundMusic.startClip();
			}
		}
	}

}