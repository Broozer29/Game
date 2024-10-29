package net.riezebos.bruus.tbd.game.movement;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DiamondShapePathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HomingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.OrbitPathFinder;
import net.riezebos.bruus.tbd.game.util.OutOfBoundsCalculator;
import net.riezebos.bruus.tbd.visuals.objects.Sprite;

public class SpriteMover {

    private static final SpriteMover instance = new SpriteMover();

    private SpriteMover () {
    }

    public static SpriteMover getInstance () {
        return instance;
    }

    private boolean shouldUpdatePathSettings (GameObject gameObject) {

        MovementConfiguration moveConfig = gameObject.getMovementConfiguration();
        boolean bool = false;
        bool =
                moveConfig.getCurrentPath() == null ||
                        moveConfig.getPathFinder().shouldRecalculatePath(gameObject) ||
                        moveConfig.getXMovementSpeed() != moveConfig.getLastUsedXMovementSpeed() ||
                        moveConfig.getYMovementSpeed() != moveConfig.getLastUsedYMovementSpeed();
        return bool; //This returns TRUE, and thus a new path is generated
    }

    public void moveGameObject (GameObject gameObject, MovementConfiguration moveConfig) {
        if (gameObject.getCurrentLocation() != null && moveConfig.getCurrentLocation() == null) {
            moveConfig.setCurrentLocation(gameObject.getCurrentLocation());
        }

        if (shouldUpdatePathSettings(gameObject)) {
            updatePath(gameObject, moveConfig);
        }
        if (moveConfig.getPathFinder() instanceof HomingPathFinder) {
            moveHomingPathFinders(gameObject, moveConfig);
        } else {
            if (!handleNextWaypointRemoval(moveConfig)) {
                //Could brick leaving this uncommented but can be handled by the movetowards/handle methods
//                return;
            }

            if (moveConfig.getPathFinder() instanceof BouncingPathFinder) {
                handleBouncingPathFinder(gameObject, moveConfig);
            }
            moveTowardsNextPoint(gameObject, moveConfig);
        }

        handleAdditionalBehaviors(gameObject, moveConfig);
    }

    private void updatePath (GameObject gameObject, MovementConfiguration moveConfig) {
//        PathFinderConfig config = PathFinderConfigCreator.createConfig(gameObject, moveConfig);
        moveConfig.setCurrentPath(moveConfig.getPathFinder().findPath(gameObject));
        moveConfig.setLastUsedXMovementSpeed(moveConfig.getXMovementSpeed());
        moveConfig.setLastUsedYMovementSpeed(moveConfig.getYMovementSpeed());
    }

    private void moveHomingPathFinders (GameObject gameObject, MovementConfiguration moveConfig) {
        if (moveConfig.getTargetToChase() != null) {
            moveConfig.getCurrentPath().setTarget(moveConfig.getTargetToChase());
            if (!moveConfig.getCurrentPath().getTarget().isVisible()) {
                acquireNewTarget(gameObject, moveConfig);
            }
        }

        if (moveConfig.getTargetToChase() == null && moveConfig.getCurrentPath().getTarget() == null) {
            acquireNewTarget(gameObject, moveConfig);
        }

        // check if the missile has lost lock
        boolean hasPassedPlayerOrNeverHadLock = false;

        if (moveConfig.getPathFinder().shouldRecalculatePath(gameObject)) {
            hasPassedPlayerOrNeverHadLock = true; // if it should recalculate path, it means it lost lock or never had
            moveConfig.getUntrackableObjects().add(moveConfig.getTargetToChase());
        }

        if (hasPassedPlayerOrNeverHadLock) {
            moveConfig.setHasLock(false);
        }


        // only calculate next direction and update location if missile still has lock
        if (moveConfig.hasLock() && moveConfig.getCurrentPath().getTarget() != null) {
            // Get the direction of the next step
            HomingPathFinder pathfinder = (HomingPathFinder) moveConfig.getPathFinder();

            Direction nextStep = pathfinder.getNextStep(gameObject, moveConfig.getRotation());


            // Based on the direction, calculate the next point. Overwrite the regular calculator with homing's specific one
            Point nextPoint = pathfinder.calculateNextPoint(moveConfig, nextStep,
                    moveConfig.getXMovementSpeed(), moveConfig.getYMovementSpeed(), moveConfig.getCurrentPath().getTarget());


            moveConfig.setNextPoint(nextPoint);
            moveConfig.getCurrentPath().setFallbackDirection(nextStep);
        } else {
            // if missile lost lock, it should keep moving in the last direction
            moveConfig.setRotation(moveConfig.getCurrentPath().getFallbackDirection());
            moveConfig.setNextPoint(calculateNextPointForHomingPathFinder(moveConfig.getCurrentLocation(),
                    moveConfig.getCurrentPath().getFallbackDirection(), moveConfig.getXMovementSpeed(),
                    moveConfig.getYMovementSpeed()));
        }

        // Update the current location
        if (moveConfig.getNextPoint() != null) {
            moveConfig.setCurrentLocation(moveConfig.getNextPoint());
            gameObject.setXCoordinate(moveConfig.getNextPoint().getX());
            gameObject.setYCoordinate(moveConfig.getNextPoint().getY());

            //Homing movement rotation is hell, just use 360 degrees visual objects so you dont need to rotate
            //Leave the code in this method but comment it out
//            if (gameObject.isAllowedVisualsToRotate()) {
//                if (hasPassedPlayerOrNeverHadLock) {
//                    gameObject.rotateGameObjectTowards(moveConfig.getRotation());
//                    gameObject.setAllowedVisualsToRotate(false);
//                } else {
//                    Point targetPoint = new Point(moveConfig.getTarget().getCenterXCoordinate(), moveConfig.getTarget().getCenterYCoordinate());
//                    gameObject.rotateGameObjectTowards(targetPoint.getX(), targetPoint.getY());
//                }
//            }

        }
    }

    private boolean handleNextWaypointRemoval (MovementConfiguration moveConfig) {
        // If reached the next point, remove it from the path
        if (!moveConfig.getCurrentPath().getWaypoints().isEmpty() &&
                moveConfig.getCurrentLocation().equals(moveConfig.getCurrentPath().getWaypoints().get(0))) {
            moveConfig.getCurrentPath().getWaypoints().remove(0);
            return true;
        }
        return false;
    }

    private void handleBouncingPathFinder (GameObject gameObject, MovementConfiguration moveConfig) {
        // Bouncing specific logic

        if (!((BouncingPathFinder) moveConfig.getPathFinder()).isAllowedToBounce()) {
            return; //Should maybe be "setVisible(false);
        }

        Direction newDirection = ((BouncingPathFinder) moveConfig.getPathFinder())
                .getNewDirection(gameObject, moveConfig.getRotation());
        if (newDirection != moveConfig.getRotation()) {
            moveConfig.setRotation(newDirection);
            updatePath(gameObject, moveConfig);
            ((BouncingPathFinder) moveConfig.getPathFinder()).increaseBounce();
        }
    }

    private void moveTowardsNextPoint (GameObject gameObject, MovementConfiguration moveConfig) {
        if (gameObject.isAllowedToMove() && !moveConfig.getCurrentPath().getWaypoints().isEmpty()) {
            gameObject.updateCurrentLocation(moveConfig.getCurrentPath().getWaypoints().get(0));
            gameObject.setXCoordinate(moveConfig.getCurrentPath().getWaypoints().get(0).getX());
            gameObject.setYCoordinate(moveConfig.getCurrentPath().getWaypoints().get(0).getY());
            moveConfig.setStepsTaken(moveConfig.getStepsTaken() + 1);
            moveConfig.getCurrentLocation().setX(gameObject.getXCoordinate());
            moveConfig.getCurrentLocation().setY(gameObject.getYCoordinate());
        }
    }

    private void handleAdditionalBehaviors (GameObject gameObject, MovementConfiguration moveConfig) {
        gameObject.rotateAfterMovement();


        if (moveConfig.getPathFinder() instanceof OrbitPathFinder) {
            adjustOrbitPath(moveConfig);
        }

        if (moveConfig.getPathFinder() instanceof DiamondShapePathFinder && moveConfig.getCurrentPath().getWaypoints().isEmpty()) {
            gameObject.setVisible(false);
        }

        if (OutOfBoundsCalculator.isOutOfBounds(gameObject) && !(moveConfig.getPathFinder() instanceof BouncingPathFinder)) {
            gameObject.setVisible(false);
            gameObject.setAllowedToMove(false);
        }
    }


    private void adjustOrbitPath (MovementConfiguration moveConfig) {
        OrbitPathFinder pathFinder = (OrbitPathFinder) moveConfig.getPathFinder();
        Sprite target = pathFinder.getTarget();
        int deltaX = target.getCenterXCoordinate() - moveConfig.getLastKnownTargetX();
        int deltaY = target.getCenterYCoordinate() - moveConfig.getLastKnownTargetY();

        if (deltaX != 0 || deltaY != 0) {
            moveConfig.setLastKnownTargetX(target.getCenterXCoordinate());
            moveConfig.setLastKnownTargetY(target.getCenterYCoordinate());
            pathFinder.adjustPathForTargetMovement(moveConfig.getCurrentPath(), deltaX, deltaY);
        }
    }

    //Helper method for acquiring a new target, used in homing pathfinders
    private void acquireNewTarget (GameObject sprite, MovementConfiguration moveConfig) {
        moveConfig.getUntrackableObjects().add(moveConfig.getTargetToChase());
        moveConfig.getCurrentPath().setTarget(null);

        HomingPathFinder pathFinder = (HomingPathFinder) moveConfig.getPathFinder();
        GameObject newObjectToChase = pathFinder.getTarget(sprite.isFriendly(), sprite.getXCoordinate(), sprite.getYCoordinate());

        if (!moveConfig.getUntrackableObjects().contains(newObjectToChase)) {
            moveConfig.setTargetToChase(newObjectToChase);
            moveConfig.getCurrentPath().setTarget(newObjectToChase);
        }
    }

    // Needed for non-orbiting PathFinders, so added to missiles
    private Point calculateNextPointForHomingPathFinder (Point currentLocation, Direction direction, float XStepSize, float YStepSize) {
        float x = currentLocation.getX();
        float y = currentLocation.getY();

        switch (direction) {
            case UP:
                y -= YStepSize;
                break;
            case DOWN:
                y += YStepSize;
                break;
            case LEFT:
                x -= XStepSize;
                break;
            case RIGHT:
                x += XStepSize;
                break;
            case LEFT_UP:
                x -= XStepSize;
                y -= YStepSize;
                break;
            case LEFT_DOWN:
                x -= XStepSize;
                y += YStepSize;
                break;
            case RIGHT_UP:
                x += XStepSize;
                y -= YStepSize;
                break;
            case RIGHT_DOWN:
                x += XStepSize;
                y += YStepSize;
                break;
            case NONE:
                // no movement
                break;
        }



        return new Point(Math.round(x), Math.round(y));
    }

}