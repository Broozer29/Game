package data.image;

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

import data.image.enums.ImageEnums;

public class ImageDatabase {

	private static ImageDatabase instance = new ImageDatabase();
	private ImageLoader imgLoader = ImageLoader.getInstance();

	// Friendly images
	private Image spaceShipImage;
	private Image model3BetterUpgrade;

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
	
	
	// Explosion animations
	private List<Image> impactExplosionOneFrames = new ArrayList<Image>();
	private List<Image> destroyedExplosionUpFrames = new ArrayList<Image>();
	private List<Image> destroyedExplosionLeftFrames = new ArrayList<Image>();
	private List<Image> destroyedExplosionRightFrames = new ArrayList<Image>();
	private List<Image> destroyedExplosionDownFrames = new ArrayList<Image>();
	private List<Image> alienBombExplosionFrames = new ArrayList<Image>();
	private List<Image> implosionFrames = new ArrayList<Image>();
	private List<Image> seekerDestroyedExplosionFrames = new ArrayList<Image>();
	private List<Image> tazerDestroyedExplosionFrames = new ArrayList<Image>();
	private List<Image> energizerDestroyedExplosionFrames = new ArrayList<Image>();
	private List<Image> bulldozerDestroyedExplosionFrames = new ArrayList<Image>();
	private List<Image> flamerDestroyedExplosionFrames = new ArrayList<Image>();
	private List<Image> bombaDestroyedExplosionFrames = new ArrayList<Image>();

	// Animations
	private List<Image> playerEngineFrames = new ArrayList<Image>();
	private List<Image> playerFireSwirlFrames = new ArrayList<Image>();
	private List<Image> playerEMPFrames = new ArrayList<Image>();
	private List<Image> defaultPlayerEngineBoostedFrames = new ArrayList<Image>();
	private List<Image> defaultPlayerShieldDamage = new ArrayList<Image>();


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
	
	//Images to Gifs
	private List<Image> defaultPlayerEngine = new ArrayList<Image>();

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
		this.initPNGtoGIFAnimation();
	}

	private void initFriendlies() {
		this.spaceShipImage = imgLoader.getImage(ImageEnums.Player_Spaceship);
		this.model3BetterUpgrade = imgLoader.getImage(ImageEnums.Player_Spaceship_Model_3);
	}

	private void initEnemies() {
		this.alienSpaceshipImage = imgLoader.getImage(ImageEnums.Alien);
		this.alienBombImage = imgLoader.getImage(ImageEnums.Alien_Bomb);
		this.seekerImage = imgLoader.getImage(ImageEnums.Seeker);
		this.tazerImage = imgLoader.getImage(ImageEnums.Tazer);
		this.energizerImage = imgLoader.getImage(ImageEnums.Energizer);
		this.bulldozerImage = imgLoader.getImage(ImageEnums.Bulldozer);
		this.flamerImage = imgLoader.getImage(ImageEnums.Flamer);
		this.bombaImage = imgLoader.getImage(ImageEnums.Bomba);
	}

	private void initProjectiles() {
		this.laserBeamImage = imgLoader.getImage(ImageEnums.Player_Laserbeam);
		this.alienLaserbeamImage = imgLoader.getImage(ImageEnums.Alien_Laserbeam);
	}

	private void initMenuImages() {
		this.startGameImage = imgLoader.getImage(ImageEnums.Start_Game);
		this.userOneImage = imgLoader.getImage(ImageEnums.User_One);
		this.userTwoImage = imgLoader.getImage(ImageEnums.User_Two);
		this.userThreeImage = imgLoader.getImage(ImageEnums.User_Three);
		this.selectUserMenuImage = imgLoader.getImage(ImageEnums.Select_User_Menu);
		this.testImage = imgLoader.getImage(ImageEnums.Test_Image);
		this.userMenuToMainMenu = imgLoader.getImage(ImageEnums.User_Menu_To_Main_Menu);
	}

	private void initBackgroundObjects() {
		this.moon = imgLoader.getImage(ImageEnums.Moon);
		this.lavaPlanet = imgLoader.getImage(ImageEnums.Lava_Planet);
		this.planetOne = imgLoader.getImage(ImageEnums.Planet_One);
		this.planetTwo = imgLoader.getImage(ImageEnums.Planet_Two);
		this.planetThree = imgLoader.getImage(ImageEnums.Planet_Three);
		this.marsPlanet = imgLoader.getImage(ImageEnums.Mars_Planet);
		this.star = imgLoader.getImage(ImageEnums.Star);
	}

	public Image getImage(ImageEnums imageType) {
		switch (imageType) {
		case Player_Spaceship:
			return this.spaceShipImage;
		case Player_Laserbeam:
			return this.laserBeamImage;
		case Alien:
			return this.alienSpaceshipImage;
		case Alien_Bomb:
			return this.alienBombImage;
		case Alien_Laserbeam:
			return this.alienLaserbeamImage;
		case Start_Game:
			return this.startGameImage;
		case User_One:
			return this.userOneImage;
		case User_Two:
			return this.userTwoImage;
		case User_Three:
			return this.userThreeImage;
		case Select_User_Menu:
			return this.selectUserMenuImage;
		case User_Menu_To_Main_Menu:
			return this.userMenuToMainMenu;
		case Moon:
			return this.moon;
		case Lava_Planet:
			return this.lavaPlanet;
		case Mars_Planet:
			return this.marsPlanet;
		case Planet_One:
			return this.planetOne;
		case Planet_Two:
			return this.planetTwo;
		case Planet_Three:
			return this.planetThree;
		case Star:
			return this.star;
		case Seeker:
			return this.seekerImage;
		case Tazer:
			return this.tazerImage;
		case Energizer:
			return this.energizerImage;
		case Bulldozer:
			return this.bulldozerImage;
		case Flamer:
			return this.flamerImage;
		case Bomba:
			return this.bombaImage;
		case Player_Spaceship_Model_3:
			return this.model3BetterUpgrade;
		default:
			return testImage;
		}
	}

	public List<Image> getGif(ImageEnums imageType) {
		switch (imageType) {
		case Impact_Explosion_One:
			return this.impactExplosionOneFrames;
		case Player_Engine:
			return this.playerEngineFrames;
		case Destroyed_Explosion:
			return this.destroyedExplosionUpFrames;
		case Destroyed_Explosion_Right:
			return this.destroyedExplosionRightFrames;
		case Destroyed_Explosion_Down:
			return this.destroyedExplosionDownFrames;
		case Destroyed_Explosion_Left:
			return this.destroyedExplosionLeftFrames;
		case Alien_Bomb_Explosion:
			return this.alienBombExplosionFrames;
		case Seeker_Missile:
			return this.seekerProjectileFrames;
		case Tazer_Missile:
			return this.tazerProjectileFrames;
		case Energizer_Missile:
			return this.energizerProjectileFrames;
		case Bulldozer_Missile:
			return this.bulldozerProjectileFrames;
		case Flamer_Missile:
			return this.flamerProjectileFrames;
		case Bomba_Missile:
			return this.bombaProjectileFrames;
		case Seeker_Normal_Exhaust:
			return this.seekerNormalExhaustFrames;
		case Seeker_Large_Exhaust:
			return this.seekerLargeExhaustFrames;
		case Seeker_Missile_Explosion:
			return this.seekerProjectileExplosion;
		case Tazer_Normal_Exhaust:
			return this.tazerNormalExhaustFrames;
		case Tazer_Large_Exhaust:
			return this.tazerLargeExhaustFrames;
		case Tazer_Missile_Explosion:
			return this.tazerProjectileExplosion;
		case Energizer_Normal_Exhaust:
			return this.energizerNormalExhaustFrames;
		case Energizer_Large_Exhaust:
			return this.energizerLargeExhaustFrames;
		case Energizer_Missile_Explosion:
			return this.energizerProjectileExplosion;
		case Bulldozer_Normal_Exhaust:
			return this.bulldozerNormalExhaustFrames;
		case Bulldozer_Large_Exhaust:
			return this.bulldozerLargeExhaustFrames;
		case Bulldozer_Missile_Explosion:
			return this.bulldozerProjectileExplosion;
		case Flamer_Normal_Exhaust:
			return this.flamerNormalExhaustFrames;
		case Flamer_Large_Exhaust:
			return this.flamerLargeExhaustFrames;
		case Flamer_Missile_Explosion:
			return this.flamerProjectileExplosion;
		case Bomba_Normal_Exhaust:
			return this.bombaNormalExhaustFrames;
		case Bomba_Large_Exhaust:
			return this.bombaLargeExhaustFrames;
		case Bomba_Missile_Explosion:
			return this.bombaProjectileExplosion;
		case Implosion:
			return this.implosionFrames;
		case Seeker_Destroyed_Explosion:
			return this.seekerDestroyedExplosionFrames;
		case Tazer_Destroyed_Explosion:
			return this.tazerDestroyedExplosionFrames;
		case Energizer_Destroyed_Explosion:
			return this.energizerDestroyedExplosionFrames;
		case Bomba_Destroyed_Explosion:
			return this.bombaDestroyedExplosionFrames;
		case Flamer_Destroyed_Explosion:
			return this.flamerDestroyedExplosionFrames;
		case Bulldozer_Destroyed_Explosion:
			return this.bulldozerDestroyedExplosionFrames;
		case Default_Player_Engine:
			return this.defaultPlayerEngine;
		case Default_Player_Engine_Boosted:
			return this.defaultPlayerEngineBoostedFrames;
		case Default_Player_Shield_Damage:
			return this.defaultPlayerShieldDamage;
		case Player_Fireswirl:
			return this.playerFireSwirlFrames;
		case Player_EMP:
			return this.playerEMPFrames;
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
		
		// Explosions
		reader.setInput(new FileImageInputStream(new File("src/resources/images/gif/Implosion.gif")));
		implosionFrames = gifToImageIcons(reader);
		
		reader.setInput(new FileImageInputStream(
				new File("src/resources/images/Ships/Ship 1/Ship 1 - Destroyed Explosion.gif")));
		seekerDestroyedExplosionFrames = gifToImageIcons(reader);
		
		reader.setInput(new FileImageInputStream(
				new File("src/resources/images/Ships/Ship 2/Ship 2 - Destroyed Explosion.gif")));
		tazerDestroyedExplosionFrames = gifToImageIcons(reader);
		
		reader.setInput(new FileImageInputStream(
				new File("src/resources/images/Ships/Ship 3/Ship 3 - Destroyed Explosion.gif")));
		energizerDestroyedExplosionFrames = gifToImageIcons(reader);
		
		reader.setInput(new FileImageInputStream(
				new File("src/resources/images/Ships/Ship 4/Ship 4 - Destroyed Explosion.gif")));
		bulldozerDestroyedExplosionFrames = gifToImageIcons(reader);
		
		reader.setInput(new FileImageInputStream(
				new File("src/resources/images/Ships/Ship 5/Ship 5 - Destroyed Explosion.gif")));
		flamerDestroyedExplosionFrames = gifToImageIcons(reader);
		
		reader.setInput(new FileImageInputStream(
				new File("src/resources/images/Ships/Ship 6/Ship 6 - Destroyed Explosion.gif")));
		bombaDestroyedExplosionFrames = gifToImageIcons(reader);

	}
	
	private void initPNGtoGIFAnimation() {
		for (int i = 1; i < 6; i++) {
			String sourceString=String.format("src/resources/images/gif/PNGtoGIF/Default Player Engine/%d.png",i);  
			Image image = imgLoader.getSpritesheetImage(sourceString);
			defaultPlayerEngine.add(image);
		}
		
		for (int i = 1; i < 6; i++) {
			String sourceString=String.format("src/resources/images/gif/PNGtoGIF/Default Player Engine Boosted/%d.png",i);  
			Image image = imgLoader.getSpritesheetImage(sourceString);
			defaultPlayerEngineBoostedFrames.add(image);
		}
		
		for (int i = 1; i < 10; i++) {
			String sourceString=String.format("src/resources/images/gif/PNGtoGIF/Player Shield Damage/%d.png",i);  
			Image image = imgLoader.getSpritesheetImage(sourceString);
			defaultPlayerShieldDamage.add(image);
		}
		
		for (int i = 1; i < 10; i++) {
			String sourceString=String.format("src/resources/images/gif/PNGtoGIF/Player EMP/%d.png",i);  
			Image image = imgLoader.getSpritesheetImage(sourceString);
			playerEMPFrames.add(image);
		}
		
		for (int i = 1; i < 46; i++) {
			String sourceString=String.format("src/resources/images/gif/PNGtoGIF/Player Fireswirl/tile0%d.png",i);  
			Image image = imgLoader.getSpritesheetImage(sourceString);
			playerFireSwirlFrames.add(image);
		}
		
	}

	private List<Image> gifToImageIcons(ImageReader reader) throws IOException {
		int n = reader.getNumImages(true);
		List<Image> imgs = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			imgs.add(new ImageIcon(reader.read(i)).getImage());
		}
		return imgs;
	}

	public Image getModel3BetterUpgrade() {
		return model3BetterUpgrade;
	}

}