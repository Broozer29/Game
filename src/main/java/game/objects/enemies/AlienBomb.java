package game.objects.enemies;

import java.util.List;

import data.DataClass;

public class AlienBomb extends Enemy {

	public AlienBomb(int x, int y, String direction) {
		super(x, y, direction);
		loadImage("Alien Bomb");
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

	// Called every loop to move the enemy
	public void move() {
		List<Integer> newCoordsList = trajectory.getPathCoordinates(xCoordinate, yCoordinate);
		xCoordinate = newCoordsList.get(0);
		yCoordinate = newCoordsList.get(1);

		if (direction.equals("Up")) {
			if (yCoordinate <= 0) {
				this.setVisible(false);
			}
		} else if (direction.equals("Down")) {
			if (yCoordinate >= DataClass.getInstance().getWindowHeight()) {
				this.setVisible(false);
			}
		}
	}

}
