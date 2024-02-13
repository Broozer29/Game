package game.objects.missiles;

import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Missile extends GameObject {

	private MissileTypeEnums missileType;

	public Missile(SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration);
		initMissile(missileConfiguration);
	}

	public Missile(SpriteAnimationConfiguration spriteAnimationConfiguration, MissileConfiguration missileConfiguration){
		super(spriteAnimationConfiguration);
		initMissile(missileConfiguration);
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
		this.boxCollision = missileConfiguration.isBoxCollision();

		if(missileConfiguration.getDestructionType() != null){
			SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
			destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
			this.destructionAnimation = new SpriteAnimation(destructionAnimation);
		}
		initMovementConfiguration(missileConfiguration);

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
			movementConfiguration.setTarget(((HomingPathFinder) pathFinder).getTarget(isFriendly(), this.xCoordinate, this.yCoordinate));
		}

		movementConfiguration.setDiamondWidth(missileConfiguration.getMovementPatternSize().getDiamondWidth());
		movementConfiguration.setDiamondHeight(missileConfiguration.getMovementPatternSize().getDiamondHeight());
		movementConfiguration.setStepsBeforeBounceInOtherDirection(missileConfiguration.getMovementPatternSize().getStepsBeforeBounceInOtherDirection());

		movementConfiguration.setAngleStep(0.1);
		movementConfiguration.setCurveDistance(1);
		movementConfiguration.setRadius(5);
		movementConfiguration.setRadiusIncrement(missileConfiguration.getMovementPatternSize().getRadiusIncrement());


		movementConfiguration.setPrimaryDirectionStepAmount(missileConfiguration.getMovementPatternSize().getPrimaryDirectionStepAmount());
		movementConfiguration.setFirstDiagonalDirectionStepAmount(missileConfiguration.getMovementPatternSize().getSecondaryDirectionStepAmount());
		movementConfiguration.setSecondDiagonalDirectionStepAmount(missileConfiguration.getMovementPatternSize().getSecondaryDirectionStepAmount());

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
			AnimationManager.getInstance().addUpperAnimation(destructionAnimation);
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