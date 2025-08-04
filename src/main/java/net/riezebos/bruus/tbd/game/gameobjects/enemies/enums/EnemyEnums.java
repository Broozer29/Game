package net.riezebos.bruus.tbd.game.gameobjects.enemies.enums;

import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public enum EnemyEnums {
    Alien_Bomb(25, 0, false,
            AudioEnums.Alien_Bomb_Destroyed, "Alien Bomb",
            ImageEnums.Alien_Bomb, ImageEnums.Alien_Bomb_Explosion, 3, EnemyCategory.Summon, EnemyTribes.Pirates, 0,
            0, 0, 0, 1, 1, 25, 15,
            0),
    Seeker(125, 0, true,
            AudioEnums.Large_Ship_Destroyed, "Enemy Seeker",
            ImageEnums.Seeker, ImageEnums.Explosion2, 55, EnemyCategory.Medium, EnemyTribes.Pirates, 10,
            8, 15, 0.45f, 1.3f, 0.9f, 75, 29,
            0),
    Tazer(125, 0, true,
            AudioEnums.Large_Ship_Destroyed, "Enemy Tazer",
            ImageEnums.Tazer, ImageEnums.Explosion2, 55, EnemyCategory.Medium, EnemyTribes.Pirates, 10,
            8, 15, 0.25f, 1, 1, 94, 34,
            1),
    Energizer(150, 0, true,
            AudioEnums.Large_Ship_Destroyed, "Enemy Energizer",
            ImageEnums.Energizer, ImageEnums.Explosion2, 50, EnemyCategory.Medium, EnemyTribes.Pirates, 10,
            8, 15, 0.6f, 1, 1, 106, 39,
            2),
    Bulldozer(200, 0, false,
            AudioEnums.Large_Ship_Destroyed, "Enemy Bulldozer",
            ImageEnums.Bulldozer, ImageEnums.Explosion2, 50, EnemyCategory.Medium, EnemyTribes.Pirates, 20,
            13, 15, 0.6f, 1.3f, 1, 119, 46,
            0),
    Flamer(150, 0, true,
            AudioEnums.Large_Ship_Destroyed, "Enemy Flamer",
            ImageEnums.Flamer, ImageEnums.Explosion2, 50, EnemyCategory.Medium, EnemyTribes.Pirates, 10,
            13, 15, 0.6f, 1, 1, 137, 61,
            1),
    Bomba(200, 0, true,
            AudioEnums.Large_Ship_Destroyed, "Enemy Bomba",
            ImageEnums.Bomba, ImageEnums.Explosion2, 55, EnemyCategory.Medium, EnemyTribes.Pirates, 20,
            13, 15, 0.4f, 1, 1, 140, 75,
            2),
    Needler(50, 0, false,
            AudioEnums.Alien_Spaceship_Destroyed, "Enemy Needler",
            ImageEnums.Needler, ImageEnums.Explosion2, 30, EnemyCategory.Small, EnemyTribes.Pirates, 10,
            3f, 3, 0.75f, 2, 0.85f, 76, 40,
            0),

    CashCarrier(200, 0, false,
            AudioEnums.Alien_Spaceship_Destroyed, "Cash Carrier",
            ImageEnums.CashCarrier, ImageEnums.Explosion2, 0, EnemyCategory.Special, EnemyTribes.Pirates, 30,
            8, 25, 0, 1.25f, 0.75f, 171, 98,
            0),
    Scout(50, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Enemy Scout",
            ImageEnums.Scout, ImageEnums.Explosion2, 30, EnemyCategory.Small, EnemyTribes.Pirates, 10,
            3f, 3, 1, 1.7f, 0.9f, 65, 38,
            0),

    FourDirectionalDrone(100, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Four Directional Drone",
            ImageEnums.FourDirectionalDrone, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Pirates, 0,
            0, 0, 0, 1.75f, 0.4f, 170, 170,
            0),
    EnemyProtossScout(150, 0, true,
            AudioEnums.ProtossShipDeath, "Enemy Protoss Scout",
            ImageEnums.ProtossScout, ImageEnums.ProtossDestroyedExplosion, 0, EnemyCategory.Summon, EnemyTribes.Pirates, 0,
            0, 0, 0, 2.5f, 0.25f, 207, 116,
            0),

    EnemyProtossShuttle(200, 0, true,
            AudioEnums.ProtossShipDeath, "Enemy Protoss Shuttle",
            ImageEnums.ProtossShuttle, ImageEnums.ProtossDestroyedExplosion, 0, EnemyCategory.Summon, EnemyTribes.Pirates, 0,
            0, 0, 0, 1.5f, 0.25f, 231, 125,
            0),
    CarrierPulsingDrone(500, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Enemy Protoss Pulsing Drone",
            ImageEnums.CarrierPulsingDrone, ImageEnums.ProtossDestroyedExplosion, 0, EnemyCategory.Summon, EnemyTribes.Pirates, 0,
            0, 0, 0, 1, 0.75f, 100, 100,
            0),

    EnemyCarrierBeacon(200, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Enemy Protoss Beacon",
            ImageEnums.ProtossCarrierBeacon, ImageEnums.ProtossDestroyedExplosion, 0, EnemyCategory.Summon, EnemyTribes.Pirates, 0,
            0, 0, 0, 1, 0.6f, 100, 100,
            0),

    RedBoss(3000, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Red Boss",
            ImageEnums.RedBoss, ImageEnums.Explosion2, 0, EnemyCategory.Boss, EnemyTribes.Pirates, 20,
            1250, 500, 0, 1.25f, 0.65f, 861, 641,
            0),

    SpaceStationBoss(3250, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Space Station Boss",
            ImageEnums.SpaceStationBoss, ImageEnums.Explosion2, 0, EnemyCategory.Boss, EnemyTribes.Pirates, 20,
            1750, 500, 0, 1.75f, 1, 850, 850,
            0),
    CarrierBoss(3500, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Carrier Boss",
            ImageEnums.CarrierBoss, ImageEnums.Explosion2, 0, EnemyCategory.Boss, EnemyTribes.Pirates, 20,
            1750, 500, 0, 1.25f, 0.75f, 465, 252,
            0),
    YellowBoss(3750, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Yellow Boss",
            ImageEnums.YellowBoss, ImageEnums.Explosion2, 0, EnemyCategory.Boss, EnemyTribes.Pirates, 20,
            1750, 500, 0, 1.25f, 0.75f, 843, 800,
            0),

    Shuriken(85, 0, false,
            AudioEnums.Alien_Spaceship_Destroyed, "Shuriken",
            ImageEnums.ShurikenEnemy, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Pirates, 10,
            0, 0, 0, 2.5f, 0.25f, 300, 300,
            0),

    PulsingDrone(225, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Pulsing Drone",
            ImageEnums.DestructableOrbitCenterMissile, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Pirates, 0,
            0, 0, 0, 2f, 0.3f, 160, 119,
            0),

    ZergDevourer(225, 0, true,
            AudioEnums.DevourerDeath, "Zerg Devourer",
            ImageEnums.DevourerIdle, ImageEnums.DevourerDeath, 35, EnemyCategory.Medium, EnemyTribes.Zerg, 10,
            12, 15, 0.5f, 1.3f, 1, 66, 54,
            0),

    ZergGuardian(200, 0, true,
            AudioEnums.GuardianDeath, "Zerg Guardian",
            ImageEnums.GuardianIdle, ImageEnums.GuardianDeath, 35, EnemyCategory.Medium, EnemyTribes.Zerg, 10,
            12, 15, 0.7f, 1.2f, 1, 78, 71,
            0),
    ZergQueen(250, 0, true,
            AudioEnums.QueenDeath, "Zerg Death",
            ImageEnums.QueenIdle, ImageEnums.QueenDeath, 35, EnemyCategory.Medium, EnemyTribes.Zerg, 0,
            15, 20, 0.75f, 1.5f, 1, 75, 68,
            0),
    ZergScourge(75, 0, false,
            AudioEnums.ScourgeDeath, "Zerg Scourge",
            ImageEnums.ScourgeIdle, ImageEnums.ScourgeDeath, 15, EnemyCategory.Small, EnemyTribes.Zerg, 0,
            4, 3, 0.75f, 1.75f, 1.4f, 31, 27,
            0),
    ZergMutalisk(200, 0, false,
            AudioEnums.MutaliskDeath, "Zerg Guardian",
            ImageEnums.MutaliskIdle, ImageEnums.MutaliskDeath, 10, EnemyCategory.Small, EnemyTribes.Zerg, 10,
            7.5f, 15, 0.7f, 1.75f, 1.2f, 64, 72,
            0),
    DevourerCocoon(200, 0, false,
            AudioEnums.ScourgeDeath, "Zerg Cocoon",
            ImageEnums.DevourerCocoon, ImageEnums.DevourerDeath, 0, EnemyCategory.Summon, EnemyTribes.Zerg, 0,
            3, 3, 1, 1f, 1, 95, 82,
            0),
    MutaGuardianCocoon(200, 0, false,
            AudioEnums.ScourgeDeath, "Zerg Cocoon",
            ImageEnums.GuardianMutaliskCocoon, ImageEnums.GuardianDeath, 0, EnemyCategory.Summon, EnemyTribes.Zerg, 0,
            3, 3, 1, 1f, 1, 60, 42,
            0),

    ShurikenMiniBoss(850, 0, false,
            AudioEnums.Alien_Spaceship_Destroyed, "Shuriken Mini Boss",
            ImageEnums.ShurikenMiniBoss, ImageEnums.Explosion2, 0, EnemyCategory.MiniBoss, EnemyTribes.Pirates, 25,
            75, 75, 0, 2f, 0.75f, 600, 600,
            0),

    MotherShipMiniBoss(750, 0, false,
            AudioEnums.Alien_Spaceship_Destroyed, "Mothership Mini Boss",
            ImageEnums.MothershipMiniboss, ImageEnums.Explosion2, 0, EnemyCategory.MiniBoss, EnemyTribes.Pirates, 10,
            75, 75, 0, 0.8f, 0.9f, 238, 125,
            0),
    MotherShipDrone(125, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Mothership drone",
            ImageEnums.MotherShipDrone, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Pirates, 0,
            0, 0, 0, 1.5f, 0.35f, 269, 125,
            0),

    MirageMiniBoss(550, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Mirage Mini Boss",
            ImageEnums.MirageMiniBoss, ImageEnums.Explosion2, 0, EnemyCategory.MiniBoss, EnemyTribes.Pirates, 0,
                0, 0, 0, 1.75f, 0.8f, 154, 99,
            0),

    DefenderMiniBoss(750, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Defender Mini Boss",
            ImageEnums.DefenderMiniBoss, ImageEnums.Explosion2, 0, EnemyCategory.MiniBoss, EnemyTribes.Pirates, 25,
            0, 0, 0, 1, 1, 250, 250,
            0),

    LaserbeamMiniBoss(750, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Laserbeam Mini Boss",
            ImageEnums.LaserMiniBoss, ImageEnums.Explosion2, 0, EnemyCategory.MiniBoss, EnemyTribes.Pirates, 25,
            0, 0, 0, 1, 1, 250, 250,
            0),

    LaserOriginDrone(125, 0, false,
            AudioEnums.Alien_Spaceship_Destroyed, "LaserOriginDrone",
            ImageEnums.FourDirectionalDrone, ImageEnums.Explosion2, 0, EnemyCategory.Summon, EnemyTribes.Pirates, 0,
            0, 0, 0, 0f, 0.5f, 170, 170,
            0);


    private final int baseHitPoints;
    private final int baseShieldPoints;

    private final boolean hasAttack;
    private final AudioEnums deathSound;

    private final String objectType;


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


    EnemyEnums(int baseHitPoints, int baseShieldPoints, boolean hasAttack, AudioEnums deathSound, String objectType,
               ImageEnums imageType, ImageEnums destructionType, float creditCost,
               EnemyCategory enemyCategory, EnemyTribes enemyTribe, float baseArmor, float cashMoneyWorth, float xpOnDeath,
               float weight, float movementSpeed, float defaultScale, int baseWidth, int baseHeight,
               int minimumStageLevelRequired) {
        this.enemyTribe = enemyTribe;
        this.baseHitPoints = baseHitPoints;
        this.baseShieldPoints = baseShieldPoints;
        this.hasAttack = hasAttack;
        this.deathSound = deathSound;
        this.objectType = objectType;
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

    public int getBaseShieldPoints() {
        return baseShieldPoints;
    }

    public boolean hasAttack() {
        return hasAttack;
    }

    public AudioEnums getDeathSound() {
        return deathSound;
    }

    public String getObjectType() {
        return objectType;
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

    public static EnemyEnums getRandomEnemyByCategory(EnemyCategory category) {
        List<EnemyEnums> filteredEnemies = Arrays.stream(EnemyEnums.values())
                .filter(enemy -> enemy.getEnemyCategory().equals(category))
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

    public boolean isHasAttack() {
        return hasAttack;
    }

    public EnemyTribes getEnemyTribe() {
        return enemyTribe;
    }
}