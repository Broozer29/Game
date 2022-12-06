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
	private Image seekerImage;
	private Image tazerImage;
	private Image energizerImage;
	private Image bulldozerImage;
	private Image flamerImage;
	private Image bombaImage;

	// Ship numbers to names:
	/*
	 * Ship1 = Seeker Ship2 = Tazer Ship3 = Energizer Ship4 = Bulldozer Ship5 =
	 * Flamer Ship6 = Bomba
	 * 
	 */

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
	private List<Image> impactExplosionOneFrames = new ArrayList<Image>();
	private List<Image> playerEngineFrames = new ArrayList<Image>();
	private List<Image> destroyedExplosionUpFrames = new ArrayList<Image>();
	private List<Image> destroyedExplosionLeftFrames = new ArrayList<Image>();
	private List<Image> destroyedExplosionRightFrames = new ArrayList<Image>();
	private List<Image> destroyedExplosionDownFrames = new ArrayList<Image>();
	private List<Image> alienBombExplosionFrames = new ArrayList<Image>();

	// Enemy Projectile Animations
	private List<Image> seekerProjectileFrames = new ArrayList<Image>();
	private List<Image> tazerProjectileFrames = new ArrayList<Image>();
	private List<Image> energizerProjectileFrames = new ArrayList<Image>();
	private List<Image> bulldozerProjectileFrames = new ArrayList<Image>();
	private List<Image> flamerProjectileFrames = new ArrayList<Image>();
	private List<Image> bombaProjectileFrames = new ArrayList<Image>();

	// Enemy Exhaust Animations
	private List<Image> seekerNormalExhaustFrames = new ArrayList<Image>();
	private List<Image> seekerLargeExhaustFrames = new ArrayList<Image>();
	private List<Image> tazerNormalExhaustFrames = new ArrayList<Image>();
	private List<Image> tazerLargeExhaustFrames = new ArrayList<Image>();
	private List<Image> energizerNormalExhaustFrames = new ArrayList<Image>();
	private List<Image> energizerLargeExhaustFrames = new ArrayList<Image>();
	private List<Image> bulldozerNormalExhaustFrames = new ArrayList<Image>();
	private List<Image> bulldozerLargeExhaustFrames = new ArrayList<Image>();
	private List<Image> flamerNormalExhaustFrames = new ArrayList<Image>();
	private List<Image> flamerLargeExhaustFrames = new ArrayList<Image>();
	private List<Image> bombaNormalExhaustFrames = new ArrayList<Image>();
	private List<Image> bombaLargeExhaustFrames = new ArrayList<Image>();

	// Enemy projectile explosions
	private List<Image> seekerProjectileExplosion = new ArrayList<Image>();
	private List<Image> tazerProjectileExplosion = new ArrayList<Image>();
	private List<Image> energizerProjectileExplosion = new ArrayList<Image>();
	private List<Image> bulldozerProjectileExplosion = new ArrayList<Image>();
	private List<Image> flamerProjectileExplosion = new ArrayList<Image>();
	private List<Image> bombaProjectileExplosion = new ArrayList<Image>();

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
		this.seekerImage = imgLoader.getImage("Seeker");
		this.tazerImage = imgLoader.getImage("Tazer");
		this.energizerImage = imgLoader.getImage("Energizer");
		this.bulldozerImage = imgLoader.getImage("Bulldozer");
		this.flamerImage = imgLoader.getImage("Flamer");
		this.bombaImage = imgLoader.getImage("Bomba");
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
		case ("Seeker"):
			return this.seekerImage;
		case ("Tazer"):
			return this.tazerImage;
		case ("Energizer"):
			return this.energizerImage;
		case ("Bulldozer"):
			return this.bulldozerImage;
		case ("Flamer"):
			return this.flamerImage;
		case ("Bomba"):
			return this.bombaImage;
		}
		return testImage;
	}

	public List<Image> getGif(String imageType) {
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
		case ("Alien Bomb Explosion"):
			return this.alienBombExplosionFrames;
		case ("Seeker Projectile"):
			return this.seekerProjectileFrames;
		case ("Tazer Projectile"):
			return this.tazerProjectileFrames;
		case ("Energizer Projectile"):
			return this.energizerProjectileFrames;
		case ("Bulldozer Projectile"):
			return this.bulldozerProjectileFrames;
		case ("Flamer Projectile"):
			return this.flamerProjectileFrames;
		case ("Bomba Projectile"):
			return this.bombaProjectileFrames;
		case ("Seeker Normal Exhaust"):
			return this.seekerNormalExhaustFrames;
		case ("Seeker Large Exhaust"):
			return this.seekerLargeExhaustFrames;
		case ("Seeker Projectile Explosion"):
			return this.seekerProjectileExplosion;
		case ("Tazer Normal Exhaust"):
			return this.tazerNormalExhaustFrames;
		case ("Tazer Large Exhaust"):
			return this.tazerLargeExhaustFrames;
		case ("Tazer Projectile Explosion"):
			return this.tazerProjectileExplosion;
		case ("Energizer Normal Exhaust"):
			return this.energizerNormalExhaustFrames;
		case ("Energizer Large Exhaust"):
			return this.energizerLargeExhaustFrames;
		case ("Energizer Projectile Explosion"):
			return this.energizerProjectileExplosion;
		case ("Bulldozer Normal Exhaust"):
			return this.bulldozerNormalExhaustFrames;
		case ("Bulldozer Large Exhaust"):
			return this.bulldozerLargeExhaustFrames;
		case ("Bulldozer Projectile Explosion"):
			return this.bulldozerProjectileExplosion;
		case ("Flamer Normal Exhaust"):
			return this.flamerNormalExhaustFrames;
		case ("Flamer Large Exhaust"):
			return this.flamerLargeExhaustFrames;
		case ("Flamer Projectile Explosion"):
			return this.flamerProjectileExplosion;
		case ("Bomba Normal Exhaust"):
			return this.bombaNormalExhaustFrames;
		case ("Bomba Large Exhaust"):
			return this.bombaLargeExhaustFrames;
		case ("Bomba Projectile Explosion"):
			return this.bombaProjectileExplosion;
		}
		return null;
	}

	public void initAnimations() throws FileNotFoundException, IOException {
		ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();

		reader.setInput(new FileImageInputStream(new File("src/resources/images/gif/explosion.gif")));
		impactExplosionOneFrames = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(new File("src/resources/images/gif/enginesmoke.gif")));
		playerEngineFrames = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(new File("src/resources/images/gif/Destroyed Explosion.gif")));
		destroyedExplosionUpFrames = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(new File("src/resources/images/gif/Destroyed Explosion Right.gif")));
		destroyedExplosionRightFrames = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(new File("src/resources/images/gif/Destroyed Explosion Left.gif")));
		destroyedExplosionDownFrames = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(new File("src/resources/images/gif/Destroyed Explosion Reverse.gif")));
		destroyedExplosionLeftFrames = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(new File("src/resources/images/gif/Alien Bomb Explosion.gif")));
		alienBombExplosionFrames = gifToImageIcons(reader);

		// Enemy projectiles
		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 1/Ship 1 - Projectile.gif")));
		seekerProjectileFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 2/Ship 2 - Projectile.gif")));
		tazerProjectileFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 3/Ship 3 - Projectile.gif")));
		energizerProjectileFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 4/Ship 4 - Projectile.gif")));
		bulldozerProjectileFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 5/Ship 5 - Projectile.gif")));
		flamerProjectileFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 6/Ship 6 - Projectile.gif")));
		bombaProjectileFrames = gifToImageIcons(reader);

		// Enemy normal exhausts
		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 1/Ship 1 - Normal Exhaust.gif")));
		seekerNormalExhaustFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 2/Ship 2 - Normal Exhaust.gif")));
		tazerNormalExhaustFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 3/Ship 3 - Normal Exhaust.gif")));
		energizerNormalExhaustFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 4/Ship 4 - Normal Exhaust.gif")));
		bulldozerNormalExhaustFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 5/Ship 5 - Normal Exhaust.gif")));
		flamerNormalExhaustFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 6/Ship 6 - Normal Exhaust.gif")));
		bombaNormalExhaustFrames = gifToImageIcons(reader);

		// Enemy large exhausts
		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 1/Ship 1 - Large Exhaust.gif")));
		seekerLargeExhaustFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 2/Ship 2 - Large Exhaust.gif")));
		tazerLargeExhaustFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 3/Ship 3 - Large Exhaust.gif")));
		energizerLargeExhaustFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 4/Ship 4 - Large Exhaust.gif")));
		bulldozerLargeExhaustFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 5/Ship 5 - Large Exhaust.gif")));
		flamerLargeExhaustFrames = gifToImageIcons(reader);

		reader.setInput(
				new FileImageInputStream(new File("src/resources/images/Ships/Ship 6/Ship 6 - Large Exhaust.gif")));
		bombaLargeExhaustFrames = gifToImageIcons(reader);

		// Enemy projectile explosions
		reader.setInput(new FileImageInputStream(
				new File("src/resources/images/Ships/Ship 1/Ship 1 - Projectile Explosion.gif")));
		seekerProjectileExplosion = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(
				new File("src/resources/images/Ships/Ship 2/Ship 2 - Projectile Explosion.gif")));
		tazerProjectileExplosion = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(
				new File("src/resources/images/Ships/Ship 3/Ship 3 - Projectile Explosion.gif")));
		energizerProjectileExplosion = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(
				new File("src/resources/images/Ships/Ship 4/Ship 4 - Projectile Explosion.gif")));
		bulldozerProjectileExplosion = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(
				new File("src/resources/images/Ships/Ship 5/Ship 5 - Projectile Explosion.gif")));
		flamerProjectileExplosion = gifToImageIcons(reader);

		reader.setInput(new FileImageInputStream(
				new File("src/resources/images/Ships/Ship 6/Ship 6 - Projectile Explosion.gif")));
		bombaProjectileExplosion = gifToImageIcons(reader);

	}

	private List<Image> gifToImageIcons(ImageReader reader) throws IOException {
		int n = reader.getNumImages(true);
		List<Image> imgs = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			imgs.add(new ImageIcon(reader.read(i)).getImage());
		}
		return imgs;
	}

}