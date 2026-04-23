package net.riezebos.bruus.tbd.game.gameobjects.missiles;

import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

public class MissileConfiguration {

    private MissileEnums missileType;
    private boolean isDestructable;
    private float damage;
    private ImageEnums destructionType;
    private boolean isFriendly;
    private boolean piercesMissiles;
    private int amountOfPierces;
    private boolean isExplosive;
    private boolean appliesOnHitEffects;

    public MissileConfiguration (MissileEnums missileType,
                                 float damage, ImageEnums destructionType, boolean isFriendly,
                                 boolean isExplosive, boolean appliesOnHitEffects,
                                 boolean isDestructable) {
        this.missileType = missileType;
        this.damage = damage;
        this.destructionType = destructionType;
        this.isFriendly = isFriendly;
        this.piercesMissiles = false; //Default setting
        this.amountOfPierces = 0; //Default setting
        this.isDestructable = isDestructable; //Default setting
        this.isExplosive = isExplosive;
        this.appliesOnHitEffects = appliesOnHitEffects;
    }

    public MissileConfiguration () {

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
