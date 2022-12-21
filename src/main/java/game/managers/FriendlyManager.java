package game.managers;

import game.objects.friendlies.SpaceShip;

public class FriendlyManager {

	private static FriendlyManager instance = new FriendlyManager();
	private SpaceShip spaceship;
	private String playerMissileType;
	private boolean playerAlive;

	private FriendlyManager() {
		initSpaceShip();
		this.playerMissileType = "Player Laserbeam";
	}

	public static FriendlyManager getInstance() {
		return instance;
	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		spaceship = null;
		initSpaceShip();
		this.playerMissileType = "Player Laserbeam";
		//True because the player has to start true?
		playerAlive = true;
	}
	
	public void updateGameTick() {
		updateSpaceShipMovement();
		checkPlayerHealth();
		spaceship.updateGameTick();
	}

	public SpaceShip getSpaceship() {
		return this.spaceship;
	}

	public boolean getPlayerStatus() {
		return this.playerAlive;
	}

	public String getPlayerMissileType() {
		return this.playerMissileType;
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
