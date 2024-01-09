package game.objects.enemies;

import VisualAndAudioData.audio.AudioEnums;
import VisualAndAudioData.image.ImageEnums;

import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public enum EnemyEnums {
    Alien_Bomb(50, 50, 25, 0, false,
            false, AudioEnums.Alien_Bomb_Destroyed, "Alien Bomb", 0,
            ImageEnums.Alien_Bomb, ImageEnums.Alien_Bomb_Explosion),
    Seeker(50, 50, 50, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Seeker", 100,
            ImageEnums.Seeker, ImageEnums.Seeker_Destroyed_Explosion),
    Tazer(50, 50, 50, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Tazer", 120,
            ImageEnums.Tazer, ImageEnums.Tazer_Destroyed_Explosion),
    Energizer(50, 50, 50, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Energizer", 100,
            ImageEnums.Energizer, ImageEnums.Energizer_Destroyed_Explosion),
    Bulldozer(50, 50, 75, 0, false,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Bulldozer", 0,
            ImageEnums.Bulldozer, ImageEnums.Bulldozer_Destroyed_Explosion),
    Flamer(50, 50, 100, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Flamer", 150,
            ImageEnums.Flamer, ImageEnums.Flamer_Destroyed_Explosion),
    Bomba(50, 50, 125, 0, true,
            false, AudioEnums.Large_Ship_Destroyed, "Enemy Bomba", 200,
            ImageEnums.Bomba, ImageEnums.Bomba_Destroyed_Explosion);
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

    EnemyEnums (int width, int height, int baseHitPoints, int baseShieldPoints, boolean hasAttack, boolean showHealthBar, AudioEnums deathSound, String objectType, int attackSpeed
    ,ImageEnums imageType, ImageEnums destructionType) {
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
    }

    //Returns the distance that enemies should have from each other for spawning formations
    public int getFormationWidthDistance () {
        return width;
    }

    public int getFormationHeightDistance () {
        return height;
    }

    public static EnemyEnums getRandomEnemy () {
        List<EnemyEnums> values = Arrays.stream(EnemyEnums.values())
                .collect(Collectors.toList());
        Random random = new Random();
        return values.get(random.nextInt(values.size()));
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
}