package gamedata;

public class BoostsUpgradesAndBuffsSettings {
	private boolean doubleShotActive;
	private boolean tripleShotActive;

	private int minTimeForPowerUpSpawn;
	private int maxTimeForPowerUpSpawn;

	private int temporaryPowerUpDuration;

	private float repairPackageHealthRestore;
	private float repairPackageShieldRestore;

	private int defaultMovementSpeedBoostAmount;
	private int defaultBoostedMovementSpeedBoostAmount;
	private int defaultAttackSpeedBonus;
	private int defaultSpecialAttackSpeedBonus;
	private int defaultAttackDamageBonus;
	private int defaultSpecialAttackDamageBonus;
	

	private PlayerStats playerStats = PlayerStats.getInstance();
	private static BoostsUpgradesAndBuffsSettings instance = new BoostsUpgradesAndBuffsSettings();

	
	//This class contains the values that powerups give to the playerStats
	private BoostsUpgradesAndBuffsSettings() {
		initDefaultSettings();
	}

	// The parameters of these should be gained from the players upgrade profile
	// Currently hardcoded because player upgrades dont exist yet
	public void initDefaultSettings() {
		playerStats = PlayerStats.getInstance();
		doubleShotActive = false;
		tripleShotActive = false;
		setMinTimeForPowerUpSpawn(750);
		setMaxTimeForPowerUpSpawn(1250);
		setRepairPackageHealthRestore(playerStats.getMaxHitPoints() / 2);
		setRepairPackageShieldRestore(playerStats.getMaxShieldHitPoints());
		setDefaultMovementSpeedBoostAmount(2);
		setDefaultBoostedMovementSpeedBoostAmount(2);
		setTemporaryPowerUpDuration(200);
		setDefaultAttackSpeedBonus(15);
		setDefaultSpecialAttackSpeedBonus(50);
		setDefaultAttackDamageBonus(10);
		setDefaultSpecialAttackDamageBonus(2);
	}

	public static BoostsUpgradesAndBuffsSettings getInstance() {
		return instance;
	}

	public boolean getDoubleShotActive() {
		return doubleShotActive;
	}

	public void setDoubleShotActive(boolean doubleShotActive) {
		this.doubleShotActive = doubleShotActive;
	}

	public boolean getTripleShotActive() {
		return tripleShotActive;
	}

	public void setTripleShotActive(boolean tripleShotActive) {
		this.tripleShotActive = tripleShotActive;
	}

	public int getMinTimeForPowerUpSpawn() {
		return minTimeForPowerUpSpawn;
	}

	public void setMinTimeForPowerUpSpawn(int minTimeForPowerUpSpawn) {
		this.minTimeForPowerUpSpawn = minTimeForPowerUpSpawn;
	}

	public int getMaxTimeForPowerUpSpawn() {
		return maxTimeForPowerUpSpawn;
	}

	public void setMaxTimeForPowerUpSpawn(int maxTimeForPowerUpSpawn) {
		this.maxTimeForPowerUpSpawn = maxTimeForPowerUpSpawn;
	}

	public int getTemporaryPowerUpDuration() {
		return temporaryPowerUpDuration;
	}

	public void setTemporaryPowerUpDuration(int temporaryPowerUpDuration) {
		this.temporaryPowerUpDuration = temporaryPowerUpDuration;
	}

	public float getRepairPackageHealthRestore() {
		return repairPackageHealthRestore;
	}

	public void setRepairPackageHealthRestore(float repairPackageHealthRestore) {
		this.repairPackageHealthRestore = repairPackageHealthRestore;
	}

	public float getRepairPackageShieldRestore() {
		return repairPackageShieldRestore;
	}

	public void setRepairPackageShieldRestore(float repairPackageShieldRestore) {
		this.repairPackageShieldRestore = repairPackageShieldRestore;
	}

	public int getDefaultMovementSpeedBoostAmount() {
		return defaultMovementSpeedBoostAmount;
	}

	public void setDefaultMovementSpeedBoostAmount(int movementSpeedBoostAmount) {
		this.defaultMovementSpeedBoostAmount = movementSpeedBoostAmount;
	}

	public int getDefaultBoostedMovementSpeedBoostAmount() {
		return defaultBoostedMovementSpeedBoostAmount;
	}

	public void setDefaultBoostedMovementSpeedBoostAmount(int defaultBoostedMovementSpeedBoostAmount) {
		this.defaultBoostedMovementSpeedBoostAmount = defaultBoostedMovementSpeedBoostAmount;
	}

	public int getDefaultAttackSpeedBonus() {
		return defaultAttackSpeedBonus;
	}

	public void setDefaultAttackSpeedBonus(int defaultAttackSpeedBonus) {
		this.defaultAttackSpeedBonus = defaultAttackSpeedBonus;
	}

	public int getDefaultSpecialAttackSpeedBonus() {
		return defaultSpecialAttackSpeedBonus;
	}

	public void setDefaultSpecialAttackSpeedBonus(int defaultSpecialAttackSpeedBonus) {
		this.defaultSpecialAttackSpeedBonus = defaultSpecialAttackSpeedBonus;
	}

	public int getDefaultAttackDamageBonus() {
		return defaultAttackDamageBonus;
	}

	public void setDefaultAttackDamageBonus(int defaultAttackDamageBonus) {
		this.defaultAttackDamageBonus = defaultAttackDamageBonus;
	}

	public int getDefaultSpecialAttackDamageBonus() {
		return defaultSpecialAttackDamageBonus;
	}

	public void setDefaultSpecialAttackDamageBonus(int defaultSpecialAttackDamageBonus) {
		this.defaultSpecialAttackDamageBonus = defaultSpecialAttackDamageBonus;
	}

	public void resetGameSettings() {
		initDefaultSettings();
	}

}