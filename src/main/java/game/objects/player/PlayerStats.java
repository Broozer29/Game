package game.objects.player;

import game.items.PlayerInventory;
import game.objects.missiles.MissileTypeEnums;
import game.objects.player.playerpresets.GunPreset;
import game.objects.player.playerpresets.SpecialGunPreset;
import VisualAndAudioData.image.ImageEnums;

public class PlayerStats {

    private static PlayerStats instance = new PlayerStats();

    private PlayerStats () {
        initDefaultSettings();
    }

    public static PlayerStats getInstance () {
        return instance;
    }

    public void resetPlayerStats () {
        initDefaultSettings();

    }

    // Preset type
    private GunPreset normalGunPreset;
    private SpecialGunPreset specialGunPreset;

    // Player attacks
    private MissileTypeEnums attackType;
    private PlayerSpecialAttackTypes specialAttackType;
    private float attackDamage;
    private float bonusAttackDamage;
    private float specialAttackDamage;
    private float attackSpeed;
    private float specialAttackSpeed;

    // Player Health
    private float maxHitPoints;
    private float maxShieldHitPoints;
    private float hitpoints;
    private float shieldHitpoints;
    private float shieldRegenDelay;

    // Movement
    private int MovementSpeed;

    // Visual aspects of player
    private ImageEnums currentExhaust;
    private ImageEnums spaceShipImage;
    private ImageEnums exhaustImage;

    // Player "homing" coordinate box
    private float homingRectangleResizeScale;
    private int homingRectangleXCoordinate;
    private int homingRectangleYCoordinate;
    private int homingRectangleWidth;
    private int homingRectangleHeight;

    // Player missile type & visuals
    private ImageEnums playerMissileType;
    private ImageEnums playerMissileImpactType;
    private float missileImpactScale;
    private float missileScale;


    private PlayerInventory playerInventory;

    public void initDefaultSettings () {
        // Health
        setHitpoints(100);
        setMaxHitPoints(100);
        setShieldHitpoints(100);
        setMaxShieldHitPoints(100);
        setShieldRegenDelay(300);

        // Movement speed
        setMovementSpeed(4);

        // Special attack
        setSpecialAttackDamage(5);
        setSpecialAttackSpeed(100);

        // HomingRectangle target size (the larger, the quicker homing missiles lose lock)
        setHomingRectangleResizeScale((float) 1.5);

        // Visuals
        setSpaceShipImage(ImageEnums.Player_Spaceship_Model_3);
        setExhaustImage(ImageEnums.Default_Player_Engine);
        playerInventory = PlayerInventory.getInstance();

        if (normalGunPreset != null) {
            normalGunPreset.loadPreset();
        }

        if (specialGunPreset != null) {
            specialGunPreset.loadPreset();
        }
    }

    public void resetForNextLevel () {
        setHitpoints(this.maxHitPoints);
        setShieldHitpoints(this.maxShieldHitPoints);
    }



    //getters and setters below

    public float getNormalAttackDamage () {
        float attackDamage = this.attackDamage + this.bonusAttackDamage;
        if (attackDamage < 1) {
            return 1;
        } else {
            return attackDamage;
        }
    }

    public void setPlayerDamage (float playerDamage) {
        this.attackDamage = playerDamage;
    }

    public float getHitpoints () {
        return hitpoints;
    }

    public void setHitpoints (float hitpoints) {
        this.hitpoints = hitpoints;
    }

    public float getShieldHitpoints () {
        return shieldHitpoints;
    }

    public void setShieldHitpoints (float shieldHitpoints) {
        this.shieldHitpoints = shieldHitpoints;
    }

    public float getAttackSpeed () {
        float currentAttackSpeed = this.attackSpeed;
        if (currentAttackSpeed < 1) {
            return 1;
        } else {
            return currentAttackSpeed;
        }
    }

    public void setAttackDamage (float attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setAttackSpeed (float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public float getShieldRegenDelay () {
        return shieldRegenDelay;
    }

    public void setShieldRegenDelay (float shieldRegenDelay) {
        this.shieldRegenDelay = shieldRegenDelay;
    }

    public float getSpecialAttackSpeed () {
        float currentSpecialAttackSpeed = this.specialAttackSpeed;
        if (currentSpecialAttackSpeed < 1) {
            return 1;
        } else {
            return currentSpecialAttackSpeed;
        }
    }

    public void setSpecialAttackSpeed (float specialAttackSpeed) {
        this.specialAttackSpeed = specialAttackSpeed;
    }

    public float getMaxHitPoints () {
        return maxHitPoints;
    }

    public void setMaxHitPoints (float maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public float getMaxShieldHitPoints () {
        return maxShieldHitPoints;
    }

    public void setMaxShieldHitPoints (float maxShieldHitPoints) {
        this.maxShieldHitPoints = maxShieldHitPoints;
    }

    public int getMovementSpeed () {
        return MovementSpeed;
    }

    public void setMovementSpeed (int movementSpeed) {
        if (movementSpeed > 0) {
            this.MovementSpeed = movementSpeed;
        }
    }

    public ImageEnums getCurrentExhaust () {
        return currentExhaust;
    }

    public void setCurrentExhaust (ImageEnums currentExhaust) {
        this.currentExhaust = currentExhaust;
    }

    public float getHomingRectangleResizeScale () {
        return homingRectangleResizeScale;
    }

    public void setHomingRectangleResizeScale (float homingRectangleResizeScale) {
        this.homingRectangleResizeScale = homingRectangleResizeScale;
    }

    public int getHomingRectangleXCoordinate () {
        return homingRectangleXCoordinate;
    }

    public void setHomingRectangleXCoordinate (int homingRectangleXCoordinate) {
        this.homingRectangleXCoordinate = homingRectangleXCoordinate;
    }

    public int getHomingRectangleYCoordinate () {
        return homingRectangleYCoordinate;
    }

    public void setHomingRectangleYCoordinate (int homingRectangleYCoordinate) {
        this.homingRectangleYCoordinate = homingRectangleYCoordinate;
    }

    public int getHomingRectangleWidth () {
        return homingRectangleWidth;
    }

    public void setHomingRectangleWidth (int homingRectangleWidth) {
        this.homingRectangleWidth = homingRectangleWidth;
    }

    public int getHomingRectangleHeight () {
        return homingRectangleHeight;
    }

    public void setHomingRectangleHeight (int homingRectangleHeight) {
        this.homingRectangleHeight = homingRectangleHeight;
    }

    public ImageEnums getSpaceShipImage () {
        return spaceShipImage;
    }

    public void setSpaceShipImage (ImageEnums spaceShipImage) {
        this.spaceShipImage = spaceShipImage;
    }

    public ImageEnums getExhaustImage () {
        return exhaustImage;
    }

    public void setExhaustImage (ImageEnums exhaustImage) {
        this.exhaustImage = exhaustImage;
    }

    public void changeHitPoints (float change) {
        this.hitpoints += change;
        if (this.hitpoints > maxHitPoints) {
            this.hitpoints = maxHitPoints;
        }
    }

    public void changeShieldHitpoints (float change) {
        this.shieldHitpoints += change;
        if (this.shieldHitpoints > maxShieldHitPoints) {
            this.shieldHitpoints = maxShieldHitPoints;
        }
    }

    public ImageEnums getPlayerMissileType () {
        return playerMissileType;
    }

    public void setPlayerMissileType (ImageEnums playerMissileType) {
        this.playerMissileType = playerMissileType;
    }

    public ImageEnums getPlayerMissileImpactType () {
        return playerMissileImpactType;
    }

    public void setPlayerMissileImpactType (ImageEnums playerMissileImpactType) {
        this.playerMissileImpactType = playerMissileImpactType;
    }

    public float getMissileImpactScale () {
        return missileImpactScale;
    }

    public void setMissileImpactScale (float missileImpactScale) {
        this.missileImpactScale = missileImpactScale;
    }

    public PlayerSpecialAttackTypes getPlayerSpecialAttackType () {
        return specialAttackType;
    }

    public void setPlayerSpecialAttackType (PlayerSpecialAttackTypes playerEMPType) {
        this.specialAttackType = playerEMPType;
    }

    public float getMissileScale () {
        return missileScale;
    }

    public void setMissileScale (float missileScale) {
        this.missileScale = missileScale;
    }

    public int getCurrentMovementSpeed () {
        return MovementSpeed;
    }


    public float getBonusAttackDamage () {
        return bonusAttackDamage;
    }

    public void addBonusAttackDamage (float bonusAttackDamage) {
        this.bonusAttackDamage += bonusAttackDamage;
    }

    public float getSpecialAttackDamage () {
        float tempspecialAttackDamage = specialAttackDamage;
        if (tempspecialAttackDamage < 0.5) {
            return (float) 0.5;
        } else {
            return tempspecialAttackDamage;
        }
    }

    public void setSpecialAttackDamage (float SpecialAttackDamage) {
        this.specialAttackDamage = SpecialAttackDamage;
    }

    public MissileTypeEnums getAttackType () {
        return attackType;
    }

    public void setAttackType (MissileTypeEnums attackType) {
        this.attackType = attackType;
    }

    public GunPreset getNormalGunPreset () {
        return normalGunPreset;
    }

    public void setNormalGunPreset (GunPreset normalGunPreset) {
        this.normalGunPreset = normalGunPreset;
    }

    public SpecialGunPreset getSpecialGunPreset () {
        return specialGunPreset;
    }

    public void setSpecialGunPreset (SpecialGunPreset specialGunPreset) {
        this.specialGunPreset = specialGunPreset;
    }

    public PlayerSpecialAttackTypes getSpecialAttackType () {
        return specialAttackType;
    }

    public float getAttackDamage () {
        return attackDamage;
    }

    public PlayerInventory getPlayerInventory () {
        return playerInventory;
    }
}