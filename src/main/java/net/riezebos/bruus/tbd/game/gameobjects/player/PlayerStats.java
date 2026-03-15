package net.riezebos.bruus.tbd.game.gameobjects.player;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.save.SaveFile;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.util.ExperienceCalculator;
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
    public static float fireFighterAttackSpeed = 0.28f;
    public static float igniteDamageMultiplier = 0.025f;
    public static float igniteDuration = 1.15f;
    private int baseMaxIgniteStacks = 1;
    public static int fireFighterHitpoints = 75;

    //captain
    public static float captainBaseDamage = 10f;
    public static float captainBaseAttackSpeed = 0.28f;
    public static int captainBaseHitpoints = 60;

    //carrier
    public static float carrierBaseDamage = 10f;
    public static float carrierBaseAttackSpeed = 1;
    public static int carrierBaseHitpoints = 75;
    public static float carrierSlowSpeed = 2.5f;
    public static float carrierFastSpeed = 4f;

    //todo deze attributen moeten eigenlijk naar spaceship, maar kunnen voor nu in playerstats blijven omdat ze (voor nu) altijd shared zijn bij alle spelers
    private float arbiterHealingMultiplier = 1f;
    private static int defaultMaxAmountOfProtoss = 14;
    private static int defaultCarrierStartingScouts = 5;
    private float protossShipBaseHealth = 55;
    private float protossShipBaseArmor = 0;
    private float protossShipThornsDamageRatio = 1.0f;


    //all classes
    public static float droneBaseDamage = 20f;
    private boolean hasThornsEnabled = false;
    private float thornsDamageRatio = 1.0f;
    public static float baseArmor = 0;
    public static float baseMoveSpeed = 4;

    // Preset type
    private PlayerClass playerClass;
    private PlayerPrimaryAttackTypes attackType;
    private PlayerSpecialAttackTypes specialAttackType;
    private float baseDamage;
    private float attackSpeed;
    private float specialAttackRechargeCooldown;

    // Player Health
    private float maxHitPoints;
    private float maxShieldHitPoints;
    private float shieldRegenDelay;

    // Visual aspects of player
    private ImageEnums spaceShipImage;

    // Player missile type & visuals
    private ImageEnums playerMissileImage;
    private ImageEnums playerMissileImpactImage;
    private float overloadedShieldDiminishAmount;
    private int piercingMissilesAmount = 0;


    //Leveling system
    private int currentLevel;
    private float currentXP;
    private float xpToNextLevel;
    private int amountOfDrones = 0;
    private int maximumAmountOfDrones = 8;

    //Willekeurig:
    private int shopRerollDiscount = 0;
    private boolean hasImprovedElectroShred = false;
    private float knockBackDamping;
    private int amountOfFreeRerolls = 0;
    private float collisionDamageReduction = 0;
    private float shieldRegenPerTick = 0.2f;
    private float mineralModifier = 1.0f;
    private int relicChanceModifier = 0;
    private float flatDamageReduction = 0;

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

        loadClassPreset();
    }

    public void initDefaultSettings() {
        if (playerClass == null) { //the class is remembered between runs because of this check, meaning it should only fire on startup
            setPlayerClass(PlayerClass.Captain);
        }
        amountOfFreeRerolls = 0;
        piercingMissilesAmount = 0;

        hasImprovedElectroShred = false;
        thornsDamageRatio = 1;
        collisionDamageReduction = 0;
        shieldRegenPerTick = 0.2f;
        droneBaseDamage = 20f;
        igniteDuration = 1.15f;
        mineralModifier = 1.0f;
        relicChanceModifier = 0;
        flatDamageReduction = 0;
        this.defaultMaxAmountOfProtoss = 12;
        this.hasThornsEnabled = false;


        setKnockBackDamping(0.85f);
        mineralModifier = 1;
        amountOfDrones = 0;
        maximumAmountOfDrones = 8;
        baseMaxIgniteStacks = 1;

        //Hack for free rerolls
        setShopRerollDiscount(0);
        if (DevTestSettings.freeReroll) {
            setShopRerollDiscount(99);
        }

        // Health
        setMaxHitPoints(captainBaseHitpoints);
        setShieldRegenDelay(300);
        setOverloadedShieldDiminishAmount(0.1f);

        //Level
        setCurrentLevel(1);
        setCurrentXP(0);
        setXpToNextLevel(200);

        loadClassPreset();
    }


    private void loadClassPreset() {
        switch (attackType) {
            case Laserbeam:
                initCaptainPreset();
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
        setAttackSpeed(carrierBaseAttackSpeed);
        setBaseDamage(DevTestSettings.instaKill ? carrierBaseDamage * 100 : carrierBaseDamage);
        this.maxHitPoints = carrierBaseHitpoints;
        this.maxShieldHitPoints = carrierBaseHitpoints;
        this.defaultMaxAmountOfProtoss = 12;
        this.protossShipBaseHealth = 45;
        this.protossShipBaseArmor = 10;
        this.arbiterHealingMultiplier = 1f;
        this.protossShipThornsDamageRatio = 1.0f;
        setKnockBackDamping(0.775f);

        //special
        setSpecialAttackRechargeCooldown(1.35f);
    }

    private void initFireFighterPreset() {
        setAttackSpeed(fireFighterAttackSpeed);
        setBaseDamage(DevTestSettings.instaKill ? fireFighterBaseDamage * 100 : fireFighterBaseDamage);
        this.attackType = PlayerPrimaryAttackTypes.Flamethrower;
        setPlayerMissileImage(ImageEnums.FireFighterFlameThrowerLooping);
        setPlayerMissileImpactImage(null);
        igniteDuration = 1.75f;
        baseMaxIgniteStacks = 2;
        this.maxHitPoints = fireFighterHitpoints;
        this.maxShieldHitPoints = fireFighterHitpoints;

        //special
        setSpecialAttackRechargeCooldown(10f);
    }


    private void initCaptainPreset() {
        setAttackSpeed(captainBaseAttackSpeed);
        setBaseDamage(DevTestSettings.instaKill ? captainBaseDamage * 100 : captainBaseDamage);
        this.attackType = PlayerPrimaryAttackTypes.Laserbeam;
        setPlayerMissileImage(this.attackType.getCorrespondingMissileEnum().getImageType());
        setPlayerMissileImpactImage(ImageEnums.Impact_Explosion_One);
        this.maxHitPoints = captainBaseHitpoints;
        this.maxShieldHitPoints = captainBaseHitpoints;

        //special
        setHasImprovedElectroShred(false);
        setSpecialAttackRechargeCooldown(3f);
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
        if (BoonManager.getInstance().getActiveBoon() != null && BoonManager.getInstance().getActiveBoon().equals(BoonEnums.THICK_HIDE)) {
            shouldIncreaseBaseDamage = false;
        }

        if (shouldIncreaseBaseDamage) {
            //Disabled om de mid-late game spannend te houden. Power moet van items komen.
//            baseDamage = ExperienceCalculator.getNextLevelBaseDamage(baseDamage);
//            droneBaseDamage = ExperienceCalculator.getNextLevelBaseDamage(droneBaseDamage);
        }

        maxHitPoints = ExperienceCalculator.getNextLevelHitPoints(maxHitPoints);
        maxShieldHitPoints = ExperienceCalculator.getNextLevelShieldPoints(maxShieldHitPoints);


        for (SpaceShip player : PlayerManager.getInstance().getAllSpaceShips()) {
            player.setMaxHitPoints(maxHitPoints);
            player.setMaxShieldPoints(maxShieldHitPoints);
            player.setCurrentHitpoints(maxHitPoints);

            if (player.getCurrentShieldPoints() < player.getMaxShieldPoints()) { //We don't want to overwrite any overloaded shields present
                player.setCurrentShieldPoints(maxShieldHitPoints);
            }
            AnimationManager.getInstance().playLevelUpAnimation(player);

        }
        AudioManager.getInstance().addAudio(AudioEnums.GenericSelect);
    }

    public float getBaseAttackSpeed() {
        return this.attackSpeed;
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
        return this.specialAttackRechargeCooldown;
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
        return maxShieldHitPoints;
    }

    public float getBaseMaxShieldPoints() {
        return maxShieldHitPoints;
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

    public PlayerPrimaryAttackTypes getAttackType() {
        return attackType;
    }

    public PlayerSpecialAttackTypes getSpecialAttackType() {
        return specialAttackType;
    }

    public float getBaseDamage() {
        return baseDamage;
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

    public boolean isHasImprovedElectroShred() {
        return hasImprovedElectroShred;
    }

    public void setHasImprovedElectroShred(boolean hasImprovedElectroShred) {
        this.hasImprovedElectroShred = hasImprovedElectroShred;
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

    public float getKnockBackDamping() {
        return knockBackDamping;
    }

    public void setKnockBackDamping(float knockBackDamping) {
        this.knockBackDamping = knockBackDamping;
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
        return shieldRegenPerTick;
    }


    public float getIgniteDuration() {
        return igniteDuration;
    }

    public float getIgniteDamage() {
        return Math.max(baseDamage * igniteDamageMultiplier, 0.01f); //prevent negative damage
    }

    public void modifyFireFighterIgniteDamageMultiplier(float amount) {
        igniteDamageMultiplier += amount;
    }

    public void modifyMaxIgniteStacks(int igniteStacks) {
        this.baseMaxIgniteStacks += igniteStacks;
    }

    public int getBaseMaxIgniteStacks() {
        return baseMaxIgniteStacks;
    }

    public float getBaseDroneDamage() {
        return droneBaseDamage;
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

    public int getMaxAmountOfProtoss() {
        return defaultMaxAmountOfProtoss;
    }

    public void modifyMaxAmountOfProtoss(int amount) {
        this.defaultMaxAmountOfProtoss += amount;
    }

    public int getCarrierStartingScouts() {
        return defaultCarrierStartingScouts;
    }

    public void setCarrierStartingScouts(int carrierStartingScouts) {
        this.defaultCarrierStartingScouts = carrierStartingScouts;
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

    public void setBaseArmor(float baseArmor) {
        this.baseArmor = baseArmor;
    }

    public void addBaseArmor(float amount) {
        this.baseArmor += amount;
    }

    public float getFlatDamageReduction() {
        return this.flatDamageReduction;
    }

    public void modifyFlatDamageReduction(float flatDamageReduction) {
        this.flatDamageReduction += flatDamageReduction;
    }

    public static void setDefaultMaxAmountOfProtoss(int defaultMaxAmountOfProtoss) {
        PlayerStats.defaultMaxAmountOfProtoss = defaultMaxAmountOfProtoss;
    }

    public static int getDefaultMaxAmountOfProtoss() {
        return defaultMaxAmountOfProtoss;
    }

    public static int getDefaultCarrierStartingScouts() {
        return defaultCarrierStartingScouts;
    }
}