package data;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;

public class ImageDatabase {

	private static ImageDatabase instance = new ImageDatabase();
	private ImageLoader imgLoader = ImageLoader.getInstance();

	// Friendly images
	private Image spaceShipImage;

	// Enemy images
	private Image alienSpaceshipImage;
	private Image alienBombImage;

	// Projectile images
	private Image alienLaserbeamImage;
	private Image laserBeamImage;

	// Menu images
	private Image startGameImage;
	private Image userOneImage;
	private Image userTwoImage;
	private Image userThreeImage;
	private Image selectUserMenuImage;
	private Image userMenuToMainMenu;

	// Animations
	private List<ImageIcon> impactExplosionOneFrames = new ArrayList<ImageIcon>();
	private List<ImageIcon> playerEngineFrames = new ArrayList<ImageIcon>();
	private List<ImageIcon> destroyedExplosionUpFrames = new ArrayList<ImageIcon>();
	private List<ImageIcon> destroyedExplosionLeftFrames = new ArrayList<ImageIcon>();
	private List<ImageIcon> destroyedExplosionRightFrames = new ArrayList<ImageIcon>();
	private List<ImageIcon> destroyedExplosionDownFrames = new ArrayList<ImageIcon>();

	// Background images
	private Image moon;
	private Image lavaPlanet;
	private Image marsPlanet;
	private Image planetOne;
	private Image planetTwo;
	private Image planetThree;
	private Image star;

	// testimages
	private Image testImage;

	private ImageDatabase() {
		initializeImages();
	}

	public static ImageDatabase getInstance() {
		return instance;
	}

	private void initializeImages() {
		this.initFriendlies();
		try {
			this.initAnimations();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.initBackgroundObjects();
		this.initEnemies();
		this.initMenuImages();
		this.initProjectiles();
	}

	private void initFriendlies() {
		this.spaceShipImage = imgLoader.getImage("Player Spaceship");
	}

	private void initEnemies() {
		this.alienSpaceshipImage = imgLoader.getImage("Default Alien Spaceship");
		this.alienBombImage = imgLoader.getImage("Alien Bomb");
	}

	private void initProjectiles() {
		this.laserBeamImage = imgLoader.getImage("Player Laserbeam");
		this.alienLaserbeamImage = imgLoader.getImage("Alien Laserbeam");
	}

	private void initMenuImages() {
		this.startGameImage = imgLoader.getImage("StartGame");
		this.userOneImage = imgLoader.getImage("UserOne");
		this.userTwoImage = imgLoader.getImage("UserTwo");
		this.userThreeImage = imgLoader.getImage("UserThree");
		this.selectUserMenuImage = imgLoader.getImage("SelectUserMenu");
		this.testImage = imgLoader.getImage("testimage");
		this.userMenuToMainMenu = imgLoader.getImage("userMenuToMainMenu");
	}

	private void initBackgroundObjects() {
		this.moon = imgLoader.getImage("Moon");
		this.lavaPlanet = imgLoader.getImage("Lava Planet");
		this.planetOne = imgLoader.getImage("Planet One");
		this.planetTwo = imgLoader.getImage("Planet Two");
		this.planetThree = imgLoader.getImage("Planet Three");
		this.marsPlanet = imgLoader.getImage("Mars Planet");
		this.star = imgLoader.getImage("Star");
	}

	public Image getImage(String imageType) {
		switch (imageType) {
		case ("Player Spaceship"):
			return this.spaceShipImage;
		case ("Player Laserbeam"):
			return this.laserBeamImage;
		case ("Default Alien Spaceship"):
			return this.alienSpaceshipImage;
		case ("Alien Bomb"):
			return this.alienBombImage;
		case ("Alien Laserbeam"):
			return this.alienLaserbeamImage;
		case ("StartGame"):
			return this.startGameImage;
		case ("UserOne"):
			return this.userOneImage;
		case ("UserTwo"):
			return this.userTwoImage;
		case ("UserThree"):
			return this.userThreeImage;
		case ("SelectUserMenu"):
			return this.selectUserMenuImage;
		case ("userMenuToMainMenu"):
			return this.userMenuToMainMenu;
		case ("Moon"):
			return this.moon;
		case ("Lava Planet"):
			return this.lavaPlanet;
		case ("Mars Planet"):
			return this.marsPlanet;
		case ("Planet One"):
			return this.planetOne;
		case ("Planet Two"):
			return this.planetTwo;
		case ("Planet Three"):
			return this.planetThree;
		case ("Star"):
			return this.star;
		}
		return testImage;
	}

	public List<ImageIcon> getGif(String imageType) {
		switch (imageType) {
		case ("Impact Explosion One"):
			return this.impactExplosionOneFrames;
		case ("Player Engine"):
			return this.playerEngineFrames;
		case ("Destroyed Explosion"):
			return this.destroyedExplosionUpFrames;
		case ("Destroyed Explosion Right"):
			return this.destroyedExplosionRightFrames;
		case ("Destroyed Explosion Down"):
			return this.destroyedExplosionDownFrames;
		case ("Destroyed Explosion Left"):
			return this.destroyedExplosionLeftFrames;
		}
		return null;
	}

	public void initAnimations() throws FileNotFoundException, IOException {
		ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();

		reader.setInput(new FileImageInputStream(new File("src/resources/images/explosion.gif")));
		impactExplosionOneFrames = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(new File("src/resources/images/enginesmoke.gif")));
		playerEngineFrames = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(new File("src/resources/images/Destroyed Explosion.gif")));
		destroyedExplosionUpFrames = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(new File("src/resources/images/Destroyed Explosion Right.gif")));
		destroyedExplosionRightFrames = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(new File("src/resources/images/Destroyed Explosion Left.gif")));
		destroyedExplosionDownFrames = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(new File("src/resources/images/Destroyed Explosion Reverse.gif")));
		destroyedExplosionLeftFrames = gifToImageIcons(reader);
	}

	private List<ImageIcon> gifToImageIcons(ImageReader reader) throws IOException {
		int n = reader.getNumImages(true);
		List<ImageIcon> imgs = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			imgs.add(new ImageIcon(reader.read(i)));
		}
		return imgs;
	}

	public List<ImageIcon> getImpactExplosionOneFrames() {
		return impactExplosionOneFrames;
	}

	public List<ImageIcon> getPlayerEngineFrames() {
		return playerEngineFrames;
	}

	public List<ImageIcon> getDestroyedExplosionUpFrames() {
		return destroyedExplosionUpFrames;
	}

	public List<ImageIcon> getDestroyedExplosionLeftFrames() {
		return destroyedExplosionLeftFrames;
	}

	public List<ImageIcon> getDestroyedExplosionRightFrames() {
		return destroyedExplosionRightFrames;
	}

	public List<ImageIcon> getDestroyedExplosionDownFrames() {
		return destroyedExplosionDownFrames;
	}

}