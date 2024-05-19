package game.objects.missiles;

import VisualAndAudioData.image.ImageEnums;
import game.objects.missiles.missiletypes.BarrierProjectile;

public enum MissileTypeEnums {
    AlienLaserbeam(false, 2, 2, 17.5f, "AlienLaserbeam", null, ImageEnums.Alien_Laserbeam),
    BombaProjectile(false, 3, 3, 50f, "BombaProjctile", ImageEnums.Bomba_Missile_Explosion, ImageEnums.Bomba_Missile),
    BulldozerProjectile(false, 3,3, 15f, "BulldozerProjectile", ImageEnums.Bulldozer_Missile_Explosion, ImageEnums.Bulldozer_Missile),
    EnergizerProjectile(false, 3,3,15f, "EnergizeProjectile", ImageEnums.Energizer_Missile_Explosion, ImageEnums.Energizer_Missile),
    FlamerProjectile(false, 3,3,15f, "FlamerProjectile", ImageEnums.Flamer_Missile_Explosion, ImageEnums.Flamer_Missile),
    SeekerProjectile(false, 3,3,15f, "SeekerProjectile", ImageEnums.Seeker_Missile_Explosion, ImageEnums.Seeker_Missile),
    TazerProjectile(false, 3,3,15f, "TazerProjectile", ImageEnums.LightningOrbDestruction, ImageEnums.LightningOrb),
    FlameThrowerProjectile(false, 3,3,10f, "FlameThrowerProjectile", ImageEnums.Flamer_Missile_Explosion, ImageEnums.Flamer_Missile),
    DefaultPlayerLaserbeam(false, 3,3,10, "DefaultPlayerLaserbeam", ImageEnums.Impact_Explosion_One, ImageEnums.Alien_Laserbeam),
    FirewallMissile(false, 3,3,10f, "FirewallMissile", null, ImageEnums.FirewallParticle),
    PlasmaLauncherMissile(false, 3,3,25f, "PlasmaLauncherMissile", ImageEnums.Impact_Explosion_One, ImageEnums.PlasmaLauncherMissile),
    Rocket1(false, 3,3,1f, "Rocket1", ImageEnums.Rocket_1_Explosion, ImageEnums.Rocket_1),
    LaserBullet(false, 2,2, 15f, "LaserBullet",ImageEnums.LaserBulletDestruction , ImageEnums.LaserBullet),
    BarrierProjectile(false, 1,1, 25f, "BarrierProjectile", ImageEnums.BarrierProjectileDestruction, ImageEnums.BarrierProjectile);

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

    public int getyMovementSpeed () {
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
