package net.riezebos.bruus.tbd.visualsandaudio.data.audio;

import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLoggerManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudioDatabase {

    private static AudioDatabase instance = new AudioDatabase();
    private PerformanceLogger performanceLogger = null;
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
    private CustomAudioClip riskOfDanger;

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


    private List<AudioEnums> clipsWithThresholds = new ArrayList<>();

    private List<CustomAudioClip> allActiveClips = new ArrayList<>();

    private Map<AudioEnums, List<CustomAudioClip>> audioClipsMap = new HashMap<>();

    private Map<AudioEnums, Integer> clipSizeConfig = new HashMap<>() {{
        put(AudioEnums.QueenDeath, 2);
        put(AudioEnums.OverlordDeath, 2);
        put(AudioEnums.MutaliskBirth, 2);
        put(AudioEnums.MutaliskDeath, 3);
        put(AudioEnums.DevourerBirth, 3);
        put(AudioEnums.DevourerHit, 3);
        put(AudioEnums.DevourerDeath, 3);
        put(AudioEnums.ScourgeCollision, 3);
        put(AudioEnums.ScourgeDeath, 3);
        put(AudioEnums.ScourgeNoticed, 3);
        put(AudioEnums.GuardianBirth, 3);
        put(AudioEnums.GuardianDeath, 3);
        put(AudioEnums.ChargingLaserbeam, 2);
        put(AudioEnums.Player_Laserbeam, 1);
        put(AudioEnums.Destroyed_Explosion, 5);
        put(AudioEnums.Alien_Bomb_Destroyed, 3);
        put(AudioEnums.Alien_Bomb_Impact, 3);
        put(AudioEnums.Alien_Spaceship_Destroyed, 5);
        put(AudioEnums.Large_Ship_Destroyed, 5);
        put(AudioEnums.Default_EMP, 2);
        put(AudioEnums.ItemAcquired, 3);
        put(AudioEnums.Firewall, 2);
        put(AudioEnums.Rocket_Launcher, 3);
        put(AudioEnums.Flamethrower, 1);
        put(AudioEnums.NotEnoughMinerals, 3);
        put(AudioEnums.StickyGrenadeExplosion, 3);
        put(AudioEnums.NewPlayerLaserbeam, 9);
        put(AudioEnums.PlayerTakesDamage, 5);
        put(AudioEnums.SpecialAttackFinishedCharging, 2);
        put(AudioEnums.SpaceStationBlastingOff, 1);
        put(AudioEnums.SpaceStationChargingUpMovement, 1);
    }};

    private AudioDatabase () {
        this.performanceLogger = new PerformanceLogger("Audio Manager");
        initializeAudiofiles();
        resetAudio();
    }

    public static AudioDatabase getInstance () {
        return instance;
    }

    public void resetAudio() {
        for (List<CustomAudioClip> clipList : audioClipsMap.values()) {
            for (CustomAudioClip clip : clipList) {
                clip.setPlaybackPosition(0);
                clip.stopClip();;
            }
        }
        performanceLogger.reset();
    }

    private void initializeAudiofiles () {
        clipsWithThresholds.add(AudioEnums.Large_Ship_Destroyed);
        clipsWithThresholds.add(AudioEnums.Destroyed_Explosion);
        clipsWithThresholds.add(AudioEnums.Alien_Bomb_Impact);
        initMusic();
        initSoundEffects();
    }

    private int tickCounter = 0;

    public void updateGameTick() {
        if (tickCounter++ % 3 == 0) {  // Run every 3rd tick to save performance, theoretically should not be noticeable
            PerformanceLoggerManager.timeAndLog(performanceLogger, "Reset Clips", this::resetClips);
        }
    }

    private void resetClips() {
        allActiveClips.removeIf(clip -> {
            if (clip.isFinished()) {
                clip.setPlaybackPosition(0);
                clip.stopClip();
                return true; // Remove from list
            }
            return false; // Keep in list
        });
    }

    private void initMusic () {
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
        riskOfDanger = new CustomAudioClip(AudioEnums.RiskOfDanger);
        silentAudio = new CustomAudioClip(AudioEnums.SilentAudio);

    }

    private void initSoundEffects () {
        for (Map.Entry<AudioEnums, Integer> entry : clipSizeConfig.entrySet()) {
            AudioEnums audioType = entry.getKey();
            int listSize = entry.getValue();
            List<CustomAudioClip> clipList = new ArrayList<>();

            for (int i = 0; i < listSize; i++) {
                CustomAudioClip clip = new CustomAudioClip(audioType);
                clipList.add(clip);
            }

            audioClipsMap.put(audioType, clipList);
        }
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
            case SilentAudio:
                return silentAudio;
            case BossBattle:
                return bossBattle;
            case WaveshaperMonster:
                return waveshaperMonster;
            case TonyLeysSnowdinRemix:
                return tonyleysSnowdingRemix;
            case GustyGardenGalaxyRemix:
                return gustyGardenGalaxyRemix;
            case WePlantsAreHappyPlantsTimeRemix:
                return wePlantsTimeRemix;
            case ApproachingNirvanaThousandPictures:
                return approachingNirvanaThousandPictures;
            case ApproachingNirvanaNoStringsAttached:
                return approachingNiravanaNoStringsAttached;
            case VendlaSonrisa:
                return vendlaSonrisa;
            case KingPalmRunway:
                return kingPalmRunway;
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
            case Ayasa_The_Reason_Why:
                return ayasaTheReasonWhy;
            case RiskOfDanger:
                return riskOfDanger;
            case mainmenu:
                return this.mainmenu;
            case Player_Laserbeam:
            case Destroyed_Explosion:
            case Alien_Spaceship_Destroyed:
            case Alien_Bomb_Impact:
            case Large_Ship_Destroyed:
            case Default_EMP:
            case Alien_Bomb_Destroyed:
            case ItemAcquired:
            case Rocket_Launcher:
            case Flamethrower:
            case Firewall:
            case NotEnoughMinerals:
            case ChargingLaserbeam:
            case NewPlayerLaserbeam:
            case StickyGrenadeExplosion:
            case PlayerTakesDamage:
            case SpaceStationBlastingOff:
            case SpaceStationChargingUpMovement:
            case SpecialAttackFinishedCharging:
            case GuardianDeath:
            case GuardianBirth:
            case QueenDeath:
            case OverlordDeath:
            default:
                return getAvailableClip(audioType);
            case NONE:
                break;
        }
        return null;
    }


    public List<CustomAudioClip> getAllActiveClips () {
        return allActiveClips;
    }

    public PerformanceLogger getPerformanceLogger () {
        return this.performanceLogger;
    }
}
