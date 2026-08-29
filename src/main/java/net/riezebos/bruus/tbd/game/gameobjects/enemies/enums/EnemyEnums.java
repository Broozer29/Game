package net.riezebos.bruus.tbd.game.gameobjects.enemies.enums;

import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public enum EnemyEnums {
    Alien_Bomb(25, 9,
            AudioEnums.Alien_Bomb_Destroyed,
            ImageEnums.Alien_Bomb, ImageEnums.Alien_Bomb_Explosion, 3, EnemyCategory.Summon, EnemyTribes.Pirates, 0,
            0, 0, 0, 1, 1, 25, 15,
            0),
    Seeker(125, 13,
            AudioEnums.Large_Ship_Destroyed,
            ImageEnums.Seeker, ImageEnums.Explosion2, 55, EnemyCategory.Medium, EnemyTribes.Pirates, 10,
            8, 15, 0.55f, 1.3f, 0.9f, 75, 29,
            0),
    Tazer(125, 10,
            AudioEnums.Large_Ship_Destroyed,
            ImageEnums.Tazer, ImageEnums.Explosion2, 55, EnemyCategory.Medium, EnemyTribes.Pirates, 10,
            8, 15, 0.35f, 1.1f, 1, 94, 34,
            99999),
    Energizer(150, 15,
            AudioEnums.Large_Ship_Destroyed,
            ImageEnums.Energizer, ImageEnums.Explosion2, 50, EnemyCategory.Medium, EnemyTribes.Pirates, 10,
            9, 15, 0.7f, 1, 1, 106, 39,
            2),
    Bulldozer(200, 0,
            AudioEnums.Large_Ship_Destroyed,
            ImageEnums.Bulldozer, ImageEnums.Explosion2, 50, EnemyCategory.Medium, EnemyTribes.Pirates, 20,
            10, 15, 0.65f, 1.3f, 1, 119, 46,
            0),
    Flamer(150, 5,
            AudioEnums.Large_Ship_Destroyed,
            ImageEnums.Flamer, ImageEnums.Explosion2, 50, EnemyCategory.Medium, EnemyTribes.Pirates, 10,
            10, 15, 0.65f, 1, 1, 137, 61,
            2),
    Bomba(200, 15,
            AudioEnums.Large_Ship_Destroyed,
            ImageEnums.Bomba, ImageEnums.Explosion2, 55, EnemyCategory.Medium, EnemyTribes.Pirates, 20,
            10, 15, 0.45f, 1, 1, 140, 75,
            3),
    Needler(50, 13,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.Needler, ImageEnums.Explosion2, 30, EnemyCategory.Small, EnemyTribes.Pirates, 10,
            3f, 3, 0.7f, 2, 0.85f, 76, 40,
            0),

    CashCarrier(200, 0,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.CashCarrier, ImageEnums.Explosion2, 0, EnemyCategory.Special, EnemyTribes.Pirates, 30,
            8, 25, 0, 1.2f, 0.8f, 171, 98,
            0),
    Scout(50, 9,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.Scout, ImageEnums.Explosion2, 30, EnemyCategory.Small, EnemyTribes.Pirates, 10,
            3f, 3, 0.9f, 1.4f, 0.9f, 65, 38,
            0),

    FourDirectionalDrone(205, 6,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.FourDirectionalDrone, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Generic, 20,
            0, 0, 0, 1.85f, 0.4f, 170, 170,
            0),
    EnemyProtossScout(150, 0, //damage overwritten by carrierboss
            AudioEnums.ProtossShipDeath,
            ImageEnums.ProtossScout, ImageEnums.ProtossDestroyedExplosion, 0, EnemyCategory.Summon, EnemyTribes.Generic, 0,
            0, 0, 0, 2.5f, 0.25f, 207, 116,
            0),

    EnemyProtossShuttle(200, 0, //damage overwritten by carrierboss
            AudioEnums.ProtossShipDeath,
            ImageEnums.ProtossShuttle, ImageEnums.ProtossDestroyedExplosion, 0, EnemyCategory.Summon, EnemyTribes.Generic, 0,
            0, 0, 0, 1.5f, 0.25f, 231, 125,
            0),
    CarrierPulsingDrone(2000, 8,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.CarrierPulsingDrone, ImageEnums.ProtossDestroyedExplosion, 0, EnemyCategory.Summon, EnemyTribes.Generic, 0,
            0, 0, 0, 1, 0.75f, 100, 100,
            0),

    EnemyCarrierBeacon(200, 0,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.ProtossCarrierBeacon, ImageEnums.ProtossDestroyedExplosion, 0, EnemyCategory.Summon, EnemyTribes.Generic, 0,
            0, 0, 0, 1, 0.6f, 100, 100,
            0),

    Shuriken(125, 30,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.ShurikenEnemy, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Generic, 10,
            0, 0, 0, 2f, 0.25f, 300, 300,
            0),

    PulsingDrone(225, 0,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.DestructableOrbitCenterMissile, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Generic, 0,
            0, 0, 0, 2f, 0.3f, 160, 119,
            0),

    ZergDevourer(90, 10,
            AudioEnums.DevourerDeath,
            ImageEnums.DevourerIdle, ImageEnums.DevourerDeath, 50, EnemyCategory.Medium, EnemyTribes.Zerg, 10,
            7, 15, 0.55f, 1.2f, 1, 66, 54,
            1),

    ZergGuardian(90, 8,
            AudioEnums.GuardianDeath,
            ImageEnums.GuardianIdle, ImageEnums.GuardianDeath, 45, EnemyCategory.Medium, EnemyTribes.Zerg, 10,
            7, 15, 0.75f, 1.1f, 1, 78, 71,
            0),
    ZergQueen(100, 10,
            AudioEnums.QueenDeath,
            ImageEnums.QueenIdle, ImageEnums.QueenDeath, 60, EnemyCategory.Medium, EnemyTribes.Zerg, 0,
            10, 20, 2, 1.35f, 1, 75, 68,
            3),
    ZergScourge(35, 13,
            AudioEnums.ScourgeDeath,
            ImageEnums.ScourgeIdle, ImageEnums.ScourgeDeath, 27.5f, EnemyCategory.Small, EnemyTribes.Zerg, 0,
            3, 3, 0.7f, 2.5f, 1.4f, 31, 27,
            0),
    ZergMutalisk(100, 0.3f,
            AudioEnums.MutaliskDeath,
            ImageEnums.MutaliskIdle, ImageEnums.MutaliskDeath, 27, EnemyCategory.Small, EnemyTribes.Zerg, 10,
            7f, 15, 0.65f, 1.55f, 1.2f, 64, 72,
            0),
    DevourerCocoon(50, 0,
            AudioEnums.ScourgeDeath,
            ImageEnums.DevourerCocoon, ImageEnums.DevourerDeath, 0, EnemyCategory.Summon, EnemyTribes.Zerg, 0,
            0, 3, 1, 1f, 1, 95, 82,
            0),
    MutaGuardianCocoon(50, 0,
            AudioEnums.ScourgeDeath,
            ImageEnums.GuardianMutaliskCocoon, ImageEnums.GuardianDeath, 0, EnemyCategory.Summon, EnemyTribes.Zerg, 0,
            0, 3, 1, 1f, 1, 60, 42,
            0),

    ShurikenMiniBoss(1050, 1,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.ShurikenMiniBoss, ImageEnums.Explosion2, 0, EnemyCategory.MiniBoss, EnemyTribes.Generic, 25,
            50, 100, 1, 2f, 1, 600, 600,
            0),

    MotherShipMiniBoss(1050, 10,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.MothershipMiniboss, ImageEnums.Explosion2, 0, EnemyCategory.MiniBoss, EnemyTribes.Generic, 10,
            50, 100, 1, 1f, 0.35f, 238, 125,
            0),
    MotherShipDrone(125, 0, //damage overwritten by mothership
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.MotherShipDrone, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Generic, 0,
            0, 0, 0, 1.5f, 0.35f, 269, 125,
            0),

    StrikerBossLaserbeamClone(250, 15,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.StrikerBoss, ImageEnums.BossExplosion, 0, EnemyCategory.Summon, EnemyTribes.Generic, 0,
            0, 0, 0, 1, 1f, 445, 346,
            0),

    StrikerBossCornerDrone(99999999, 5,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.StrikerCornerDrone, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Generic, 0,
            0, 0, 0, 1, 1f, 118, 66,
            0),

    RedBoss(45000, 9,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.RedBoss, ImageEnums.BossExplosion, 0, EnemyCategory.Boss, EnemyTribes.Generic, 20,
            950, 500, 0, 1.25f, 1, 861, 641,
            0),

    SpaceStationBoss(4450,11,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.SpaceStationBoss, ImageEnums.BossExplosion, 0, EnemyCategory.Boss, EnemyTribes.Generic, 20,
            950, 500, 0, 1.75f, 1, 850, 850,
            0),
    CarrierBoss(4800, 10,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.CarrierBoss, ImageEnums.BossExplosion, 0, EnemyCategory.Boss, EnemyTribes.Generic, 20,
            1050, 500, 0, 1.25f, 0.75f, 465, 252,
            1),
    YellowBoss(5000, 9,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.YellowBoss, ImageEnums.BossExplosion, 0, EnemyCategory.Boss, EnemyTribes.Generic, 20,
            1150, 500, 0, 1.25f, 0.75f, 843, 800,
            1),

    TwinBoss(4000, 9,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.TwinBoss, ImageEnums.BossExplosion, 0, EnemyCategory.Boss, EnemyTribes.Generic, 20,
            1150, 500, 0, 2.15f, 0.5f, 386, 285,
            2),

    StrikerBoss(4450, 9,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.StrikerBoss, ImageEnums.BossExplosion, 0, EnemyCategory.Boss, EnemyTribes.Generic, 20,
            1150, 500, 0, 1.3f, 1, 445, 346,
            1),

    FinalBoss(7000, 12,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.FinalBossAnim, ImageEnums.BossExplosion, 0, EnemyCategory.Boss, EnemyTribes.Generic, 20,
            1150, 500, 0, 2f, 0.25f, 337, 196,
            99999),

    BlueBoss(4850, 9,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.BlueBoss, ImageEnums.BossExplosion, 0, EnemyCategory.Boss, EnemyTribes.Generic, 20,
            1150, 500, 0, 0.15f, 1, 337, 196,
            2),

    BlueBossFactory(2500000, 5,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.BlueBossFactory, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Generic, 0,
            0, 0, 0, 0.15f, 1f, 323, 149,
            0),

    BlueBossFactoryDefender(250, 0, //inherited from the factory
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.BlueBossFactoryDefender, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Generic, 0,
            0, 0, 0, 4, 0.5f, 92, 57,
            0),

    MirageMiniBoss(850, 10,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.MirageMiniBoss, ImageEnums.Explosion2, 0, EnemyCategory.MiniBoss, EnemyTribes.Generic, 0,
            50, 100, 1, 1.55f, 0.8f, 154, 99,
            0),

    DefenderMiniBoss(1050, 10,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.DefenderMiniBoss, ImageEnums.Explosion2, 0, EnemyCategory.MiniBoss, EnemyTribes.Generic, 35,
            50, 100, 1, 1.55f, 1, 250, 250,
            0),

    LaserbeamMiniBoss(1050, 6,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.LaserMiniBoss, ImageEnums.Explosion2, 0, EnemyCategory.MiniBoss, EnemyTribes.Generic, 25,
            50, 100, 1, 1.45f, 1, 250, 250,
            0),

    LaserOriginDrone(125, 10,
            AudioEnums.Alien_Spaceship_Destroyed,
            ImageEnums.Test_Image, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Generic, 0,
            0, 0, 0, 0f, 0.5f, 170, 170,
            0),

    RoyalGuardBarricade(200, 12,
            AudioEnums.Large_Ship_Destroyed,
            ImageEnums.RoyalGuardBarricade, ImageEnums.Explosion2, 75, EnemyCategory.Medium, EnemyTribes.RoyalGuard, 10,
            10, 15, 0.55f, 0.8f, 0.65f, 257, 170,
            0),
    RoyalGuardBarricadeMinion(75, 10,
            AudioEnums.Alien_Bomb_Destroyed,
            ImageEnums.RoyalGuardBarricadeMinion, ImageEnums.Alien_Bomb_Explosion, 0, EnemyCategory.Summon, EnemyTribes.RoyalGuard, 0,
            0, 0, 0.55f, 2, 1, 44, 28,
            50),

    RoyalGuardCaptain(125, 4,
            AudioEnums.Large_Ship_Destroyed,
            ImageEnums.RoyalGuardCaptain, ImageEnums.Explosion2, 55, EnemyCategory.Medium, EnemyTribes.RoyalGuard, 10,
            10, 15, 0.2f, 1, 0.65f, 201, 84,
            0),

    RoyalGuardFlagbearer(120, 5,
            AudioEnums.Large_Ship_Destroyed,
            ImageEnums.RoyalGuardFlagbearer, ImageEnums.Explosion2, 85, EnemyCategory.Medium, EnemyTribes.RoyalGuard, 10,
            10, 15, 0.3f, 1.6f, 0.55f, 175, 61,
            99),


    RoyalGuardGrenadier(125, 10,
            AudioEnums.Large_Ship_Destroyed,
            ImageEnums.RoyalGuardGrenadier, ImageEnums.Explosion2, 55, EnemyCategory.Medium, EnemyTribes.RoyalGuard, 10,
            10, 15, 0.55f, 1, 0.65f, 190, 128,
            50),

    RoyalGuardGuardsmen(150, 10,
            AudioEnums.Large_Ship_Destroyed,
            ImageEnums.RoyalGuardGuardsmen, ImageEnums.Explosion2, 75, EnemyCategory.Medium, EnemyTribes.RoyalGuard, 10,
            10, 15, 0.55f, 1.3f, 0.5f, 177, 67,
            0),


    RoyalGuardShieldbearer(140, 10,
            AudioEnums.Large_Ship_Destroyed,
            ImageEnums.RoyalGuardShieldbearer, ImageEnums.Explosion2, 75, EnemyCategory.Medium, EnemyTribes.RoyalGuard, 10,
            10, 15, 0.55f, 1.3f, 0.5f, 221, 122,
            0),

    ;


    private final int baseHitPoints;
    private float baseDamage;

    private final AudioEnums deathSound;
    private final ImageEnums imageType;

    private final ImageEnums destructionType;
    private final float creditCost;
    private EnemyCategory enemyCategory;
    private EnemyTribes enemyTribe;

    private float baseArmor;
    private float cashMoneyWorth;
    private float xpOnDeath;
    private float weight;
    private float movementSpeed;
    private float defaultScale;

    private int baseWidth;
    private int baseHeight;
    private int minimumStageLevelRequired;


    EnemyEnums(int baseHitPoints, float baseDamage, AudioEnums deathSound,
               ImageEnums imageType, ImageEnums destructionType, float creditCost,
               EnemyCategory enemyCategory, EnemyTribes enemyTribe, float baseArmor, float cashMoneyWorth, float xpOnDeath,
               float weight, float movementSpeed, float defaultScale, int baseWidth, int baseHeight,
               int minimumStageLevelRequired) {
        this.baseDamage = baseDamage;
        this.enemyTribe = enemyTribe;
        this.baseHitPoints = baseHitPoints;
        this.deathSound = deathSound;
        this.imageType = imageType;
        this.destructionType = destructionType;
        this.creditCost = creditCost;
        this.enemyCategory = enemyCategory;
        this.baseArmor = baseArmor;
        this.cashMoneyWorth = cashMoneyWorth;
        this.xpOnDeath = xpOnDeath;
        this.weight = weight;
        this.movementSpeed = movementSpeed;
        this.defaultScale = defaultScale;
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
        this.minimumStageLevelRequired = minimumStageLevelRequired;
    }

    public int getBaseHitPoints() {
        return baseHitPoints;
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public AudioEnums getDeathSound() {
        return deathSound;
    }

    public ImageEnums getImageEnum() {
        return imageType;
    }

    public ImageEnums getDestructionImageEnum() {
        return destructionType;
    }

    public float getCreditCost() {
        return creditCost;
    }

    public EnemyCategory getEnemyCategory() {
        return enemyCategory;
    }

    public float getBaseArmor() {
        return baseArmor;
    }


    public ImageEnums getImageType() {
        return imageType;
    }

    public float getCashMoneyWorth() {
        return cashMoneyWorth;
    }

    public float getXpOnDeath() {
        return xpOnDeath;
    }

    public ImageEnums getDestructionType() {
        return destructionType;
    }

    public float getWeight() {
        return weight;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public float getDefaultScale() {
        return defaultScale;
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    public int getBaseWidth() {
        return baseWidth;
    }

    public static EnemyEnums getRandomEnemyByCategory(EnemyCategory category, EnemyTribes enemyTribe) {
        List<EnemyEnums> filteredEnemies = Arrays.stream(EnemyEnums.values())
                .filter(enemy -> enemy.getEnemyCategory().equals(category) && enemy.getEnemyTribe().equals(enemyTribe))
                .collect(Collectors.toList());
        if (filteredEnemies.isEmpty()) {
            return EnemyEnums.Scout;
        }

        return filteredEnemies.get(new Random().nextInt(filteredEnemies.size()));
    }

    public int getMinimumStageLevelRequired() {
        return minimumStageLevelRequired;
    }

    public static int getAmountOfBossEnemies() {
        return Arrays.stream(EnemyEnums.values())
                .filter(enemy -> enemy.getEnemyCategory().equals(EnemyCategory.Boss))
                .toList().size();
    }

    public EnemyTribes getEnemyTribe() {
        return enemyTribe;
    }

    public int getBossKillCountRequiredBeforeAllowedToSpawn() {
        return this.minimumStageLevelRequired; //since bosses cannot be spawn regularly, we can simply re-use this field
    }
}