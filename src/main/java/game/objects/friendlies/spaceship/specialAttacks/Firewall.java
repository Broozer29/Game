package game.objects.friendlies.spaceship.specialAttacks;

import data.PlayerStats;
import data.image.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileManager;
import game.objects.missiles.missiletypes.FirewallMissile;
import visual.objects.SpriteAnimation;

public class Firewall extends SpecialAttack {

	public Firewall(int x, int y, float scale, SpriteAnimation animation, float damage, boolean friendly,
			PathFinder pathFinder, int fireWallSize, Direction rotation) {
		super(x, y, scale, animation, damage, friendly);
		initFireWallParticles(pathFinder, fireWallSize, rotation);
	}

	private void initFireWallParticles(PathFinder pathFinder, int fireWallSize, Direction rotation) {
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
			Missile firewallParticle = MissileCreator.getInstance().createFriendlyMissile(x, y,
					ImageEnums.FirewallParticle, null, rotation, scale, pathFinder, firewallSpeed, firewallSpeed,
					PlayerAttackTypes.Firewall);
			firewallParticle.getAnimation().rotateAnimetion(animationRotation);
			this.specialAttackMissiles.add(firewallParticle);
			MissileManager.getInstance().addExistingMissile(firewallParticle);
		}
		
	}

}