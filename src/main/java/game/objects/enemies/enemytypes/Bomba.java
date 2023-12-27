package game.objects.enemies.enemytypes;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.missiles.*;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;
import visual.objects.SpriteAnimation;

public class Bomba extends Enemy {

	private PathFinder missilePathFinder;
	private List<Direction> missileDirections = new ArrayList<Direction>();

	public Bomba(SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
		super(spriteConfiguration, enemyConfiguration);

		//The correct imageenum can be gotten from a method in enemyenums like the BGO enum method
		//Below is sloppy and temporary
		SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
		exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bomba_Normal_Exhaust);
		this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

		SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
		destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bomba_Destroyed_Explosion);
		this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

		//Specialized behaviour configuration stuff
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
		int xMovementSpeed = 4;
		int yMovementSpeed = 2;
		if (attackSpeedCurrentFrameCount >= attackSpeed) {

			for (Direction direction : missileDirections) {
				SpriteConfiguration missileSpriteConfiguration = this.spriteConfiguration;
				missileSpriteConfiguration.setyCoordinate(yCoordinate + this.height / 2);
				missileSpriteConfiguration.setImageType(ImageEnums.Bomba_Missile);

				MissileConfiguration missileConfiguration = new MissileConfiguration(MissileTypeEnums.BombaProjectile,
						100, 100, null, ImageEnums.Bomba_Missile_Explosion, isFriendly()
						, new RegularPathFinder(), direction, xMovementSpeed,yMovementSpeed, true
						, "Bomba Missile", (float) 7.5);


				Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration);
				
				
				if(missileDirections.contains(Direction.DOWN)) {
					newMissile.rotateGameObject(Direction.DOWN);
				} else if(missileDirections.contains(Direction.LEFT)) {
					newMissile.rotateGameObject(Direction.LEFT);
				} else if(missileDirections.contains(Direction.RIGHT)) {
					newMissile.rotateGameObject(Direction.RIGHT);
				} else if(missileDirections.contains(Direction.UP)) {
					newMissile.rotateGameObject(Direction.UP);
				}
				
				missileManager.addExistingMissile(newMissile);

			}

			attackSpeedCurrentFrameCount = 0;
		}
		if (attackSpeedCurrentFrameCount < attackSpeed) {
			this.attackSpeedCurrentFrameCount++;
		}
	}

	private void initDirectionFromRotation() {
		switch (this.movementConfiguration.getRotation()) {
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