package data.movement;

public class HomingTrajectory extends Trajectory {

	public HomingTrajectory(String trajectoryDirection, int stepSize, boolean infiniteMovement, int angleModuloSize) {
		super(trajectoryDirection, "Homing", stepSize, infiniteMovement, angleModuloSize);
		lostTargetLock = false;
		createHomingPath();
	}

	private void createHomingPath() {
		Path newPath = new HomingPath(trajectoryDirection, stepSize, angleModuloSize);
		addPath(newPath);
		setPath();
	}

}
