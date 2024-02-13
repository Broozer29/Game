package VisualAndAudioData.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageLoader {

    private static ImageLoader instance = new ImageLoader();
    private BufferedImage bufferedImage = null;

    private ImageLoader () {
    }

    public static ImageLoader getInstance () {
        return instance;
    }


    public BufferedImage getImage (ImageEnums image) {
        try {
            String path = convertImageStringToURL(image);
            InputStream stream = this.getClass().getResourceAsStream(path);
            if (stream == null) {
                System.out.println("InputStream is null for path: " + path);
                return null;
            } else {
                System.out.println("Loading image: " + path);
            }
            bufferedImage = ImageIO.read(stream);
            if (bufferedImage == null) {
                System.out.println("Loading of path failed: " + path);
                return null;
            }
//			System.out.println("Loaded image: " + image + " this should not be 0 or 'null' -> " + bufferedImage.getWidth());
            return bufferedImage;
        } catch (IOException e) {
            System.out.println("Failed to load image: " + image);
            e.printStackTrace();
            return null;
        }
    }

    public BufferedImage getSpritesheetImage (String spritesheetImageString) {
        try {
            InputStream stream = this.getClass().getResourceAsStream(spritesheetImageString);
            if (stream == null) {
                System.out.println("InputStream is null for path: " + spritesheetImageString);
                return null;
            }
            bufferedImage = ImageIO.read(stream);
            if (bufferedImage == null) {
                System.out.println("This one is broken: " + " with a path of " + spritesheetImageString);
                return null;
            }
            return bufferedImage;
        } catch (IOException e) {
            System.out.println("Failed to load spritesheet image: " + spritesheetImageString);
            e.printStackTrace();
            return null;
        }
    }

    public BufferedImage getSpritesheetImageFromStream (InputStream stream) {
        try {
            return ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String convertImageStringToURL (ImageEnums image) {
        switch (image) {
            case Player_Spaceship:
                return "/images/spaceship.png";
            case Player_Laserbeam:
                return "/images/laserbeam.png";
            case Test_Image:
                return "/images/testimage.jpg";
            case Alien:
                return "/images/Alien spaceship.png";
            case Alien_Bomb:
                return "/images/Alien bomb.png";
            case Alien_Laserbeam:
                return "/images/alienlaserbeam.png";
            case Start_Game:
                return "/images/Start game.png";
            case User_One:
                return "/images/testimage.jpg";
            case User_Two:
                return "/images/testimage.jpg";
            case User_Three:
                return "/images/testimage.jpg";
            case Select_User_Menu:
                return "/images/Select user.png";
            case User_Menu_To_Main_Menu:
                return "/images/testimage.jpg";
            case Moon:
                return "/images/background/moon1.png";
            case Lava_Planet:
                return "/images/background/lavaplanet1.png";
            case Mars_Planet:
                return "/images/background/marsplanet1.png";
            case Planet_One:
                return "/images/background/planet1.png";
            case Planet_Two:
                return "/images/background/planet2.png";
            case Planet_Three:
                return "/images/background/planet3.png";
            case Star:
                return "/images/background/star.png";
            case Seeker:
                return "/images/Ships/Ship 1/Ship1.png";
            case Tazer:
                return "/images/Ships/Ship 2/Ship2.png";
            case Energizer:
                return "/images/Ships/Ship 3/Ship3.png";
            case Bulldozer:
                return "/images/Ships/Ship 4/Ship4.png";
            case Flamer:
                return "/images/Ships/Ship 5/Ship5.png";
            case Bomba:
                return "/images/Ships/Ship 6/Ship6.png";
            case Player_Spaceship_Model_3:
                return "/images/Ships/Player Ships/TM_3_Better_Model_Upgrade.png";
            case Health_Bar:
                return "/images/UI/HealthBar.png";
            case Shield_Bar:
                return "/images/UI/ShieldBar.png";
            case Health_Shield_Frames:
                return "/images/UI/HealthShieldFrames.png";
            case Icon_Border:
                return "/images/UI/IconFrame.png";
            case Frame:
                return "/images/UI/Frame.png";
            case Gold_Filling:
                return "/images/UI/GoldFilling.png";
            case Blue_Filling:
                return "/images/UI/BlueFilling.png";
            case Red_Filling:
                return "/images/UI/RedFilling.png";
            case Warm_Nebula:
                return "/images/background/WarmNebula.png";
            case Cold_Nebula:
                return "/images/background/ColdNebula.png";
            case Regular_Nebula:
                return "/images/background/RegularNebula.png";
            case Blue_Nebula_1:
                return "/images/background/Blue Nebula 1 - 1024x1024.png";
            case Blue_Nebula_2:
                return "/images/background/Blue Nebula 2 - 1024x1024.png";
            case Blue_Nebula_3:
                return "/images/background/Blue Nebula 3 - 1024x1024.png";
            case Blue_Nebula_4:
                return "/images/background/Blue Nebula 4 - 1024x1024.png";
            case Blue_Nebula_5:
                return "/images/background/Blue Nebula 5 - 1024x1024.png";
            case Blue_Nebula_6:
                return "/images/background/Blue Nebula 6 - 1024x1024.png";
            case Green_Nebula_1:
                return "/images/background/Green Nebula 1 - 1024x1024.png";
            case Green_Nebula_2:
                return "/images/background/Green Nebula 2 - 1024x1024.png";
            case Green_Nebula_3:
                return "/images/background/Green Nebula 3 - 1024x1024.png";
            case Green_Nebula_4:
                return "/images/background/Green Nebula 4 - 1024x1024.png";
            case Green_Nebula_5:
                return "/images/background/Green Nebula 5 - 1024x1024.png";
            case Green_Nebula_6:
                return "/images/background/Green Nebula 6 - 1024x1024.png";
            case Green_Nebula_7:
                return "/images/background/Green Nebula 7 - 1024x1024.png";
            case Purple_Nebula_1:
                return "/images/background/Purple Nebula 1 - 1024x1024.png";
            case Purple_Nebula_2:
                return "/images/background/Purple Nebula 2 - 1024x1024.png";
            case Purple_Nebula_3:
                return "/images/background/Purple Nebula 3 - 1024x1024.png";
            case Purple_Nebula_4:
                return "/images/background/Purple Nebula 4 - 1024x1024.png";
            case Purple_Nebula_5:
                return "/images/background/Purple Nebula 5 - 1024x1024.png";
            case Purple_Nebula_6:
                return "/images/background/Purple Nebula 6 - 1024x1024.png";
            case Purple_Nebula_7:
                return "/images/background/Purple Nebula 7 - 1024x1024.png";
            case Parallex_1:
                return "/images/background/Parallex_1.png";
            case Parallex_2:
                return "/images/background/Parallex_2.png";
            case Parallex_3:
                return "/images/background/Parallex_3.png";
            case Parallex_4:
                return "/images/background/Parallex_4.png";
            case Parallex_5:
                return "/images/background/Parallex_5.png";
            case Player_EMP_Plus:
                return "/images/gif/SpriteSheets/Player_Default_EMP.png";
            case TripleShotIcon:
                return "/images/Icons/MultiShotIcon.png";
            case DoubleShotIcon:
                return "/images/Icons/Double_Shot.png";
            case Starcraft2_Artanis_Shield:
                return "/images/Icons/Starcraft2 Artanis Shield.png";
            case Starcraft2_Auto_Tracking:
                return "/images/Icons/Starcraft2 Auto Tracking.png";
            case Starcraft2_Blink:
                return "/images/Icons/Starcraft2 Blink.png";
            case Starcraft2_Blue_Flame:
                return "/images/Icons/Starcraft2 Blue Flame.png";
            case Starcraft2_Concussive_Shells:
                return "/images/Icons/Starcraft2 Concussive Shells.png";
            case Starcraft2_Corsair_Cloak:
                return "/images/Icons/Starcraft2 Corsair Cloak.png";
            case Starcraft2_Drone_Cloak:
                return "/images/Icons/Starcraft2 Drone Cloak.png";
            case Starcraft2_DT_Blink:
                return "/images/Icons/Starcraft2 DT Blink.png";
            case Starcraft2_Dual_Rockets:
                return "/images/Icons/Starcraft2 Dual Rockets.png";
            case Starcraft2_Energizer_Speed:
                return "/images/Icons/Starcraft2 Energizer Speed.png";
            case Starcraft2_Energizer_Speed2:
                return "/images/Icons/Starcraft2 Energizer Speed2.png";
            case Starcraft2_Energy_Siphon:
                return "/images/Icons/Starcraft2 Energy Siphon.png";
            case Starcraft2_Fire_Cloak:
                return "/images/Icons/Starcraft2 Fire Cloak.png";
            case Starcraft2_Fire_Hardened_Shields:
                return "/images/Icons/Starcraft2 Fire Hardened Shields.png";
            case Starcraft2_Flame_Turret:
                return "/images/Icons/Starcraft2 Flame Turret.png";
            case Starcraft2_Force_Field:
                return "/images/Icons/Starcraft2 Force Field.png";
            case Starcraft2_Guardian_Shield:
                return "/images/Icons/Starcraft2 Guardian Shield.png";
            case Starcraft2_Hardened_Shields:
                return "/images/Icons/Starcraft2 Hardened Shields.png";
            case Starcraft2_Health_Upgrade_1:
                return "/images/Icons/Starcraft2 Health Upgrade.png";
            case Starcraft2_Health_Upgrade_2:
                return "/images/Icons/Starcraft2 Health Upgrade 2.png";
            case Starcraft2_Ignite_Afterburners:
                return "/images/Icons/Starcraft2 Ignite Afterburners.png";
            case Starcraft2_Immortal_Barrier:
                return "/images/Icons/Starcraft2 Immortal Barrier.png";
            case Starcraft2_Immortal_Original_Barrier:
                return "/images/Icons/Starcraft2 Immortal Original Barrier.png";
            case Starcraft2_LaserBeam:
                return "/images/Icons/Starcraft2 LaserBeam.png";
            case Starcraft2_LaserDrill:
                return "/images/Icons/Starcraft2 LaserDrill.png";
            case Starcraft2_MovementSpeed:
                return "/images/Icons/Starcraft2 Movement Speed.png";
            case Starcraft2_Point_Defense_Drone:
                return "/images/Icons/Starcraft2 Point Defense Drone.png";
            case Starcraft2_Protoss_Cloak:
                return "/images/Icons/Starcraft2 Protoss Cloak.png";
            case Starcraft2_Protoss_Shield_Disintegrate:
                return "/images/Icons/Starcraft2 Protoss Shield Disintegration.png";
            case Starcraft2_Protoss_Shields_1:
                return "/images/Icons/Starcraft2 Protoss Shields1.png";
            case Starcraft2_Protoss_Shields_2:
                return "/images/Icons/Starcraft2 Protoss Shields2.png";
            case Starcraft2_Protoss_Shields_3:
                return "/images/Icons/Starcraft2 Protoss Shields3.png";
            case Starcraft2_Psi_Storm1:
                return "/images/Icons/Starcraft2 Psi Storm1.png";
            case Starcraft2_Psi_Storm2:
                return "/images/Icons/Starcraft2 Psi Storm2.png";
            case Starcraft2_Psi_Storm3:
                return "/images/Icons/Starcraft2 Psi Storm3.png";
            case Starcraft2_Pulse_Grenade:
                return "/images/Icons/Starcraft2 Pulse Grenade.png";
            case Starcraft2_Pulse_Laser:
                return "/images/Icons/Starcraft2 Pulse Laser.png";
            case Starcraft2_Repair_Blink:
                return "/images/Icons/Starcraft2 Repair Blink.png";
            case Starcraft2_Rocket_Cluster:
                return "/images/Icons/Starcraft2 Rocket Cluster.png";
            case Starcraft2_Seeker_Missile:
                return "/images/Icons/Starcraft2 Seeker Missile.png";
            case Starcraft2_Shield_Barrier:
                return "/images/Icons/Starcraft2 Shield Barrier.png";
            case Starcraft2_Shield_Piercing:
                return "/images/Icons/Starcraft2 Shield Piercing.png";
            case Starcraft2_Stim1:
                return "/images/Icons/Starcraft2 Stim1.png";
            case Starcraft2_Stim2:
                return "/images/Icons/Starcraft2 Stim2.png";
            case Starcraft2_Stim3:
                return "/images/Icons/Starcraft2 Stim3.png";
            case Starcraft2_Terran_Plating1:
                return "/images/Icons/Starcraft2 Terran Plating 1.png";
            case Starcraft2_Terran_Plating2:
                return "/images/Icons/Starcraft2 Terran Plating 2.png";
            case Starcraft2_Terran_Plating3:
                return "/images/Icons/Starcraft2 Terran Plating 3.png";
            case Starcraft2_Terran_Speed1:
                return "/images/Icons/Starcraft2 Terran Speed1.png";
            case Starcraft2_Terran_Speed2:
                return "/images/Icons/Starcraft2 Terran Speed2.png";
            case Starcraft2_Terran_Speed3:
                return "/images/Icons/Starcraft2 Terran Speed3.png";
            case Starcraft2_Terran_Weapons1:
                return "/images/Icons/Starcraft2 Terran Weapons1.png";
            case Starcraft2_Terran_Weapons2:
                return "/images/Icons/Starcraft2 Terran Weapons2.png";
            case Starcraft2_Terran_Weapons3:
                return "/images/Icons/Starcraft2 Terran Weapons3.png";
            case Starcraft2_Third_Blink:
                return "/images/Icons/Starcraft2 Third Blink.png";
            case Starcraft2_Time_Warp:
                return "/images/Icons/Starcraft2 Time Warp.png";
            case Starcraft2_Vespene_Drone:
                return "/images/Icons/Starcraft2 Drone Vespene Siphon.png";
            case Starcraft2_Vespene_Gas:
                return "/images/Icons/Starcraft2 Vespene Gas.png";
            case Starcraft2_Vespene_Siphon:
                return "/images/Icons/Starcraft2 Vespene Siphon.png";
            case Starcraft2_Wraith_Cloak:
                return "/images/Icons/Starcraft2 Wraith Cloak.png";
            case Starcraft2_Yellow_Blink:
                return "/images/Icons/Starcraft2 Yellow Blink.png";
            case Starcraft2_Heal:
                return "/images/Icons/Starcraft2 Heal.png";
            case Starcraft2_Electric_Field:
                return "/images/Icons/Starcraft2 Electric Field.png";
            case Starcraft2_Firebat_Weapon:
                return "/images/Icons/Starcraft2 Firebat Weapon.png";
            case Letter_A:
                return "/images/Letters/Letter-A.png";
            case Letter_B:
                return "/images/Letters/Letter-B.png";
            case Letter_C:
                return "/images/Letters/Letter-C.png";
            case Letter_Closing_Bracket:
                return "/images/Letters/Letter-].png";
            case Letter_Dot:
                return "/images/Letters/Letter-Dot.png";
            case Letter_D:
                return "/images/Letters/Letter-D.png";
            case Letter_E:
                return "/images/Letters/Letter-E.png";
            case Letter_F:
                return "/images/Letters/Letter-F.png";
            case Letter_G:
                return "/images/Letters/Letter-G.png";
            case Letter_H:
                return "/images/Letters/Letter-H van Hooi.png";
            case Letter_I:
                return "/images/Letters/Letter-I.png";
            case Letter_J:
                return "/images/Letters/Letter-J.png";
            case Letter_K:
                return "/images/Letters/Letter-K.png";
            case Letter_L:
                return "/images/Letters/Letter-L.png";
            case Letter_M:
                return "/images/Letters/Letter-M.png";
            case Letter_N:
                return "/images/Letters/Letter-N.png";
            case Letter_O:
                return "/images/Letters/Letter-O.png";
            case Letter_Open_Bracket:
                return "/images/Letters/Letter-[.png";
            case Letter_P:
                return "/images/Letters/Letter-P.png";
            case Letter_Q:
                return "/images/Letters/Letter-Q.png";
            case Letter_R:
                return "/images/Letters/Letter-R.png";
            case Letter_S:
                return "/images/Letters/Letter-S.png";
            case Letter_T:
                return "/images/Letters/Letter-T.png";
            case Letter_U:
                return "/images/Letters/Letter-U.png";
            case Letter_V:
                return "/images/Letters/Letter-V.png";
            case Letter_W:
                return "/images/Letters/Letter-W.png";
            case Letter_X:
                return "/images/Letters/Letter-X.png";
            case Letter_Y:
                return "/images/Letters/Letter-Y.png";
            case Letter_Z:
                return "/images/Letters/Letter-Z.png";
            case Letter_a:
                return "/images/Letters/Letter-LowercaseA.png";
            case Letter_b:
                return "/images/Letters/Letter-LowercaseB.png";
            case Letter_c:
                return "/images/Letters/Letter-LowercaseC.png";
            case Letter_d:
                return "/images/Letters/Letter-LowercaseD.png";
            case Letter_double_points:
                return "/images/Letters/Letter-DubbelePunt.png";
            case Letter_e:
                return "/images/Letters/Letter-LowercaseE.png";
            case Letter_equals:
                return "/images/Letters/Letter-Equals.png";
            case Letter_f:
                return "/images/Letters/Letter-LowercaseF.png";
            case Letter_g:
                return "/images/Letters/Letter-LowercaseG.png";
            case Letter_greater_than:
                return "/images/Letters/Letter-GreaterThan.png";
            case Letter_h:
                return "/images/Letters/Letter-LowercaseH.png";
            case Letter_i:
                return "/images/Letters/Letter-LowercaseI.png";
            case Letter_j:
                return "/images/Letters/Letter-LowercaseJ.png";
            case Letter_k:
                return "/images/Letters/Letter-LowercaseK.png";
            case Letter_l:
                return "/images/Letters/Letter-LowercaseL.png";
            case Letter_m:
                return "/images/Letters/Letter-LowercaseM.png";
            case Letter_n:
                return "/images/Letters/Letter-LowercaseN.png";
            case Letter_o:
                return "/images/Letters/Letter-LowercaseO.png";
            case Letter_p:
                return "/images/Letters/Letter-LowercaseP.png";
            case Letter_point_comma:
                return "/images/Letters/Letter-PuntKomma.png";
            case Letter_q:
                return "/images/Letters/Letter-LowercaseQ.png";
            case Letter_r:
                return "/images/Letters/Letter-LowercaseR.png";
            case Letter_s:
                return "/images/Letters/Letter-LowercaseS.png";
            case Letter_smaller_than:
                return "/images/Letters/Letter-SmallerThan.png";
            case Letter_t:
                return "/images/Letters/Letter-LowercaseT.png";
            case Letter_u:
                return "/images/Letters/Letter-LowercaseU.png";
            case Letter_v:
                return "/images/Letters/Letter-LowercaseV.png";
            case Letter_w:
                return "/images/Letters/Letter-LowercaseW.png";
            case Letter_x:
                return "/images/Letters/Letter-LowercaseX.png";
            case Letter_y:
                return "/images/Letters/Letter-LowercaseY.png";
            case Letter_z:
                return "/images/Letters/Letter-LowercaseZ.png";
            case Title_Image:
                return "/images/TitleImage.png";
            case Long_Card:
                return "/images/UI/Cards/Long Card.png";
            case Square_Card:
                return "/images/UI/Cards/Square Card.png";
            case Invisible:
                System.out.println("Loading a star.png for INVISIBLE");
                return "/images/background/star.png";
//			return "/images/invisible.png";
            case Wide_Card:
                return "/images/UI/Cards/Wide Card.png";
            case Letter_Percentage:
                return "/images/Letters/percent.png";
            case Letter_Komma:
                return "/images/Letters/komma.png";
            case Letter_One:
                return "/images/Letters/Letter_One.png";
            case Letter_Two:
                return "/images/Letters/Letter_Two.png";
            case Letter_Three:
                return "/images/Letters/Letter_Three.png";
            case Letter_Four:
                return "/images/Letters/Letter_Four.png";
            case Letter_Five:
                return "/images/Letters/Letter_Five.png";
            case Letter_Six:
                return "/images/Letters/Letter_Six.png";
            case Letter_Seven:
                return "/images/Letters/Letter_Seven.png";
            case Letter_Eight:
                return "/images/Letters/Letter_Eight.png";
            case Letter_Nine:
                return "/images/Letters/Letter_Nine.png";
            case Letter_Zero:
                return "/images/Letters/Letter_Zero.png";
            case Starcraft2_Advanced_Optics:
                return "/images/Icons/Starcraft2 AdvancedOptics.png";
            case CannisterOfGasoline:
                return "/images/Icons/CannisterOfGasoline.png";
            case Starcraft2_Battery:
                return "/images/Icons/Starcraft2 Battery.png";
            case Starcraft2_Focused_Crystal:
                return "/images/Icons/Starcraft2 FocusedCrystal.png";
            case Starcraft2_Platinum_Sponge:
                return  "/images/Icons/Starcraft2 Platinum Sponge.png";
            case Starcraft2_Overclock:
                return  "/images/Icons/Starcraft2 Overclock.png";
            case Starcraft2_Armor_Piercing:
                return  "/images/Icons/Starcraft2 Armor Piercing.png";
            case MoneyPrinter:
                return  "/images/Icons/moneyprinter.png";
            case StickyDynamite:
                return  "/images/Icons/stickydynamite.png";
            case Star_Blue1:
                return "/images/background/LargeStars/star_blue01.png";
            case Star_Blue2:
                return "/images/background/LargeStars/star_blue02.png";
            case Star_Blue3:
                return "/images/background/LargeStars/star_blue03.png";
            case Star_Blue4:
                return "/images/background/LargeStars/star_blue04.png";
            case Star_Orange1:
                return "/images/background/LargeStars/star_orange01.png";
            case Star_Orange2:
                return "/images/background/LargeStars/star_orange02.png";
            case Star_Orange3:
                return "/images/background/LargeStars/star_orange03.png";
            case Star_Orange4:
                return "/images/background/LargeStars/star_orange04.png";
            case Star_Yellow1:
                return "/images/background/LargeStars/star_yellow01.png";
            case Star_Yellow2:
                return "/images/background/LargeStars/star_yellow02.png";
            case Star_Yellow3:
                return "/images/background/LargeStars/star_yellow03.png";
            case Star_Yellow4:
                return "/images/background/LargeStars/star_yellow04.png";
            case Star_White1:
                return "/images/background/LargeStars/star_white01.png";
            case Star_White2:
                return "/images/background/LargeStars/star_white02.png";
            case Star_White3:
                return "/images/background/LargeStars/star_white03.png";
            case Star_White4:
                return "/images/background/LargeStars/star_white04.png";
            case Star_Red1:
                return "/images/background/LargeStars/star_red01.png";
            case Star_Red2:
                return "/images/background/LargeStars/star_red02.png";
            case Star_Red3:
                return "/images/background/LargeStars/star_red03.png";
            case Star_Red4:
                return "/images/background/LargeStars/star_red04.png";
            case Moon2:
                return "/images/background/Moon 2.png";
            case Moon3:
                return "/images/background/Moon 3.png";
            case Moon4:
                return "/images/background/Moon 4.png";
            case Moon5:
                return "/images/background/Moon 5.png";
            case GreenPlanet1:
                return "/images/background/Green planet 1.png";
            case GreenPlanet2:
                return "/images/background/Green planet 2.png";
            case BluePlanet1:
                return "/images/background/Blue planet 1.png";
            case BluePlanet2:
                return "/images/background/Blue planet 2.png";
            case BluePlanet3:
                return "/images/background/Blue planet 3.png";
            case BluePlanet4:
                return "/images/background/Blue planet 4.png";
            case BluePlanet5:
                return "/images/background/Blue planet 5.png";
            case BluePlanet6:
                return "/images/background/Blue planet 6.png";
            default:
                return "";
        }
    }

}