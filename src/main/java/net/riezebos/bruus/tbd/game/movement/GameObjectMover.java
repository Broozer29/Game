package net.riezebos.bruus.tbd.game.movement;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.OrbitPathFinder;
import net.riezebos.bruus.tbd.game.util.OutOfBoundsCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;

public class GameObjectMover {

    private static final GameObjectMover instance = new GameObjectMover();

    private GameObjectMover() {
    }

    public static GameObjectMover getInstance() {
        return instance;
    }

    private boolean shouldUpdatePathSettings(GameObject gameObject) {

        MovementConfiguration moveConfig = gameObject.getMovementConfiguration();
        boolean bool = false;
        bool =
                moveConfig.getCurrentPath() == null ||
                        moveConfig.getPathFinder().shouldRecalculatePath(gameObject) ||
                        moveConfig.getMovementSpeed() != moveConfig.getLastUsedMovementSpeed();
        return bool; //This returns TRUE, and thus a new path is generated
    }

    public void moveGameObject(GameObject gameObject, MovementConfiguration moveConfig) {
        if (gameObject.getCurrentLocation() != null && moveConfig.getCurrentLocation() == null) {
            moveConfig.setCurrentLocation(gameObject.getCurrentLocation());
        }

        if (shouldUpdatePathSettings(gameObject)) {
            updatePath(gameObject, moveConfig);
        }
        handleNextWaypointRemoval(moveConfig);
        if (moveConfig.getPathFinder() instanceof BouncingPathFinder) {
            handleBouncingPathFinder(gameObject, moveConfig);
        }
        moveTowardsNextPoint(gameObject, moveConfig);

        handleAdditionalBehaviors(gameObject, moveConfig);
    }

    private void updatePath(GameObject gameObject, MovementConfiguration moveConfig) {
        moveConfig.setCurrentPath(moveConfig.getPathFinder().findPath(gameObject));
        moveConfig.setLastUsedMovementSpeed(moveConfig.getMovementSpeed());
    }

    private boolean handleNextWaypointRemoval(MovementConfiguration moveConfig) {
        // If reached the next point, remove it from the path
        if (!moveConfig.getCurrentPath().getWaypoints().isEmpty() &&
                moveConfig.getCurrentLocation().equals(moveConfig.getCurrentPath().getWaypoints().get(0))) {
            moveConfig.getCurrentPath().getWaypoints().remove(0);
            return true;
        }
        return false;
    }

    private void handleBouncingPathFinder(GameObject gameObject, MovementConfiguration moveConfig) {
        if (!((BouncingPathFinder) moveConfig.getPathFinder()).isAllowedToBounce()) {
            return; //Should maybe be "setVisible(false);
        }

        Direction newDirection = ((BouncingPathFinder) moveConfig.getPathFinder())
                .getNewDirection(gameObject, moveConfig.getRotation());
        if (newDirection != moveConfig.getRotation()) {
            moveConfig.setDirection(newDirection);
            updatePath(gameObject, moveConfig);
            ((BouncingPathFinder) moveConfig.getPathFinder()).increaseBounce();
        }
    }

    private void moveTowardsNextPoint(GameObject gameObject, MovementConfiguration moveConfig) {
        if (gameObject.isAllowedToMove() && !moveConfig.getCurrentPath().getWaypoints().isEmpty()) {
            gameObject.updateCurrentLocation(moveConfig.getCurrentPath().getWaypoints().get(0));
            gameObject.setXCoordinate(moveConfig.getCurrentPath().getWaypoints().get(0).getX());
            gameObject.setYCoordinate(moveConfig.getCurrentPath().getWaypoints().get(0).getY());
            moveConfig.setStepsTaken(moveConfig.getStepsTaken() + 1);
            moveConfig.getCurrentLocation().setX(gameObject.getXCoordinate());
            moveConfig.getCurrentLocation().setY(gameObject.getYCoordinate());
        }
    }

    private void handleAdditionalBehaviors(GameObject gameObject, MovementConfiguration moveConfig) {
        gameObject.rotateAfterMovement();


        if (moveConfig.getPathFinder() instanceof OrbitPathFinder) {
            adjustOrbitPath(moveConfig);
        }

        if (OutOfBoundsCalculator.isOutOfBounds(gameObject) && !(moveConfig.getPathFinder() instanceof BouncingPathFinder)) {
            gameObject.setVisible(false);
            gameObject.setAllowedToMove(false);
        }
    }


    private void adjustOrbitPath(MovementConfiguration moveConfig) {
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
}