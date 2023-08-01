package game.objects.enemies;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.movement.RegularPathFinder;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileManager;
import game.objects.missiles.missiletypes.BombaProjectile;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;

public class Bomba extends Enemy {

	private PathFinder missilePathFinder;
	private List<Direction> missileDirections = new ArrayList<Direction>();

	public Bomba(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder,
			int xMovementSpeed, int yMovementSpeed) {
		super(x, y, destination, rotation, EnemyEnums.Bomba, scale, pathFinder, xMovementSpeed, yMovementSpeed);
		loadImage(ImageEnums.Bomba);
		setExhaustanimation(ImageEnums.Bomba_Normal_Exhaust);
		setDeathAnimation(ImageEnums.Bomba_Destroyed_Explosion);
		this.exhaustAnimation.setFrameDelay(1);
		this.hitPoints = 100;
		this.maxHitPoints = 100;
		this.attackSpeedFrameCount = 200;
		this.hasAttack = true;
		this.showHealthBar = true;
		this.deathSound = AudioEnums.Large_Ship_Destroyed;
		this.setVisible(true);
		this.setRotation(rotation);
		this.deathAnimation.rotateAnimetion(rotation);
		this.missilePathFinder = new RegularPathFinder();
		this.initDirectionFromRotation();
	}

	// Called every game tick. If weapon is not on cooldown, fire a shot.
	// Current board block attack is set to 7, this shouldnt be a hardcoded value
	// This function doesn't discern enemy types yet either, should be re-written
	// when new enemies are introduced
	public void fireAction() {
		if (missileManager == null) {
			missileManager = MissileManager.getInstance();
		}
		int xMovementSpeed = 5;
		int yMovementSpeed = 2;
		if (currentAttackSpeedFrameCount >= attackSpeedFrameCount) {

			for (Direction direction : missileDirections) {
				Missile newMissile = MissileCreator.getInstance().createEnemyMissile(xCoordinate,
						yCoordinate + this.height / 2, ImageEnums.Bomba_Missile, ImageEnums.Bomba_Missile_Explosion,
						direction, scale, missilePathFinder, xMovementSpeed, yMovementSpeed, (float) 7.5);
				
				
				if(missileDirections.contains(Direction.DOWN)) {
					newMissile.rotateMissileAnimation(Direction.DOWN);
				} else if(missileDirections.contains(Direction.LEFT)) {
					newMissile.rotateMissileAnimation(Direction.LEFT);
				} else if(missileDirections.contains(Direction.RIGHT)) {
					newMissile.rotateMissileAnimation(Direction.RIGHT);
				} else if(missileDirections.contains(Direction.UP)) {
					newMissile.rotateMissileAnimation(Direction.UP);
				}
				
				missileManager.addExistingMissile(newMissile);

			}

			currentAttackSpeedFrameCount = 0;
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}
	}

	private void initDirectionFromRotation() {
		switch (this.rotation) {
		case DOWN:
			missileDirections.add(Direction.LEFT_DOWN);
			missileDirections.add(Direction.DOWN);
			missileDirections.add(Direction.RIGHT_DOWN);
			break;
		case LEFT:
		case LEFT_DOWN:
		case LEFT_UP:
			missileDirections.add(Direction.LEFT_DOWN);
			missileDirections.add(Direction.LEFT);
			missileDirections.add(Direction.LEFT_UP);
			break;
		case NONE:
			missileDirections.add(Direction.LEFT);
			break;
		case RIGHT:
		case RIGHT_DOWN:
		case RIGHT_UP:
			missileDirections.add(Direction.RIGHT_UP);
			missileDirections.add(Direction.RIGHT);
			missileDirections.add(Direction.RIGHT_DOWN);
			break;
		case UP:
			missileDirections.add(Direction.LEFT_UP);
			missileDirections.add(Direction.UP);
			missileDirections.add(Direction.RIGHT_UP);
			break;
		default:
			missileDirections.add(Direction.LEFT);
			break;

		}
	}
}