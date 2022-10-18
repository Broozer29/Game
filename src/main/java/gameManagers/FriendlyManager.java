package gameManagers;

import gameObjectes.SpaceShip;

public class FriendlyManager {

	private static FriendlyManager instance = new FriendlyManager();
	private SpaceShip spaceship;
	private boolean playerAlive;

	private FriendlyManager() {
		initSpaceShip();
	}

	public static FriendlyManager getInstance() {
		return instance;
	}

	public void updateGameTick() {
		updateSpaceShipMovement();
		checkPlayerHealth();
	}

	public SpaceShip getSpaceship() {
		return this.spaceship;
	}
	
	public boolean getPlayerStatus() {
		return this.playerAlive;
	}

	private void checkPlayerHealth() {
		if (spaceship.getHitpoints() <= 0) {
			this.playerAlive = false;
			spaceship.setVisible(false);
		}
	}

	private void updateSpaceShipMovement() {
		if (spaceship.isVisible()) {
			spaceship.move();
		}
	}

	private void initSpaceShip() {
		this.spaceship = new SpaceShip();
		this.playerAlive = true;
	}

}
