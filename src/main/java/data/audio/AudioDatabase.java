package data.audio;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class AudioDatabase {

	private static AudioDatabase instance = new AudioDatabase();
	private AudioLoader audioLoader = AudioLoader.getInstance();
	private CustomAudioClip FuriWisdomOfRage;
	private CustomAudioClip FuriMyOnlyChance;
	private CustomAudioClip FuriMakeThisRight;
	private CustomAudioClip DefaultMusic;

	// Van alle clips een lijst met clips maken, dan vervolgens een clip teruggeven
	// die niet gebruikt wordt.
	// Zo kun je een maximum aantal clips afdwingen per soort zonder dat clips niet
	// kunnen afspelen.

	private int clipListSize = 15;
	private List<CustomAudioClip> allActiveClips = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> alienBombImpactList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> laserBeamClipList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> destroyedExplosionClipList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> alienSpaceshipDestroyedClipList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> largeShipDestroyed = new ArrayList<CustomAudioClip>();

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

	public void updateGameTick() {
		testResetClips();
	}

	private void testResetClips() {
		for (int i = 0; i < allActiveClips.size(); i++) {
			if (allActiveClips.get(i).getAudioType().equals("Large Ship Destroyed")
					|| allActiveClips.get(i).getAudioType().equals("Destroyed Explosion")
					|| allActiveClips.get(i).getAudioType().equals("Alien Bomb Impact")) {
				if (allActiveClips.get(i).aboveThreshold()) {
					allActiveClips.get(i).setFramePosition(0);
					allActiveClips.get(i).stopClip();
					allActiveClips.remove(allActiveClips.get(i));
				}
			}

			else if (!allActiveClips.get(i).isActive()) {
				allActiveClips.get(i).stopClip();
				allActiveClips.get(i).setFramePosition(0);
				allActiveClips.remove(allActiveClips.get(i));
			}

		}
	}

	private void initMusic() throws LineUnavailableException {
		DefaultMusic = new CustomAudioClip("DefaultMusic", true);
		FuriWisdomOfRage = new CustomAudioClip("Furi - Wisdom of rage", true);
		FuriMakeThisRight = new CustomAudioClip("Furi - Make this right", true);
		FuriMyOnlyChance = new CustomAudioClip("Furi - My only chance", true);

	}

	private void initSoundEffects() throws LineUnavailableException {
		for (int i = 0; i < clipListSize; i++) {
//			Clip tempLaserbeamClip = audioLoader.getSoundfile("Player Laserbeam");
			CustomAudioClip laserBeamClip = new CustomAudioClip("Player Laserbeam", false);
			laserBeamClipList.add(laserBeamClip);

			CustomAudioClip destroyedExplosion = new CustomAudioClip("Destroyed Explosion", false);
			destroyedExplosionClipList.add(destroyedExplosion);

			CustomAudioClip alienSpaceshipDestroyed = new CustomAudioClip("Alien Spaceship Destroyed", false);
			alienSpaceshipDestroyedClipList.add(alienSpaceshipDestroyed);

			CustomAudioClip alienBombImpact = new CustomAudioClip("Alien Bomb Impact", false);
			alienBombImpactList.add(alienBombImpact);

			CustomAudioClip largeShipDestroyedClip = new CustomAudioClip("Large Ship Destroyed", false);
			largeShipDestroyed.add(largeShipDestroyedClip);
		}

	}

	public CustomAudioClip getAudioClip(String audioType) {
		switch (audioType) {
		case ("Furi - Wisdom of rage"):
			return getFuriWisdomOfRage();
		case ("Furi - My only chance"):
			return getFuriMyOnlyChance();
		case ("Furi - Make this right"):
			return getFuriMakeThisRight();
		case ("DefaultMusic"):
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

	private CustomAudioClip getFuriWisdomOfRage() {
		return FuriWisdomOfRage;
	}

	private CustomAudioClip getFuriMyOnlyChance() {
		return FuriMyOnlyChance;
	}

	private CustomAudioClip getFuriMakeThisRight() {
		return FuriMakeThisRight;
	}

	private CustomAudioClip getDefaultMusic() {
		return DefaultMusic;
	}

	private CustomAudioClip getLaserBeam() {
		for (int i = 0; i < laserBeamClipList.size(); i++) {
			if (!laserBeamClipList.get(i).isActive()) {
				allActiveClips.add(laserBeamClipList.get(i));
				return laserBeamClipList.get(i);
			}
		}
		return null;
	}

	private CustomAudioClip getDestroyedExplosion() {
		for (int i = 0; i < destroyedExplosionClipList.size(); i++) {
			if (!destroyedExplosionClipList.get(i).isRunning()) {
				allActiveClips.add(destroyedExplosionClipList.get(i));
				return destroyedExplosionClipList.get(i);
			}
		}
		return null;
	}

	private CustomAudioClip getDefaultAlienExplosion() {
		for (int i = 0; i < alienSpaceshipDestroyedClipList.size(); i++) {
			if (!alienSpaceshipDestroyedClipList.get(i).isRunning()) {
				allActiveClips.add(alienSpaceshipDestroyedClipList.get(i));
				return alienSpaceshipDestroyedClipList.get(i);
			}
		}
		return null;
	}

	private CustomAudioClip getAlienBombImpact() {
		for (int i = 0; i < alienBombImpactList.size(); i++) {
			if (!alienBombImpactList.get(i).isRunning()) {
				allActiveClips.add(alienBombImpactList.get(i));
				return alienBombImpactList.get(i);
			}
		}
		return null;
	}

	private CustomAudioClip getLargeShipDestroyed() {
		for (int i = 0; i < largeShipDestroyed.size(); i++) {
//			System.out.println("Clip ID: " + laserBeamClipList.get(i) + " Active: "
//					+ laserBeamClipList.get(i).isActive() + " Running: " + laserBeamClipList.get(i).isRunning()
//					+ " Frame position: " + laserBeamClipList.get(i).getFramePosition());
			if (!largeShipDestroyed.get(i).isRunning()) {
				allActiveClips.add(largeShipDestroyed.get(i));
				return largeShipDestroyed.get(i);
			}
		}
		return null;
	}

}
