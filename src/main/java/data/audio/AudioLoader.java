package data.audio;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
//		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		try {
			Clip clip = AudioSystem.getClip();
			URL url = getClass().getResource(convertAudioToFileString(audioFile));
			System.out.println("URL for " + audioFile + ": " + url);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			clip.open(audioInputStream);
			clip.setFramePosition(0);
			System.out.println(clip.getFrameLength());
			return clip;

		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	private String convertAudioToFileString(AudioEnums audioFile) {
		switch (audioFile) {
		case Player_Laserbeam:
			return "/audio/laserbeam1.wav";
		case Destroyed_Explosion:
			return "/audio/Destroyed Explosion.wav";
		case Alien_Spaceship_Destroyed:
			return "/audio/Alien Spaceship Destroyed.wav";
		case Furi_Make_This_Right:
			return "/audio/music/Furi - Make this right.wav";
		case Furi_Wisdowm_Of_Rage:
			return "/audio/music/Furi - Wisdom of rage.wav";
		case Furi_My_Only_Chance:
			return "/audio/music/Furi - My only chance.wav";
		case Ayasa_The_Reason_Why:
			return "/audio/music/Ayasa - The reason why.wav";
		case Apple_Holder_Remix:
			return "/audio/music/defaultmusic.wav";
		case Alien_Bomb_Impact:
			return "/audio/Alien Bomb Impact.wav";
		case Large_Ship_Destroyed:
			return "/audio/Large Ship Destroyed.wav";
		case Default_EMP:
			return "/audio/Default EMP.wav";
		case Alien_Bomb_Destroyed:
			// DUPLICATE
			return "/audio/Destroyed Explosion.wav";
		case Power_Up_Acquired:
			return "/audio/PowerUpAcquired.wav";
		case Rocket_Launcher:
			return "/audio/Rocket Launcher.wav";
		case Flamethrower: 
			return "/audio/Flamethrower.wav";
		case Firewall:
			return "/audio/Firewall.wav";
		}
		return null;
	}
}
