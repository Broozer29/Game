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

	public Clip getSoundfile(AudioEnums audioFile) throws LineUnavailableException {
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

	private String convertAudioToFileString(AudioEnums audioFile) {
		switch (audioFile) {
		case Player_Laserbeam:
			return "src/resources/audio/laserbeam1.wav";
		case Destroyed_Explosion:
			return "src/resources/audio/Destroyed Explosion.wav";
		case Alien_Spaceship_Destroyed:
			return "src/resources/audio/Alien Spaceship Destroyed.wav";
		case Furi_Make_This_Right:
			return "src/resources/audio/music/Furi - Make this right.wav";
		case Furi_Wisdowm_Of_Rage:
			return "src/resources/audio/music/Furi - Wisdom of rage.wav";
		case Furi_My_Only_Chance:
			return "src/resources/audio/music/Furi - My only chance.wav";
		case Ayasa_The_Reason_Why:
			return "src/resources/audio/music/Ayasa - The reason why.wav";
		case Apple_Holder_Remix:
			return "src/resources/audio/music/defaultmusic.wav";
		case Alien_Bomb_Impact:
			return "src/resources/audio/Alien Bomb Impact.wav";
		case Large_Ship_Destroyed:
			return "src/resources/audio/Large Ship Destroyed.wav";
		case Default_EMP:
			return "src/resources/audio/Default EMP.wav";
		case Alien_Bomb_Destroyed:
			// DUPLICATE
			return "src/resources/audio/Destroyed Explosion.wav";
		case Power_Up_Acquired:
			return "src/resources/audio/PowerUpAcquired.wav";
		}
		return null;
	}
}
