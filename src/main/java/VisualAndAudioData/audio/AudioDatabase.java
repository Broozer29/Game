package VisualAndAudioData.audio;

import VisualAndAudioData.audio.enums.AudioEnums;

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
    private CustomAudioClip kingPalmRunway;
    private CustomAudioClip marvelGoldenDawn;
    private CustomAudioClip toneboxMemoryUpload;
    private CustomAudioClip forhillIris;
    private CustomAudioClip toneboxRadiumCloudHighway;

    private CustomAudioClip knightSomethingMemorable;
    private CustomAudioClip rainFormerlyKnownAsPurple;
    private CustomAudioClip bloodOnTheDanceFloor;
    private CustomAudioClip lemminoFireCracker;
    private CustomAudioClip mydnyte;
    private CustomAudioClip leYouthChills;
    private CustomAudioClip paintingTheSkies;

    private CustomAudioClip cannonsFireForYou;
    private CustomAudioClip embrzRainOnMyWindow;
    private CustomAudioClip embrzLightFalls;
    private CustomAudioClip deadmau5Monophobia;
    private CustomAudioClip johnyTheme;
    private CustomAudioClip viqGirlFromNowhere;
    private CustomAudioClip spaceSailorsCosmos;
    private CustomAudioClip newArcadesSevered;
    private CustomAudioClip arksunArisen;
    private CustomAudioClip ghostDataGodsOfTheArtificial;
    private CustomAudioClip ghostDataDarkHarvest;
    private CustomAudioClip blackGummySuperHuman;
    private CustomAudioClip madukAlone;
    private CustomAudioClip approachingNirvanaThousandPictures;
    private CustomAudioClip approachingNiravanaNoStringsAttached;
    private CustomAudioClip vendlaSonrisa;
    private CustomAudioClip waveshaperMonster;
    private CustomAudioClip tonyleysSnowdingRemix;
    private CustomAudioClip gustyGardenGalaxyRemix;
    private CustomAudioClip wePlantsTimeRemix;
    private CustomAudioClip bossBattle;
    private CustomAudioClip silentAudio;


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
    private List<CustomAudioClip> notEnoughMineralsClipList = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> chargingLaserbeamClipList = new ArrayList<CustomAudioClip>();


    private Map<AudioEnums, List<CustomAudioClip>> audioClipsMap = new HashMap<>();

    private AudioDatabase () {
        initializeAudiofiles();
        resetSongs();
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
        leYouthChills.setFramePosition(0);
        paintingTheSkies.setFramePosition(0);

        diqRose.setFramePosition(0);
        fiveSecondsBeforeSunrise.setFramePosition(0);
        downtownBinaryAstral.setFramePosition(0);
        carpenterBrutEnraged.setFramePosition(6048000);
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
        mydnyte.setFramePosition(0);
        lemminoFireCracker.setFramePosition(0);
        cannonsFireForYou.setFramePosition(0);
        embrzRainOnMyWindow.setFramePosition(0);
        deadmau5Monophobia.setFramePosition(0);
        embrzLightFalls.setFramePosition(0);
        johnyTheme.setFramePosition(0);
        viqGirlFromNowhere.setFramePosition(0);
        spaceSailorsCosmos.setFramePosition(0);
        newArcadesSevered.setFramePosition(0);
        arksunArisen.setFramePosition(0);
        ghostDataGodsOfTheArtificial.setFramePosition(0);
        ghostDataDarkHarvest.setFramePosition(0);
        blackGummySuperHuman.setFramePosition(0);
        madukAlone.setFramePosition(0);


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
            notEnoughMineralsClipList.get(i).setFramePosition(0);
            chargingLaserbeamClipList.get(i).setFramePosition(0);
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
        mainmenu = new CustomAudioClip(AudioEnums.mainmenu, false);
        paintingTheSkies = new CustomAudioClip(AudioEnums.Robert_Nickson_Painting_The_Skies, false);

        diqRose = new CustomAudioClip(AudioEnums.Viq_Rose, false);
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
        lemminoFireCracker = new CustomAudioClip(AudioEnums.Lemmino_Firecracker, false);
        mydnyte = new CustomAudioClip(AudioEnums.Mydnyte, false);
        leYouthChills = new CustomAudioClip(AudioEnums.Le_Youth_Chills, false);
        cannonsFireForYou = new CustomAudioClip(AudioEnums.Cannons_Fire_For_You, false);
        embrzRainOnMyWindow = new CustomAudioClip(AudioEnums.EMBRZ_Rain_On_My_Window, false);
        embrzLightFalls = new CustomAudioClip(AudioEnums.EMBRZ_Light_Falls, false);
        deadmau5Monophobia = new CustomAudioClip(AudioEnums.Deadmau5_Monophobia, false);
        johnyTheme = new CustomAudioClip(AudioEnums.Johny_Theme, false);
        viqGirlFromNowhere = new CustomAudioClip(AudioEnums.Viq_Girl_From_Nowhere, false);
        spaceSailorsCosmos = new CustomAudioClip(AudioEnums.Space_Sailors_Cosmos, false);
        newArcadesSevered = new CustomAudioClip(AudioEnums.New_Arcades_Severed, false);
        arksunArisen = new CustomAudioClip(AudioEnums.Arksun_Arisen, false);
        ghostDataGodsOfTheArtificial = new CustomAudioClip(AudioEnums.Ghost_Data_Gods_Of_The_Artificial, false);
        ghostDataDarkHarvest = new CustomAudioClip(AudioEnums.Ghost_Data_Dark_Harvest, false);
        blackGummySuperHuman = new CustomAudioClip(AudioEnums.BlackGummy_SuperHuman, false);
        madukAlone = new CustomAudioClip(AudioEnums.Maduk_Alone, false);
        kingPalmRunway = new CustomAudioClip(AudioEnums.KingPalmRunway, false);
        approachingNiravanaNoStringsAttached = new CustomAudioClip(AudioEnums.ApproachingNirvanaNoStringsAttached, false);
        approachingNirvanaThousandPictures = new CustomAudioClip(AudioEnums.ApproachingNirvanaThousandPictures, false);
        vendlaSonrisa = new CustomAudioClip(AudioEnums.VendlaSonrisa, false);
        waveshaperMonster = new CustomAudioClip(AudioEnums.WaveshaperMonster, false);
        tonyleysSnowdingRemix = new CustomAudioClip(AudioEnums.TonyLeysSnowdinRemix, false);
        gustyGardenGalaxyRemix = new CustomAudioClip(AudioEnums.GustyGardenGalaxyRemix, false);
        wePlantsTimeRemix = new CustomAudioClip(AudioEnums.WePlantsAreHappyPlantsTimeRemix, false);
        bossBattle = new CustomAudioClip(AudioEnums.BossBattle, false);
        silentAudio = new CustomAudioClip(AudioEnums.SilentAudio, true);

    }

    private void initSoundEffects () throws LineUnavailableException {
        for (int i = 0; i < clipListSize; i++) {
            CustomAudioClip chargingLaserbeam = new CustomAudioClip(AudioEnums.ChargingLaserbeam, false);
            chargingLaserbeamClipList.add(chargingLaserbeam);

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

            CustomAudioClip notEnoughMinerals = new CustomAudioClip(AudioEnums.NotEnoughMinerals, false);
            notEnoughMineralsClipList.add(notEnoughMinerals);
        }

        audioClipsMap.put(AudioEnums.ChargingLaserbeam, chargingLaserbeamClipList);
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
        audioClipsMap.put(AudioEnums.NotEnoughMinerals, notEnoughMineralsClipList);
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


    public CustomAudioClip getAudioClip (AudioEnums audioType) {
        switch (audioType) {
            case SilentAudio: return silentAudio;
            case BossBattle: return bossBattle;
            case WaveshaperMonster: return waveshaperMonster;
            case TonyLeysSnowdinRemix: return tonyleysSnowdingRemix;
            case GustyGardenGalaxyRemix: return gustyGardenGalaxyRemix;
            case WePlantsAreHappyPlantsTimeRemix: return wePlantsTimeRemix;
            case ApproachingNirvanaThousandPictures: return approachingNirvanaThousandPictures;
            case ApproachingNirvanaNoStringsAttached: return approachingNiravanaNoStringsAttached;
            case VendlaSonrisa: return vendlaSonrisa;
            case KingPalmRunway: return kingPalmRunway;
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
            case Viq_Rose:
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
            case Tonebox_Radium_Cloud_Highway:
                return toneboxRadiumCloudHighway;
            case Lemmino_Firecracker:
                return lemminoFireCracker;
            case Mydnyte:
                return mydnyte;
            case Carpenter_Brut_Youre_Mine:
                return carpenterBrutYoureMine;
            case Le_Youth_Chills:
                return leYouthChills;
            case Robert_Nickson_Painting_The_Skies:
                return paintingTheSkies;
            case Cannons_Fire_For_You:
                return cannonsFireForYou;
            case EMBRZ_Rain_On_My_Window:
                return embrzRainOnMyWindow;
            case EMBRZ_Light_Falls:
                return embrzLightFalls;
            case Deadmau5_Monophobia:
                return deadmau5Monophobia;
            case Johny_Theme:
                return johnyTheme;
            case Viq_Girl_From_Nowhere:
                return viqGirlFromNowhere;
            case Space_Sailors_Cosmos:
                return spaceSailorsCosmos;
            case New_Arcades_Severed:
                return newArcadesSevered;
            case Arksun_Arisen:
                return arksunArisen;
            case Ghost_Data_Gods_Of_The_Artificial:
                return ghostDataGodsOfTheArtificial;
            case Ghost_Data_Dark_Harvest:
                return ghostDataDarkHarvest;
            case BlackGummy_SuperHuman:
                return blackGummySuperHuman;
            case Maduk_Alone:
                return madukAlone;
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
            case NotEnoughMinerals:
            case ChargingLaserbeam:
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
