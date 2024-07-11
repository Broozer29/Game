package game.gameobjects.enemies.enemytypes;

import java.util.ArrayList;
import java.util.List;

import VisualAndAudioData.audio.enums.AudioEnums;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.deprecatedpathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.gameobjects.enemies.EnemyConfiguration;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.missiles.*;
import VisualAndAudioData.image.ImageEnums;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Bomba extends Enemy {

	private List<Direction> missileDirections = new ArrayList<Direction>();

	public Bomba(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, enemyConfiguration, movementConfiguration);

		SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 3, false);
		destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bomba_Destroyed_Explosion);
		this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

		//Specialized behaviour configuration stuff
		this.initDirectionFromRotation();
		this.damage = MissileEnums.BombaProjectile.getDamage();
	}

	public void fireAction() {
		double currentTime = GameStateInfo.getInstance().getGameSeconds();
		if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
			if (!chargingUpAttackAnimation.isPlaying()) {
				chargingUpAttackAnimation.refreshAnimation();
				AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
			}

			if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
				shootMissile();
				lastAttackTime = currentTime; // Update the last attack time after firing
			}
		}
	}

	private void shootMissile(){
		for (Direction direction : missileDirections) {
			//Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
			SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,ImageEnums.Bomba_Missile
			,this.scale);


			//Create missile movement attributes and create a movement configuration
			MissileEnums missileType = MissileEnums.BombaProjectile;
			PathFinder missilePathFinder = new RegularPathFinder();
			MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
			MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
					missileType.getxMovementSpeed(), missileType.getyMovementSpeed(), missilePathFinder, movementPatternSize, direction
			);


			//Create remaining missile attributes and a missile configuration
			boolean isFriendly = false;
			int maxHitPoints = 35;
			int maxShields = 0;
			AudioEnums deathSound = null;
			boolean allowedToDealDamage = true;
			String objectType = "Bomba Missile";

			MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
					deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType, false,
					true, true, true);


			//Create the missile and finalize the creation process, then add it to the manager and consequently the game
			Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
			missile.setOwnerOrCreator(this);
			missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
			missile.rotateGameObjectTowards(missile.getMovementConfiguration().getDestination().getX(), missile.getMovementConfiguration().getDestination().getY(), true);
			missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
			missile.resetMovementPath();
			missile.setAllowedVisualsToRotate(false);
			this.missileManager.addExistingMissile(missile);
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