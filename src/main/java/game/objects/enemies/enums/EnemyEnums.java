package game.objects.enemies.enums;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;

import java.util.Random;


public enum EnemyEnums {
    Alien_Bomb(10, 10, 25, 0, false,
            false, AudioEnums.Alien_Bomb_Destroyed, "Alien Bomb", 0,
            ImageEnums.Alien_Bomb, ImageEnums.Alien_Bomb_Explosion, 3, EnemyCategory.Basic, false, 0, 1,
            2),
    Seeker(50, 50, 50, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Seeker", 300,
            ImageEnums.Seeker, ImageEnums.Seeker_Destroyed_Explosion, 8, EnemyCategory.Basic, false, 10, 2,
            4),
    Tazer(50, 50, 50, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Tazer", 450,
            ImageEnums.Tazer, ImageEnums.Tazer_Destroyed_Explosion, 8, EnemyCategory.Basic, false, 10, 2,
            4),
    Energizer(50, 50, 50, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Energizer", 300,
            ImageEnums.Energizer, ImageEnums.Energizer_Destroyed_Explosion, 8, EnemyCategory.Basic, false, 10, 2,
            4),
    Bulldozer(50, 50, 75, 0, false,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Bulldozer", 0,
            ImageEnums.Bulldozer, ImageEnums.Bulldozer_Destroyed_Explosion, 12, EnemyCategory.Basic, false, 20, 3,
            6),
    Flamer(50, 50, 100, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Flamer", 450,
            ImageEnums.Flamer, ImageEnums.Flamer_Destroyed_Explosion, 8, EnemyCategory.Basic, false, 10, 3,
            6),
    Bomba(50, 50, 125, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Bomba", 350,
            ImageEnums.Bomba, ImageEnums.Bomba_Destroyed_Explosion, 16, EnemyCategory.Basic, false, 20, 4,
            8);
    private final int width;
    private final int height;

    private final int baseHitPoints;
    private final int baseShieldPoints;

    private final boolean hasAttack;
    private final boolean showHealthBar;
    private final AudioEnums deathSound;

    private final String objectType;

    private final int attackSpeed;

    private final ImageEnums imageType;

    private final ImageEnums destructionType;
    private final float creditCost;
    private EnemyCategory enemyCategory;

    private boolean boxCollision;
    private float baseArmor;
    private float cashMoneyWorth;
    private float xpOnDeath;

    EnemyEnums (int width, int height, int baseHitPoints, int baseShieldPoints, boolean hasAttack, boolean showHealthBar, AudioEnums deathSound, String objectType, int attackSpeed
            , ImageEnums imageType, ImageEnums destructionType, float creditCost, EnemyCategory enemyCategory, boolean boxCollision,
                float baseArmor, float cashMoneyWorth, float xpOnDeath) {
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

    public int getAttackSpeed () {
        return attackSpeed;
    }

    public ImageEnums getImageEnum () {
        return imageType;
    }

    public ImageEnums getDestructionType () {
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
}