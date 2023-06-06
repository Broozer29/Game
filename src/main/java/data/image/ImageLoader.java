package data.image;

import java.awt.Image;

import javax.swing.ImageIcon;

import data.image.enums.ImageEnums;

public class ImageLoader {

	private static ImageLoader instance = new ImageLoader();

	private ImageLoader() {
	}

	public static ImageLoader getInstance() {
		return instance;
	}

	public Image getImage(ImageEnums image) {
		ImageIcon ii = new ImageIcon(convertImageStringToURL(image));
		return ii.getImage();
	}
	
	public Image getSpritesheetImage(String spritesheetImageString) {
		ImageIcon ii = new ImageIcon(spritesheetImageString);
		return ii.getImage();
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
		default:
			break;
			
			
			//Can be safely deleted?
//		case("Default Player Engine 1"):
//			return "src/resources/images/gif/PNGtoGIF/Default Player Engine/1.png";
//		case("Default Player Engine 2"):
//			return "src/resources/images/gif/PNGtoGIF/Default Player Engine/2.png";
//		case("Default Player Engine 3"):
//			return "src/resources/images/gif/PNGtoGIF/Default Player Engine/3.png";
//		case("Default Player Engine 4"):
//			return "src/resources/images/gif/PNGtoGIF/Default Player Engine/4.png";
//		case("Default Player Engine 5"):
//			return "src/resources/images/gif/PNGtoGIF/Default Player Engine/5.png";
//		case("Default Player Engine Boosted 1"):
//			return "src/resources/images/gif/PNGtoGIF/Default Player Engine Boosted/1.png";
//		case("Default Player Engine Boosted 2"):
//			return "src/resources/images/gif/PNGtoGIF/Default Player Engine Boosted/2.png";
//		case("Default Player Engine Boosted 3"):
//			return "src/resources/images/gif/PNGtoGIF/Default Player Engine Boosted/3.png";
//		case("Default Player Engine Boosted 4"):
//			return "src/resources/images/gif/PNGtoGIF/Default Player Engine Boosted/4.png";
//		case("Default Player Engine Boosted 5"):
//			return "src/resources/images/gif/PNGtoGIF/Default Player Engine Boosted/5.png";
		
		}
		return "notfound";
	}
}
