package net.riezebos.bruus.tbd.visualsandaudio.data.audio;

import javafx.scene.media.MediaPlayer;
import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.settings.Settings;
import net.riezebos.bruus.tbd.game.settings.SettingsManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicTrack;


public class CustomAudioClip {

    private AudioLoader audioLoader = AudioLoader.getInstance();
    private MediaPlayer mediaPlayer; // JavaFX MediaPlayer for in-memory audio
    private AudioEnums clipType; // null for a music clip, which has a musicTrack instead
    private MusicTrack musicTrack; // null for a sound effect
    private boolean ownsPlayer = false; // Set for music clips created fresh; only those free their player in dispose()
    private boolean loop;
    private double pausedPosition = 0; // Position to remember on pause

    private double songDuration = 0; //fallback for local files since the game doesnt wait for it to load before playing, thus returning 0

    private boolean isMediaPlayerPlaying = false; // Flag to track if MediaPlayer is playing
    private boolean isMediaPlayerFinished = false; // Flag to track if MediaPlayer has finished
    private boolean isPaused = false; // Flag to track paused state
    private boolean startedSinceReset = false; // Set when the clip starts, so a reset only touches clips that played
    private boolean music = false; // True for a music clip, which follows the music volume setting; every other clip is an effect

    public CustomAudioClip(AudioEnums clipType) {
        this.clipType = clipType;
        this.loop = false;

        initMediaPlayer();
    }

    //A music clip. ownsPlayer is true for a clip created fresh for one use, false for a clip kept in the AudioDatabase pool
    public CustomAudioClip(MusicTrack musicTrack, boolean ownsPlayer) {
        this.musicTrack = musicTrack;
        this.ownsPlayer = ownsPlayer;
        this.music = true;
        this.loop = false;

        initMediaPlayer();
    }

    private void initMediaPlayer() {
        try {
            mediaPlayer = musicTrack != null ? audioLoader.getMusicfile(musicTrack) : audioLoader.getSoundfile(clipType);

            if (mediaPlayer != null) {
                isMediaPlayerFinished = false; // Media has not finished yet
                isMediaPlayerPlaying = false;  // Media is not playing yet

                mediaPlayer.setOnPlaying(() -> {
                    isMediaPlayerPlaying = true; // Mark media as playing
                    isMediaPlayerFinished = false; // Reset finished flag
                });

                mediaPlayer.setOnEndOfMedia(() -> {
                    isMediaPlayerPlaying = false; // Mark media as stopped
                    isMediaPlayerFinished = true; // Mark media as finished
                });


                if(musicTrack != null){
                    this.songDuration = mediaPlayer.getTotalDuration().toSeconds(); //try to load it prematurely as a fallback
                }
            }
        } catch (Exception e) {
            System.out.println("Missing song file: " + (musicTrack != null ? musicTrack : this.getAudioType()));

            e.printStackTrace();
        }
    }

    public double getTotalSecondsInPlayback() {
        if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
            return mediaPlayer.getTotalDuration().toSeconds();
        }
        return songDuration; // Default or fallback value, defaults to 0
    }

    // Get the current playback position in seconds (for MediaPlayer and streams)
    public double getCurrentSecondsInPlayback() {
        if (mediaPlayer != null && mediaPlayer.getCurrentTime() != null && isMediaPlayerPlaying) {
            return mediaPlayer.getCurrentTime().toSeconds();
        }
        return 0; // Return 0 if the media hasn't started playing yet
    }


    // Set playback position using seconds (for both MediaPlayer and streams)
    public void setPlaybackPosition(double seconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(javafx.util.Duration.seconds(seconds));
            mediaPlayer.stop();
        }
    }

    private boolean aboveThreshold() {
        if (clipType == null) {
            return false; //a music clip has no threshold
        }
        switch (clipType) {
            case Large_Ship_Destroyed:
            case Alien_Bomb_Impact:
                return getCurrentSecondsInPlayback() > 1.2;
            case Destroyed_Explosion:
                return getCurrentSecondsInPlayback() > 0.5;
            default:
                return false;
        }
    }

    public void stopClip() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop the MediaPlayer

        }
    }

    public void dispose() {
        if (mediaPlayer != null && ownsPlayer) {
            mediaPlayer.stop();
            mediaPlayer.dispose(); // Release native resources
            mediaPlayer = null;
        }
    }


    public void startClip() {
        if (mediaPlayer == null) {
            return; //the file failed to load, there is nothing to play
        }
        adjustVolume();
        mediaPlayer.play();
        startedSinceReset = true;

        //Wordt al gehandeld in een runnable op de mediaPlayer zelf, maar misschien voorkomt dit de audio bug?
        isMediaPlayerPlaying = true;
        isMediaPlayerFinished = false;

        if (loop) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
    }

    // Pause the playback and let the current thread finish
    public synchronized void pauseClip() {
        if (isPaused) return; // Avoid double pause
        isPaused = true;
        if (mediaPlayer != null && isMediaPlayerPlaying) {
            pausedPosition = mediaPlayer.getCurrentTime().toSeconds();
            mediaPlayer.pause(); // Pause the MediaPlayer
        }
    }

    // Resume playback for both MediaPlayer and streamed audio
    public synchronized void resumeClip() {
        if (!isPaused) return; // Only resume if paused
        isPaused = false;
        if (mediaPlayer != null && isMediaPlayerPlaying) { //Only resume if the clip was actually playing
            mediaPlayer.seek(javafx.util.Duration.seconds(pausedPosition));
            mediaPlayer.play(); // Resume MediaPlayer playback
        }
    }


    public void muteAudioClip() {
        if (mediaPlayer != null) {
            mediaPlayer.setMute(true);
        }
    }

    //Dit is allemaa; "* 0.7f" omdat de default audio levels zijn veranderd met het gebruik van MediaPlayer ipv Clip objecten
    public void adjustVolume() {
        if (mediaPlayer != null) {
            double volume = clipType == null ? 0.7f : switch (clipType) {
                case Player_Laserbeam -> 0.65 * 0.7f;
                case Large_Ship_Destroyed -> 0.6 * 0.7f;
                case Alien_Spaceship_Destroyed -> 0.4 * 0.7f;
                case Alien_Bomb_Impact -> 0.3 * 0.7f;
                case Alien_Bomb_Destroyed -> 0.2 * 0.7f;
                case Default_EMP -> 0.07f * 0.7f;
                case NotEnoughMinerals -> 0.6f * 0.7f;
                case GenericError -> 0.6f * 0.7f;
                case ChargingBigIronLaserbeam -> 0.5f;
                case ScourgeDeath -> 0.5f;
                case ScourgeCollision -> 0.5f;
                case ScourgeNoticed -> 0.5f;
                case MutaliskBirth -> 0.5f;
                case MutaliskDeath -> 0.5f;
                case QueenDeath -> 0.5f;
                case DevourerBirth -> 0.5f;
                case DevourerDeath -> 0.5f;
                case DevourerHit -> 0.5f;
                case BroodlingAttached -> 0.5f;
                case GuardianBirth -> 0.5f;
                case GuardianDeath -> 0.5f;
                case OverlordDeath -> 0.5f;
                default -> 0.7f;
            };
            volume *= getSettingsFactor();
            mediaPlayer.setVolume(volume);
            if(DevTestSettings.devTestMuteMode){
                mediaPlayer.setVolume(0.01);
            }
        }
    }


    //The player's choice from the settings page: the music or the effects percentage
    private double getSettingsFactor() {
        Settings settings = SettingsManager.getInstance().getSettings();
        return (music ? settings.getMusicVolume() : settings.getEffectsVolume()) / 100.0;
    }

    public boolean hasStartedSinceReset() {
        return startedSinceReset;
    }

    public void clearStartedSinceReset() {
        startedSinceReset = false;
    }

    //True when the file could not be loaded or JavaFX stopped the player after a media error
    public boolean hasFailedToLoad() {
        return mediaPlayer == null || mediaPlayer.getStatus() == MediaPlayer.Status.HALTED;
    }

    public boolean isRunning() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public boolean isFinished() {
        if (isPaused) {
            return false;
        }

        if(isMediaPlayerFinished && this.isLoop()){
            this.setPlaybackPosition(0);
            this.startClip();
        }
        // Use the finished flag for MediaPlayer
        if (isMediaPlayerFinished) {
            return true;
        }

        //For short duration sound effects
        if (aboveThreshold()) {
            return true;
        }

        return false;
    }


    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        if (mediaPlayer != null) {
            mediaPlayer.setCycleCount(loop ? MediaPlayer.INDEFINITE : 1);
        }
    }

    //Null for a music clip
    public AudioEnums getAudioType() {
        return this.clipType;
    }

    //Null for a sound effect
    public MusicTrack getMusicTrack() {
        return this.musicTrack;
    }

    public MediaPlayer getMediaPlayer() {
        if(this.mediaPlayer == null){
            initMediaPlayer();
        }
        return this.mediaPlayer;
    }

    public boolean isMediaPlayerPlaying() {
        return isMediaPlayerPlaying;
    }

    public void setMediaPlayerPlaying(boolean mediaPlayerPlaying) {
        isMediaPlayerPlaying = mediaPlayerPlaying;
    }

    public boolean isMediaPlayerFinished() {
        return isMediaPlayerFinished;
    }

    public void setMediaPlayerFinished(boolean mediaPlayerFinished) {
        isMediaPlayerFinished = mediaPlayerFinished;
    }
}
