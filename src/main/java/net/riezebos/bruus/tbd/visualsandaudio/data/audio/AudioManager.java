package net.riezebos.bruus.tbd.visualsandaudio.data.audio;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelTypes;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicMediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicTrack;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers.MacOSMediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers.SpotifyMediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    private static AudioManager instance = new AudioManager();
    private CustomAudioClip backGroundMusic = null;
    private AudioDatabase audioDatabase = AudioDatabase.getInstance();
    private MusicTrack currentSong;
    private CustomAudioClip previewClip = null; //The song the PLAYLIST page plays; always a fresh owned clip, never a pooled one
    private boolean menuMusicPausedByPreview = false; //Set when startPreview paused backGroundMusic, so stopPreviewAndResume resumes it
    private Map<AudioEnums, Long> lastPlayTimeMap = new HashMap<>();
    private MusicMediaPlayer musicMediaPlayer = DevTestSettings.useItunes ? MusicMediaPlayer.iTunesMacOS : MusicMediaPlayer.LocalFiles;
    private MacOSMediaPlayer macOSMediaPlayer = MacOSMediaPlayer.getInstance();
    private SpotifyMediaPlayer spotifyMediaPlayer = SpotifyMediaPlayer.getInstance();
    private double predictedEndGameSeconds = -1; // Predicted game seconds when the song will end
    private double lastSyncGameSeconds = -1; // Initialize to a value ensuring immediate sync on the first check
    private double musicStartGameSeconds = -1; // Game seconds when the current level's song was started
    private static final double MUSIC_END_GRACE_SECONDS = 5; // How long past the predicted song end a level waits for the end-of-song signal
    private static final double FALLBACK_LEVEL_SECONDS = 240; // Level length when the song failed to load

    private AudioManager() {


    }

    //Resets the manager
    public void resetManager() {
        releaseBackgroundMusic();
        predictedEndGameSeconds = -1;
        lastSyncGameSeconds = -1;
        musicStartGameSeconds = -1;
        audioDatabase.resetAudio();
        lastPlayTimeMap.clear();
    }

    //Stops the current background music, disposes it (a no-op for pooled clips) and clears the field
    private void releaseBackgroundMusic() {
        if (backGroundMusic != null) {
            backGroundMusic.setLoop(false);
            backGroundMusic.setPlaybackPosition(0);
            backGroundMusic.stopClip();
            backGroundMusic.dispose();
            backGroundMusic = null;
        }
    }

    public static AudioManager getInstance() {
        return instance;
    }

    //Plays the whole song once from the start for the PLAYLIST page. A preview that is already playing is stopped and disposed first.
    //The menu music is paused in place, only when it exists and is playing, and is never released
    public boolean startPreview(MusicTrack track) {
        disposePreview();
        CustomAudioClip clip = new CustomAudioClip(track, true);
        if (clip.hasFailedToLoad()) {
            clip.dispose();
            return false; //the menu music is left alone
        }
        if (!menuMusicPausedByPreview && backGroundMusic != null && backGroundMusic.isMediaPlayerPlaying()) {
            backGroundMusic.pauseClip();
            menuMusicPausedByPreview = true;
        }
        clip.setLoop(false);
        if (DevTestSettings.devTestMuteMode) {
            clip.muteAudioClip();
        }
        clip.startClip();
        previewClip = clip;
        return true;
    }

    //Disposes a running preview and clears the paused-by-preview flag without resuming; for callers that restart the menu music themselves
    public void discardPreview() {
        disposePreview();
        menuMusicPausedByPreview = false;
    }

    //Stops and disposes the preview, then resumes the menu music where it was paused, only when startPreview paused it
    public void stopPreviewAndResume() {
        disposePreview();
        if (menuMusicPausedByPreview) {
            menuMusicPausedByPreview = false;
            if (backGroundMusic != null) {
                backGroundMusic.resumeClip();
            }
        }
    }

    //True once when the preview reached its end by itself; the clip is disposed then, and the menu music stays paused
    public boolean pollPreviewEnded() {
        if (previewClip != null && previewClip.isMediaPlayerFinished()) {
            disposePreview();
            return true;
        }
        return false;
    }

    public double getPreviewSeconds() {
        return previewClip == null ? 0 : previewClip.getCurrentSecondsInPlayback();
    }

    public double getPreviewTotalSeconds() {
        return previewClip == null ? 0 : previewClip.getTotalSecondsInPlayback();
    }

    private void disposePreview() {
        if (previewClip != null) {
            previewClip.stopClip();
            previewClip.dispose();
            previewClip = null;
        }
    }

    public void addAudio(AudioEnums audioType) {
        playAudio(audioType);
    }

    // Play singular audios
    private void playAudio(AudioEnums audioType) {
        if (audioType != null && canPlayAudio(audioType)) {
            CustomAudioClip clip = audioDatabase.getAudioClip(audioType);
            if (clip != null) {
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
    public void playDefaultBackgroundMusicForALevel(MusicTrack track, boolean loop) {
        releaseBackgroundMusic();

        backGroundMusic = audioDatabase.getMusicClip(track);
        if (backGroundMusic != null) {
            if (DevTestSettings.devTestMuteMode) {
                System.out.println("Played audio through AudioManager but MuteMode was ON");
                backGroundMusic.muteAudioClip();
            }
            backGroundMusic.setLoop(loop);
            backGroundMusic.startClip();
            this.currentSong = track;
        }
    }

    //Stops Apple Music or Spotify when one of them plays the music, as when leaving a run
    public void stopExternalMusicPlayer() {
        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS && !DevTestSettings.devTestMuteMode) { //if mute mode is on, dont stop playback since it shouldnt have started
            macOSMediaPlayer.stopPlayback();
            macOSMediaPlayer.resetCurrentSeconds();
        }
        if (this.musicMediaPlayer == MusicMediaPlayer.Spotify && !DevTestSettings.devTestMuteMode) {  //if mute mode is on, dont stop playback since it shouldnt have started
            spotifyMediaPlayer.stopPlayback();
        }
    }

    public void stopMusicAudio() {
        if (backGroundMusic != null) {
            AudioDatabase.getInstance().removeClipFromActiveClips(backGroundMusic);
            releaseBackgroundMusic();
            currentSong = null;
        }

        if (GameState.getInstance().getGameState().equals(GameStatusEnums.Dead) || GameState.getInstance().getGameState().equals(GameStatusEnums.Show_Level_Score_Card)) {
            stopExternalMusicPlayer();
            releaseBackgroundMusic();
            currentSong = null;
        }
    }

    public MusicTrack getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(MusicTrack currentSong) {
        this.currentSong = currentSong;
    }

    public MusicMediaPlayer getMusicMediaPlayer() {
        return musicMediaPlayer;
    }

    public void setMusicMediaPlayer(MusicMediaPlayer musicMediaPlayer) {
        this.musicMediaPlayer = musicMediaPlayer;
    }

    public void tryToDisposeBackgroundMusic() {
        if (backGroundMusic != null) {
            backGroundMusic.dispose();
            backGroundMusic = null;
        }
    }

    public boolean isLevelMusicFinished() {
        if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) || this.musicMediaPlayer == MusicMediaPlayer.LocalFiles) {
            if (backGroundMusic != null) {
                if (shouldResync(GameState.getInstance().getGameSeconds())) {
                    synchronizePrediction();
                    lastSyncGameSeconds = GameState.getInstance().getGameSeconds();
                }
                if (backGroundMusic.isFinished()) {
                    return true;
                }
                return !backGroundMusic.isLoop() && isLevelMusicOverdue();
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

    //Safety net so a regular level can't hang when its song never reports its end or fails to load
    private boolean isLevelMusicOverdue() {
        double gameSeconds = GameState.getInstance().getGameSeconds();
        if (predictedEndGameSeconds > 0 && gameSeconds > predictedEndGameSeconds + MUSIC_END_GRACE_SECONDS) {
            System.out.println("Level music did not report its end, ending the level after its predicted end");
            return true;
        }
        if (backGroundMusic.hasFailedToLoad() && musicStartGameSeconds >= 0 && gameSeconds > musicStartGameSeconds + FALLBACK_LEVEL_SECONDS) {
            System.out.println("Level music failed to load, ending the level after the fallback length");
            return true;
        }
        return false;
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
            if (backGroundMusic != null) {
                backGroundMusic.stopClip();
            }
        }
    }

    private void goToNextSong() {
        if (this.musicMediaPlayer == MusicMediaPlayer.iTunesMacOS) {
            macOSMediaPlayer.goToNextSong();
        } else if (this.musicMediaPlayer == MusicMediaPlayer.Spotify) {
            spotifyMediaPlayer.goToNextSong();
        } else if (this.musicMediaPlayer == MusicMediaPlayer.LocalFiles) {
            //idk moet dit iets doen? Reset de clip en stop hem voor de zekerheid, mogelijk geheel onnodig
            releaseBackgroundMusic(); //release het attribuut voor de volgende
        }
    }

    private boolean shouldResync(double currentGameSeconds) {
        if(predictedEndGameSeconds <= 0){
            return true;
        }

        // Default resync interval
        double resyncInterval = 10;
        if(this.musicMediaPlayer == MusicMediaPlayer.LocalFiles || LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss)) {
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
            if (backGroundMusic != null) {
                actualCurrentSeconds = backGroundMusic.getCurrentSecondsInPlayback();
                totalSeconds = backGroundMusic.getTotalSecondsInPlayback();
            }
        }

        if (actualCurrentSeconds >= 0 && totalSeconds > 0) {
            predictedEndGameSeconds = GameState.getInstance().getGameSeconds() + (totalSeconds - actualCurrentSeconds);
        }
    }


    public boolean isBackgroundMusicInitializing() {
        if (this.musicMediaPlayer == MusicMediaPlayer.LocalFiles) {
            if (backGroundMusic == null) {
                return false;
            }
            return backGroundMusic.getTotalSecondsInPlayback() < 0;
        }
        return false;
    }

    public void playDefaultBackgroundMusicForALevel() {
        if (this.musicMediaPlayer == MusicMediaPlayer.LocalFiles) {
            releaseBackgroundMusic();

            MusicTrack track = MusicTrack.getRandomLevelTrack();
            CustomAudioClip clip = new CustomAudioClip(track, true); //create a new customaudioclip as the database does NOT preload these, loop defaults to false
            backGroundMusic = clip; //set before starting, so a clip that failed to load is still tracked by the level-end safety net
            currentSong = track;
            musicStartGameSeconds = GameState.getInstance().getGameSeconds();
            clip.startClip(); //fire & forget

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

    //Applies the volume settings to the music and every sound playing now, so a change is heard at once
    public void applyVolumeSettings() {
        if (backGroundMusic != null) {
            backGroundMusic.adjustVolume();
        }
        for (CustomAudioClip audioClip : audioDatabase.getAllActiveClips()) {
            audioClip.adjustVolume();
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
            if (backGroundMusic != null) {
                backGroundMusic.pauseClip();
            }
        } else if (this.musicMediaPlayer.equals(MusicMediaPlayer.iTunesMacOS)) {
            if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) && backGroundMusic != null) {
                backGroundMusic.pauseClip();
            } else {
                macOSMediaPlayer.stopPlayback();
            }
        } else if (this.musicMediaPlayer.equals(MusicMediaPlayer.Spotify)) {
            if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) && backGroundMusic != null) {
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
            if (backGroundMusic != null) {
                backGroundMusic.resumeClip();
            }
        } else if (this.musicMediaPlayer.equals(MusicMediaPlayer.iTunesMacOS)) {
            if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) && backGroundMusic != null) {
                backGroundMusic.resumeClip();
            } else {
                macOSMediaPlayer.resumePlayback();
            }
        } else if (this.musicMediaPlayer.equals(MusicMediaPlayer.Spotify)) {
            if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) && backGroundMusic != null) {
                backGroundMusic.resumeClip();
            } else {
                spotifyMediaPlayer.resumePlayback();
            }
        }

    }

    public double getPredictedEndGameSeconds() {
        return predictedEndGameSeconds;
    }

    public boolean bossIsBarking(FinalBoss finalBoss) {
        return audioDatabase.getAllActiveClips().stream()
                .anyMatch(clip -> clip.getAudioType() != null && finalBoss.getBossBarker().getAllBarkEnums().contains(clip.getAudioType()));
    }
}