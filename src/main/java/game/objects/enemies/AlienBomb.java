package game.objects.enemies;

import java.util.List;

import data.DataClass;

public class AlienBomb extends Enemy {

	public AlienBomb(int x, int y, String direction, int scale) {
		super(x, y, direction, scale);
		loadImage("Alien Bomb");
		this.initBoardBlockSpeeds();
		this.hitPoints = 10;
		this.maxHitPoints = 10;
		this.attackSpeedFrameCount = 999999;
		this.movementSpeed = 1;
		this.deathSound = "Destroyed Explosion";
		this.hasAttack = false;
		this.showHealthBar = false;
		this.trajectory.setEnemyTrajectoryType(this);
		this.setVisible(true);
		this.setRotation(direction);
	}
	
	private void initBoardBlockSpeeds() {
		this.boardBlockSpeeds.add(0,1);
		this.boardBlockSpeeds.add(1,1);
		this.boardBlockSpeeds.add(2,1);
		this.boardBlockSpeeds.add(3,1);
		this.boardBlockSpeeds.add(4,1);
		this.boardBlockSpeeds.add(5,1);
		this.boardBlockSpeeds.add(6,1);
		this.boardBlockSpeeds.add(7,1);
	}

}
