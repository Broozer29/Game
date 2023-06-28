package game.managers;

import java.util.ArrayList;
import java.util.List;

import data.PlayerStats;
import game.objects.friendlies.spaceship.SpaceShip;

public class PlayerManager {

	private static PlayerManager instance = new PlayerManager();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private PlayerStats playerStats = PlayerStats.getInstance();
	private SpaceShip spaceship;
	private List<Integer> playerCoordinatesList = new ArrayList<Integer>();

	private boolean playerAlive;

	private PlayerManager() {
		initSpaceShip();
	}

	public static PlayerManager getInstance() {
		return instance;
	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		initSpaceShip();
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

	private void checkPlayerHealth() {
		if (playerStats.getHitpoints() <= 0) {
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
		animationManager.addExhaustAnimation(playerStats.getExhaustAnimation());
		this.playerAlive = true;
	}

	public List<Integer> getNearestFriendlyHomingCoordinates() {
		
		playerCoordinatesList.set(0,spaceship.getCenterXCoordinate());
		playerCoordinatesList.set(1,spaceship.getCenterYCoordinate());

		return playerCoordinatesList;
	}

}