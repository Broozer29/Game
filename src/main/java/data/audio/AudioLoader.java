package data.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioLoader {

	private static AudioLoader instance = new AudioLoader();

	private AudioLoader() {

	}

	public static AudioLoader getInstance() {
		return instance;
	}

	public Clip getSoundfile(String audioFile) throws LineUnavailableException {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(convertAudioToFileString(audioFile)).getAbsoluteFile());
			clip.open(audioInputStream);
			clip.setFramePosition(0);
			return clip;

		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String convertAudioToFileString(String audioFile) {
		switch (audioFile) {
		case ("Player Laserbeam"):
			return "src/resources/audio/laserbeam1.wav";
		case ("Destroyed Explosion"):
			return "src/resources/audio/Destroyed Explosion.wav";
		case ("Alien Spaceship Destroyed"):
			return "src/resources/audio/Alien Spaceship Destroyed.wav";
		case ("Furi - Make this right"):
			return "src/resources/audio/music/Furi - Make this right.wav";
		case ("Furi - Wisdom of rage"):
			return "src/resources/audio/music/Furi - Wisdom of rage.wav";
		case ("Furi - My only chance"):
			return "src/resources/audio/music/Furi - My only chance.wav";
		case ("Ayasa - The reason why"):
			return "src/resources/audio/music/Ayasa - The reason why.wav";
		case ("DefaultMusic"):
			return "src/resources/audio/music/defaultmusic.wav";
		case("Alien Bomb Impact"):
			return "src/resources/audio/Alien Bomb Impact.wav";
		case("Large Ship Destroyed"):
			return "src/resources/audio/Large Ship Destroyed.wav";
		case("Default EMP"):
			return "src/resources/audio/Default EMP.wav";
		}
		return null;
	}
}
