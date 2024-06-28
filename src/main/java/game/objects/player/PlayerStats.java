package game.objects.player;

import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.audio.enums.AudioEnums;
import game.objects.missiles.MissileEnums;
import VisualAndAudioData.image.ImageEnums;
import game.objects.player.spaceship.SpaceShip;
import game.util.ExperienceCalculator;
import game.util.LevelAnimationPlayer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

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
    private MissileEnums attackType;
    private PlayerSpecialAttackTypes specialAttackType;
    private float baseDamage;
    private float bonusDamageMultiplier;
    private float attackSpeed;
    private float specialAttackSpeed;
    private float attackSpeedBonus;

    // Player Health
    private float maxHitPoints;
    private float maxShieldHitPoints;
    private float shieldRegenDelay;

    // Movement
    private int MovementSpeed;

    // Visual aspects of player
    private ImageEnums currentExhaust;
    private ImageEnums spaceShipImage;
    private ImageEnums exhaustImage;


    // Player missile type & visuals
    private ImageEnums playerMissileImage;
    private ImageEnums playerMissileImpactImage;
    private float missileImpactScale;
    private float missileScale;
    private int maxSpecialAttackCharges;
    private float criticalStrikeDamageMultiplier = 2.0f;
    private float maxOverloadingShieldMultiplier = 2.0f;
    private float overloadedShieldDiminishAmount;
    private float maxShieldMultiplier = 1.0f;
    private int piercingMissilesAmount = 0;


    //Leveling system
    private int currentLevel;
    private float currentXP;
    private float xpToNextLevel;
    private int amountOfDrones = 0;
    private int maximumAmountOfDrones = 10;


    public void initDefaultSettings () {
        piercingMissilesAmount = 0;
        bonusDamageMultiplier = 1f; //Otherwhise it's damage * 0 = 0

        attackType = MissileEnums.PlayerLaserbeam;
        specialAttackType = PlayerSpecialAttackTypes.EMP;

        // Health
        setMaxHitPoints(100);
        setMaxShieldMultiplier(1.0f);
        setMaxShieldHitPoints(100);
        setShieldRegenDelay(300);
        setOverloadedShieldDiminishAmount(0.5f);

        // Movement speed
        setMovementSpeed(4);

        // Special attack
        setSpecialAttackSpeed(1);
        setMaxSpecialAttackCharges(1);

        //Modifiers/multipliers
        setCriticalStrikeDamageMultiplier(2.0f);
        setMaxOverloadingShieldMultiplier(2.0f);


        //Level
        setCurrentLevel(1);
        setCurrentXP(0);
        setXpToNextLevel(100);

        // Visuals
        setSpaceShipImage(ImageEnums.Player_Spaceship_Model_3);
        setExhaustImage(ImageEnums.Default_Player_Engine);

        loadNormalGunPreset();
        loadSpecialGunPreset();
    }

    private void loadSpecialGunPreset () {
        setPlayerSpecialAttackType(PlayerSpecialAttackTypes.EMP);
    }

    private void loadNormalGunPreset () {
        switch (attackType) {
            case PlayerLaserbeam:
                initLaserbeamPreset();
                break;
            case Rocket1:
                initRocketPreset();
                break;
            default:
                break;
        }
    }

    private void initRocketPreset () {
        setAttackSpeed(0.5f);
        setBaseDamage(40);
        setPlayerMissileImage(ImageEnums.Rocket_1);
        setPlayerMissileImpactImage(ImageEnums.Rocket_1_Explosion);
        setMissileScale(1);
        setMissileImpactScale(1);
    }


    private void initLaserbeamPreset () {
        setAttackSpeed(0.25f);
        setBaseDamage(20);
        setPlayerMissileImage(ImageEnums.Player_Laserbeam);
        setPlayerMissileImpactImage(ImageEnums.Impact_Explosion_One);
        setMissileScale(1);
        setMissileImpactScale(1);
    }

    public void addXP (float xp) {
        this.currentXP += xp;
        int loopBreaker = 0;
        while (currentXP >= xpToNextLevel) {
            increasePlayerLevel();
            loopBreaker++;
            if (loopBreaker > 10) {
                return;
            }
        }
    }

    private void increasePlayerLevel () {
        xpToNextLevel = ExperienceCalculator.getNextLevelXPRequired(xpToNextLevel);
        currentLevel++;

        baseDamage = ExperienceCalculator.getNextLevelBaseDamage(baseDamage);
        maxHitPoints = ExperienceCalculator.getNextLevelHitPoints(maxHitPoints);
        maxShieldHitPoints = ExperienceCalculator.getNextLevelShieldPoints(maxShieldHitPoints);


        SpaceShip player = PlayerManager.getInstance().getSpaceship();
        player.setMaxHitPoints(maxHitPoints);
        player.setMaxShieldPoints(maxShieldHitPoints);
        player.setCurrentHitpoints(maxHitPoints);
        player.setCurrentShieldPoints(maxShieldHitPoints);

        LevelAnimationPlayer.playLevelUpAnimation(player);
        try {
            AudioManager.getInstance().addAudio(AudioEnums.Power_Up_Acquired);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public float getNormalAttackDamage () {
        float attackDamage = this.baseDamage * this.bonusDamageMultiplier;
        if (attackDamage < 1) {
            return 1;
        } else {
            return attackDamage;
        }
    }


    public void setPlayerDamage (float playerDamage) {
        this.baseDamage = playerDamage;
    }

    public float getAttackSpeed () {
        float baseAttackSpeed = this.attackSpeed;
        float attackSpeedIncrease = this.attackSpeedBonus;

        // Calculate the new attack speed
        float newAttackSpeed = baseAttackSpeed / (1 + attackSpeedIncrease / 100);

        // Ensure the attack speed does not fall below 1
        if (newAttackSpeed < 0.05) {
            return 0.05f;
        } else {
            return newAttackSpeed;
        }
    }

    public void setBaseDamage (float baseDamage) {
        this.baseDamage = baseDamage;
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
        if (currentSpecialAttackSpeed < 0.05) {
            return 0.05f;
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
        return maxShieldHitPoints * maxShieldMultiplier;
    }

    public void setMaxShieldHitPoints (float maxShieldHitPoints) {
        this.maxShieldHitPoints = maxShieldHitPoints * maxShieldMultiplier;
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


    public ImageEnums getPlayerMissileImage () {
        return playerMissileImage;
    }

    public void setPlayerMissileImage (ImageEnums playerMissileImage) {
        this.playerMissileImage = playerMissileImage;
    }

    public ImageEnums getPlayerMissileImpactImage () {
        return playerMissileImpactImage;
    }

    public void setPlayerMissileImpactImage (ImageEnums playerMissileImpactImage) {
        this.playerMissileImpactImage = playerMissileImpactImage;
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


    public float getBonusDamageMultiplier () {
        return bonusDamageMultiplier;
    }

    //This can be used for negative AND positive modifiers
    public void modifyBonusDamageMultiplier (float bonusDamageMultiplier) {
        this.bonusDamageMultiplier += bonusDamageMultiplier;
    }

    public MissileEnums getAttackType () {
        return attackType;
    }

    public void setAttackType (MissileEnums attackType) {
        this.attackType = attackType;
    }

    public PlayerSpecialAttackTypes getSpecialAttackType () {
        return specialAttackType;
    }

    public float getBaseDamage () {
        return baseDamage;
    }

    public int getMaxSpecialAttackCharges () {
        return maxSpecialAttackCharges;
    }

    public void setMaxSpecialAttackCharges (int maxSpecialAttackCharges) {
        this.maxSpecialAttackCharges = maxSpecialAttackCharges;
    }

    public float getCriticalStrikeDamageMultiplier () {
        return criticalStrikeDamageMultiplier;
    }

    public void setCriticalStrikeDamageMultiplier (float criticalStrikeDamageMultiplier) {
        this.criticalStrikeDamageMultiplier = criticalStrikeDamageMultiplier;
    }

    public void addCriticalStrikeDamageMultiplier (float criticalStrikeDamageMultiplier) {
        this.criticalStrikeDamageMultiplier += criticalStrikeDamageMultiplier;
    }

    public float getAttackSpeedBonus () {
        return attackSpeedBonus;
    }

    public void setAttackSpeedBonus (float attackSpeedBonus) {
        this.attackSpeedBonus = attackSpeedBonus;
    }

    public void modifyAttackSpeedBonus (float attackSpeedBonus) {
        this.attackSpeedBonus += attackSpeedBonus;
    }

    public float getMaxOverloadingShieldMultiplier () {
        return maxOverloadingShieldMultiplier;
    }

    public void setMaxOverloadingShieldMultiplier (float maxOverloadingShieldMultiplier) {
        this.maxOverloadingShieldMultiplier = maxOverloadingShieldMultiplier;
    }

    public void addMaxOverloadingShieldMultiplier (float maxOverloadingShieldMultiplier) {
        this.maxOverloadingShieldMultiplier += maxOverloadingShieldMultiplier;
    }

    public int getCurrentLevel () {
        return currentLevel;
    }

    public void setCurrentLevel (int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public float getCurrentXP () {
        return currentXP;
    }

    public void setCurrentXP (float currentXP) {
        this.currentXP = currentXP;
    }

    public float getXpToNextLevel () {
        return xpToNextLevel;
    }

    public void setXpToNextLevel (float xpToNextLevel) {
        this.xpToNextLevel = xpToNextLevel;
    }

    public int getMaximumAmountOfDrones () {
        return maximumAmountOfDrones;
    }

    public void setMaximumAmountOfDrones (int maximumAmountOfDrones) {
        this.maximumAmountOfDrones = maximumAmountOfDrones;
    }

    public int getAmountOfDrones () {
        return amountOfDrones;
    }

    public void setAmountOfDrones (int amountOfDrones) {
        this.amountOfDrones = amountOfDrones;
    }

    public void addDrone (int amountOfDrones) {
        if (this.amountOfDrones + amountOfDrones < this.maximumAmountOfDrones) {
            this.amountOfDrones += amountOfDrones;
        }
    }

    public float getOverloadedShieldDiminishAmount () {
        return overloadedShieldDiminishAmount;
    }

    public void setOverloadedShieldDiminishAmount (float overloadedShieldDiminishAmount) {
        this.overloadedShieldDiminishAmount = overloadedShieldDiminishAmount;
    }

    public float getMaxShieldMultiplier () {
        return maxShieldMultiplier;
    }

    public void setMaxShieldMultiplier (float maxShieldMultiplier) {
        this.maxShieldMultiplier = maxShieldMultiplier;
    }

    public void addMaxShieldMultiplier (float maxShieldMultiplier) {
        this.maxShieldMultiplier += maxShieldMultiplier;
    }

    public int getPiercingMissilesAmount () {
        return piercingMissilesAmount;
    }

    public void setPiercingMissilesAmount (int piercingMissilesAmount) {
        this.piercingMissilesAmount = piercingMissilesAmount;
    }

    public void addPiercingMissilesAmount (int piercingMissilesAmount) {
        this.piercingMissilesAmount += piercingMissilesAmount;
    }
}