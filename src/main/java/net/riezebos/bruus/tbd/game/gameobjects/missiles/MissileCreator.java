package net.riezebos.bruus.tbd.game.gameobjects.missiles;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.*;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class MissileCreator {

    private static MissileCreator instance = new MissileCreator();

    private MissileCreator() {
    }

    public static MissileCreator getInstance() {
        return instance;
    }

    public SpriteConfiguration createMissileSpriteConfig(int xCoordinate, int yCoordinate, ImageEnums missileImageType, float scale) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setImageType(missileImageType);
        spriteConfiguration.setScale(scale);
        return spriteConfiguration;
    }

    public MissileConfiguration createMissileConfiguration(MissileEnums attackType, float damage, ImageEnums missileDestructionImage, boolean isFriendly,
                                                           boolean isExplosive, boolean appliesOnHitEffects, boolean isDestructable) {
        return new MissileConfiguration(attackType, damage, missileDestructionImage, isFriendly,
                isExplosive, appliesOnHitEffects, isDestructable);
    }

    public MovementConfiguration createMissileMovementConfig(float moveSpeed, PathFinder pathFinder, Direction direction) {
        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setMovementSpeed(moveSpeed);
        movementConfiguration.setOriginalMovementSpeed(moveSpeed);
        movementConfiguration.setPathFinder(pathFinder);
        movementConfiguration.setDirection(direction);
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        return movementConfiguration;
    }

    public Missile createMissile(SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        switch (missileConfiguration.getMissileType()) {
            case DefaultLaserBullet -> {
                return new GenericMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
            }
            case BombaProjectile -> {
                return new BombaProjectile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case StationaryExplodingBomb -> {
                return new StationaryExplodingBomb(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case TazerProjectile -> {
                return new TazerProjectile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case DefaultAnimatedBullet, Orbitter, PlayerLaserbeam -> {
                return new GenericMissile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
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
            case YellowBossOrb -> {
                return new YellowBossOrb(upgradeConfig(spriteConfiguration, 0), missileConfiguration, movementConfiguration);
            }
        }
        return null;
    }

    private SpriteAnimationConfiguration upgradeConfig(SpriteConfiguration spriteConfiguration, int frameDelay) {
        return new SpriteAnimationConfiguration(spriteConfiguration, frameDelay, true);
    }

}


