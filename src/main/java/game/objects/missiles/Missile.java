package game.objects.missiles;

import game.managers.AnimationManager;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;
import visual.objects.SpriteAnimation;

public class Missile extends GameObject {

	private MissileTypeEnums missileType;

	public Missile(SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration);
		initMissile(missileConfiguration);

		SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
		this.animation = new SpriteAnimation(spriteAnimationConfiguration);


		SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
		destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
		this.destructionAnimation = new SpriteAnimation(destructionAnimation);

		initMovementConfiguration(missileConfiguration);
	}

	private void initMissile(MissileConfiguration missileConfiguration){
		this.friendly = missileConfiguration.isFriendly();
		this.maxHitPoints = missileConfiguration.getMaxHitPoints();
		this.maxShieldPoints = missileConfiguration.getMaxShields();
		this.deathSound = missileConfiguration.getDeathSound();
		this.allowedToDealDamage = missileConfiguration.isAllowedToDealDamage();
		this.objectType = missileConfiguration.getObjectType();
		this.damage = missileConfiguration.getDamage();
		this.missileType = missileConfiguration.getMissileType();

	}
	
	private void initMovementConfiguration(MissileConfiguration missileConfiguration) {
		PathFinder pathFinder = missileConfiguration.getPathfinder();
		movementConfiguration = new MovementConfiguration();
		movementConfiguration.setPathFinder(pathFinder);
		movementConfiguration.setCurrentLocation(currentLocation);
		movementConfiguration.setDestination(pathFinder.calculateInitialEndpoint(currentLocation, missileConfiguration.getMovementDirection(), isFriendly()));
		movementConfiguration.setRotation(missileConfiguration.getMovementDirection());
		movementConfiguration.setXMovementSpeed(missileConfiguration.getxMovementSpeed());
		movementConfiguration.setYMovementSpeed(missileConfiguration.getyMovementSpeed());
		movementConfiguration.setStepsTaken(0);
		movementConfiguration.setHasLock(true);

		if(pathFinder instanceof HomingPathFinder){
			this.objectToChase = ((HomingPathFinder) pathFinder).getTarget(isFriendly());
		}
	}

	public SpriteAnimation getAnimation() {
		if (this.animation != null) {
			return this.animation;
		}
		return null;
	}

	public void missileAction() {
		// Exists to be overriden
	}

	public void destroyMissile() {
		if (this.destructionAnimation != null) {
			AnimationManager.getInstance().addDestroyedExplosion(destructionAnimation);
		}
		this.setVisible(false);
	}

	public MissileTypeEnums getMissileType () {
		return missileType;
	}

	public void setMissileType (MissileTypeEnums missileType) {
		this.missileType = missileType;
	}
}