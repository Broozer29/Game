package data.movement;

public class RegularPath extends Path {

	public RegularPath(String pathDirection, int stepSize, int moduloDivider, int stepsToTake) {
		super("Regular" , pathDirection, stepSize, moduloDivider);
		this.stepsToTake = stepsToTake;
		this.stepsTaken = 0;
	}

}
