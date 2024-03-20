package VisualAndAudioData.audio;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.enums.LevelSongs;
import game.spawner.enums.LevelDifficulty;
import game.spawner.enums.LevelLength;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {

	private static AudioManager instance = new AudioManager();
	private CustomAudioClip backGroundMusic = null;
	private AudioDatabase audioDatabase = AudioDatabase.getInstance();

	private Queue<LevelSongs> backgroundMusicTracksThatHavePlayed = new LinkedList<>();
	private LevelSongs currentSong;

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


	public void playRandomBackgroundMusic(LevelDifficulty difficulty, LevelLength length) throws UnsupportedAudioFileException, IOException {
		LevelSongs backgroundMusic;
		do {
			// Select a song based on provided criteria or randomly if none are provided
			if (difficulty != null && length != null) {
				backgroundMusic = LevelSongs.getRandomSong(difficulty, length);
			} else if (difficulty != null) {
				backgroundMusic = LevelSongs.getRandomSongByDifficulty(difficulty);
			} else if (length != null) {
				backgroundMusic = LevelSongs.getRandomSongByLength(length);
			} else {
				backgroundMusic = LevelSongs.getRandomSong();
			}
		}
		while (backgroundMusicTracksThatHavePlayed.contains(backgroundMusic) && backgroundMusic != null);
		if (backgroundMusic != null) {
			playMusicAudio(AudioEnums.Large_Ship_Destroyed);
//			playMusicAudio(backgroundMusic.getAudioEnum());
			System.out.println("Comment line 82 in AudioManager for functional music");
			addTrackToHistory(backgroundMusic);
			this.currentSong = backgroundMusic;
		}
	}

	private void addTrackToHistory(LevelSongs track) {
		backgroundMusicTracksThatHavePlayed.add(track);
		// Remove the oldest entry if the list size exceeds 3
		if (backgroundMusicTracksThatHavePlayed.size() > 3) {
			backgroundMusicTracksThatHavePlayed.poll(); // This is more efficient for queues
		}
	}


//	public void playRandomBackgroundMusic() {
//		AudioEnums backGroundMusicEnum;
//		do {
//			backGroundMusic = audioDatabase.selectRandomMusicTrack();
//			backGroundMusicEnum = backGroundMusic.getAudioType();
//		} while (backgroundMusicTracksThatHavePlayed.contains(backGroundMusicEnum));
//
////		backGroundMusic = AudioDatabase.getInstance().getAudioClip(AudioEnums.Destroyed_Explosion);
//
//		if (backGroundMusic != null) {
//			backGroundMusic.startClip();
//			addTrackToHistory(backGroundMusicEnum);
//		}
//	}
//
//	private void addTrackToHistory(AudioEnums track) {
//		backgroundMusicTracksThatHavePlayed.add(track);
//		// Remove the oldest entry if the list size exceeds 3
//		if (backgroundMusicTracksThatHavePlayed.size() > 3) {
//			backgroundMusicTracksThatHavePlayed.remove(0);
//		}
//	}
	
	public void stopMusicAudio() {
		if (backGroundMusic != null) {
			backGroundMusic.stopClip();
			backGroundMusic.setFramePosition(0);
			backGroundMusic = null;
		}
	}

	public LevelSongs getCurrentSong () {
		return currentSong;
	}

	public void setCurrentSong (LevelSongs currentSong) {
		this.currentSong = currentSong;
	}

	public CustomAudioClip getBackgroundMusic() {
		return this.backGroundMusic;
	}


}