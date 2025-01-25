package net.riezebos.bruus.tbd.visualsandaudio.data.image;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageDatabase {

    private static ImageDatabase instance = new ImageDatabase();
    private ImageLoader imgLoader = ImageLoader.getInstance();

    // Friendly images
    private BufferedImage model3BetterUpgrade;
    private BufferedImage uidamageoverlay;
    private BufferedImage inputMapping;

    // Enemy images
    private BufferedImage alienBombImage;
    private List<BufferedImage> overlordIdle = new ArrayList<>();
    private List<BufferedImage> fireWall = new ArrayList<>();
    private List<BufferedImage> laserbeamBody = new ArrayList<>();
    private List<BufferedImage> laserbeamStart = new ArrayList<>();
    private List<BufferedImage> laserbeamEnd = new ArrayList<>();
    private List<BufferedImage> seekerFrames = new ArrayList<>();
    private List<BufferedImage> tazer = new ArrayList<>();
    private List<BufferedImage> energizer = new ArrayList<>();
    private List<BufferedImage> bulldozer = new ArrayList<>();
    private List<BufferedImage> flamer = new ArrayList<>();
    private List<BufferedImage> bomba = new ArrayList<>();

    private List<BufferedImage> needler = new ArrayList<>();
    private List<BufferedImage> destructableOrbitCenterMissile = new ArrayList<>();
    private List<BufferedImage> shurikenEnemy = new ArrayList<>();
    private List<BufferedImage> moduleScorchFlames = new ArrayList<>();


    // Ship numbers to names:
    /*
     * Ship1 = Seeker Ship2 = Tazer Ship3 = Energizer Ship4 = Bulldozer Ship5 = Flamer
     * Ship6 = Bomba
     */

    // Projectile images


    // Menu images
    private BufferedImage titleImage;
    private BufferedImage entanglingFlames;
    private BufferedImage longCard;
    private BufferedImage wideCard;
    private BufferedImage squareCard;

    private BufferedImage sc2GradeBronze;
    private BufferedImage sc2GradeSilver;
    private BufferedImage sc2GradeGold;
    private BufferedImage sc2GradePlatinum;
    private BufferedImage sc2GradeDiamond;
    private BufferedImage sc2GradeMaster;
    private BufferedImage sc2GradeGrandMaster;
    private BufferedImage stickyOil;
    private BufferedImage escalatingFlames;


    private BufferedImage UIScoreTextImage;
    private BufferedImage UIYouDied;
    private BufferedImage UILevelCompleted;

    // Game GUI images

    private BufferedImage progressBar;
    private BufferedImage progressBarFilling;
    private BufferedImage frame;
    private BufferedImage redFilling;
    private BufferedImage goldFilling;
    private BufferedImage blueFilling;


    // Icons
    private BufferedImage starcraft2_Protoss_Shield_Disintegrate;
    private BufferedImage starcraft2_Dual_Rockets;
    private BufferedImage starcraft2_Auto_Tracking;
    private BufferedImage starcraft2_Blue_Flame;
    private BufferedImage starcraft2_Concussive_Shells;
    private BufferedImage starcraft2_Energy_Siphon;
    private BufferedImage starcraft2_Fire_Hardened_Shields;
    private BufferedImage sc2BatteryUpgrade;
    private BufferedImage starcraft2_Health_Upgrade_2;
    private BufferedImage starcraft2_Seeker_Missile;
    private BufferedImage starcraft2_Vespene_Drone;
    private BufferedImage starcraft2_Heal;
    private BufferedImage starcraft2_Electric_Field;
    private BufferedImage starcraft2Contract;
    private BufferedImage starcraft2ModuleElectrify;
    private BufferedImage starcraft2ModuleCommand;
    private BufferedImage starcraft2_Advanced_Optics;
    private BufferedImage cannisterOfGasoline;
    private BufferedImage starcraft2_PlatinumSponge;

    private BufferedImage starcraft2_Battery;
    private BufferedImage starcraft2_FocusedCrystal;
    private BufferedImage starcraft2_FireBatWeapon;
    private BufferedImage starcraft2_Overclock;
    private BufferedImage starcraft2_ArmorPiercing;
    private BufferedImage moneyPrinter;
    private BufferedImage stickyDynamite;
    private BufferedImage starcraft2Keystone;

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

    private BufferedImage topazGem7;
    private BufferedImage laserBullet;

    private BufferedImage peepoDeepFriedSadge;
    private BufferedImage peepoFeelsCringeMan;
    private BufferedImage peepoFeelsRetardedMan;
    private BufferedImage peepoHmmm;
    private BufferedImage peepoLookingDown;
    private BufferedImage peepoMonkaHmmm;
    private BufferedImage peepoMonkaLaugh;
    private BufferedImage peepoPauseChamp;
    private BufferedImage peepoClown;
    private BufferedImage peepoCringe;
    private BufferedImage peepoLaugh;
    private BufferedImage peepoLyingSadge;
    private BufferedImage peepoOkay;
    private BufferedImage peepoSad;
    private BufferedImage peepoSad2;
    private BufferedImage peepoShrug;
    private BufferedImage peepoSmadge;
    private BufferedImage peepoSmokedge;
    private BufferedImage peepoSmug;
    private BufferedImage peepoStare;
    private BufferedImage peepoUhm;
    private BufferedImage peepoAngy;
    private BufferedImage peepoBruh;
    private BufferedImage peepoCoffee;
    private BufferedImage peepoConfused;
    private BufferedImage peepoGottem;
    private BufferedImage peepoHands;
    private BufferedImage peepoLaugh2;
    private BufferedImage peepoPointLaugh;
    private BufferedImage peepoW;
    private BufferedImage peepoSadClown;
    private BufferedImage peepoSadge;
    private BufferedImage peepoSadgeCry;
    private BufferedImage peepoShruge;
    private BufferedImage peepoSkillIssue;

    private BufferedImage VIPTicket;
    private BufferedImage piercingLaser;
    private BufferedImage thornweaver;
    private BufferedImage thornedplates;
    private BufferedImage barbedmissiles;
    private BufferedImage barbedaegis;
    private BufferedImage fourDirectionalDrone;
    private BufferedImage spaceStationBoss;
    private BufferedImage classSelectionUI;
    private BufferedImage fireFighter;
    private BufferedImage sc2MineExplosion;


    private List<BufferedImage> cashCarrierFrames = new ArrayList<>();
    private List<BufferedImage> spaceStationBossFrames = new ArrayList<>();
    private List<BufferedImage> impactExplosionOneFrames = new ArrayList<>();
    private List<BufferedImage> destroyedExplosionUpFrames = new ArrayList<>();
    private List<BufferedImage> alienBombExplosionFrames = new ArrayList<>();
    private List<BufferedImage> selectNewClassAnimation = new ArrayList<>();

    // Animations
    private List<BufferedImage> playerEMPFrames = new ArrayList<>();
    private List<BufferedImage> defaultPlayerShieldDamage = new ArrayList<>();
    private List<BufferedImage> guardianBotFrames = new ArrayList<>();
    private List<BufferedImage> portal5Frames = new ArrayList<>();
    private List<BufferedImage> warpFrames = new ArrayList<>();
    private List<BufferedImage> chargingFrames = new ArrayList<>();
    private List<BufferedImage> energyCircleFrames = new ArrayList<>();

    private List<BufferedImage> plasmaCoatedDebuff = new ArrayList<>();
    private List<BufferedImage> gasolineExplosion = new ArrayList<>();
    private List<BufferedImage> gasolineBurning = new ArrayList<>();
    private List<BufferedImage> healingAnimation = new ArrayList<>();
    private List<BufferedImage> stickyDynamiteExplosion = new ArrayList<>();
    private List<BufferedImage> plasmaLauncherMissileFrames = new ArrayList<>();

    private List<BufferedImage> superChargedFrames = new ArrayList<>();
    private List<BufferedImage> laserBulletDeathFrames = new ArrayList<>();
    private List<BufferedImage> lightningOrbDestruction = new ArrayList<>();
    private List<BufferedImage> freezeEffect = new ArrayList<>();

    // Enemy Projectile Animations
    private List<BufferedImage> seekerProjectileFrames = new ArrayList<>();
    private List<BufferedImage> bombaProjectileFrames = new ArrayList<>();
    private List<BufferedImage> Rocket1ProjectileFrames = new ArrayList<>();
    private List<BufferedImage> Rocket1ExplosionFrames = new ArrayList<>();
    private List<BufferedImage> barrierProjectileDestruction = new ArrayList<>();
    private List<BufferedImage> laserbeamChargingFrames = new ArrayList<>();

    // Enemy Exhaust Animations
    private List<BufferedImage> explosion2 = new ArrayList<>();
    private List<BufferedImage> electroShredImproved = new ArrayList<>();

    // Enemy projectile explosions
    private List<BufferedImage> bombaProjectileExplosion = new ArrayList<>();
    private List<BufferedImage> alienLaserBeamAnimated = new ArrayList<>();

    private List<BufferedImage> barrierProjectile = new ArrayList<>();
    private List<BufferedImage> lightningOrb = new ArrayList<>();
    private List<BufferedImage> levelUpAnimation = new ArrayList<>();
    private List<BufferedImage> cashExplosion = new ArrayList<>();
    private List<BufferedImage> redBossFrames = new ArrayList<>();

    // Background images
    private BufferedImage moon;
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
    private BufferedImage bargainBucket;
    private BufferedImage corrosiveOil;
    private BufferedImage fireShieldIcon;
    private BufferedImage moduleScorchIcon;


    private BufferedImage starOrange1;
    private BufferedImage starOrange2;
    private BufferedImage starOrange3;
    private BufferedImage starOrange4;

    private BufferedImage yellowWings1;
    private BufferedImage yellowWings2;
    private BufferedImage yellowWings3;
    private BufferedImage yellowWings4;
    private BufferedImage yellowWings5;

    private BufferedImage redWings1;
    private BufferedImage redWings2;
    private BufferedImage redWings3;
    private BufferedImage redWings4;
    private BufferedImage redWings5;

    private BufferedImage blueWings1;
    private BufferedImage blueWings2;
    private BufferedImage blueWings3;
    private BufferedImage blueWings4;
    private BufferedImage blueWings5;
    private BufferedImage purpleWings1;
    private BufferedImage purpleWings2;
    private BufferedImage purpleWings3;
    private BufferedImage purpleWings4;
    private BufferedImage purpleWings5;
    private BufferedImage informationCard;
    private BufferedImage lockedIcon;
    private BufferedImage sc2ConcentratedLaser;
    private BufferedImage sc2RepairBeam;
    private List<BufferedImage> scout = new ArrayList<>();


    private List<BufferedImage> fireFighterFlameThrowerAppearing = new ArrayList<>();
    private List<BufferedImage> fireFighterFlameThrowerLooping = new ArrayList<>();
    private List<BufferedImage> fireFighterFlameThrowerDissipating = new ArrayList<>();
    private List<BufferedImage> fireFighterFireShield = new ArrayList<>();
    private List<BufferedImage> fireFighterFireShieldAppearing = new ArrayList<>();
    private List<BufferedImage> HighlightImages = new ArrayList<>();
    private List<BufferedImage> thornsDamage = new ArrayList<>();
    private List<BufferedImage> devourerIdle = new ArrayList<>();
    private List<BufferedImage> devourerAttacking = new ArrayList<>();
    private List<BufferedImage> devourerDeath = new ArrayList<>();
    private List<BufferedImage> devourerMissile = new ArrayList<>();
    private List<BufferedImage> devourerMissileExplosion = new ArrayList<>();
    private List<BufferedImage> devourerDebuffStage1 = new ArrayList<>();
    private List<BufferedImage> devourerDebuffStage2 = new ArrayList<>();
    private List<BufferedImage> devourerDebuffStage3 = new ArrayList<>();
    private List<BufferedImage> devourerDebuffStage4 = new ArrayList<>();
    private List<BufferedImage> devourerBirth = new ArrayList<>();
    private List<BufferedImage> mutaliskIdle = new ArrayList<>();
    private List<BufferedImage> mutaliskDeath = new ArrayList<>();
    private List<BufferedImage> mutaliskMissile = new ArrayList<>();
    private List<BufferedImage> mutaliskMissileImpact = new ArrayList<>();
    private List<BufferedImage> mutaliskBirth = new ArrayList<>();
    private List<BufferedImage> devourerCocoon = new ArrayList<>();
    private List<BufferedImage> guardianMutaliskCocoon = new ArrayList<>();
    private List<BufferedImage> scourgeIdle = new ArrayList<>();
    private List<BufferedImage> scourgeExplosion = new ArrayList<>();
    private List<BufferedImage> scourgeDeath = new ArrayList<>();
    private List<BufferedImage> guardianIdle = new ArrayList<>();
    private List<BufferedImage> guardianChargingAnimation = new ArrayList<>();
    private List<BufferedImage> guardianMissile = new ArrayList<>();
    private List<BufferedImage> guardianMissileImpact = new ArrayList<>();
    private List<BufferedImage> guardianDeath = new ArrayList<>();
    private List<BufferedImage> guardianBirth = new ArrayList<>();
    private List<BufferedImage> queenIdle = new ArrayList<>();
    private List<BufferedImage> queenAttacking = new ArrayList<>();
    private List<BufferedImage> queenDeath = new ArrayList<>();
    private List<BufferedImage> broodlingIdle = new ArrayList<>();
    private List<BufferedImage> broodlingAttacking = new ArrayList<>();
    private List<BufferedImage> broodlingDeath = new ArrayList<>();
    // testimages
    private BufferedImage invisible;

    // Images to Gifs
    private List<BufferedImage> defaultPlayerEngine = new ArrayList<>();
    private BufferedImage guirefresh;
    private BufferedImage warcraft3HealingWave;
    private BufferedImage starcraft2LockOn;
    private BufferedImage starcraft2_Psi_Storm2;

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
        this.initIcons();
        this.initLetters();
    }

    private void initFriendlies () {
        this.model3BetterUpgrade = imgLoader.getImage(ImageEnums.Player_Spaceship_Model_3);
    }

    private void initEnemies () {
        this.alienBombImage = imgLoader.getImage(ImageEnums.Alien_Bomb);
        this.fourDirectionalDrone = imgLoader.getImage(ImageEnums.FourDirectionalDrone);
        this.spaceStationBoss = imgLoader.getImage(ImageEnums.SpaceStationBoss);

    }

    private void initProjectiles () {
        this.invisible = imgLoader.getImage(ImageEnums.Invisible);
    }

    private void initMenuImages () {
        this.titleImage = imgLoader.getImage(ImageEnums.Title_Image);
        this.informationCard = imgLoader.getImage(ImageEnums.InformationCard);
    }

    private void initGameUIobjects () {
        this.frame = imgLoader.getImage(ImageEnums.Frame);
        this.redFilling = imgLoader.getImage(ImageEnums.Red_Filling);
        this.goldFilling = imgLoader.getImage(ImageEnums.Gold_Filling);
        this.blueFilling = imgLoader.getImage(ImageEnums.Blue_Filling);
    }

    private void initIcons () {
        this.starcraft2_Protoss_Shield_Disintegrate = imgLoader
                .getImage(ImageEnums.Starcraft2_Protoss_Shield_Disintegrate);
        this.starcraft2_Psi_Storm2 = imgLoader.getImage(ImageEnums.Starcraft2_Psi_Storm2);
        this.starcraft2_Dual_Rockets = imgLoader.getImage(ImageEnums.Starcraft2_Dual_Rockets);
        this.starcraft2_Auto_Tracking = imgLoader.getImage(ImageEnums.Starcraft2_Auto_Tracking);
        this.starcraft2_Blue_Flame = imgLoader.getImage(ImageEnums.Starcraft2_Blue_Flame);
        this.starcraft2_Concussive_Shells = imgLoader.getImage(ImageEnums.Starcraft2_Concussive_Shells);
        this.starcraft2_Energy_Siphon = imgLoader.getImage(ImageEnums.Starcraft2_Energy_Siphon);
        this.starcraft2_Fire_Hardened_Shields = imgLoader.getImage(ImageEnums.Starcraft2_Fire_Hardened_Shields);
        this.starcraft2_Health_Upgrade_2 = imgLoader.getImage(ImageEnums.Starcraft2_Health_Upgrade_2);
        this.starcraft2_Seeker_Missile = imgLoader.getImage(ImageEnums.Starcraft2_Seeker_Missile);
        this.starcraft2_Vespene_Drone = imgLoader.getImage(ImageEnums.Starcraft2_Vespene_Drone);
        this.starcraft2_Heal = imgLoader.getImage(ImageEnums.Starcraft2_Heal);
        this.starcraft2_Electric_Field = imgLoader.getImage(ImageEnums.Starcraft2_Electric_Field);
        this.starcraft2_Advanced_Optics = imgLoader.getImage(ImageEnums.Starcraft2_Advanced_Optics);
        this.cannisterOfGasoline = imgLoader.getImage(ImageEnums.CannisterOfGasoline);
        this.starcraft2_Battery = imgLoader.getImage(ImageEnums.Starcraft2_Battery);
        this.starcraft2_FocusedCrystal = imgLoader.getImage(ImageEnums.Starcraft2_Focused_Crystal);
        this.starcraft2_PlatinumSponge = imgLoader.getImage(ImageEnums.Starcraft2_Platinum_Sponge);
        this.starcraft2_Overclock = imgLoader.getImage(ImageEnums.Starcraft2_Overclock);
        this.starcraft2_ArmorPiercing = imgLoader.getImage(ImageEnums.Starcraft2_Armor_Piercing);
        this.moneyPrinter = imgLoader.getImage(ImageEnums.MoneyPrinter);
        this.stickyDynamite = imgLoader.getImage(ImageEnums.StickyDynamite);
        this.topazGem7 = imgLoader.getImage(ImageEnums.TopazGem7);
        this.VIPTicket = imgLoader.getImage(ImageEnums.VIPTicket);
        this.piercingLaser = imgLoader.getImage(ImageEnums.PiercingLaser);
        this.sc2ConcentratedLaser = imgLoader.getImage(ImageEnums.Starcraft2ConcentratedLaser);

        this.redWings1 = imgLoader.getImage(ImageEnums.RedWings1);
        this.redWings2 = imgLoader.getImage(ImageEnums.RedWings2);
        this.redWings3 = imgLoader.getImage(ImageEnums.RedWings3);
        this.redWings4 = imgLoader.getImage(ImageEnums.RedWings4);
        this.redWings5 = imgLoader.getImage(ImageEnums.RedWings5);
        this.classSelectionUI = imgLoader.getImage(ImageEnums.SelectClass);
        this.starcraft2_FireBatWeapon = imgLoader.getImage(ImageEnums.Starcraft2FireBatWeapon);
        this.sc2RepairBeam = imgLoader.getImage(ImageEnums.Starcraft2RepairBeam);
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

        this.purpleWings1 = imgLoader.getImage(ImageEnums.PurpleWings1);
        this.purpleWings2 = imgLoader.getImage(ImageEnums.PurpleWings2);
        this.purpleWings3 = imgLoader.getImage(ImageEnums.PurpleWings3);
        this.purpleWings4 = imgLoader.getImage(ImageEnums.PurpleWings4);
        this.purpleWings5 = imgLoader.getImage(ImageEnums.PurpleWings5);

        this.blueWings1 = imgLoader.getImage(ImageEnums.BlueWings1);
        this.blueWings2 = imgLoader.getImage(ImageEnums.BlueWings2);
        this.blueWings3 = imgLoader.getImage(ImageEnums.BlueWings3);
        this.blueWings4 = imgLoader.getImage(ImageEnums.BlueWings4);
        this.blueWings5 = imgLoader.getImage(ImageEnums.BlueWings5);
        this.escalatingFlames = imgLoader.getImage(ImageEnums.EscalatingFlames);

        this.yellowWings1 = imgLoader.getImage(ImageEnums.YellowWings1);
        this.yellowWings2 = imgLoader.getImage(ImageEnums.YellowWings2);
        this.yellowWings3 = imgLoader.getImage(ImageEnums.YellowWings3);
        this.yellowWings4 = imgLoader.getImage(ImageEnums.YellowWings4);
        this.yellowWings5 = imgLoader.getImage(ImageEnums.YellowWings5);

        this.progressBar = imgLoader.getImage(ImageEnums.ProgressBar);
        this.progressBarFilling = imgLoader.getImage(ImageEnums.ProgressBarFilling);

        this.UILevelCompleted = imgLoader.getImage(ImageEnums.UILevelComplete);
        this.UIYouDied = imgLoader.getImage(ImageEnums.UIYouDied);
        this.UIScoreTextImage = imgLoader.getImage(ImageEnums.UIScoreTextCard);

        this.sc2GradeBronze = imgLoader.getImage(ImageEnums.GradeBronze);
        this.sc2GradeSilver = imgLoader.getImage(ImageEnums.GradeSilver);
        this.sc2GradeGold = imgLoader.getImage(ImageEnums.GradeGold);
        this.sc2GradePlatinum = imgLoader.getImage(ImageEnums.GradePlatinum);
        this.sc2GradeDiamond = imgLoader.getImage(ImageEnums.GradeDiamond);
        this.sc2GradeMaster = imgLoader.getImage(ImageEnums.GradeMaster);
        this.sc2GradeGrandMaster = imgLoader.getImage(ImageEnums.GradeGrandMaster);

        this.peepoDeepFriedSadge = imgLoader.getImage(ImageEnums.peepoDeepFriedSadge);
        this.peepoFeelsCringeMan = imgLoader.getImage(ImageEnums.peepoFeelsCringeMan);
        this.peepoFeelsRetardedMan = imgLoader.getImage(ImageEnums.peepoFeelsRetardedMan);
        this.peepoHmmm = imgLoader.getImage(ImageEnums.peepoHmmm);
        this.peepoLookingDown = imgLoader.getImage(ImageEnums.peepoLookingDown);
        this.peepoMonkaHmmm = imgLoader.getImage(ImageEnums.peepoMonkaHmmm);
        this.peepoMonkaLaugh = imgLoader.getImage(ImageEnums.peepoMonkaLaugh);
        this.peepoPauseChamp = imgLoader.getImage(ImageEnums.peepoPauseChamp);
        this.peepoClown = imgLoader.getImage(ImageEnums.peepoClown);
        this.peepoCringe = imgLoader.getImage(ImageEnums.peepoCringe);
        this.peepoLaugh = imgLoader.getImage(ImageEnums.peepoLaugh);
        this.peepoLyingSadge = imgLoader.getImage(ImageEnums.peepoLyingSadge);
        this.peepoOkay = imgLoader.getImage(ImageEnums.peepoOkay);
        this.peepoSad = imgLoader.getImage(ImageEnums.peepoSad);
        this.peepoSad2 = imgLoader.getImage(ImageEnums.peepoSad2);
        this.peepoShrug = imgLoader.getImage(ImageEnums.peepoShrug);
        this.peepoSmadge = imgLoader.getImage(ImageEnums.peepoSmadge);
        this.peepoSmokedge = imgLoader.getImage(ImageEnums.peepoSmokedge);
        this.peepoSmug = imgLoader.getImage(ImageEnums.peepoSmug);
        this.peepoStare = imgLoader.getImage(ImageEnums.peepoStare);
        this.peepoUhm = imgLoader.getImage(ImageEnums.peepoUhm);
        this.peepoAngy = imgLoader.getImage(ImageEnums.peepoAngy);
        this.peepoBruh = imgLoader.getImage(ImageEnums.peepoBruh);
        this.peepoCoffee = imgLoader.getImage(ImageEnums.peepoCoffee);
        this.peepoConfused = imgLoader.getImage(ImageEnums.peepoConfused);
        this.peepoGottem = imgLoader.getImage(ImageEnums.peepoGottem);
        this.peepoHands = imgLoader.getImage(ImageEnums.peepoHands);
        this.peepoLaugh2 = imgLoader.getImage(ImageEnums.peepoLaugh2);
        this.peepoPointLaugh = imgLoader.getImage(ImageEnums.peepoPointLaugh);
        this.peepoW = imgLoader.getImage(ImageEnums.peepoW);
        this.peepoSadClown = imgLoader.getImage(ImageEnums.peepoSadClown);
        this.peepoSadge = imgLoader.getImage(ImageEnums.peepoSadge);
        this.peepoSadgeCry = imgLoader.getImage(ImageEnums.peepoSadgeCry);
        this.peepoShruge = imgLoader.getImage(ImageEnums.peepoShruge);
        this.peepoSkillIssue = imgLoader.getImage(ImageEnums.peepoSkillIssue);
        this.uidamageoverlay = imgLoader.getImage(ImageEnums.UIDamageOverlay);
        this.inputMapping = imgLoader.getImage(ImageEnums.InputMapping);
        this.fireFighter = imgLoader.getImage(ImageEnums.FireFighter);
    }

    private void initBackgroundObjects () {
        this.moon = imgLoader.getImage(ImageEnums.Moon);
        this.lavaPlanet = imgLoader.getImage(ImageEnums.Lava_Planet);
        this.planetOne = imgLoader.getImage(ImageEnums.Planet_One);
        this.planetTwo = imgLoader.getImage(ImageEnums.Planet_Two);
        this.planetThree = imgLoader.getImage(ImageEnums.Planet_Three);
        this.marsPlanet = imgLoader.getImage(ImageEnums.Mars_Planet);
        this.star = imgLoader.getImage(ImageEnums.Star);
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
        this.bluePlanet1 = imgLoader.getImage(ImageEnums.BluePlanet1);
        this.bluePlanet2 = imgLoader.getImage(ImageEnums.BluePlanet2);
        this.bluePlanet3 = imgLoader.getImage(ImageEnums.BluePlanet3);
        this.bluePlanet4 = imgLoader.getImage(ImageEnums.BluePlanet4);
        this.bluePlanet5 = imgLoader.getImage(ImageEnums.BluePlanet5);
        this.bluePlanet6 = imgLoader.getImage(ImageEnums.BluePlanet6);

        this.lockedIcon = imgLoader.getImage(ImageEnums.LockedIcon);
        this.laserBullet = imgLoader.getImage(ImageEnums.LaserBullet);
        this.guirefresh = imgLoader.getImage(ImageEnums.GUIRefresh);
        this.starcraft2Keystone = imgLoader.getImage(ImageEnums.Starcraft2Keystone);
        this.warcraft3HealingWave = imgLoader.getImage(ImageEnums.Starcraft2BouncingLaser);
        this.starcraft2LockOn = imgLoader.getImage(ImageEnums.Starcraft2LockOn);
        this.thornedplates = imgLoader.getImage(ImageEnums.Thornedplates);
        this.thornweaver = imgLoader.getImage(ImageEnums.ThornWeaver);
        this.barbedmissiles = imgLoader.getImage(ImageEnums.BarbedMissiles);
        this.barbedaegis = imgLoader.getImage(ImageEnums.BarbedAegis);

        this.starcraft2Contract = imgLoader.getImage(ImageEnums.Contract);
        this.starcraft2ModuleCommand = imgLoader.getImage(ImageEnums.ModuleCommand);
        this.starcraft2ModuleElectrify = imgLoader.getImage(ImageEnums.ModuleElectrify);
        this.sc2BatteryUpgrade = imgLoader.getImage(ImageEnums.Starcraft2_BatteryUpgrade);
        this.sc2MineExplosion = imgLoader.getImage(ImageEnums.Starcraft2_MineExplosion);
        this.stickyOil = imgLoader.getImage(ImageEnums.StickyOilIcon);
        this.entanglingFlames = imgLoader.getImage(ImageEnums.EntanglingFlames);
        this.bargainBucket = imgLoader.getImage(ImageEnums.BargainBucket);
        this.corrosiveOil = imgLoader.getImage(ImageEnums.CorrosiveOil);
        this.moduleScorchIcon = imgLoader.getImage(ImageEnums.ModuleScorchIcon);
        this.fireShieldIcon = imgLoader.getImage(ImageEnums.FireShieldIcon);
    }

    public BufferedImage getImage (ImageEnums imageType) {
        switch (imageType) {
            case CorrosiveOil: return corrosiveOil;
            case ModuleScorchIcon: return moduleScorchIcon;
            case FireShieldIcon: return fireShieldIcon;
            case BargainBucket: return bargainBucket;
            case EntanglingFlames: return entanglingFlames;
            case EscalatingFlames: return escalatingFlames;
            case StickyOilIcon: return stickyOil;
            case Starcraft2FireBatWeapon: return this.starcraft2_FireBatWeapon;
            case Contract: return starcraft2Contract;
            case ModuleElectrify: return starcraft2ModuleElectrify;
            case ModuleCommand: return starcraft2ModuleCommand;
            case Starcraft2RepairBeam: return sc2RepairBeam;
            case Starcraft2_BatteryUpgrade: return sc2BatteryUpgrade;
            case Starcraft2_MineExplosion: return sc2MineExplosion;
            case InputMapping:
                return inputMapping;
            case SpaceStationBoss:
                return spaceStationBoss;
            case UIDamageOverlay:
                return uidamageoverlay;
            case RedWings1:
                return redWings1;
            case RedWings2:
                return redWings2;
            case RedWings3:
                return redWings3;
            case RedWings4:
                return redWings4;
            case RedWings5:
                return redWings5;
            case FourDirectionalDrone:
                return fourDirectionalDrone;
            case ThornWeaver:
                return thornweaver;
            case BarbedMissiles:
                return barbedmissiles;
            case BarbedAegis:
                return barbedaegis;
            case Thornedplates:
                return thornedplates;
            case Starcraft2LockOn:
                return starcraft2LockOn;
            case VIPTicket:
                return VIPTicket;
            case PiercingLaser:
                return piercingLaser;
            case Starcraft2BouncingLaser:
                return warcraft3HealingWave;
            case Starcraft2Keystone:
                return starcraft2Keystone;
            case GUIRefresh:
                return guirefresh;
            case peepoDeepFriedSadge:
                return peepoDeepFriedSadge;
            case peepoFeelsCringeMan:
                return peepoFeelsCringeMan;
            case peepoFeelsRetardedMan:
                return peepoFeelsRetardedMan;
            case peepoHmmm:
                return peepoHmmm;
            case peepoLookingDown:
                return peepoLookingDown;
            case peepoMonkaHmmm:
                return peepoMonkaHmmm;
            case peepoMonkaLaugh:
                return peepoMonkaLaugh;
            case peepoPauseChamp:
                return peepoPauseChamp;
            case peepoClown:
                return peepoClown;
            case peepoCringe:
                return peepoCringe;
            case peepoLaugh:
                return peepoLaugh;
            case peepoLyingSadge:
                return peepoLyingSadge;
            case peepoOkay:
                return peepoOkay;
            case peepoSad:
                return peepoSad;
            case peepoSad2:
                return peepoSad2;
            case peepoShrug:
                return peepoShrug;
            case peepoSmadge:
                return peepoSmadge;
            case peepoSmokedge:
                return peepoSmokedge;
            case peepoSmug:
                return peepoSmug;
            case peepoStare:
                return peepoStare;
            case peepoUhm:
                return peepoUhm;
            case peepoAngy:
                return peepoAngy;
            case peepoBruh:
                return peepoBruh;
            case peepoCoffee:
                return peepoCoffee;
            case peepoConfused:
                return peepoConfused;
            case peepoGottem:
                return peepoGottem;
            case peepoHands:
                return peepoHands;
            case peepoLaugh2:
                return peepoLaugh2;
            case peepoPointLaugh:
                return peepoPointLaugh;
            case peepoW:
                return peepoW;
            case peepoSadClown:
                return peepoSadClown;
            case peepoSadge:
                return peepoSadge;
            case peepoSadgeCry:
                return peepoSadgeCry;
            case peepoShruge:
                return peepoShruge;
            case peepoSkillIssue:
                return peepoSkillIssue;
            case UIScoreTextCard:
                return UIScoreTextImage;
            case UIYouDied:
                return UIYouDied;
            case UILevelComplete:
                return UILevelCompleted;
            case GradeBronze:
                return sc2GradeBronze;
            case GradeSilver:
                return sc2GradeSilver;
            case GradeGold:
                return sc2GradeGold;
            case GradePlatinum:
                return sc2GradePlatinum;
            case GradeDiamond:
                return sc2GradeDiamond;
            case GradeMaster:
                return sc2GradeMaster;
            case GradeGrandMaster:
                return sc2GradeGrandMaster;
            case ProgressBar:
                return progressBar;
            case ProgressBarFilling:
                return progressBarFilling;
            case LaserBullet:
                return laserBullet;
            case TopazGem7:
                return topazGem7;
            case LockedIcon:
                return lockedIcon;
            case InformationCard:
                return informationCard;
            case Moon2:
                return moon2;
            case Moon3:
                return moon3;
            case Moon4:
                return moon4;
            case Moon5:
                return moon5;
            case GreenPlanet1:
                return greenPlanet1;
            case GreenPlanet2:
                return greenPlanet2;
            case BluePlanet1:
                return bluePlanet1;
            case BluePlanet2:
                return bluePlanet2;
            case BluePlanet3:
                return bluePlanet3;
            case BluePlanet4:
                return bluePlanet4;
            case BluePlanet5:
                return bluePlanet5;
            case BluePlanet6:
                return bluePlanet6;
            case Star_White1:
                return starWhite1;
            case Star_White2:
                return starWhite2;
            case Star_White3:
                return starWhite3;
            case Star_White4:
                return starWhite4;
            case Star_Red1:
                return starRed1;
            case Star_Red2:
                return starRed2;
            case Star_Red3:
                return starRed3;
            case Star_Red4:
                return starRed4;
            case Star_Orange1:
                return starOrange1;
            case Star_Orange2:
                return starOrange2;
            case Star_Orange3:
                return starOrange3;
            case Star_Orange4:
                return starOrange4;
            case Star_Yellow1:
                return starYellow1;
            case Star_Yellow2:
                return starYellow2;
            case Star_Yellow3:
                return starYellow3;
            case Star_Yellow4:
                return starYellow4;
            case Star_Blue1:
                return starBlue1;
            case Star_Blue2:
                return starBlue2;
            case Star_Blue3:
                return starBlue3;
            case Star_Blue4:
                return starBlue4;
            case PurpleWings1:
                return purpleWings1;
            case PurpleWings2:
                return purpleWings2;
            case PurpleWings3:
                return purpleWings3;
            case PurpleWings4:
                return purpleWings4;
            case PurpleWings5:
                return purpleWings5;
            case YellowWings1:
                return yellowWings1;
            case YellowWings2:
                return yellowWings2;
            case YellowWings3:
                return yellowWings3;
            case YellowWings4:
                return yellowWings4;
            case YellowWings5:
                return yellowWings5;
            case BlueWings1:
                return blueWings1;
            case BlueWings2:
                return blueWings2;
            case BlueWings3:
                return blueWings3;
            case BlueWings4:
                return blueWings4;
            case BlueWings5:
                return blueWings5;
            case Starcraft2_Focused_Crystal:
                return starcraft2_FocusedCrystal;
            case Starcraft2_Platinum_Sponge:
                return starcraft2_PlatinumSponge;
            case Starcraft2_Overclock:
                return starcraft2_Overclock;
            case Starcraft2_Armor_Piercing:
                return starcraft2_ArmorPiercing;
            case MoneyPrinter:
                return moneyPrinter;
            case StickyDynamite:
                return stickyDynamite;
            case Alien_Bomb:
                return this.alienBombImage;
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
            case Player_Spaceship_Model_3:
                return this.model3BetterUpgrade;
            case Frame:
                return this.frame;
            case Red_Filling:
                return this.redFilling;
            case Gold_Filling:
                return this.goldFilling;
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
            case Starcraft2_Protoss_Shield_Disintegrate:
                return starcraft2_Protoss_Shield_Disintegrate;
            case Starcraft2_Psi_Storm2:
                return starcraft2_Psi_Storm2;
            case Starcraft2_Dual_Rockets:
                return starcraft2_Dual_Rockets;
            case Starcraft2_Auto_Tracking:
                return starcraft2_Auto_Tracking;
            case Starcraft2_Blue_Flame:
                return starcraft2_Blue_Flame;
            case Starcraft2_Concussive_Shells:
                return starcraft2_Concussive_Shells;
            case Starcraft2_Energy_Siphon:
                return starcraft2_Energy_Siphon;
            case Starcraft2_Fire_Hardened_Shields:
                return starcraft2_Fire_Hardened_Shields;
            case Starcraft2_Health_Upgrade_2:
                return starcraft2_Health_Upgrade_2;
            case Starcraft2_Seeker_Missile:
                return starcraft2_Seeker_Missile;
            case Starcraft2_Vespene_Drone:
                return starcraft2_Vespene_Drone;
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
            case SelectClass: return classSelectionUI;
            case FireFighter: return fireFighter;
            case Starcraft2ConcentratedLaser: return sc2ConcentratedLaser;
            case Invisible:
                return invisible;
            case Starcraft2_Electric_Field:
                return starcraft2_Electric_Field;
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
                return star;
        }
    }

    public List<BufferedImage> getAnimation (ImageEnums imageType) {
        switch (imageType) {
            case ModuleScorchFlames: return moduleScorchFlames;
            case LingeringFlameLooping: return fireWall;
            case FireFighterFlameThrowerLooping: return fireFighterFlameThrowerLooping;
            case FireFighterFlameThrowerAppearing: return fireFighterFlameThrowerAppearing;
            case FireFighterFlameThrowerDissipating: return fireFighterFlameThrowerDissipating;
            case FireFighterFireShieldAppearing: return fireFighterFireShieldAppearing;
            case FireFighterFireShield: return fireFighterFireShield;
            case CashCarrier: return cashCarrierFrames;
            case Overlord: return overlordIdle;
            case DevourerBirth:
                return devourerBirth;
            case DevourerCocoon:
                return devourerCocoon;
            case MutaliskIdle:
                return mutaliskIdle;
            case MutaliskBirth:
                return mutaliskBirth;
            case MutaliskDeath:
                return mutaliskDeath;
            case MutaliskMissile:
                return mutaliskMissile;
            case MutaliskMissileImpact:
                return mutaliskMissileImpact;
            case GuardianMutaliskCocoon:
                return guardianMutaliskCocoon;
            case ScourgeDeath:
                return scourgeDeath;
            case ScourgeIdle:
                return scourgeIdle;
            case ScourgeExplosion:
                return scourgeExplosion;
            case GuardianChargingAnimation:
                return guardianChargingAnimation;
            case GuardianBirth:
                return guardianBirth;
            case GuardianDeath:
                return guardianDeath;
            case GuardianIdle:
                return guardianIdle;
            case GuardianMissile:
                return guardianMissile;
            case GuardianMissileImpact:
                return guardianMissileImpact;
            case QueenAttacking:
                return queenAttacking;
            case QueenIdle:
                return queenIdle;
            case QueenDeath:
                return queenDeath;
            case BroodlingIdle:
                return broodlingIdle;
            case BroodlingAttacking:
                return broodlingAttacking;
            case BroodlingDeath:
                return broodlingDeath;
            case DevourerDebuffStage1:
                return devourerDebuffStage1;
            case DevourerDebuffStage2:
                return devourerDebuffStage2;
            case DevourerDebuffStage3:
                return devourerDebuffStage3;
            case DevourerDebuffStage4:
                return devourerDebuffStage4;
            case DevourerIdle:
                return devourerIdle;
            case DevourerAttacking:
                return devourerAttacking;
            case DevourerDeath:
                return devourerDeath;
            case DevourerMissile:
                return devourerMissile;
            case DevourerMissileExplosion:
                return devourerMissileExplosion;
            case SpaceStationBoss:
                return spaceStationBossFrames;
            case LaserbeamCharging:
                return laserbeamChargingFrames;
            case LaserbeamEnd:
                return laserbeamEnd;
            case LaserbeamStart:
                return laserbeamStart;
            case LaserbeamBody:
                return laserbeamBody;
            case ShurikenEnemy:
                return shurikenEnemy;
            case FreezeEffect:
                return freezeEffect;
            case ThornsDamage:
                return thornsDamage;
            case ElectroShredImproved:
                return electroShredImproved;
            case Tazer:
                return tazer;
            case Seeker:
                return seekerFrames;
            case CashExplosion:
                return cashExplosion;
            case LaserBulletDestruction:
                return laserBulletDeathFrames;
            case LightningOrbDestruction:
                return lightningOrbDestruction;
            case Explosion2:
                return explosion2;
            case Flamer:
                return flamer;
            case Bomba:
                return bomba;
            case DestructableOrbitCenterMissile:
                return destructableOrbitCenterMissile;
            case AlienLaserBeamAnimated:
                return alienLaserBeamAnimated;
            case RedBoss:
                return redBossFrames;
            case LightningOrb:
                return lightningOrb;
            case SuperChargedBuff:
                return superChargedFrames;
            case BarrierProjectile:
                return barrierProjectile;
            case BarrierProjectileDestruction:
                return barrierProjectileDestruction;
            case SelectNewClassAnimation: return selectNewClassAnimation;
            case PlasmaLauncherMissile:
                return plasmaLauncherMissileFrames;
            case StickyDynamiteExplosion:
                return stickyDynamiteExplosion;
            case Impact_Explosion_One:
                return this.impactExplosionOneFrames;
            case Destroyed_Explosion:
                return this.destroyedExplosionUpFrames;
            case Alien_Bomb_Explosion:
                return this.alienBombExplosionFrames;
            case Seeker_Missile:
                return this.seekerProjectileFrames;
            case Bomba_Missile:
                return this.bombaProjectileFrames;
            case Bomba_Missile_Explosion:
                return this.bombaProjectileExplosion;
            case Default_Player_Engine:
                return this.defaultPlayerEngine;
            case LevelUpAnimation:
                return levelUpAnimation;
            case Default_Player_Shield_Damage:
                return this.defaultPlayerShieldDamage;
            case Electroshred:
                return this.playerEMPFrames;
            case Drone:
                return this.guardianBotFrames;
            case Highlight:
                return HighlightImages;
            case Rocket_1:
                return Rocket1ProjectileFrames;
            case Rocket_1_Explosion:
                return Rocket1ExplosionFrames;
            case Portal5:
                return portal5Frames;
            case WarpIn:
                return warpFrames;
            case Charging:
                return chargingFrames; // animation addition update: unused
            case EnergyCircle:
                return energyCircleFrames; // animation addition update: unused
            case PlasmaCoatedDebuff:
                return plasmaCoatedDebuff;
            case GasolineExplosion:
                return gasolineExplosion;
            case IgniteBurning:
                return gasolineBurning;
            case Healing:
                return healingAnimation;
            case Scout:
                return scout;
            case Needler:
                return needler;
            case Energizer:
                return energizer;
            case Bulldozer:
                return bulldozer;
        }
        return null;
    }

    public void initAnimations () throws IOException {
        ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();

        reader.setInput(ImageIO.createImageInputStream(getClass().getResourceAsStream("/images/gif/explosion.gif")));
        impactExplosionOneFrames = gifToImageIcons(reader);

        reader.setInput(
                ImageIO.createImageInputStream(getClass().getResourceAsStream("/images/gif/Destroyed Explosion.gif")));
        destroyedExplosionUpFrames = gifToImageIcons(reader);

        reader.setInput(
                ImageIO.createImageInputStream(getClass().getResourceAsStream("/images/gif/Alien Bomb Explosion.gif")));
        alienBombExplosionFrames = gifToImageIcons(reader);

        // Enemy projectiles
        reader.setInput(ImageIO.createImageInputStream(
                getClass().getResourceAsStream("/images/Ships/Ship 1/Ship 1 - Projectile.gif")));
        seekerProjectileFrames = gifToImageIcons(reader);
    }

    private void initPNGtoGIFAnimation () {

        for (int i = 0; i < 7; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Devourer/birth/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            devourerBirth.add(image);
        }

        for (int i = 0; i < 32; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/ModuleScorch/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            moduleScorchFlames.add(image);
        }

        for (int i = 0; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Zerg Cocoon/Devourer/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            devourerCocoon.add(image);
        }

        for (int i = 0; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Mutalisk/idle/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            mutaliskIdle.add(image);
        }

        for (int i = 0; i < 9; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Mutalisk/death/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            mutaliskDeath.add(image);
        }

        for (int i = 0; i < 10; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Mutalisk/missile/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            mutaliskMissile.add(image);
        }

        for (int i = 0; i < 10; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Mutalisk/missileimpact/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            mutaliskMissileImpact.add(image);
        }

        for (int i = 0; i < 6; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Mutalisk/birth/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            mutaliskBirth.add(image);
        }

        for (int i = 0; i < 9; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Zerg Cocoon/MutaGuardian/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            guardianMutaliskCocoon.add(image);
        }

        for (int i = 0; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Scourge/idle/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            scourgeIdle.add(image);
        }

        for (int i = 0; i < 9; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Scourge/death/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            scourgeDeath.add(image);
        }

        for (int i = 0; i < 8; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Scourge/explosion/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            scourgeExplosion.add(image);
        }

        for (int i = 0; i < 6; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Guardian/attacking/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            guardianChargingAnimation.add(image);
        }

        for (int i = 0; i < 7; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Guardian/idle/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            guardianIdle.add(image);
        }

        for (int i = 0; i < 9; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Guardian/death/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            guardianDeath.add(image);
        }

        for (int i = 0; i < 7; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Guardian/birth/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            guardianBirth.add(image);
        }

        for (int i = 0; i < 32; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Guardian/missile/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            guardianMissile.add(image);
        }

        for (int i = 0; i < 10; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Guardian/missile impact/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            guardianMissileImpact.add(image);
        }

        for (int i = 0; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Queen/idle/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            queenIdle.add(image);
        }

        for (int i = 0; i < 7; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Queen/attacking/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            queenAttacking.add(image);
        }

        for (int i = 0; i < 9; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Queen/death/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            queenDeath.add(image);
        }

        for (int i = 0; i < 8; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Queen/broodling/idle/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            broodlingIdle.add(image);
        }
        for (int i = 0; i < 4; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Queen/broodling/attacking/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            broodlingAttacking.add(image);
        }

        for (int i = 0; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Queen/broodling/death/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            broodlingDeath.add(image);
        }


        for (int i = 1; i < 6; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Default Player Engine/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            defaultPlayerEngine.add(image);
        }


        for (int i = 1; i < 10; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Player Shield Damage/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            defaultPlayerShieldDamage.add(image);
        }

        for (int i = 1; i < 41; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/FireWall/fire_1f_40_%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            fireWall.add(image);
        }

        for (int i = 1; i < 10; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Player EMP/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            playerEMPFrames.add(image);
        }

        for (int i = 0; i < 32; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/SelectNewClass/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            selectNewClassAnimation.add(image);
        }

        for (int i = 1; i < 16; i++) {
            String sourceString = String.format("/images/Ships/Guardian Bot/GuardianBot%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            guardianBotFrames.add(image);
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

        for (int i = 1; i < 65; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Portal5/portal%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            portal5Frames.add(image);
        }

        for (int i = 1; i < 10; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/WarpFrames/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            warpFrames.add(image);
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


        for (int i = 1; i < 4; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/PlasmaCoatedBurning/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            plasmaCoatedDebuff.add(image);
        }

        for (int i = 0; i < 63; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Explosion 1/tile0%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            stickyDynamiteExplosion.add(image);
        }

        for (int i = 0; i < 39; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/GasolineExplosion/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            gasolineExplosion.add(image);
        }

        for (int i = 0; i < 25; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/GasolineBurning/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            gasolineBurning.add(image);
        }

        for (int i = 0; i < 5; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Healing/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            healingAnimation.add(image);
        }

        for (int i = 0; i < 61; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/PlasmaLauncherMissile/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            plasmaLauncherMissileFrames.add(image);
        }

        for (int i = 0; i < 8; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Barrier Projectile/0%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            barrierProjectile.add(image);
        }

        for (int i = 0; i < 8; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Barrier Projectile/Destruction/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            barrierProjectileDestruction.add(image);
        }

        for (int i = 0; i < 11; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/SuperCharged/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            superChargedFrames.add(image);
        }

        for (int i = 1; i < 9; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/TazerProjectile/Thundersphere%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            lightningOrb.add(image);
        }
        for (int i = 2; i < 64; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Explosion 2/tile%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            explosion2.add(image);
        }

        for (int i = 0; i < 4; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/LaserBulletDeath/LaserBulletDeath%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            laserBulletDeathFrames.add(image);
        }

        for (int i = 1; i < 9; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/TazerProjectile/Destruction/ThundersphereDestruction%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            lightningOrbDestruction.add(image);
        }

        for (int i = 0; i < 35; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Level Up Animation/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            levelUpAnimation.add(image);
        }

        for (int i = 0; i < 64; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/CoinExplosion/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            cashExplosion.add(image);
        }

        for (int i = 1; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Ship 1/Animated/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            seekerFrames.add(image);
        }

        for (int i = 1; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/ScoutAnimated/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            scout.add(image);
        }

        for (int i = 1; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/NeedlerAnimated/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            needler.add(image);
        }

        for (int i = 1; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Ship 2/Animated/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            tazer.add(image);
        }

        for (int i = 1; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Ship 3/Animated/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            energizer.add(image);
        }

        for (int i = 1; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Ship 4/Animated/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            bulldozer.add(image);
        }

        for (int i = 1; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Ship 5/Animated/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            flamer.add(image);
        }

        for (int i = 1; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Ship 6/Animated/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            bomba.add(image);
        }


        for (int i = 1; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/RedBossAnimated/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            redBossFrames.add(image);
        }

        for (int i = 1; i < 4; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Spacebombs/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            destructableOrbitCenterMissile.add(image);
        }

        for (int i = 1; i < 5; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/AlienLaserbeam/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            alienLaserBeamAnimated.add(image);
        }

        for (int i = 1; i < 10; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Player EMP/doubled/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            electroShredImproved.add(image);
        }

        for (int i = 0; i < 7; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Thorns/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            thornsDamage.add(image);
        }

        for (int i = 0; i < 20; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Fireshield/looping/fireshield%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            fireFighterFireShield.add(image);
        }

        for (int i = 0; i < 16; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Fireshield/appearing/fireshield%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            fireFighterFireShieldAppearing.add(image);
        }

        for (int i = 0; i < 19; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/FireFighterPrimary/looping/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            fireFighterFlameThrowerLooping.add(image);
        }

        for (int i = 0; i < 10; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/FireFighterPrimary/init/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            fireFighterFlameThrowerAppearing.add(image);
        }

        for (int i = 0; i < 12; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/FireFighterPrimary/dissipating/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            fireFighterFlameThrowerDissipating.add(image);
        }

        for (int i = 0; i < 36; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/FreezeEffect/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            freezeEffect.add(image);
        }

        for (int i = 0; i < 91; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Shuriken/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            shurikenEnemy.add(image);
        }

        for (int i = 0; i < 4; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Laserbeam/Body/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            laserbeamBody.add(image);
        }

        for (int i = 0; i < 4; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Laserbeam/Start/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            laserbeamStart.add(image);
        }

        for (int i = 0; i < 4; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Laserbeam/End/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            laserbeamEnd.add(image);
        }

        for (int i = 0; i < 16; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/Laserbeam/ChargingUp/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            laserbeamChargingFrames.add(image);
        }

        for (int i = 0; i < 120; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/SpaceStation/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            spaceStationBossFrames.add(image);
        }

        for (int i = 1; i < 13; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/BombaProjectile/Missile/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            bombaProjectileFrames.add(image);
        }

        for (int i = 0; i < 24; i++) {
            String sourceString = String.format("/images/gif/PNGtoGIF/BombaProjectile/Explosion/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            bombaProjectileExplosion.add(image);
        }

        for (int i = 0; i < 7; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Devourer/idle/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            devourerIdle.add(image);
        }

        for (int i = 0; i < 5; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Devourer/attacking/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            devourerAttacking.add(image);
        }

        for (int i = 1; i < 9; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Devourer/death/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            devourerDeath.add(image);
        }

        for (int i = 0; i < 4; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Devourer/projectile/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            devourerMissile.add(image);
        }

        for (int i = 0; i < 10; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Devourer/projectile/explosion/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            devourerMissileExplosion.add(image);
        }

        for (int i = 0; i < 4; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Devourer/projectile/debuff/stage1/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            devourerDebuffStage1.add(image);
        }

        for (int i = 0; i < 4; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Devourer/projectile/debuff/stage2/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            devourerDebuffStage2.add(image);
        }

        for (int i = 0; i < 4; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Devourer/projectile/debuff/stage3/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            devourerDebuffStage3.add(image);
        }

        for (int i = 0; i < 4; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Devourer/projectile/debuff/stage4/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            devourerDebuffStage4.add(image);
        }

        for (int i = 0; i < 3; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/Overlord/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            overlordIdle.add(image);
        }

        for (int i = 0; i < 4; i++) {
            String sourceString = String.format("/images/Ships/Enemy Ships/CashCarrier/%d.png", i);
            BufferedImage image = imgLoader.getSpritesheetImageFromStream(getClass().getResourceAsStream(sourceString));
            cashCarrierFrames.add(image);
        }

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
        List<BufferedImage> sprites = new ArrayList<>();
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
}