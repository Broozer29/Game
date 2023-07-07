package game.objects.enemies;

import java.util.ArrayList;
import java.util.List;

import data.audio.AudioEnums;
import data.image.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.movement.RegularPathFinder;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileManager;

public class Bomba extends Enemy {

	private PathFinder missilePathFinder;
	private List<Direction> missileDirections = new ArrayList<Direction>();

	public Bomba(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder) {
		super(x, y, destination, rotation, EnemyEnums.Bomba, scale, pathFinder);
		loadImage(ImageEnums.Bomba);
		setExhaustanimation(ImageEnums.Bomba_Normal_Exhaust);
		setDeathAnimation(ImageEnums.Bomba_Destroyed_Explosion);
		this.exhaustAnimation.setFrameDelay(3);
		this.deathAnimation.setFrameDelay(4);
		this.initBoardBlockSpeeds();
		this.hitPoints = 100;
		this.maxHitPoints = 100;
		this.attackSpeedFrameCount = 200;
		this.XMovementSpeed = 2;
		this.YMovementSpeed = 1;
		this.hasAttack = true;
		this.showHealthBar = true;
		this.deathSound = AudioEnums.Large_Ship_Destroyed;
		this.setVisible(true);
		this.setRotation(rotation);
		this.missilePathFinder = new RegularPathFinder();
		this.initDirectionFromRotation();
	}

	private void initBoardBlockSpeeds() {
		this.boardBlockSpeeds.add(0, 1);
		this.boardBlockSpeeds.add(1, 1);
		this.boardBlockSpeeds.add(2, 1);
		this.boardBlockSpeeds.add(3, 2);
		this.boardBlockSpeeds.add(4, 2);
		this.boardBlockSpeeds.add(5, 2);
		this.boardBlockSpeeds.add(6, 3);
		this.boardBlockSpeeds.add(7, 3);
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
				missileManager.addExistingMissile(MissileCreator.getInstance().createEnemyMissile(xCoordinate,
						yCoordinate + +this.height / 2, ImageEnums.Bomba_Missile, ImageEnums.Bomba_Missile_Explosion,
						direction, scale, missilePathFinder, xMovementSpeed, yMovementSpeed, (float) 7.5));
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