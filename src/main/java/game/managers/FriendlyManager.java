package game.managers;

import java.util.ArrayList;
import java.util.List;

import game.objects.friendlies.SpaceShip;

public class FriendlyManager {

	private static FriendlyManager instance = new FriendlyManager();
	private AnimationManager animationManager = AnimationManager.getInstance();
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
		// True because the player has to start true?
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
		this.spaceship = new SpaceShip("Model 3 Better Model Upgrade", "Default Player Engine");
		animationManager.addExhaustAnimation(spaceship.getExhaustAnimation());
		this.playerAlive = true;
	}

	public List<Integer> getNearestFriendlyHomingCoordinates() {
		List<Integer> coordinatesList = new ArrayList<Integer>();
		coordinatesList.add(spaceship.getCenterXCoordinate());
		coordinatesList.add(spaceship.getCenterYCoordinate());

		return coordinatesList;
	}

}
