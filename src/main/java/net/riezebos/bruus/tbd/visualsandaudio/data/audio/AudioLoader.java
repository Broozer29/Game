package net.riezebos.bruus.tbd.visualsandaudio.data.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;

public class AudioLoader {

    private static AudioLoader instance = new AudioLoader();
    private final LinkedList<String> recentTracks = new LinkedList<>();
    private static final int MAX_HISTORY_SIZE = 10;
    private static final int MAX_RETRIES = 10;

    private AudioLoader () {

    }

    public static AudioLoader getInstance () {
        return instance;
    }

    // Load the audio file into memory using MediaPlayer for small audio files
    public MediaPlayer getSoundfile(AudioEnums audioFile) {
        try {
            URL url = getClass().getResource(convertAudioToFileString(audioFile));
            if (url != null) {
                Media media = new Media(url.toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(false); // Prevent auto-playing the sound
                return mediaPlayer;
            }
            System.out.println("Failed to load audio file: " + audioFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Helper method to get a random custom music file from /audio/music/custom/ folder.
     * Prevents repeating recently played tracks by maintaining a history of the last 10 tracks.
     * If the folder is empty or doesn't exist, falls back to a random boss song.
     * @return The path to a random custom music file or fallback boss song
     */
    private String getRandomCustomMusicFile() {
        try {
            URL customFolderUrl = getClass().getResource("/audio/music/custom");
            if (customFolderUrl != null) {
                File customFolder = new File(customFolderUrl.toURI());
                if (customFolder.exists() && customFolder.isDirectory()) {
                    File[] wavFiles = customFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));
                    if (wavFiles != null && wavFiles.length > 0) {
                        Random random = new Random();
                        String selectedTrack = null;
                        int attempts = 0;

                        // Try to find a track not in recent history
                        while (attempts < MAX_RETRIES) {
                            File selectedFile = wavFiles[random.nextInt(wavFiles.length)];
                            selectedTrack = "/audio/music/custom/" + selectedFile.getName();

                            if (!recentTracks.contains(selectedTrack)) {
                                break;
                            }
                            attempts++;
                        }

                        // If we couldn't find a non-recent track after MAX_RETRIES, clear history
                        if (attempts >= MAX_RETRIES) {
                            recentTracks.clear();
                            // Pick a fresh random track after clearing history
                            File selectedFile = wavFiles[random.nextInt(wavFiles.length)];
                            selectedTrack = "/audio/music/custom/" + selectedFile.getName();
                        }

                        // Add to history
                        recentTracks.add(selectedTrack);

                        // Maintain max history size (FIFO - remove oldest if exceeds limit)
                        if (recentTracks.size() > MAX_HISTORY_SIZE) {
                            recentTracks.removeFirst();
                        }

                        return selectedTrack;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error accessing custom music folder: " + e.getMessage());
        }
        // Fallback to random boss song if folder is empty or doesn't exist
        return convertAudioToFileString(AudioEnums.getRandomBossSong());
    }

    public String convertAudioToFileString (AudioEnums audioFile) {
        if(audioFile == AudioEnums.CustomMusicFile){
            return getRandomCustomMusicFile();
        }


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
            case Furi_Wisdowm_Of_Rage:
                return "/audio/music/Waveshaper wisdom of rage.wav";
            case FullConfession:
                return "/audio/music/Full Confession.wav";
            case MeatGrinder:
                return "/audio/music/Meat Grinder.wav";
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
            case NONE:
                break;
            case Firewall:
                return "/audio/Firewall.wav";
            case mainmenu:
                return "/audio/music/mainmenu.wav";
            case Filth:
                return "/audio/music/Filth.wav";
            case Blood_On_The_Dancefloor:
                return "/audio/music/Blood_On_The_Dancefloor.wav";
            case MausoleumMash:
                return "/audio/music/Mausoleum mash.wav";
            case Arisen:
                return "/audio/music/Arisen.wav";
            case Enraged:
                return "/audio/music/enraged.wav";
            case Lemmino_Firecracker:
                return "/audio/music/lemmino Firecracker.wav";
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
            case SilentAudio:
                return "/audio/silence.wav";
            case VendlaSonrisa:return "/audio/music/Vendla - Sonrisa (Royalty Free Music).wav";
            case nomad:return "/audio/music/nomad.wav";
            case WaveshaperMonster: return "/audio/music/Waveshaper - Monster.wav";
            case RiskOfDanger: return "/audio/music/Risk of Danger.wav";
            case BossBattle: return "/audio/music/boss battle.wav";
            case RMChase: return "/audio/music/RM Chase.wav";
        }
        return null;
    }
}
