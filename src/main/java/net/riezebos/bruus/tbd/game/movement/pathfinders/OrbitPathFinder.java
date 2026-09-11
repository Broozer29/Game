package net.riezebos.bruus.tbd.game.movement.pathfinders;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Path;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;

import java.util.ArrayList;
import java.util.List;

public class OrbitPathFinder implements PathFinder {

    private GameObject target;
    private boolean reverse = false;

    public OrbitPathFinder (GameObject target) {
        this.target = target;
    }

    @Override
    public Path findPath (GameObject gameObject) {
        MovementConfiguration orbitConfig = gameObject.getMovementConfiguration();
        Direction fallbackDirection = orbitConfig.getRotation();
        double radius = orbitConfig.getOrbitRadius();
        float movementSpeed = orbitConfig.getMovementSpeed();

        // Calculate angle step based on movement speed and radius
        // Arc length = radius * angle, so angle = arc_length / radius
        // We want each step to move approximately 'movementSpeed' pixels along the arc
        double angleStep = movementSpeed / radius;

        // Calculate how many complete orbits we want to generate
        int numberOfOrbits = (gameObject instanceof Drone || gameObject instanceof Missile) ? 50 : 2;

        // Total angle to cover (multiple complete circles)
        double totalAngle = numberOfOrbits * Math.PI * 2;

        // Calculate the number of steps needed
        int maximumSteps = (int) Math.ceil(totalAngle / angleStep);

        // Determine the angle for the starting point relative to the target
        double startAngle = Math.atan2(
                gameObject.getCenterYCoordinate() - target.getCenterYCoordinate(),
                gameObject.getCenterXCoordinate() - target.getCenterXCoordinate()
        );

        // Precompute constant values
        double gameObjectHalfWidth = gameObject.getWidth() / 2.0;
        double gameObjectHalfHeight = gameObject.getHeight() / 2.0;

        // Initialize waypoints with an appropriate size
        List<Point> waypoints = new ArrayList<>(maximumSteps);

        for (int i = 0; i < maximumSteps; i++) {
            double angle = (reverse ? -angleStep : angleStep) * i + startAngle;

            // Calculate x and y coordinates on the orbit
            double x = (target.getCenterXCoordinate() + Math.cos(angle) * radius - gameObjectHalfWidth);
            double y = (target.getCenterYCoordinate() + Math.sin(angle) * radius - gameObjectHalfHeight);

            waypoints.add(new Point(x, y));
        }

        return new Path(waypoints, fallbackDirection);
    }


    @Override
    public Direction getNextStep (GameObject gameObject, Direction fallbackDirection) {
        MovementConfiguration moveconfig = gameObject.getMovementConfiguration();
        Point currentLocation = gameObject.getCurrentLocation();
        ;        // Get the list of waypoints from the path
        List<Point> waypoints = moveconfig.getCurrentPath().getWaypoints();

        // Check if there are any waypoints left. If not, return the fallback direction.
        if (waypoints.isEmpty()) {
            return fallbackDirection;
        }

        // Get the next waypoint
        Point nextWaypoint = waypoints.get(0);

        // Calculate the direction towards the next waypoint
        Direction direction = calculateDirection(currentLocation, nextWaypoint);

        // If the current location is already at the next waypoint,
        // remove the waypoint from the list and recursively call this function to get the next
        // direction.
        if (currentLocation.equals(nextWaypoint)) {
            waypoints.remove(0);
            return getNextStep(gameObject, fallbackDirection);
        }

        return direction;
    }

    public Direction calculateDirection (Point start, Point end) {
        float dx = end.getX() - start.getX();
        float dy = end.getY() - start.getY();

        // Fuck it, om een of andere reden is dy de tegenovergestelde richting.
        // Waarom dit is I dont fucking know maar het werkt op deze manier
        if (dx > 0) {
            if (dy < 0) {
                return Direction.RIGHT_UP;
            } else if (dy > 0) {
                return Direction.RIGHT_DOWN;
            } else {
                return Direction.RIGHT;
            }
        } else if (dx < 0) {
            if (dy < 0) {
                return Direction.LEFT_UP;
            } else if (dy > 0) {
                return Direction.LEFT_DOWN;
            } else {
                return Direction.LEFT;
            }
        } else {

            if (dy < 0) {
                return Direction.UP;
            } else if (dy > 0) {
                return Direction.DOWN;
            } else {
                return Direction.LEFT; // start and end are the same point
            }
        }
    }

    @Override
    public boolean shouldRecalculatePath (GameObject gameObject) {
        MovementConfiguration configuration = gameObject.getMovementConfiguration();
        return (configuration.getCurrentPath() == null || configuration.getCurrentPath().getWaypoints().isEmpty());
    }


    @Override
    public Point calculateInitialEndpoint (Point start, Direction rotation, boolean friendly) {
        // This method could return the first waypoint on the path.
        return start;
    }

    public void adjustPathForTargetMovement (Path path, float targetDeltaX, float targetDeltaY) {
        List<Point> waypoints = path.getWaypoints();
        for (Point waypoint : waypoints) {
            waypoint.setX(waypoint.getX() + targetDeltaX);
            waypoint.setY(waypoint.getY() + targetDeltaY);
        }
    }

    public Sprite getTarget () {
        return target;
    }

    @Override
    public Point calculateEndPointBySteps (Point start, Direction rotation, int steps, int xMovementspeed,
                                           int yMovementspeed) {
        // Should not be used for OrbitPathFinders
        return start;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
}
