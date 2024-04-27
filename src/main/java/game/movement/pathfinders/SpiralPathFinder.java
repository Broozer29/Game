package game.movement.pathfinders;

import VisualAndAudioData.DataClass;
import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.SpiralPathFinderConfig;

import java.util.ArrayList;
import java.util.List;

public class SpiralPathFinder implements PathFinder {

    private static final int MAX_WAYPOINTS = 1000;
    private final int windowWidth;
    private final int playableWindowMinHeight;
    private final int playableWindowMaxHeight;

    public SpiralPathFinder () {
        this.windowWidth = DataClass.getInstance().getWindowWidth();
        this.playableWindowMinHeight = DataClass.getInstance().getPlayableWindowMinHeight();
        this.playableWindowMaxHeight = DataClass.getInstance().getPlayableWindowMaxHeight();
    }

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
        waypoints.add(start);
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

        return new Path(waypoints, Direction.LEFT, false, config.isFriendly());
    }




    @Override
    public Direction getNextStep (Point currentLocation, Path path, Direction fallbackDirection) {
        return null;
    }

    @Override
    public boolean shouldRecalculatePath (Path path) {
        return(path == null || path.getWaypoints().isEmpty());
    }

    @Override
    public Point calculateInitialEndpoint (Point start, Direction rotation, boolean friendly) {
        int endXCoordinate = 0;
        int endYCoordinate = 0;
        int xCoordinate = start.getX();
        int yCoordinate = start.getY();
        DataClass dataClass = DataClass.getInstance();

        // friendly is not used for regular paths
        switch (rotation) {
            case UP:
                endYCoordinate = this.playableWindowMinHeight - 150;
                endXCoordinate = xCoordinate;
                break;
            case DOWN:
                endYCoordinate = this.playableWindowMaxHeight + 150;
                endXCoordinate = xCoordinate;
                break;
            case LEFT:
                endYCoordinate = yCoordinate;
                endXCoordinate = 0 - 150;
                break;
            case RIGHT:
                endYCoordinate = yCoordinate;
                endXCoordinate = dataClass.getWindowWidth() + 150;
                break;
            case RIGHT_UP:
                endYCoordinate = this.playableWindowMinHeight -150;
                endXCoordinate = dataClass.getWindowWidth() + 150;
                break;
            case RIGHT_DOWN:
                endYCoordinate = this.playableWindowMaxHeight + 150;
                endXCoordinate = dataClass.getWindowWidth() + 150;
                break;
            case LEFT_UP:
                endYCoordinate = this.playableWindowMinHeight -150;
                endXCoordinate = 0 - 150;
                break;
            case LEFT_DOWN:
                endYCoordinate = this.playableWindowMaxHeight + 150;
                endXCoordinate = 0 - 150;
                break;
            default:
                endYCoordinate = yCoordinate;
                endXCoordinate = 0 + 150;
                break;
        }

        Point endPoint = new Point(endXCoordinate, endYCoordinate);
        return endPoint;
    }


    @Override
    public Point calculateEndPointBySteps (Point start, Direction rotation, int steps, int xMovementspeed, int yMovementspeed) {
        return null;
    }

}
