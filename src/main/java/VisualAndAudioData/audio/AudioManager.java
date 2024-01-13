package VisualAndAudioData.audio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {

	private static AudioManager instance = new AudioManager();
	private CustomAudioClip backGroundMusic = null;
	private AudioDatabase audioDatabase = AudioDatabase.getInstance();

	private List<AudioEnums> backgroundMusicTracksThatHavePlayed = new ArrayList<>();

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
		}
	}

	public void playRandomBackgroundMusic() {
		AudioEnums backGroundMusicEnum;
		do {
			backGroundMusic = audioDatabase.selectRandomMusicTrack();
			backGroundMusicEnum = backGroundMusic.getAudioType();
		} while (backgroundMusicTracksThatHavePlayed.contains(backGroundMusicEnum));

		if (backGroundMusic != null) {
			backGroundMusic.startClip();
			addTrackToHistory(backGroundMusicEnum);
		}
	}

	private void addTrackToHistory(AudioEnums track) {
		backgroundMusicTracksThatHavePlayed.add(track);
		// Remove the oldest entry if the list size exceeds 3
		if (backgroundMusicTracksThatHavePlayed.size() > 3) {
			backgroundMusicTracksThatHavePlayed.remove(0);
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