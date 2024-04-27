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

	protected MissileTypeEnums missileType;
	protected boolean destroysMissiles;

	public Missile(SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, movementConfiguration);
		initMissile(missileConfiguration);
		if (movementConfiguration != null) {
			initMovementConfiguration(movementConfiguration);
		}
	}

	public Missile(SpriteAnimationConfiguration spriteAnimationConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration){
		super(spriteAnimationConfiguration, movementConfiguration);
		initMissile(missileConfiguration);
		if (movementConfiguration != null) {
			initMovementConfiguration(movementConfiguration);
		}
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

	public boolean isDestroysMissiles () {
		return destroysMissiles;
	}

	public void setDestroysMissiles (boolean destroysMissiles) {
		this.destroysMissiles = destroysMissiles;
	}

	public MissileTypeEnums getMissileType () {
		return missileType;
	}

	public void setMissileType (MissileTypeEnums missileType) {
		this.missileType = missileType;
	}
}