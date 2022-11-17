package data;

import java.awt.Image;

public class ImageMemorizer {

	private static ImageMemorizer instance = new ImageMemorizer();
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
	private Image impactExplosionOne;
	private Image playerEngine;
	private Image destroyedExplosionUp;
	private Image destroyedExplosionLeft;
	private Image destroyedExplosionRight;
	private Image destroyedExplosionDown;

	// Background images
	private Image moon;
	private Image lavaPlanet;
	private Image marsPlanet;
	private Image planetOne;
	private Image planetTwo;
	private Image planetThree;
	private Image star;
	
	//testimages
	private Image testImage;

	private ImageMemorizer() {
		initializeImages();
	}

	public static ImageMemorizer getInstance() {
		return instance;
	}

	private void initializeImages() {
		this.initFriendlies();
		this.initAnimations();
		this.initBackgroundObjects();
		this.initEnemies();
		this.initMenuImages();
		this.initProjectiles();
	}

	private void initFriendlies() {
		this.spaceShipImage = imgLoader.getImage("spaceship");
	}

	private void initEnemies() {
		this.alienSpaceshipImage = imgLoader.getImage("Alien");
		this.alienBombImage = imgLoader.getImage("Alien bomb");
	}

	private void initProjectiles() {
		this.laserBeamImage = imgLoader.getImage("laserbeam");
		this.alienLaserbeamImage = imgLoader.getImage("alienlaserbeam");
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
		this.moon = imgLoader.getImage("moon1");
		this.lavaPlanet = imgLoader.getImage("lavaplanet1");
		this.planetOne = imgLoader.getImage("planet1");
		this.planetTwo = imgLoader.getImage("planet2");
		this.planetThree = imgLoader.getImage("planet3");
		this.marsPlanet = imgLoader.getImage("marsplanet1");
	}

	private void initAnimations() {
		this.impactExplosionOne = imgLoader.getGif("Impact Explosion One");
		this.playerEngine = imgLoader.getGif("Player Engine");
		this.destroyedExplosionUp = imgLoader.getGif("Destroyed Explosion");
		this.destroyedExplosionRight = imgLoader.getGif("Destroyed Explosion Right");
		this.destroyedExplosionLeft = imgLoader.getGif("Destroyde Explosion Left");
		this.destroyedExplosionDown = imgLoader.getGif("Destroyed Explosion Reverse");

	}

	public Image getImage(String imageType) {
		switch(imageType) {
		case("spaceship"):
			return this.spaceShipImage;
		case("laserbeam"):
			return this.laserBeamImage;
		case("Alien"):
			return this.alienSpaceshipImage;
		case("Alien bomb"):
			return this.alienBombImage;
		case("alienlaserbeam"):
			return this.alienLaserbeamImage;
		case("StartGame"):
			return this.startGameImage;
		case("UserOne"):
			return this.userOneImage;
		case("UserTwo"):
			return this.userTwoImage;
		case("UserThree"):
			return this.userThreeImage;
		case("SelectUserMenu"):
			return this.selectUserMenuImage;
		case("userMenuToMainMenu"):
			return this.userMenuToMainMenu;
		case("moon1"):
			return this.moon;
		case("lavaplanet1"):
			return this.lavaPlanet;
		case("marsplanet1"):
			return this.marsPlanet;
		case("planet1"):
			return this.planetOne;
		case("planet2"):
			return this.planetTwo;
		case("planet3"):
			return this.planetThree;
		case("star"):
			return this.star;
		}
		return testImage;
	}
	
	public Image getGif(String imageType) {
		switch(imageType) {
		case("Impact Explosion One"):
			return this.impactExplosionOne;
		case("Player Engine"):
			return this.playerEngine;
		case("Destroyed Explosion"):
			return this.destroyedExplosionUp;
		case("Destroyed Explosion Right"):
			return this.destroyedExplosionRight;
		case("Destroyed Explosion Down"):
			return this.destroyedExplosionDown;
		case("Destroyed Explosion Left"):
			return this.destroyedExplosionLeft;
		}
		return testImage;
	}
}