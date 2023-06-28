package game.managers;

import game.objects.Explosion;
import game.objects.enemies.Enemy;
import game.objects.friendlies.FriendlyObject;
import game.objects.friendlies.spaceship.SpaceShip;
import game.objects.friendlies.spaceship.SpecialAttack;
import game.objects.missiles.Missile;

public class MovementInitiator {
	
	private static MovementInitiator instance = new MovementInitiator();
	
	private MovementInitiator() {

	}
	
	public static MovementInitiator getInstance() {
		return instance;
	}
	
	public void moveEnemy(Enemy enemy) {
		enemy.move();
	}
	
	public void moveMissile(Missile missile) {
		missile.move();
	}
	
	public void moveFriendlyObjects(FriendlyObject friendly) {
		friendly.move();
	}
	
	public void moveSpecialAttacks(SpecialAttack specialAttack) {
		specialAttack.moveSpecialAttackMissiles();
		
	}
	
	public void movePlayerFollowingExplosion(Explosion explosion, SpaceShip spaceShip) {
		explosion.setX(spaceShip.getXCoordinate());
		explosion.setY(spaceShip.getYCoordinate());
		explosion.getAnimation().setX(spaceShip.getXCoordinate());
		explosion.getAnimation().setY(spaceShip.getYCoordinate());
	}
	
	public void movePlayerFollowingSpecialAttack(SpecialAttack specialAttack, SpaceShip spaceShip) {
		specialAttack.setX(spaceShip.getXCoordinate());
		specialAttack.setY(spaceShip.getYCoordinate());
		specialAttack.getAnimation().setX(spaceShip.getXCoordinate());
		specialAttack.getAnimation().setY(spaceShip.getYCoordinate());
	}
	
}