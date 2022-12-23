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
	private List<Clip> allActiveClips = new ArrayList<Clip>();
	private List<Clip> alienBombImpactList = new ArrayList<Clip>();
	private List<Clip> laserBeamClipList = new ArrayList<Clip>();
	private List<Clip> destroyedExplosionClipList = new ArrayList<Clip>();
	private List<Clip> alienSpaceshipDestroyedClipList = new ArrayList<Clip>();
	private List<Clip> largeShipDestroyed = new ArrayList<Clip>();

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
	public void resetFinishedClips() {
		for (int i = 0; i < allActiveClips.size(); i++) {
			if (!allActiveClips.get(i).isRunning()) {
				allActiveClips.get(i).stop();
				if (laserBeamClipList.contains(allActiveClips.get(i))) {
					allActiveClips.get(i).setFramePosition(400);
				} else {
					allActiveClips.get(i).setFramePosition(0);
				}
				allActiveClips.remove(allActiveClips.get(i));
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
			Clip tempLargeShipDestroyed = audioLoader.getSoundfile("Large Ship Destroyed");
			largeShipDestroyed.add(tempLargeShipDestroyed);
		}
	}

	public Clip getAudioClip(String audioType) {
		switch (audioType) {
		case ("Furi - Wisdom of rage"):
			return getFuriWisdomOfRage();
		case ("Furi - My only chance"):
			return getFuriMyOnlyChance();
		case ("Furi - Make this right"):
			return getFuriMakeThisRight();
		case ("getDefaultMusic"):
			return getDefaultMusic();
		case ("Player Laserbeam"):
			return getLaserBeam();
		case ("Destroyed Explosion"):
			return getDestroyedExplosion();
		case ("Alien Spaceship Destroyed"):
			return getDefaultAlienExplosion();
		case ("Alien Bomb Impact"):
			return getAlienBombImpact();
		case ("Large Ship Destroyed"):
			return getLargeShipDestroyed();
		}
		return null;
	}

	private Clip getFuriWisdomOfRage() {
		return FuriWisdomOfRage;
	}

	private Clip getFuriMyOnlyChance() {
		return FuriMyOnlyChance;
	}

	private Clip getFuriMakeThisRight() {
		return FuriMakeThisRight;
	}

	private Clip getDefaultMusic() {
		return DefaultMusic;
	}

	private Clip getLaserBeam() {
		for (int i = 0; i < laserBeamClipList.size(); i++) {
			if (!laserBeamClipList.get(i).isActive()) {
				allActiveClips.add(laserBeamClipList.get(i));
				return laserBeamClipList.get(i);
			}
		}
		return null;
	}

	private Clip getDestroyedExplosion() {
		for (int i = 0; i < destroyedExplosionClipList.size(); i++) {
			if (!destroyedExplosionClipList.get(i).isActive()) {
				allActiveClips.add(destroyedExplosionClipList.get(i));
				return destroyedExplosionClipList.get(i);
			}
		}
		return null;
	}

	private Clip getDefaultAlienExplosion() {
		for (int i = 0; i < alienSpaceshipDestroyedClipList.size(); i++) {
			if (!alienSpaceshipDestroyedClipList.get(i).isActive()) {
				allActiveClips.add(alienSpaceshipDestroyedClipList.get(i));
				return alienSpaceshipDestroyedClipList.get(i);
			}
		}
		return null;
	}

	private Clip getAlienBombImpact() {
		for (int i = 0; i < alienBombImpactList.size(); i++) {
			if (!alienBombImpactList.get(i).isActive()) {
				allActiveClips.add(alienBombImpactList.get(i));
				return alienBombImpactList.get(i);
			}
		}
		return null;
	}

	private Clip getLargeShipDestroyed() {
		for (int i = 0; i < largeShipDestroyed.size(); i++) {
			if (!largeShipDestroyed.get(i).isActive()) {
				allActiveClips.add(largeShipDestroyed.get(i));
				return largeShipDestroyed.get(i);
			}
		}
		return null;
	}

}
