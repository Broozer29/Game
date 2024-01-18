package VisualAndAudioData.audio;

import javax.sound.sampled.LineUnavailableException;
import java.util.*;

public class AudioDatabase {

    private static AudioDatabase instance = new AudioDatabase();
    private CustomAudioClip FuriWisdomOfRage;
    private CustomAudioClip FuriMyOnlyChance;
    private CustomAudioClip FuriMakeThisRight;
    private CustomAudioClip ayasaTheReasonWhy;
    private CustomAudioClip DefaultMusic;
    private CustomAudioClip NewArcadesSolace;
    private CustomAudioClip mainmenu;
    private CustomAudioClip diqRose;
    private CustomAudioClip fiveSecondsBeforeSunrise;
    private CustomAudioClip downtownBinaryAstral;
    private CustomAudioClip carpenterBrutEnraged;
    private CustomAudioClip carpenterBrutYoureMine;
    private CustomAudioClip alphaRoomComeBack;
    private CustomAudioClip carpenterBrutDanger;
    private CustomAudioClip downtownBinaryFantasia;
    private CustomAudioClip downtownBinaryLightCycles;
    private CustomAudioClip marvelGoldenDawn;
    private CustomAudioClip toneboxMemoryUpload;
    private CustomAudioClip forhillIris;
    private CustomAudioClip toneboxRadiumCloudHighway;

    private CustomAudioClip knightSomethingMemorable;
    private CustomAudioClip rainFormerlyKnownAsPurple;
    private CustomAudioClip bloodOnTheDanceFloor;


    private List<CustomAudioClip> backgroundMusicTracks = new ArrayList();

    // Van alle clips een lijst met clips maken, dan vervolgens een clip teruggeven
    // die niet gebruikt wordt.
    // Zo kun je een maximum aantal clips afdwingen per soort zonder dat clips niet
    // kunnen afspelen.

    private int clipListSize = 10;
    private List<AudioEnums> clipsWithThresholds = new ArrayList<AudioEnums>();

    private List<CustomAudioClip> allActiveClips = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> defaultEMPClipList = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> alienBombImpactList = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> alienBombDestroyedList = new ArrayList<CustomAudioClip>();

    private List<CustomAudioClip> laserBeamClipList = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> destroyedExplosionClipList = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> alienSpaceshipDestroyedClipList = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> largeShipDestroyedClipList = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> powerUpAcquiredClipList = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> rocketLauncherClipList = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> flamethrowerClipList = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> firewallClipList = new ArrayList<CustomAudioClip>();


    private Map<AudioEnums, List<CustomAudioClip>> audioClipsMap = new HashMap<>();

    private AudioDatabase () {
        initializeAudiofiles();
    }

    public static AudioDatabase getInstance () {
        return instance;
    }

    //A reset of background songs that should be called by the AudioManager
    //This should reset all background songs!
    public void resetSongs () {
        FuriWisdomOfRage.setFramePosition(0);
        FuriMakeThisRight.setFramePosition(0);
        FuriMyOnlyChance.setFramePosition(0);
        DefaultMusic.setFramePosition(0);
        ayasaTheReasonWhy.setFramePosition(0);
        NewArcadesSolace.setFramePosition(0);
        mainmenu.setFramePosition(0);

        diqRose.setFramePosition(0);
        fiveSecondsBeforeSunrise.setFramePosition(0);
        downtownBinaryAstral.setFramePosition(0);
        carpenterBrutEnraged.setFramePosition(0);
        carpenterBrutYoureMine.setFramePosition(0);
        alphaRoomComeBack.setFramePosition(0);
        carpenterBrutDanger.setFramePosition(0);
        downtownBinaryFantasia.setFramePosition(0);
        downtownBinaryLightCycles.setFramePosition(0);
        marvelGoldenDawn.setFramePosition(0);
        toneboxMemoryUpload.setFramePosition(0);
        forhillIris.setFramePosition(0);
        toneboxRadiumCloudHighway.setFramePosition(0);
        knightSomethingMemorable.setFramePosition(0);
        rainFormerlyKnownAsPurple.setFramePosition(0);
        bloodOnTheDanceFloor.setFramePosition(0);


        for (int i = 0; i < clipListSize; i++) {
            defaultEMPClipList.get(i).setFramePosition(0);
            alienBombImpactList.get(i).setFramePosition(0);
            laserBeamClipList.get(i).setFramePosition(0);
            destroyedExplosionClipList.get(i).setFramePosition(0);
            alienSpaceshipDestroyedClipList.get(i).setFramePosition(0);
            largeShipDestroyedClipList.get(i).setFramePosition(0);
            powerUpAcquiredClipList.get(i).setFramePosition(0);
            rocketLauncherClipList.get(i).setFramePosition(0);
            flamethrowerClipList.get(i).setFramePosition(0);
            firewallClipList.get(i).setFramePosition(0);
        }
    }

    private void initializeAudiofiles () {
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

    public void updateGameTick () {
        resetClips();
    }

    private void resetClips () {
        for (int i = 0; i < allActiveClips.size(); i++) {
            if (clipsWithThresholds.contains(allActiveClips.get(i).getAudioType())
                    && !allActiveClips.get(i).isActive()) {
                if (allActiveClips.get(i).aboveThreshold()) {
                    allActiveClips.get(i).setFramePosition(0);
                    allActiveClips.get(i).stopClip();
                    allActiveClips.remove(allActiveClips.get(i));
                }
            } else if (!allActiveClips.get(i).isActive()) {
                allActiveClips.get(i).stopClip();
                allActiveClips.get(i).setFramePosition(0);
                allActiveClips.remove(allActiveClips.get(i));
            }

        }
    }

    private void initMusic () throws LineUnavailableException {
        DefaultMusic = new CustomAudioClip(AudioEnums.Apple_Holder_Remix, false);
        FuriWisdomOfRage = new CustomAudioClip(AudioEnums.Furi_Wisdowm_Of_Rage, false);
        FuriMakeThisRight = new CustomAudioClip(AudioEnums.Furi_Make_This_Right, false);
        FuriMyOnlyChance = new CustomAudioClip(AudioEnums.Furi_My_Only_Chance, false);
        ayasaTheReasonWhy = new CustomAudioClip(AudioEnums.Ayasa_The_Reason_Why, false);
        NewArcadesSolace = new CustomAudioClip(AudioEnums.New_Arcades_Solace, false);
        mainmenu = new CustomAudioClip(AudioEnums.mainmenu, true);

        diqRose = new CustomAudioClip(AudioEnums.Diq_Rose, false);
        fiveSecondsBeforeSunrise = new CustomAudioClip(AudioEnums.Five_Seconds_Before_Sunrise, false);
        downtownBinaryAstral = new CustomAudioClip(AudioEnums.Downtown_Binary_Astral, false);
        carpenterBrutEnraged = new CustomAudioClip(AudioEnums.Carpenter_Brut_Enraged, false);
        carpenterBrutYoureMine = new CustomAudioClip(AudioEnums.Carpenter_Brut_Youre_Mine, false);
        alphaRoomComeBack = new CustomAudioClip(AudioEnums.Alpha_Room_Come_Back, false);
        carpenterBrutDanger = new CustomAudioClip(AudioEnums.Carpenter_Brut_Danger, false);
        downtownBinaryFantasia = new CustomAudioClip(AudioEnums.Downtown_Binary_Fantasia, false);
        downtownBinaryLightCycles = new CustomAudioClip(AudioEnums.Downtown_Binary_Light_Cycles, false);
        marvelGoldenDawn = new CustomAudioClip(AudioEnums.Marvel_Golden_Dawn, false);
        toneboxMemoryUpload = new CustomAudioClip(AudioEnums.Tonebox_Memory_Upload, false);
        forhillIris = new CustomAudioClip(AudioEnums.Forhill_Iris, false);
        toneboxRadiumCloudHighway = new CustomAudioClip(AudioEnums.Tonebox_Radium_Cloud_Highway, false);
        knightSomethingMemorable = new CustomAudioClip(AudioEnums.Knight_Something_Memorable, false);
        rainFormerlyKnownAsPurple = new CustomAudioClip(AudioEnums.The_Rain_Formerly_Known_As_Purple, false);
        bloodOnTheDanceFloor = new CustomAudioClip(AudioEnums.Blood_On_The_Dancefloor, false);


        backgroundMusicTracks.add(FuriMyOnlyChance);
        backgroundMusicTracks.add(FuriWisdomOfRage);
        backgroundMusicTracks.add(FuriMakeThisRight);
        backgroundMusicTracks.add(NewArcadesSolace);
        backgroundMusicTracks.add(diqRose);
        backgroundMusicTracks.add(fiveSecondsBeforeSunrise);
        backgroundMusicTracks.add(downtownBinaryAstral);
        backgroundMusicTracks.add(carpenterBrutEnraged);
        backgroundMusicTracks.add(carpenterBrutYoureMine);
        backgroundMusicTracks.add(alphaRoomComeBack);
        backgroundMusicTracks.add(carpenterBrutDanger);
        backgroundMusicTracks.add(downtownBinaryFantasia);
        backgroundMusicTracks.add(downtownBinaryLightCycles);
        backgroundMusicTracks.add(marvelGoldenDawn);
        backgroundMusicTracks.add(toneboxMemoryUpload);
        backgroundMusicTracks.add(forhillIris);
        backgroundMusicTracks.add(toneboxRadiumCloudHighway);
        backgroundMusicTracks.add(knightSomethingMemorable);
        backgroundMusicTracks.add(rainFormerlyKnownAsPurple);
        backgroundMusicTracks.add(bloodOnTheDanceFloor);
    }

    private void initSoundEffects () throws LineUnavailableException {
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

            CustomAudioClip rocketLauncherClip = new CustomAudioClip(AudioEnums.Rocket_Launcher, false);
            rocketLauncherClipList.add(rocketLauncherClip);

            CustomAudioClip flamethrowerClip = new CustomAudioClip(AudioEnums.Flamethrower, false);
            flamethrowerClipList.add(flamethrowerClip);

            CustomAudioClip firewallClip = new CustomAudioClip(AudioEnums.Firewall, false);
            firewallClipList.add(firewallClip);

            CustomAudioClip alienBombDestroyed = new CustomAudioClip(AudioEnums.Alien_Bomb_Destroyed, false);
            alienBombDestroyedList.add(alienBombDestroyed);
        }

        audioClipsMap.put(AudioEnums.Player_Laserbeam, laserBeamClipList);
        audioClipsMap.put(AudioEnums.Destroyed_Explosion, destroyedExplosionClipList);
        audioClipsMap.put(AudioEnums.Alien_Spaceship_Destroyed, alienSpaceshipDestroyedClipList);
        audioClipsMap.put(AudioEnums.Alien_Bomb_Impact, alienBombImpactList);
        audioClipsMap.put(AudioEnums.Alien_Bomb_Destroyed, alienBombDestroyedList);
        audioClipsMap.put(AudioEnums.Large_Ship_Destroyed, largeShipDestroyedClipList);
        audioClipsMap.put(AudioEnums.Default_EMP, defaultEMPClipList);
        audioClipsMap.put(AudioEnums.Power_Up_Acquired, powerUpAcquiredClipList);
        audioClipsMap.put(AudioEnums.Firewall, firewallClipList);
        audioClipsMap.put(AudioEnums.Rocket_Launcher, rocketLauncherClipList);
        audioClipsMap.put(AudioEnums.Flamethrower, flamethrowerClipList);

    }

    //Return a Clip which is not running
    private CustomAudioClip getAvailableClip (AudioEnums clipType) {
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

    public CustomAudioClip selectRandomMusicTrack() {
        Random random = new Random();
        CustomAudioClip clip = null;
        int attempts = 0;
        int maxAttempts = backgroundMusicTracks.size(); // Assuming this is the total number of tracks available

        while (clip == null && attempts < maxAttempts) {
            clip = getAudioClip(backgroundMusicTracks.get(random.nextInt(backgroundMusicTracks.size())).getAudioType());
            attempts++;
        }

        if (clip == null) {
            System.out.println("Man it really went to shit");
            // Handle the case where no valid track is found after all attempts
            // You might want to log an error or throw an exception depending on your application's requirements
        }

        return clip;
    }

    public CustomAudioClip getAudioClip (AudioEnums audioType) {
        switch (audioType) {
            case Furi_Wisdowm_Of_Rage:
                return FuriWisdomOfRage;
            case Furi_My_Only_Chance:
                return FuriMyOnlyChance;
            case Furi_Make_This_Right:
                return FuriMakeThisRight;
            case Apple_Holder_Remix:
                return DefaultMusic;
            case New_Arcades_Solace:
                return NewArcadesSolace;
            case Diq_Rose:
                return diqRose;
            case Five_Seconds_Before_Sunrise:
                return fiveSecondsBeforeSunrise;
            case Downtown_Binary_Astral:
                return downtownBinaryAstral;
            case Carpenter_Brut_Enraged:
                return carpenterBrutEnraged;
            case Carpenter_Brut_Danger:
                return carpenterBrutDanger;
            case Alpha_Room_Come_Back:
                return alphaRoomComeBack;
            case Knight_Something_Memorable:
                return knightSomethingMemorable;
            case Downtown_Binary_Fantasia:
                return downtownBinaryFantasia;
            case Downtown_Binary_Light_Cycles:
                return downtownBinaryLightCycles;
            case Marvel_Golden_Dawn:
                return marvelGoldenDawn;
            case Tonebox_Memory_Upload:
                return toneboxMemoryUpload;
            case Forhill_Iris:
                return forhillIris;
            case The_Rain_Formerly_Known_As_Purple:
                return rainFormerlyKnownAsPurple;
            case Blood_On_The_Dancefloor:
                return bloodOnTheDanceFloor;
            case Player_Laserbeam:
            case Destroyed_Explosion:
            case Alien_Spaceship_Destroyed:
            case Alien_Bomb_Impact:
            case Large_Ship_Destroyed:
            case Default_EMP:
            case Alien_Bomb_Destroyed:
            case Power_Up_Acquired:
            case Rocket_Launcher:
            case Flamethrower:
            case Firewall:
                return getAvailableClip(audioType);
            case Ayasa_The_Reason_Why:
                return ayasaTheReasonWhy;
            case mainmenu:
                return this.mainmenu;

            case NONE:
                break;
        }
        return null;
    }

}
