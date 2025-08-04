package net.riezebos.bruus.tbd.visualsandaudio.data.audio;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.MiniBossConfig;
import net.riezebos.bruus.tbd.game.level.enums.LevelTypes;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.LevelSongs;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicMediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers.MacOSMediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers.SpotifyMediaPlayer;

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
    private MusicMediaPlayer musicMediaPlayer = MusicMediaPlayer.iTunesMacOS;
    private MacOSMediaPlayer macOSMediaPlayer = MacOSMediaPlayer.getInstance();
    private SpotifyMediaPlayer spotifyMediaPlayer = SpotifyMediaPlayer.getInstance();
    private double predictedEndGameSeconds = -1; // Predicted game seconds when the song will end
    private double lastSyncGameSeconds = -1; // Initialize to a value ensuring immediate sync on the first check

    private boolean isMusicControlledByThirdPartyApp = true;

    public boolean devTestShortLevelMode = false;


    private AudioManager() {
        CustomAudioClip silenceClip = AudioDatabase.getInstance().getAudioClip(AudioEnums.SilentAudio);
        silenceClip.setLoop(true);
        silenceClip.startClip();
    }

    //Resets the manager
    public void resetManager() {
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

    public static AudioManager getInstance() {
        return instance;
    }

    public void addAudio(AudioEnums audioType) {
        playAudio(audioType);
    }

    // Play singular audios
    private void playAudio(AudioEnums audioType) {
        if (audioType != null) {
            CustomAudioClip clip = audioDatabase.getAudioClip(audioType);
            if (clip != null && canPlayAudio(audioType)) {
                clip.startClip();

                if (soundCooldownMap.containsKey(audioType)) {
                    lastPlayTimeMap.put(audioType, System.currentTimeMillis()); // Update last played time
                }
            }
        }
    }

    private static final Map<AudioEnums, Long> soundCooldownMap = Map.of(
            AudioEnums.NotEnoughMinerals, 500L, //in ms
            AudioEnums.PlayerTakesDamage, 100L,
            AudioEnums.ClassCarrierSlowingDown, 200L,
            AudioEnums.ClassCarrierSpeedingUp, 200L,
            AudioEnums.AchievementUnlocked, 200L
    );

    private boolean canPlayAudio(AudioEnums audioType) {
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
    public void playDefaultBackgroundMusic(AudioEnums audioType, boolean loop) {
        if (backGroundMusic != null) {
            backGroundMusic.setLoop(false);
            backGroundMusic.setPlaybackPosition(0);
            backGroundMusic.stopClip();
        }

        backGroundMusic = audioDatabase.getAudioClip(audioType);
        if (backGroundMusic != null) {
            if (DevTestSettings.devTestMuteMode) {
                System.out.println("Played audio through AudioManager but MuteMode was ON");
                backGroundMusic.muteAudioClip();
            }
            backGroundMusic.setLoop(loop);
            backGroundMusic.startClip();
            this.currentSong = backGroundMusic.getAudioType();
        }
    }


    private void playRandomBackgroundMusic(LevelDifficulty difficulty, MiniBossConfig length, boolean loop) {
        AudioEnums backgroundMusic = null;
        int attempts = 0; // Initialize a counter for the number of attempts
        boolean allowDuplicates = false; // Flag to allow duplicates after 10 attempts

        do {
            // Select a song based on provided criteria or randomly if none are provided
            if (difficulty != null && length != null) {
                backgroundMusic = LevelSongs.getRandomSong(difficulty, length);
            } else if (difficulty != null) {
                backgroundMusic = LevelSongs.getRandomSongByDifficulty(difficulty);
            }
            //DEPRECATED DO NOT USE
//            else if (length != null) {
//                backgroundMusic = LevelSongs.getRandomSongByLength(length);
//            }
            else {
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


    private void addTrackToHistory(AudioEnums track) {
        backgroundMusicTracksThatHavePlayed.add(track);
        // Remove the oldest entry if the list size exceeds 3
        if (backgroundMusicTracksThatHavePlayed.size() > 3) {
            backgroundMusicTracksThatHavePlayed.poll(); // This is more efficient for queues
        }
    }

    public void stopMusicAudio() {
        if (backGroundMusic != null) {
            backGroundMusic.stopClip();
            backGroundMusic.setLoop(false);
            backGroundMusic = null;
            currentSong = null;
        }

        if (GameState.getInstance().getGameState().equals(GameStatusEnums.Dead) || GameState.getInstance().getGameState().equals(GameStatusEnums.Show_Level_Score_Card)) {
            if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
                macOSMediaPlayer.stopPlayback();
            }
            if (this.musicMediaPlayer == MusicMediaPlayer.Spotify) {
                spotifyMediaPlayer.stopPlayback();
            }
            backGroundMusic = null;
            currentSong = null;
        }
    }

    public AudioEnums getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(AudioEnums currentSong) {
        this.currentSong = currentSong;
    }

    public MusicMediaPlayer getMusicMediaPlayer() {
        return musicMediaPlayer;
    }

    public void setMusicMediaPlayer(MusicMediaPlayer musicMediaPlayer) {
        this.musicMediaPlayer = musicMediaPlayer;
        if (musicMediaPlayer.equals(MusicMediaPlayer.iTunesMacOS) || musicMediaPlayer.equals(MusicMediaPlayer.Spotify)) {
            isMusicControlledByThirdPartyApp = true;
        } else {
            isMusicControlledByThirdPartyApp = false;
        }
    }

    public boolean isLevelMusicFinished() {
        if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) || this.musicMediaPlayer == MusicMediaPlayer.Default) {
            return backGroundMusic.isFinished();
        }

        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS || this.musicMediaPlayer == MusicMediaPlayer.Spotify) {
            if (!hasStartedMusic()) {
                return false; // Song hasn't started yet
            }

            double currentGameSeconds = GameState.getInstance().getGameSeconds();
            if (predictedEndGameSeconds > 0) {
                if (shouldResync(currentGameSeconds)) {
                    synchronizePrediction(currentGameSeconds);
                    lastSyncGameSeconds = currentGameSeconds;
                }
                // Check if the current game seconds match or exceed the predicted end time
                if (currentGameSeconds >= predictedEndGameSeconds - 2f) {
                    stopPlayback();
                    goToNextSong();
                    return true;
                }
            }
        }

        return false; // If none of the conditions are met, the track hasn't finished
    }

    // Abstracted methods to handle the media players
    private boolean hasStartedMusic() {
        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
            return macOSMediaPlayer.hasStartedMusic();
        } else if (this.musicMediaPlayer == MusicMediaPlayer.Spotify) {
            return spotifyMediaPlayer.hasStartedMusic();
        }
        return false;
    }

    private void stopPlayback() {
        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
            macOSMediaPlayer.stopPlayback();
        } else if (this.musicMediaPlayer == MusicMediaPlayer.Spotify) {
            spotifyMediaPlayer.stopPlayback();
        }
    }

    private void goToNextSong() {
        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
            macOSMediaPlayer.goToNextSong();
        } else if (this.musicMediaPlayer == MusicMediaPlayer.Spotify) {
            spotifyMediaPlayer.goToNextSong();
        }
    }

    private boolean shouldResync(double currentGameSeconds) {
        // Default resync interval
        double resyncInterval = 10;

        // Determine if it's time to resync
        return currentGameSeconds >= lastSyncGameSeconds + resyncInterval;
    }


    private void synchronizePrediction(double currentGameSeconds) {
        double actualCurrentSeconds = -1;
        double totalSeconds = -1;

        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
            macOSMediaPlayer.synchronizePlaybackInfo();
            actualCurrentSeconds = macOSMediaPlayer.getCurrentSeconds();
            totalSeconds = macOSMediaPlayer.getTotalSeconds();
        } else if (this.musicMediaPlayer == MusicMediaPlayer.Spotify) {
            spotifyMediaPlayer.synchronizePlaybackInfo();
            actualCurrentSeconds = spotifyMediaPlayer.getCurrentSeconds();
            totalSeconds = spotifyMediaPlayer.getTotalSeconds();
        }

        if (actualCurrentSeconds >= 0 && totalSeconds > 0) {
            predictedEndGameSeconds = currentGameSeconds + (totalSeconds - actualCurrentSeconds);
        }
    }

    public double getBackgroundSongTotalLength() {
        if (this.musicMediaPlayer == MusicMediaPlayer.Default) {
            return backGroundMusic.getTotalSecondsInPlayback();
        }
        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
            return macOSMediaPlayer.getTotalSeconds();
        }
        if (this.musicMediaPlayer == MusicMediaPlayer.Spotify) {
            return spotifyMediaPlayer.getTotalSeconds();
        }
        return -1;
    }


    public boolean isBackgroundMusicInitializing() {
        if (this.musicMediaPlayer == MusicMediaPlayer.Default) {
            return backGroundMusic.getTotalSecondsInPlayback() < 0;
        }
        return false;
    }

    public void playDefaultBackgroundMusic(LevelDifficulty difficulty, MiniBossConfig miniBossConfig, boolean loop) {
        if (this.musicMediaPlayer == MusicMediaPlayer.Default) {
            this.playRandomBackgroundMusic(difficulty, miniBossConfig, loop);
        }

        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
            macOSMediaPlayer.setPlaybackPositionTo0();
            macOSMediaPlayer.startPlayback();

            // Calculate and store the predicted end time
            double trackDuration = macOSMediaPlayer.getTotalSeconds();
            predictedEndGameSeconds = GameState.getInstance().getGameSeconds() + trackDuration;
            lastSyncGameSeconds = GameState.getInstance().getGameSeconds();
        } else if (this.musicMediaPlayer == MusicMediaPlayer.Spotify) {
            spotifyMediaPlayer.setPlaybackPositionTo0();
            spotifyMediaPlayer.startPlayback();

            double trackDuration = spotifyMediaPlayer.getTotalSeconds();
            predictedEndGameSeconds = GameState.getInstance().getGameSeconds() + trackDuration;
            lastSyncGameSeconds = GameState.getInstance().getGameSeconds();
        }
    }

    public double getCurrentSecondsInPlayback() {
        if (this.musicMediaPlayer == MusicMediaPlayer.Default) {
            if (backGroundMusic == null) {
                return -1;
            }
            return backGroundMusic.getCurrentSecondsInPlayback();
        }

        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
            return macOSMediaPlayer.getCurrentSeconds();
        }

        if(this.musicMediaPlayer == MusicMediaPlayer.Spotify){
            return spotifyMediaPlayer.getCurrentSeconds();
        }

        return -1;
    }

    public double getTotalPlaybackLengthInSeconds() {
        if (this.musicMediaPlayer == MusicMediaPlayer.Default) {
            if (backGroundMusic == null) {
                return 0;
            }
            return backGroundMusic.getTotalSecondsInPlayback();
        }

        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
            return macOSMediaPlayer.getTotalSeconds();
        }

        if(this.musicMediaPlayer == MusicMediaPlayer.Spotify){
            return spotifyMediaPlayer.getTotalSeconds();
        }

        return -1;
    }

    public double getCurrentSongProgression() {
        double currentSeconds = getCurrentSecondsInPlayback();
        double totalSeconds = getTotalPlaybackLengthInSeconds();

        if (totalSeconds <= 0 || currentSeconds < 0) {
            return 0.0;
        }

        return currentSeconds / totalSeconds;
    }


    public void pauseAllAudio() {
        for (CustomAudioClip audioClip : audioDatabase.getAllActiveClips()) {
            audioClip.pauseClip();
        }

        if (this.musicMediaPlayer.equals(MusicMediaPlayer.Default)) {
            backGroundMusic.pauseClip();
        } else if (this.musicMediaPlayer.equals(MusicMediaPlayer.iTunesMacOS)) {
            if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss)) {
                backGroundMusic.pauseClip();
            } else {
                macOSMediaPlayer.stopPlayback();
            }
        } else if (this.musicMediaPlayer.equals(MusicMediaPlayer.Spotify)) {
            if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss)) {
                backGroundMusic.pauseClip();
            } else {
                spotifyMediaPlayer.stopPlayback();
            }
        }
    }

    public void resumeAllAudio() {
        for (CustomAudioClip audioClip : audioDatabase.getAllActiveClips()) {
            audioClip.resumeClip();
        }

        if (this.musicMediaPlayer.equals(MusicMediaPlayer.Default)) {
            backGroundMusic.resumeClip();
        } else if (this.musicMediaPlayer.equals(MusicMediaPlayer.iTunesMacOS)) {
            if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss)) {
                backGroundMusic.resumeClip();
            } else {
                macOSMediaPlayer.resumePlayback();
            }
        } else if (this.musicMediaPlayer.equals(MusicMediaPlayer.Spotify)) {
            if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss)) {
                backGroundMusic.resumeClip();
            } else {
                spotifyMediaPlayer.resumePlayback();
            }
        }

    }

    public CustomAudioClip getBackGroundMusicCustomAudioclip() {
        return backGroundMusic;
    }

    public double getPredictedEndGameSeconds() {
        return predictedEndGameSeconds;
    }

    public void setPredictedEndGameSeconds(double predictedEndGameSeconds) {
        this.predictedEndGameSeconds = predictedEndGameSeconds;
    }

    public boolean isMusicControlledByThirdPartyApp() {
        return isMusicControlledByThirdPartyApp;
    }

    public void setMusicControlledByThirdPartyApp(boolean musicControlledByThirdPartyApp) {
        isMusicControlledByThirdPartyApp = musicControlledByThirdPartyApp;
    }
}