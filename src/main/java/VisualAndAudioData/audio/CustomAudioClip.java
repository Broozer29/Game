package VisualAndAudioData.audio;

import VisualAndAudioData.audio.enums.AudioEnums;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomAudioClip {

    private AudioLoader audioLoader = AudioLoader.getInstance();
//    private static final ExecutorService audioExecutor = Executors.newCachedThreadPool();
    private Clip clip;
    private AudioEnums clipType;
    private boolean loop;

    public CustomAudioClip (AudioEnums clipType, boolean loop) {
        this.clipType = clipType;
        this.loop = loop;
        initClip();
    }

    private void initClip () {
        try {
            clip = audioLoader.getSoundfile(clipType);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public long getFramePosition () {
        return this.clip.getFramePosition();
    }

    public boolean aboveThreshold () {
        switch (clipType) {
            case Large_Ship_Destroyed, Alien_Bomb_Impact:
                if (clip.getFramePosition() > 25000) {
                    return true;
                } else
                    return false;

            case Destroyed_Explosion:
                if (clip.getFramePosition() > 12000) {
                    return true;
                } else
                    return false;
        }
        return true;
    }

    public void stopClip () {
        this.clip.stop();
    }

    public void startClip () {
        adjustVolume();
        this.clip.start();
        if (loop) {
            this.clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void muteAudioClip(){
        if (this.clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-70);
        }
    }

    private void adjustVolume () {
        if (this.clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            switch (clipType) {
                case Player_Laserbeam:
                    volume.setValue(-9);
                    break;
                case Large_Ship_Destroyed:
                    volume.setValue(-4);
                    break;
                case Alien_Bomb_Impact:
                    volume.setValue(-2);
                    break;
                case Apple_Holder_Remix:
                    volume.setValue(-10);
                    break;
                case Furi_Wisdowm_Of_Rage:
                    volume.setValue(-8);
                    System.out.println("Set the music to very low volume in customaudioclip at 88");
//				volume.setValue(-50);
                    break;
                case Default_EMP:
                    volume.setValue(-15);
                    break;
                case NotEnoughMinerals:
                    volume.setValue(-4);
                    break;
            }
        } else {
            System.out.println("Master Gain control is not supported: " + clipType);
            // Handle the situation where Master Gain is not supported
        }
    }

    public int getFrameLength () {
        //If audio needs to be cut earlier, like the reset manager does, this needs to be refactored here.
        return clip.getFrameLength();
    }

    public Clip getClip () {
        return this.clip;
    }

    public boolean isRunning () {
        return this.clip.isRunning();
    }

    public boolean isActive () {
        return this.clip.isActive();
    }

    public void closeclip () {
        this.clip.setFramePosition(0);
        this.clip.close();
    }

    public boolean isLoop () {
        return loop;
    }

    public void setLoop (boolean loop) {
        this.loop = loop;
    }

    public void setFramePosition (int framePosition) {
        this.clip.setFramePosition(framePosition);
    }

    public AudioEnums getAudioType () {
        return this.clipType;
    }
}