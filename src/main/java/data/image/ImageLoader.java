package data.image;

import javax.swing.ImageIcon;
import java.awt.Image;

public class ImageLoader {

	private static ImageLoader instance = new ImageLoader();

	private ImageLoader() {
	}

	public static ImageLoader getInstance() {
		return instance;
	}

	public Image getImage(String imageString) {
		ImageIcon ii = new ImageIcon(convertImageStringToURL(imageString));
		return ii.getImage();
	}
	
	public Image getSpritesheetImage(String spritesheetImageString) {
		ImageIcon ii = new ImageIcon(spritesheetImageString);
		return ii.getImage();
	}

	private String convertImageStringToURL(String imageString) {
		switch (imageString) {
		case ("Player Spaceship"):
			return "src/resources/images/spaceship.png";
		case ("Player Laserbeam"):
			return "src/resources/images/laserbeam.png";
		case ("testimage"):
			return "src/resources/images/testimage.jpg";
		case ("Default Alien Spaceship"):
			return "src/resources/images/Alien spaceship.png";
		case ("Alien Bomb"):
			return "src/resources/images/Alien bomb.png";
		case ("Alien Laserbeam"):
			return "src/resources/images/alienlaserbeam.png";
		case ("StartGame"):
			return "src/resources/images/Start game.png";
		case ("UserOne"):
			return "src/resources/images/testimage.jpg";
		case ("UserTwo"):
			return "src/resources/images/testimage.jpg";
		case ("UserThree"):
			return "src/resources/images/testimage.jpg";
		case ("SelectUserMenu"):
			return "src/resources/images/Select user.png";
		case ("userMenuToMainMenu"):
			return "src/resources/images/testimage.jpg";
		case ("Moon"):
			return "src/resources/images/background/moon1.png";
		case ("Lava Planet"):
			return "src/resources/images/background/lavaplanet1.png";
		case ("Mars Planet"):
			return "src/resources/images/background/marsplanet1.png";
		case ("Planet One"):
			return "src/resources/images/background/planet1.png";
		case ("Planet Two"):
			return "src/resources/images/background/planet2.png";
		case ("Planet Three"):
			return "src/resources/images/background/planet3.png";
		case ("Star"):
			return "src/resources/images/background/star.png";
		case("Seeker"):
			return "src/resources/images/Ships/Ship 1/Ship1.png";
		case("Tazer"):
			return "src/resources/images/Ships/Ship 2/Ship2.png";
		case("Energizer"):
			return "src/resources/images/Ships/Ship 3/Ship3.png";
		case("Bulldozer"):
			return "src/resources/images/Ships/Ship 4/Ship4.png";
		case("Flamer"):
			return "src/resources/images/Ships/Ship 5/Ship5.png";
		case("Bomba"):
			return "src/resources/images/Ships/Ship 6/Ship6.png";
		case("Model 3 Better Model Upgrade"):
			return "src/resources/images/Ships/Player ships/TM_3_Better_Model_Upgrade.png";
		case("Default Player Engine 1"):
			return "src/resources/images/gif/PNGtoGIF/Default Player Engine/1.png";
		case("Default Player Engine 2"):
			return "src/resources/images/gif/PNGtoGIF/Default Player Engine/2.png";
		case("Default Player Engine 3"):
			return "src/resources/images/gif/PNGtoGIF/Default Player Engine/3.png";
		case("Default Player Engine 4"):
			return "src/resources/images/gif/PNGtoGIF/Default Player Engine/4.png";
		case("Default Player Engine 5"):
			return "src/resources/images/gif/PNGtoGIF/Default Player Engine/5.png";
		case("Default Player Engine Boosted 1"):
			return "src/resources/images/gif/PNGtoGIF/Default Player Engine Boosted/1.png";
		case("Default Player Engine Boosted 2"):
			return "src/resources/images/gif/PNGtoGIF/Default Player Engine Boosted/2.png";
		case("Default Player Engine Boosted 3"):
			return "src/resources/images/gif/PNGtoGIF/Default Player Engine Boosted/3.png";
		case("Default Player Engine Boosted 4"):
			return "src/resources/images/gif/PNGtoGIF/Default Player Engine Boosted/4.png";
		case("Default Player Engine Boosted 5"):
			return "src/resources/images/gif/PNGtoGIF/Default Player Engine Boosted/5.png";
		
		}
		return "notfound";
	}
}
