package gameObjectes;

import java.util.List;

public class Trajectory {
	private int xCoordinate;
	private int yCoordinate;
	private float xDestination;
	private float yDestination;
	
	public Trajectory(List<Float> coordinatesList, int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.xDestination = coordinatesList.get(0);
		this.yDestination = coordinatesList.get(1);
	}
	
	
	private void calculateTrajectory(List<Float> coordinatesList) {
		float distanceBetweenX = xCoordinate - xDestination;
		float distanceBetweenY = yCoordinate - yDestination;
		
		float distanceXSteps = distanceBetweenX / coordinatesList.get(0);
		float distanceYSteps = distanceBetweenY / coordinatesList.get(1);
		
		System.out.println("distanceXSteps: " + distanceXSteps + " distanceYSteps " + distanceYSteps);
	}
}
