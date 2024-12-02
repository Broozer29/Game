package net.riezebos.bruus.tbd.game.gameobjects.player;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.movement.PathFinderEnums;
import net.riezebos.bruus.tbd.game.util.ExperienceCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;

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

    private float specialBaseDamage;
    private float specialBonusDamageMultiplier;
    private float attackSpeed;
    private float specialAttackSpeed;
    private float attackSpeedBonus;

    // Player Health
    private float maxHitPoints;
    private float maxShieldHitPoints;
    private float shieldRegenDelay;

    // Movement
    private float movementSpeed;
    private float movementSpeedModifier;

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
    private float thornsDamageRatio = 0.0f;


    //Leveling system
    private int currentLevel;
    private float currentXP;
    private float xpToNextLevel;
    private int amountOfDrones = 0;
    private int maximumAmountOfDrones = 10;

    //Willekeurig:
    private int shopRerollDiscount = 0;
    private PathFinderEnums dronePathFinder = PathFinderEnums.Regular;
    private float droneDamageBonusRatio = 0;
    private float droneDamageRatio = 1;
    private boolean hasImprovedElectroShred = false;
    private boolean attacksApplyThorns = false;
    private float chanceForThornsToApplyOnHitEffects = 0;
    private float thornsArmorDamageBonusRatio = 0;
    private float knockBackDamping;

    public void initDefaultSettings () {
        piercingMissilesAmount = 0;
        bonusDamageMultiplier = 1f; //Otherwhise it's damage * 0 = 0

        attackType = MissileEnums.PlayerLaserbeam;
        specialAttackType = PlayerSpecialAttackTypes.EMP;
        attacksApplyThorns = false;
        chanceForThornsToApplyOnHitEffects = 0;
        hasImprovedElectroShred = false;

        thornsDamageRatio = 0;
        thornsArmorDamageBonusRatio = 0;
        droneDamageRatio = 1;
        shopRerollDiscount = 0;
        dronePathFinder = PathFinderEnums.Regular;
        amountOfDrones = 0;


        setKnockBackDamping(0.85f);
        setThornsDamageRatio(0);
        setDroneDamageRatio(1);
        setDroneDamageBonusRatio(0);
        setShopRerollDiscount(0);
        dronePathFinder = PathFinderEnums.Regular;

        // Health
        setMaxHitPoints(50);
        setMaxShieldMultiplier(1.0f);
        setMaxShieldHitPoints(50);
        setShieldRegenDelay(300);
        setOverloadedShieldDiminishAmount(0.5f);

        // Movement speed
        setMovementSpeed(4);
        movementSpeedModifier = 1.0f;

        // Special attack
        setSpecialAttackSpeed(3f);
        setSpecialBonusDamageMultiplier(1); //Otherwhise it's damage * 0 = 0
        setMaxSpecialAttackCharges(1);

        //Modifiers/multipliers
        setCriticalStrikeDamageMultiplier(2.0f);
        setMaxOverloadingShieldMultiplier(2.0f);


        //Level
        setCurrentLevel(1);
        setCurrentXP(0);
        setXpToNextLevel(125);

        // Visuals
        setSpaceShipImage(ImageEnums.Player_Spaceship_Model_3);
        setExhaustImage(ImageEnums.Default_Player_Engine);

        loadNormalGunPreset();
        loadSpecialGunPreset();
    }

    private void loadSpecialGunPreset () {
        setPlayerSpecialAttackType(PlayerSpecialAttackTypes.EMP);
        setSpecialBaseDamage(baseDamage * 0.1f);
        setHasImprovedElectroShred(false);
    }

    private void loadNormalGunPreset () {
        switch (attackType) {
            case PlayerLaserbeam:
                initLaserbeamPreset();
                break;
            case DefaultRocket:
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
        setAttackSpeed(0.28f);
        setBaseDamage(20);
        setPlayerMissileImage(this.attackType.getImageType());
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

        if(player.getCurrentShieldPoints() < player.getMaxShieldPoints()) { //We don't want to overwrite any overloaded shields present
            player.setCurrentShieldPoints(maxShieldHitPoints);
        }

        AnimationManager.getInstance().playLevelUpAnimation(player);
        AudioManager.getInstance().addAudio(AudioEnums.Power_Up_Acquired);
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

    public float getSpecialAttackCooldown () {
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


    public void setMovementSpeed (float movementSpeed) {
        if (movementSpeed > 0) {
            this.movementSpeed = movementSpeed;
        }
    }

    public void modifyMovementSpeedModifier (float modifier) {
        this.movementSpeedModifier += modifier;
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

    public float getMovementSpeed () {
        float moveSpeed = this.movementSpeed * this.movementSpeedModifier;
        if (moveSpeed < 0.25f) {
            return 0.25f;
        } else {
            return moveSpeed;
        }
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

    public int getShopRerollDiscount () {
        return shopRerollDiscount;
    }

    public void setShopRerollDiscount (int shopRerollDiscount) {
        this.shopRerollDiscount = shopRerollDiscount;
    }

    public void addDroneBonusDamage (float damageBonus) {
        this.droneDamageBonusRatio += damageBonus;
    }

    public void setDroneStraightLinePathFinder (PathFinderEnums pathFinderEnums) {
        this.dronePathFinder = pathFinderEnums;
    }

    public PathFinderEnums getDronePathFinder () {
        return dronePathFinder;
    }

    public void setDronePathFinder (PathFinderEnums dronePathFinder) {
        this.dronePathFinder = dronePathFinder;
    }

    public float getDroneDamageBonusRatio () {
        return droneDamageBonusRatio;
    }

    public void setDroneDamageBonusRatio (float droneDamageBonusRatio) {
        this.droneDamageBonusRatio = droneDamageBonusRatio;
    }

    public float getDroneDamageRatio () {
        return droneDamageRatio + droneDamageBonusRatio;
    }

    public void setDroneDamageRatio (float droneDamageRatio) {
        this.droneDamageRatio = droneDamageRatio;
    }

    public boolean isHasImprovedElectroShred () {
        return hasImprovedElectroShred;
    }

    public void setHasImprovedElectroShred (boolean hasImprovedElectroShred) {
        this.hasImprovedElectroShred = hasImprovedElectroShred;
    }

    public float getSpecialBaseDamage () {
        return specialBaseDamage;
    }

    public void setSpecialBaseDamage (float specialBaseDamage) {
        this.specialBaseDamage = specialBaseDamage;
    }

    public float getSpecialDamage () {
        return this.specialBaseDamage * this.specialBonusDamageMultiplier;
    }


    public float getSpecialBonusDamageMultiplier () {
        return specialBonusDamageMultiplier;
    }

    public void setSpecialBonusDamageMultiplier (float specialBonusDamageMultiplier) {
        this.specialBonusDamageMultiplier = specialBonusDamageMultiplier;
    }

    public void modifySpecialBonusDamageMultiplier (float specialBonusDamageMultiplier) {
        this.specialBonusDamageMultiplier += specialBonusDamageMultiplier;
    }

    public float getThornsDamageRatio () {
        return thornsDamageRatio;
    }

    public void setThornsDamageRatio (float thornsDamageRatio) {
        this.thornsDamageRatio = thornsDamageRatio;
    }

    public void modifyThornsDamageRatio (float thornsDamageRatio) {
        this.thornsDamageRatio += thornsDamageRatio;
    }

    public float getThornsDamage () {
        float thornsDamage = 0;
        if (thornsDamageRatio > 0) {
            thornsDamage = baseDamage * thornsDamageRatio;
        }
        if (thornsArmorDamageBonusRatio > 0) {
            GameObject spaceShip = PlayerManager.getInstance().getSpaceship();
            thornsDamage += thornsArmorDamageBonusRatio * (spaceShip.getBaseArmor() + spaceShip.getArmorBonus());
        }

        return thornsDamage;
    }

    public boolean isAttacksApplyThorns () {
        return attacksApplyThorns;
    }

    public void setAttacksApplyThorns (boolean attacksApplyThorns) {
        this.attacksApplyThorns = attacksApplyThorns;
    }

    public float getChanceForThornsToApplyOnHitEffects () {
        return chanceForThornsToApplyOnHitEffects;
    }

    public void setChanceForThornsToApplyOnHitEffects (float chanceForThornsToApplyOnHitEffects) {
        this.chanceForThornsToApplyOnHitEffects = chanceForThornsToApplyOnHitEffects;
    }

    public float getThornsArmorDamageBonusRatio () {
        return thornsArmorDamageBonusRatio;
    }

    public void setThornsArmorDamageBonusRatio (float thornsArmorDamageBonusRatio) {
        this.thornsArmorDamageBonusRatio = thornsArmorDamageBonusRatio;
    }

    public float getKnockBackDamping () {
        return knockBackDamping;
    }

    public void setKnockBackDamping (float knockBackDamping) {
        this.knockBackDamping = knockBackDamping;
    }
}