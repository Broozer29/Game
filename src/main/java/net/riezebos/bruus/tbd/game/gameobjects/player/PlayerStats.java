package net.riezebos.bruus.tbd.game.gameobjects.player;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.defensive.ThickHide;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.util.ExperienceCalculator;
import net.riezebos.bruus.tbd.game.gamestate.save.SaveFile;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;

import java.util.Map;

public class PlayerStats {

    private static PlayerStats instance = new PlayerStats();

    private PlayerStats() {
        initDefaultSettings();
    }

    public static PlayerStats getInstance() {
        return instance;
    }

    public void resetPlayerStats() {
        initDefaultSettings();
    }

    //firefighter
    public static float fireFighterBaseDamage = 10f;
    public static float fireFighterAttackSpeed = 0.32f;
    public static float igniteDamageMultiplier = 0.024f;
    public static float igniteDuration = 1.35f;
    private float igniteDurationMultiplier = 1;
    private float bonusIgniteDamageMultiplier = 1f;
    private float fuelCannisterMultiplier = 1;
    private float fuelCannisterRegenMultiplier = 1;
    private int fireFighterIgniteMaxStacks = 1;
    public static int fireFighterHitpoints = 75;

    //captain
    public static float captainBaseDamage = 10f;
    public static float captainAttackSpeed = 0.28f;
    public static int captainBaseHitpoints = 50;

    //carrier
    public static float carrierBaseDamage = 10f;
    public static float carrierAttackSpeed = 1;
    public static float carrierBaseArmor = 10;
    private float arbiterHealingMultiplier = 1f;
    private int amountOfProtossScouts = 0;
    private int amountOfProtossArbiters = 0;
    private int amountOfProtossShuttles = 0;
    private int maxAmountOfProtoss = 12;
    private int carrierStartingScouts = 5;
    private float protossShipBaseHealth = 45;
    private float protossShipBaseArmor = 10;
    public static float carrierSlowSpeed = 2.5f;
    public static float carrierFastSpeed = 4f;
    private float protossShipThornsDamageRatio = 1.0f;
    public static int carrierHitpoints = 100;

    //all classes
    public static float droneBaseDamage = 20f;
    private boolean hasThornsEnabled = false;
    private float thornsDamageRatio = 1.0f;
    private float baseArmor = 0;

    // Preset type
    private PlayerClass playerClass;
    private PlayerPrimaryAttackTypes attackType;
    private PlayerSpecialAttackTypes specialAttackType;
    private DroneTypes droneType;
    private float baseDamage;
    private float bonusDamageMultiplier;

    private float specialBaseDamage;
    private float specialBonusDamageMultiplier;
    private float attackSpeed;
    private float specialAttackRechargeCooldown;
    private float attackSpeedBonus;

    // Player Health
    private float maxHitPoints;
    private float maxShieldHitPoints;
    private float shieldRegenDelay;
    private float shieldRegenMultiplier;
    private boolean continueShieldRegenThroughDamage;

    // Movement
    private float movementSpeed;
    private float movementSpeedModifier;

    // Visual aspects of player
    private ImageEnums spaceShipImage;


    // Player missile type & visuals
    private ImageEnums playerMissileImage;
    private ImageEnums playerMissileImpactImage;
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
    private int maximumAmountOfDrones = 8;
    private int droneOrbitRadius = 85;

    //Willekeurig:
    private int shopRerollDiscount = 0;
    private float droneDamageBonusRatio = 0;
    private float droneDamageRatio = 1;
    private boolean hasImprovedElectroShred = false;
    private float chanceForThornsToApplyOnHitEffects = 0;
    private float thornsArmorDamageBonusRatio = 1;
    private float knockBackDamping;
    private int amountOfFreeRerolls = 0;
    private float collisionDamageReduction = 0;
    private float shieldRegenPerTick = 0.2f;
    private float mineralModifier = 1.0f;
    private int relicChanceModifier = 0;

    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
        if (playerClass.equals(PlayerClass.Captain)) {
            attackType = PlayerPrimaryAttackTypes.Laserbeam;
            specialAttackType = PlayerSpecialAttackTypes.EMP;
            spaceShipImage = ImageEnums.Player_Spaceship_Model_3;
        } else if (playerClass.equals(PlayerClass.FireFighter)) {
            attackType = PlayerPrimaryAttackTypes.Flamethrower;
            specialAttackType = PlayerSpecialAttackTypes.FlameShield;
            spaceShipImage = ImageEnums.FireFighter;
        } else if (playerClass.equals(PlayerClass.Carrier)) {
            attackType = PlayerPrimaryAttackTypes.Carrier;
            specialAttackType = PlayerSpecialAttackTypes.PlaceCarrierDrone;
            spaceShipImage = ImageEnums.ProtossCarrier;
        }

        loadNormalGunPreset();
        loadSpecialGunPreset();
    }

    public void initDefaultSettings() {
        if (playerClass == null) { //the class is remembered between runs because of this check, meaning it should only fire on startup
            setPlayerClass(PlayerClass.Captain);
        }
        amountOfFreeRerolls = 0;
        piercingMissilesAmount = 0;
        bonusDamageMultiplier = 1f; //Otherwhise it's damage * 0 = 0

        shieldRegenMultiplier = 1;
        chanceForThornsToApplyOnHitEffects = 0;
        hasImprovedElectroShred = false;
        continueShieldRegenThroughDamage = false;
        thornsDamageRatio = 1;
        thornsArmorDamageBonusRatio = 0;
        collisionDamageReduction = 0;
        shieldRegenPerTick = 0.2f;
        droneType = DroneTypes.Missile;
        droneBaseDamage = 20f;
        mineralModifier = 1.0f;
        relicChanceModifier = 0;
        this.maxAmountOfProtoss = 12;
        this.amountOfProtossScouts = carrierStartingScouts;
        this.amountOfProtossArbiters = 0;
        this.amountOfProtossShuttles = 0;
        this.hasThornsEnabled = false;


        setKnockBackDamping(0.85f);
        mineralModifier = 1;
        amountOfDrones = 0;
        maximumAmountOfDrones = 8;
        droneOrbitRadius = 85;
        setDroneDamageRatio(1);
        setDroneDamageBonusRatio(0);

        //Hack for free rerolls
        setShopRerollDiscount(0);
        if (DevTestSettings.freeReroll) {
            setShopRerollDiscount(99);
        }


        // Health
        setMaxHitPoints(captainBaseHitpoints);
        setMaxShieldMultiplier(1.0f);
        setMaxShieldHitPoints(captainBaseHitpoints);
        setShieldRegenDelay(300);
        setOverloadedShieldDiminishAmount(0.1f);

        // Movement speed
        movementSpeed = 4;
        movementSpeedModifier = 1.0f;

        //normal attack
        this.attackSpeedBonus = 0;

        // Special attack
        setSpecialBonusDamageMultiplier(1); //Otherwhise it's damage * 0 = 0
        setMaxSpecialAttackCharges(1);

        //Modifiers/multipliers
        setCriticalStrikeDamageMultiplier(2.0f);
        setMaxOverloadingShieldMultiplier(2.0f);


        //Level
        setCurrentLevel(1);
        setCurrentXP(0);
        setXpToNextLevel(200);

        // Visuals
        loadNormalGunPreset();
        loadSpecialGunPreset();
    }

    private void loadSpecialGunPreset() {
        switch (specialAttackType) {
            case EMP:
                loadEMPPreset();
                break;
            case FlameShield:
                loadFlameShieldPreset();
                break;
            case PlaceCarrierDrone:
                initCarrierSpecial();
                break;
        }
    }

    private void initCarrierSpecial() {
        setSpecialBaseDamage(0);
        setSpecialAttackRechargeCooldown(1.35f);
    }

    private void loadFlameShieldPreset() {
        setSpecialBaseDamage(baseDamage);
        setSpecialAttackRechargeCooldown(10f);
        bonusIgniteDamageMultiplier = 1f;
    }

    private void loadEMPPreset() {
        setSpecialBaseDamage(baseDamage * 1.5f);
        setHasImprovedElectroShred(false);
        setSpecialAttackRechargeCooldown(3f);
    }

    private void loadNormalGunPreset() {
        switch (attackType) {
            case Laserbeam:
                initLaserbeamPreset();
                break;
            case Flamethrower:
                initFireFighterPreset();
                break;
            case Carrier:
                initCarrierPreset();
                break;
        }
    }

    private void initCarrierPreset() {
        setAttackSpeed(carrierAttackSpeed);
        setBaseDamage(carrierBaseDamage);
        this.maxHitPoints = carrierHitpoints;
        this.maxShieldHitPoints = carrierHitpoints;
        this.amountOfProtossScouts = carrierStartingScouts;
        this.amountOfProtossArbiters = 0;
        this.amountOfProtossShuttles = 0;
        this.maxAmountOfProtoss = 12;
        this.movementSpeed = 2.5f;
        this.protossShipBaseHealth = 45;
        this.protossShipBaseArmor = 10;
        this.arbiterHealingMultiplier = 1f;
        this.protossShipThornsDamageRatio = 1.0f;
        setKnockBackDamping(0.775f);
    }

    private void initFireFighterPreset() {
        setAttackSpeed(fireFighterAttackSpeed);
        setBaseDamage(fireFighterBaseDamage);
        this.attackType = PlayerPrimaryAttackTypes.Flamethrower;
        setPlayerMissileImage(ImageEnums.FireFighterFlameThrowerLooping);
        setPlayerMissileImpactImage(null);
        setMissileScale(1);
        setFuelCannisterMultiplier(1);
        setFuelCannisterRegenMultiplier(1);
        igniteDuration = 1.5f;
        fireFighterIgniteMaxStacks = 1;
        this.maxHitPoints = fireFighterHitpoints;
        this.maxShieldHitPoints = fireFighterHitpoints;
    }


    private void initLaserbeamPreset() {
        setAttackSpeed(captainAttackSpeed);
        setBaseDamage(captainBaseDamage);
        this.attackType = PlayerPrimaryAttackTypes.Laserbeam;
        setPlayerMissileImage(this.attackType.getCorrespondingMissileEnum().getImageType());
        setPlayerMissileImpactImage(ImageEnums.Impact_Explosion_One);
        this.maxHitPoints = captainBaseHitpoints;
        this.maxShieldHitPoints = captainBaseHitpoints;
    }

    public void addXP(float xp) {
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

    private void increasePlayerLevel() {
        xpToNextLevel = ExperienceCalculator.getNextLevelXPRequired(xpToNextLevel);
        currentLevel++;


        BoonManager.getInstance().activateBoons(BoonActivationEnums.LevelingUp);

        //Ik ben dizzy terwijl ik dit maak, ik kom er ff niet uit of dit juist is of niet ik doe het nu omslachtig om het zeker te weten plz help me nog anderhalf uur op de werkvloer
        boolean shouldIncreaseBaseDamage = true;
        if (BoonManager.getInstance().getDefensiveBoon() != null && BoonManager.getInstance().getDefensiveBoon().equals(BoonEnums.THICK_HIDE)) {
            shouldIncreaseBaseDamage = false;
        }

        if (shouldIncreaseBaseDamage) {
            baseDamage = ExperienceCalculator.getNextLevelBaseDamage(baseDamage);
        }

        maxHitPoints = ExperienceCalculator.getNextLevelHitPoints(maxHitPoints);
        maxShieldHitPoints = ExperienceCalculator.getNextLevelShieldPoints(maxShieldHitPoints);
        droneBaseDamage = ExperienceCalculator.getNextLevelBaseDamage(droneBaseDamage);


        SpaceShip player = PlayerManager.getInstance().getSpaceship();
        player.setMaxHitPoints(maxHitPoints);
        player.setMaxShieldPoints(maxShieldHitPoints);
        player.setCurrentHitpoints(maxHitPoints);

        if (player.getCurrentShieldPoints() < player.

                getMaxShieldPoints()) { //We don't want to overwrite any overloaded shields present
            player.setCurrentShieldPoints(maxShieldHitPoints);
        }

        AnimationManager.getInstance().

                playLevelUpAnimation(player);
        AudioManager.getInstance().

                addAudio(AudioEnums.ItemAcquired);
    }

    public float getNormalAttackDamage() {
        float attackDamage = this.baseDamage * this.bonusDamageMultiplier;
        if (attackDamage < 1) {
            return 1;
        } else {
            return attackDamage;
        }
    }


    public float getAttackSpeed() {
        float baseAttackSpeed = this.attackSpeed; // The default cooldown in milliseconds
        float attackSpeedBonus = this.attackSpeedBonus; // Total attack speed modifier applied

        if (attackSpeedBonus <= -100) { // Prevent division by zero or negative scaling
            return baseAttackSpeed * 3.0f; // Clamp to maximum slowdown
        }

        float newAttackSpeed = baseAttackSpeed / (1 + attackSpeedBonus / 100); // Adjusted calculation

        // Minimum threshold to prevent the attack speed from becoming too fast
        if (newAttackSpeed < 0.03f) {
            newAttackSpeed = 0.03f;
        }

        // Maximum threshold to prevent attack speed from being too slow
        if (newAttackSpeed > baseAttackSpeed * 3.0f) {
            newAttackSpeed = baseAttackSpeed * 3.0f;
        }

        return newAttackSpeed;
    }

    public float getIgniteTickRate() {
        float newAttackSpeed = this.getAttackSpeed() / 2;

        if (newAttackSpeed < 0.03f) {
            newAttackSpeed = 0.03f;
        }

        return newAttackSpeed;
    }


    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public float getShieldRegenDelay() {
        return shieldRegenDelay;
    }

    public void setShieldRegenDelay(float shieldRegenDelay) {
        this.shieldRegenDelay = shieldRegenDelay;
    }

    public float getSpecialAttackCooldown() {
        float currentSpecialAttackSpeed = this.specialAttackRechargeCooldown;
        if (currentSpecialAttackSpeed < 0.05) {
            return 0.05f;
        } else {
            return currentSpecialAttackSpeed;
        }
    }

    public void setSpecialAttackRechargeCooldown(float specialAttackRechargeCooldown) {
        this.specialAttackRechargeCooldown = specialAttackRechargeCooldown;
    }

    public float getMaxHitPoints() {
        return maxHitPoints;
    }

    public void setMaxHitPoints(float maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public float getMaxShieldHitPoints() {
        return maxShieldHitPoints * maxShieldMultiplier;
    }

    public float getBaseMaxShieldPoints() {
        return maxShieldHitPoints * maxShieldMultiplier;
    }

    public void setMaxShieldHitPoints(float maxShieldHitPoints) {
        this.maxShieldHitPoints = maxShieldHitPoints * maxShieldMultiplier;
    }


    public void setMovementSpeed(float movementSpeed) {
        if (movementSpeed > 0) {
            this.movementSpeed = movementSpeed;
        }
    }

    public void modifyMovementSpeedModifier(float modifier) {
        this.movementSpeedModifier += modifier;
    }


    public ImageEnums getSpaceShipImage() {
        return spaceShipImage;
    }

    public void setSpaceShipImage(ImageEnums spaceShipImage) {
        this.spaceShipImage = spaceShipImage;
    }


    public ImageEnums getPlayerMissileImage() {
        return playerMissileImage;
    }

    public void setPlayerMissileImage(ImageEnums playerMissileImage) {
        this.playerMissileImage = playerMissileImage;
    }

    public ImageEnums getPlayerMissileImpactImage() {
        return playerMissileImpactImage;
    }

    public void setPlayerMissileImpactImage(ImageEnums playerMissileImpactImage) {
        this.playerMissileImpactImage = playerMissileImpactImage;
    }


    public PlayerSpecialAttackTypes getPlayerSpecialAttackType() {
        return specialAttackType;
    }

    public void setPlayerSpecialAttackType(PlayerSpecialAttackTypes playerEMPType) {
        this.specialAttackType = playerEMPType;
    }

    public float getMissileScale() {
        return missileScale;
    }

    public void setMissileScale(float missileScale) {
        this.missileScale = missileScale;
    }

    public float getMovementSpeed() {
        float moveSpeed = this.movementSpeed * this.movementSpeedModifier;
        if (moveSpeed < 0.25f) {
            return 0.25f;
        } else {
            return moveSpeed;
        }
    }


    public float getBonusDamageMultiplier() {
        return bonusDamageMultiplier;
    }

    //This can be used for negative AND positive modifiers
    public void modifyBonusDamageMultiplier(float bonusDamageMultiplier) {
        this.bonusDamageMultiplier += bonusDamageMultiplier;
    }

    public PlayerPrimaryAttackTypes getAttackType() {
        return attackType;
    }

    public PlayerSpecialAttackTypes getSpecialAttackType() {
        return specialAttackType;
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public int getMaxSpecialAttackCharges() {
        return maxSpecialAttackCharges;
    }

    public void setMaxSpecialAttackCharges(int maxSpecialAttackCharges) {
        this.maxSpecialAttackCharges = maxSpecialAttackCharges;
    }

    public float getCriticalStrikeDamageMultiplier() {
        return criticalStrikeDamageMultiplier;
    }

    public void setCriticalStrikeDamageMultiplier(float criticalStrikeDamageMultiplier) {
        this.criticalStrikeDamageMultiplier = criticalStrikeDamageMultiplier;
    }

    public void addCriticalStrikeDamageMultiplier(float criticalStrikeDamageMultiplier) {
        this.criticalStrikeDamageMultiplier += criticalStrikeDamageMultiplier;
    }

    public float getAttackSpeedBonus() {
        return attackSpeedBonus;
    }

    public void modifyAttackSpeedBonus(float attackSpeedBonus) {
        this.attackSpeedBonus += attackSpeedBonus;
    }

    public float getMaxOverloadingShieldMultiplier() {
        return maxOverloadingShieldMultiplier;
    }

    public void setMaxOverloadingShieldMultiplier(float maxOverloadingShieldMultiplier) {
        this.maxOverloadingShieldMultiplier = maxOverloadingShieldMultiplier;
    }

    public void addMaxOverloadingShieldMultiplier(float maxOverloadingShieldMultiplier) {
        this.maxOverloadingShieldMultiplier += maxOverloadingShieldMultiplier;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public float getCurrentXP() {
        return currentXP;
    }

    public void setCurrentXP(float currentXP) {
        this.currentXP = currentXP;
    }

    public float getXpToNextLevel() {
        return xpToNextLevel;
    }

    public void setXpToNextLevel(float xpToNextLevel) {
        this.xpToNextLevel = xpToNextLevel;
    }

    public int getMaximumAmountOfDrones() {
        return maximumAmountOfDrones;
    }

    public void setMaximumAmountOfDrones(int maximumAmountOfDrones) {
        this.maximumAmountOfDrones = maximumAmountOfDrones;
    }

    public int getAmountOfDrones() {
        return amountOfDrones;
    }

    public void setAmountOfDrones(int amountOfDrones) {
        this.amountOfDrones = amountOfDrones;
    }

    public float getOverloadedShieldDiminishAmount() {
        return overloadedShieldDiminishAmount;
    }

    public void setOverloadedShieldDiminishAmount(float overloadedShieldDiminishAmount) {
        this.overloadedShieldDiminishAmount = overloadedShieldDiminishAmount;
    }

    public float getMaxShieldMultiplier() {
        return maxShieldMultiplier;
    }

    public void setMaxShieldMultiplier(float maxShieldMultiplier) {
        this.maxShieldMultiplier = maxShieldMultiplier;
    }

    public void addMaxShieldMultiplier(float maxShieldMultiplier) {
        this.maxShieldMultiplier += maxShieldMultiplier;
    }

    public int getPiercingMissilesAmount() {
        return piercingMissilesAmount;
    }

    public void setPiercingMissilesAmount(int piercingMissilesAmount) {
        this.piercingMissilesAmount = piercingMissilesAmount;
    }

    public void addPiercingMissilesAmount(int piercingMissilesAmount) {
        this.piercingMissilesAmount += piercingMissilesAmount;
    }

    public int getShopRerollDiscount() {
        return shopRerollDiscount;
    }

    public void setShopRerollDiscount(int shopRerollDiscount) {
        this.shopRerollDiscount = shopRerollDiscount;
    }

    public void addDroneBonusDamage(float damageBonus) {
        this.droneDamageBonusRatio += damageBonus;
    }

    public float getDroneDamageBonusRatio() {
        return droneDamageBonusRatio;
    }

    public void setDroneDamageBonusRatio(float droneDamageBonusRatio) {
        this.droneDamageBonusRatio = droneDamageBonusRatio;
    }

    public float getDroneDamageRatio() {
        return droneDamageRatio + droneDamageBonusRatio;
    }

    public void setDroneDamageRatio(float droneDamageRatio) {
        this.droneDamageRatio = droneDamageRatio;
    }

    public boolean isHasImprovedElectroShred() {
        return hasImprovedElectroShred;
    }

    public void setHasImprovedElectroShred(boolean hasImprovedElectroShred) {
        this.hasImprovedElectroShred = hasImprovedElectroShred;
    }

    public float getSpecialBaseDamage() {
        return specialBaseDamage;
    }

    public void setSpecialBaseDamage(float specialBaseDamage) {
        this.specialBaseDamage = specialBaseDamage;
    }

    public float getSpecialDamage() {
        return this.specialBaseDamage * this.specialBonusDamageMultiplier;
    }


    public float getSpecialBonusDamageMultiplier() {
        return specialBonusDamageMultiplier;
    }

    public void setSpecialBonusDamageMultiplier(float specialBonusDamageMultiplier) {
        this.specialBonusDamageMultiplier = specialBonusDamageMultiplier;
    }

    public void modifySpecialBonusDamageMultiplier(float specialBonusDamageMultiplier) {
        this.specialBonusDamageMultiplier += specialBonusDamageMultiplier;
    }

    public float getThornsDamageRatio() {
        return thornsDamageRatio;
    }

    public void setThornsDamageRatio(float thornsDamageRatio) {
        this.thornsDamageRatio = thornsDamageRatio;
    }

    public void modifyThornsDamageRatio(float thornsDamageRatio) {
        this.thornsDamageRatio += thornsDamageRatio;
    }

    public float getThornsDamage() {
        float thornsDamage = 0;
        if (thornsDamageRatio > 0) {
            thornsDamage = this.baseDamage * (thornsDamageRatio);
        }
        if (thornsArmorDamageBonusRatio > 1.01) {
            GameObject spaceShip = PlayerManager.getInstance().getSpaceship();
            thornsDamage += thornsArmorDamageBonusRatio * (spaceShip.getBaseArmor() + spaceShip.getArmorBonus());
        }

        return thornsDamage;
    }

    public float getChanceForThornsToApplyOnHitEffects() {
        return chanceForThornsToApplyOnHitEffects;
    }

    public void setChanceForThornsToApplyOnHitEffects(float chanceForThornsToApplyOnHitEffects) {
        this.chanceForThornsToApplyOnHitEffects = chanceForThornsToApplyOnHitEffects;
    }

    public float getThornsArmorDamageBonusRatio() {
        return thornsArmorDamageBonusRatio;
    }

    public void setThornsArmorDamageBonusRatio(float thornsArmorDamageBonusRatio) {
        this.thornsArmorDamageBonusRatio = thornsArmorDamageBonusRatio;
    }

    public float getKnockBackDamping() {
        return knockBackDamping;
    }

    public void setKnockBackDamping(float knockBackDamping) {
        this.knockBackDamping = knockBackDamping;
    }

    public int getDroneOrbitRadius() {
        return droneOrbitRadius;
    }

    public void setDroneOrbitRadius(int droneOrbitRadius) {
        this.droneOrbitRadius = droneOrbitRadius;
    }

    public void addDroneBonusOrbitRange(float orbitrangeBonus) {
        this.droneOrbitRadius += orbitrangeBonus;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public int getAmountOfFreeRerolls() {
        return amountOfFreeRerolls;
    }

    public void setAmountOfFreeRerolls(int amountOfFreeRerolls) {
        this.amountOfFreeRerolls = amountOfFreeRerolls;
    }

    public float getCollisionDamageReduction() {
        return collisionDamageReduction;
    }

    public void modifyCollisionDamageReduction(float amount) {
        this.collisionDamageReduction += amount;
    }

    public float getShieldRegenPerTick() {
        return shieldRegenPerTick * shieldRegenMultiplier;
    }

    public void modifyShieldRegenMultiplier(float amount) {
        this.shieldRegenMultiplier += amount;
    }

    public float getIgniteDuration() {
        return igniteDuration * igniteDurationMultiplier;
    }

    public void modifyDefaultIgniteDuration(float amount) {
        this.igniteDuration += amount;
    }

    public void modifyIgniteDurationModifier(float amount) {
        this.igniteDurationMultiplier += amount;
    }

    public void modifyIgniteItemDamageMultiplier(float amount) {
        this.bonusIgniteDamageMultiplier += amount;
    }

    public float getIgniteDamage() {
        return baseDamage * (igniteDamageMultiplier * bonusIgniteDamageMultiplier);
    }

    public void modifyFireFighterIgniteDamageMultiplier(float amount) {
        igniteDamageMultiplier += amount;
    }

    public void setFireFighterIgniteMaxStacks(int fireFighterIgniteMaxStacks) {
        this.fireFighterIgniteMaxStacks = fireFighterIgniteMaxStacks;
    }

    public int getFireFighterIgniteMaxStacks() {
        return fireFighterIgniteMaxStacks;
    }

    public float getDroneDamage() {
        return droneBaseDamage * droneDamageRatio;
    }

    public DroneTypes getDroneType() {
        return droneType;
    }

    public void setDroneType(DroneTypes droneType) {
        this.droneType = droneType;
    }

    public boolean isContinueShieldRegenThroughDamage() {
        return continueShieldRegenThroughDamage;
    }

    public void setContinueShieldRegenThroughDamage(boolean continueShieldRegenThroughDamage) {
        this.continueShieldRegenThroughDamage = continueShieldRegenThroughDamage;
    }

    public float getMineralModifier() {
        return mineralModifier;
    }

    public void addMineralModifier(float modifier) {
        this.mineralModifier += modifier;
    }

    public void loadInSaveFile(SaveFile saveFile) {
        PlayerInventory.getInstance().resetInventory();
        setPlayerClass(saveFile.getPlayerclass());
        PlayerStats.getInstance().resetPlayerStats();
        for (int i = 1; i < saveFile.getPlayerLevel(); i++) {
            increasePlayerLevel();
        }

        currentXP = saveFile.getPlayerXP();

        for (Map.Entry<ItemEnums, Integer> entry : saveFile.getItems().entrySet()) {
            ItemEnums itemType = entry.getKey();
            int quantity = entry.getValue();
            for (int i = 0; i < quantity; i++) {
                PlayerInventory.getInstance().addItem(itemType);
            }
        }
        PlayerInventory.getInstance().setCashMoney(saveFile.getMoney());
    }

    public int getRelicChanceModifier() {
        return relicChanceModifier;
    }

    public void modifyRelicChanceModifier(int amount) {
        this.relicChanceModifier += amount;
    }

    public float getCarrierBaseArmor() {
        return carrierBaseArmor;
    }

    public int getAmountOfProtossScouts() {
        return amountOfProtossScouts;
    }

    public void setAmountOfProtossScouts(int amountOfProtossScouts) {
        this.amountOfProtossScouts = amountOfProtossScouts;
    }

    public int getAmountOfProtossArbiters() {
        return amountOfProtossArbiters;
    }

    public void setAmountOfProtossArbiters(int amountOfProtossArbiters) {
        this.amountOfProtossArbiters = amountOfProtossArbiters;
    }

    public int getAmountOfProtossShuttles() {
        return amountOfProtossShuttles;
    }

    public void setAmountOfProtossShuttles(int amountOfProtossShuttles) {
        this.amountOfProtossShuttles = amountOfProtossShuttles;
    }

    public int getMaxAmountOfProtoss() {
        return maxAmountOfProtoss;
    }

    public void modifyMaxAmountOfProtoss(int amount) {
        this.maxAmountOfProtoss += amount;
    }

    public int getCarrierStartingScouts() {
        return carrierStartingScouts;
    }

    public void setCarrierStartingScouts(int carrierStartingScouts) {
        this.carrierStartingScouts = carrierStartingScouts;
    }

    public float getProtossShipBaseHealth() {
        return protossShipBaseHealth;
    }

    public float getProtossShipBaseArmor() {
        return protossShipBaseArmor;
    }

    public float getArbiterHealingMultiplier() {
        return arbiterHealingMultiplier;
    }

    public void modifyArbiterHealingMultiplier(float amount) {
        this.arbiterHealingMultiplier += amount;
    }

    public boolean isHasThornsEnabled() {
        return hasThornsEnabled;
    }

    public void setHasThornsEnabled(boolean hasThornsEnabled) {
        this.hasThornsEnabled = hasThornsEnabled;
    }

    public float getProtossShipThornsDamageRatio() {
        return protossShipThornsDamageRatio;
    }

    public void modifyProtossShipThornsDamageRatio(float amount) {
        this.protossShipThornsDamageRatio += amount;
    }

    public float getFuelCannisterMultiplier() {
        return fuelCannisterMultiplier;
    }

    public void setFuelCannisterMultiplier(float fuelCannisterMultiplier) {
        this.fuelCannisterMultiplier = fuelCannisterMultiplier;
    }

    public void modifyFuelCannisterMultiplier(float amount) {
        this.fuelCannisterMultiplier += amount;
    }

    public float getFuelCannisterRegenMultiplier() {
        return fuelCannisterRegenMultiplier;
    }

    public void modifyFuelCannisterRegenMultiplier(float amount) {
        this.fuelCannisterRegenMultiplier += amount;
    }

    public void setFuelCannisterRegenMultiplier(float fuelCannisterRegenMultiplier) {
        this.fuelCannisterRegenMultiplier = fuelCannisterRegenMultiplier;
    }

    public float getBaseArmor() {
        return baseArmor;
    }

    public void setBaseArmor(float baseArmor) {
        this.baseArmor = baseArmor;
    }

    public void addBaseArmor(float amount) {
        this.baseArmor += amount;
    }
}