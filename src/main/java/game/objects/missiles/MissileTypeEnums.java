package game.objects.missiles;

import VisualAndAudioData.image.ImageEnums;
import game.objects.missiles.missiletypes.*;

public enum MissileTypeEnums {
    AlienLaserbeam(false, 2, 2, 7.5f, "AlienLaserbeam", null, ImageEnums.Alien_Laserbeam),
    BombaProjectile(false, 3, 3, 5f, "BombaProjctile", ImageEnums.Bomba_Missile_Explosion, ImageEnums.Bomba_Missile),
    BulldozerProjectile(false, 3,3, 5f, "BulldozerProjectile", ImageEnums.Bulldozer_Missile_Explosion, ImageEnums.Bulldozer_Missile),
    EnergizerProjectile(false, 3,3,5f, "EnergizeProjectile", ImageEnums.Energizer_Missile_Explosion, ImageEnums.Energizer_Missile),
    FlamerProjectile(false, 3,3,5f, "FlamerProjectile", ImageEnums.Flamer_Missile_Explosion, ImageEnums.Flamer_Missile),
    SeekerProjectile(false, 3,3,5f, "SeekerProjectile", ImageEnums.Seeker_Missile_Explosion, ImageEnums.Seeker_Missile),
    TazerProjectile(false, 3,3,5f, "TazerProjectile", ImageEnums.Tazer_Missile_Explosion, ImageEnums.Tazer_Missile),
    FlameThrowerProjectile(false, 3,3,5f, "FlameThrowerProjectile", ImageEnums.Flamer_Missile_Explosion, ImageEnums.Flamer_Missile),
    DefaultPlayerLaserbeam(false, 3,3,99999999f, "DefaultPlayerLaserbeam", null, ImageEnums.Alien_Laserbeam),
    FirewallMissile(false, 3,3,5f, "FirewallMissile", null, ImageEnums.FirewallParticle),
    Rocket1(false, 3,3,5f, "Rocket1", ImageEnums.Rocket_1_Explosion, ImageEnums.Rocket_1);

    private final boolean boxCollision;
    private final int xMovementSpeed;
    private final int yMovementspeed;

    private final float damage;
    private String objectType;

    private ImageEnums deathOrExplosionImageEnum;
    private ImageEnums imageType;

    MissileTypeEnums (boolean boxCollision, int xMovementSpeed, int yMovementspeed, float damage, String objectType, ImageEnums deathOrExplosionImageEnum, ImageEnums imageType) {
        this.boxCollision = boxCollision;
        this.xMovementSpeed = xMovementSpeed;
        this.yMovementspeed = yMovementspeed;
        this.damage = damage;
        this.objectType = objectType;
        this.deathOrExplosionImageEnum = deathOrExplosionImageEnum;
        this.imageType = imageType;
    }

    public ImageEnums getImageType () {
        return imageType;
    }

    public int getxMovementSpeed () {
        return xMovementSpeed;
    }

    public int getyMovementspeed () {
        return yMovementspeed;
    }

    public float getDamage () {
        return damage;
    }

    public String getObjectType () {
        return objectType;
    }

    public ImageEnums getDeathOrExplosionImageEnum () {
        return deathOrExplosionImageEnum;
    }

    public boolean isBoxCollision () {
        return boxCollision;
    }
}
