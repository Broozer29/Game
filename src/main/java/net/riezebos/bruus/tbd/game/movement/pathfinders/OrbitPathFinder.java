package net.riezebos.bruus.tbd.game.movement.pathfinders;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.Drones.Drone;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Path;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;

import java.util.ArrayList;
import java.util.List;

public class OrbitPathFinder implements PathFinder {

    private GameObject target;

    private int radius;
    private int totalFrames;
    private double offsetAngle; // NEW: Angle offset for this drone

    public OrbitPathFinder (GameObject target, int radius, int totalFrames, double offsetAngle) {
        this.target = target;
        this.radius = radius;
        this.totalFrames = totalFrames;
        this.offsetAngle = offsetAngle;
    }

    @Override
    public Path findPath (GameObject gameObject) {


        MovementConfiguration orbitConfig = gameObject.getMovementConfiguration();
        Direction fallbackDirection = orbitConfig.getRotation();
        double radius = orbitConfig.getOrbitRadius(); // Use the radius from the config
        int totalFrames = orbitConfig.getOrbitSpeed(); // Use the total frames from the config

        int maximumSteps = 300;
        if(gameObject instanceof Drone){
            maximumSteps = 20000;
        }

        double angleStep = Math.PI * 2 / totalFrames;
        List<Point> waypoints = new ArrayList<>();

        // Determine the angle for the starting point relative to the target
        double startAngle = Math.atan2(
                (gameObject.getYCoordinate() + gameObject.getHeight() / 2.0) - (target.getYCoordinate() + target.getHeight() / 2.0),
                (gameObject.getXCoordinate() + gameObject.getWidth() / 2.0) - (target.getXCoordinate() + target.getWidth() / 2.0)
        );

        // Generate waypoints that include a smooth transition from start to orbit
        // Precompute constant values
        double centerX = target.getXCoordinate() + target.getWidth() / 2.0;
        double centerY = target.getYCoordinate() + target.getHeight() / 2.0;
        double gameObjectHalfWidth = gameObject.getWidth() / 2.0;
        double gameObjectHalfHeight = gameObject.getHeight() / 2.0;

        // Initialize waypoints with an appropriate size
        waypoints = new ArrayList<>(maximumSteps);

        for (int i = 0; i < maximumSteps; i++) {
            double angle = angleStep * i + startAngle; // Start angle relative to the initial position

            // Efficiently calculate x and y
            int x = (int) (centerX + Math.cos(angle) * radius - gameObjectHalfWidth);
            int y = (int) (centerY + Math.sin(angle) * radius - gameObjectHalfHeight);

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
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();

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

    public void adjustPathForTargetMovement (Path path, int targetDeltaX, int targetDeltaY) {
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

    public int getFrames () {
        return this.totalFrames;
    }
}
