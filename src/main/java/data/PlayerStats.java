package data;

import data.image.enums.ImageEnums;
import game.movement.PathFinder;
import game.movement.RegularPathFinder;
import visual.objects.SpriteAnimation;

public class PlayerStats {

	private static PlayerStats instance = new PlayerStats();

	private PlayerStats() {
		initDefaultSettings();
	}

	public static PlayerStats getInstance() {
		return instance;
	}

	// Player damage
	private float defaultAttackDamage;
	private float bonusAttackDamage;
	private float defaultSpecialAttackDamage;
	private float bonusSpecialAttackDamage;
	private float attackSpeed;
	private float bonusAttackSpeed;
	private float specialAttackSpeed;
	private float bonusSpecialAttackSpeed;

	// Player Health
	private float maxHitPoints;
	private float maxShieldHitPoints;
	private float hitpoints;
	private float shieldHitpoints;
	private float shieldRegenDelay;

	// Movement
	private int currentMovementSpeed;
	private int defaultMovementSpeed;
	private int bonusMovementSpeed;
	private int defaultBoostedMovementSpeed;
	private int boostedBonusMovementSpeed;

	// Visual aspects of player
	private ImageEnums currentExhaust;
	private ImageEnums spaceShipImage;
	private ImageEnums exhaustImage;
	private ImageEnums defaultEngineType;
	private ImageEnums boostedEngineType;
	private ImageEnums playerEMPType;
	private SpriteAnimation exhaustAnimation;

	// Player "homing" coordinate box
	private float homingRectangleResizeScale;
	private int homingRectangleXCoordinate;
	private int homingRectangleYCoordinate;
	private int homingRectangleWidth;
	private int homingRectangleHeight;

	// Player missile type & visuals
	private ImageEnums playerMissileType;
	private ImageEnums playerMissileImpactType;
	private float missileImpactScale;
	private float missileScale;
	private PathFinder missilePathFinder;

	public void initDefaultSettings() {
		setHitpoints(100);
		setMaxHitPoints(100);
		setShieldHitpoints(100);
		setMaxShieldHitPoints(100);
		setMovementSpeed(2);
		addBonusMovementSpeed(0);
		setDefaultBoostedMovementSpeed(4);
		addBoostedBonusMovementSpeed(0);
		setAttackSpeed(15);
		setSpecialAttackSpeed(100);
		setShieldRegenDelay(300);
		setHomingRectangleResizeScale((float) 1.5);
		setMissilePathFinder(new RegularPathFinder());
		setSpaceShipImage(ImageEnums.Player_Spaceship_Model_3);
		setExhaustImage(ImageEnums.Default_Player_Engine);
		setPlayerMissileType(ImageEnums.Player_Laserbeam);
		setPlayerMissileImpactType(ImageEnums.Impact_Explosion_One);
		setMissileScale(1);
		setMissileImpactScale(1);
		setDefaultEngineType(ImageEnums.Default_Player_Engine);
		setBoostedEngineType(ImageEnums.Default_Player_Engine_Boosted);
		setPlayerEMPType(ImageEnums.Player_EMP);
	}

	public float getNormalAttackDamage() {
		float attackDamage = this.defaultAttackDamage + this.bonusAttackDamage;
		if (attackDamage < 1) {
			return 1;
		} else {
			return attackDamage;
		}
	}

	public void setPlayerDamage(float playerDamage) {
		this.defaultAttackDamage = playerDamage;
	}

	public float getHitpoints() {
		return hitpoints;
	}

	public void setHitpoints(float hitpoints) {
		this.hitpoints = hitpoints;
	}

	public float getShieldHitpoints() {
		return shieldHitpoints;
	}

	public void setShieldHitpoints(float shieldHitpoints) {
		this.shieldHitpoints = shieldHitpoints;
	}

	public float getAttackSpeed() {
		float currentAttackSpeed = this.attackSpeed + bonusAttackSpeed;
		if (currentAttackSpeed < 1) {
			return 1;
		} else {
			return currentAttackSpeed;
		}
	}

	public void setAttackSpeed(float attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	public float getShieldRegenDelay() {
		return shieldRegenDelay;
	}

	public void setShieldRegenDelay(float shieldRegenDelay) {
		this.shieldRegenDelay = shieldRegenDelay;
	}

	public float getSpecialAttackSpeed() {
		float currentSpecialAttackSpeed = this.specialAttackSpeed + bonusSpecialAttackSpeed;
		if (currentSpecialAttackSpeed < 1) {
			return 1;
		} else {
			return currentSpecialAttackSpeed;
		}
	}

	public void setSpecialAttackSpeed(float specialAttackSpeed) {
		this.specialAttackSpeed = specialAttackSpeed;
	}

	public float getMaxHitPoints() {
		return maxHitPoints;
	}

	public void setMaxHitPoints(float maxHitPoints) {
		this.maxHitPoints = maxHitPoints;
	}

	public float getMaxShieldHitPoints() {
		return maxShieldHitPoints;
	}

	public void setMaxShieldHitPoints(float maxShieldHitPoints) {
		this.maxShieldHitPoints = maxShieldHitPoints;
	}

	public int getMovementSpeed() {
		return defaultMovementSpeed;
	}

	public void setMovementSpeed(int movementSpeed) {
		if (movementSpeed > 0) {
			this.defaultMovementSpeed = movementSpeed;
		}
	}

	public ImageEnums getCurrentExhaust() {
		return currentExhaust;
	}

	public void setCurrentExhaust(ImageEnums currentExhaust) {
		this.currentExhaust = currentExhaust;
	}

	public SpriteAnimation getExhaustAnimation() {
		return exhaustAnimation;
	}

	public void setExhaustAnimation(SpriteAnimation exhaustAnimation) {
		this.exhaustAnimation = exhaustAnimation;
	}

	public float getHomingRectangleResizeScale() {
		return homingRectangleResizeScale;
	}

	public void setHomingRectangleResizeScale(float homingRectangleResizeScale) {
		this.homingRectangleResizeScale = homingRectangleResizeScale;
	}

	public int getHomingRectangleXCoordinate() {
		return homingRectangleXCoordinate;
	}

	public void setHomingRectangleXCoordinate(int homingRectangleXCoordinate) {
		this.homingRectangleXCoordinate = homingRectangleXCoordinate;
	}

	public int getHomingRectangleYCoordinate() {
		return homingRectangleYCoordinate;
	}

	public void setHomingRectangleYCoordinate(int homingRectangleYCoordinate) {
		this.homingRectangleYCoordinate = homingRectangleYCoordinate;
	}

	public int getHomingRectangleWidth() {
		return homingRectangleWidth;
	}

	public void setHomingRectangleWidth(int homingRectangleWidth) {
		this.homingRectangleWidth = homingRectangleWidth;
	}

	public int getHomingRectangleHeight() {
		return homingRectangleHeight;
	}

	public void setHomingRectangleHeight(int homingRectangleHeight) {
		this.homingRectangleHeight = homingRectangleHeight;
	}

	public PathFinder getMissilePathFinder() {
		return missilePathFinder;
	}

	public void setMissilePathFinder(PathFinder missilePathFinder) {
		this.missilePathFinder = missilePathFinder;
	}

	public ImageEnums getSpaceShipImage() {
		return spaceShipImage;
	}

	public void setSpaceShipImage(ImageEnums spaceShipImage) {
		this.spaceShipImage = spaceShipImage;
	}

	public ImageEnums getExhaustImage() {
		return exhaustImage;
	}

	public void setExhaustImage(ImageEnums exhaustImage) {
		this.exhaustImage = exhaustImage;
	}

	public void changeHitPoints(float change) {
		this.hitpoints += change;
		if (this.hitpoints > maxHitPoints) {
			this.hitpoints = maxHitPoints;
		}
	}

	public void changeShieldHitpoints(float change) {
		this.shieldHitpoints += change;
		if (this.shieldHitpoints > maxShieldHitPoints) {
			this.shieldHitpoints = maxShieldHitPoints;
		}
	}

	public ImageEnums getPlayerMissileType() {
		return playerMissileType;
	}

	public void setPlayerMissileType(ImageEnums playerMissileType) {
		this.playerMissileType = playerMissileType;
	}

	public ImageEnums getPlayerMissileImpactType() {
		return playerMissileImpactType;
	}

	public void setPlayerMissileImpactType(ImageEnums playerMissileImpactType) {
		this.playerMissileImpactType = playerMissileImpactType;
	}

	public float getMissileImpactScale() {
		return missileImpactScale;
	}

	public void setMissileImpactScale(float missileImpactScale) {
		this.missileImpactScale = missileImpactScale;
	}

	public ImageEnums getDefaultEngineType() {
		return defaultEngineType;
	}

	public void setDefaultEngineType(ImageEnums defaultEngineType) {
		this.defaultEngineType = defaultEngineType;
	}

	public ImageEnums getBoostedEngineType() {
		return boostedEngineType;
	}

	public void setBoostedEngineType(ImageEnums boostedEngineType) {
		this.boostedEngineType = boostedEngineType;
	}

	public ImageEnums getPlayerEMPType() {
		return playerEMPType;
	}

	public void setPlayerEMPType(ImageEnums playerEMPType) {
		this.playerEMPType = playerEMPType;
	}

	public float getMissileScale() {
		return missileScale;
	}

	public void setMissileScale(float missileScale) {
		this.missileScale = missileScale;
	}

	public int getBonusMovementSpeed() {
		return bonusMovementSpeed;
	}

	public void addBonusMovementSpeed(int bonusMovementSpeed) {
		this.bonusMovementSpeed += bonusMovementSpeed;
	}

	public int getBoostedMovementSpeed() {
		return defaultBoostedMovementSpeed;
	}

	public void setDefaultBoostedMovementSpeed(int boostedMovementSpeed) {
		this.defaultBoostedMovementSpeed = boostedMovementSpeed;
	}

	public int getBoostedBonusMovementSpeed() {
		return boostedBonusMovementSpeed;
	}

	public void addBoostedBonusMovementSpeed(int boostedBonusMovementSpeed) {
		this.boostedBonusMovementSpeed += boostedBonusMovementSpeed;
	}

	public int getCurrentMovementSpeed(boolean boosted) {
		currentMovementSpeed = 0;
		if (boosted) {
			currentMovementSpeed = defaultBoostedMovementSpeed + boostedBonusMovementSpeed;
		} else {
			currentMovementSpeed = defaultMovementSpeed + bonusMovementSpeed;
		}

		if (currentMovementSpeed < 1) {
			return 1;
		} else
			return currentMovementSpeed;

	}

	public void setCurrentMovementSpeed(int currentMovementSpeed) {
		this.currentMovementSpeed = currentMovementSpeed;
	}

	public float getBonusAttackSpeed() {
		return bonusAttackSpeed;
	}

	// Negative value increase attack speed, positive values decrease attack speed
	public void addBonusAttackSpeed(float bonusAttackSpeed) {
		this.bonusAttackSpeed += bonusAttackSpeed;
	}

	public float getBonusSpecialAttackSpeed() {
		return bonusSpecialAttackSpeed;
	}

	// Negative value increase attack speed, positive values decrease attack speed
	public void addBonusSpecialAttackSpeed(float bonusSpecialAttackSpeed) {
		this.bonusSpecialAttackSpeed += bonusSpecialAttackSpeed;
	}

	public float getBonusDefaultAttackDamage() {
		return bonusAttackDamage;
	}

	public void addBonusDefaultAttackDamage(float bonusDefaultAttackDamage) {
		this.bonusAttackDamage += bonusDefaultAttackDamage;
	}

	public float getSpecialAttackDamage() {
		float specialAttackDamage = defaultSpecialAttackDamage + bonusSpecialAttackDamage;
		if (specialAttackDamage < 0) {
			return (float) 0.5;
		} else {
			return specialAttackDamage;
		}
	}

	public void setDefaultSpecialAttackDamage(float defaultSpecialAttackDamage) {
		this.defaultSpecialAttackDamage = defaultSpecialAttackDamage;
	}

	public float getBonusDefaultSpecialAttackDamage() {
		return bonusSpecialAttackDamage;
	}

	public void addBonusSpecialAttackDamage(float bonusDefaultSpecialAttackDamage) {
		this.bonusSpecialAttackDamage += bonusDefaultSpecialAttackDamage;
	}

}
