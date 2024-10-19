package net.riezebos.bruus.tbd.game.gameobjects.missiles;

import net.riezebos.bruus.tbd.visuals.audiodata.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visuals.audiodata.image.ImageEnums;

public class MissileConfiguration {

    private MissileEnums missileType;
    private boolean isDestructable;
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
    private boolean piercesMissiles;
    private int amountOfPierces;
    private boolean isExplosive;
    private boolean appliesOnHitEffects;

    public MissileConfiguration (MissileEnums missileType, int maxHitPoints, int maxShields, AudioEnums deathSound,
                                 float damage, ImageEnums destructionType, boolean isFriendly, boolean allowedToDealDamage,
                                 String objectType, boolean boxCollision, boolean isExplosive, boolean appliesOnHitEffects,
                                 boolean isDestructable) {
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
        this.piercesMissiles = false; //Default setting
        this.amountOfPierces = 0; //Default setting
        this.isDestructable = isDestructable; //Default setting
        this.isExplosive = isExplosive;
        this.appliesOnHitEffects = appliesOnHitEffects;
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

    public MissileEnums getMissileType () {
        return missileType;
    }

    public void setMissileType (MissileEnums missileType) {
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

    public boolean isPiercesMissiles () {
        return piercesMissiles;
    }

    public void setPiercesMissiles (boolean piercesMissiles) {
        this.piercesMissiles = piercesMissiles;
    }

    public int getAmountOfPierces () {
        return amountOfPierces;
    }

    public void setAmountOfPierces (int amountOfPierces) {
        this.amountOfPierces = amountOfPierces;
    }

    public boolean isExplosive () {
        return isExplosive;
    }

    public void setExplosive (boolean explosive) {
        isExplosive = explosive;
    }

    public boolean isAppliesOnHitEffects () {
        return appliesOnHitEffects;
    }

    public void setAppliesOnHitEffects (boolean appliesOnHitEffects) {
        this.appliesOnHitEffects = appliesOnHitEffects;
    }

    public boolean isDestructable () {
        return isDestructable;
    }

    public void setDestructable (boolean destructable) {
        isDestructable = destructable;
    }
}
