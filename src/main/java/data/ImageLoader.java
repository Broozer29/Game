package data;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Toolkit;

public class ImageLoader {

	private static ImageLoader instance = new ImageLoader();
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();

	private ImageLoader() {
	}

	public static ImageLoader getInstance() {
		return instance;
	}

	public Image getImage(String imageString) {
		ImageIcon ii = new ImageIcon(convertImageStringToURL(imageString));
		return ii.getImage();
	}

	private static String convertImageStringToURL(String imageString) {
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

		}
		return "notfound";
	}
}
