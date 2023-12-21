package game.objects.neutral;

public class ExplosionConfiguration {
    private boolean isFriendly;
    private float damage;
    private boolean allowedToDealDamage;

    public ExplosionConfiguration (boolean isFriendly, float damage, boolean allowedToDealDamage) {
        this.isFriendly = isFriendly;
        this.damage = damage;
        this.allowedToDealDamage = allowedToDealDamage;
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

    public boolean isAllowedToDealDamage () {
        return allowedToDealDamage;
    }

    public void setAllowedToDealDamage (boolean allowedToDealDamage) {
        this.allowedToDealDamage = allowedToDealDamage;
    }
}
