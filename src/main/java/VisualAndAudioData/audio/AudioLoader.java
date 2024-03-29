package VisualAndAudioData.audio;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioLoader {

    private static AudioLoader instance = new AudioLoader();

    private AudioLoader () {

    }

    public static AudioLoader getInstance () {
        return instance;
    }

    public Clip getSoundfile (AudioEnums audioFile) throws LineUnavailableException {
//		System.out.println("Working Directory = " + System.getProperty("user.dir"));
        try {
            Clip clip = AudioSystem.getClip();
            URL url = getClass().getResource(convertAudioToFileString(audioFile));
//			System.out.println("URL for " + audioFile + ": " + url);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip.open(audioInputStream);
            clip.setFramePosition(0);
            System.out.println("Loading audio file: " + audioFile);
            return clip;

        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String convertAudioToFileString (AudioEnums audioFile) {
        switch (audioFile) {
            case Player_Laserbeam:
                return "/audio/laserbeam1.wav";
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
            case Diq_Rose:
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
        }
        return null;
    }
}
