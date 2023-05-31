package data.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.LineUnavailableException;

public class AudioDatabase {

	private static AudioDatabase instance = new AudioDatabase();
	private CustomAudioClip FuriWisdomOfRage;
	private CustomAudioClip FuriMyOnlyChance;
	private CustomAudioClip FuriMakeThisRight;
	private CustomAudioClip ayasaTheReasonWhy;
	private CustomAudioClip DefaultMusic;

	// Van alle clips een lijst met clips maken, dan vervolgens een clip teruggeven
	// die niet gebruikt wordt.
	// Zo kun je een maximum aantal clips afdwingen per soort zonder dat clips niet
	// kunnen afspelen.

	private int clipListSize = 10;
	private List<String> clipsWithThresholds = new ArrayList<String>();

	private List<CustomAudioClip> allActiveClips = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> defaultEMPClipList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> alienBombImpactList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> laserBeamClipList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> destroyedExplosionClipList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> alienSpaceshipDestroyedClipList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> largeShipDestroyedClipList = new ArrayList<CustomAudioClip>();

	
	private Map<String, List<CustomAudioClip>> audioClipsMap = new HashMap<>();
	
	private AudioDatabase() {
		initializeAudiofiles();
	}

	public static AudioDatabase getInstance() {
		return instance;
	}

	private void initializeAudiofiles() {
		clipsWithThresholds.add("Large Ship Destroyed");
		clipsWithThresholds.add("Destroyed Explosion");
		clipsWithThresholds.add("Alien Bomb Impact");
		try {
			initMusic();
			initSoundEffects();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void updateGameTick() {
		resetClips();
	}

	private void resetClips() {
		for (int i = 0; i < allActiveClips.size(); i++) {
			if (clipsWithThresholds.contains(allActiveClips.get(i).getAudioType())
					&& !allActiveClips.get(i).isActive()) {
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
		ayasaTheReasonWhy = new CustomAudioClip("Ayasa - The reason why", true);

	}

	private void initSoundEffects() throws LineUnavailableException {
		for (int i = 0; i < clipListSize; i++) {
			CustomAudioClip laserBeamClip = new CustomAudioClip("Player Laserbeam", false);
			laserBeamClipList.add(laserBeamClip);

			CustomAudioClip destroyedExplosion = new CustomAudioClip("Destroyed Explosion", false);
			destroyedExplosionClipList.add(destroyedExplosion);

			CustomAudioClip alienSpaceshipDestroyed = new CustomAudioClip("Alien Spaceship Destroyed", false);
			alienSpaceshipDestroyedClipList.add(alienSpaceshipDestroyed);

			CustomAudioClip alienBombImpact = new CustomAudioClip("Alien Bomb Impact", false);
			alienBombImpactList.add(alienBombImpact);

			CustomAudioClip largeShipDestroyedClip = new CustomAudioClip("Large Ship Destroyed", false);
			largeShipDestroyedClipList.add(largeShipDestroyedClip);

			CustomAudioClip defaultEMP = new CustomAudioClip("Default EMP", false);
			defaultEMPClipList.add(defaultEMP);
		}
		
		audioClipsMap.put("Player Laserbeam", laserBeamClipList);
		audioClipsMap.put("Destroyed Explosion", destroyedExplosionClipList);
		audioClipsMap.put("Alien Spaceship Destroyed", alienSpaceshipDestroyedClipList);
		audioClipsMap.put("Alien Bomb Impact", alienBombImpactList);
		audioClipsMap.put("Large Ship Destroyed", largeShipDestroyedClipList);
		audioClipsMap.put("Default EMP", defaultEMPClipList);

	}
	
	//Return a Clip which is not running
	private CustomAudioClip getAvailableClip(String clipType) {
	    List<CustomAudioClip> clipList = audioClipsMap.get(clipType);
	    if (clipList != null) {
	        for (CustomAudioClip clip : clipList) {
	            if (!clip.isRunning()) {
	                allActiveClips.add(clip);
	                return clip;
	            }
	        }
	    }
	    return null;
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
	        case ("Destroyed Explosion"):
	        case ("Alien Spaceship Destroyed"):
	        case ("Alien Bomb Impact"):
	        case ("Large Ship Destroyed"):
	        case ("Default EMP"):
	            return getAvailableClip(audioType);
	        case ("Ayasa - The reason why"):
	            return getAyasatheReasonWhy();
	    }
	    return null;
	}

	private CustomAudioClip getAyasatheReasonWhy() {
		return ayasaTheReasonWhy;
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

}
