package net.riezebos.bruus.tbd.game.gameobjects.missiles;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.*;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class MissileCreator {

    private static MissileCreator instance = new MissileCreator();

    private MissileCreator () {
    }

    public static MissileCreator getInstance () {
        return instance;
    }

    public SpriteConfiguration createMissileSpriteConfig (int xCoordinate, int yCoordinate, ImageEnums missileImageType, float scale) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setImageType(missileImageType);
        spriteConfiguration.setScale(scale);
        return spriteConfiguration;
    }

    public MissileConfiguration createMissileConfiguration (MissileEnums attackType, int maxHitPoints, int maxShields,
                                                            AudioEnums deathSound, float damage, ImageEnums missileDestructionImage,
                                                            boolean isFriendly, boolean allowedToDealDamage, String objectType, boolean isBoxCollision,
                                                            boolean isExplosive, boolean appliesOnHitEffects, boolean isDestructable) {
        MissileConfiguration missileConfiguration = new MissileConfiguration(attackType, maxHitPoints, maxShields,
                deathSound, damage, missileDestructionImage, isFriendly,
                allowedToDealDamage, objectType, isBoxCollision, isExplosive, appliesOnHitEffects, isDestructable);
        return missileConfiguration;
    }

    public MovementConfiguration createMissileMovementConfig (float xSpeed, float ySpeed, PathFinder pathFinder, MovementPatternSize movementPatternSize, Direction rotation) {
        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setXMovementSpeed(xSpeed);
        movementConfiguration.setYMovementSpeed(ySpeed);
        movementConfiguration.setPathFinder(pathFinder);
        movementConfiguration.setPatternSize(movementPatternSize);
        movementConfiguration.setDirection(rotation);
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        return movementConfiguration;
    }

    public Missile createMissile (SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        switch (missileConfiguration.getMissileType()) {
            case DefaultLaserBullet -> {
                return new GenericMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
            }
            case BombaProjectile -> {
                return new BombaProjectile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case TazerProjectile -> {
                return new TazerProjectile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case DefaultAnimatedBullet, Orbitter, PlayerLaserbeam -> {
                return new GenericMissile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case DefaultRocket -> {
                return new Rocket1(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case BarrierProjectile -> {
                return new BarrierProjectile(upgradeConfig(spriteConfiguration, 3), missileConfiguration, movementConfiguration);
            }
            case OrbitCenter -> {
                return new OrbitterWithOrbits(upgradeConfig(spriteConfiguration, 4), missileConfiguration, movementConfiguration);
            }
            case ProtossShuttleMissile -> {
                return new ProtossShuttleMissile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case ReflectiveBlocks -> {
                return new ReflectiveBlocks(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
        }
        return null;
    }

    private SpriteAnimationConfiguration upgradeConfig (SpriteConfiguration spriteConfiguration, int frameDelay) {
        return new SpriteAnimationConfiguration(spriteConfiguration, frameDelay, true);
    }

}


