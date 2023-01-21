package data.movement;

public class RegularTrajectory extends Trajectory {

	public RegularTrajectory(String trajectoryDirection, int stepSize, boolean infiniteMovement, int angleModuloSize,
			int totalDistance) {
		super(trajectoryDirection, "Regular", stepSize, infiniteMovement, angleModuloSize);
		this.totalDistance = totalDistance;
		createRegularPath();
	}

	// Called when needed to create a straight or angled line trajectory
	private void createRegularPath() {
		Path newPath = null;
		if (trajectoryDirection.equals("Left") || trajectoryDirection.equals("Right")
				|| trajectoryDirection.equals("Up") || trajectoryDirection.equals("Down")) {
			int stepsToTake = totalDistance / stepSize;
			newPath = new RegularPath(trajectoryDirection, stepSize, 0, stepsToTake);

		} else if (trajectoryDirection.equals("LeftUp") || trajectoryDirection.equals("RightUp")
				|| trajectoryDirection.equals("LeftDown") || trajectoryDirection.equals("RightDown")) {
			int stepsToTake = totalDistance / stepSize;
			newPath = new RegularPath(trajectoryDirection, stepSize, angleModuloSize, stepsToTake);
		}
		addPath(newPath);
		setPath();
	}

}
