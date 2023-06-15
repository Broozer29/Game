package game.managers;

import java.util.List;

import game.objects.enemies.Enemy;
import game.objects.missiles.Missile;

public class MovementManager {
	
	private static MovementManager instance = new MovementManager();
	
	private MovementManager() {

	}
	
	public static MovementManager getInstance() {
		return instance;
	}
	
	public void moveEnemy(Enemy enemy) {
		enemy.move();
	}
	
	public void moveMissile(Missile missile) {
		missile.move();
	}
	
}
