package net.riezebos.bruus.tbd.visualsandaudio.data.audio;

import javafx.scene.media.MediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

import javax.sound.sampled.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomAudioClip {

    private AudioLoader audioLoader = AudioLoader.getInstance();
    private MediaPlayer mediaPlayer; // JavaFX MediaPlayer for in-memory audio
    private AudioEnums clipType;
    private boolean loop;
    private boolean isStream = false;
    private SourceDataLine audioLine; // For streaming audio
    private AudioInputStream audioStream; // For tracking stream info
    private static final ExecutorService executor = Executors.newFixedThreadPool(6); //Maximum 4 audio streams at a time for performance
    private long totalBytesRead = 0;
    private long totalFramesInStream = 0;
    private AudioFormat streamFormat; // Store the audio format for streams
    private double pausedPosition = 0; // Position to remember on pause
    private InputStream inputStream; //InputStream required to start playback from a specific point

    private boolean isMediaPlayerPlaying = false; // Flag to track if MediaPlayer is playing
    private boolean isMediaPlayerFinished = false; // Flag to track if MediaPlayer has finished
    private boolean isPaused = false; // Flag to track paused state

    public CustomAudioClip (AudioEnums clipType) {
        this.clipType = clipType;
        this.loop = false;
        this.isStream = clipType.shouldBeStreamed();

        if (!isStream) {
            initMediaPlayer();
        }
    }

    private void initMediaPlayer () {
        if (!isStream) {
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
                e.printStackTrace();
            }
        }
    }

    public double getTotalSecondsInPlayback () {
        if (isStream) {
            // For streams, approximate length based on frames and sample rate
            if (streamFormat != null && totalFramesInStream > 0) {
                double frameRate = streamFormat.getFrameRate();
                return totalFramesInStream / frameRate;
            }

            return -1; // Return -1 if we cannot determine the length
        } else if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
            return mediaPlayer.getTotalDuration().toSeconds();
        }
        return 0; // Default or fallback value
    }

    // Get the current playback position in seconds (for MediaPlayer and streams)
    public double getCurrentSecondsInPlayback () {
        if (isStream) {
            if (audioStream == null || audioLine == null) {
                return 0; // Stream not ready
            }
            // Approximate time based on bytes read and sample rate
            double frameSize = streamFormat.getFrameSize();
            double frameRate = streamFormat.getFrameRate();
            return (totalBytesRead / frameSize) / frameRate;
        } else if (mediaPlayer != null && mediaPlayer.getCurrentTime() != null && isMediaPlayerPlaying) {
            return mediaPlayer.getCurrentTime().toSeconds();
        }
        return 0; // Return 0 if the media hasn't started playing yet
    }


    // Set playback position using seconds (for both MediaPlayer and streams)
    public void setPlaybackPosition (double seconds) {
        if (isStream) {
            if (audioLine != null && audioStream != null) {
                try {
                    if (audioStream.markSupported()) {
                        // Reset the stream to the specified position (approximate)
                        long bytePosition = (long) (seconds * streamFormat.getFrameRate() * streamFormat.getFrameSize());
                        audioStream.reset(); // Reset the stream
                        bytePosition = Math.max(bytePosition, 0);
                        audioStream.skip(bytePosition); // Skip to the target position
                        totalBytesRead = bytePosition; // Update the totalBytesRead counter
//                    audioLine.start();
                    }
                } catch (IOException e) {
                    System.out.println("Error: Tried to reset an Audio Stream that was already closed/reset: " + this.getAudioType());
                }
            }
        } else if (mediaPlayer != null) {
            mediaPlayer.seek(javafx.util.Duration.seconds(seconds));
            mediaPlayer.stop();
        }
    }

    private boolean aboveThreshold () {
        if (isStream) {
            return false;
        }
        double currentTimeInSeconds = getCurrentSecondsInPlayback();
        switch (clipType) {
            case Large_Ship_Destroyed:
            case Alien_Bomb_Impact:
                return currentTimeInSeconds > 1.2;
            case Destroyed_Explosion:
                return currentTimeInSeconds > 0.5;
//            case ChargingLaserbeam:
//                return currentTimeInSeconds > 3;
            default:
                return false;
        }
    }

    public void stopClip () {
        if (isStream) {
            if (audioLine != null) {
                audioLine.stop(); // Immediately stop playback
                audioLine.flush(); // Clear the buffer to ensure no residual audio
            }
            try {
                if (audioStream != null) {
                    audioStream.close(); // Close the stream to release resources
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.stop(); // Stop the MediaPlayer
            }
        }
    }


    public void startClip () {
        if (isStream) {
            // Stop any current playback if the audio line is active
            if (audioLine != null && audioLine.isRunning()) {
                audioLine.stop();
                audioLine.flush();
                audioLine.close();
            }

            // Close any existing stream to release resources
            if (audioStream != null) {
                try {
                    audioStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Start a new stream for playback
            playAudioStream(clipType); // Handle streaming
        } else if (mediaPlayer != null) {
            adjustVolume();
            mediaPlayer.play();
            if (loop) {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            }
        }
    }

    // Pause the playback and let the current thread finish
    public synchronized void pauseClip() {
        if (isPaused) return; // Avoid double pause
        isPaused = true;

        if (isStream) {
            if (audioLine != null && audioLine.isRunning()) {
                pausedPosition = getCurrentSecondsInPlayback(); // Save current position
                audioLine.stop(); // Stop audio line
                audioLine.flush();
                audioLine.close();
            }

            // Close the stream to allow the current thread to finish
            try {
                if (audioStream != null) {
                    audioStream.close();
                }
            } catch (IOException e) {
                System.out.println("Audio stream could not be closed, it probably already was");
            }
        } else if (mediaPlayer != null && isMediaPlayerPlaying) {
            pausedPosition = mediaPlayer.getCurrentTime().toSeconds();
            mediaPlayer.pause(); // Pause the MediaPlayer
        }
    }

    // Resume playback for both MediaPlayer and streamed audio
    public synchronized void resumeClip() {
        if (!isPaused) return; // Only resume if paused
        isPaused = false;

        if (isStream) {
            startStreamFromPosition(pausedPosition); // Restart from paused position
        } else if (mediaPlayer != null && isMediaPlayerPlaying) { //Only resume if the clip was actually playing
            mediaPlayer.seek(javafx.util.Duration.seconds(pausedPosition));
            mediaPlayer.play(); // Resume MediaPlayer playback
        }
    }


    public void muteAudioClip () {
        if (isStream) {
            if (audioLine != null && audioLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-70);
            }
        } else if (mediaPlayer != null) {
            mediaPlayer.setMute(true);
        }
    }

    private void adjustVolume () {
        if (isStream) {
            if (audioLine != null && audioLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-60);
            }
        } else if (mediaPlayer != null) {
            double volume = switch (clipType) {
                case Player_Laserbeam -> 0.65;
                case SpecialAttackFinishedCharging -> 2.2f;
                case Large_Ship_Destroyed -> 0.8;
                case Alien_Spaceship_Destroyed -> 0.8;
                case Alien_Bomb_Impact -> 0.5;
                case Apple_Holder_Remix -> 0.5;
                case Furi_Wisdowm_Of_Rage -> 0.4;
                case Default_EMP -> 0.1;
                case NotEnoughMinerals -> 0.6;
                default -> 1.0;
            };

            mediaPlayer.setVolume(volume);
        }
    }

    // Method to start the stream from a specific position
    // Method to start the stream from a specific position
    private void startStreamFromPosition(double startPosition) {
        executor.submit(() -> {
            try {
                // Open a new input stream from the audio file
                inputStream = getClass().getResourceAsStream(AudioLoader.getInstance().convertAudioToFileString(clipType));
                if (inputStream == null) throw new FileNotFoundException("Audio file not found: " + clipType);

                // Get a fresh AudioInputStream for the audio file
                audioStream = AudioSystem.getAudioInputStream(inputStream);
                streamFormat = audioStream.getFormat();
                totalFramesInStream = audioStream.getFrameLength();
                totalBytesRead = 0;

                // Prepare the audio line
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, streamFormat);
                audioLine = (SourceDataLine) AudioSystem.getLine(info);
                audioLine.open(streamFormat);
                audioLine.start();

                // Set the volume
                if (audioLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl volume = (FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
                    volume.setValue(AudioManager.getInstance().devTestmuteMode ? -70 : -10);
                }

                // Skip to the start position
                long bytesToSkip = (long) (startPosition * streamFormat.getFrameRate() * streamFormat.getFrameSize());
                byte[] skipBuffer = new byte[4096];
                while (bytesToSkip > 0) {
                    int toRead = (int) Math.min(skipBuffer.length, bytesToSkip);
                    int bytesRead = audioStream.read(skipBuffer, 0, toRead);
                    if (bytesRead == -1) break;
                    bytesToSkip -= bytesRead;
                    totalBytesRead += bytesRead;
                }

                // Play audio from the target position
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1) {
                    audioLine.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                }

                audioLine.drain();
                audioStream.close();

            } catch (UnsupportedAudioFileException | LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e){
                System.out.println("Could not close or read the stream because: " + e.getMessage());
            }
        });
    }


    // Main method to start streaming from the beginning
    public void playAudioStream(AudioEnums clipType) {
        pausedPosition = 0; // Start from the beginning
        startStreamFromPosition(pausedPosition);
    }

    public boolean isRunning () {
        if (isStream) {
            return audioLine != null && audioLine.isRunning();
        }
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public boolean isFinished () {
        if(isPaused){
            return false;
        }

        if (isStream) {
            return getCurrentSecondsInPlayback() >= getTotalSecondsInPlayback();
        }

        // Use the finished flag for MediaPlayer
        if (isMediaPlayerFinished) {
            return true;
        }

        //For short duration sound effects
        if (aboveThreshold()) {
            return true;
        }

        // Tolerance buffer to avoid premature termination
        double tolerance = 0.05;
        double currentTime = getCurrentSecondsInPlayback();
        double clipLength = getTotalSecondsInPlayback();

        return (currentTime >= (clipLength - tolerance));
    }



    public boolean isLoop () {
        return loop;
    }

    public void setLoop (boolean loop) {
        this.loop = loop;
        if (mediaPlayer != null) {
            mediaPlayer.setCycleCount(loop ? MediaPlayer.INDEFINITE : 1);
        }
    }

    public AudioEnums getAudioType () {
        return this.clipType;
    }
}
