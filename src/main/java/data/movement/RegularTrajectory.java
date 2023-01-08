package data.movement;

import java.util.List;

public class RegularTrajectory extends Trajectory {
	private PathFactory pathFactory = PathFactory.getInstance();

	public RegularTrajectory(String trajectoryDirection, int stepSize, boolean infiniteMovement,
			int angleModuloSize, int totalDistance) {
		super(trajectoryDirection, "Regular", stepSize, infiniteMovement, angleModuloSize);
		this.totalDistance = totalDistance;
		createRegularPath();
	}

	// Called when needed to create a straight or angled line trajectory
	private void createRegularPath() {
		Path newPath = null;
		if (trajectoryDirection.equals("Left") || trajectoryDirection.equals("Right")
				|| trajectoryDirection.equals("Up") || trajectoryDirection.equals("Down")) {
			newPath = pathFactory.getStraightLine(trajectoryDirection, totalDistance, stepSize);

		} else if (trajectoryDirection.equals("LeftUp") || trajectoryDirection.equals("RightUp")
				|| trajectoryDirection.equals("LeftDown") || trajectoryDirection.equals("RightDown")) {
			newPath = pathFactory.getAngledLine(trajectoryDirection, totalDistance, stepSize, angleModuloSize);
		}
		addPath(newPath);
		setPath();
	}
}
