package data.audio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

public class CustomAudioClip {

	AudioLoader audioLoader = AudioLoader.getInstance();

	Clip clip;
	AudioEnums clipType;
	boolean loop;

	public CustomAudioClip(AudioEnums clipType, boolean loop) {
		this.clipType = clipType;
		this.loop = loop;
		initClip();
	}

	private void initClip() {
		try {
			clip = audioLoader.getSoundfile(clipType);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		switch (clipType) {
		case Player_Laserbeam:
			volume.setValue(-4);
			break;
		case Large_Ship_Destroyed:
			volume.setValue(-2);
			break;
		case Alien_Bomb_Impact:
			volume.setValue(-2);
			break;
		case Apple_Holder_Remix:
			volume.setValue(-10);
			break;
		case Furi_Wisdowm_Of_Rage:
			volume.setValue(-5);
		case Default_EMP:
			volume.setValue(-15);
			break;
		}
	}

	public int getFramePosition() {
		return this.clip.getFramePosition();
	}

	public boolean aboveThreshold() {
		switch (clipType) {
		case Large_Ship_Destroyed:
			if (clip.getFramePosition() > 25000) {
				return true;
			} else
				return false;

		case Destroyed_Explosion:
			if (clip.getFramePosition() > 12000) {
				return true;
			} else
				return false;

		case Alien_Bomb_Impact:
			if (clip.getFramePosition() > 25000) {
				return true;
			} else
				return false;
		}
		return true;
	}

	public void stopClip() {
		this.clip.stop();
	}

	public void startClip() {
		this.clip.start();
		if (loop) {
			this.clip.loop(Clip.LOOP_CONTINUOUSLY);
		}

	}

	public void resetFramePosition() {
		this.clip.setFramePosition(0);
	}

	public boolean isRunning() {
		return this.clip.isRunning();
	}

	public boolean isActive() {
		return this.clip.isActive();
	}

	public void closeclip() {
		this.clip.close();
	}

	public void setFramePosition(int framePosition) {
		this.clip.setFramePosition(framePosition);
	}

	public AudioEnums getAudioType() {
		return this.clipType;
	}
}