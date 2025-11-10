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
    private CustomAudioClip nomad;
    private CustomAudioClip keygen;
    private CustomAudioClip bloodOnTheDanceFloor;
    private CustomAudioClip waveshaperMonster;

    private CustomAudioClip silentAudio;


    private List<AudioEnums> clipsWithThresholds = new ArrayList<>();

    private List<CustomAudioClip> allActiveClips = new ArrayList<>();

    private Map<AudioEnums, List<CustomAudioClip>> audioClipsMap = new HashMap<>();

    private Map<AudioEnums, Integer> clipSizeConfig = new HashMap<>() {{
        put(AudioEnums.Lemmino_Firecracker, 1);
        put(AudioEnums.GodRunDetected, 1);
        put(AudioEnums.VendlaSonrisa, 1);
        put(AudioEnums.mainmenu, 1);
        put(AudioEnums.GenericError, 3);
        put(AudioEnums.ScarabExplosion, 2);
        put(AudioEnums.CoinCollected, 2);
        put(AudioEnums.AchievementUnlocked, 2);
        put(AudioEnums.QueenDeath, 2);
        put(AudioEnums.OverlordDeath, 2);
        put(AudioEnums.MutaliskBirth, 2);
        put(AudioEnums.MutaliskDeath, 3);
        put(AudioEnums.ProtossShipDeath, 5);
        put(AudioEnums.ClassCarrierSlowingDown, 4);
        put(AudioEnums.ClassCarrierSpeedingUp, 2);

        put(AudioEnums.CarrierRdy0, 1);
        put(AudioEnums.CarrierWhat0, 1);
        put(AudioEnums.CarrierWhat1, 1);
        put(AudioEnums.CarrierYes0, 1);
        put(AudioEnums.CarrierYes1, 1);
        put(AudioEnums.CarrierYes2, 1);
        put(AudioEnums.CarrierYes3, 1);

        put(AudioEnums.CaptainMisc0, 1);
        put(AudioEnums.CaptainMisc1, 1);
        put(AudioEnums.CaptainRdy0, 1);
        put(AudioEnums.CaptainWhat0, 1);
        put(AudioEnums.CaptainWhat1, 1);
        put(AudioEnums.CaptainWhat2, 1);
        put(AudioEnums.CaptainWhat3, 1);
        put(AudioEnums.CaptainYes0, 1);
        put(AudioEnums.CaptainYes1, 1);
        put(AudioEnums.CaptainYes2, 1);

        put(AudioEnums.FireFighterMisc0, 1);
        put(AudioEnums.FireFighterMisc1, 1);
        put(AudioEnums.FireFighterYes0, 1);
        put(AudioEnums.FireFighterYes1, 1);
        put(AudioEnums.FireFighterYes2, 1);
        put(AudioEnums.FireFighterYes3, 1);

        put(AudioEnums.DevourerBirth, 3);
        put(AudioEnums.DevourerHit, 3);
        put(AudioEnums.DevourerDeath, 3);
        put(AudioEnums.ScourgeCollision, 3);
        put(AudioEnums.ScourgeDeath, 3);
        put(AudioEnums.ScourgeNoticed, 3);
        put(AudioEnums.GuardianBirth, 3);
        put(AudioEnums.GuardianDeath, 3);
        put(AudioEnums.ChargingLaserbeam, 6);
        put(AudioEnums.Player_Laserbeam, 1);
        put(AudioEnums.Destroyed_Explosion, 5);
        put(AudioEnums.Alien_Bomb_Destroyed, 3);
        put(AudioEnums.Alien_Bomb_Impact, 3);
        put(AudioEnums.Alien_Spaceship_Destroyed, 5);
        put(AudioEnums.Large_Ship_Destroyed, 5);
        put(AudioEnums.Default_EMP, 2);
        put(AudioEnums.GenericSelect, 5);
        put(AudioEnums.Firewall, 2);
        put(AudioEnums.Rocket_Launcher, 3);
        put(AudioEnums.Flamethrower, 1);
        put(AudioEnums.NotEnoughMinerals, 3);
        put(AudioEnums.StickyGrenadeExplosion, 3);
        put(AudioEnums.NewPlayerLaserbeam, 9);
        put(AudioEnums.PlayerTakesDamage, 5);
        put(AudioEnums.SpecialAttackFinishedCharging, 2);
        put(AudioEnums.SpaceStationBlastingOff, 2);
        put(AudioEnums.SpaceStationChargingUpMovement, 2);
    }};

    private AudioDatabase() {
        this.performanceLogger = new PerformanceLogger("Audio Manager");
        initializeAudiofiles();
        resetAudio();
    }

    public static AudioDatabase getInstance() {
        return instance;
    }

    public void resetAudio() {
        for (List<CustomAudioClip> clipList : audioClipsMap.values()) {
            for (CustomAudioClip clip : clipList) {
                clip.setPlaybackPosition(0);
                clip.stopClip();
            }
        }
        allActiveClips.clear();
        performanceLogger.reset();
    }

    private void initializeAudiofiles() {
        clipsWithThresholds.add(AudioEnums.Large_Ship_Destroyed);
        clipsWithThresholds.add(AudioEnums.Destroyed_Explosion);
        clipsWithThresholds.add(AudioEnums.Alien_Bomb_Impact);
        initMusic();
        loadSoundEffects();
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

    private void initMusic() {
        FuriWisdomOfRage = new CustomAudioClip(AudioEnums.Furi_Wisdowm_Of_Rage);
        bloodOnTheDanceFloor = new CustomAudioClip(AudioEnums.Blood_On_The_Dancefloor);
        silentAudio = new CustomAudioClip(AudioEnums.SilentAudio);
        keygen = new CustomAudioClip(AudioEnums.keygen);
        nomad = new CustomAudioClip(AudioEnums.nomad);
        waveshaperMonster = new CustomAudioClip(AudioEnums.WaveshaperMonster);
    }

    private void loadSoundEffects() {
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
        return null; //Soms spelen audio bestanden niet af, deze breakpoint staat hier omdat ik het een keer wil betrappen
    }


    public CustomAudioClip getAudioClip(AudioEnums audioType) {
        switch (audioType) {
            case SilentAudio:
                return silentAudio;
            case Furi_Wisdowm_Of_Rage:
                return FuriWisdomOfRage;
            case Blood_On_The_Dancefloor:
                return bloodOnTheDanceFloor;
            case nomad:
                return this.nomad;
            case keygen:
                return this.keygen;
            case WaveshaperMonster:
                return this.waveshaperMonster;
            case Player_Laserbeam:
            case Destroyed_Explosion:
            case Alien_Spaceship_Destroyed:
            case Alien_Bomb_Impact:
            case Large_Ship_Destroyed:
            case Default_EMP:
            case Alien_Bomb_Destroyed:
            case GenericSelect:
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
            case ProtossShipDeath:
            case ClassCarrierSlowingDown:
            case ClassCarrierSpeedingUp:
            default:
                return getAvailableClip(audioType);
            case NONE:
                break;
        }
        return null;
    }


    public List<CustomAudioClip> getAllActiveClips() {
        return allActiveClips;
    }

    public void removeClipFromActiveClips(CustomAudioClip clip){
        if(allActiveClips.contains(clip)){
            clip.setPlaybackPosition(0);
            clip.stopClip();
            allActiveClips.remove(clip);
        }
    }

    public PerformanceLogger getPerformanceLogger() {
        return this.performanceLogger;
    }
}
