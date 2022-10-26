package Data;

import javax.swing.ImageIcon;

public class ImageLoader {

	private static ImageLoader instance = new ImageLoader();

	private ImageLoader() {
	}

	public static ImageLoader getInstance() {
		return instance;
	}

	public java.awt.Image getImage(String imageString) {

		ImageIcon ii = new ImageIcon(convertImageStringToURL(imageString));
		return ii.getImage();
	}

	private String convertImageStringToURL(String imageString){
		switch(imageString) {
			case("spaceship"):
				return "src/resources/images/spaceship.png";
			case("laserbeam"):
				return "src/resources/images/laserbeam.png";
			case("testimage"):
				return "src/resources/images/testimage.jpg";
			case("Alien spaceship"):
				return "src/resources/images/Alien spaceship.png";
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
			case("Player Engine Idle"):
				return "src/resources/images/enginesmoke2.gif";
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
			
		}
		return "notfound";
	}
}
