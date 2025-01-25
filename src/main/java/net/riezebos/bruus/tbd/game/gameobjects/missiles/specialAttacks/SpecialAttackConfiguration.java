package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.movement.Point;

public class SpecialAttackConfiguration {
    private float damage;
    private boolean friendly;
    private boolean allowedToDealDamage;
    private boolean boxCollision;
    private boolean allowRepeatedDamage;
    private boolean destroysMissiles;
    private Point destination;
    private boolean isAllowedToApplyItemEffects;


    public SpecialAttackConfiguration (float damage, boolean friendly, boolean allowedToDealDamage, boolean boxCollision, boolean allowRepeatedDamage, boolean destorysMissiles,
                                       boolean isAllowedToApplyItemEffects) {
        this.damage = damage;
        this.friendly = friendly;
        this.allowedToDealDamage = allowedToDealDamage;
        this.boxCollision = boxCollision;
        this.allowRepeatedDamage = allowRepeatedDamage;
        this.destroysMissiles = destorysMissiles;
        this.isAllowedToApplyItemEffects = isAllowedToApplyItemEffects;
    }

    public boolean isDestroysMissiles () {
        return destroysMissiles;
    }

    public void setDestorysMissiles (boolean destorysMissiles) {
        this.destroysMissiles = destorysMissiles;
    }

    public float getDamage () {
        return damage;
    }

    public void setDamage (float damage) {
        this.damage = damage;
    }

    public boolean isFriendly () {
        return friendly;
    }

    public void setFriendly (boolean friendly) {
        this.friendly = friendly;
    }

    public boolean isAllowedToDealDamage () {
        return allowedToDealDamage;
    }

    public void setAllowedToDealDamage (boolean allowedToDealDamage) {
        this.allowedToDealDamage = allowedToDealDamage;
    }

    public boolean isBoxCollision () {
        return boxCollision;
    }

    public void setBoxCollision (boolean boxCollision) {
        this.boxCollision = boxCollision;
    }

    public boolean isAllowRepeatedDamage () {
        return allowRepeatedDamage;
    }

    public void setAllowRepeatedDamage (boolean allowRepeatedDamage) {
        this.allowRepeatedDamage = allowRepeatedDamage;
    }

    public void setDestroysMissiles (boolean destroysMissiles) {
        this.destroysMissiles = destroysMissiles;
    }

    public Point getDestination () {
        return destination;
    }

    public void setDestination (Point destination) {
        this.destination = destination;
    }

    public boolean isAllowedToApplyItemEffects () {
        return isAllowedToApplyItemEffects;
    }

    public void setAllowedToApplyItemEffects (boolean allowedToApplyItemEffects) {
        isAllowedToApplyItemEffects = allowedToApplyItemEffects;
    }
}
