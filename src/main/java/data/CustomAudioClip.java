package data;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

public class CustomAudioClip {

	AudioLoader audioLoader = AudioLoader.getInstance();

	Clip clip;
	String clipType;
	boolean loop;

	public CustomAudioClip(String clipType, boolean loop) {
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
		case ("Player Laserbeam"):
			volume.setValue(-6);
			break;
		case ("Destroyed Explosion"):
			volume.setValue(-2);
			break;
		case ("Alien Spaceship Destroyed"):
			break;
		case ("Alien Bomb Impact"):
			volume.setValue(-2);
		}
	}

	public int getFramePosition() {
		return this.clip.getFramePosition();
	}
	
	public boolean aboveThreshold() {
		switch(clipType) {
		case("Large Ship Destroyed"):
			if(clip.getFramePosition() > 25000) {
				return true;
			} else return false;
	
		} 
		return true;
	}

	public void stopClip() {
		this.clip.stop();
	}

	public void startClip() {
		this.clip.start();
		if(loop) {
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
		return this.clip.isRunning();
	}

	public void closeclip() {
		this.clip.close();
	}

	public void setFramePosition(int framePosition) {
		this.clip.setFramePosition(framePosition);
	}
	
	public String getAudioType() {
		return this.clipType;
	}
}
