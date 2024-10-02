package VisualAndAudioData.image;

import game.gameobjects.enemies.enemytypes.Needler;
import game.gameobjects.enemies.enemytypes.Seeker;

public enum ImageEnums {

    //// Menu Buttons
    Start_Game, User_One, User_Two, User_Three, Select_User_Menu, Test_Image, User_Menu_To_Main_Menu, Title_Image, ProgressBar, ProgressBarFilling,

    //// Animations
    Impact_Explosion_One, Player_Engine, Destroyed_Explosion, Destroyed_Explosion_Right, Destroyed_Explosion_Left,
    Destroyed_Explosion_Down, Alien_Bomb_Explosion, Seeker_Normal_Exhaust, Seeker_Large_Exhaust, Tazer_Normal_Exhaust,
    Tazer_Large_Exhaust, Energizer_Normal_Exhaust, Energizer_Large_Exhaust, Bulldozer_Normal_Exhaust,
    Bulldozer_Large_Exhaust, Flamer_Normal_Exhaust, Flamer_Large_Exhaust, Bomba_Normal_Exhaust, Bomba_Large_Exhaust,
    Seeker_Destroyed_Explosion, Tazer_Destroyed_Explosion, Energizer_Destroyed_Explosion, Bomba_Destroyed_Explosion,
    Flamer_Destroyed_Explosion, Bulldozer_Destroyed_Explosion, Default_Player_Engine, Default_Player_Engine_Boosted,
    Default_Player_Shield_Damage, Player_Fireswirl, Player_EMP, Player_EMP_Plus, Drone, FirewallParticle, Portal5,
    WarpIn, Shield4, Shield3, Shield2, Charging, EnergyCircle, Flamewarp, Frontshield, FrontshieldB, PulsatingShield, PulsatingStar, Radar,
    RotatingBoxes, RotatingCones, GreenEnergyOrb, PurpleEnergyBlocks, Fireshield, EnergyFlower, PurpleEnergyBarrier, RedHole, CrossingEnergyBeams,
    PlasmaCoatedDebuff, GasolineExplosion, GasolineBurning, Healing, StickyDynamiteExplosion, MoneyPrinter, StickyDynamite,
    BarrierProjectile, BarrierProjectileDestruction, LightningProjectile, SuperChargedBuff, LightningOrb, Explosion2, LaserBulletDestruction,
    LightningOrbDestruction, LevelUpAnimation, CirclingRings, SpiralingGreenEnergy,GreenEnergyOrb2Looping,GreenEnergyOrb2, ElectroShredImproved, ThornsDamage,
    FreezeEffect, ShurikenEnemy,

    //// Background Objects
    Moon, Lava_Planet, Planet_One, Planet_Two, Planet_Three, Mars_Planet, Star, Parallex_1, Parallex_2, Parallex_3,
    Parallex_4, Parallex_5, Warm_Nebula, Cold_Nebula, Regular_Nebula, Blue_Nebula_1, Blue_Nebula_2, Blue_Nebula_3,
    Blue_Nebula_4, Blue_Nebula_5, Blue_Nebula_6, Green_Nebula_1, Green_Nebula_2, Green_Nebula_3, Green_Nebula_4,
    Green_Nebula_5, Green_Nebula_6, Green_Nebula_7, Purple_Nebula_1, Purple_Nebula_2, Purple_Nebula_3, Purple_Nebula_4,
    Purple_Nebula_5, Purple_Nebula_6, Purple_Nebula_7, Star_Blue1, Star_Blue2, Star_Blue3, Star_Blue4, Star_Red1, Star_Red2,
    Star_Red3, Star_Red4, Star_Yellow1, Star_Yellow2, Star_Yellow3, Star_Yellow4, Star_Orange1, Star_Orange2, Star_Orange3, Star_Orange4,
    Star_White1, Star_White2, Star_White3, Star_White4, Moon2, Moon3, Moon4, Moon5, GreenPlanet1, GreenPlanet2, BluePlanet1,
    BluePlanet2, BluePlanet3, BluePlanet4, BluePlanet5, BluePlanet6, CashCarrier, CashExplosion, GradeBronze, GradeSilver, GradeGold, GradePlatinum, GradeDiamond
    , GradeMaster, GradeGrandMaster, UIScoreTextCard, UIYouDied, UILevelComplete, Starcraft2Keystone, Starcraft2BouncingLaser, VIPTicket, PiercingLaser,
    Starcraft2LockOn, ThornWeaver, BarbedMissiles, BarbedAegis, Thornedplates,
    // Missile Animations
    Seeker_Missile, Tazer_Missile, Energizer_Missile, Bulldozer_Missile, Flamer_Missile, Bomba_Missile,
    Seeker_Missile_Explosion, Tazer_Missile_Explosion, Energizer_Missile_Explosion, Bulldozer_Missile_Explosion,
    Flamer_Missile_Explosion, Bomba_Missile_Explosion, Flamethrower_Animation, Rocket_1, Rocket_1_Explosion, Firespout_Animation,
    PlasmaLauncherMissile, DestructableOrbitCenterMissile, LaserbeamBody, LaserbeamHead, LaserbeamStart, LaserbeamEnd,

    // Missile Images
    Player_Laserbeam, Alien_Laserbeam, AlienLaserBeamAnimated, LaserBullet,

    // Friendly images
    Player_Spaceship, Player_Spaceship_Model_3,

    // Enemy images
    Alien_Bomb, Seeker, Tazer, Energizer, Bulldozer, Flamer, Bomba, Alien, Alien_Bomb_Animation, Needler, Scout, FourDirectionalDrone,
    RedBoss,

    // Unused?
    Implosion, Invisible, Invisible_Animation,

    // GUI
    Health_Bar, Shield_Bar, Icon_Border, Health_Shield_Frames, Frame, Red_Filling, Gold_Filling, Blue_Filling, Highlight, Long_Card, Wide_Card, Square_Card,
    InformationCard,
    YellowWings1, YellowWings2, YellowWings3, YellowWings4, YellowWings5, BlueWings1, BlueWings2, BlueWings3, BlueWings4, BlueWings5,
    PurpleWings1,PurpleWings2,PurpleWings3,PurpleWings4,PurpleWings5, RedWings1, RedWings2, RedWings3, RedWings4, RedWings5,
    // Icons
    TripleShotIcon, DoubleShotIcon, Starcraft2_Point_Defense_Drone, Starcraft2_Protoss_Cloak, LockedIcon, TopazGem7,
    Starcraft2_Protoss_Shield_Disintegrate, Starcraft2_Protoss_Shields_1, Starcraft2_Protoss_Shields_2,
    Starcraft2_Protoss_Shields_3, Starcraft2_Psi_Storm1, Starcraft2_Psi_Storm2, Starcraft2_Psi_Storm3,
    Starcraft2_Pulse_Grenade, Starcraft2_Pulse_Laser, Starcraft2_Repair_Blink, Starcraft2_Rocket_Cluster,
    Starcraft2_Dual_Rockets, Starcraft2_Artanis_Shield, Starcraft2_Auto_Tracking, Starcraft2_Blink,
    Starcraft2_Blue_Flame, Starcraft2_Concussive_Shells, Starcraft2_Corsair_Cloak, Starcraft2_Drone_Cloak,
    Starcraft2_DT_Blink, Starcraft2_Energizer_Speed, Starcraft2_Energizer_Speed2, Starcraft2_Fire_Cloak,
    Starcraft2_Energy_Siphon, Starcraft2_Fire_Hardened_Shields, Starcraft2_Flame_Turret, Starcraft2_Force_Field,
    Starcraft2_Guardian_Shield, Starcraft2_Hardened_Shields, Starcraft2_Health_Upgrade_1, Starcraft2_Health_Upgrade_2,
    Starcraft2_Ignite_Afterburners, Starcraft2_Immortal_Barrier, Starcraft2_Immortal_Original_Barrier,
    Starcraft2_LaserBeam, Starcraft2_LaserDrill, Starcraft2_MovementSpeed, Starcraft2_Seeker_Missile,
    Starcraft2_Shield_Barrier, Starcraft2_Shield_Piercing, Starcraft2_Stim1, Starcraft2_Stim2, Starcraft2_Stim3,
    Starcraft2_Terran_Plating1, Starcraft2_Terran_Plating2, Starcraft2_Terran_Plating3, Starcraft2_Terran_Speed1,
    Starcraft2_Terran_Speed2, Starcraft2_Terran_Speed3, Starcraft2_Terran_Weapons1, Starcraft2_Terran_Weapons2,
    Starcraft2_Terran_Weapons3, Starcraft2_Third_Blink, Starcraft2_Time_Warp, Starcraft2_Vespene_Gas,
    Starcraft2_Vespene_Siphon, Starcraft2_Vespene_Drone, Starcraft2_Wraith_Cloak, Starcraft2_Yellow_Blink,
    Starcraft2_Heal, Starcraft2_Electric_Field, Starcraft2_Firebat_Weapon, Starcraft2_Advanced_Optics, CannisterOfGasoline,
    Starcraft2_Battery, Starcraft2_Focused_Crystal, Starcraft2_Platinum_Sponge, Starcraft2_Overclock, Starcraft2_Armor_Piercing,


    //Peepo's
    peepoDeepFriedSadge, peepoFeelsCringeMan, peepoFeelsRetardedMan, peepoHmmm, peepoLookingDown, peepoMonkaHmmm, peepoMonkaLaugh,
    peepoPauseChamp, peepoClown, peepoCringe, peepoLaugh, peepoLyingSadge, peepoOkay, peepoSad, peepoSad2, peepoShrug,
    peepoSmadge, peepoSmokedge, peepoSmug, peepoStare, peepoUhm, peepoAngy, peepoBruh, peepoCoffee, peepoConfused, peepoGottem,
    peepoHands, peepoLaugh2, peepoPointLaugh, peepoW, peepoSadClown, peepoSadge, peepoSadgeCry, peepoShruge, peepoSkillIssue, GUIRefresh,


    // Letters
    Letter_a, Letter_b, Letter_c, Letter_d, Letter_e, Letter_f, Letter_g, Letter_h, Letter_i, Letter_j, Letter_k,
    Letter_l, Letter_m, Letter_n, Letter_o, Letter_p, Letter_q, Letter_r, Letter_s, Letter_t, Letter_u, Letter_v,
    Letter_w, Letter_x, Letter_y, Letter_z, Letter_Open_Bracket, Letter_Closing_Bracket, Letter_double_points,
    Letter_equals, Letter_point_comma, Letter_greater_than, Letter_smaller_than, Letter_Dot, Letter_A, Letter_B,
    Letter_C, Letter_D, Letter_E, Letter_F, Letter_G, Letter_H, Letter_I, Letter_J, Letter_K, Letter_L, Letter_M,
    Letter_N, Letter_O, Letter_P, Letter_Q, Letter_R, Letter_S, Letter_T, Letter_U, Letter_V, Letter_W, Letter_X,
    Letter_Y, Letter_Z, Letter_Percentage, Letter_Komma, Letter_One, Letter_Two, Letter_Three, Letter_Four, Letter_Five,
    Letter_Six, Letter_Seven, Letter_Eight, Letter_Nine, Letter_Zero;

    public static ImageEnums fromChar (char c) {
        switch (c) {
            case 'a':
                return ImageEnums.Letter_a;
            case 'b':
                return ImageEnums.Letter_b;
            case 'c':
                return ImageEnums.Letter_c;
            case 'd':
                return ImageEnums.Letter_d;
            case 'e':
                return ImageEnums.Letter_e;
            case 'f':
                return ImageEnums.Letter_f;
            case 'g':
                return ImageEnums.Letter_g;
            case 'h':
                return ImageEnums.Letter_h;
            case 'i':
                return ImageEnums.Letter_i;
            case 'j':
                return ImageEnums.Letter_j;
            case 'k':
                return ImageEnums.Letter_k;
            case 'l':
                return ImageEnums.Letter_l;
            case 'm':
                return ImageEnums.Letter_m;
            case 'n':
                return ImageEnums.Letter_n;
            case 'o':
                return ImageEnums.Letter_o;
            case 'p':
                return ImageEnums.Letter_p;
            case 'q':
                return ImageEnums.Letter_q;
            case 'r':
                return ImageEnums.Letter_r;
            case 's':
                return ImageEnums.Letter_s;
            case 't':
                return ImageEnums.Letter_t;
            case 'u':
                return ImageEnums.Letter_u;
            case 'v':
                return ImageEnums.Letter_v;
            case 'w':
                return ImageEnums.Letter_w;
            case 'x':
                return ImageEnums.Letter_x;
            case 'y':
                return ImageEnums.Letter_y;
            case 'z':
                return ImageEnums.Letter_z;
            case '(':
                return ImageEnums.Letter_Open_Bracket;
            case ')':
                return ImageEnums.Letter_Closing_Bracket;
            case ':':
                return ImageEnums.Letter_double_points;
            case '=':
                return ImageEnums.Letter_equals;
            case ';':
                return ImageEnums.Letter_point_comma;
            case '>':
                return ImageEnums.Letter_greater_than;
            case '<':
                return ImageEnums.Letter_smaller_than;
            case 'A':
                return ImageEnums.Letter_A;
            case 'B':
                return ImageEnums.Letter_B;
            case 'C':
                return ImageEnums.Letter_C;
            case 'D':
                return ImageEnums.Letter_D;
            case 'E':
                return ImageEnums.Letter_E;
            case 'F':
                return ImageEnums.Letter_F;
            case 'G':
                return ImageEnums.Letter_G;
            case 'H':
                return ImageEnums.Letter_H;
            case 'I':
                return ImageEnums.Letter_I;
            case 'J':
                return ImageEnums.Letter_J;
            case 'K':
                return ImageEnums.Letter_K;
            case 'L':
                return ImageEnums.Letter_L;
            case 'M':
                return ImageEnums.Letter_M;
            case 'N':
                return ImageEnums.Letter_N;
            case 'O':
                return ImageEnums.Letter_O;
            case 'P':
                return ImageEnums.Letter_P;
            case 'Q':
                return ImageEnums.Letter_Q;
            case 'R':
                return ImageEnums.Letter_R;
            case 'S':
                return ImageEnums.Letter_S;
            case 'T':
                return ImageEnums.Letter_T;
            case 'U':
                return ImageEnums.Letter_U;
            case 'V':
                return ImageEnums.Letter_V;
            case 'W':
                return ImageEnums.Letter_W;
            case 'X':
                return ImageEnums.Letter_X;
            case 'Y':
                return ImageEnums.Letter_Y;
            case 'Z':
                return ImageEnums.Letter_Z;
            case '.':
                return ImageEnums.Letter_Dot;
            case '%':
                return ImageEnums.Letter_Percentage;
            case ',':
                return ImageEnums.Letter_Komma;
            case '1':
                return ImageEnums.Letter_One;
            case '2':
                return ImageEnums.Letter_Two;
            case '3':
                return ImageEnums.Letter_Three;
            case '4':
                return ImageEnums.Letter_Four;
            case '5':
                return ImageEnums.Letter_Five;
            case '6':
                return ImageEnums.Letter_Six;
            case '7':
                return ImageEnums.Letter_Seven;
            case '8':
                return ImageEnums.Letter_Eight;
            case '9':
                return ImageEnums.Letter_Nine;
            case '0':
                return ImageEnums.Letter_Zero;
            default:
                throw new IllegalArgumentException("No ImageEnums enum constant for character: " + c);
        }

    }

    public static ImageEnums getImageEnumForDigit (char digit) {
        switch (digit) {
            case '0':
                return ImageEnums.Letter_Zero;
            case '1':
                return ImageEnums.Letter_One;
            case '2':
                return ImageEnums.Letter_Two;
            case '3':
                return ImageEnums.Letter_Three;
            case '4':
                return ImageEnums.Letter_Four;
            case '5':
                return ImageEnums.Letter_Five;
            case '6':
                return ImageEnums.Letter_Six;
            case '7':
                return ImageEnums.Letter_Seven;
            case '8':
                return ImageEnums.Letter_Eight;
            case '9':
                return ImageEnums.Letter_Nine;
            default:
                throw new IllegalArgumentException("Invalid digit: " + digit);
        }
    }
}