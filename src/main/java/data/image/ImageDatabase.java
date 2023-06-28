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
	private Image TitleImage;

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
	
	
	//Font Letters
	private Image letter_A;
	private Image letter_B;
	private Image letter_LowercaseA;
	private Image letter_LowercaseB;

	private Image letter_C;
	private Image letter_D;
	private Image letter_E;
	private Image letter_F;
	private Image letter_G;
	private Image letter_H;
	private Image letter_I;
	private Image letter_J;
	private Image letter_K;
	private Image letter_L;
	private Image letter_M;
	private Image letter_N;
	private Image letter_O;
	private Image letter_P;
	private Image letter_Q;
	private Image letter_R;
	private Image letter_S;
	private Image letter_T;
	private Image letter_U;
	private Image letter_V;
	private Image letter_W;
	private Image letter_X;
	private Image letter_Y;
	private Image letter_Z;

	private Image letter_LowercaseC;
	private Image letter_LowercaseD;
	private Image letter_LowercaseE;
	private Image letter_LowercaseF;
	private Image letter_LowercaseG;
	private Image letter_LowercaseH;
	private Image letter_LowercaseI;
	private Image letter_LowercaseJ;
	private Image letter_LowercaseK;
	private Image letter_LowercaseL;
	private Image letter_LowercaseM;
	private Image letter_LowercaseN;
	private Image letter_LowercaseO;
	private Image letter_LowercaseP;
	private Image letter_LowercaseQ;
	private Image letter_LowercaseR;
	private Image letter_LowercaseS;
	private Image letter_LowercaseT;
	private Image letter_LowercaseU;
	private Image letter_LowercaseV;
	private Image letter_LowercaseW;
	private Image letter_LowercaseX;
	private Image letter_LowercaseY;
	private Image letter_LowercaseZ;

	private Image letter_Open_Bracket;
	private Image letter_Closing_Bracket;
	private Image letter_Double_Points;
	private Image letter_Equals;
	private Image letter_Point_Comma;
	private Image letter_Greater_Than;
	private Image letter_Smaller_Than;
	private Image letter_Dot;
	
	
	
	
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
	private List<Image> firewallParticleFrames = new ArrayList<Image>();

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
	private Image invisibile;
	private List<Image> invisibleAnimation = new ArrayList<Image>();

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
		this.initLetters();
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
		this.invisibile = imgLoader.getImage(ImageEnums.Invisible);
	}

	private void initMenuImages() {
		this.startGameImage = imgLoader.getImage(ImageEnums.Start_Game);
		this.userOneImage = imgLoader.getImage(ImageEnums.User_One);
		this.userTwoImage = imgLoader.getImage(ImageEnums.User_Two);
		this.userThreeImage = imgLoader.getImage(ImageEnums.User_Three);
		this.selectUserMenuImage = imgLoader.getImage(ImageEnums.Select_User_Menu);
		this.testImage = imgLoader.getImage(ImageEnums.Test_Image);
		this.userMenuToMainMenu = imgLoader.getImage(ImageEnums.User_Menu_To_Main_Menu);
		this.TitleImage = imgLoader.getImage(ImageEnums.Title_Image);
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
	
	private void initLetters() {
		this.letter_A = imgLoader.getImage(ImageEnums.Letter_A);
		this.letter_B = imgLoader.getImage(ImageEnums.Letter_B);
		this.letter_C = imgLoader.getImage(ImageEnums.Letter_C);
		this.letter_D = imgLoader.getImage(ImageEnums.Letter_D);
		this.letter_E = imgLoader.getImage(ImageEnums.Letter_E);
		this.letter_F = imgLoader.getImage(ImageEnums.Letter_F);
		this.letter_G = imgLoader.getImage(ImageEnums.Letter_G);
		this.letter_H = imgLoader.getImage(ImageEnums.Letter_H);
		this.letter_I = imgLoader.getImage(ImageEnums.Letter_I);
		this.letter_J = imgLoader.getImage(ImageEnums.Letter_J);
		this.letter_K = imgLoader.getImage(ImageEnums.Letter_K);
		this.letter_L = imgLoader.getImage(ImageEnums.Letter_L);
		this.letter_M = imgLoader.getImage(ImageEnums.Letter_M);
		this.letter_N = imgLoader.getImage(ImageEnums.Letter_N);
		this.letter_O = imgLoader.getImage(ImageEnums.Letter_O);
		this.letter_P = imgLoader.getImage(ImageEnums.Letter_P);
		this.letter_Q = imgLoader.getImage(ImageEnums.Letter_Q);
		this.letter_R = imgLoader.getImage(ImageEnums.Letter_R);
		this.letter_S = imgLoader.getImage(ImageEnums.Letter_S);
		this.letter_T = imgLoader.getImage(ImageEnums.Letter_T);
		this.letter_U = imgLoader.getImage(ImageEnums.Letter_U);
		this.letter_V = imgLoader.getImage(ImageEnums.Letter_V);
		this.letter_W = imgLoader.getImage(ImageEnums.Letter_W);
		this.letter_X = imgLoader.getImage(ImageEnums.Letter_X);
		this.letter_Y = imgLoader.getImage(ImageEnums.Letter_Y);
		this.letter_Z = imgLoader.getImage(ImageEnums.Letter_Z);

		this.letter_LowercaseA = imgLoader.getImage(ImageEnums.Letter_a);
		this.letter_LowercaseB = imgLoader.getImage(ImageEnums.Letter_b);
		this.letter_LowercaseC = imgLoader.getImage(ImageEnums.Letter_c);
		this.letter_LowercaseD = imgLoader.getImage(ImageEnums.Letter_d);
		this.letter_LowercaseE = imgLoader.getImage(ImageEnums.Letter_e);
		this.letter_LowercaseF = imgLoader.getImage(ImageEnums.Letter_f);
		this.letter_LowercaseG = imgLoader.getImage(ImageEnums.Letter_g);
		this.letter_LowercaseH = imgLoader.getImage(ImageEnums.Letter_h);
		this.letter_LowercaseI = imgLoader.getImage(ImageEnums.Letter_i);
		this.letter_LowercaseJ = imgLoader.getImage(ImageEnums.Letter_j);
		this.letter_LowercaseK = imgLoader.getImage(ImageEnums.Letter_k);
		this.letter_LowercaseL = imgLoader.getImage(ImageEnums.Letter_l);
		this.letter_LowercaseM = imgLoader.getImage(ImageEnums.Letter_m);
		this.letter_LowercaseN = imgLoader.getImage(ImageEnums.Letter_n);
		this.letter_LowercaseO = imgLoader.getImage(ImageEnums.Letter_o);
		this.letter_LowercaseP = imgLoader.getImage(ImageEnums.Letter_p);
		this.letter_LowercaseQ = imgLoader.getImage(ImageEnums.Letter_q);
		this.letter_LowercaseR = imgLoader.getImage(ImageEnums.Letter_r);
		this.letter_LowercaseS = imgLoader.getImage(ImageEnums.Letter_s);
		this.letter_LowercaseT = imgLoader.getImage(ImageEnums.Letter_t);
		this.letter_LowercaseU = imgLoader.getImage(ImageEnums.Letter_u);
		this.letter_LowercaseV = imgLoader.getImage(ImageEnums.Letter_v);
		this.letter_LowercaseW = imgLoader.getImage(ImageEnums.Letter_w);
		this.letter_LowercaseX = imgLoader.getImage(ImageEnums.Letter_x);
		this.letter_LowercaseY = imgLoader.getImage(ImageEnums.Letter_y);
		this.letter_LowercaseZ = imgLoader.getImage(ImageEnums.Letter_z);

		this.letter_Open_Bracket = imgLoader.getImage(ImageEnums.Letter_Open_Bracket);
		this.letter_Closing_Bracket = imgLoader.getImage(ImageEnums.Letter_Closing_Bracket);
		this.letter_Double_Points = imgLoader.getImage(ImageEnums.Letter_double_points);
		this.letter_Equals = imgLoader.getImage(ImageEnums.Letter_equals);
		this.letter_Point_Comma = imgLoader.getImage(ImageEnums.Letter_point_comma);
		this.letter_Greater_Than = imgLoader.getImage(ImageEnums.Letter_greater_than);
		this.letter_Smaller_Than = imgLoader.getImage(ImageEnums.Letter_smaller_than);
		this.letter_Dot = imgLoader.getImage(ImageEnums.Letter_Dot);
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
		case Letter_A:
			return letter_A;
		case Letter_a:
			return letter_LowercaseA;
		case Letter_B:
		    return letter_B;
		case Letter_C:
		    return letter_C;
		case Letter_D:
		    return letter_D;
		case Letter_E:
		    return letter_E;
		case Letter_F:
		    return letter_F;
		case Letter_G:
		    return letter_G;
		case Letter_H:
		    return letter_H;
		case Letter_I:
		    return letter_I;
		case Letter_J:
		    return letter_J;
		case Letter_K:
		    return letter_K;
		case Letter_L:
		    return letter_L;
		case Letter_M:
		    return letter_M;
		case Letter_N:
		    return letter_N;
		case Letter_O:
		    return letter_O;
		case Letter_P:
		    return letter_P;
		case Letter_Q:
		    return letter_Q;
		case Letter_R:
		    return letter_R;
		case Letter_S:
		    return letter_S;
		case Letter_T:
		    return letter_T;
		case Letter_U:
		    return letter_U;
		case Letter_V:
		    return letter_V;
		case Letter_W:
		    return letter_W;
		case Letter_X:
		    return letter_X;
		case Letter_Y:
		    return letter_Y;
		case Letter_Z:
		    return letter_Z;
		case Letter_b:
		    return letter_LowercaseB;
		case Letter_c:
		    return letter_LowercaseC;
		case Letter_z:
		    return letter_LowercaseZ;
		case Letter_Open_Bracket:
		    return letter_Open_Bracket;
		case Letter_Closing_Bracket:
		    return letter_Closing_Bracket;
		case Letter_double_points:
		    return letter_Double_Points;
		case Letter_equals:
		    return letter_Equals;
		case Letter_point_comma:
		    return letter_Point_Comma;
		case Letter_greater_than:
		    return letter_Greater_Than;
		case Letter_smaller_than:
		    return letter_Smaller_Than;
		case Letter_Dot:
		    return letter_Dot;
		case Letter_d:
		    return letter_LowercaseD;
		case Letter_e:
		    return letter_LowercaseE;
		case Letter_f:
		    return letter_LowercaseF;
		case Letter_g:
		    return letter_LowercaseG;
		case Letter_h:
		    return letter_LowercaseH;
		case Letter_i:
		    return letter_LowercaseI;
		case Letter_j:
		    return letter_LowercaseJ;
		case Letter_k:
		    return letter_LowercaseK;
		case Letter_l:
		    return letter_LowercaseL;
		case Letter_m:
		    return letter_LowercaseM;
		case Letter_n:
		    return letter_LowercaseN;
		case Letter_o:
		    return letter_LowercaseO;
		case Letter_p:
		    return letter_LowercaseP;
		case Letter_q:
		    return letter_LowercaseQ;
		case Letter_r:
		    return letter_LowercaseR;
		case Letter_s:
		    return letter_LowercaseS;
		case Letter_t:
		    return letter_LowercaseT;
		case Letter_u:
		    return letter_LowercaseU;
		case Letter_v:
		    return letter_LowercaseV;
		case Letter_w:
		    return letter_LowercaseW;
		case Letter_x:
		    return letter_LowercaseX;
		case Letter_y:
		    return letter_LowercaseY;
		case Title_Image:
			return TitleImage;
		case Invisible:
			return invisibile;

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
		case Flamethrower_Animation:
			return this.destroyedExplosionRightFrames;
		case FirewallParticle:
			return firewallParticleFrames;
		case Invisible_Animation:
			return invisibleAnimation;
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
		//Start all of them with 1 because the filenames start with numbering with 1
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
		
		for (int i = 1; i < 5; i++) {
			String sourceString = String.format("src/resources/images/gif/PNGtoGIF/Firewall/flame%d.png", i);
			Image image = imgLoader.getSpritesheetImage(sourceString);
			firewallParticleFrames.add(image);
		}
		
		
		for (int i = 1; i < 4; i++) {
			Image image = imgLoader.getImage(ImageEnums.Invisible);
			invisibleAnimation.add(image);
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