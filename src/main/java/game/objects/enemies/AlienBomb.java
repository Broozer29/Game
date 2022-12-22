package game.objects.enemies;

import data.movement.Trajectory;

public class AlienBomb extends Enemy {

	public AlienBomb(int x, int y, String direction, int angleModuloDivider, float scale) {
		super(x, y, direction, "Alien Bomb", scale);
		loadImage("Alien Bomb");
		setDeathAnimation("Alien Bomb Explosion");
		this.deathAnimation.setFrameDelay(2);
		this.initBoardBlockSpeeds();
		this.hitPoints = 10;
		this.angleModuloDivider = angleModuloDivider;
		this.maxHitPoints = 10;
		this.attackSpeedFrameCount = 999999;
		this.movementSpeed = 1;
		this.deathSound = "Destroyed Explosion";
		this.hasAttack = false;
		this.showHealthBar = false;
		this.trajectory = new Trajectory(direction, totalDistance(), movementSpeed, angleModuloDivider, true);
		this.setVisible(true);
		this.setRotation(direction);
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
