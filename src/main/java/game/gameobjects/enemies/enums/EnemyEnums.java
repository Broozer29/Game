package game.gameobjects.enemies.enums;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public enum EnemyEnums {
    Alien_Bomb(25, 0, false,
            AudioEnums.Alien_Bomb_Destroyed, "Alien Bomb",
            ImageEnums.Alien_Bomb, ImageEnums.Alien_Bomb_Explosion, 3, EnemyCategory.Summon,  0, 0,
            2, 0, 1, 1, 25, 15, 0),
    Seeker(150, 0, true,
            AudioEnums.Large_Ship_Destroyed, "Enemy Seeker",
            ImageEnums.Seeker, ImageEnums.Explosion2, 25, EnemyCategory.Mercenary,  10, 1,
            4, 0.7f, 1.3f, 1, 75, 29, 0),
    Tazer(150, 0, true,
            AudioEnums.Large_Ship_Destroyed, "Enemy Tazer",
            ImageEnums.Tazer, ImageEnums.Explosion2, 25, EnemyCategory.Mercenary,  10, 1,
            4, 0.6f, 1, 1, 94, 34, 1),
    Energizer(150, 0, true,
            AudioEnums.Large_Ship_Destroyed, "Enemy Energizer",
            ImageEnums.Energizer, ImageEnums.Explosion2, 25, EnemyCategory.Mercenary,  10, 1,
            4, 0.8f, 1, 1, 106, 39, 1),
    Bulldozer(200, 0, false,
            AudioEnums.Large_Ship_Destroyed, "Enemy Bulldozer",
            ImageEnums.Bulldozer, ImageEnums.Explosion2, 25, EnemyCategory.Mercenary,  20, 2,
            6, 0.8f, 1.3f, 1, 119, 46, 0),
    Flamer(150, 0, true,
            AudioEnums.Large_Ship_Destroyed, "Enemy Flamer",
            ImageEnums.Flamer, ImageEnums.Explosion2, 25, EnemyCategory.Mercenary,  10, 2,
            6, 0.8f, 1, 1, 137, 61, 0),
    Bomba(200, 0, true,
            AudioEnums.Large_Ship_Destroyed, "Enemy Bomba",
            ImageEnums.Bomba, ImageEnums.Explosion2, 40, EnemyCategory.Mercenary,  20, 2,
            8, 0.6f, 1, 1, 140, 75, 1),
    Needler(50, 0, false,
            AudioEnums.Alien_Spaceship_Destroyed, "Enemy Needler",
            ImageEnums.Needler, ImageEnums.Explosion2, 20, EnemyCategory.Basic, 10, 1,
            3, 0.75f, 2, 0.7f, 76, 40, 0),

    CashCarrier(200, 0, false,
            AudioEnums.Alien_Spaceship_Destroyed, "Cash Carrier",
            ImageEnums.CashCarrier, ImageEnums.Explosion2, 0, EnemyCategory.Special, 40, 5,
            35, 0, 1.3f, 0.5f, 171, 98, 0),
    Scout(50, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Enemy Scout",
            ImageEnums.Scout, ImageEnums.Explosion2, 20, EnemyCategory.Basic,  10, 2,
            3, 1, 1.7f, 0.8f, 65, 38, 0),

    FourDirectionalDrone(200, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Four Directional Drone",
            ImageEnums.FourDirectionalDrone, ImageEnums.Explosion2, 0, EnemyCategory.Summon,  0, 0,
            0, 0, 0f, 0.6f, 170, 170, 0),
    RedBoss(200, 0, true,
            AudioEnums.Alien_Spaceship_Destroyed, "Red Boss",
            ImageEnums.RedBoss, ImageEnums.Explosion2, 0, EnemyCategory.Boss,  20, 150,
            100, 0, 1.25f, 0.65f, 861, 641, 0);
    ;

    private final int baseHitPoints;
    private final int baseShieldPoints;

    private final boolean hasAttack;
    private final AudioEnums deathSound;

    private final String objectType;


    private final ImageEnums imageType;

    private final ImageEnums destructionType;
    private final float creditCost;
    private EnemyCategory enemyCategory;

    private float baseArmor;
    private float cashMoneyWorth;
    private float xpOnDeath;
    private float weight;
    private float movementSpeed;
    private float defaultScale;

    private int baseWidth;
    private int baseHeight;
    private int minimumStageLevelRequired;

    EnemyEnums (int baseHitPoints, int baseShieldPoints, boolean hasAttack, AudioEnums deathSound, String objectType,
                ImageEnums imageType, ImageEnums destructionType, float creditCost,
                EnemyCategory enemyCategory, float baseArmor, float cashMoneyWorth, float xpOnDeath,
                float weight, float movementSpeed, float defaultScale, int baseWidth, int baseHeight,
                int minimumStageLevelRequired) {
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


    public static EnemyEnums getRandomEnemy () {
        EnemyEnums[] enums = EnemyEnums.values();
        Random random = new Random();
        EnemyEnums randomValue = enums[random.nextInt(enums.length)];

        if (randomValue == EnemyEnums.Alien_Bomb) {
            return getRandomEnemy();
        }
        return randomValue;
    }

    public int getBaseHitPoints () {
        return baseHitPoints;
    }

    public int getBaseShieldPoints () {
        return baseShieldPoints;
    }

    public boolean isHasAttack () {
        return hasAttack;
    }

    public AudioEnums getDeathSound () {
        return deathSound;
    }

    public String getObjectType () {
        return objectType;
    }

    public ImageEnums getImageEnum () {
        return imageType;
    }

    public ImageEnums getDestructionImageEnum () {
        return destructionType;
    }

    public float getCreditCost () {
        return creditCost;
    }

    public EnemyCategory getEnemyCategory () {
        return enemyCategory;
    }

    public float getBaseArmor () {
        return baseArmor;
    }


    public ImageEnums getImageType () {
        return imageType;
    }

    public float getCashMoneyWorth () {
        return cashMoneyWorth;
    }

    public float getXpOnDeath () {
        return xpOnDeath;
    }

    public ImageEnums getDestructionType () {
        return destructionType;
    }

    public float getWeight () {
        return weight;
    }

    public float getMovementSpeed () {
        return movementSpeed;
    }

    public float getDefaultScale () {
        return defaultScale;
    }

    public int getBaseHeight () {
        return baseHeight;
    }

    public int getBaseWidth () {
        return baseWidth;
    }

    public static EnemyEnums getRandomEnemy (EnemyCategory category) {
        List<EnemyEnums> filteredEnemies = Arrays.stream(EnemyEnums.values())
                .filter(enemy -> enemy.getEnemyCategory().equals(category))
                .collect(Collectors.toList());
        if (filteredEnemies.isEmpty()) {
            return EnemyEnums.Scout;
        }

        return filteredEnemies.get(new Random().nextInt(filteredEnemies.size()));
    }

    public int getMinimumStageLevelRequired () {
        return minimumStageLevelRequired;
    }
}