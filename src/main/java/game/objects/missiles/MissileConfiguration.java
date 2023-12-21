package game.objects.missiles;

import game.movement.Direction;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;

public class MissileConfiguration {

    private MissileTypeEnums missileType;

    private int maxHitPoints;
    private int maxShields;

    private AudioEnums deathSound;
    private float damage;


    private ImageEnums destructionType;

    private boolean isFriendly;

    private PathFinder pathfinder;

    private Direction movementDirection;
    private int xMovementSpeed;
    private int yMovementSpeed;

    private boolean allowedToDealDamage;
    private String objectType;

    private GameObject targetToChase;

    public MissileConfiguration (MissileTypeEnums missileType, int maxHitPoints, int maxShields, AudioEnums deathSound, ImageEnums destructionType, boolean isFriendly, PathFinder pathfinder, Direction movementDirection, int xMovementSpeed, int yMovementSpeed, boolean allowedToDealDamage, String objectType, float damage) {
        this.missileType = missileType;
        this.maxHitPoints = maxHitPoints;
        this.maxShields = maxShields;
        this.deathSound = deathSound;
        this.destructionType = destructionType;
        this.isFriendly = isFriendly;
        this.pathfinder = pathfinder;
        this.movementDirection = movementDirection;
        this.xMovementSpeed = xMovementSpeed;
        this.yMovementSpeed = yMovementSpeed;
        this.allowedToDealDamage = allowedToDealDamage;
        this.objectType = objectType;
        this.damage = damage;
    }

    public MissileConfiguration(){

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

    public AudioEnums getDeathSound () {
        return deathSound;
    }

    public void setDeathSound (AudioEnums deathSound) {
        this.deathSound = deathSound;
    }

    public ImageEnums getDestructionType () {
        return destructionType;
    }

    public void setDestructionType (ImageEnums destructionType) {
        this.destructionType = destructionType;
    }

    public boolean isFriendly () {
        return isFriendly;
    }

    public void setFriendly (boolean friendly) {
        isFriendly = friendly;
    }

    public PathFinder getPathfinder () {
        return pathfinder;
    }

    public void setPathfinder (PathFinder pathfinder) {
        this.pathfinder = pathfinder;
    }

    public Direction getMovementDirection () {
        return movementDirection;
    }

    public void setMovementDirection (Direction movementDirection) {
        this.movementDirection = movementDirection;
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

    public float getDamage () {
        return damage;
    }

    public void setDamage (int damage) {
        this.damage = damage;
    }

    public MissileTypeEnums getMissileType () {
        return missileType;
    }

    public void setMissileType (MissileTypeEnums missileType) {
        this.missileType = missileType;
    }

    public void setDamage (float damage) {
        this.damage = damage;
    }

    public GameObject getTargetToChase () {
        return targetToChase;
    }

    public void setTargetToChase (GameObject targetToChase) {
        this.targetToChase = targetToChase;
    }
}
