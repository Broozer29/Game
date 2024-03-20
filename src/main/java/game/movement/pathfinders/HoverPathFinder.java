package game.movement.pathfinders;

import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.HoverPathFinderConfig;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.util.BoardBlockUpdater;

import java.util.ArrayList;
import java.util.List;

public class HoverPathFinder implements PathFinder{

    public Path findPath(PathFinderConfig pathFinderConfig) {
        if (!(pathFinderConfig instanceof HoverPathFinderConfig)) {
            throw new IllegalArgumentException("Expected HoverPathFinderConfig");
        }

        HoverPathFinderConfig config = (HoverPathFinderConfig) pathFinderConfig;

        // Generate a random end point within the specified board block
        Point endPoint = getRandomCoordinateInBlock(config.getBoardBlockToHoverIn(), config.getWidth(), config.getHeight());

        // Calculate the path to the randomly chosen end point
        List<Point> pathList = calculatePath(config.getStart(), endPoint, config.getxMovementSpeed(), config.getyMovementSpeed());

        return new Path(pathList, config.getFallbackDirection(), false, config.isFriendly());
    }

    @Override
    public Direction getNextStep (Point currentLocation, Path path, Direction fallbackDirection) {
        //Not needed
        return null;
    }

    private List<Point> calculatePath(Point start, Point end, int maxStepSizeX, int maxStepSizeY) {
        List<Point> pathList = new ArrayList<>();
        pathList.add(start);

        int totalDistanceX = end.getX() - start.getX();
        int totalDistanceY = end.getY() - start.getY();
        int stepsToEndpoint = Math.max(Math.abs(totalDistanceX) / maxStepSizeX, Math.abs(totalDistanceY) / maxStepSizeY);

        double stepSizeX = totalDistanceX / (double) stepsToEndpoint;
        double stepSizeY = totalDistanceY / (double) stepsToEndpoint;

        for (int i = 1; i <= stepsToEndpoint; i++) {
            int nextX = start.getX() + (int) (stepSizeX * i);
            int nextY = start.getY() + (int) (stepSizeY * i);
            pathList.add(new Point(nextX, nextY));
        }

        return pathList;
    }

    private Point getRandomCoordinateInBlock(int blockIndex, int objectWidth, int objectHeight) {
        // Assuming BoardBlockUpdater.getRandomCoordinateInBlock is implemented as discussed
        return BoardBlockUpdater.getRandomCoordinateInBlock(blockIndex, objectWidth, objectHeight);
    }

    @Override
    public boolean shouldRecalculatePath(Path path) {
        return path == null || path.getWaypoints().isEmpty();
    }

    @Override
    public Point calculateInitialEndpoint (Point start, Direction rotation, boolean friendly) {
        //Not needed
        return null;
    }

    @Override
    public Point calculateEndPointBySteps (Point start, Direction rotation, int steps, int xMovementspeed, int yMovementspeed) {
        //Not needed
        return null;
    }
}
