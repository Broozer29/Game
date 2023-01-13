package data.movement;

import java.util.ArrayList;
import java.util.List;

import game.managers.FriendlyManager;

public class HomingTrajectory extends Trajectory {

	private PathFactory pathFactory = PathFactory.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();

	public HomingTrajectory(String trajectoryDirection, int stepSize, boolean infiniteMovement, int angleModuloSize) {
		super(trajectoryDirection, "Homing", stepSize, infiniteMovement, angleModuloSize);
		lostTargetLock = false;
		createHomingPath();
	}

	// Called when needed to create a homing trajectory
	private void createHomingPath() {
		// Er zou niet steeds opnieuw een pad gemaakt moeten worden. Een homing missile
		// zou een eindeloos pad moeten zijn dat een fallback heeft na X stappen
		// of als het te dichtbij de speler komt
		List<Integer> destinationCoordinatesList = friendlyManager.getNearestFriendlyHomingCoordinates();
		int xCoordinateDestination = destinationCoordinatesList.get(0);
		int yCoordinateDestination = destinationCoordinatesList.get(1);
		Path newPath = pathFactory.getHomingPath(stepSize, friendly, trajectoryDirection, angleModuloSize);
		addPath(newPath);
		setPath();
	}

}
