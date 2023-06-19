package game.objects.friendlies.friendlyobjects;

import data.image.enums.ImageEnums;
import game.managers.EnemyManager;
import game.managers.ExplosionManager;
import game.managers.MissileManager;
import game.movement.Direction;
import game.movement.HomingPathFinder;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.Explosion;
import game.objects.enemies.Enemy;
import game.objects.missiles.Missile;
import image.objects.SpriteAnimation;

public class GuardianDrone extends FriendlyObject {

	FriendlyEnums friendlyType;
	int attackSpeedCooldown;
	int attackFrameCount;

	public GuardianDrone(int x, int y, Point destination, Direction rotation, FriendlyEnums friendlyType, float scale,
			PathFinder pathFinder, int attackSpeedCooldown) {
		super(x, y, destination, rotation, friendlyType, scale, pathFinder);
		this.attackSpeedCooldown = attackSpeedCooldown;
	}

	public void activateGuardianDrone() {
		switch (friendlyType) {
		case Absorbtion_Guardian_Bot:
			break;
		case Explosion_Guardian_Bot:
			createExplosion();
			break;
		case Homing_Missile_Guardian_Bot:
			fireHomingMissile();
			break;
		default:
			break;
		}
	}

	private void createExplosion() {
		if (attackFrameCount >= attackSpeedCooldown) {
			SpriteAnimation explosionAnimation = new SpriteAnimation(this.getCenterXCoordinate(),
					this.getCenterYCoordinate(), ImageEnums.Destroyed_Explosion, false, 1);
			explosionAnimation.setFrameDelay(2);
			int explosionDamage = 5;
			Explosion explosion = new Explosion(this.getCenterXCoordinate(), this.getCenterYCoordinate(), 1,
					explosionAnimation, explosionDamage, true);
			ExplosionManager.getInstance().addExistingExplosion(explosion);
			attackFrameCount = 0;
		} else {
			attackFrameCount++;
		}
	}

	private void fireHomingMissile() {
		if (attackFrameCount >= attackSpeedCooldown) {
			PathFinder pathFinder = new HomingPathFinder();
			MissileManager.getInstance().addFriendlyMissile(this.getCenterXCoordinate(), this.getCenterYCoordinate(),
					ImageEnums.Default_Player_Engine_Boosted, ImageEnums.Impact_Explosion_One, Direction.NONE, 1,
					pathFinder);

		} else {
			attackFrameCount++;
		}
	}

}
