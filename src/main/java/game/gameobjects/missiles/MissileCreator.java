package game.gameobjects.missiles;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.MovementPatternSize;
import game.movement.pathfinders.PathFinder;
import game.gameobjects.missiles.missiletypes.*;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

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
        movementConfiguration.setRotation(rotation);
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        return movementConfiguration;
    }

    public Missile createMissile (SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        switch (missileConfiguration.getMissileType()) {
            case ScoutLaserBullet -> {
                return new GenericMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
            }
            case BombaProjectile -> {
                return new BombaProjectile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case BulldozerProjectile -> {
                return new BulldozerProjectile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case EnergizerProjectile -> {
                return new EnergizerProjectile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case FlamerProjectile -> {
                return new FlamerProjectile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case SeekerProjectile -> {
                return new SeekerProjectile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case TazerProjectile -> {
                return new TazerProjectile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case FlameThrowerProjectile -> {
                return new FlamethrowerProjectile(upgradeConfig(spriteConfiguration, 2), missileConfiguration, movementConfiguration);
            }
            case PlasmaLauncherMissile, Orbitter, PlayerLaserbeam -> {
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
        }
        return null;
    }

    private SpriteAnimationConfiguration upgradeConfig (SpriteConfiguration spriteConfiguration, int frameDelay) {
        return new SpriteAnimationConfiguration(spriteConfiguration, frameDelay, true);
    }

}


