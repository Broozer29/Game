package VisualAndAudioData.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

public class ImageDatabase {

    private static ImageDatabase instance = new ImageDatabase();
    private ImageLoader imgLoader = ImageLoader.getInstance();

    // Friendly images
    private BufferedImage spaceShipImage;
    private BufferedImage model3BetterUpgrade;

    // Enemy images
    private BufferedImage alienSpaceshipImage;
    private BufferedImage alienBombImage;
    private BufferedImage seekerImage;
    private BufferedImage tazerImage;
    private BufferedImage energizerImage;
    private BufferedImage bulldozerImage;
    private BufferedImage flamerImage;
    private BufferedImage bombaImage;

    private List<BufferedImage> alienBombFrames = new ArrayList<BufferedImage>();

    // Ship numbers to names:
    /*
     * Ship1 = Seeker Ship2 = Tazer Ship3 = Energizer Ship4 = Bulldozer Ship5 = Flamer
     * Ship6 = Bomba
     */

    // Projectile images
    private BufferedImage alienLaserbeamImage;
    private BufferedImage laserBeamImage;


    // Menu images
    private BufferedImage startGameImage;
    private BufferedImage userOneImage;
    private BufferedImage userTwoImage;
    private BufferedImage userThreeImage;
    private BufferedImage selectUserMenuImage;
    private BufferedImage userMenuToMainMenu;
    private BufferedImage titleImage;
    private BufferedImage longCard;
    private BufferedImage wideCard;
    private BufferedImage squareCard;

    // Game GUI images
    private BufferedImage healthBarImage;
    private BufferedImage shieldBarImage;
    private BufferedImage frame;
    private BufferedImage iconBorder;
    private BufferedImage redFilling;
    private BufferedImage goldFilling;
    private BufferedImage blueFilling;

    // Icons
    private BufferedImage tripleShotIcon;
    private BufferedImage doubleShotIcon;
    private BufferedImage starcraft2_Point_Defense_Drone;
    private BufferedImage starcraft2_Protoss_Cloak;
    private BufferedImage starcraft2_Protoss_Shield_Disintegrate;
    private BufferedImage starcraft2_Protoss_Shields_1;
    private BufferedImage starcraft2_Protoss_Shields_2;
    private BufferedImage starcraft2_Protoss_Shields_3;
    private BufferedImage starcraft2_Psi_Storm1;
    private BufferedImage starcraft2_Psi_Storm2;
    private BufferedImage starcraft2_Psi_Storm3;
    private BufferedImage starcraft2_Pulse_Grenade;
    private BufferedImage starcraft2_Pulse_Laser;
    private BufferedImage starcraft2_Repair_Blink;
    private BufferedImage starcraft2_Rocket_Cluster;
    private BufferedImage starcraft2_Dual_Rockets;
    private BufferedImage starcraft2_Artanis_Shield;
    private BufferedImage starcraft2_Auto_Tracking;
    private BufferedImage starcraft2_Blink;
    private BufferedImage starcraft2_Blue_Flame;
    private BufferedImage starcraft2_Concussive_Shells;
    private BufferedImage starcraft2_Corsair_Cloak;
    private BufferedImage starcraft2_Drone_Cloak;
    private BufferedImage starcraft2_DT_Blink;
    private BufferedImage starcraft2_Energizer_Speed;
    private BufferedImage starcraft2_Energizer_Speed2;
    private BufferedImage starcraft2_Fire_Cloak;
    private BufferedImage starcraft2_Energy_Siphon;
    private BufferedImage starcraft2_Fire_Hardened_Shields;
    private BufferedImage starcraft2_Flame_Turret;
    private BufferedImage starcraft2_Force_Field;
    private BufferedImage starcraft2_Guardian_Shield;
    private BufferedImage starcraft2_Hardened_Shields;
    private BufferedImage starcraft2_Health_Upgrade_1;
    private BufferedImage starcraft2_Health_Upgrade_2;
    private BufferedImage starcraft2_Ignite_Afterburners;
    private BufferedImage starcraft2_Immortal_Barrier;
    private BufferedImage starcraft2_Immortal_Original_Barrier;
    private BufferedImage starcraft2_LaserBeam;
    private BufferedImage starcraft2_LaserDrill;
    private BufferedImage starcraft2_MovementSpeed;
    private BufferedImage starcraft2_Seeker_Missile;
    private BufferedImage starcraft2_Shield_Barrier;
    private BufferedImage starcraft2_Shield_Piercing;
    private BufferedImage starcraft2_Stim1;
    private BufferedImage starcraft2_Stim2;
    private BufferedImage starcraft2_Stim3;
    private BufferedImage starcraft2_Terran_Plating1;
    private BufferedImage starcraft2_Terran_Plating2;
    private BufferedImage starcraft2_Terran_Plating3;
    private BufferedImage starcraft2_Terran_Speed1;
    private BufferedImage starcraft2_Terran_Speed2;
    private BufferedImage starcraft2_Terran_Speed3;
    private BufferedImage starcraft2_Terran_Weapons1;
    private BufferedImage starcraft2_Terran_Weapons2;
    private BufferedImage starcraft2_Terran_Weapons3;
    private BufferedImage starcraft2_Third_Blink;
    private BufferedImage starcraft2_Time_Warp;
    private BufferedImage starcraft2_Vespene_Gas;
    private BufferedImage starcraft2_Vespene_Siphon;
    private BufferedImage starcraft2_Vespene_Drone;
    private BufferedImage starcraft2_Wraith_Cloak;
    private BufferedImage starcraft2_Yellow_Blink;
    private BufferedImage starcraft2_Heal;
    private BufferedImage starcraft2_Electric_Field;
    private BufferedImage starcraft2_Firebat_Weapon;
    private BufferedImage starcraft2_Advanced_Optics;
    private BufferedImage cannisterOfGasoline;
    private BufferedImage starcraft2_PlatinumSponge;

    private BufferedImage starcraft2_Battery;
    private BufferedImage starcraft2_FocusedCrystal;

    private BufferedImage starcraft2_Overclock;
    private BufferedImage starcraft2_ArmorPiercing;
    private BufferedImage moneyPrinter;
    private BufferedImage stickyDynamite;

    // Font Letters
    private BufferedImage letter_A;
    private BufferedImage letter_B;
    private BufferedImage letter_LowercaseA;
    private BufferedImage letter_LowercaseB;

    private BufferedImage letter_C;
    private BufferedImage letter_D;
    private BufferedImage letter_E;
    private BufferedImage letter_F;
    private BufferedImage letter_G;
    private BufferedImage letter_H;
    private BufferedImage letter_I;
    private BufferedImage letter_J;
    private BufferedImage letter_K;
    private BufferedImage letter_L;
    private BufferedImage letter_M;
    private BufferedImage letter_N;
    private BufferedImage letter_O;
    private BufferedImage letter_P;
    private BufferedImage letter_Q;
    private BufferedImage letter_R;
    private BufferedImage letter_S;
    private BufferedImage letter_T;
    private BufferedImage letter_U;
    private BufferedImage letter_V;
    private BufferedImage letter_W;
    private BufferedImage letter_X;
    private BufferedImage letter_Y;
    private BufferedImage letter_Z;

    private BufferedImage letter_LowercaseC;
    private BufferedImage letter_LowercaseD;
    private BufferedImage letter_LowercaseE;
    private BufferedImage letter_LowercaseF;
    private BufferedImage letter_LowercaseG;
    private BufferedImage letter_LowercaseH;
    private BufferedImage letter_LowercaseI;
    private BufferedImage letter_LowercaseJ;
    private BufferedImage letter_LowercaseK;
    private BufferedImage letter_LowercaseL;
    private BufferedImage letter_LowercaseM;
    private BufferedImage letter_LowercaseN;
    private BufferedImage letter_LowercaseO;
    private BufferedImage letter_LowercaseP;
    private BufferedImage letter_LowercaseQ;
    private BufferedImage letter_LowercaseR;
    private BufferedImage letter_LowercaseS;
    private BufferedImage letter_LowercaseT;
    private BufferedImage letter_LowercaseU;
    private BufferedImage letter_LowercaseV;
    private BufferedImage letter_LowercaseW;
    private BufferedImage letter_LowercaseX;
    private BufferedImage letter_LowercaseY;
    private BufferedImage letter_LowercaseZ;
    private BufferedImage letter_Percentage;
    private BufferedImage letter_Komma;
    private BufferedImage letter_Open_Bracket;
    private BufferedImage letter_Closing_Bracket;
    private BufferedImage letter_Double_Points;
    private BufferedImage letter_Equals;
    private BufferedImage letter_Point_Comma;
    private BufferedImage letter_Greater_Than;
    private BufferedImage letter_Smaller_Than;
    private BufferedImage letter_Dot;

    private BufferedImage letter_Zero;
    private BufferedImage letter_One;
    private BufferedImage letter_Two;
    private BufferedImage letter_Three;
    private BufferedImage letter_Four;
    private BufferedImage letter_Five;
    private BufferedImage letter_Six;
    private BufferedImage letter_Seven;
    private BufferedImage letter_Eight;
    private BufferedImage letter_Nine;


    // Lists
    private List<BufferedImage> impactExplosionOneFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> destroyedExplosionUpFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> destroyedExplosionLeftFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> destroyedExplosionRightFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> destroyedExplosionDownFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> alienBombExplosionFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> implosionFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> seekerDestroyedExplosionFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> tazerDestroyedExplosionFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> energizerDestroyedExplosionFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> bulldozerDestroyedExplosionFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> flamerDestroyedExplosionFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> bombaDestroyedExplosionFrames = new ArrayList<BufferedImage>();

    // Animations
    private List<BufferedImage> playerEngineFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> playerFireSwirlFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> playerEMPFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> playerEMPPlusFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> defaultPlayerEngineBoostedFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> defaultPlayerShieldDamage = new ArrayList<BufferedImage>();
    private List<BufferedImage> guardianBotFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> firewallParticleFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> fireSpoutFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> portal5Frames = new ArrayList<BufferedImage>();
    private List<BufferedImage> warpFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> shield4Frames = new ArrayList<BufferedImage>();
    private List<BufferedImage> shield3Frames = new ArrayList<BufferedImage>();
    private List<BufferedImage> shield2Frames = new ArrayList<BufferedImage>();
    private List<BufferedImage> chargingFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> energyCircleFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> flamewarpFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> frontShieldFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> frontShieldBFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> pulsatingShieldFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> pulsatingStarFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> radarFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> rotatingBoxesFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> rotatingConeFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> greenEnergyOrbFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> purpleEnergyBlockFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> fireshieldFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> energyFlowerFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> purpleEnergyBarrier = new ArrayList<BufferedImage>();
    private List<BufferedImage> redHoleFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> crossingEnergyBeams = new ArrayList<BufferedImage>();

    private List<BufferedImage> plasmaCoatedDebuff = new ArrayList<BufferedImage>();
    private List<BufferedImage> gasolineExplosion = new ArrayList<>();
    private List<BufferedImage> gasolineBurning = new ArrayList<>();
    private List<BufferedImage> healingAnimation = new ArrayList<>();
    private List<BufferedImage> stickyDynamiteExplosion = new ArrayList<>();
    private List<BufferedImage> plasmaLauncherMissileFrames = new ArrayList<>();

    // Enemy Projectile Animations
    private List<BufferedImage> seekerProjectileFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> tazerProjectileFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> energizerProjectileFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> bulldozerProjectileFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> flamerProjectileFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> bombaProjectileFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> Rocket1ProjectileFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> Rocket1ExplosionFrames = new ArrayList<BufferedImage>();

    // Enemy Exhaust Animations
    private List<BufferedImage> seekerNormalExhaustFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> seekerLargeExhaustFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> tazerNormalExhaustFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> tazerLargeExhaustFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> energizerNormalExhaustFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> energizerLargeExhaustFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> bulldozerNormalExhaustFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> bulldozerLargeExhaustFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> flamerNormalExhaustFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> flamerLargeExhaustFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> bombaNormalExhaustFrames = new ArrayList<BufferedImage>();
    private List<BufferedImage> bombaLargeExhaustFrames = new ArrayList<BufferedImage>();

    // Enemy projectile explosions
    private List<BufferedImage> seekerProjectileExplosion = new ArrayList<BufferedImage>();
    private List<BufferedImage> tazerProjectileExplosion = new ArrayList<BufferedImage>();
    private List<BufferedImage> energizerProjectileExplosion = new ArrayList<BufferedImage>();
    private List<BufferedImage> bulldozerProjectileExplosion = new ArrayList<BufferedImage>();
    private List<BufferedImage> flamerProjectileExplosion = new ArrayList<BufferedImage>();
    private List<BufferedImage> bombaProjectileExplosion = new ArrayList<BufferedImage>();

    // Background images
    private BufferedImage moon;
    private BufferedImage moon1;
    private BufferedImage moon2;
    private BufferedImage moon3;
    private BufferedImage moon4;
    private BufferedImage moon5;
    private BufferedImage greenPlanet1;
    private BufferedImage greenPlanet2;
    private BufferedImage bluePlanet1;
    private BufferedImage bluePlanet2;
    private BufferedImage bluePlanet3;
    private BufferedImage bluePlanet4;
    private BufferedImage bluePlanet5;
    private BufferedImage bluePlanet6;
    private BufferedImage lavaPlanet;
    private BufferedImage marsPlanet;
    private BufferedImage planetOne;
    private BufferedImage planetTwo;
    private BufferedImage planetThree;
    private BufferedImage star;
    private BufferedImage parralex1;
    private BufferedImage parralex2;
    private BufferedImage parralex3;
    private BufferedImage parralex4;
    private BufferedImage parralex5;
    private BufferedImage warmNebula;
    private BufferedImage coldNebula;
    private BufferedImage regularNebula;
    private BufferedImage blueNebula1;
    private BufferedImage blueNebula2;
    private BufferedImage blueNebula3;
    private BufferedImage blueNebula4;
    private BufferedImage blueNebula5;
    private BufferedImage blueNebula6;
    private BufferedImage greenNebula1;
    private BufferedImage greenNebula2;
    private BufferedImage greenNebula3;
    private BufferedImage greenNebula4;
    private BufferedImage greenNebula5;
    private BufferedImage greenNebula6;
    private BufferedImage greenNebula7;
    private BufferedImage purpleNebula1;
    private BufferedImage purpleNebula2;
    private BufferedImage purpleNebula3;
    private BufferedImage purpleNebula4;
    private BufferedImage purpleNebula5;
    private BufferedImage purpleNebula6;
    private BufferedImage purpleNebula7;

    private BufferedImage starRed1;
    private BufferedImage starRed2;
    private BufferedImage starRed3;
    private BufferedImage starRed4;

    private BufferedImage starYellow1;
    private BufferedImage starYellow2;
    private BufferedImage starYellow3;
    private BufferedImage starYellow4;

    private BufferedImage starWhite1;
    private BufferedImage starWhite2;
    private BufferedImage starWhite3;
    private BufferedImage starWhite4;

    private BufferedImage starBlue1;
    private BufferedImage starBlue2;
    private BufferedImage starBlue3;
    private BufferedImage starBlue4;

    private BufferedImage starOrange1;
    private BufferedImage starOrange2;
    private BufferedImage starOrange3;
    private BufferedImage starOrange4;

    private List<BufferedImage> HighlightImages = new ArrayList<BufferedImage>();

    // testimages
    private BufferedImage testImage;
    private BufferedImage invisibile;
    private List<BufferedImage> invisibleAnimation = new ArrayList<BufferedImage>();

    // Images to Gifs
    private List<BufferedImage> defaultPlayerEngine = new ArrayList<BufferedImage>();

    private ImageDatabase () {
        initializeImages();
    }

    public static ImageDatabase getInstance () {
        return instance;
    }

    private void initializeImages () {
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

    private void initFriendlies () {
        this.spaceShipImage = imgLoader.getImage(ImageEnums.Player_Spaceship);
        this.model3BetterUpgrade = imgLoader.getImage(ImageEnums.Player_Spaceship_Model_3);
    }

    private void initEnemies () {
        this.alienSpaceshipImage = imgLoader.getImage(ImageEnums.Alien);
        this.alienBombImage = imgLoader.getImage(ImageEnums.Alien_Bomb);
        this.seekerImage = imgLoader.getImage(ImageEnums.Seeker);
        this.tazerImage = imgLoader.getImage(ImageEnums.Tazer);
        this.energizerImage = imgLoader.getImage(ImageEnums.Energizer);
        this.bulldozerImage = imgLoader.getImage(ImageEnums.Bulldozer);
        this.flamerImage = imgLoader.getImage(ImageEnums.Flamer);
        this.bombaImage = imgLoader.getImage(ImageEnums.Bomba);
    }

    private void initProjectiles () {
        this.laserBeamImage = imgLoader.getImage(ImageEnums.Player_Laserbeam);
        this.alienLaserbeamImage = imgLoader.getImage(ImageEnums.Alien_Laserbeam);
        this.invisibile = imgLoader.getImage(ImageEnums.Invisible);
    }

    private void initMenuImages () {
        this.startGameImage = imgLoader.getImage(ImageEnums.Start_Game);
        this.userOneImage = imgLoader.getImage(ImageEnums.User_One);
        this.userTwoImage = imgLoader.getImage(ImageEnums.User_Two);
        this.userThreeImage = imgLoader.getImage(ImageEnums.User_Three);
        this.selectUserMenuImage = imgLoader.getImage(ImageEnums.Select_User_Menu);
        this.testImage = imgLoader.getImage(ImageEnums.Test_Image);
        this.userMenuToMainMenu = imgLoader.getImage(ImageEnums.User_Menu_To_Main_Menu);
        this.titleImage = imgLoader.getImage(ImageEnums.Title_Image);
    }

    private void initGameUIobjects () {
        this.healthBarImage = imgLoader.getImage(ImageEnums.Health_Bar);
        this.shieldBarImage = imgLoader.getImage(ImageEnums.Shield_Bar);
        this.frame = imgLoader.getImage(ImageEnums.Frame);
        this.iconBorder = imgLoader.getImage(ImageEnums.Icon_Border);
        this.redFilling = imgLoader.getImage(ImageEnums.Red_Filling);
        this.goldFilling = imgLoader.getImage(ImageEnums.Gold_Filling);
        this.blueFilling = imgLoader.getImage(ImageEnums.Blue_Filling);
    }

    private void initIcons () {
        this.tripleShotIcon = imgLoader.getImage(ImageEnums.TripleShotIcon);
        this.doubleShotIcon = imgLoader.getImage(ImageEnums.DoubleShotIcon);
        this.starcraft2_Point_Defense_Drone = imgLoader.getImage(ImageEnums.Starcraft2_Point_Defense_Drone);
        this.starcraft2_Protoss_Cloak = imgLoader.getImage(ImageEnums.Starcraft2_Protoss_Cloak);
        this.starcraft2_Protoss_Shield_Disintegrate = imgLoader
                .getImage(ImageEnums.Starcraft2_Protoss_Shield_Disintegrate);
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
        this.starcraft2_Electric_Field = imgLoader.getImage(ImageEnums.Starcraft2_Electric_Field);
        this.starcraft2_Firebat_Weapon = imgLoader.getImage(ImageEnums.Starcraft2_Firebat_Weapon);
        this.starcraft2_Advanced_Optics = imgLoader.getImage(ImageEnums.Starcraft2_Advanced_Optics);
        this.cannisterOfGasoline = imgLoader.getImage(ImageEnums.CannisterOfGasoline);
        this.starcraft2_Battery = imgLoader.getImage(ImageEnums.Starcraft2_Battery);
        this.starcraft2_FocusedCrystal = imgLoader.getImage(ImageEnums.Starcraft2_Focused_Crystal);
        this.starcraft2_PlatinumSponge = imgLoader.getImage(ImageEnums.Starcraft2_Platinum_Sponge);
        this.starcraft2_Overclock = imgLoader.getImage(ImageEnums.Starcraft2_Overclock);
        this.starcraft2_ArmorPiercing = imgLoader.getImage(ImageEnums.Starcraft2_Armor_Piercing);
        this.moneyPrinter = imgLoader.getImage(ImageEnums.MoneyPrinter);
        this.stickyDynamite = imgLoader.getImage(ImageEnums.StickyDynamite);
    }


    private void initLetters () {
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
        this.letter_Percentage = imgLoader.getImage(ImageEnums.Letter_Percentage);
        this.letter_Komma = imgLoader.getImage(ImageEnums.Letter_Komma);

        this.letter_Zero = imgLoader.getImage(ImageEnums.Letter_Zero);
        this.letter_One = imgLoader.getImage(ImageEnums.Letter_One);
        this.letter_Two = imgLoader.getImage(ImageEnums.Letter_Two);
        this.letter_Three = imgLoader.getImage(ImageEnums.Letter_Three);
        this.letter_Four = imgLoader.getImage(ImageEnums.Letter_Four);
        this.letter_Five = imgLoader.getImage(ImageEnums.Letter_Five);
        this.letter_Six = imgLoader.getImage(ImageEnums.Letter_Six);
        this.letter_Seven = imgLoader.getImage(ImageEnums.Letter_Seven);
        this.letter_Eight = imgLoader.getImage(ImageEnums.Letter_Eight);
        this.letter_Nine = imgLoader.getImage(ImageEnums.Letter_Nine);

        this.longCard = imgLoader.getImage(ImageEnums.Long_Card);
        this.wideCard = imgLoader.getImage(ImageEnums.Wide_Card);
        this.squareCard = imgLoader.getImage(ImageEnums.Square_Card);
    }

    private void initBackgroundObjects () {
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

        this.starBlue1 = imgLoader.getImage(ImageEnums.Star_Blue1);
        this.starBlue2 = imgLoader.getImage(ImageEnums.Star_Blue2);
        this.starBlue3 = imgLoader.getImage(ImageEnums.Star_Blue3);
        this.starBlue4 = imgLoader.getImage(ImageEnums.Star_Blue4);

        this.starRed1 = imgLoader.getImage(ImageEnums.Star_Red1);
        this.starRed2 = imgLoader.getImage(ImageEnums.Star_Red2);
        this.starRed3 = imgLoader.getImage(ImageEnums.Star_Red3);
        this.starRed4 = imgLoader.getImage(ImageEnums.Star_Red4);

        this.starOrange1 = imgLoader.getImage(ImageEnums.Star_Orange1);
        this.starOrange2 = imgLoader.getImage(ImageEnums.Star_Orange2);
        this.starOrange3 = imgLoader.getImage(ImageEnums.Star_Orange3);
        this.starOrange4 = imgLoader.getImage(ImageEnums.Star_Orange4);

        this.starWhite1 = imgLoader.getImage(ImageEnums.Star_White1);
        this.starWhite2 = imgLoader.getImage(ImageEnums.Star_White2);
        this.starWhite3 = imgLoader.getImage(ImageEnums.Star_White3);
        this.starWhite4 = imgLoader.getImage(ImageEnums.Star_White4);

        this.starYellow1 = imgLoader.getImage(ImageEnums.Star_Yellow1);
        this.starYellow2 = imgLoader.getImage(ImageEnums.Star_Yellow2);
        this.starYellow3 = imgLoader.getImage(ImageEnums.Star_Yellow3);
        this.starYellow4 = imgLoader.getImage(ImageEnums.Star_Yellow4);

        this.moon2 = imgLoader.getImage(ImageEnums.Moon2);
        this.moon3 = imgLoader.getImage(ImageEnums.Moon3);
        this.moon4 = imgLoader.getImage(ImageEnums.Moon4);
        this.moon5 = imgLoader.getImage(ImageEnums.Moon5);
        this.greenPlanet1 = imgLoader.getImage(ImageEnums.GreenPlanet1);
        this.greenPlanet2 = imgLoader.getImage(ImageEnums.GreenPlanet2);
        this.bluePlanet1  = imgLoader.getImage(ImageEnums.BluePlanet1);
        this.bluePlanet2  = imgLoader.getImage(ImageEnums.BluePlanet2);
        this.bluePlanet3  = imgLoader.getImage(ImageEnums.BluePlanet3);
        this.bluePlanet4  = imgLoader.getImage(ImageEnums.BluePlanet4);
        this.bluePlanet5  = imgLoader.getImage(ImageEnums.BluePlanet5);
        this.bluePlanet6  = imgLoader.getImage(ImageEnums.BluePlanet6);
    }

    public BufferedImage getImage (ImageEnums imageType) {
        switch (imageType) {
            case Moon2: return moon2;
            case Moon3: return moon3;
            case Moon4: return moon4;
            case Moon5: return moon5;
            case GreenPlanet1: return greenPlanet1;
            case GreenPlanet2: return greenPlanet2;
            case BluePlanet1: return bluePlanet1;
            case BluePlanet2: return bluePlanet2;
            case BluePlanet3: return bluePlanet3;
            case BluePlanet4: return bluePlanet4;
            case BluePlanet5: return bluePlanet5;
            case BluePlanet6: return bluePlanet6;
            case Star_White1: return starWhite1;
            case Star_White2: return starWhite2;
            case Star_White3: return starWhite3;
            case Star_White4: return starWhite4;
            case Star_Red1: return starRed1;
            case Star_Red2: return starRed2;
            case Star_Red3: return starRed3;
            case Star_Red4: return starRed4;
            case Star_Orange1: return starOrange1;
            case Star_Orange2: return starOrange2;
            case Star_Orange3: return starOrange3;
            case Star_Orange4: return starOrange4;
            case Star_Yellow1: return starYellow1;
            case Star_Yellow2: return starYellow2;
            case Star_Yellow3: return starYellow3;
            case Star_Yellow4: return starYellow4;
            case Star_Blue1: return starBlue1;
            case Star_Blue2: return starBlue2;
            case Star_Blue3: return starBlue3;
            case Star_Blue4: return starBlue4;

            case Starcraft2_Focused_Crystal:return starcraft2_FocusedCrystal;
            case Starcraft2_Platinum_Sponge:return starcraft2_PlatinumSponge;
            case Starcraft2_Overclock: return starcraft2_Overclock;
            case Starcraft2_Armor_Piercing: return starcraft2_ArmorPiercing;
            case MoneyPrinter: return moneyPrinter;
            case StickyDynamite: return stickyDynamite;
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
                return this.frame;
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
                return titleImage;
            case Invisible:
                return invisibile;
            case Starcraft2_Electric_Field:
                return starcraft2_Electric_Field;
            case Starcraft2_Firebat_Weapon:
                return starcraft2_Firebat_Weapon;
            case Long_Card:
                return longCard;
            case Square_Card:
                return squareCard;
            case Wide_Card:
                return wideCard;
            case Letter_Percentage:
                return letter_Percentage;
            case Letter_Komma:
                return letter_Komma;
            case Letter_Zero:
                return letter_Zero;
            case Letter_One:
                return letter_One;
            case Letter_Two:
                return letter_Two;
            case Letter_Three:
                return letter_Three;
            case Letter_Four:
                return letter_Four;
            case Letter_Five:
                return letter_Five;
            case Letter_Six:
                return letter_Six;
            case Letter_Seven:
                return letter_Seven;
            case Letter_Eight:
                return letter_Eight;
            case Letter_Nine:
                return letter_Nine;
            case CannisterOfGasoline:
                return cannisterOfGasoline;
            case Starcraft2_Advanced_Optics:
                return starcraft2_Advanced_Optics;
            case Starcraft2_Battery:
                return starcraft2_Battery;
            case Blue_Filling:
                return blueFilling;
            default:
                return testImage;
        }
    }

    public List<BufferedImage> getAnimation (ImageEnums imageType) {
        switch (imageType) {
            case PlasmaLauncherMissile:
                return plasmaLauncherMissileFrames;
            case StickyDynamiteExplosion:
                return stickyDynamiteExplosion;
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
            case Drone:
                return this.guardianBotFrames;
            case Flamethrower_Animation:
                return this.destroyedExplosionRightFrames;
            case FirewallParticle:
                return firewallParticleFrames;
            case Invisible_Animation:
                return invisibleAnimation;
            case Highlight:
                return HighlightImages;
            case Rocket_1:
                return Rocket1ProjectileFrames;
            case Rocket_1_Explosion:
                return Rocket1ExplosionFrames;
            case Firespout_Animation:
                return fireSpoutFrames;
            case Alien_Bomb_Animation:
                return alienBombFrames;
            case Portal5:
                return portal5Frames;
            case Warp:
                return warpFrames;
            case Shield4:
                return shield4Frames; // animation addition update: unused
            case Shield3:
                return shield3Frames; // animation addition update: unused
            case Shield2:
                return shield2Frames; // animation addition update: unused
            case Charging:
                return chargingFrames; // animation addition update: unused
            case EnergyCircle:
                return energyCircleFrames; // animation addition update: unused
            case Flamewarp:
                return flamewarpFrames; // animation addition update: unused
            case Frontshield:
                return frontShieldFrames; // animation addition update: unused
            case FrontshieldB:
                return frontShieldBFrames; // animation addition update: unused
            case PulsatingShield:
                return pulsatingShieldFrames; // animation addition update: unused
            case PulsatingStar:
                return pulsatingStarFrames; // animation addition update: unused
            case Radar:
                return radarFrames; // animation addition update: unused
            case RotatingBoxes:
                return rotatingBoxesFrames; // animation addition update: unused
            case RotatingCones:
                return rotatingConeFrames; // animation addition update: unused
            case GreenEnergyOrb:
                return greenEnergyOrbFrames; // animation addition update: unused
            case PurpleEnergyBlocks:
                return purpleEnergyBlockFrames; // animation addition update: unused
            case Fireshield:
                return fireshieldFrames; // animation addition update: unused
            case EnergyFlower:
                return energyFlowerFrames; // animation addition update: unused
            case PurpleEnergyBarrier:
                return purpleEnergyBarrier; // animation addition update: unused
            case RedHole:
                return redHoleFrames; // animation addition update: unused
            case CrossingEnergyBeams:
                return crossingEnergyBeams;  // animation addition update: unused
            case PlasmaCoatedDebuff:
                return plasmaCoatedDebuff;
            case GasolineExplosion:
                return gasolineExplosion;
            case GasolineBurning:
                return gasolineBurning;
            case Healing:
                return healingAnimation;
        }
        return null;
    }

    public void initAnimations () throws FileNotFoundException, IOException {
        ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();

        reader.setInput(ImageIO.createImageInputStream(getClass().getResourceAsStream("/images/gif/explosion.gif")));
        impactExplosionOneFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(getClass().getResourceAsStream("/images/gif/enginesmoke.gif")));
        playerEngineFrames = gifToImageIcons(reader);

        reader.setInput(
                ImageIO.createImageInputStream(getClass().getResourceAsStream("/images/gif/Destroyed Explosion.gif")));
        destroyedExplosionUpFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO
                .createImageInputStream(getClass().getResourceAsStream("/images/gif/Destroyed Explosion Right.gif")));
        destroyedExplosionRightFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO
                .createImageInputStream(getClass().getResourceAsStream("/images/gif/Destroyed Explosion Left.gif")));
        destroyedExplosionDownFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO
                .createImageInputStream(getClass().getResourceAsStream("/images/gif/Destroyed Explosion Reverse.gif")));
        destroyedExplosionLeftFrames = gifToImageIcons(reader);

        reader.setInput(
                ImageIO.createImageInputStream(getClass().getResourceAsStream("/images/gif/Alien Bomb Explosion.gif")));
        alienBombExplosionFrames = gifToImageIcons(reader);

        // Enemy projectiles
        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 1/Ship 1 - Projectile.gif")));
        seekerProjectileFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 2/Ship 2 - Projectile.gif")));
        tazerProjectileFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 3/Ship 3 - Projectile.gif")));
        energizerProjectileFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 4/Ship 4 - Projectile.gif")));
        bulldozerProjectileFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 5/Ship 5 - Projectile.gif")));
        flamerProjectileFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 6/Ship 6 - Projectile.gif")));
        bombaProjectileFrames = gifToImageIcons(reader);

        // Enemy normal exhausts
        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 1/Ship 1 - Normal Exhaust.gif")));
        seekerNormalExhaustFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 2/Ship 2 - Normal Exhaust.gif")));
        tazerNormalExhaustFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 3/Ship 3 - Normal Exhaust.gif")));
        energizerNormalExhaustFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 4/Ship 4 - Normal Exhaust.gif")));
        bulldozerNormalExhaustFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 5/Ship 5 - Normal Exhaust.gif")));
        flamerNormalExhaustFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 6/Ship 6 - Normal Exhaust.gif")));
        bombaNormalExhaustFrames = gifToImageIcons(reader);

        // Enemy large exhausts
        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 1/Ship 1 - Large Exhaust.gif")));
        seekerLargeExhaustFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 2/Ship 2 - Large Exhaust.gif")));
        tazerLargeExhaustFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 3/Ship 3 - Large Exhaust.gif")));
        energizerLargeExhaustFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 4/Ship 4 - Large Exhaust.gif")));
        bulldozerLargeExhaustFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 5/Ship 5 - Large Exhaust.gif")));
        flamerLargeExhaustFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 6/Ship 6 - Large Exhaust.gif")));
        bombaLargeExhaustFrames = gifToImageIcons(reader);

        // Enemy projectile explosions
        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 1/Ship 1 - Projectile Explosion.gif")));
        seekerProjectileExplosion = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 2/Ship 2 - Projectile Explosion.gif")));
        tazerProjectileExplosion = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 3/Ship 3 - Projectile Explosion.gif")));
        energizerProjectileExplosion = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 4/Ship 4 - Projectile Explosion.gif")));
        bulldozerProjectileExplosion = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 5/Ship 5 - Projectile Explosion.gif")));
        flamerProjectileExplosion = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 6/Ship 6 - Projectile Explosion.gif")));
        bombaProjectileExplosion = gifToImageIcons(reader);

        // Explosions
        reader.setInput(ImageIO.createImageInputStream(getClass().getResourceAsStream("/images/gif/Implosion.gif")));
        implosionFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 1/Ship 1 - Destroyed Explosion.gif")));
        seekerDestroyedExplosionFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 2/Ship 2 - Destroyed Explosion.gif")));
        tazerDestroyedExplosionFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 3/Ship 3 - Destroyed Explosion.gif")));
        energizerDestroyedExplosionFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 4/Ship 4 - Destroyed Explosion.gif")));
        bulldozerDestroyedExplosionFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 5/Ship 5 - Destroyed Explosion.gif")));
        flamerDestroyedExplosionFrames = gifToImageIcons(reader);

        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 6/Ship 6 - Destroyed Explosion.gif")));
        bombaDestroyedExplosionFrames = gifToImageIcons(reader);


    }

    private void initPNGtoGIFAnimation () {
        for (int i = 1; i < 6; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Default Player Engine/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            defaultPlayerEngine.add(image);
        }

        for (int i = 1; i < 6; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Default Player Engine Boosted/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            defaultPlayerEngineBoostedFrames.add(image);
        }

        for (int i = 1; i < 10; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Player Shield Damage/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            defaultPlayerShieldDamage.add(image);
        }

        for (int i = 1; i < 10; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Player EMP/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            playerEMPFrames.add(image);
        }

        for (int i = 1; i < 16; i++) {
            String sourceString = String.format("/images/Ships/Guardian Bot/GuardianBot%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            guardianBotFrames.add(image);
        }

        for (int i = 1; i < 46; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Player Fireswirl/tile0%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            playerFireSwirlFrames.add(image);
        }

        for (int i = 1; i < 5; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Firewall/flame%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            firewallParticleFrames.add(image);
        }

        for (int i = 1; i < 4; i++) {
            BufferedImage image = imgLoader.getImage(ImageEnums.Invisible);
            invisibleAnimation.add(image);
        }

        for (int i = 1; i < 21; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Highlight/highlight%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            HighlightImages.add(image);
        }

        for (int i = 0; i < 9; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Missile1/Missile_1_Explosion_00%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            Rocket1ExplosionFrames.add(image);
        }

        for (int i = 0; i < 10; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Missile1/Missile_1_Flying_00%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            Rocket1ProjectileFrames.add(image);
        }

        for (int i = 1; i < 31; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/FireSpout/Firespout%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            fireSpoutFrames.add(image);
        }

        for (int i = 0; i < 10; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Alien Bomb/Bomb_3_Idle_00%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            alienBombFrames.add(image);
        }

        for (int i = 1; i < 65; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Portal5/portal%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            portal5Frames.add(image);
        }

        for (int i = 1; i < 9; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/WarpFrames/WarpFrame%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            warpFrames.add(image);
        }

        for (int i = 0; i < 20; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Shield4/shield4%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            shield4Frames.add(image);
        }

        for (int i = 0; i < 11; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Shield3/shield3%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            shield3Frames.add(image);
        }

        for (int i = 0; i < 12; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Shield2/shield2%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            shield2Frames.add(image);
        }

        for (int i = 0; i < 9; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Charging/charging%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            chargingFrames.add(image);
        }

        for (int i = 0; i < 8; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Energy Circle/energycircle%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            energyCircleFrames.add(image);
        }

        for (int i = 0; i < 19; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Flamewarp/flamewarp%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            flamewarpFrames.add(image);
        }

        for (int i = 1; i < 17; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Frontshield/frontshield%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            frontShieldFrames.add(image);
        }

        for (int i = 1; i < 17; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Frontshield2/frontshieldb%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            frontShieldBFrames.add(image);
        }

        for (int i = 0; i < 8; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Pulsating shield/pulsatingshield%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            pulsatingShieldFrames.add(image);
        }

        for (int i = 0; i < 6; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Pulsating star/pulsatingstar%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            pulsatingStarFrames.add(image);
        }

        for (int i = 0; i < 13; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Radar/radar%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            radarFrames.add(image);
        }

        for (int i = 1; i < 14; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Rotatingboxes/rotatingboxes%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            rotatingBoxesFrames.add(image);
        }

        for (int i = 1; i < 17; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Rotatingcone/rotatingcone%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            rotatingConeFrames.add(image);
        }

        for (int i = 0; i < 20; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/GreenEnergyOrbs/greenEnergyOrbs%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            greenEnergyOrbFrames.add(image);
        }

        for (int i = 0; i < 20; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/PurpleEnergyBlocks/purpleEnergyBlocks%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            purpleEnergyBlockFrames.add(image);
        }

        for (int i = 0; i < 20; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Fireshield/fireshield%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            fireshieldFrames.add(image);
        }

        for (int i = 0; i < 20; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/EnergyFlower/energyflower%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            energyFlowerFrames.add(image);
        }

        for (int i = 0; i < 20; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/PurpleEnergyBarrier/purpleenergybarrier%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            purpleEnergyBarrier.add(image);
        }

        for (int i = 0; i < 20; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/RedHole/7200%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            redHoleFrames.add(image);
        }

        for (int i = 0; i < 12; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/CrossingEnergyBeams/1000%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            crossingEnergyBeams.add(image);
        }

        for (int i = 1; i < 4; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/PlasmaCoatedBurning/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            plasmaCoatedDebuff.add(image);
        }

        for (int i = 0; i < 16; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/GasolineExplosion/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            gasolineExplosion.add(image);
        }

        for (int i = 0; i < 31; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/GasolineBurning/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            gasolineBurning.add(image);
        }

        for (int i = 0; i < 5; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Healing/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            healingAnimation.add(image);
        }

        for (int i = 0; i < 39; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/StickyDynamiteExplosion/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            stickyDynamiteExplosion.add(image);
        }

        for (int i = 0; i < 60; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/PlasmaLauncherMissile/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            plasmaLauncherMissileFrames.add(image);
        }

    }

    private void initSpriteSheets () {
        BufferedImage empImage = imgLoader.getImage(ImageEnums.Player_EMP_Plus);
        playerEMPPlusFrames = cutSpriteSheetToImages(empImage, 8, 8);

    }

    private List<BufferedImage> gifToImageIcons (ImageReader reader) throws IOException {
        int n = reader.getNumImages(true);
        List<BufferedImage> imgs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            imgs.add(reader.read(i));
        }
        return imgs;
    }

    private List<BufferedImage> cutSpriteSheetToImages (BufferedImage image, int rows, int cols) {
        final int spriteWidth = image.getWidth(null) / cols; // width of a single sprite
        final int spriteHeight = image.getHeight(null) / rows; // height of a single sprite
        List<BufferedImage> sprites = new ArrayList<BufferedImage>();
//		BufferedImage spriteSheetImage = toBufferedImage(image);
        // load the sprite sheet
        // split the sprite sheet into individual sprites
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // get the subimage from the sprite sheet
                BufferedImage sprite = image.getSubimage(j * spriteWidth, i * spriteHeight, spriteWidth, spriteHeight);
                sprites.add(sprite);
            }
        }

        return sprites;
    }

//	private BufferedImage toBufferedImage(Image image) {
//		if (image instanceof BufferedImage) {
//			return (BufferedImage) image;
//		}
//
//		BufferedImage buff = new BufferedImage(image.getWidth(null), image.getHeight(null),
//				BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g = buff.createGraphics();
//		g.drawImage(image, 0, 0, null);
//		g.dispose();
//
//		return buff;
//	}

}