package data.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
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
	 * Ship1 = Seeker Ship2 = Tazer Ship3 = Energizer Ship4 = Bulldozer Ship5 = Flamer
	 * Ship6 = Bomba
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

	// Game GUI images
	private Image healthBarImage;
	private Image shieldBarImage;
	private Image Frame;
	private Image iconBorder;
	private Image redFilling;
	private Image goldFilling;
	
	// Icons
	private Image tripleShotIcon;
	private Image doubleShotIcon;
	private Image starcraft2_Point_Defense_Drone;
	private Image starcraft2_Protoss_Cloak;
	private Image starcraft2_Protoss_Shield_Disintegrate;
	private Image starcraft2_Protoss_Shields_1;
	private Image starcraft2_Protoss_Shields_2;
	private Image starcraft2_Protoss_Shields_3;
	private Image starcraft2_Psi_Storm1;
	private Image starcraft2_Psi_Storm2;
	private Image starcraft2_Psi_Storm3;
	private Image starcraft2_Pulse_Grenade;
	private Image starcraft2_Pulse_Laser;
	private Image starcraft2_Repair_Blink;
	private Image starcraft2_Rocket_Cluster;
	private Image starcraft2_Dual_Rockets;
	private Image starcraft2_Artanis_Shield;
	private Image starcraft2_Auto_Tracking;
	private Image starcraft2_Blink;
	private Image starcraft2_Blue_Flame;
	private Image starcraft2_Concussive_Shells;
	private Image starcraft2_Corsair_Cloak;
	private Image starcraft2_Drone_Cloak;
	private Image starcraft2_DT_Blink;
	private Image starcraft2_Energizer_Speed;
	private Image starcraft2_Energizer_Speed2;
	private Image starcraft2_Fire_Cloak;
	private Image starcraft2_Energy_Siphon;
	private Image starcraft2_Fire_Hardened_Shields;
	private Image starcraft2_Flame_Turret;
	private Image starcraft2_Force_Field;
	private Image starcraft2_Guardian_Shield;
	private Image starcraft2_Hardened_Shields;
	private Image starcraft2_Health_Upgrade_1;
	private Image starcraft2_Health_Upgrade_2;
	private Image starcraft2_Ignite_Afterburners;
	private Image starcraft2_Immortal_Barrier;
	private Image starcraft2_Immortal_Original_Barrier;
	private Image starcraft2_LaserBeam;
	private Image starcraft2_LaserDrill;
	private Image starcraft2_MovementSpeed;
	private Image starcraft2_Seeker_Missile;
	private Image starcraft2_Shield_Barrier;
	private Image starcraft2_Shield_Piercing;
	private Image starcraft2_Stim1;
	private Image starcraft2_Stim2;
	private Image starcraft2_Stim3;
	private Image starcraft2_Terran_Plating1;
	private Image starcraft2_Terran_Plating2;
	private Image starcraft2_Terran_Plating3;
	private Image starcraft2_Terran_Speed1;
	private Image starcraft2_Terran_Speed2;
	private Image starcraft2_Terran_Speed3;
	private Image starcraft2_Terran_Weapons1;
	private Image starcraft2_Terran_Weapons2;
	private Image starcraft2_Terran_Weapons3;
	private Image starcraft2_Third_Blink;
	private Image starcraft2_Time_Warp;
	private Image starcraft2_Vespene_Gas;
	private Image starcraft2_Vespene_Siphon;
	private Image starcraft2_Vespene_Drone;
	private Image starcraft2_Wraith_Cloak;
	private Image starcraft2_Yellow_Blink;
	private Image starcraft2_Heal;

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
	private List<Image> playerEMPPlusFrames = new ArrayList<Image>();
	private List<Image> defaultPlayerEngineBoostedFrames = new ArrayList<Image>();
	private List<Image> defaultPlayerShieldDamage = new ArrayList<Image>();
	private List<Image> guardianBotFrames = new ArrayList<Image>();

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
	private Image parralex1;
	private Image parralex2;
	private Image parralex3;
	private Image parralex4;
	private Image parralex5;
	private Image warmNebula;
	private Image coldNebula;
	private Image regularNebula;
	private Image blueNebula1;
	private Image blueNebula2;
	private Image blueNebula3;
	private Image blueNebula4;
	private Image blueNebula5;
	private Image blueNebula6;
	private Image greenNebula1;
	private Image greenNebula2;
	private Image greenNebula3;
	private Image greenNebula4;
	private Image greenNebula5;
	private Image greenNebula6;
	private Image greenNebula7;
	private Image purpleNebula1;
	private Image purpleNebula2;
	private Image purpleNebula3;
	private Image purpleNebula4;
	private Image purpleNebula5;
	private Image purpleNebula6;
	private Image purpleNebula7;

	// testimages
	private Image testImage;

	// Images to Gifs
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
		this.initGameUIobjects();
		this.initSpriteSheets();
		this.initIcons();
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

	private void initGameUIobjects() {
		this.healthBarImage = imgLoader.getImage(ImageEnums.Health_Bar);
		this.shieldBarImage = imgLoader.getImage(ImageEnums.Shield_Bar);
		this.Frame = imgLoader.getImage(ImageEnums.Frame);
		this.iconBorder = imgLoader.getImage(ImageEnums.Icon_Border);
		this.redFilling = imgLoader.getImage(ImageEnums.Red_Filling);
		this.goldFilling = imgLoader.getImage(ImageEnums.Gold_Filling);
	}
	
	private void initIcons() {
		this.tripleShotIcon = imgLoader.getImage(ImageEnums.TripleShotIcon);
		this.doubleShotIcon = imgLoader.getImage(ImageEnums.DoubleShotIcon);
		this.starcraft2_Point_Defense_Drone = imgLoader.getImage(ImageEnums.Starcraft2_Point_Defense_Drone);
		this.starcraft2_Protoss_Cloak = imgLoader.getImage(ImageEnums.Starcraft2_Protoss_Cloak);
		this.starcraft2_Protoss_Shield_Disintegrate = imgLoader.getImage(ImageEnums.Starcraft2_Protoss_Shield_Disintegrate);
		this.starcraft2_Protoss_Shields_1 = imgLoader.getImage(ImageEnums.Starcraft2_Protoss_Shields_1);
		this.starcraft2_Protoss_Shields_2 = imgLoader.getImage(ImageEnums.Starcraft2_Protoss_Shields_2);
		this.starcraft2_Protoss_Shields_3 = imgLoader.getImage(ImageEnums.Starcraft2_Protoss_Shields_3);
		this.starcraft2_Psi_Storm1 = imgLoader.getImage(ImageEnums.Starcraft2_Psi_Storm1);
		this.starcraft2_Psi_Storm2 = imgLoader.getImage(ImageEnums.Starcraft2_Psi_Storm2);
		this.starcraft2_Psi_Storm3 = imgLoader.getImage(ImageEnums.Starcraft2_Psi_Storm3);
		this.starcraft2_Pulse_Grenade = imgLoader.getImage(ImageEnums.Starcraft2_Pulse_Grenade);
		this.starcraft2_Pulse_Laser = imgLoader.getImage(ImageEnums.Starcraft2_Pulse_Laser);
		this.starcraft2_Repair_Blink = imgLoader.getImage(ImageEnums.Starcraft2_Repair_Blink);
		this.starcraft2_Rocket_Cluster = imgLoader.getImage(ImageEnums.Starcraft2_Rocket_Cluster);
		this.starcraft2_Dual_Rockets = imgLoader.getImage(ImageEnums.Starcraft2_Dual_Rockets);
		this.starcraft2_Artanis_Shield = imgLoader.getImage(ImageEnums.Starcraft2_Artanis_Shield);
		this.starcraft2_Auto_Tracking = imgLoader.getImage(ImageEnums.Starcraft2_Auto_Tracking);
		this.starcraft2_Blink = imgLoader.getImage(ImageEnums.Starcraft2_Blink);
		this.starcraft2_Blue_Flame = imgLoader.getImage(ImageEnums.Starcraft2_Blue_Flame);
		this.starcraft2_Concussive_Shells = imgLoader.getImage(ImageEnums.Starcraft2_Concussive_Shells);
		this.starcraft2_Corsair_Cloak = imgLoader.getImage(ImageEnums.Starcraft2_Corsair_Cloak);
		this.starcraft2_Drone_Cloak = imgLoader.getImage(ImageEnums.Starcraft2_Drone_Cloak);
		this.starcraft2_DT_Blink = imgLoader.getImage(ImageEnums.Starcraft2_DT_Blink);
		this.starcraft2_Energizer_Speed = imgLoader.getImage(ImageEnums.Starcraft2_Energizer_Speed);
		this.starcraft2_Energizer_Speed2 = imgLoader.getImage(ImageEnums.Starcraft2_Energizer_Speed2);
		this.starcraft2_Fire_Cloak = imgLoader.getImage(ImageEnums.Starcraft2_Fire_Cloak);
		this.starcraft2_Energy_Siphon = imgLoader.getImage(ImageEnums.Starcraft2_Energy_Siphon);
		this.starcraft2_Fire_Hardened_Shields = imgLoader.getImage(ImageEnums.Starcraft2_Fire_Hardened_Shields);
		this.starcraft2_Flame_Turret = imgLoader.getImage(ImageEnums.Starcraft2_Flame_Turret);
		this.starcraft2_Force_Field = imgLoader.getImage(ImageEnums.Starcraft2_Force_Field);
		this.starcraft2_Guardian_Shield = imgLoader.getImage(ImageEnums.Starcraft2_Guardian_Shield);
		this.starcraft2_Hardened_Shields = imgLoader.getImage(ImageEnums.Starcraft2_Hardened_Shields);
		this.starcraft2_Health_Upgrade_1 = imgLoader.getImage(ImageEnums.Starcraft2_Health_Upgrade_1);
		this.starcraft2_Health_Upgrade_2 = imgLoader.getImage(ImageEnums.Starcraft2_Health_Upgrade_2);
		this.starcraft2_Ignite_Afterburners = imgLoader.getImage(ImageEnums.Starcraft2_Ignite_Afterburners);
		this.starcraft2_Immortal_Barrier = imgLoader.getImage(ImageEnums.Starcraft2_Immortal_Barrier);
		this.starcraft2_Immortal_Original_Barrier = imgLoader.getImage(ImageEnums.Starcraft2_Immortal_Original_Barrier);
		this.starcraft2_LaserBeam = imgLoader.getImage(ImageEnums.Starcraft2_LaserBeam);
		this.starcraft2_LaserDrill = imgLoader.getImage(ImageEnums.Starcraft2_LaserDrill);
		this.starcraft2_MovementSpeed = imgLoader.getImage(ImageEnums.Starcraft2_MovementSpeed);
		this.starcraft2_Seeker_Missile = imgLoader.getImage(ImageEnums.Starcraft2_Seeker_Missile);
		this.starcraft2_Shield_Barrier = imgLoader.getImage(ImageEnums.Starcraft2_Shield_Barrier);
		this.starcraft2_Shield_Piercing = imgLoader.getImage(ImageEnums.Starcraft2_Shield_Piercing);
		this.starcraft2_Stim1 = imgLoader.getImage(ImageEnums.Starcraft2_Stim1);
		this.starcraft2_Stim2 = imgLoader.getImage(ImageEnums.Starcraft2_Stim2);
		this.starcraft2_Stim3 = imgLoader.getImage(ImageEnums.Starcraft2_Stim3);
		this.starcraft2_Terran_Plating1 = imgLoader.getImage(ImageEnums.Starcraft2_Terran_Plating1);
		this.starcraft2_Terran_Plating2 = imgLoader.getImage(ImageEnums.Starcraft2_Terran_Plating2);
		this.starcraft2_Terran_Plating3 = imgLoader.getImage(ImageEnums.Starcraft2_Terran_Plating3);
		this.starcraft2_Terran_Speed1 = imgLoader.getImage(ImageEnums.Starcraft2_Terran_Speed1);
		this.starcraft2_Terran_Speed2 = imgLoader.getImage(ImageEnums.Starcraft2_Terran_Speed2);
		this.starcraft2_Terran_Speed3 = imgLoader.getImage(ImageEnums.Starcraft2_Terran_Speed3);
		this.starcraft2_Terran_Weapons1 = imgLoader.getImage(ImageEnums.Starcraft2_Terran_Weapons1);
		this.starcraft2_Terran_Weapons2 = imgLoader.getImage(ImageEnums.Starcraft2_Terran_Weapons2);
		this.starcraft2_Terran_Weapons3 = imgLoader.getImage(ImageEnums.Starcraft2_Terran_Weapons3);
		this.starcraft2_Third_Blink = imgLoader.getImage(ImageEnums.Starcraft2_Third_Blink);
		this.starcraft2_Time_Warp = imgLoader.getImage(ImageEnums.Starcraft2_Time_Warp);
		this.starcraft2_Vespene_Gas = imgLoader.getImage(ImageEnums.Starcraft2_Vespene_Gas);
		this.starcraft2_Vespene_Siphon = imgLoader.getImage(ImageEnums.Starcraft2_Vespene_Siphon);
		this.starcraft2_Vespene_Drone = imgLoader.getImage(ImageEnums.Starcraft2_Vespene_Drone);
		this.starcraft2_Wraith_Cloak = imgLoader.getImage(ImageEnums.Starcraft2_Wraith_Cloak);
		this.starcraft2_Yellow_Blink = imgLoader.getImage(ImageEnums.Starcraft2_Yellow_Blink);
		this.starcraft2_Heal = imgLoader.getImage(ImageEnums.Starcraft2_Heal);
	}

	private void initBackgroundObjects() {
		this.moon = imgLoader.getImage(ImageEnums.Moon);
		this.lavaPlanet = imgLoader.getImage(ImageEnums.Lava_Planet);
		this.planetOne = imgLoader.getImage(ImageEnums.Planet_One);
		this.planetTwo = imgLoader.getImage(ImageEnums.Planet_Two);
		this.planetThree = imgLoader.getImage(ImageEnums.Planet_Three);
		this.marsPlanet = imgLoader.getImage(ImageEnums.Mars_Planet);
		this.star = imgLoader.getImage(ImageEnums.Star);
		this.parralex1 = imgLoader.getImage(ImageEnums.Parallex_1);
		this.parralex2 = imgLoader.getImage(ImageEnums.Parallex_2);
		this.parralex3 = imgLoader.getImage(ImageEnums.Parallex_3);
		this.parralex4 = imgLoader.getImage(ImageEnums.Parallex_4);
		this.parralex5 = imgLoader.getImage(ImageEnums.Parallex_5);
		this.coldNebula = imgLoader.getImage(ImageEnums.Cold_Nebula);
		this.warmNebula = imgLoader.getImage(ImageEnums.Warm_Nebula);
		this.regularNebula = imgLoader.getImage(ImageEnums.Regular_Nebula);
		this.blueNebula1 = imgLoader.getImage(ImageEnums.Blue_Nebula_1);
		this.blueNebula2 = imgLoader.getImage(ImageEnums.Blue_Nebula_2);
		this.blueNebula3 = imgLoader.getImage(ImageEnums.Blue_Nebula_3);
		this.blueNebula4 = imgLoader.getImage(ImageEnums.Blue_Nebula_4);
		this.blueNebula5 = imgLoader.getImage(ImageEnums.Blue_Nebula_5);
		this.blueNebula6 = imgLoader.getImage(ImageEnums.Blue_Nebula_6);
		this.greenNebula1 = imgLoader.getImage(ImageEnums.Green_Nebula_1);
		this.greenNebula2 = imgLoader.getImage(ImageEnums.Green_Nebula_2);
		this.greenNebula3 = imgLoader.getImage(ImageEnums.Green_Nebula_3);
		this.greenNebula4 = imgLoader.getImage(ImageEnums.Green_Nebula_4);
		this.greenNebula5 = imgLoader.getImage(ImageEnums.Green_Nebula_5);
		this.greenNebula6 = imgLoader.getImage(ImageEnums.Green_Nebula_6);
		this.greenNebula7 = imgLoader.getImage(ImageEnums.Green_Nebula_7);
		this.purpleNebula1 = imgLoader.getImage(ImageEnums.Purple_Nebula_1);
		this.purpleNebula2 = imgLoader.getImage(ImageEnums.Purple_Nebula_2);
		this.purpleNebula3 = imgLoader.getImage(ImageEnums.Purple_Nebula_3);
		this.purpleNebula4 = imgLoader.getImage(ImageEnums.Purple_Nebula_4);
		this.purpleNebula5 = imgLoader.getImage(ImageEnums.Purple_Nebula_5);
		this.purpleNebula6 = imgLoader.getImage(ImageEnums.Purple_Nebula_6);
		this.purpleNebula7 = imgLoader.getImage(ImageEnums.Purple_Nebula_7);

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
		case Health_Bar:
			return this.healthBarImage;
		case Shield_Bar:
			return this.shieldBarImage;
		case Frame:
			return this.Frame;
		case Icon_Border:
			return this.iconBorder;
		case Red_Filling:
			return this.redFilling;
		case Gold_Filling:
			return this.goldFilling;
		case Cold_Nebula:
			return coldNebula;
		case Warm_Nebula:
			return warmNebula;
		case Regular_Nebula:
			return regularNebula;
		case Blue_Nebula_1:
			return blueNebula1;
		case Blue_Nebula_2:
			return blueNebula2;
		case Blue_Nebula_3:
			return blueNebula3;
		case Blue_Nebula_4:
			return blueNebula4;
		case Blue_Nebula_5:
			return blueNebula5;
		case Blue_Nebula_6:
			return blueNebula6;
		case Green_Nebula_1:
			return greenNebula1;
		case Green_Nebula_2:
			return greenNebula2;
		case Green_Nebula_3:
			return greenNebula3;
		case Green_Nebula_4:
			return greenNebula4;
		case Green_Nebula_5:
			return greenNebula5;
		case Green_Nebula_6:
			return greenNebula6;
		case Green_Nebula_7:
			return greenNebula7;
		case Purple_Nebula_1:
			return purpleNebula1;
		case Purple_Nebula_2:
			return purpleNebula2;
		case Purple_Nebula_3:
			return purpleNebula3;
		case Purple_Nebula_4:
			return purpleNebula4;
		case Purple_Nebula_5:
			return purpleNebula5;
		case Purple_Nebula_6:
			return purpleNebula6;
		case Purple_Nebula_7:
			return purpleNebula7;
		case Parallex_1:
			return parralex1;
		case Parallex_2:
			return parralex2;
		case Parallex_3:
			return parralex3;
		case Parallex_4:
			return parralex4;
		case Parallex_5:
			return parralex5;
		case TripleShotIcon:
			return tripleShotIcon;
		case DoubleShotIcon:
			return doubleShotIcon;
		case Starcraft2_Point_Defense_Drone:
			return starcraft2_Point_Defense_Drone;
		case Starcraft2_Protoss_Cloak:
			return starcraft2_Protoss_Cloak;
		case Starcraft2_Protoss_Shield_Disintegrate:
			return starcraft2_Protoss_Shield_Disintegrate;
		case Starcraft2_Protoss_Shields_1:
			return starcraft2_Protoss_Shields_1;
		case Starcraft2_Protoss_Shields_2:
			return starcraft2_Protoss_Shields_2;
		case Starcraft2_Protoss_Shields_3:
			return starcraft2_Protoss_Shields_3;
		case Starcraft2_Psi_Storm1:
			return starcraft2_Psi_Storm1;
		case Starcraft2_Psi_Storm2:
			return starcraft2_Psi_Storm2;
		case Starcraft2_Psi_Storm3:
			return starcraft2_Psi_Storm3;
		case Starcraft2_Pulse_Grenade:
			return starcraft2_Pulse_Grenade;
		case Starcraft2_Pulse_Laser:
			return starcraft2_Pulse_Laser;
		case Starcraft2_Repair_Blink:
			return starcraft2_Repair_Blink;
		case Starcraft2_Rocket_Cluster:
			return starcraft2_Rocket_Cluster;
		case Starcraft2_Dual_Rockets:
			return starcraft2_Dual_Rockets;
		case Starcraft2_Artanis_Shield:
			return starcraft2_Artanis_Shield;
		case Starcraft2_Auto_Tracking:
			return starcraft2_Auto_Tracking;
		case Starcraft2_Blink:
			return starcraft2_Blink;
		case Starcraft2_Blue_Flame:
			return starcraft2_Blue_Flame;
		case Starcraft2_Concussive_Shells:
			return starcraft2_Concussive_Shells;
		case Starcraft2_Corsair_Cloak:
			return starcraft2_Corsair_Cloak;
		case Starcraft2_Drone_Cloak:
			return starcraft2_Drone_Cloak;
		case Starcraft2_DT_Blink:
			return starcraft2_DT_Blink;
		case Starcraft2_Energizer_Speed:
			return starcraft2_Energizer_Speed;
		case Starcraft2_Energizer_Speed2:
			return starcraft2_Energizer_Speed2;
		case Starcraft2_Fire_Cloak:
			return starcraft2_Fire_Cloak;
		case Starcraft2_Energy_Siphon:
			return starcraft2_Energy_Siphon;
		case Starcraft2_Fire_Hardened_Shields:
			return starcraft2_Fire_Hardened_Shields;
		case Starcraft2_Flame_Turret:
			return starcraft2_Flame_Turret;
		case Starcraft2_Force_Field:
			return starcraft2_Force_Field;
		case Starcraft2_Guardian_Shield:
			return starcraft2_Guardian_Shield;
		case Starcraft2_Hardened_Shields:
			return starcraft2_Hardened_Shields;
		case Starcraft2_Health_Upgrade_1:
			return starcraft2_Health_Upgrade_1;
		case Starcraft2_Health_Upgrade_2:
			return starcraft2_Health_Upgrade_2;
		case Starcraft2_Ignite_Afterburners:
			return starcraft2_Ignite_Afterburners;
		case Starcraft2_Immortal_Barrier:
			return starcraft2_Immortal_Barrier;
		case Starcraft2_Immortal_Original_Barrier:
			return starcraft2_Immortal_Original_Barrier;
		case Starcraft2_LaserBeam:
			return starcraft2_LaserBeam;
		case Starcraft2_LaserDrill:
			return starcraft2_LaserDrill;
		case Starcraft2_MovementSpeed:
			return starcraft2_MovementSpeed;
		case Starcraft2_Seeker_Missile:
			return starcraft2_Seeker_Missile;
		case Starcraft2_Shield_Barrier:
			return starcraft2_Shield_Barrier;
		case Starcraft2_Shield_Piercing:
			return starcraft2_Shield_Piercing;
		case Starcraft2_Stim1:
			return starcraft2_Stim1;
		case Starcraft2_Stim2:
			return starcraft2_Stim2;
		case Starcraft2_Stim3:
			return starcraft2_Stim3;
		case Starcraft2_Terran_Plating1:
			return starcraft2_Terran_Plating1;
		case Starcraft2_Terran_Plating2:
			return starcraft2_Terran_Plating2;
		case Starcraft2_Terran_Plating3:
			return starcraft2_Terran_Plating3;
		case Starcraft2_Terran_Speed1:
			return starcraft2_Terran_Speed1;
		case Starcraft2_Terran_Speed2:
			return starcraft2_Terran_Speed2;
		case Starcraft2_Terran_Speed3:
			return starcraft2_Terran_Speed3;
		case Starcraft2_Terran_Weapons1:
			return starcraft2_Terran_Weapons1;
		case Starcraft2_Terran_Weapons2:
			return starcraft2_Terran_Weapons2;
		case Starcraft2_Terran_Weapons3:
			return starcraft2_Terran_Weapons3;
		case Starcraft2_Third_Blink:
			return starcraft2_Third_Blink;
		case Starcraft2_Time_Warp:
			return starcraft2_Time_Warp;
		case Starcraft2_Vespene_Gas:
			return starcraft2_Vespene_Gas;
		case Starcraft2_Vespene_Siphon:
			return starcraft2_Vespene_Siphon;
		case Starcraft2_Vespene_Drone:
			return starcraft2_Vespene_Drone;
		case Starcraft2_Wraith_Cloak:
			return starcraft2_Wraith_Cloak;
		case Starcraft2_Yellow_Blink:
			return starcraft2_Yellow_Blink;
		case Starcraft2_Heal:
			return starcraft2_Heal;
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
		case Player_EMP_Plus:
			return this.playerEMPPlusFrames;
		case Guardian_Bot:
			return this.guardianBotFrames;
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
			String sourceString = String.format("src/resources/images/gif/PNGtoGIF/Default Player Engine/%d.png", i);
			Image image = imgLoader.getSpritesheetImage(sourceString);
			defaultPlayerEngine.add(image);
		}

		for (int i = 1; i < 6; i++) {
			String sourceString = String
					.format("src/resources/images/gif/PNGtoGIF/Default Player Engine Boosted/%d.png", i);
			Image image = imgLoader.getSpritesheetImage(sourceString);
			defaultPlayerEngineBoostedFrames.add(image);
		}

		for (int i = 1; i < 10; i++) {
			String sourceString = String.format("src/resources/images/gif/PNGtoGIF/Player Shield Damage/%d.png", i);
			Image image = imgLoader.getSpritesheetImage(sourceString);
			defaultPlayerShieldDamage.add(image);
		}

		for (int i = 1; i < 10; i++) {
			String sourceString = String.format("src/resources/images/gif/PNGtoGIF/Player EMP/%d.png", i);
			Image image = imgLoader.getSpritesheetImage(sourceString);
			playerEMPFrames.add(image);
		}
		
		for (int i = 1; i < 16; i++) {
			String sourceString = String.format("src/resources/images/Ships/Guardian Bot/GuardianBot%d.png", i);
			Image image = imgLoader.getSpritesheetImage(sourceString);
			guardianBotFrames.add(image);
		}

		for (int i = 1; i < 46; i++) {
			String sourceString = String.format("src/resources/images/gif/PNGtoGIF/Player Fireswirl/tile0%d.png", i);
			Image image = imgLoader.getSpritesheetImage(sourceString);
			playerFireSwirlFrames.add(image);
		}

	}
	
	private void initSpriteSheets() {
		Image empImage = imgLoader.getImage(ImageEnums.Player_EMP_Plus);
		playerEMPPlusFrames = cutSpriteSheetToImages(empImage, 8, 8);
				
	}

	private List<Image> gifToImageIcons(ImageReader reader) throws IOException {
		int n = reader.getNumImages(true);
		List<Image> imgs = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			imgs.add(new ImageIcon(reader.read(i)).getImage());
		}
		return imgs;
	}

	private List<Image> cutSpriteSheetToImages(Image image, int rows, int cols) {
		final int spriteWidth = image.getWidth(null) / cols; // width of a single sprite
		final int spriteHeight = image.getHeight(null) / rows; // height of a single sprite
		List<Image> sprites = new ArrayList<Image>();
		BufferedImage spriteSheetImage = toBufferedImage(image);
		// load the sprite sheet
		// split the sprite sheet into individual sprites
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				// get the subimage from the sprite sheet
				Image sprite = spriteSheetImage.getSubimage(j * spriteWidth, i * spriteHeight, spriteWidth,
						spriteHeight);
				sprites.add(sprite);
			}
		}
		
		return sprites;
	}
	
	private BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		BufferedImage buff = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = buff.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return buff;
	}

}