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
	
	public Image getGif(String imageString) {
	    return toolkit.createImage(convertImageStringToURL(imageString));
	}

	private static String convertImageStringToURL(String imageString){
		switch(imageString) {
			case("spaceship"):
				return "src/resources/images/spaceship.png";
			case("laserbeam"):
				return "src/resources/images/laserbeam.png";
			case("testimage"):
				return "src/resources/images/testimage.jpg";
			case("Alien"):
				return "src/resources/images/Alien spaceship.png";
			case("Alien bomb"):
				return "src/resources/images/Alien bomb.png";
			case("alienlaserbeam"):
				return "src/resources/images/alienlaserbeam.png";
			case("StartGame"):
				return "src/resources/images/Start game.png";
			case("UserOne"):
				return "src/resources/images/testimage.jpg";
			case("UserTwo"):
				return "src/resources/images/testimage.jpg";
			case("UserThree"):
				return "src/resources/images/testimage.jpg";
			case("SelectUserMenu"):
				return "src/resources/images/Select user.png";
			case("userMenuToMainMenu"):
				return "src/resources/images/testimage.jpg";
			case("Impact Explosion One"):
				return "src/resources/images/explosion.gif";
			case("Player Engine"):
				return "src/resources/images/enginesmoke.gif";
			case("Destroyed Explosion"):
				return "src/resources/images/Destroyed Explosion.gif";
			case("Destroyed Explosion Right"):
				return "src/resources/images/Destroyed Explosion Right.gif";
			case("Destroyed Explosion Left"):
				return "src/resources/images/Destroyed Explosion Left.gif";
			case("Destroyed Explosion Reverse"):
				return "src/resources/images/Destroyed Explosion Reverse.gif";
			case("moon1"):
				return "src/resources/images/background/moon1.png";
			case("lavaplanet1"):
				return "src/resources/images/background/lavaplanet1.png";
			case("marsplanet1"):
				return "src/resources/images/background/marsplanet1.png";
			case("planet1"):
				return "src/resources/images/background/planet1.png";
			case("planet2"):
				return "src/resources/images/background/planet2.png";
			case("planet3"):
				return "src/resources/images/background/planet3.png";
			case("star"):
				return "src/resources/images/background/star.png";
			
		}
		return "notfound";
	}
}
