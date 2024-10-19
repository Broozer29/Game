package VisualAndAudioData.audio;

import VisualAndAudioData.audio.enums.AudioEnums;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
        try {
            URL url = getClass().getResource(convertAudioToFileString(audioFile));
            if (url != null) {
                Media media = new Media(url.toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(false); // Prevent auto-playing the sound
                System.out.println("Loading audio file: " + audioFile);
                return mediaPlayer;
            }
        } catch (Exception e) {
            System.out.println(audioFile);
            e.printStackTrace();
            System.out.println("");

        }
        return null;
    }


    public String convertAudioToFileString (AudioEnums audioFile) {
        switch (audioFile) {
            case Le_Youth_Chills:
                return "/audio/music/Le Youth - Chills.wav";
            case Robert_Nickson_Painting_The_Skies:
                return "/audio/music/Painting The Skies.wav";
            case Player_Laserbeam:
                return "/audio/laserbeam1.wav";
            case StickyGrenadeExplosion:
                return "/audio/StickyGrenadeExplosion.wav";
            case NewPlayerLaserbeam:
                return  "/audio/NewPlayerLaserbeam.wav";
            case PlayerTakesDamage:
                return "/audio/PlayerTakesDamage.wav";
            case Destroyed_Explosion:
                return "/audio/Destroyed Explosion.wav";
            case Alien_Spaceship_Destroyed:
                return "/audio/Alien Spaceship Destroyed.wav";
            case Furi_Make_This_Right:
                return "/audio/music/Furi - Make this right.wav";
            case Furi_Wisdowm_Of_Rage:
                return "/audio/music/Furi - Wisdom of rage.wav";
            case Furi_My_Only_Chance:
                return "/audio/music/Furi - My only chance.wav";
            case Ayasa_The_Reason_Why:
                return "/audio/music/Ayasa - The reason why.wav";
            case Apple_Holder_Remix:
                return "/audio/music/defaultmusic.wav";
            case Alien_Bomb_Impact:
                return "/audio/Alien Bomb Impact.wav";
            case Large_Ship_Destroyed:
                return "/audio/Large Ship Destroyed.wav";
            case Default_EMP:
                return "/audio/Default EMP.wav";
            case Alien_Bomb_Destroyed:
                // DUPLICATE
                return "/audio/Destroyed Explosion.wav";
            case Power_Up_Acquired:
                return "/audio/PowerUpAcquired.wav";
            case Rocket_Launcher:
                return "/audio/Rocket Launcher.wav";
            case Flamethrower:
                return "/audio/Flamethrower.wav";
            case NONE:
                break;
            case Firewall:
                return "/audio/Firewall.wav";
            case New_Arcades_Solace:
                return "/audio/music/NEW ARCADES - SOLACE.wav";
            case mainmenu:
                return "/audio/music/mainmenu.wav";
            case Viq_Rose:
                return "/audio/music/Diq - Rose.wav";
            case Five_Seconds_Before_Sunrise:
                return "/audio/music/5 Seconds Before Sunrise.wav";
            case Downtown_Binary_Astral:
                return "/audio/music/Downtown Binary - Astral.wav";
            case Carpenter_Brut_Enraged:
                return "/audio/music/Carpenter Brut - Enraged.wav";
            case Carpenter_Brut_Youre_Mine:
                return "/audio/music/Carpenter Brut - You're mine.wav";
            case Alpha_Room_Come_Back:
                return "/audio/music/Alpha Room - Come Back.wav";
            case Carpenter_Brut_Danger:
                return "/audio/music/Carpenter Brut - Danger 802 .wav";
            case Knight_Something_Memorable:
                return "/audio/music/Kn1ght - Something Memorable.wav";
            case Downtown_Binary_Fantasia:
                return "/audio/music/Downtown Binary - Fantasia.wav";
            case Downtown_Binary_Light_Cycles:
                return "/audio/music/Downtown Binary - Light Cycles.wav";
            case Marvel_Golden_Dawn:
                return "/audio/music/Marvel83 - Golden Dawn.wav";
            case Tonebox_Memory_Upload:
                return "/audio/music/Tonebox - Memory Uploaded.wav";
            case Forhill_Iris:
                return "/audio/music/Forhill - Iris.wav";
            case Tonebox_Radium_Cloud_Highway:
                return "/audio/music/Tonebox Radium - Cloud Highway.wav";
            case The_Rain_Formerly_Known_As_Purple:
                return "/audio/music/The Rain Formerly Known As Purple.wav";
            case Blood_On_The_Dancefloor:
                return "/audio/music/Blood On The Dancefloor.wav";
            case Lemmino_Firecracker:
                return "/audio/music/lemmino Firecracker.wav";
            case Cannons_Fire_For_You:
                return "/audio/music/Cannons - Fire for You.wav";
            case EMBRZ_Rain_On_My_Window:
                return "/audio/music/EMBRZ - rain on my window.wav";
            case EMBRZ_Light_Falls:
                return "/audio/music/EMBRZ - Light Falls.wav";
            case Mydnyte:
                return "/audio/music/mydnyte.wav";
            case Deadmau5_Monophobia:
                return "/audio/music/deadmau5 - Monophobia.wav";
            case Johny_Theme:
                return "/audio/music/Johny Theme.wav";
            case Viq_Girl_From_Nowhere:
                return "/audio/music/Viq - Girl from Nowhere.wav";
            case Space_Sailors_Cosmos:
                return "/audio/music/Space Sailors - Cosmos.wav";
            case New_Arcades_Severed:
                return "/audio/music/New Arcades - Severed.wav";
            case Arksun_Arisen:
                return "/audio/music/Arksun - Arisen.wav";
            case Ghost_Data_Gods_Of_The_Artificial:
                return "/audio/music/GHOST-DATA-Gods-of-the-Artificial.wav";
            case Ghost_Data_Dark_Harvest:
                return "/audio/music/GHOST-DATA-Dark-Harvest.wav";
            case BlackGummy_SuperHuman:
                return "/audio/music/BlackGummy - SuperHuman.wav";
            case Maduk_Alone:
                return "/audio/music/Maduk - Alone.wav";
            case NotEnoughMinerals:
                return "/audio/Not Enough Minerals.wav";
            case KingPalmRunway:
                return "/audio/music/KING PALM - Runway.wav";
            case ChargingLaserbeam:
                return "/audio/Laserbeam Charging1.wav";
            case SilentAudio:
                return "/audio/silence.wav";
            case VendlaSonrisa:return "/audio/music/Vendla - Sonrisa (Royalty Free Music).wav";
            case ApproachingNirvanaNoStringsAttached: return "/audio/music/Approaching Nirvana - No Strings Attached (Edit).wav";
            case ApproachingNirvanaThousandPictures: return "/audio/music/Approaching Nirvana - Worth a Thousand Pictures.wav";
            case WaveshaperMonster: return "/audio/music/Waveshaper - Monster.wav";
            case TonyLeysSnowdinRemix: return "/audio/music/Tony Leys - Snowdin Shopkeep.wav";
            case GustyGardenGalaxyRemix: return "/audio/music/Gusty Garden Galaxy Remix.wav";
            case WePlantsAreHappyPlantsTimeRemix: return "/audio/music/We Plants Are Happy Plants - Time.wav";
            case BossBattle: return "/audio/music/boss battle.wav";
        }
        return null;
    }
}
