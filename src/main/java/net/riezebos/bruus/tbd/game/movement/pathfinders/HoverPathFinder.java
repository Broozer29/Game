package net.riezebos.bruus.tbd.game.movement.pathfinders;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.*;

import java.util.ArrayList;
import java.util.List;

public class HoverPathFinder implements PathFinder {

    private double gameSecondsSinceEmptyList;
    private double secondsToHoverStill = 5;
    private boolean shouldDecreaseBoardBlock;
    private int decreaseBoardBlockAmountBy;
    private boolean isHovering;


    public Path findPath (GameObject gameObject) {
        MovementConfiguration config = gameObject.getMovementConfiguration();
        // Generate a random end point within the specified board block
        Point endPoint = getRandomCoordinateInBlock(config.getBoardBlockToHoverIn(), gameObject.getWidth(), gameObject.getHeight());
        Point start = new Point(gameObject.getXCoordinate(), gameObject.getYCoordinate());

        // Calculate the path to the randomly chosen end point
        List<Point> pathList = calculatePath(start, endPoint, config.getXMovementSpeed(), config.getYMovementSpeed());

        return new Path(pathList, config.getRotation());
    }

    @Override
    public Direction getNextStep (GameObject gameObject, Direction fallbackDirection) {
        //Not needed
        return null;
    }

    private List<Point> calculatePath(Point start, Point end, float maxStepSizeX, float maxStepSizeY) {
        List<Point> pathList = new ArrayList<>();
        pathList.add(start);
        if(maxStepSizeX == 0 && maxStepSizeY == 0){
            return pathList;
        }

        int totalDistanceX = end.getX() - start.getX();
        int totalDistanceY = end.getY() - start.getY();
        int stepsToEndpoint = (int) Math.max(Math.abs(totalDistanceX) / maxStepSizeX, Math.abs(totalDistanceY) / maxStepSizeY);

        float stepSizeX = totalDistanceX / (float) stepsToEndpoint;
        float stepSizeY = totalDistanceY / (float) stepsToEndpoint;

        for (int i = 1; i <= stepsToEndpoint; i++) {
            float nextX = start.getX() + stepSizeX * i;
            float nextY = start.getY() + stepSizeY * i;
            pathList.add(new Point(nextX, nextY));
        }
        return pathList;
    }


    private Point getRandomCoordinateInBlock (int blockIndex, int objectWidth, int objectHeight) {
        return BoardBlockUpdater.getRandomCoordinateInBlock(blockIndex, objectWidth, objectHeight);
    }

    @Override
    public boolean shouldRecalculatePath (GameObject gameObject) {
        Path path = gameObject.getMovementConfiguration().getCurrentPath();


        if (path == null) {
            // Path is null, should recalculate immediately.
            return true;
        }

        if (path.getWaypoints().isEmpty()) {
            if (gameSecondsSinceEmptyList == 0) {
                // Only set the timestamp if it hasn't been set yet. If true, stay still on the current location and rotate it
                gameSecondsSinceEmptyList = GameState.getInstance().getGameSeconds();
                isHovering = true;
                if (allowRotation(gameObject)) {
                    gameObject.setAllowedVisualsToRotate(true);
                    gameObject.rotateObjectTowardsRotation(true);
                    gameObject.setAllowedVisualsToRotate(false);
                }

                if (shouldDecreaseBoardBlock) {
                    int newBoardBlock = gameObject.getMovementConfiguration().getBoardBlockToHoverIn();
                    newBoardBlock -= decreaseBoardBlockAmountBy;
                    if (newBoardBlock < 1 || newBoardBlock > 8) {
                        gameObject.getMovementConfiguration().setPathFinder(new RegularPathFinder());
                    } else {
                        gameObject.getMovementConfiguration().setBoardBlockToHoverIn(newBoardBlock);
                    }
                }


            }

            // Check if 3 seconds have passed since the path became empty. If true, move
            if (GameState.getInstance().getGameSeconds() > gameSecondsSinceEmptyList + secondsToHoverStill) {
                // After 3 seconds, allow recalculation.
                isHovering = false;
                gameSecondsSinceEmptyList = 0; // Reset the timer for the next use.

                if (allowRotation(gameObject)) {
                    gameObject.setAllowedVisualsToRotate(true);
                }
                return true;
            }

            // If not enough time has passed, do not recalculate yet.

            return false;
        }

        // If the path is not empty, reset the timer as it's not relevant in this case.
        gameSecondsSinceEmptyList = 0;
        return false;
    }

    private boolean allowRotation(GameObject gameObject){
        if (gameObject instanceof Enemy) {
            Enemy enemy = (Enemy) gameObject;
            if(enemy.getMissileTypePathFinders() != null) {
                if (enemy.getMissileTypePathFinders().equals(PathFinderEnums.StraightLine) ||
                        enemy.getMissileTypePathFinders().equals(PathFinderEnums.Homing)) {
                    return false;
                }
            }

            if(enemy.getEnemyType().getEnemyCategory().equals(EnemyCategory.Boss)){
                return false;
            }
        }

        return true;
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

    public boolean isShouldDecreaseBoardBlock () {
        return shouldDecreaseBoardBlock;
    }

    public void setShouldDecreaseBoardBlock (boolean shouldDecreaseBoardBlock) {
        this.shouldDecreaseBoardBlock = shouldDecreaseBoardBlock;
    }

    public int getDecreaseBoardBlockAmountBy () {
        return decreaseBoardBlockAmountBy;
    }

    public void setDecreaseBoardBlockAmountBy (int decreaseBoardBlockAmountBy) {
        this.decreaseBoardBlockAmountBy = decreaseBoardBlockAmountBy;
    }

    public double getSecondsToHoverStill () {
        return secondsToHoverStill;
    }

    public void setSecondsToHoverStill (double secondsToHoverStill) {
        this.secondsToHoverStill = secondsToHoverStill;
    }

    public boolean isHovering () {
        return isHovering;
    }

    public double getGameSecondsSinceEmptyList () {
        return gameSecondsSinceEmptyList;
    }
}
