package game.objects.enemies.enums;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;

import java.util.Random;


public enum EnemyEnums {
    Alien_Bomb(10, 10, 25, 0, false,
            false, AudioEnums.Alien_Bomb_Destroyed, "Alien Bomb", 0,
            ImageEnums.Alien_Bomb, ImageEnums.Alien_Bomb_Explosion, 3, EnemyCategory.Basic, false, 0, 0,
            2, 0),
    Seeker(50, 50, 100, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Seeker", 2,
            ImageEnums.Seeker, ImageEnums.Seeker_Destroyed_Explosion, 8, EnemyCategory.Mercenary, false, 10, 1,
            4, 0.7f),
    Tazer(50, 50, 100, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Tazer", 2.5f,
            ImageEnums.Tazer, ImageEnums.Tazer_Destroyed_Explosion, 8, EnemyCategory.Mercenary, false, 10, 1,
            4, 0.5f),
    Energizer(50, 50, 100, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Energizer", 2.5f,
            ImageEnums.Energizer, ImageEnums.Energizer_Destroyed_Explosion, 8, EnemyCategory.Mercenary, false, 10, 1,
            4, 0.5f),
    Bulldozer(50, 50, 150, 0, false,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Bulldozer", 0,
            ImageEnums.Bulldozer, ImageEnums.Bulldozer_Destroyed_Explosion, 12, EnemyCategory.Mercenary, false, 20, 2,
            6, 0.8f),
    Flamer(50, 50, 150, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Flamer", 2.5f,
            ImageEnums.Flamer, ImageEnums.Flamer_Destroyed_Explosion, 8, EnemyCategory.Mercenary, false, 10, 2,
            6,0.7f),
    Bomba(50, 50, 150, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Bomba", 4f,
            ImageEnums.Bomba, ImageEnums.Bomba_Destroyed_Explosion, 16, EnemyCategory.Mercenary, false, 20, 2,
            8, 0.5f),
    Needler(62, 40, 50, 0, false, false, AudioEnums.Alien_Spaceship_Destroyed,
            "Enemy Needler", 0f, ImageEnums.Needler, ImageEnums.Explosion2, 6, EnemyCategory.Basic,
            false, 10, 1, 3, 1),

    Scout(62, 40, 50, 0, false, false, AudioEnums.Alien_Spaceship_Destroyed,
            "Enemy Scout", 2f, ImageEnums.Scout, ImageEnums.Explosion2, 6, EnemyCategory.Basic,
            false, 10, 1, 3, 1);
    private final int width;
    private final int height;

    private final int baseHitPoints;
    private final int baseShieldPoints;

    private final boolean hasAttack;
    private final boolean showHealthBar;
    private final AudioEnums deathSound;

    private final String objectType;

    private final float attackSpeed;

    private final ImageEnums imageType;

    private final ImageEnums destructionType;
    private final float creditCost;
    private EnemyCategory enemyCategory;

    private boolean boxCollision;
    private float baseArmor;
    private float cashMoneyWorth;
    private float xpOnDeath;
    private float weight;

    EnemyEnums (int width, int height, int baseHitPoints, int baseShieldPoints, boolean hasAttack, boolean showHealthBar, AudioEnums deathSound, String objectType, float attackSpeed
            , ImageEnums imageType, ImageEnums destructionType, float creditCost, EnemyCategory enemyCategory, boolean boxCollision,
                float baseArmor, float cashMoneyWorth, float xpOnDeath, float weight) {
        this.width = width;
        this.height = height;
        this.baseHitPoints = baseHitPoints;
        this.baseShieldPoints = baseShieldPoints;
        this.hasAttack = hasAttack;
        this.showHealthBar = showHealthBar;
        this.deathSound = deathSound;
        this.objectType = objectType;
        this.attackSpeed = attackSpeed;
        this.imageType = imageType;
        this.destructionType = destructionType;
        this.creditCost = creditCost;
        this.enemyCategory = enemyCategory;
        this.boxCollision = boxCollision;
        this.baseArmor = baseArmor;
        this.cashMoneyWorth = cashMoneyWorth;
        this.xpOnDeath = xpOnDeath;
        this.weight = weight;
    }

    //Returns the distance that enemies should have from each other for spawning formations
    public int getFormationWidthDistance () {
        return width;
    }

    public int getFormationHeightDistance () {
        return height;
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

    public boolean isShowHealthBar () {
        return showHealthBar;
    }

    public AudioEnums getDeathSound () {
        return deathSound;
    }

    public String getObjectType () {
        return objectType;
    }

    public float getAttackSpeed () {
        return attackSpeed;
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

    public boolean isBoxCollision () {
        return boxCollision;
    }

    public float getBaseArmor () {
        return baseArmor;
    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
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
}