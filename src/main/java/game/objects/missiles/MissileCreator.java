package game.objects.missiles;

import game.objects.missiles.missiletypes.*;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class MissileCreator {

	private static MissileCreator instance = new MissileCreator();

	private MissileCreator() {
	}

	public static MissileCreator getInstance() {
		return instance;
	}

	public Missile createMissile(SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		switch (missileConfiguration.getMissileType()) {
			case AlienLaserbeam, DefaultPlayerLaserbeam -> {
				return new GenericMissile(spriteConfiguration,missileConfiguration);
			}
			case BombaProjectile -> {
				return new BombaProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case BulldozerProjectile -> {
				return new BulldozerProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case EnergizerProjectile -> {
				return new EnergizerProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case FlamerProjectile -> {
				return new FlamerProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case SeekerProjectile -> {
				return new SeekerProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case TazerProjectile -> {
				return new TazerProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case FlameThrowerProjectile -> {
				return new FlamethrowerProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
            case FirewallMissile, PlasmaLauncherMissile -> {
				return new GenericMissile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case Rocket1 -> {
				return new Rocket1(upgradeConfig(spriteConfiguration, 2, true), missileConfiguration);
			}
        }
		return null;
	}

	private SpriteAnimationConfiguration upgradeConfig(SpriteConfiguration spriteConfiguration, int frameDelay, boolean infiniteLoop){
		return new SpriteAnimationConfiguration(spriteConfiguration, frameDelay, infiniteLoop);
	}
}


