package gameManagers;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {

	private static AudioManager instance = new AudioManager();
	private AudioInputStream defaultPlayerLaserBeamAudioStream = null;

	private AudioManager() {

	}

	public static AudioManager getInstance() {
		return instance;
	}

	public void addAudioToPlayList(String audioType) throws UnsupportedAudioFileException, IOException {
		playAudio(getAudioStream(audioType));
	}

	private AudioInputStream getAudioStream(String audioType) throws UnsupportedAudioFileException, IOException {
		switch (audioType) {
		case ("DefaultPlayerLaserbeam"):
			if (defaultPlayerLaserBeamAudioStream == null) {
				defaultPlayerLaserBeamAudioStream = AudioSystem.getAudioInputStream(new File("src/resources/audio/Earth5.wav").getAbsoluteFile());
			}
			return defaultPlayerLaserBeamAudioStream;
		}
		return null;
	}

	private void playAudio(AudioInputStream audioInputStream) throws UnsupportedAudioFileException, IOException {
		String soundName = "src/resources/audio/Earth5.wav";
		Clip clip;
		AudioInputStream defaultPlayerLaserBeamAudioStream = AudioSystem.getAudioInputStream(new File("src/resources/audio/Earth5.wav").getAbsoluteFile());
		try {
			clip = AudioSystem.getClip();
			clip.open(defaultPlayerLaserBeamAudioStream);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

}
