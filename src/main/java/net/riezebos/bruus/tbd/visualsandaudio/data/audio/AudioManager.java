package net.riezebos.bruus.tbd.visualsandaudio.data.audio;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelTypes;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicMediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers.MacOSMediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers.SpotifyMediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    private static AudioManager instance = new AudioManager();
    private CustomAudioClip backGroundMusic = null;
    private AudioDatabase audioDatabase = AudioDatabase.getInstance();
    private AudioEnums currentSong;
    private Map<AudioEnums, Long> lastPlayTimeMap = new HashMap<>();
    private MusicMediaPlayer musicMediaPlayer = MusicMediaPlayer.LocalFiles;
    private MacOSMediaPlayer macOSMediaPlayer = MacOSMediaPlayer.getInstance();
    private SpotifyMediaPlayer spotifyMediaPlayer = SpotifyMediaPlayer.getInstance();
    private double predictedEndGameSeconds = -1; // Predicted game seconds when the song will end
    private double lastSyncGameSeconds = -1; // Initialize to a value ensuring immediate sync on the first check

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
    public void playDefaultBackgroundMusicForALevel(AudioEnums audioType, boolean loop) {
        if (backGroundMusic != null) {
            backGroundMusic.setLoop(loop);
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

    public void stopMusicAudio() {
        if (backGroundMusic != null) {
            backGroundMusic.stopClip();
            backGroundMusic.setLoop(false);
            AudioDatabase.getInstance().removeClipFromActiveClips(backGroundMusic);
            backGroundMusic = null;
            currentSong = null;
        }

        if (GameState.getInstance().getGameState().equals(GameStatusEnums.Dead) || GameState.getInstance().getGameState().equals(GameStatusEnums.Show_Level_Score_Card)) {
            if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS && !DevTestSettings.devTestMuteMode) { //if mute mode is on, dont stop playback since it shouldnt have started
                macOSMediaPlayer.stopPlayback();
                macOSMediaPlayer.resetCurrentSeconds();
            }
            if (this.musicMediaPlayer == MusicMediaPlayer.Spotify && !DevTestSettings.devTestMuteMode) {  //if mute mode is on, dont stop playback since it shouldnt have started
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
    }

    public boolean isLevelMusicFinished() {
        if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) || this.musicMediaPlayer == MusicMediaPlayer.LocalFiles) {
            if (backGroundMusic != null) {
                if (shouldResync(GameState.getInstance().getGameSeconds())) {
                    synchronizePrediction();
                    lastSyncGameSeconds = GameState.getInstance().getGameSeconds();
                }
                return backGroundMusic.isFinished();
            } else {
                return false; //might never be reached but safetynet should music fail to play
            }
        }

        if (!hasStartedMusic()) {
            return false; // Song hasn't started yet
        }

        //For itunes/spotify
        if (predictedEndGameSeconds > 0) {
            if (shouldResync(GameState.getInstance().getGameSeconds())) {
                synchronizePrediction();
                lastSyncGameSeconds = GameState.getInstance().getGameSeconds();
            }
            // Check if the current game seconds match or exceed the predicted end time
            if (GameState.getInstance().getGameSeconds() >= predictedEndGameSeconds - 2f) {
                stopPlayback();
                goToNextSong();
                return true;
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
        } else if (this.musicMediaPlayer == MusicMediaPlayer.LocalFiles) {
            return this.backGroundMusic != null && this.backGroundMusic.isRunning();
        }
        return false;
    }

    private void stopPlayback() {
        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
            macOSMediaPlayer.stopPlayback();
        } else if (this.musicMediaPlayer == MusicMediaPlayer.Spotify) {
            spotifyMediaPlayer.stopPlayback();
        } else if (this.musicMediaPlayer == MusicMediaPlayer.LocalFiles) {
            backGroundMusic.stopClip();
        }
    }

    private void goToNextSong() {
        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
            macOSMediaPlayer.goToNextSong();
        } else if (this.musicMediaPlayer == MusicMediaPlayer.Spotify) {
            spotifyMediaPlayer.goToNextSong();
        } else if (this.musicMediaPlayer == MusicMediaPlayer.LocalFiles) {
            //idk moet dit iets doen? Reset de clip en stop hem voor de zekerheid, mogelijk geheel onnodig
            backGroundMusic.setPlaybackPosition(0);
            backGroundMusic.setLoop(false);
            backGroundMusic.stopClip();
            backGroundMusic = null; //release het attribuut voor de volgende
        }
    }

    private boolean shouldResync(double currentGameSeconds) {
        // Default resync interval
        double resyncInterval = 10;
        if(this.musicMediaPlayer == MusicMediaPlayer.LocalFiles) {
            resyncInterval = 2; //local files can sync much more without issues
        }

        if (predictedEndGameSeconds > 1 && predictedEndGameSeconds - currentGameSeconds < 10) {
            return false; //Never resync if we are almost finished to prevent incorrect portal spawning
        }

        // Determine if it's time to resync
        return currentGameSeconds >= lastSyncGameSeconds + resyncInterval;
    }


    private void synchronizePrediction() {
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
        } else if (this.musicMediaPlayer == MusicMediaPlayer.LocalFiles) {
            actualCurrentSeconds = backGroundMusic.getCurrentSecondsInPlayback();
            totalSeconds = backGroundMusic.getTotalSecondsInPlayback();
        }

        if (actualCurrentSeconds >= 0 && totalSeconds > 0) {
            predictedEndGameSeconds = GameState.getInstance().getGameSeconds() + (totalSeconds - actualCurrentSeconds);
        }
    }


    public boolean isBackgroundMusicInitializing() {
        if (this.musicMediaPlayer == MusicMediaPlayer.LocalFiles) {
            return backGroundMusic.getTotalSecondsInPlayback() < 0;
        }
        return false;
    }

    public void playDefaultBackgroundMusicForALevel() {
        if (this.musicMediaPlayer == MusicMediaPlayer.LocalFiles) {
            if (backGroundMusic != null) {
                backGroundMusic.setLoop(false);
                backGroundMusic.setPlaybackPosition(0);
                backGroundMusic.stopClip();
            }

            CustomAudioClip clip = new CustomAudioClip(AudioEnums.CustomMusicFile); //create a new customaudioclip as the database does NOT preload these, loop defaults to false
            clip.startClip(); //fire & forget
            backGroundMusic = clip;

            lastSyncGameSeconds = GameState.getInstance().getGameSeconds() - 9; //manually force a resync after 1 sec through the isLevelMusicFinished() method
        } else if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
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

    public void pauseAllAudio() {
        if (DevTestSettings.devTestMuteMode) {
            return; //No audio should be playing at all
        }

        for (CustomAudioClip audioClip : audioDatabase.getAllActiveClips()) {
            audioClip.pauseClip();
        }

        if (this.musicMediaPlayer.equals(MusicMediaPlayer.LocalFiles)) {
            backGroundMusic.pauseClip();
        } else if (this.musicMediaPlayer.equals(MusicMediaPlayer.iTunesMacOS)) {
            if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) && backGroundMusic != null) {
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
        if (DevTestSettings.devTestMuteMode) {
            return; //No audio should be playing at all
        }

        for (CustomAudioClip audioClip : audioDatabase.getAllActiveClips()) {
            audioClip.resumeClip();
        }

        if (this.musicMediaPlayer.equals(MusicMediaPlayer.LocalFiles)) {
            backGroundMusic.resumeClip();
        } else if (this.musicMediaPlayer.equals(MusicMediaPlayer.iTunesMacOS)) {
            if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) && backGroundMusic != null) {
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

    public double getPredictedEndGameSeconds() {
        return predictedEndGameSeconds;
    }
}