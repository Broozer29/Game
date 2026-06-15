package net.riezebos.bruus.tbd.game.gameobjects.neutral;

public class ExplosionConfiguration {
    private boolean isFriendly;
    private float damage;
    private boolean applyOnHitEffects;

    public ExplosionConfiguration (boolean isFriendly, float damage, boolean applyOnHitEffects) {
        this.isFriendly = isFriendly;
        this.damage = damage;
        this.applyOnHitEffects = applyOnHitEffects;
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

    public void setDamage (float damage) {
        this.damage = damage;
    }

    public boolean isApplyOnHitEffects () {
        return applyOnHitEffects;
    }

    public void setApplyOnHitEffects (boolean applyOnHitEffects) {
        this.applyOnHitEffects = applyOnHitEffects;
    }
}
