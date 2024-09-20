package game.util.collision;

import game.movement.Point;

public class CollisionInfo {
    private boolean collided;
    private Point collisionPoint;

    public CollisionInfo(boolean collided, Point collisionPoint) {
        this.collided = collided;
        this.collisionPoint = collisionPoint;
    }

    public boolean isCollided() {
        return collided;
    }

    public Point getCollisionPoint() {
        return collisionPoint;
    }
}