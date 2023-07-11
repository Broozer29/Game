package data;

import data.image.ImageEnums;
import game.movement.PathFinder;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.friendlies.spaceship.PlayerSpecialAttackTypes;
import game.playerpresets.GunPreset;
import game.playerpresets.SpecialGunPreset;
import visual.objects.SpriteAnimation;

public class PlayerStats {

	private static PlayerStats instance = new PlayerStats();

	private PlayerStats() {
		initDefaultSettings();
	}

	public static PlayerStats getInstance() {
		return instance;
	}

	public void resetPlayerStats() {
		initDefaultSettings();
		if (normalGunPreset != null) {
			normalGunPreset.loadPreset();
		} else {
			System.out.println("No normal gun preset was loaded! If this happens there has been an oversight in code.");
		}
		
		if(specialGunPreset != null) {
			specialGunPreset.loadPreset();
		} else {
			System.out.println("No special gun preset was loaded! If this happens there has been an oversight in code.");
		}
	}

	// Preset type
	private GunPreset normalGunPreset;
	private SpecialGunPreset specialGunPreset;

	// Player attacks
	private PlayerAttackTypes attackType = PlayerAttackTypes.Laserbeam;
	private PlayerSpecialAttackTypes specialAttackType = PlayerSpecialAttackTypes.EMP;
	private float attackDamage;
	private float bonusAttackDamage;
	private float specialAttackDamage;
	private float bonusSpecialAttackDamage;
	private float attackSpeed;
	private float bonusAttackSpeed;
	private float specialAttackSpeed;
	private float bonusSpecialAttackSpeed;

	private int flameThrowerMaxSteps;
	private int flameThrowerBonusMaxSteps;
	private int firewallSize;
	private int firewallSpeed;
	private float firewallDamage;

	// Player Health
	private float maxHitPoints;
	private float maxShieldHitPoints;
	private float hitpoints;
	private float shieldHitpoints;
	private float shieldRegenDelay;

	// Movement
	private int currentMovementSpeed;
	private int MovementSpeed;
	private int bonusMovementSpeed;
	private int BoostedMovementSpeed;
	private int boostedBonusMovementSpeed;

	// Visual aspects of player
	private ImageEnums currentExhaust;
	private ImageEnums spaceShipImage;
	private ImageEnums exhaustImage;
	private ImageEnums engineType;
	private ImageEnums boostedEngineType;
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
		// Health
		setHitpoints(1000000);
		setMaxHitPoints(1000000);
		setShieldHitpoints(100);
		setMaxShieldHitPoints(100);
		setShieldRegenDelay(300);

		// Movement speed
		setMovementSpeed(2);
		addBonusMovementSpeed(0);
		setBoostedMovementSpeed(4);
		addBoostedBonusMovementSpeed(0);

		// Special attack
		setSpecialAttackDamage(5);
		setSpecialAttackSpeed(100);

		// HomingRectangle target size (the larger, the quicker homing missiles lose lock)
		setHomingRectangleResizeScale((float) 1.5);

		// Visuals
		setSpaceShipImage(ImageEnums.Player_Spaceship_Model_3);
		setExhaustImage(ImageEnums.Default_Player_Engine);
		setEngineType(ImageEnums.Default_Player_Engine);
		setBoostedEngineType(ImageEnums.Default_Player_Engine_Boosted);

	}

	public float getNormalAttackDamage() {
		float attackDamage = this.attackDamage + this.bonusAttackDamage;
		if (attackDamage < 1) {
			return 1;
		} else {
			return attackDamage;
		}
	}

	public void setPlayerDamage(float playerDamage) {
		this.attackDamage = playerDamage;
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

	public void setAttackDamage(float attackDamage) {
		this.attackDamage = attackDamage;
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
		return MovementSpeed;
	}

	public void setMovementSpeed(int movementSpeed) {
		if (movementSpeed > 0) {
			this.MovementSpeed = movementSpeed;
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

	public ImageEnums getEngineType() {
		return engineType;
	}

	public void setEngineType(ImageEnums engineType) {
		this.engineType = engineType;
	}

	public ImageEnums getBoostedEngineType() {
		return boostedEngineType;
	}

	public void setBoostedEngineType(ImageEnums boostedEngineType) {
		this.boostedEngineType = boostedEngineType;
	}

	public PlayerSpecialAttackTypes getPlayerSpecialAttackType() {
		return specialAttackType;
	}

	public void setPlayerSpecialAttackType(PlayerSpecialAttackTypes playerEMPType) {
		this.specialAttackType = playerEMPType;
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
		return BoostedMovementSpeed;
	}

	public void setBoostedMovementSpeed(int boostedMovementSpeed) {
		this.BoostedMovementSpeed = boostedMovementSpeed;
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
			currentMovementSpeed = BoostedMovementSpeed + boostedBonusMovementSpeed;
		} else {
			currentMovementSpeed = MovementSpeed + bonusMovementSpeed;
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

	public float getBonusAttackDamage() {
		return bonusAttackDamage;
	}

	public void addBonusAttackDamage(float bonusAttackDamage) {
		this.bonusAttackDamage += bonusAttackDamage;
	}

	public float getSpecialAttackDamage() {
		float tempspecialAttackDamage = specialAttackDamage + bonusSpecialAttackDamage;
		if (tempspecialAttackDamage < 0.5) {
			return (float) 0.5;
		} else {
			return tempspecialAttackDamage;
		}
	}

	public void setSpecialAttackDamage(float SpecialAttackDamage) {
		this.specialAttackDamage = SpecialAttackDamage;
	}

	public float getBonusSpecialAttackDamage() {
		return bonusSpecialAttackDamage;
	}

	public void addBonusSpecialAttackDamage(float bonusSpecialAttackDamage) {
		this.bonusSpecialAttackDamage += bonusSpecialAttackDamage;
	}

	public PlayerAttackTypes getAttackType() {
		return attackType;
	}

	public void setAttackType(PlayerAttackTypes attackType) {
		this.attackType = attackType;
	}

	public int getFlameThrowerMaxSteps() {
		int attackRange = flameThrowerMaxSteps + flameThrowerBonusMaxSteps;
		if (attackRange < 1) {
			return 1;
		}
		return flameThrowerMaxSteps;
	}

	public void setFlameThrowerMaxSteps(int flameThrowerAttackRange) {
		this.flameThrowerMaxSteps = flameThrowerAttackRange;
	}

	public int getFlameThrowerBonusMaxSteps() {

		return flameThrowerBonusMaxSteps;
	}

	public void addFlameThrowerBonusMaxRange(float flameThrowerBonusAttackRange) {
		this.flameThrowerBonusMaxSteps += flameThrowerBonusAttackRange;
	}

	public int getFirewallSize() {
		return firewallSize;
	}

	public void setFirewallSize(int firewallSize) {
		this.firewallSize = firewallSize;
	}

	public float getFirewallDamage() {
		return firewallDamage;
	}

	public void setFirewallDamage(float firewallDamage) {
		this.firewallDamage = firewallDamage;
	}

	public int getFirewallSpeed() {
		return firewallSpeed;
	}

	public void setFirewallSpeed(int firewallSpeed) {
		this.firewallSpeed = firewallSpeed;
	}
	
	public GunPreset getNormalGunPreset() {
		return normalGunPreset;
	}

	public void setNormalGunPreset(GunPreset normalGunPreset) {
		this.normalGunPreset = normalGunPreset;
	}

	public SpecialGunPreset getSpecialGunPreset() {
		return specialGunPreset;
	}

	public void setSpecialGunPreset(SpecialGunPreset specialGunPreset) {
		this.specialGunPreset = specialGunPreset;
	}

}