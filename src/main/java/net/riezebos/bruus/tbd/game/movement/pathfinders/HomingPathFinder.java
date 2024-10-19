package net.riezebos.bruus.tbd.game.movement.pathfinders;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Path;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visuals.audiodata.DataClass;

import java.util.ArrayList;
import java.util.List;

public class HomingPathFinder implements PathFinder {
    @Override
    public Path findPath (GameObject gameObject) {
            Point start = new Point(gameObject.getXCoordinate(), gameObject.getYCoordinate());
            Direction fallbackDirection = gameObject.getMovementConfiguration().getRotation();

            List<Point> waypoints = new ArrayList<>();
            waypoints.add(start);
            Path path = new Path(waypoints, fallbackDirection);
            path.setHoming(true);
            path.setFriendly(gameObject.isFriendly());

            return new Path(waypoints, fallbackDirection);
    }

    @Override
    public Direction getNextStep (GameObject gameObject, Direction fallbackDirection) {
        Path path = gameObject.getMovementConfiguration().getCurrentPath();
        Point currentLocation = gameObject.getCurrentLocation();


        if (shouldRecalculatePath(gameObject)) {
            return fallbackDirection;
        } else {
            Direction calculatedDirection;
            if (path.getHomingTargetLocation() == null) {
                calculatedDirection = fallbackDirection;
                return calculatedDirection;
            }
            calculatedDirection = calculateDirection(currentLocation, path.getHomingTargetLocation());
            if (calculatedDirection == null) {
                calculatedDirection = fallbackDirection;
            }
            return calculatedDirection;
        }
    }

    public boolean shouldRecalculatePath (GameObject gameObject) {
        Path currentPath = gameObject.getMovementConfiguration().getCurrentPath();

        if(currentPath == null){
            return true;
        }

        boolean hasPassed = false;
        if (currentPath.isFriendly() && !EnemyManager.getInstance().enemiesToHomeTo()) {
            hasPassed = hasPassedTarget(currentPath);
        } else if (!currentPath.isFriendly()) {
            hasPassed = hasPassedTarget(currentPath);
        } else if (currentPath.isFriendly() && EnemyManager.getInstance().enemiesToHomeTo()) {
            hasPassed = true;
        }
        return hasPassed;
    }

    private boolean hasPassedTarget (Path currentPath) {
        Point currentLocation = currentPath.getCurrentLocation();
        Point targetLocation = null;
        if (currentPath.getHomingTargetLocation() == null) {
            GameObject target = getTarget(currentPath.isFriendly(), currentLocation.getX(), currentLocation.getY());
            targetLocation = new Point(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        } else {
            targetLocation = currentPath.getHomingTargetLocation();
        }

        switch (currentPath.getFallbackDirection()) {
            case UP:
                return currentLocation.getY() < targetLocation.getY();
            case DOWN:
                return currentLocation.getY() > targetLocation.getY();
            case LEFT:
                return currentLocation.getX() < targetLocation.getX();
            case RIGHT:
                return currentLocation.getX() > targetLocation.getX();
            case LEFT_UP:
                return currentLocation.getX() < targetLocation.getX() || currentLocation.getY() < targetLocation.getY();
            case RIGHT_UP:
                return currentLocation.getX() > targetLocation.getX() || currentLocation.getY() < targetLocation.getY();
            case LEFT_DOWN:
                return currentLocation.getX() < targetLocation.getX() || currentLocation.getY() > targetLocation.getY();
            case RIGHT_DOWN:
                return currentLocation.getX() > targetLocation.getX() || currentLocation.getY() > targetLocation.getY();
            default:
                return false;
        }

    }

    private Direction calculateDirection (Point current, Point target) {
        if (target == null) {
            return Direction.RIGHT;
        }
        int dx = target.getX() - current.getX();
        int dy = target.getY() - current.getY();

        if (dx > 0) {
            if (dy > 0) {
                return Direction.RIGHT_DOWN;
            } else if (dy < 0) {
                return Direction.RIGHT_UP;
            } else {
                return Direction.RIGHT;
            }
        } else if (dx < 0) {
            if (dy > 0) {
                return Direction.LEFT_DOWN;
            } else if (dy < 0) {
                return Direction.LEFT_UP;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (dy < 0) {
                return Direction.UP;
            } else if (dy > 0) {
                return Direction.DOWN;
            }
        }
        return null;
    }

    @Override
    public Point calculateInitialEndpoint (Point start, Direction rotation, boolean friendly) {
        if (friendly) {
            EnemyManager enemyManager = EnemyManager.getInstance();
            if (enemyManager.getClosestEnemy(start.getX(), start.getY()) == null) {
                int endXCoordinate = DataClass.getInstance().getWindowWidth();
                int endYCoordinate = PlayerManager.getInstance().getSpaceship().getCenterYCoordinate();
                return new Point(endXCoordinate, endYCoordinate);
            } else
                return enemyManager.getClosestEnemy(start.getX(), start.getY()).getCurrentLocation();
        } else {
            PlayerManager friendlyManager = PlayerManager.getInstance();
            int xCoordinate = friendlyManager.getNearestFriendlyHomingCoordinates().get(0);
            int yCoordinate = friendlyManager.getNearestFriendlyHomingCoordinates().get(1);
            return new Point(xCoordinate, yCoordinate);
        }
    }

    @Override
    public Point calculateEndPointBySteps (Point start, Direction rotation, int steps, int xMovementspeed,
                                           int yMovementspeed) {
        PlayerManager friendlyManager = PlayerManager.getInstance();
        int xCoordinate = friendlyManager.getNearestFriendlyHomingCoordinates().get(0);
        int yCoordinate = friendlyManager.getNearestFriendlyHomingCoordinates().get(1);
        return new Point(xCoordinate, yCoordinate);
    }

    public GameObject getTarget (boolean isFriendly, int xCoordinate, int yCoordinate) {
        if (isFriendly) {
            return EnemyManager.getInstance().getClosestEnemy(xCoordinate, yCoordinate);
        } else {
            return PlayerManager.getInstance().getSpaceship();
        }
    }


    //Custom method from SpriteMover specifically so homing projectiles can line up
    // Custom method for homing projectiles to align precisely with target center
    public Point calculateNextPoint (MovementConfiguration movementConfiguration, Direction direction, float XStepSize, float YStepSize, GameObject target) {
        float x = movementConfiguration.getCurrentLocation().getX();
        float y = movementConfiguration.getCurrentLocation().getY();
        int targetX = target.getCenterXCoordinate() - target.getWidth() / 2; // Adjust target coordinates for center alignment
        int targetY = target.getCenterYCoordinate() - target.getHeight() / 2;

        // Calculate the distance to target
        double distanceToTargetX = targetX - x;
        double distanceToTargetY = targetY - y;

        // Dynamically adjust step size based on distance to prevent overshooting
        double adjustedXStepSize = Math.abs(distanceToTargetX) < XStepSize ? Math.abs(distanceToTargetX) : XStepSize;
        double adjustedYStepSize = Math.abs(distanceToTargetY) < YStepSize ? Math.abs(distanceToTargetY) : YStepSize;

        // Move towards the target with adjusted step sizes
        x += Integer.signum((int) distanceToTargetX) * adjustedXStepSize;
        y += Integer.signum((int) distanceToTargetY) * adjustedYStepSize;

        return new Point(x, y);
    }


}