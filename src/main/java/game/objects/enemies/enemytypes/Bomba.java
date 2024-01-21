package game.objects.enemies.enemytypes;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.missiles.*;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Bomba extends Enemy {

	private List<Direction> missileDirections = new ArrayList<Direction>();

	public Bomba(SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
		super(spriteConfiguration, enemyConfiguration);

		SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
		exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bomba_Normal_Exhaust);
		this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

		SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, false);
		destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bomba_Destroyed_Explosion);
		this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

		//Specialized behaviour configuration stuff
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
		if (attackSpeedCurrentFrameCount >= attackSpeed) {

			for (Direction direction : missileDirections) {
				SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();

				MissileTypeEnums missileType = MissileTypeEnums.BombaProjectile;

				missileSpriteConfiguration.setxCoordinate(xCoordinate);
				missileSpriteConfiguration.setyCoordinate(yCoordinate + this.height / 2);
				missileSpriteConfiguration.setScale(this.scale);
				missileSpriteConfiguration.setImageType(missileType.getImageType());

				MissileConfiguration missileConfiguration = new MissileConfiguration(missileType,
						100, 100, null, missileType.getDeathOrExplosionImageEnum(), isFriendly()
						, new RegularPathFinder(), direction, missileType.getxMovementSpeed(),missileType.getyMovementspeed(), true
						, missileType.getObjectType(), missileType.getDamage(), MovementPatternSize.SMALL, missileType.isBoxCollision());


				Missile newMissile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration);
				newMissile.setOwnerOrCreator(this);
				
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