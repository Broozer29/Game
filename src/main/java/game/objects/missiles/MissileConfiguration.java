package game.objects.missiles;

import game.movement.Direction;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;

public class MissileConfiguration {

    private MissileTypeEnums missileType;

    private int maxHitPoints;
    private int maxShields;

    private AudioEnums deathSound;
    private float damage;


    private ImageEnums destructionType;

    private boolean isFriendly;

    private boolean allowedToDealDamage;
    private String objectType;
    private boolean boxCollision;
    private boolean destroysMissiles;

    public MissileConfiguration (MissileTypeEnums missileType, int maxHitPoints, int maxShields, AudioEnums deathSound,
                                 float damage, ImageEnums destructionType, boolean isFriendly, boolean allowedToDealDamage,
                                 String objectType, boolean boxCollision) {
        this.missileType = missileType;
        this.maxHitPoints = maxHitPoints;
        this.maxShields = maxShields;
        this.deathSound = deathSound;
        this.damage = damage;
        this.destructionType = destructionType;
        this.isFriendly = isFriendly;
        this.allowedToDealDamage = allowedToDealDamage;
        this.objectType = objectType;
        this.boxCollision = boxCollision;
        this.destroysMissiles = false; //Default setting
    }

    public MissileConfiguration () {

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


    public boolean isBoxCollision () {
        return boxCollision;
    }

    public void setBoxCollision (boolean boxCollision) {
        this.boxCollision = boxCollision;
    }

    public boolean isDestroysMissiles () {
        return destroysMissiles;
    }

    public void setDestroysMissiles (boolean destroysMissiles) {
        this.destroysMissiles = destroysMissiles;
    }
}
