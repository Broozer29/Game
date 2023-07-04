package game.objects.friendlies.spaceship.specialAttacks;


import data.PlayerStats;
import data.image.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.missiles.FirewallMissile;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileManager;
import visual.objects.SpriteAnimation;

public class Firewall extends SpecialAttack {

	public Firewall(int x, int y, float scale, SpriteAnimation animation, float damage, boolean friendly, PathFinder pathFinder, int fireWallSize, Direction rotation) {
		super(x, y, scale, animation, damage, friendly);
		initFireWallParticles(pathFinder, fireWallSize, rotation);
	}
	
	private void initFireWallParticles(PathFinder pathFinder, int fireWallSize, Direction rotation) {
	    for(int i = 0; i < fireWallSize; i++) {
	        int x = this.xCoordinate + 10;
	        int y;
	        
	        int fireWallKernel = 20;
	        if (i % 2 == 0) {
	            // even index - go "down"
	            y = this.yCoordinate + (fireWallKernel * (i / 2));
	        } else {
	            // odd index - go "up"
	            y = this.yCoordinate - (fireWallKernel * ((i + 1) / 2));
	        }
	        
	        Point start = new Point(x,y);
	        Point end = pathFinder.calculateInitialEndpoint(start, rotation);
	        float damage = PlayerStats.getInstance().getFirewallDamage();
	        int firewallSpeed = PlayerStats.getInstance().getFirewallSpeed();
	        
	        Missile firewallParticle = new FirewallMissile(x, y, end, ImageEnums.FirewallParticle, null, rotation, 1, pathFinder, true, firewallSpeed, firewallSpeed, damage);
	        this.specialAttackMissiles.add(firewallParticle);
	        MissileManager.getInstance().addExistingMissile(firewallParticle);
	    }
	}

}