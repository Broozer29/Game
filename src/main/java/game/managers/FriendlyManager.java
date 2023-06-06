package game.managers;

import java.util.ArrayList;
import java.util.List;

import data.PlayerStats;
import data.image.enums.ImageEnums;
import game.objects.friendlies.SpaceShip;

public class FriendlyManager {

	private static FriendlyManager instance = new FriendlyManager();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private PlayerStats playerStats = PlayerStats.getInstance();
	private SpaceShip spaceship;

	private boolean playerAlive;

	private FriendlyManager() {
		initSpaceShip();
	}

	public static FriendlyManager getInstance() {
		return instance;
	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		spaceship = null;
		initSpaceShip();
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
		List<Integer> coordinatesList = new ArrayList<Integer>();
		coordinatesList.add(spaceship.getCenterXCoordinate());
		coordinatesList.add(spaceship.getCenterYCoordinate());

		return coordinatesList;
	}

}
