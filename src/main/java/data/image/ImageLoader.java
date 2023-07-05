package data.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {

	private static ImageLoader instance = new ImageLoader();
	private BufferedImage bufferedImage = null;
	private ImageLoader() {
	}

	public static ImageLoader getInstance() {
		return instance;
	}

	public BufferedImage getImage(ImageEnums image) {
		try {
			bufferedImage = ImageIO.read(new File(convertImageStringToURL(image)));
			return bufferedImage;
		} catch (IOException e) {
			System.out.println("Failed to load image: " + image);
			e.printStackTrace();
			return null;
		}
	}
	
	public BufferedImage getSpritesheetImage(String spritesheetImageString) {
		try {
			bufferedImage = ImageIO.read(new File(spritesheetImageString));
			return bufferedImage;
		} catch (IOException e) {
			System.out.println("Failed to load spritesheet image: " + spritesheetImageString);
			e.printStackTrace();
			return null;
		}
	}

	private String convertImageStringToURL(ImageEnums image) {
		switch (image) {
		case Player_Spaceship:
			return "src/resources/images/spaceship.png";
		case Player_Laserbeam:
			return "src/resources/images/laserbeam.png";
		case Test_Image:
			return "src/resources/images/testimage.jpg";
		case Alien:
			return "src/resources/images/Alien spaceship.png";
		case Alien_Bomb:
			return "src/resources/images/Alien bomb.png";
		case Alien_Laserbeam:
			return "src/resources/images/alienlaserbeam.png";
		case Start_Game:
			return "src/resources/images/Start game.png";
		case User_One:
			return "src/resources/images/testimage.jpg";
		case User_Two:
			return "src/resources/images/testimage.jpg";
		case User_Three:
			return "src/resources/images/testimage.jpg";
		case Select_User_Menu:
			return "src/resources/images/Select user.png";
		case User_Menu_To_Main_Menu:
			return "src/resources/images/testimage.jpg";
		case Moon:
			return "src/resources/images/background/moon1.png";
		case Lava_Planet:
			return "src/resources/images/background/lavaplanet1.png";
		case Mars_Planet:
			return "src/resources/images/background/marsplanet1.png";
		case Planet_One:
			return "src/resources/images/background/planet1.png";
		case Planet_Two:
			return "src/resources/images/background/planet2.png";
		case Planet_Three:
			return "src/resources/images/background/planet3.png";
		case Star:
			return "src/resources/images/background/star.png";
		case Seeker:
			return "src/resources/images/Ships/Ship 1/Ship1.png";
		case Tazer:
			return "src/resources/images/Ships/Ship 2/Ship2.png";
		case Energizer:
			return "src/resources/images/Ships/Ship 3/Ship3.png";
		case Bulldozer:
			return "src/resources/images/Ships/Ship 4/Ship4.png";
		case Flamer:
			return "src/resources/images/Ships/Ship 5/Ship5.png";
		case Bomba:
			return "src/resources/images/Ships/Ship 6/Ship6.png";
		case Player_Spaceship_Model_3:
			return "src/resources/images/Ships/Player ships/TM_3_Better_Model_Upgrade.png";
		case Health_Bar:
			return "src/resources/images/UI/HealthBar.png";
		case Shield_Bar:
			return "src/resources/images/UI/ShieldBar.png";
		case Health_Shield_Frames:
			return "src/resources/images/UI/HealthShieldFrames.png";
		case Icon_Border:
			return "src/resources/images/UI/IconFrame.png";
		case Frame:
			return "src/resources/images/UI/Frame.png";
		case Gold_Filling:
			return "src/resources/images/UI/GoldFilling.png";
		case Red_Filling:
			return "src/resources/images/UI/RedFilling.png";
		case Warm_Nebula:
			return "src/resources/images/background/WarmNebula.png";
		case Cold_Nebula:
			return "src/resources/images/background/ColdNebula.png";
		case Regular_Nebula:
			return "src/resources/images/background/RegularNebula.png";
		case Blue_Nebula_1:
			return "src/resources/images/background/Blue Nebula 1 - 1024x1024.png";
		case Blue_Nebula_2:
			return "src/resources/images/background/Blue Nebula 2 - 1024x1024.png";
		case Blue_Nebula_3:
			return "src/resources/images/background/Blue Nebula 3 - 1024x1024.png";
		case Blue_Nebula_4:
			return "src/resources/images/background/Blue Nebula 4 - 1024x1024.png";
		case Blue_Nebula_5:
			return "src/resources/images/background/Blue Nebula 5 - 1024x1024.png";
		case Blue_Nebula_6:
			return "src/resources/images/background/Blue Nebula 6 - 1024x1024.png";
		case Green_Nebula_1:
			return "src/resources/images/background/Green Nebula 1 - 1024x1024.png";
		case Green_Nebula_2:
			return "src/resources/images/background/Green Nebula 2 - 1024x1024.png";
		case Green_Nebula_3:
			return "src/resources/images/background/Green Nebula 3 - 1024x1024.png";
		case Green_Nebula_4:
			return "src/resources/images/background/Green Nebula 4 - 1024x1024.png";
		case Green_Nebula_5:
			return "src/resources/images/background/Green Nebula 5 - 1024x1024.png";
		case Green_Nebula_6:
			return "src/resources/images/background/Green Nebula 6 - 1024x1024.png";
		case Green_Nebula_7:
			return "src/resources/images/background/Green Nebula 7 - 1024x1024.png";
		case Purple_Nebula_1:
			return "src/resources/images/background/Purple Nebula 1 - 1024x1024.png";
		case Purple_Nebula_2:
			return "src/resources/images/background/Purple Nebula 2 - 1024x1024.png";
		case Purple_Nebula_3:
			return "src/resources/images/background/Purple Nebula 3 - 1024x1024.png";
		case Purple_Nebula_4:
			return "src/resources/images/background/Purple Nebula 4 - 1024x1024.png";
		case Purple_Nebula_5:
			return "src/resources/images/background/Purple Nebula 5 - 1024x1024.png";
		case Purple_Nebula_6:
			return "src/resources/images/background/Purple Nebula 6 - 1024x1024.png";
		case Purple_Nebula_7:
			return "src/resources/images/background/Purple Nebula 7 - 1024x1024.png";
		case Parallex_1:
			return "src/resources/images/background/Parallex_1.png";
		case Parallex_2:
			return "src/resources/images/background/Parallex_2.png";
		case Parallex_3:
			return "src/resources/images/background/Parallex_3.png";
		case Parallex_4:
			return "src/resources/images/background/Parallex_4.png";
		case Parallex_5:
			return "src/resources/images/background/Parallex_5.png";
		case Player_EMP_Plus:
			return "src/resources/images/gif/SpriteSheets/Player_Default_EMP.png";
		case TripleShotIcon:
			return  "src/resources/images/Icons/MultiShotIcon.png";
		case DoubleShotIcon:
			return  "src/resources/images/Icons/Double_Shot.png";
		case Starcraft2_Artanis_Shield:
			return  "src/resources/images/Icons/Starcraft2 Artanis Shield.png";
		case Starcraft2_Auto_Tracking:
			return  "src/resources/images/Icons/Starcraft2 Auto Tracking.png";
		case Starcraft2_Blink:
			return  "src/resources/images/Icons/Starcraft2 Blink.png";
		case Starcraft2_Blue_Flame:
			return  "src/resources/images/Icons/Starcraft2 Blue Flame.png";
		case Starcraft2_Concussive_Shells:
			return  "src/resources/images/Icons/Starcraft2 Concussive Shells.png";
		case Starcraft2_Corsair_Cloak:
			return  "src/resources/images/Icons/Starcraft2 Corsair Cloak.png";
		case Starcraft2_Drone_Cloak:
			return  "src/resources/images/Icons/Starcraft2 Drone Cloak.png";
		case Starcraft2_DT_Blink:
			return  "src/resources/images/Icons/Starcraft2 DT Blink.png";
		case Starcraft2_Dual_Rockets:
			return  "src/resources/images/Icons/Starcraft2 Dual Rockets.png";
		case Starcraft2_Energizer_Speed:
			return  "src/resources/images/Icons/Starcraft2 Energizer Speed.png";
		case Starcraft2_Energizer_Speed2:
			return  "src/resources/images/Icons/Starcraft2 Energizer Speed2.png";
		case Starcraft2_Energy_Siphon:
			return  "src/resources/images/Icons/Starcraft2 Energy Siphon.png";
		case Starcraft2_Fire_Cloak:
			return  "src/resources/images/Icons/Starcraft2 Fire Cloak.png";
		case Starcraft2_Fire_Hardened_Shields:
			return  "src/resources/images/Icons/Starcraft2 Fire Hardened Shields.png";
		case Starcraft2_Flame_Turret:
			return  "src/resources/images/Icons/Starcraft2 Flame Turret.png";
		case Starcraft2_Force_Field:
			return  "src/resources/images/Icons/Starcraft2 Force Field.png";
		case Starcraft2_Guardian_Shield:
			return  "src/resources/images/Icons/Starcraft2 Guardian Shield.png";
		case Starcraft2_Hardened_Shields:
			return  "src/resources/images/Icons/Starcraft2 Hardened Shields.png";
		case Starcraft2_Health_Upgrade_1:
			return  "src/resources/images/Icons/Starcraft2 Health Upgrade.png";
		case Starcraft2_Health_Upgrade_2:
			return  "src/resources/images/Icons/Starcraft2 Health Upgrade 2.png";
		case Starcraft2_Ignite_Afterburners:
			return  "src/resources/images/Icons/Starcraft2 Ignite Afterburners.png";
		case Starcraft2_Immortal_Barrier:
			return  "src/resources/images/Icons/Starcraft2 Immortal Barrier.png";
		case Starcraft2_Immortal_Original_Barrier:
			return  "src/resources/images/Icons/Starcraft2 Immortal Original Barrier.png";
		case Starcraft2_LaserBeam:
			return  "src/resources/images/Icons/Starcraft2 LaserBeam.png";
		case Starcraft2_LaserDrill:
			return  "src/resources/images/Icons/Starcraft2 LaserDrill.png";
		case Starcraft2_MovementSpeed:
			return  "src/resources/images/Icons/Starcraft2 Movement Speed.png";
		case Starcraft2_Point_Defense_Drone:
			return  "src/resources/images/Icons/Starcraft2 Point Defense Drone.png";
		case Starcraft2_Protoss_Cloak:
			return  "src/resources/images/Icons/Starcraft2 Protoss Cloak.png";
		case Starcraft2_Protoss_Shield_Disintegrate:
			return  "src/resources/images/Icons/Starcraft2 Protoss Shield Disintegration.png";			
		case Starcraft2_Protoss_Shields_1:
			return  "src/resources/images/Icons/Starcraft2 Protoss Shields1.png";
		case Starcraft2_Protoss_Shields_2:
			return  "src/resources/images/Icons/Starcraft2 Protoss Shields2.png";
		case Starcraft2_Protoss_Shields_3:
			return  "src/resources/images/Icons/Starcraft2 Protoss Shields3.png";
		case Starcraft2_Psi_Storm1:
			return  "src/resources/images/Icons/Starcraft2 Psi Storm1.png";
		case Starcraft2_Psi_Storm2:
			return  "src/resources/images/Icons/Starcraft2 Psi Storm2.png";
		case Starcraft2_Psi_Storm3:
			return  "src/resources/images/Icons/Starcraft2 Psi Storm3.png";
		case Starcraft2_Pulse_Grenade:
			return  "src/resources/images/Icons/Starcraft2 Pulse Grenade.png";
		case Starcraft2_Pulse_Laser:
			return  "src/resources/images/Icons/Starcraft2 Pulse Laser.png";
		case Starcraft2_Repair_Blink:
			return  "src/resources/images/Icons/Starcraft2 Repair Blink.png";
		case Starcraft2_Rocket_Cluster:
			return  "src/resources/images/Icons/Starcraft2 Rocket Cluster.png";
		case Starcraft2_Seeker_Missile:
			return  "src/resources/images/Icons/Starcraft2 Seeker Missile.png";
		case Starcraft2_Shield_Barrier:
			return  "src/resources/images/Icons/Starcraft2 Shield Barrier.png";
		case Starcraft2_Shield_Piercing:
			return  "src/resources/images/Icons/Starcraft2 Shield Piercing.png";
		case Starcraft2_Stim1:
			return  "src/resources/images/Icons/Starcraft2 Stim1.png";
		case Starcraft2_Stim2:
			return  "src/resources/images/Icons/Starcraft2 Stim2.png";
		case Starcraft2_Stim3:
			return  "src/resources/images/Icons/Starcraft2 Stim3.png";
		case Starcraft2_Terran_Plating1:
			return  "src/resources/images/Icons/Starcraft2 Terran Plating 1.png";
		case Starcraft2_Terran_Plating2:
			return  "src/resources/images/Icons/Starcraft2 Terran Plating 2.png";
		case Starcraft2_Terran_Plating3:
			return  "src/resources/images/Icons/Starcraft2 Terran Plating 3.png";
		case Starcraft2_Terran_Speed1:
			return "src/resources/images/Icons/Starcraft2 Terran Speed1.png";
		case Starcraft2_Terran_Speed2:
			return  "src/resources/images/Icons/Starcraft2 Terran Speed2.png";
		case Starcraft2_Terran_Speed3:
			return  "src/resources/images/Icons/Starcraft2 Terran Speed3.png";
		case Starcraft2_Terran_Weapons1:
			return  "src/resources/images/Icons/Starcraft2 Terran Weapons1.png";
		case Starcraft2_Terran_Weapons2:
			return  "src/resources/images/Icons/Starcraft2 Terran Weapons2.png";			
		case Starcraft2_Terran_Weapons3:
			return  "src/resources/images/Icons/Starcraft2 Terran Weapons3.png";
		case Starcraft2_Third_Blink:
			return  "src/resources/images/Icons/Starcraft2 Third Blink.png";
		case Starcraft2_Time_Warp:
			return  "src/resources/images/Icons/Starcraft2 Time Warp.png";
		case Starcraft2_Vespene_Drone:
			return  "src/resources/images/Icons/Starcraft2 Drone Vespene Siphon.png";
		case Starcraft2_Vespene_Gas:
			return  "src/resources/images/Icons/Starcraft2 Vespene Gas.png";
		case Starcraft2_Vespene_Siphon:
			return  "src/resources/images/Icons/Starcraft2 Vespene Siphon.png";
		case Starcraft2_Wraith_Cloak:
			return  "src/resources/images/Icons/Starcraft2 Wraith Cloak.png";
		case Starcraft2_Yellow_Blink:
			return  "src/resources/images/Icons/Starcraft2 Yellow Blink.png";
		case Starcraft2_Heal:
			return  "src/resources/images/Icons/Starcraft2 Heal.png";
	    case Letter_A:
	        return  "src/resources/images/Letters/Letter-A.png";
	    case Letter_B:
	        return  "src/resources/images/Letters/Letter-B.png";
	    case Letter_C:
	        return  "src/resources/images/Letters/Letter-C.png";
	    case Letter_Closing_Bracket:
	        return  "src/resources/images/Letters/Letter-].png";
	    case Letter_Dot:
	    	return "src/resources/images/Letters/Letter-Dot.png";
	    case Letter_D:
	        return  "src/resources/images/Letters/Letter-D.png";
	    case Letter_E:
	        return  "src/resources/images/Letters/Letter-E.png";
	    case Letter_F:
	        return  "src/resources/images/Letters/Letter-F.png";
	    case Letter_G:
	        return  "src/resources/images/Letters/Letter-G.png";
	    case Letter_H:
	        return  "src/resources/images/Letters/Letter-H van Hooi.png";
	    case Letter_I:
	        return  "src/resources/images/Letters/Letter-I.png";
	    case Letter_J:
	        return  "src/resources/images/Letters/Letter-J.png";
	    case Letter_K:
	        return  "src/resources/images/Letters/Letter-K.png";
	    case Letter_L:
	        return  "src/resources/images/Letters/Letter-L.png";
	    case Letter_M:
	        return  "src/resources/images/Letters/Letter-M.png";
	    case Letter_N:
	        return  "src/resources/images/Letters/Letter-N.png";
	    case Letter_O:
	        return  "src/resources/images/Letters/Letter-O.png";
	    case Letter_Open_Bracket:
	        return  "src/resources/images/Letters/Letter-[.png";
	    case Letter_P:
	        return  "src/resources/images/Letters/Letter-P.png";
	    case Letter_Q:
	        return  "src/resources/images/Letters/Letter-Q.png";
	    case Letter_R:
	        return  "src/resources/images/Letters/Letter-R.png";
	    case Letter_S:
	        return  "src/resources/images/Letters/Letter-S.png";
	    case Letter_T:
	        return  "src/resources/images/Letters/Letter-T.png";
	    case Letter_U:
	        return  "src/resources/images/Letters/Letter-U.png";
	    case Letter_V:
	        return  "src/resources/images/Letters/Letter-V.png";
	    case Letter_W:
	        return  "src/resources/images/Letters/Letter-W.png";
	    case Letter_X:
	        return  "src/resources/images/Letters/Letter-X.png";
	    case Letter_Y:
	        return  "src/resources/images/Letters/Letter-Y.png";
	    case Letter_Z:
	        return  "src/resources/images/Letters/Letter-Z.png";
	    case Letter_a:
	        return  "src/resources/images/Letters/Letter-LowercaseA.png";
	    case Letter_b:
	        return  "src/resources/images/Letters/Letter-LowercaseB.png";
	    case Letter_c:
	        return  "src/resources/images/Letters/Letter-LowercaseC.png";
	    case Letter_d:
	        return  "src/resources/images/Letters/Letter-LowercaseD.png";
	    case Letter_double_points:
	        return  "src/resources/images/Letters/Letter-DubbelePunt.png";
	    case Letter_e:
	        return  "src/resources/images/Letters/Letter-LowercaseE.png";
	    case Letter_equals:
	        return  "src/resources/images/Letters/Letter-Equals.png";
	    case Letter_f:
	        return  "src/resources/images/Letters/Letter-LowercaseF.png";
	    case Letter_g:
	        return  "src/resources/images/Letters/Letter-LowercaseG.png";
	    case Letter_greater_than:
	        return  "src/resources/images/Letters/Letter-GreaterThan.png";
	    case Letter_h:
	        return  "src/resources/images/Letters/Letter-LowercaseH.png";
	    case Letter_i:
	        return  "src/resources/images/Letters/Letter-LowercaseI.png";
	    case Letter_j:
	        return  "src/resources/images/Letters/Letter-LowercaseJ.png";
	    case Letter_k:
	        return  "src/resources/images/Letters/Letter-LowercaseK.png";
	    case Letter_l:
	        return  "src/resources/images/Letters/Letter-LowercaseL.png";
	    case Letter_m:
	        return  "src/resources/images/Letters/Letter-LowercaseM.png";
	    case Letter_n:
	        return  "src/resources/images/Letters/Letter-LowercaseN.png";
	    case Letter_o:
	        return  "src/resources/images/Letters/Letter-LowercaseO.png";
	    case Letter_p:
	        return  "src/resources/images/Letters/Letter-LowercaseP.png";
	    case Letter_point_comma:
	        return  "src/resources/images/Letters/Letter-PuntKomma.png";
	    case Letter_q:
	        return  "src/resources/images/Letters/Letter-LowercaseQ.png";
	    case Letter_r:
	        return  "src/resources/images/Letters/Letter-LowercaseR.png";
	    case Letter_s:
	        return  "src/resources/images/Letters/Letter-LowercaseS.png";
	    case Letter_smaller_than:
	        return  "src/resources/images/Letters/Letter-SmallerThan.png";
	    case Letter_t:
	        return  "src/resources/images/Letters/Letter-LowercaseT.png";
	    case Letter_u:
	        return  "src/resources/images/Letters/Letter-LowercaseU.png";
	    case Letter_v:
	        return  "src/resources/images/Letters/Letter-LowercaseV.png";
	    case Letter_w:
	        return  "src/resources/images/Letters/Letter-LowercaseW.png";
	    case Letter_x:
	        return  "src/resources/images/Letters/Letter-LowercaseX.png";
	    case Letter_y:
	        return  "src/resources/images/Letters/Letter-LowercaseY.png";
	    case Letter_z:
	        return  "src/resources/images/Letters/Letter-LowercaseZ.png";
	    case Title_Image:
			return "src/resources/images/TitleImage.png";
	    case Invisible:
	    	return "src/resources/images/invisible.png";
	    case Starcraft2_Firebat_Weapon:
	    	return "src/resources/images/Icons/Starcraft2_Firebat_Weapon.png";
	    case Starcraft2_Electric_Field:
	    	return  "src/resources/images/Icons/Starcraft2 Electric Field.png";
		default:
			break;
		
		}
		return "notfound";
	}
}