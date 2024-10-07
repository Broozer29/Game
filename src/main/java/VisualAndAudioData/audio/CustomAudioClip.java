package VisualAndAudioData.audio;

import VisualAndAudioData.audio.enums.AudioEnums;
import javafx.scene.media.MediaPlayer;
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
    private boolean isStream = false; // Flag to indicate if this is a stream
    private SourceDataLine audioLine; // For streaming audio
    private AudioInputStream audioStream; // For tracking stream info
    private static final ExecutorService executor = Executors.newFixedThreadPool(4); // Limit to 4 threads
    private long totalBytesRead = 0;
    private long totalFramesInStream = 0;
    private AudioFormat streamFormat; // Store the audio format for streams

    public CustomAudioClip(AudioEnums clipType, boolean loop) {
        this.clipType = clipType;
        this.loop = loop;
        this.isStream = clipType.shouldBeStreamed(); // Check if this should be a stream

        if (!isStream) {
            initMediaPlayer();
        }
    }

    private boolean isMediaPlayerPlaying = false; // Flag to track if media is playing
    private boolean isMediaPlayerFinished = false; // Flag to track if media has finished

    private void initMediaPlayer() {
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

    // Get the total clip length in seconds (for MediaPlayer and streams)
    public double getClipLengthInSeconds() {
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
    public double getCurrentTimeInSeconds() {
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
    public void setPlaybackPosition(double seconds) {
        if (isStream) {
            if (audioLine != null && audioStream != null) {
                try {
                    // Reset the stream to the specified position (approximate)
                    long bytePosition = (long) (seconds * streamFormat.getFrameRate() * streamFormat.getFrameSize());
                    audioStream.reset(); // Reset the stream
                    bytePosition = Math.max(bytePosition, 0);
                    audioStream.skip(bytePosition); // Skip to the target position
                    totalBytesRead = bytePosition; // Update the totalBytesRead counter
//                    audioLine.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (mediaPlayer != null) {
            mediaPlayer.seek(javafx.util.Duration.seconds(seconds));
            mediaPlayer.stop();
        }
    }

    public boolean aboveThreshold() {
        if (isStream) {
            return false; // Streams might not use this functionality
        }
        double currentTimeInSeconds = getCurrentTimeInSeconds();
        switch (clipType) {
            case Large_Ship_Destroyed:
            case Alien_Bomb_Impact:
                return currentTimeInSeconds > 1; // Equivalent of 25000 frames
            case Destroyed_Explosion:
                return currentTimeInSeconds > 0.5; // Equivalent of 12000 frames
            default:
                return false;
        }
    }

    public void stopClip() {
        if (isStream) {
            if (audioLine != null) {
                audioLine.stop();
            }
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        }
    }

    public void startClip() {
        if (isStream) {
            playAudioStream(clipType); // Handle streaming
        } else if (mediaPlayer != null) {
            adjustVolume();
            mediaPlayer.play();
            if (loop) {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            }
        }
    }

    public void muteAudioClip() {
        if (isStream) {
            if (audioLine != null && audioLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-70);
            }
        } else if (mediaPlayer != null) {
            mediaPlayer.setMute(true);
        }
    }

    private void adjustVolume() {
        if (isStream) {
            if (audioLine != null && audioLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-10); // Set to desired value
            }
        } else if (mediaPlayer != null) {
            double volume = switch (clipType) {
                case Player_Laserbeam -> 0.25;
                case Large_Ship_Destroyed -> 0.6;
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

    public void playAudioStream(AudioEnums clipType) {
        executor.submit(() -> {
            try {
                InputStream inputStream = getClass().getResourceAsStream(AudioLoader.getInstance().convertAudioToFileString(clipType));

                if (inputStream == null) {
                    throw new FileNotFoundException("Audio file not found: " + clipType);
                }

                audioStream = AudioSystem.getAudioInputStream(inputStream);
                streamFormat = audioStream.getFormat(); // Store the format for later use
                totalFramesInStream = audioStream.getFrameLength(); // Set the total frames

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, streamFormat);
                audioLine = (SourceDataLine) AudioSystem.getLine(info);
                audioLine.open(streamFormat);
                audioLine.start();

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1) {
                    audioLine.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead; // Track the total bytes read
                }

                audioLine.drain();
//                audioLine.close();
                audioStream.close();

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean isRunning() {
        if (isStream) {
            return audioLine != null && audioLine.isRunning();
        }
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public boolean isFinished() {
        if (isStream) {
            return audioLine != null && !audioLine.isActive(); // Streams work fine
        }

        // Use the finished flag for MediaPlayer
        if (isMediaPlayerFinished) {
            return true;
        }

        // Tolerance buffer to avoid premature termination (small value like 0.05 seconds)
        double tolerance = 0.05;
        double currentTime = getCurrentTimeInSeconds();
        double clipLength = getClipLengthInSeconds();

        return (currentTime >= (clipLength - tolerance));
    }


    public void closeclip() {
        if (isStream) {
            if (audioLine != null) {
                audioLine.close();
            }
            try {
                if (audioStream != null) {
                    audioStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
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
}
