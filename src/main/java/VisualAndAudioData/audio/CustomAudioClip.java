package VisualAndAudioData.audio;

import VisualAndAudioData.audio.enums.AudioEnums;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomAudioClip {

    private AudioLoader audioLoader = AudioLoader.getInstance();
    private Clip clip;
    private AudioEnums clipType;
    private boolean loop;
    private boolean isStream = false; // Flag to indicate if this is a stream
    private SourceDataLine audioLine; // For streaming audio
    private AudioInputStream audioStream; // For tracking stream info
    private static final ExecutorService executor = Executors.newFixedThreadPool(4); // Limit to 4 threads
    private long totalBytesRead = 0;
    private long totalFramesInStream = 0;

    public CustomAudioClip(AudioEnums clipType, boolean loop) {
        this.clipType = clipType;
        this.loop = loop;
        this.isStream = clipType.shouldBeStreamed(); // Check if this should be a stream

        if (!isStream) {
            initClip();
        }
    }

    private void initClip() {
        if(!isStream) {
            try {
                clip = audioLoader.getSoundfile(clipType);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }


    public int getFrameLength() {
        if (isStream) {
            if (audioStream == null) {
                // If the stream is not ready, return -1 or another default value
                return 5000000;
            }
            // Return the total frames in the stream once initialized
            return (int) totalFramesInStream;
        }
        return clip.getFrameLength();
    }

    public long getFramePosition() {
        if (isStream) {
            if (audioStream == null || audioLine == null) {
                // If the stream is not ready yet, return 0 or another default value
                return 0;
            }
            // Convert total bytes read to frames once the stream is ready
            AudioFormat format = audioStream.getFormat();
            return totalBytesRead / format.getFrameSize();
        }
        return this.clip.getFramePosition();
    }


    public boolean aboveThreshold() {
        if (isStream) {
            return false; // Streams might not use this functionality
        }
        switch (clipType) {
            case Large_Ship_Destroyed, Alien_Bomb_Impact:
                return clip.getFramePosition() > 25000;
            case Destroyed_Explosion:
                return clip.getFramePosition() > 12000;
            default:
                return true;
        }
    }

    public void stopClip() {
        if (isStream) {
            if (audioLine != null && audioLine.isRunning()) {
                audioLine.stop();
                audioLine.close();
            }
        } else {
            this.clip.stop();
        }
    }

    public void startClip() {
        if (isStream) {
            playAudioStream(clipType); // Handle streaming
        } else {
            adjustVolume();
            this.clip.start();
            if (loop) {
                this.clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
    }

    public void muteAudioClip() {
        if (isStream) {
            // Mute logic for streaming, if necessary
            if (audioLine != null && audioLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-70);
            }
        } else if (this.clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-70);
        }
    }

    private void adjustVolume() {
        if (isStream) {
            if (audioLine != null && audioLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-10); // Set to desired value
            }
        } else if (this.clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            switch (clipType) {
                case Player_Laserbeam:
                    volume.setValue(-9);
                    break;
                case Large_Ship_Destroyed:
                    volume.setValue(-4);
                    break;
                case Alien_Bomb_Impact:
                    volume.setValue(-6);
                    break;
                case Apple_Holder_Remix:
                    volume.setValue(-6);
                    break;
                case Furi_Wisdowm_Of_Rage:
                    volume.setValue(-8);
                    break;
                case Default_EMP:
                    volume.setValue(-25);
                    break;
                case NotEnoughMinerals:
                    volume.setValue(-4);
                    break;
            }
        }
    }

    public void playAudioStream(AudioEnums clipType) {
        executor.submit(() -> {
            try {
                // Use getResourceAsStream to load the file from the classpath
                InputStream inputStream = getClass().getResourceAsStream(AudioLoader.getInstance().convertAudioToFileString(clipType));

                if (inputStream == null) {
                    throw new FileNotFoundException("Audio file not found: " + clipType);
                }

                audioStream = AudioSystem.getAudioInputStream(inputStream);
                AudioFormat format = audioStream.getFormat();

                totalFramesInStream = audioStream.getFrameLength(); // Set the total frames

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                audioLine = (SourceDataLine) AudioSystem.getLine(info);

                audioLine.open(format);
                audioLine.start();

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1) {
                    audioLine.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead; // Track the total bytes read
                }

                audioLine.drain();
                audioLine.close();
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
        return this.clip.isRunning();
    }

    public boolean isActive() {
        if (isStream) {
            return audioLine != null && audioLine.isActive();
        }
        return this.clip.isActive();
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
        } else {
            this.clip.setFramePosition(0);
            this.clip.close();
        }
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setFramePosition(int framePosition) {
        if (!isStream) {
            this.clip.setFramePosition(framePosition);
        }
    }

    public AudioEnums getAudioType() {
        return this.clipType;
    }
}
