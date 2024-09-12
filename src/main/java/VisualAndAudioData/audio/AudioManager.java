package VisualAndAudioData.audio;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.enums.LevelSongs;
import com.badlogic.gdx.Audio;
import game.level.enums.LevelDifficulty;
import game.level.enums.LevelLength;

import java.io.IOException;
import java.util.*;

import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {

    private static AudioManager instance = new AudioManager();
    private CustomAudioClip backGroundMusic = null;
    private AudioDatabase audioDatabase = AudioDatabase.getInstance();
    private AudioEnums currentSong;
    private Queue<AudioEnums> backgroundMusicTracksThatHavePlayed = new LinkedList<>();
    private Map<AudioEnums, Long> lastPlayTimeMap = new HashMap<>();
    private static final long COOLDOWN_DURATION = 1000; // Cooldown in milliseconds, adjust as needed

    public boolean testMode = false;
    public boolean muteMode = false;

    private AudioManager () {

    }

    //Resets the manager
    public void resetManager () {
        // Removing or placing the line below somewhere else completely bricks level
        // transitioning, idfk why tho
        backGroundMusic = null;
        audioDatabase.resetSongs();
        lastPlayTimeMap.clear();
    }

    public static AudioManager getInstance () {
        return instance;
    }

    public void addAudio (AudioEnums audioType) throws UnsupportedAudioFileException, IOException {
        playAudio(audioType);
    }

    // Play singular audios
    private void playAudio (AudioEnums audioType) throws UnsupportedAudioFileException, IOException {
        if (audioType != null) {
            CustomAudioClip clip = audioDatabase.getAudioClip(audioType);
            if (clip != null && canPlayAudio(audioType)) {
                clip.startClip();
                lastPlayTimeMap.put(audioType, System.currentTimeMillis()); // Update last played time
            }
        }
    }

    private static final Set<AudioEnums> soundsWithCooldown = Set.of(
            AudioEnums.NotEnoughMinerals
    );

    private boolean canPlayAudio (AudioEnums audioType) {
        if (!soundsWithCooldown.contains(audioType)) {
            return true; // No cooldown needed for this sound
        }
        Long lastPlayTime = lastPlayTimeMap.get(audioType);
        if (lastPlayTime == null) {
            return true; // No record found, can play
        }
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastPlayTime >= COOLDOWN_DURATION);
    }

    // Plays the background music directly, overwriting existing music
    public void playBackgroundMusic (AudioEnums audioType, boolean loop) throws UnsupportedAudioFileException, IOException {
        if(backGroundMusic != null){
            backGroundMusic.stopClip();
        }

        backGroundMusic = audioDatabase.getAudioClip(audioType);
        if (!(backGroundMusic == null)) {
            if(muteMode){
                backGroundMusic.muteAudioClip();
            }
            backGroundMusic.setLoop(loop);
            backGroundMusic.startClip();
            this.currentSong = backGroundMusic.getAudioType();

        }
    }


    public void playRandomBackgroundMusic (LevelDifficulty difficulty, LevelLength length, boolean loop) throws UnsupportedAudioFileException, IOException {
        AudioEnums backgroundMusic = null;
        int attempts = 0; // Initialize a counter for the number of attempts
        boolean allowDuplicates = false; // Flag to allow duplicates after 10 attempts

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
            System.out.println("I'm in a do-while loop trying to select: " + difficulty + " / " + length + " and selected " + backgroundMusic);

            attempts++; // Increment the attempt counter

            if (attempts > 10) {
                allowDuplicates = true; // Allow duplicates after 10 attempts
                System.out.println("Exceeded 10 attempts. Allowing duplicate songs.");
                break; // Exiting the loop, but since we're allowing duplicates now, we'll simply proceed.
            }
        }
        // Continue if the track has not been played recently or if duplicates are allowed
        while (backgroundMusicTracksThatHavePlayed.contains(backgroundMusic) && !allowDuplicates && backgroundMusic != null);
        if (backgroundMusic != null) {
            if (testMode) {
                playBackgroundMusic(AudioEnums.Large_Ship_Destroyed, false);
            } else {
                playBackgroundMusic(backgroundMusic, loop);
            }

            addTrackToHistory(backgroundMusic);
            this.currentSong = backgroundMusic;
        }
    }


    private void addTrackToHistory (AudioEnums track) {
        backgroundMusicTracksThatHavePlayed.add(track);
        // Remove the oldest entry if the list size exceeds 3
        if (backgroundMusicTracksThatHavePlayed.size() > 3) {
            backgroundMusicTracksThatHavePlayed.poll(); // This is more efficient for queues
        }
    }

    public void stopMusicAudio () {
        if (backGroundMusic != null) {
            backGroundMusic.stopClip();
            backGroundMusic.setFramePosition(0);
//            backGroundMusic = null;
        }
    }

    public AudioEnums getCurrentSong () {
        return currentSong;
    }

    public void setCurrentSong (AudioEnums currentSong) {
        this.currentSong = currentSong;
    }

    public CustomAudioClip getBackgroundMusic () {
        return this.backGroundMusic;
    }


}