package data;

import game.objects.enemies.Enemy;
import game.objects.missiles.Missile;

public class DirectionCoordinator {

	private static DirectionCoordinator instance = new DirectionCoordinator();
	
	private DirectionCoordinator(){
		
	}
	
	public static DirectionCoordinator getInstance() {
		return instance;
	}
	
	public int getEnemyAngleModulo(Enemy enemy) {
		return 0;
	}
	
	public int getMissileAngleModulo(Missile missile) {
		return 0;
	}
	
}
