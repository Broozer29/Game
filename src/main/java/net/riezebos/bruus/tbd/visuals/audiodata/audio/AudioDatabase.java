package net.riezebos.bruus.tbd.visuals.audiodata.audio;

import net.riezebos.bruus.tbd.visuals.audiodata.audio.enums.AudioEnums;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    private int clipListSize = 7;
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
    private List<CustomAudioClip> newPlayerLaserbeamClipList = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> stickyGrenadeExplosionClipList = new ArrayList<CustomAudioClip>();
    private List<CustomAudioClip> playerTakesDamageClipList = new ArrayList<CustomAudioClip>();

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
//        FuriWisdomOfRage.setPlaybackPosition(0);
//        FuriMakeThisRight.setPlaybackPosition(0);
//        FuriMyOnlyChance.setPlaybackPosition(0);
//        DefaultMusic.setPlaybackPosition(0);
//        ayasaTheReasonWhy.setPlaybackPosition(0);
//        NewArcadesSolace.setPlaybackPosition(0);
//        mainmenu.setPlaybackPosition(0);
//        leYouthChills.setPlaybackPosition(0);
//        paintingTheSkies.setPlaybackPosition(0);
//
//        diqRose.setPlaybackPosition(0);
//        fiveSecondsBeforeSunrise.setPlaybackPosition(0);
//        downtownBinaryAstral.setPlaybackPosition(0);
//        carpenterBrutEnraged.setPlaybackPosition(6048000);
//        carpenterBrutYoureMine.setPlaybackPosition(0);
//        alphaRoomComeBack.setPlaybackPosition(0);
//        carpenterBrutDanger.setPlaybackPosition(0);
//        downtownBinaryFantasia.setPlaybackPosition(0);
//        downtownBinaryLightCycles.setPlaybackPosition(0);
//        marvelGoldenDawn.setPlaybackPosition(0);
//        toneboxMemoryUpload.setPlaybackPosition(0);
//        forhillIris.setPlaybackPosition(0);
//        toneboxRadiumCloudHighway.setPlaybackPosition(0);
//        knightSomethingMemorable.setPlaybackPosition(0);
//        rainFormerlyKnownAsPurple.setPlaybackPosition(0);
//        bloodOnTheDanceFloor.setPlaybackPosition(0);
//        mydnyte.setPlaybackPosition(0);
//        lemminoFireCracker.setPlaybackPosition(0);
//        cannonsFireForYou.setPlaybackPosition(0);
//        embrzRainOnMyWindow.setPlaybackPosition(0);
//        deadmau5Monophobia.setPlaybackPosition(0);
//        embrzLightFalls.setPlaybackPosition(0);
//        johnyTheme.setPlaybackPosition(0);
//        viqGirlFromNowhere.setPlaybackPosition(0);
//        spaceSailorsCosmos.setPlaybackPosition(0);
//        newArcadesSevered.setPlaybackPosition(0);
//        arksunArisen.setPlaybackPosition(0);
//        ghostDataGodsOfTheArtificial.setPlaybackPosition(0);
//        ghostDataDarkHarvest.setPlaybackPosition(0);
//        blackGummySuperHuman.setPlaybackPosition(0);
//        madukAlone.setPlaybackPosition(0);


        for (int i = 0; i < clipListSize; i++) {
            defaultEMPClipList.get(i).setPlaybackPosition(0);
            alienBombImpactList.get(i).setPlaybackPosition(0);
            laserBeamClipList.get(i).setPlaybackPosition(0);
            destroyedExplosionClipList.get(i).setPlaybackPosition(0);
            alienSpaceshipDestroyedClipList.get(i).setPlaybackPosition(0);
            largeShipDestroyedClipList.get(i).setPlaybackPosition(0);
            powerUpAcquiredClipList.get(i).setPlaybackPosition(0);
            rocketLauncherClipList.get(i).setPlaybackPosition(0);
            flamethrowerClipList.get(i).setPlaybackPosition(0);
            firewallClipList.get(i).setPlaybackPosition(0);
            notEnoughMineralsClipList.get(i).setPlaybackPosition(0);
            chargingLaserbeamClipList.get(i).setPlaybackPosition(0);
            newPlayerLaserbeamClipList.get(i).setPlaybackPosition(0);
            stickyGrenadeExplosionClipList.get(i).setPlaybackPosition(0);
            playerTakesDamageClipList.get(i).setPlaybackPosition(0);
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
            if (allActiveClips.get(i).isFinished()) {
                allActiveClips.get(i).setPlaybackPosition(0);
                allActiveClips.get(i).stopClip();
                allActiveClips.remove(allActiveClips.get(i));
            }
        }
    }

    private void initMusic () throws LineUnavailableException {
        DefaultMusic = new CustomAudioClip(AudioEnums.Apple_Holder_Remix);
        FuriWisdomOfRage = new CustomAudioClip(AudioEnums.Furi_Wisdowm_Of_Rage);
        FuriMakeThisRight = new CustomAudioClip(AudioEnums.Furi_Make_This_Right);
        FuriMyOnlyChance = new CustomAudioClip(AudioEnums.Furi_My_Only_Chance);
        ayasaTheReasonWhy = new CustomAudioClip(AudioEnums.Ayasa_The_Reason_Why);
        NewArcadesSolace = new CustomAudioClip(AudioEnums.New_Arcades_Solace);
        mainmenu = new CustomAudioClip(AudioEnums.mainmenu);
        paintingTheSkies = new CustomAudioClip(AudioEnums.Robert_Nickson_Painting_The_Skies);

        diqRose = new CustomAudioClip(AudioEnums.Viq_Rose);
        fiveSecondsBeforeSunrise = new CustomAudioClip(AudioEnums.Five_Seconds_Before_Sunrise);
        downtownBinaryAstral = new CustomAudioClip(AudioEnums.Downtown_Binary_Astral);
        carpenterBrutEnraged = new CustomAudioClip(AudioEnums.Carpenter_Brut_Enraged);
        carpenterBrutYoureMine = new CustomAudioClip(AudioEnums.Carpenter_Brut_Youre_Mine);
        alphaRoomComeBack = new CustomAudioClip(AudioEnums.Alpha_Room_Come_Back);
        carpenterBrutDanger = new CustomAudioClip(AudioEnums.Carpenter_Brut_Danger);
        downtownBinaryFantasia = new CustomAudioClip(AudioEnums.Downtown_Binary_Fantasia);
        downtownBinaryLightCycles = new CustomAudioClip(AudioEnums.Downtown_Binary_Light_Cycles);
        marvelGoldenDawn = new CustomAudioClip(AudioEnums.Marvel_Golden_Dawn);
        toneboxMemoryUpload = new CustomAudioClip(AudioEnums.Tonebox_Memory_Upload);
        forhillIris = new CustomAudioClip(AudioEnums.Forhill_Iris);
        toneboxRadiumCloudHighway = new CustomAudioClip(AudioEnums.Tonebox_Radium_Cloud_Highway);
        knightSomethingMemorable = new CustomAudioClip(AudioEnums.Knight_Something_Memorable);
        rainFormerlyKnownAsPurple = new CustomAudioClip(AudioEnums.The_Rain_Formerly_Known_As_Purple);
        bloodOnTheDanceFloor = new CustomAudioClip(AudioEnums.Blood_On_The_Dancefloor);
        lemminoFireCracker = new CustomAudioClip(AudioEnums.Lemmino_Firecracker);
        mydnyte = new CustomAudioClip(AudioEnums.Mydnyte);
        leYouthChills = new CustomAudioClip(AudioEnums.Le_Youth_Chills);
        cannonsFireForYou = new CustomAudioClip(AudioEnums.Cannons_Fire_For_You);
        embrzRainOnMyWindow = new CustomAudioClip(AudioEnums.EMBRZ_Rain_On_My_Window);
        embrzLightFalls = new CustomAudioClip(AudioEnums.EMBRZ_Light_Falls);
        deadmau5Monophobia = new CustomAudioClip(AudioEnums.Deadmau5_Monophobia);
        johnyTheme = new CustomAudioClip(AudioEnums.Johny_Theme);
        viqGirlFromNowhere = new CustomAudioClip(AudioEnums.Viq_Girl_From_Nowhere);
        spaceSailorsCosmos = new CustomAudioClip(AudioEnums.Space_Sailors_Cosmos);
        newArcadesSevered = new CustomAudioClip(AudioEnums.New_Arcades_Severed);
        arksunArisen = new CustomAudioClip(AudioEnums.Arksun_Arisen);
        ghostDataGodsOfTheArtificial = new CustomAudioClip(AudioEnums.Ghost_Data_Gods_Of_The_Artificial);
        ghostDataDarkHarvest = new CustomAudioClip(AudioEnums.Ghost_Data_Dark_Harvest);
        blackGummySuperHuman = new CustomAudioClip(AudioEnums.BlackGummy_SuperHuman);
        madukAlone = new CustomAudioClip(AudioEnums.Maduk_Alone);
        kingPalmRunway = new CustomAudioClip(AudioEnums.KingPalmRunway);
        approachingNiravanaNoStringsAttached = new CustomAudioClip(AudioEnums.ApproachingNirvanaNoStringsAttached);
        approachingNirvanaThousandPictures = new CustomAudioClip(AudioEnums.ApproachingNirvanaThousandPictures);
        vendlaSonrisa = new CustomAudioClip(AudioEnums.VendlaSonrisa);
        waveshaperMonster = new CustomAudioClip(AudioEnums.WaveshaperMonster);
        tonyleysSnowdingRemix = new CustomAudioClip(AudioEnums.TonyLeysSnowdinRemix);
        gustyGardenGalaxyRemix = new CustomAudioClip(AudioEnums.GustyGardenGalaxyRemix);
        wePlantsTimeRemix = new CustomAudioClip(AudioEnums.WePlantsAreHappyPlantsTimeRemix);
        bossBattle = new CustomAudioClip(AudioEnums.BossBattle);
        silentAudio = new CustomAudioClip(AudioEnums.SilentAudio);

    }

    private void initSoundEffects () throws LineUnavailableException {
        for (int i = 0; i < clipListSize; i++) {
            CustomAudioClip chargingLaserbeam = new CustomAudioClip(AudioEnums.ChargingLaserbeam);
            chargingLaserbeamClipList.add(chargingLaserbeam);

            CustomAudioClip laserBeamClip = new CustomAudioClip(AudioEnums.Player_Laserbeam);
            laserBeamClipList.add(laserBeamClip);

            CustomAudioClip destroyedExplosion = new CustomAudioClip(AudioEnums.Destroyed_Explosion);
            destroyedExplosionClipList.add(destroyedExplosion);

            CustomAudioClip alienSpaceshipDestroyed = new CustomAudioClip(AudioEnums.Alien_Spaceship_Destroyed);
            alienSpaceshipDestroyedClipList.add(alienSpaceshipDestroyed);

            CustomAudioClip alienBombImpact = new CustomAudioClip(AudioEnums.Alien_Bomb_Impact);
            alienBombImpactList.add(alienBombImpact);

            CustomAudioClip largeShipDestroyedClip = new CustomAudioClip(AudioEnums.Large_Ship_Destroyed);
            largeShipDestroyedClipList.add(largeShipDestroyedClip);

            CustomAudioClip defaultEMP = new CustomAudioClip(AudioEnums.Default_EMP);
            defaultEMPClipList.add(defaultEMP);

            CustomAudioClip powerUpAcquired = new CustomAudioClip(AudioEnums.Power_Up_Acquired);
            powerUpAcquiredClipList.add(powerUpAcquired);

            CustomAudioClip rocketLauncherClip = new CustomAudioClip(AudioEnums.Rocket_Launcher);
            rocketLauncherClipList.add(rocketLauncherClip);

            CustomAudioClip flamethrowerClip = new CustomAudioClip(AudioEnums.Flamethrower);
            flamethrowerClipList.add(flamethrowerClip);

            CustomAudioClip firewallClip = new CustomAudioClip(AudioEnums.Firewall);
            firewallClipList.add(firewallClip);

            CustomAudioClip alienBombDestroyed = new CustomAudioClip(AudioEnums.Alien_Bomb_Destroyed);
            alienBombDestroyedList.add(alienBombDestroyed);

            CustomAudioClip notEnoughMinerals = new CustomAudioClip(AudioEnums.NotEnoughMinerals);
            notEnoughMineralsClipList.add(notEnoughMinerals);

            CustomAudioClip newPlayerLaserbeam = new CustomAudioClip(AudioEnums.NewPlayerLaserbeam);
            newPlayerLaserbeamClipList.add(newPlayerLaserbeam);

            CustomAudioClip stickyGrenadeExplosion = new CustomAudioClip(AudioEnums.StickyGrenadeExplosion);
            stickyGrenadeExplosionClipList.add(stickyGrenadeExplosion);

            CustomAudioClip playerTakesDamage = new CustomAudioClip(AudioEnums.PlayerTakesDamage);
            playerTakesDamageClipList.add(playerTakesDamage);
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
        audioClipsMap.put(AudioEnums.StickyGrenadeExplosion, stickyGrenadeExplosionClipList);
        audioClipsMap.put(AudioEnums.NewPlayerLaserbeam, newPlayerLaserbeamClipList);
        audioClipsMap.put(AudioEnums.PlayerTakesDamage, playerTakesDamageClipList);
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
            case NewPlayerLaserbeam:
            case StickyGrenadeExplosion:
            case PlayerTakesDamage:
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
