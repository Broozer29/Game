package game.objects.enemies;

import game.movement.Direction;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.PathFinder;
import VisualAndAudioData.audio.enums.AudioEnums;
import game.objects.enemies.enums.EnemyEnums;

public class EnemyConfiguration {
    private EnemyEnums enemyType;

    private int maxHitPoints;
    private int maxShields;
    private boolean hasAttack;
    private boolean showHealthBar;
    private AudioEnums deathSound;

    private boolean allowedToDealDamage;

    private String objectType;

    private float attackSpeed;

    private boolean isBoxCollision;

    private float baseArmor;
    private float xpOnDeath;

    private float cashMoneyWorth;

    public EnemyConfiguration (EnemyEnums enemyType, int maxHitPoints, int maxShields, boolean hasAttack, boolean showHealthBar, AudioEnums deathSound, boolean allowedToDealDamage, String objectType, float attackSpeed, boolean isBoxCollision, float baseArmor, float xpOnDeath, float cashMoneyWorth) {
        this.enemyType = enemyType;
        this.maxHitPoints = maxHitPoints;
        this.maxShields = maxShields;
        this.hasAttack = hasAttack;
        this.showHealthBar = showHealthBar;
        this.deathSound = deathSound;
        this.allowedToDealDamage = allowedToDealDamage;
        this.objectType = objectType;
        this.attackSpeed = attackSpeed;
        this.isBoxCollision = isBoxCollision;
        this.baseArmor = baseArmor;
        this.xpOnDeath = xpOnDeath;
        this.cashMoneyWorth = cashMoneyWorth;
    }

    public EnemyEnums getEnemyType () {
        return enemyType;
    }

    public void setEnemyType (EnemyEnums enemyType) {
        this.enemyType = enemyType;
    }

    public int getMaxHitPoints () {
        return maxHitPoints;
    }

    public void setMaxHitPoints (int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public int getMaxShields () {
        return maxShields;
    }

    public void setMaxShields (int maxShields) {
        this.maxShields = maxShields;
    }

    public boolean isHasAttack () {
        return hasAttack;
    }

    public void setHasAttack (boolean hasAttack) {
        this.hasAttack = hasAttack;
    }

    public boolean isShowHealthBar () {
        return showHealthBar;
    }

    public void setShowHealthBar (boolean showHealthBar) {
        this.showHealthBar = showHealthBar;
    }

    public AudioEnums getDeathSound () {
        return deathSound;
    }

    public void setDeathSound (AudioEnums deathSound) {
        this.deathSound = deathSound;
    }

    public boolean isAllowedToDealDamage () {
        return allowedToDealDamage;
    }

    public void setAllowedToDealDamage (boolean allowedToDealDamage) {
        this.allowedToDealDamage = allowedToDealDamage;
    }

    public String getObjectType () {
        return objectType;
    }

    public void setObjectType (String objectType) {
        this.objectType = objectType;
    }

    public float getAttackSpeed () {
        return attackSpeed;
    }

    public void setAttackSpeed (int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public boolean isBoxCollision () {
        return isBoxCollision;
    }

    public void setBoxCollision (boolean boxCollision) {
        isBoxCollision = boxCollision;
    }

    public float getBaseArmor () {
        return baseArmor;
    }

    public void setBaseArmor (float baseArmor) {
        this.baseArmor = baseArmor;
    }

    public float getXpOnDeath () {
        return xpOnDeath;
    }

    public void setXpOnDeath (float xpOnDeath) {
        this.xpOnDeath = xpOnDeath;
    }

    public float getCashMoneyWorth () {
        return cashMoneyWorth;
    }

    public void setCashMoneyWorth (float cashMoneyWorth) {
        this.cashMoneyWorth = cashMoneyWorth;
    }
}
