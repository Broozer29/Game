package data;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class AudioDatabase {

	private static AudioDatabase instance = new AudioDatabase();
	private AudioLoader audioLoader = AudioLoader.getInstance();
	private Clip FuriWisdomOfRage;
	private Clip FuriMyOnlyChance;
	private Clip FuriMakeThisRight;
	private Clip DefaultMusic;
	//Van alle clips een lijst met clips maken, dan vervolgens een clip teruggeven die niet gebruikt wordt.
	//Zo kun je een maximum aantal clips afdwingen per soort zonder dat clips niet kunnen afspelen.

	private Clip laserBeam;
	private Clip destroyedExplosion;

	private AudioDatabase() {
		initializeAudiofiles();
	}

	public static AudioDatabase getInstance() {
		return instance;
	}

	private void initializeAudiofiles() {
		try {
			initMusic();
			initSoundEffects();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	private void initMusic() throws LineUnavailableException {
		DefaultMusic = audioLoader.getSoundfile("DefaultMusic");
		FuriWisdomOfRage = audioLoader.getSoundfile("Furi - Wisdom of rage");
		FuriMakeThisRight = audioLoader.getSoundfile("Furi - Make this right");
		FuriMyOnlyChance = audioLoader.getSoundfile("Furi - My only chance");
	}

	private void initSoundEffects() throws LineUnavailableException {
		laserBeam = audioLoader.getSoundfile("Player Laserbeam");
		destroyedExplosion = audioLoader.getSoundfile("Destroyed Explosion");
	}

	public Clip getFuriWisdomOfRage() {
		return FuriWisdomOfRage;
	}

	public Clip getFuriMyOnlyChance() {
		return FuriMyOnlyChance;
	}

	public Clip getFuriMakeThisRight() {
		return FuriMakeThisRight;
	}

	public Clip getDefaultMusic() {
		return DefaultMusic;
	}

	public Clip getLaserBeam() {
		if(laserBeam.getFramePosition() > (laserBeam.getFrameLength() / 10)) {
			laserBeam.stop();
			laserBeam.setFramePosition(400);
		}
		return laserBeam;
	}

	public Clip getDestroyedExplosion() {
		return destroyedExplosion;
	}

}
