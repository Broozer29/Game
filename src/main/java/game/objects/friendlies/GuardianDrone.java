package game.objects.friendlies;

import data.image.ImageEnums;
import game.managers.AnimationManager;
import game.managers.ExplosionManager;
import game.movement.Direction;
import game.movement.HomingPathFinder;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.Explosion;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileManager;
import visual.objects.SpriteAnimation;

public class GuardianDrone extends FriendlyObject {

	private FriendlyEnums guardianType;
	private int attackSpeedCooldown;
	private int attackFrameCount;

	public GuardianDrone(int x, int y, Point destination, Direction rotation, FriendlyEnums friendlyType, float scale,
			PathFinder pathFinder, int attackSpeedCooldown) {
		super(x, y, destination, rotation, friendlyType, scale, pathFinder);
		this.attackSpeedCooldown = attackSpeedCooldown;
		this.guardianType = friendlyType;
		SpriteAnimation anim = new SpriteAnimation(x, y, ImageEnums.Guardian_Bot, true, scale);
		this.animation = anim;
	}

	public void activateGuardianDrone() {
		switch (guardianType) {
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
			MissileManager missileManager = MissileManager.getInstance();

			int xMovementSpeed = 5;
			int yMovementSpeed = 2;
			missileManager.addExistingMissile(MissileCreator.getInstance().createFriendlyMissile(
					this.getCenterXCoordinate(), this.getCenterYCoordinate(), ImageEnums.Player_Laserbeam,
					ImageEnums.Impact_Explosion_One, Direction.RIGHT, 1, pathFinder, xMovementSpeed, yMovementSpeed,
					PlayerAttackTypes.Laserbeam));
			attackFrameCount = 0;

		} else {
			attackFrameCount++;
		}
	}

}