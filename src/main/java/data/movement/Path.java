package data.movement;

import java.util.List;

public class Path {
	private List<Point> waypoints;

	public Path(List<Point> wayPoints) {
		this.waypoints = wayPoints;
	}

	public List<Point> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Point> waypoints) {
		this.waypoints = waypoints;
	}

	// constructor, getters, setters, etc.
}