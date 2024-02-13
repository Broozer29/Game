package game.movement;

import VisualAndAudioData.image.ImageRotator;
import game.movement.pathfinderconfigs.*;
import game.movement.pathfinders.*;
import game.objects.GameObject;
import game.objects.enemies.enemytypes.Seeker;
import game.objects.missiles.missiletypes.SeekerProjectile;
import game.objects.player.PlayerManager;
import game.util.OutOfBoundsCalculator;
import visualobjects.Sprite;

public class SpriteMover {

    private static SpriteMover instance = new SpriteMover();

    private long modulocounter = 0;

    private SpriteMover () {

    }

    public static SpriteMover getInstance () {
        return instance;
    }

    private boolean shouldUpdatePathSettings (MovementConfiguration moveConfig) {

        if (moveConfig.getCurrentPath() == null || moveConfig.getCurrentPath().getWaypoints().isEmpty()
                || moveConfig.getXMovementSpeed() != moveConfig.getLastUsedXMovementSpeed()
                || moveConfig.getYMovementSpeed() != moveConfig.getLastUsedYMovementSpeed()
                || moveConfig.getPathFinder().shouldRecalculatePath(moveConfig.getCurrentPath())) {
            return true;
        } else
            return false;
    }

    // Moves the sprite only, not it's corresponding animations!
    // Add the "out of bounds" detector!
    public void moveSprite (GameObject gameObject, MovementConfiguration moveConfig) {
        if (shouldUpdatePathSettings(moveConfig)) {
            // calculate a new path if necessary
            PathFinderConfig config = PathFinderConfigCreator.createConfig(gameObject, moveConfig);
            moveConfig.setCurrentPath(moveConfig.getPathFinder().findPath(config));

            moveConfig.setLastUsedXMovementSpeed(moveConfig.getXMovementSpeed());
            moveConfig.setLastUsedYMovementSpeed(moveConfig.getYMovementSpeed());
        }

        if (moveConfig.getPathFinder() instanceof HomingPathFinder) {
            moveHomingPathFinders(gameObject, moveConfig);
            return;
        }
//        else {
//            moveConfig.getCurrentPath().updateCurrentLocation(gameObject.getCurrentLocation());
//        }

        // if reached the next point, remove it from the path
        if (moveConfig.getCurrentLocation().equals(moveConfig.getCurrentPath().getWaypoints().get(0))) {
            moveConfig.getCurrentPath().getWaypoints().remove(0);
        }

        if (moveConfig.getPathFinder() instanceof BouncingPathFinder) {
            Direction newDirection = (Direction) ((BouncingPathFinder) moveConfig.getPathFinder()).getNewDirection(gameObject, moveConfig.getRotation());
            if (newDirection != moveConfig.getRotation()) {
                moveConfig.setRotation(newDirection);
                moveConfig.setCurrentPath(moveConfig.getPathFinder().findPath(PathFinderConfigCreator.createConfig(gameObject, moveConfig)));
            }
        }

        // move towards the next point

        if (moveConfig.getCurrentPath().getWaypoints().size() > 0 && gameObject.isAllowedToMove()) {
            gameObject.updateCurrentLocation(moveConfig.getCurrentPath().getWaypoints().get(0));
            gameObject.setX(moveConfig.getCurrentPath().getWaypoints().get(0).getX());
            gameObject.setY(moveConfig.getCurrentPath().getWaypoints().get(0).getY());
            moveConfig.setStepsTaken(moveConfig.getStepsTaken() + 1);
            moveConfig.getCurrentLocation().setX(gameObject.getXCoordinate());
            moveConfig.getCurrentLocation().setY(gameObject.getYCoordinate());

            //Rotate the object towards it's target
            if(moveConfig.getCurrentPath().getWaypoints().size() > 1 && gameObject.isAllowedVisualsToRotate()){
                Point finalPoint = moveConfig.getCurrentPath().getWaypoints().get(moveConfig.getCurrentPath().getWaypoints().size() - 1);
                gameObject.rotateGameObjectTowards(finalPoint.getX(), finalPoint.getY());
                gameObject.setAllowedVisualsToRotate(false);
            }

        }

        if (moveConfig.getPathFinder() instanceof OrbitPathFinder) {
            adjustOrbitPath(moveConfig);
        }
        if (moveConfig.getPathFinder() instanceof DiamondShapePathFinder) {
            if (moveConfig.getCurrentPath().getWaypoints().isEmpty()) {
                gameObject.setVisible(false);
            }
        }
        if (OutOfBoundsCalculator.isOutOfBounds(gameObject)) {
            gameObject.setVisible(false);
            gameObject.setAllowedToMove(false);
        }
    }

    private void moveHomingPathFinders (GameObject gameObject, MovementConfiguration moveConfig) {
        if (moveConfig.getTarget() != null) {
            moveConfig.getCurrentPath().setTarget(moveConfig.getTarget());
            if (!moveConfig.getCurrentPath().getTarget().isVisible()) {
                acquireNewTarget(gameObject, moveConfig);
            }
        }

        if (moveConfig.getTarget() == null && moveConfig.getCurrentPath().getTarget() == null) {
            acquireNewTarget(gameObject, moveConfig);
        }

        // check if the missile has lost lock
        boolean hasPassedPlayerOrNeverHadLock = false;

        if (moveConfig.getPathFinder().shouldRecalculatePath(moveConfig.getCurrentPath())) {
            hasPassedPlayerOrNeverHadLock = true; // if it should recalculate path, it means it lost lock or never had
            moveConfig.getUntrackableObjects().add(moveConfig.getTarget());
        }

        if (hasPassedPlayerOrNeverHadLock) {
            moveConfig.setHasLock(false);
        }


        // only calculate next direction and update location if missile still has lock
        if (moveConfig.hasLock()) {
            // Get the direction of the next step
            HomingPathFinder pathfinder = (HomingPathFinder) moveConfig.getPathFinder();

            Direction nextStep = pathfinder.getNextStep(moveConfig.getCurrentLocation(),
                    moveConfig.getCurrentPath(), moveConfig.getRotation());


            // Based on the direction, calculate the next point. Overwrite the regular calculator with homing's specific one
            Point nextPoint = pathfinder.calculateNextPoint(moveConfig.getCurrentLocation(), nextStep,
                    moveConfig.getXMovementSpeed(), moveConfig.getYMovementSpeed(), moveConfig.getCurrentPath().getTarget());


            moveConfig.setNextPoint(nextPoint);
            moveConfig.getCurrentPath().setFallbackDirection(nextStep);
        } else {
            // if missile lost lock, it should keep moving in the last direction
            moveConfig.setRotation(moveConfig.getCurrentPath().getFallbackDirection());
            moveConfig.setNextPoint(calculateNextPoint(moveConfig.getCurrentLocation(),
                    moveConfig.getCurrentPath().getFallbackDirection(), moveConfig.getXMovementSpeed(),
                    moveConfig.getYMovementSpeed()));
        }

        // Update the current location
        if (moveConfig.getNextPoint() != null) {
            moveConfig.setCurrentLocation(moveConfig.getNextPoint());
            gameObject.setCenterCoordinates(moveConfig.getNextPoint().getX(), moveConfig.getNextPoint().getY());
            Point maxPoint = moveConfig.getCurrentPath().getWaypoints().get(moveConfig.getCurrentPath().getWaypoints().size() - 1);
            gameObject.rotateGameObjectTowards(maxPoint.getX(), maxPoint.getY());
        }
        if(gameObject instanceof SeekerProjectile){
            System.out.println(gameObject.getMovementConfiguration().getTarget().getCurrentLocation());
            System.out.println(PlayerManager.getInstance().getSpaceship().getCurrentLocation());
        }

    }

    //Gets a new nearest target and blacklists the current target, used for friendly homing gameobjects
    private void acquireNewTarget (GameObject sprite, MovementConfiguration moveConfig) {
        moveConfig.getUntrackableObjects().add(moveConfig.getTarget());
        moveConfig.getCurrentPath().setTarget(null);

        HomingPathFinder pathFinder = (HomingPathFinder) moveConfig.getPathFinder();
        GameObject newObjectToChase = pathFinder.getTarget(sprite.isFriendly(), sprite.getXCoordinate(), sprite.getYCoordinate());

        if (!moveConfig.getUntrackableObjects().contains(newObjectToChase)) {
            moveConfig.setTarget(newObjectToChase);
            moveConfig.getCurrentPath().setTarget(newObjectToChase);
        }
    }

    // Adjusts the orbiting path when the target changes coordinates
    private void adjustOrbitPath (MovementConfiguration moveConfig) {
        Sprite target = ((OrbitPathFinder) moveConfig.getPathFinder()).getTarget();
        // Check if the target's position has changed
        if (target.getCenterXCoordinate() != moveConfig.getLastKnownTargetX()
                || target.getCenterYCoordinate() != moveConfig.getLastKnownTargetY()) {

            int deltaX = 0;
            int deltaY = 0;

            if (moveConfig.getLastKnownTargetX() == 0 || moveConfig.getLastKnownTargetY() == 0) {
                deltaX = 0;
                deltaY = 0;
            } else {
                deltaX = target.getCenterXCoordinate() - moveConfig.getLastKnownTargetX();
                deltaY = target.getCenterYCoordinate() - moveConfig.getLastKnownTargetY();
            }

            // Update the last known position
            moveConfig.setLastKnownTargetX(target.getCenterXCoordinate());
            moveConfig.setLastKnownTargetY(target.getCenterYCoordinate());

            // Adjust the orbit path based on the deltas
            ((OrbitPathFinder) moveConfig.getPathFinder()).adjustPathForTargetMovement(moveConfig.getCurrentPath(),
                    deltaX, deltaY);
        }
    }


    // Needed for non-orbiting PathFinders, so added to missiles
    private Point calculateNextPoint (Point currentLocation, Direction direction, int XStepSize, int YStepSize) {
        int x = currentLocation.getX();
        int y = currentLocation.getY();

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
        return new Point(x, y);
    }
}
