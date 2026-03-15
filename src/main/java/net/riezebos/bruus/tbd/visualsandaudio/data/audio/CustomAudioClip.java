package net.riezebos.bruus.tbd.visualsandaudio.data.audio;

import javafx.scene.media.MediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;


public class CustomAudioClip {

    private AudioLoader audioLoader = AudioLoader.getInstance();
    private MediaPlayer mediaPlayer; // JavaFX MediaPlayer for in-memory audio
    private AudioEnums clipType;
    private boolean loop;
    private double pausedPosition = 0; // Position to remember on pause

    private boolean isMediaPlayerPlaying = false; // Flag to track if MediaPlayer is playing
    private boolean isMediaPlayerFinished = false; // Flag to track if MediaPlayer has finished
    private boolean isPaused = false; // Flag to track paused state

    public CustomAudioClip(AudioEnums clipType) {
        this.clipType = clipType;
        this.loop = false;

        initMediaPlayer();
    }

    private void initMediaPlayer() {
        try {
            mediaPlayer = audioLoader.getSoundfile(clipType);

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
            }
        } catch (Exception e) {
            System.out.println("Missing song file: " + this.getAudioType());

            e.printStackTrace();
        }
    }

    public double getTotalSecondsInPlayback() {
        if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
            return mediaPlayer.getTotalDuration().toSeconds();
        }
        return 0; // Default or fallback value
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
        double currentTimeInSeconds = getCurrentSecondsInPlayback();
        switch (clipType) {
            case Large_Ship_Destroyed:
            case Alien_Bomb_Impact:
                return currentTimeInSeconds > 1.2;
            case Destroyed_Explosion:
                return currentTimeInSeconds > 0.5;
            default:
                return false;
        }
    }

    public void stopClip() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop the MediaPlayer

        }
    }


    public void startClip() {
        adjustVolume();
        mediaPlayer.play();

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
    private void adjustVolume() {
        if (mediaPlayer != null) {
            double volume = switch (clipType) {
                case Player_Laserbeam -> 0.65 * 0.7f;
                case Large_Ship_Destroyed -> 0.6 * 0.7f;
                case Alien_Spaceship_Destroyed -> 0.4 * 0.7f;
                case Alien_Bomb_Impact -> 0.15 * 0.7f;
                case Default_EMP -> 0.07f * 0.7f;
                case NotEnoughMinerals -> 0.6f * 0.7f;
                case GenericError -> 0.6f * 0.7f;
                case Furi_Wisdowm_Of_Rage -> 0.7f;
                case Blood_On_The_Dancefloor -> 0.7f;
                case LeanRockShred -> 0.7f;
                case nomad -> 0.7f;
                case keygen -> 0.7f;
                case MausoleumMash -> 0.7f;
                case Arisen -> 0.7f;
                default -> 0.7f;
            };

            mediaPlayer.setVolume(volume);
        }
    }


    public boolean isRunning() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public boolean isFinished() {
        if (isPaused) {
            return false;
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

    public AudioEnums getAudioType() {
        return this.clipType;
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
