package game.objects.player.specialAttacks;

import game.movement.Direction;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileManager;
import game.objects.player.PlayerStats;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;

public class Firewall extends SpecialAttack {

	public Firewall(SpriteAnimationConfiguration spriteAnimationConfiguration, MissileConfiguration missileConfiguration, int fireWallAmount) {
		super(spriteAnimationConfiguration, missileConfiguration);
		initFireWallParticles(missileConfiguration, fireWallAmount);
		this.setObjectType("Firewall");
	}

	private void initFireWallParticles(MissileConfiguration missileConfiguration, int fireWallSize) {
		for (int i = 0; i < fireWallSize; i++) {
			int x = this.xCoordinate + 10;
			int y;
			float scale = (float) 1.5;
			Direction animationRotation = Direction.LEFT;
			int fireWallKernel = 19;
			if (i % 2 == 0) {
				// even index - go "down"
				animationRotation = Direction.RIGHT;
				y = this.yCoordinate + (fireWallKernel * (i / 2));
			} else {
				// odd index - go "up"
				animationRotation = Direction.LEFT;
				y = this.yCoordinate - (fireWallKernel * ((i + 1) / 2));
			}

			int firewallSpeed = PlayerStats.getInstance().getFirewallSpeed();

			SpriteConfiguration spriteConfiguration1 = this.spriteConfiguration;
			spriteConfiguration1.setImageType(ImageEnums.FirewallParticle);

			MissileConfiguration missileConfiguration1 = new MissileConfiguration();
			missileConfiguration1.setxMovementSpeed(firewallSpeed);
			missileConfiguration1.setyMovementSpeed(firewallSpeed);
			missileConfiguration1.setAllowedToDealDamage(true);
			missileConfiguration1.setMovementDirection(this.movementDirection);
			missileConfiguration1.setPathfinder(new RegularPathFinder());
			missileConfiguration1.setDamage(1.5f);



			Missile firewallParticle = MissileCreator.getInstance().createMissile(spriteConfiguration1, missileConfiguration1);
			firewallParticle.getAnimation().rotateAnimetion(animationRotation);
			this.specialAttackMissiles.add(firewallParticle);
			MissileManager.getInstance().addExistingMissile(firewallParticle);
		}
		
	}

}