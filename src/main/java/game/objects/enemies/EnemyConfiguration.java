package game.objects.enemies;

import game.movement.Direction;
import game.movement.pathfinders.PathFinder;
import game.objects.enemies.EnemyEnums;
import gamedata.audio.AudioEnums;

public class EnemyConfiguration {
    private EnemyEnums enemyType;

    private int maxHitPoints;
    private int maxShields;
    private boolean hasAttack;
    private boolean showHealthBar;
    private AudioEnums deathSound;

    private Direction movementDirection;
    private PathFinder movementPathFinder;
    private int xMovementSpeed;
    private int yMovementSpeed;
    private boolean allowedToDealDamage;

    private String objectType;

    private int attackSpeed;

    public EnemyConfiguration (EnemyEnums enemyType, int maxHitPoints, int maxShields, boolean hasAttack, boolean showHealthBar, AudioEnums deathSound, Direction movementDirection, PathFinder movementPathFinder, int xMovementSpeed, int yMovementSpeed, boolean allowedToDealDamage, String objectType, int attackSpeed) {
        this.enemyType = enemyType;
        this.maxHitPoints = maxHitPoints;
        this.maxShields = maxShields;
        this.hasAttack = hasAttack;
        this.showHealthBar = showHealthBar;
        this.deathSound = deathSound;
        this.movementDirection = movementDirection;
        this.movementPathFinder = movementPathFinder;
        this.xMovementSpeed = xMovementSpeed;
        this.yMovementSpeed = yMovementSpeed;
        this.allowedToDealDamage = allowedToDealDamage;
        this.objectType = objectType;
        this.attackSpeed = attackSpeed;
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

    public Direction getMovementDirection () {
        return movementDirection;
    }

    public void setMovementDirection (Direction movementDirection) {
        this.movementDirection = movementDirection;
    }

    public PathFinder getMovementPathFinder () {
        return movementPathFinder;
    }

    public void setMovementPathFinder (PathFinder movementPathFinder) {
        this.movementPathFinder = movementPathFinder;
    }

    public int getxMovementSpeed () {
        return xMovementSpeed;
    }

    public void setxMovementSpeed (int xMovementSpeed) {
        this.xMovementSpeed = xMovementSpeed;
    }

    public int getyMovementSpeed () {
        return yMovementSpeed;
    }

    public void setyMovementSpeed (int yMovementSpeed) {
        this.yMovementSpeed = yMovementSpeed;
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

    public int getAttackSpeed () {
        return attackSpeed;
    }

    public void setAttackSpeed (int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }
}
