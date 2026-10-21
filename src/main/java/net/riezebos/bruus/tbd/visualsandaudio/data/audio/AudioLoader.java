package net.riezebos.bruus.tbd.visualsandaudio.data.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicTrack;

import java.net.URL;

public class AudioLoader {

    private static AudioLoader instance = new AudioLoader();

    private AudioLoader () {

    }

    public static AudioLoader getInstance () {
        return instance;
    }

    // Load the audio file into memory using MediaPlayer for small audio files
    public MediaPlayer getSoundfile(AudioEnums audioFile) {
        return createMediaPlayer(convertAudioToFileString(audioFile), String.valueOf(audioFile));
    }

    // Builds a player for a song from its own path
    public MediaPlayer getMusicfile(MusicTrack track) {
        return createMediaPlayer(track.getPath(), String.valueOf(track));
    }

    private MediaPlayer createMediaPlayer(String resourcePath, String name) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url != null) {
                Media media = new Media(url.toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(false); // Prevent auto-playing the sound
                return mediaPlayer;
            }
            System.out.println("Failed to load audio file: " + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String convertAudioToFileString (AudioEnums audioFile) {
        switch (audioFile) {
            case CaptainMisc0: return "/audio/selectclass/captain/misc0.wav";
            case CaptainMisc1: return "/audio/selectclass/captain/misc1.wav";
            case CaptainRdy0: return "/audio/selectclass/captain/rdy0.wav";
            case CaptainWhat0: return "/audio/selectclass/captain/what0.wav";
            case CaptainWhat1: return "/audio/selectclass/captain/what1.wav";
            case CaptainWhat2: return "/audio/selectclass/captain/what2.wav";
            case CaptainWhat3: return "/audio/selectclass/captain/what3.wav";
            case CaptainYes0: return "/audio/selectclass/captain/yes0.wav";
            case CaptainYes1: return "/audio/selectclass/captain/yes1.wav";
            case CaptainYes2: return "/audio/selectclass/captain/yes2.wav";

            case CarrierRdy0: return "/audio/selectclass/carrier/rdy0.wav";
            case CarrierWhat0: return "/audio/selectclass/carrier/what0.wav";
            case CarrierWhat1: return "/audio/selectclass/carrier/what1.wav";
            case CarrierYes0: return "/audio/selectclass/carrier/yes0.wav";
            case CarrierYes1: return "/audio/selectclass/carrier/yes1.wav";
            case CarrierYes2: return "/audio/selectclass/carrier/yes2.wav";
            case CarrierYes3: return "/audio/selectclass/carrier/yes3.wav";

            case FireFighterMisc0: return "/audio/selectclass/firefighter/misc0.wav";
            case FireFighterMisc1: return "/audio/selectclass/firefighter/misc1.wav";
            case FireFighterYes0: return "/audio/selectclass/firefighter/yes0.wav";
            case FireFighterYes1: return "/audio/selectclass/firefighter/yes1.wav";
            case FireFighterYes2: return "/audio/selectclass/firefighter/yes2.wav";
            case FireFighterYes3: return "/audio/selectclass/firefighter/yes3.wav";


            case ClassCarrierSlowingDown: return "/audio/carrierslow.wav";
            case ClassCarrierSpeedingUp: return "/audio/carrierfast.wav";
            case ProtossShipDeath: return "/audio/ProtossShipDeath.wav";
            case QueenDeath: return "/audio/Zerg/QueenDeath.wav";
            case MutaliskBirth: return "/audio/Zerg/MutaliskBirth.wav";
            case MutaliskDeath: return "/audio/Zerg/MutaliskDeath.wav";
            case DevourerBirth: return "/audio/Zerg/DevourerBirth.wav";
            case DevourerDeath: return "/audio/Zerg/DevourerDeath.wav";
            case DevourerHit: return "/audio/Zerg/DevourerHit.wav";
            case ScourgeCollision: return "/audio/Zerg/ScourgeCollision.wav";
            case ScourgeDeath: return "/audio/Zerg/ScourgeDeath.wav";
            case ScourgeNoticed: return "/audio/Zerg/ScourgeNoticed.wav";
            case GuardianBirth: return "/audio/Zerg/GuardianBirth.wav";
            case GuardianDeath: return "/audio/Zerg/GuardianDeath.wav";
            case BroodlingAttached: return "/audio/Zerg/BroodlingAttached.wav";
            case OverlordDeath: return "/audio/Zerg/OverlordDeath.wav";
            case FinalBossMock1: return "/audio/finalbossbarks/mock1.wav";
            case FinalBossMock2: return "/audio/finalbossbarks/mock2.wav";
            case FinalBossMock3: return "/audio/finalbossbarks/mock3.wav";
            case FinalBossMock4: return "/audio/finalbossbarks/mock4.wav";
            case FinalBossMock5: return "/audio/finalbossbarks/mock5.wav";
            case FinalBossMock6: return "/audio/finalbossbarks/mock6.wav";
            case FinalBossMock7: return "/audio/finalbossbarks/mock7.wav";
            case FinalBossMock8: return "/audio/finalbossbarks/mock8.wav";
            case FinalBossGreeting: return "/audio/finalbossbarks/greeting.wav";
            case FinalBossTaunt1: return "/audio/finalbossbarks/taunt1.wav";
            case FinalBossTaunt2: return "/audio/finalbossbarks/taunt2.wav";
            case FinalBossTaunt3: return "/audio/finalbossbarks/taunt3.wav";
            case FinalBossTaunt4WhenLowHp: return "/audio/finalbossbarks/taunt4.wav";
            case FinalBossPhaseTransition1: return "/audio/finalbossbarks/phasetransition1.wav";
            case FinalBossPhaseTransition2: return "/audio/finalbossbarks/phasetransition2.wav";
            case FinalBossPhaseTransition3: return "/audio/finalbossbarks/phasetransition3.wav";
            case FinalBossPhaseTransition4: return "/audio/finalbossbarks/phasetransition4.wav";
            case FinalBossPhaseTransition5: return "/audio/finalbossbarks/phasetransition5.wav";
            case FinalBossOh: return "/audio/finalbossbarks/Oh.wav";
            case FinalBossManyBulletsTaunt1: return "/audio/finalbossbarks/manybulletstaunt1.wav";
            case FinalBossManyBulletsTaunt2: return "/audio/finalbossbarks/manybulletstaunt2.wav";
            case FinalBossManyBulletsTaunt3: return "/audio/finalbossbarks/manybulletstaunt3.wav";
            case AchievementUnlocked: return "/audio/achievement.wav";
            case CoinCollected: return "/audio/coin.wav";
            case GenericError: return "/audio/genericerror.wav";
            case DistressCall: return "/audio/distressbeacon.wav";
            case Player_Laserbeam:
                return "/audio/laserbeam1.wav";
            case GodRunDetected:
                return "/audio/godrundetected.wav";
            case StickyGrenadeExplosion:
                return "/audio/StickyGrenadeExplosion.wav";
            case ChargingBigIronLaserbeam:
                return "/audio/ChargingBigIronLaserbeam.wav";
            case NewPlayerLaserbeam:
                return  "/audio/NewPlayerLaserbeam.wav";
            case PlayerTakesDamage:
                return "/audio/PlayerTakesDamage.wav";
            case Destroyed_Explosion:
                return "/audio/Destroyed Explosion.wav";
            case Alien_Spaceship_Destroyed:
                return "/audio/Alien Spaceship Destroyed.wav";
            case Alien_Bomb_Impact:
                return "/audio/Alien Bomb Impact.wav";
            case Large_Ship_Destroyed:
                return "/audio/Large Ship Destroyed.wav";
            case Default_EMP:
                return "/audio/Default EMP.wav";
            case Alien_Bomb_Destroyed:
                // DUPLICATE
                return "/audio/Destroyed Explosion.wav";
            case GenericSelect:
                return "/audio/PowerUpAcquired.wav";
            case Rocket_Launcher:
                return "/audio/Rocket Launcher.wav";
            case Flamethrower:
                return "/audio/Flamethrower.wav";
            case Firewall:
                return "/audio/Firewall.wav";
            case NotEnoughMinerals:
                return "/audio/Not Enough Minerals.wav";
            case ChargingLaserbeam:
                return "/audio/Laserbeam Charging1.wav";
            case SpaceStationBlastingOff:
                return "/audio/SpaceStationBlastingOff.wav";
            case SpaceStationChargingUpMovement:
                return "/audio/SpaceStationChargingUpMovement.wav";
            case SpecialAttackFinishedCharging:
                return "/audio/ElectroShredFinishedCharging.wav";
            case ScarabExplosion:
                return "/audio/scarabexplosion.wav";
        }
        return null;
    }
}
