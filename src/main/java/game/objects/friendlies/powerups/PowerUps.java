package game.objects.friendlies.powerups;

public enum PowerUps {
	
	
	//Permanent upgrades from a store or smth
	DOUBLE_SHOT,
	TRIPLE_SHOT,
	
	//Temporary powerups acquired in-game
	INCREASED_MOVEMENT_SPEED,
	INCREASED_NORMAL_DAMAGE,
	INCREASED_SPECIAL_DAMAGE,
	INCREASED_NORMAL_ATTACK_SPEED,
	INCREASED_SPECIAL_ATTACK_SPEED,
	HEALTH_AND_SHIELD_RESTORE,
	
	
	//For the use of generating random powerup effects
	RANDOM,
	
	//For debugging uses
	DUMMY_DO_NOT_USE
	
}