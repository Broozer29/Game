package net.riezebos.bruus.tbd.visualsandaudio.data.audio;

import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.LevelLength;
import net.riezebos.bruus.tbd.game.level.enums.LevelTypes;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.LevelSongs;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicMediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers.MacOSMediaPlayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class AudioManager {

    private static AudioManager instance = new AudioManager();
    private CustomAudioClip backGroundMusic = null;
    private AudioDatabase audioDatabase = AudioDatabase.getInstance();
    private AudioEnums currentSong;
    private Queue<AudioEnums> backgroundMusicTracksThatHavePlayed = new LinkedList<>();
    private Map<AudioEnums, Long> lastPlayTimeMap = new HashMap<>();
    private MusicMediaPlayer musicMediaPlayer = MusicMediaPlayer.Default;
    private MacOSMediaPlayer macOSMediaPlayer = MacOSMediaPlayer.getInstance();
    public boolean devTestShortLevelMode = false;
    public boolean devTestmuteMode = false;

    private AudioManager () {
        CustomAudioClip silenceClip = AudioDatabase.getInstance().getAudioClip(AudioEnums.SilentAudio);
        silenceClip.setLoop(true);
        silenceClip.startClip();
    }

    //Resets the manager
    public void resetManager () {
        // Removing or placing the line below somewhere else completely bricks level
        // transitioning, idfk why tho

        if (backGroundMusic != null) {
            backGroundMusic.stopClip();
            backGroundMusic.setLoop(false);
        }
        backGroundMusic = null;
        audioDatabase.resetAudio();
        lastPlayTimeMap.clear();
    }

    public static AudioManager getInstance () {
        return instance;
    }

    public void addAudio (AudioEnums audioType){
        playAudio(audioType);
    }

    // Play singular audios
    private void playAudio (AudioEnums audioType) {
        if (audioType != null) {
            CustomAudioClip clip = audioDatabase.getAudioClip(audioType);
            if (clip != null && canPlayAudio(audioType)) {
                clip.startClip();

                if(soundCooldownMap.containsKey(audioType)) {
                    lastPlayTimeMap.put(audioType, System.currentTimeMillis()); // Update last played time
                }
            }
        }
    }

    private static final Map<AudioEnums, Long> soundCooldownMap = Map.of(
            AudioEnums.NotEnoughMinerals, 500L, // Cooldown for NotEnoughMinerals is 2000 ms (2 seconds)
            AudioEnums.PlayerTakesDamage, 100L     // Cooldown for SomeOtherSound is 5000 ms (5 seconds)
    );

    private boolean canPlayAudio (AudioEnums audioType) {
        Long cooldownDuration = soundCooldownMap.get(audioType);

        if (cooldownDuration == null) {
            return true; // No cooldown specified for this sound, so it can play
        }

        Long lastPlayTime = lastPlayTimeMap.get(audioType);

        if (lastPlayTime == null) {
            return true; // No record found, can play
        }

        long currentTime = System.currentTimeMillis();
        return (currentTime - lastPlayTime >= cooldownDuration); // Compare with specific cooldown duration
    }

    // Plays the background music directly, overwriting existing music
    public void playDefaultBackgroundMusic (AudioEnums audioType, boolean loop){
        if (backGroundMusic != null) {
            backGroundMusic.setLoop(false);
            backGroundMusic.setPlaybackPosition(0);
            backGroundMusic.stopClip();
        }

        backGroundMusic = audioDatabase.getAudioClip(audioType);
        if (backGroundMusic != null) {
            if (devTestmuteMode) {
                System.out.println("Played audio through AudioManager but MuteMode was ON");
                backGroundMusic.muteAudioClip();
            }
            backGroundMusic.setLoop(loop);
            backGroundMusic.startClip();
            this.currentSong = backGroundMusic.getAudioType();
        }
    }


    private void playRandomBackgroundMusic (LevelDifficulty difficulty, LevelLength length, boolean loop){
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
                allowDuplicates = true; // Allow duplicates after 10 attempts, it might look like its needed, but it is because of the do-while condition
                System.out.println("Exceeded 10 attempts. Allowing duplicate songs.");
                break; // Exiting the loop, but since we're allowing duplicates now, we'll simply proceed.
            }
        }
        // Continue if the track has not been played recently or if duplicates are allowed
        while (backgroundMusicTracksThatHavePlayed.contains(backgroundMusic) && !allowDuplicates && backgroundMusic != null);
        if (backgroundMusic != null) {
            if (devTestShortLevelMode) {
                playDefaultBackgroundMusic(AudioEnums.Large_Ship_Destroyed, false);
            } else {
                playDefaultBackgroundMusic(backgroundMusic, loop);
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
            backGroundMusic.setLoop(false);
            backGroundMusic = null;
        }

        if (this.musicMediaPlayer == MusicMediaPlayer.MacOS) {
            macOSMediaPlayer.stopPlayback();
            macOSMediaPlayer.stopPolling();
            backGroundMusic = null;
        }
    }

    public AudioEnums getCurrentSong () {
        return currentSong;
    }

    public void setCurrentSong (AudioEnums currentSong) {
        this.currentSong = currentSong;
    }

    public MusicMediaPlayer getMusicMediaPlayer () {
        return musicMediaPlayer;
    }

    public void setMusicMediaPlayer (MusicMediaPlayer musicMediaPlayer) {
        this.musicMediaPlayer = musicMediaPlayer;
    }

    public boolean isLevelMusicFinished () {
        if(LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) || this.musicMediaPlayer == MusicMediaPlayer.Default){
            return backGroundMusic.isFinished();
        }

        if (this.musicMediaPlayer == MusicMediaPlayer.MacOS) {
            if (!macOSMediaPlayer.hasStartedMusic()) {
                return false;  // Song hasn't started yet
            }
            // Check if the track has finished playing
            if (macOSMediaPlayer.hasStartedMusic() && !macOSMediaPlayer.isPlaying() && macOSMediaPlayer.getCurrentSeconds() > 0) {
                return true;  // The track has finished
            }
        }

        return false;  // If none of the conditions are met, the track hasn't finished
    }

    public boolean isBackgroundMusicInitializing () {
        if (this.musicMediaPlayer == MusicMediaPlayer.Default) {
            return backGroundMusic.getTotalSecondsInPlayback() < 0;
        }
        return false;
    }

    public void playDefaultBackgroundMusic (LevelDifficulty difficulty, LevelLength length, boolean loop) {
        if (this.musicMediaPlayer == MusicMediaPlayer.Default) {
            this.playRandomBackgroundMusic(difficulty, length, loop);
        }

        if (this.musicMediaPlayer == MusicMediaPlayer.MacOS) {
            macOSMediaPlayer.setPlaybackPosition(0);
            macOSMediaPlayer.startPlayback();
            macOSMediaPlayer.startPolling();
        }
    }

    public double getCurrentSecondsInPlayback () {
        if (this.musicMediaPlayer == MusicMediaPlayer.Default) {
            if(backGroundMusic == null){
                return -1;
            }
            return backGroundMusic.getCurrentSecondsInPlayback();
        }

        if (this.musicMediaPlayer == MusicMediaPlayer.MacOS) {
            return macOSMediaPlayer.getCurrentSeconds();
        }

        return -1;
    }

    public double getTotalPlaybackLengthInSeconds () {
        if (this.musicMediaPlayer == MusicMediaPlayer.Default) {
            if(backGroundMusic == null){
                return 0;
            }
            return backGroundMusic.getTotalSecondsInPlayback();
        }

        if (this.musicMediaPlayer == MusicMediaPlayer.MacOS) {
            return macOSMediaPlayer.getTotalSeconds();
        }

        return -1;
    }

    public void pauseAllAudio(){
        for(CustomAudioClip audioClip : audioDatabase.getAllActiveClips()){
            audioClip.pauseClip();
        }

        if(LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) || backGroundMusic != null){
            backGroundMusic.pauseClip();
        } else if(this.musicMediaPlayer.equals(MusicMediaPlayer.MacOS)){
            macOSMediaPlayer.stopPlayback();
        }
    }

    public void resumeAllAudio(){
        for(CustomAudioClip audioClip : audioDatabase.getAllActiveClips()){
            audioClip.resumeClip();
        }

        if(LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) || backGroundMusic != null){
            backGroundMusic.resumeClip();
        } else if(this.musicMediaPlayer.equals(MusicMediaPlayer.MacOS)){
            macOSMediaPlayer.resumePlayback();
        }

    }

    public CustomAudioClip getBackGroundMusicCustomAudioclip () {
        return backGroundMusic;
    }

}