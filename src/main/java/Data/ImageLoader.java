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
			case("alien"):
				return "src/resources/images/testimage.jpg";
			case("StartGame"):
				return "src/resources/images/testimage.jpg";
			case("UserOne"):
				return "src/resources/images/testimage.jpg";
			case("UserTwo"):
				return "src/resources/images/testimage.jpg";
			case("UserThree"):
				return "src/resources/images/testimage.jpg";
			case("SelectUserMenu"):
				return "src/resources/images/testimage.jpg";
			case("userMenuToMainMenu"):
				return "src/resources/images/testimage.jpg";
		}
		return "notfound";
	}
}
