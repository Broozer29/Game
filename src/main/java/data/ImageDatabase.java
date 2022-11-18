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
	
	//testimages
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
	
	public List<ImageIcon> getGif(String imageType) {
		switch(imageType) {
		case("Impact Explosion One"):
			return this.impactExplosionOneFrames;
		case("Player Engine"):
			return this.playerEngineFrames;
		case("Destroyed Explosion"):
			return this.destroyedExplosionUpFrames;
		case("Destroyed Explosion Right"):
			return this.destroyedExplosionRightFrames;
		case("Destroyed Explosion Down"):
			return this.destroyedExplosionDownFrames;
		case("Destroyed Explosion Left"):
			return this.destroyedExplosionLeftFrames;
		}
		return null;
	}
	
	public void initAnimations() throws FileNotFoundException, IOException {
	      ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
	      
	      reader.setInput( new FileImageInputStream( new File("src/resources/images/explosion.gif")));
	      impactExplosionOneFrames = gifToImageIcons(reader);
	      
	      reader.setInput( new FileImageInputStream( new File("src/resources/images/enginesmoke.gif")));
	      playerEngineFrames = gifToImageIcons(reader);
	      
	      reader.setInput( new FileImageInputStream( new File("src/resources/images/Destroyed Explosion.gif")));
	      destroyedExplosionUpFrames = gifToImageIcons(reader);
	      
	      reader.setInput( new FileImageInputStream( new File("src/resources/images/Destroyed Explosion Right.gif")));
	      destroyedExplosionRightFrames = gifToImageIcons(reader);
	      
	      reader.setInput( new FileImageInputStream( new File("src/resources/images/Destroyed Explosion Left.gif")));
	      destroyedExplosionDownFrames = gifToImageIcons(reader);
	      
	      reader.setInput( new FileImageInputStream( new File("src/resources/images/Destroyed Explosion Reverse.gif")));
	      destroyedExplosionLeftFrames = gifToImageIcons(reader);
	}
	
	private List<ImageIcon> gifToImageIcons(ImageReader reader) throws IOException{
	      int n = reader.getNumImages( true );
	      List<ImageIcon> imgs = new ArrayList<>();
	      for(int i = 0; i<n; i++){
	          imgs.add( new ImageIcon(reader.read(i)) );
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