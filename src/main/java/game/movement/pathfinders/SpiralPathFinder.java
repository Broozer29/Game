package game.movement.pathfinders;

import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.SpiralPathFinderConfig;

import java.util.ArrayList;
import java.util.List;

public class SpiralPathFinder implements PathFinder {

    private static final int MAX_WAYPOINTS = 1000;

    @Override
    public Path findPath(PathFinderConfig pathFinderConfig) {
        if (!(pathFinderConfig instanceof SpiralPathFinderConfig)) {
            throw new IllegalArgumentException("Expected SpiralPathFinderConfig");
        }

        SpiralPathFinderConfig config = (SpiralPathFinderConfig) pathFinderConfig;
        Point start = config.getStart();
        double radius = config.getRadius();
        double radiusIncrement = config.getRadiusIncrement();
        int maxStepDistance = 5; // The maximum distance between points

        List<Point> waypoints = new ArrayList<>();
        double currentAngle = 0.0;

        while (waypoints.size() < MAX_WAYPOINTS) {
            int x = start.getX() + (int) (radius * Math.cos(currentAngle));
            int y = start.getY() + (int) (radius * Math.sin(currentAngle));

            waypoints.add(new Point(x, y));

            // Calculate the angle increment based on the circumference of the current circle
            // This is to ensure that the step distance approximates the maxStepDistance
            double circumference = 2 * Math.PI * radius;
            double angleStep = maxStepDistance / circumference * 2 * Math.PI;

            currentAngle += angleStep;
            currentAngle = currentAngle % (2 * Math.PI); // Normalize the angle
            radius += radiusIncrement; // Increment the radius for the next point
        }

        return new Path(waypoints, Direction.NONE, false, config.isFriendly());
    }




    @Override
    public Direction getNextStep (Point currentLocation, Path path, Direction fallbackDirection) {
        return null;
    }

    @Override
    public boolean shouldRecalculatePath (Path path) {
        if(path == null || path.getWaypoints().isEmpty()){
            return true;
        } else return false;
    }

    @Override
    public Point calculateInitialEndpoint (Point start, Direction rotation, boolean friendly) {
        return null;
    }

    @Override
    public Point calculateEndPointBySteps (Point start, Direction rotation, int steps, int xMovementspeed, int yMovementspeed) {
        return null;
    }

}
