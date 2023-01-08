package data.movement;

public class HomingPath extends Path {

	public HomingPath(String pathDirection, int stepSize, int moduloDivider) {
		super("Homing", pathDirection, stepSize, moduloDivider);
		this.initHomingPath(pathDirection);
	}

}
