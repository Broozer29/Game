package game.objects.missiles.specialAttacks;

public class SpecialAttackConfiguration {
    private float damage;
    private boolean friendly;
    private boolean allowedToDealDamage;
    private boolean boxCollision;
    private boolean allowRepeatedDamage;

    public SpecialAttackConfiguration (float damage, boolean friendly, boolean allowedToDealDamage, boolean boxCollision, boolean allowRepeatedDamage) {
        this.damage = damage;
        this.friendly = friendly;
        this.allowedToDealDamage = allowedToDealDamage;
        this.boxCollision = boxCollision;
        this.allowRepeatedDamage = allowRepeatedDamage;
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
}
