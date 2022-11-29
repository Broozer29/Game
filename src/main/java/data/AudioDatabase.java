package data;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class AudioDatabase {

	private static AudioDatabase instance = new AudioDatabase();
	private AudioLoader audioLoader = AudioLoader.getInstance();
	private Clip FuriWisdomOfRage;
	private Clip FuriMyOnlyChance;
	private Clip FuriMakeThisRight;
	private Clip DefaultMusic;

	// Van alle clips een lijst met clips maken, dan vervolgens een clip teruggeven
	// die niet gebruikt wordt.
	// Zo kun je een maximum aantal clips afdwingen per soort zonder dat clips niet
	// kunnen afspelen.

	private int clipListSize = 10;
	private List<Clip> alienBombImpactList = new ArrayList<Clip>();
	private List<Clip> laserBeamClipList = new ArrayList<Clip>();
	private List<Clip> destroyedExplosionClipList = new ArrayList<Clip>();
	private List<Clip> alienSpaceshipDestroyedClipList = new ArrayList<Clip>();

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

	// Resets clips that have finished playing
	public void updateGameTick() {
		for (int i = 0; i < laserBeamClipList.size(); i++) {
			if (!laserBeamClipList.get(i).isRunning()) {
				laserBeamClipList.get(i).stop();
				laserBeamClipList.get(i).setFramePosition(400);
			}
		}

		for (int i = 0; i < destroyedExplosionClipList.size(); i++) {
			if (!destroyedExplosionClipList.get(i).isRunning()) {
				destroyedExplosionClipList.get(i).stop();
				destroyedExplosionClipList.get(i).setFramePosition(0);
			}
		}
		for (int i = 0; i < alienSpaceshipDestroyedClipList.size(); i++) {
			if (!alienSpaceshipDestroyedClipList.get(i).isRunning()) {
				alienSpaceshipDestroyedClipList.get(i).stop();
				alienSpaceshipDestroyedClipList.get(i).setFramePosition(0);
			}
		}
		for (int i = 0; i < alienBombImpactList.size(); i++) {
			if (!alienBombImpactList.get(i).isRunning()) {
				alienBombImpactList.get(i).stop();
				alienBombImpactList.get(i).setFramePosition(0);
			}
		}

	}

	private void initMusic() throws LineUnavailableException {
		DefaultMusic = audioLoader.getSoundfile("DefaultMusic");
		FuriWisdomOfRage = audioLoader.getSoundfile("Furi - Wisdom of rage");
		FuriMakeThisRight = audioLoader.getSoundfile("Furi - Make this right");
		FuriMyOnlyChance = audioLoader.getSoundfile("Furi - My only chance");
	}

	private void initSoundEffects() throws LineUnavailableException {
		for (int i = 0; i < clipListSize; i++) {
			Clip tempLaserbeamClip = audioLoader.getSoundfile("Player Laserbeam");
			laserBeamClipList.add(tempLaserbeamClip);
			Clip tempDestroyedExplosionClip = audioLoader.getSoundfile("Destroyed Explosion");
			destroyedExplosionClipList.add(tempDestroyedExplosionClip);
			Clip tempAlienSpaceshipExplosionClip = audioLoader.getSoundfile("Alien Spaceship Destroyed");
			alienSpaceshipDestroyedClipList.add(tempAlienSpaceshipExplosionClip);
			Clip tempAlienBombImpactClip = audioLoader.getSoundfile("Alien Bomb Impact");
			alienBombImpactList.add(tempAlienBombImpactClip);
		}

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
		for (int i = 0; i < laserBeamClipList.size(); i++) {
			if (!laserBeamClipList.get(i).isActive()) {
				return laserBeamClipList.get(i);
			}
		}
		return null;
	}

	public Clip getDestroyedExplosion() {
		for (int i = 0; i < destroyedExplosionClipList.size(); i++) {
			if (!destroyedExplosionClipList.get(i).isActive()) {
				return destroyedExplosionClipList.get(i);
			}
		}
		return null;
	}

	public Clip getDefaultAlienExplosion() {
		for (int i = 0; i < alienSpaceshipDestroyedClipList.size(); i++) {
			if (!alienSpaceshipDestroyedClipList.get(i).isActive()) {
				return alienSpaceshipDestroyedClipList.get(i);
			}
		}
		return null;
	}
	
	public Clip getAlienBombImpact() {
		for (int i = 0; i < alienBombImpactList.size(); i++) {
			if (!alienBombImpactList.get(i).isActive()) {
				return alienBombImpactList.get(i);
			}
		}
		return null;
	}

}
