package game.objects.enemies;

import data.movement.Direction;
import data.movement.PathFinder;
import data.movement.Point;
import game.managers.MissileManager;

public class AlienBomb extends Enemy {

	public AlienBomb(int x, int y, Point destination, Direction rotation, float scale, PathFinder pathFinder) {
		super(x, y, destination, rotation, "Alien Bomb", scale, pathFinder);
		loadImage("Alien Bomb");
		setDeathAnimation("Alien Bomb Explosion");
		this.deathAnimation.setFrameDelay(2);
		this.initBoardBlockSpeeds();
		this.hitPoints = 10;
		this.maxHitPoints = 10;
		this.attackSpeedFrameCount = 999999;
		this.movementSpeed = 1;
		this.deathSound = "Destroyed Explosion";
		this.hasAttack = false;
		this.showHealthBar = false;
		this.setVisible(true);
		this.setRotation(rotation);
	}

	private void initBoardBlockSpeeds() {
		this.boardBlockSpeeds.add(0, 1);
		this.boardBlockSpeeds.add(1, 1);
		this.boardBlockSpeeds.add(2, 1);
		this.boardBlockSpeeds.add(3, 1);
		this.boardBlockSpeeds.add(4, 1);
		this.boardBlockSpeeds.add(5, 1);
		this.boardBlockSpeeds.add(6, 1);
		this.boardBlockSpeeds.add(7, 1);
	}

}
