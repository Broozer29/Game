package game.movement;

import game.objects.Explosion;
import game.objects.enemies.Enemy;
import game.objects.friendlies.FriendlyObject;
import game.objects.friendlies.spaceship.SpaceShip;
import game.objects.friendlies.spaceship.specialAttacks.SpecialAttack;
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
		enemy.updateCurrentBoardBlock();
	}

	public void moveMissile(Missile missile) {
		missile.move();
		missile.updateCurrentBoardBlock();
	}

	public void moveFriendlyObjects(FriendlyObject friendly) {
		friendly.move();
		friendly.updateCurrentBoardBlock();
	}

	public void moveSpecialAttacks(SpecialAttack specialAttack) {
		specialAttack.moveSpecialAttackMissiles();
		specialAttack.updateCurrentBoardBlock();
	}

	public void movePlayerFollowingExplosion(Explosion explosion, SpaceShip spaceShip) {
		if (explosion.centeredAroundPlayer()) {
			explosion.setCenterCoordinates(spaceShip.getCenterXCoordinate(), spaceShip.getCenterYCoordinate());
			explosion.getAnimation().setCenterCoordinates(spaceShip.getCenterXCoordinate(), spaceShip.getCenterYCoordinate());
		} else {
			explosion.setX(spaceShip.getXCoordinate());
			explosion.setY(spaceShip.getYCoordinate());
			explosion.getAnimation().setX(spaceShip.getXCoordinate());
			explosion.getAnimation().setY(spaceShip.getYCoordinate());
		}
		explosion.updateCurrentBoardBlock();
		explosion.getAnimation().setAnimationBounds(explosion.getAnimation().getXCoordinate(), explosion.getAnimation().getYCoordinate());
		explosion.getAnimation().updateCurrentBoardBlock();
	}

	public void movePlayerFollowingSpecialAttack(SpecialAttack specialAttack, SpaceShip spaceShip) {
		if (specialAttack.centeredAroundPlayer()) {
			specialAttack.setCenterCoordinates(spaceShip.getCenterXCoordinate(), spaceShip.getCenterYCoordinate());
			specialAttack.getAnimation().setCenterCoordinates(spaceShip.getCenterXCoordinate(), spaceShip.getCenterYCoordinate());
		} else {
			specialAttack.setX(spaceShip.getXCoordinate());
			specialAttack.setY(spaceShip.getYCoordinate());
			specialAttack.getAnimation().setX(spaceShip.getXCoordinate());
			specialAttack.getAnimation().setY(spaceShip.getYCoordinate());
		}
		specialAttack.updateCurrentBoardBlock();
		specialAttack.getAnimation().setAnimationBounds(specialAttack.getAnimation().getXCoordinate(), specialAttack.getAnimation().getYCoordinate());
		specialAttack.getAnimation().updateCurrentBoardBlock();
	}

}