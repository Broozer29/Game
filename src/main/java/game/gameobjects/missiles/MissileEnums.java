package game.gameobjects.missiles;

import VisualAndAudioData.image.ImageEnums;

public enum MissileEnums {
    BombaProjectile(false, "BombaProjctile", ImageEnums.Bomba_Missile_Explosion, ImageEnums.Bomba_Missile),
    TazerProjectile(false, "TazerProjectile", ImageEnums.LightningOrbDestruction, ImageEnums.LightningOrb),
    PlayerLaserbeam(false, "DefaultPlayerLaserbeam", ImageEnums.Impact_Explosion_One, ImageEnums.AlienLaserBeamAnimated),
    PlasmaLauncherMissile(false, "PlasmaLauncherMissile", ImageEnums.Impact_Explosion_One, ImageEnums.PlasmaLauncherMissile),
    DefaultRocket(false, "DefaultRocket", ImageEnums.Rocket_1_Explosion, ImageEnums.Rocket_1),
    ScoutLaserBullet(false, "LaserBullet", ImageEnums.LaserBulletDestruction, ImageEnums.LaserBullet),
    OrbitCenter(false, "OrbitCenter", ImageEnums.Destroyed_Explosion, ImageEnums.DestructableOrbitCenterMissile),
    Orbitter(false, "Orbitter", ImageEnums.BarrierProjectileDestruction, ImageEnums.GreenEnergyOrb2Looping),
    BarrierProjectile(false, "BarrierProjectile", ImageEnums.BarrierProjectileDestruction, ImageEnums.BarrierProjectile);

    private final boolean usesBoxCollision;
    private String objectType;
    private ImageEnums deathOrExplosionImageEnum;
    private ImageEnums imageType;

    MissileEnums (boolean usesBoxCollision, String objectType, ImageEnums deathOrExplosionImageEnum, ImageEnums imageType) {
        this.usesBoxCollision = usesBoxCollision;
        this.objectType = objectType;
        this.deathOrExplosionImageEnum = deathOrExplosionImageEnum;
        this.imageType = imageType;
    }

    public ImageEnums getImageType () {
        return imageType;
    }


    public String getObjectType () {
        return objectType;
    }

    public ImageEnums getDeathOrExplosionImageEnum () {
        return deathOrExplosionImageEnum;
    }

    public boolean isUsesBoxCollision () {
        return usesBoxCollision;
    }
}
