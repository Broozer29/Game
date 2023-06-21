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
	private List<AudioEnums> clipsWithThresholds = new ArrayList<AudioEnums>();

	private List<CustomAudioClip> allActiveClips = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> defaultEMPClipList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> alienBombImpactList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> laserBeamClipList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> destroyedExplosionClipList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> alienSpaceshipDestroyedClipList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> largeShipDestroyedClipList = new ArrayList<CustomAudioClip>();
	private List<CustomAudioClip> powerUpAcquiredClipList = new ArrayList<CustomAudioClip>();

	
	private Map<AudioEnums, List<CustomAudioClip>> audioClipsMap = new HashMap<>();
	
	private AudioDatabase() {
		initializeAudiofiles();
	}

	public static AudioDatabase getInstance() {
		return instance;
	}

	private void initializeAudiofiles() {
		clipsWithThresholds.add(AudioEnums.Large_Ship_Destroyed);
		clipsWithThresholds.add(AudioEnums.Destroyed_Explosion);
		clipsWithThresholds.add(AudioEnums.Alien_Bomb_Impact);
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
		DefaultMusic = new CustomAudioClip(AudioEnums.Apple_Holder_Remix, true);
		FuriWisdomOfRage = new CustomAudioClip(AudioEnums.Furi_Wisdowm_Of_Rage, true);
		FuriMakeThisRight = new CustomAudioClip(AudioEnums.Furi_Make_This_Right , true);
		FuriMyOnlyChance = new CustomAudioClip(AudioEnums.Furi_My_Only_Chance, true);
		ayasaTheReasonWhy = new CustomAudioClip(AudioEnums.Ayasa_The_Reason_Why, true);

	}

	private void initSoundEffects() throws LineUnavailableException {
		for (int i = 0; i < clipListSize; i++) {
			CustomAudioClip laserBeamClip = new CustomAudioClip(AudioEnums.Player_Laserbeam, false);
			laserBeamClipList.add(laserBeamClip);

			CustomAudioClip destroyedExplosion = new CustomAudioClip(AudioEnums.Destroyed_Explosion, false);
			destroyedExplosionClipList.add(destroyedExplosion);

			CustomAudioClip alienSpaceshipDestroyed = new CustomAudioClip(AudioEnums.Alien_Spaceship_Destroyed, false);
			alienSpaceshipDestroyedClipList.add(alienSpaceshipDestroyed);

			CustomAudioClip alienBombImpact = new CustomAudioClip(AudioEnums.Alien_Bomb_Impact, false);
			alienBombImpactList.add(alienBombImpact);

			CustomAudioClip largeShipDestroyedClip = new CustomAudioClip(AudioEnums.Large_Ship_Destroyed, false);
			largeShipDestroyedClipList.add(largeShipDestroyedClip);

			CustomAudioClip defaultEMP = new CustomAudioClip(AudioEnums.Default_EMP, false);
			defaultEMPClipList.add(defaultEMP);
			
			CustomAudioClip powerUpAcquired = new CustomAudioClip(AudioEnums.Power_Up_Acquired, false);
			powerUpAcquiredClipList.add(powerUpAcquired);
		}
		
		audioClipsMap.put(AudioEnums.Player_Laserbeam, laserBeamClipList);
		audioClipsMap.put(AudioEnums.Destroyed_Explosion, destroyedExplosionClipList);
		audioClipsMap.put(AudioEnums.Alien_Spaceship_Destroyed, alienSpaceshipDestroyedClipList);
		audioClipsMap.put(AudioEnums.Alien_Bomb_Impact, alienBombImpactList);
		audioClipsMap.put(AudioEnums.Large_Ship_Destroyed, largeShipDestroyedClipList);
		audioClipsMap.put(AudioEnums.Default_EMP, defaultEMPClipList);
		audioClipsMap.put(AudioEnums.Power_Up_Acquired, powerUpAcquiredClipList);

	}
	
	//Return a Clip which is not running
	private CustomAudioClip getAvailableClip(AudioEnums clipType) {
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

	public CustomAudioClip getAudioClip(AudioEnums audioType) {
	    switch (audioType) {
	        case Furi_Wisdowm_Of_Rage:
	            return getFuriWisdomOfRage();
	        case Furi_My_Only_Chance:
	            return getFuriMyOnlyChance();
	        case Furi_Make_This_Right:
	            return getFuriMakeThisRight();
	        case Apple_Holder_Remix:
	            return getDefaultMusic();
	        case Player_Laserbeam:
	        case Destroyed_Explosion:
	        case Alien_Spaceship_Destroyed:
	        case Alien_Bomb_Impact:
	        case Large_Ship_Destroyed:
	        case Default_EMP:
	        case Alien_Bomb_Destroyed:
	        case Power_Up_Acquired:
	            return getAvailableClip(audioType);
	        case Ayasa_The_Reason_Why:
	            return getAyasatheReasonWhy();
		case NONE:
			break;
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
