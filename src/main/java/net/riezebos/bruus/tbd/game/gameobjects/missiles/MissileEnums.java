package net.riezebos.bruus.tbd.game.gameobjects.missiles;

import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

public enum MissileEnums {
    BombaProjectile(ImageEnums.Bomba_Missile_Explosion, ImageEnums.Bomba_Missile),
    TazerProjectile(ImageEnums.LightningOrbDestruction, ImageEnums.LightningOrb),
    PlayerLaserbeam(ImageEnums.Impact_Explosion_One, ImageEnums.AlienLaserBeamAnimated),
    DefaultAnimatedBullet(ImageEnums.Impact_Explosion_One, ImageEnums.AlienLaserBeamAnimated),
    DefaultLaserBullet(ImageEnums.LaserBulletDestruction, ImageEnums.LaserBullet),
    OrbitCenter(ImageEnums.Destroyed_Explosion, ImageEnums.DestructableOrbitCenterMissile),
    Orbitter(ImageEnums.BarrierProjectileDestruction, ImageEnums.BarrierProjectile),
    ProtossShuttleMissile(ImageEnums.ProtossShuttleMissileExplosion, ImageEnums.ProtossShuttleMissile),
    ReflectiveBlocks(ImageEnums.ProtossShuttleMissileExplosion, ImageEnums.RotatingBoxes),
    StationaryExplodingBomb(ImageEnums.Bomba_Missile_Explosion, ImageEnums.Bomba_Missile),
    YellowBossOrb(ImageEnums.Impact_Explosion_One, ImageEnums.AlienLaserBeamAnimated),
    BarrierProjectile(ImageEnums.BarrierProjectileDestruction, ImageEnums.BarrierProjectile);

    private ImageEnums deathOrExplosionImageEnum;
    private ImageEnums imageType;

    MissileEnums(ImageEnums deathOrExplosionImageEnum, ImageEnums imageType) {
        this.deathOrExplosionImageEnum = deathOrExplosionImageEnum;
        this.imageType = imageType;
    }

    public ImageEnums getImageType() {
        return imageType;
    }

    public ImageEnums getDeathOrExplosionImageEnum() {
        return deathOrExplosionImageEnum;
    }
}
