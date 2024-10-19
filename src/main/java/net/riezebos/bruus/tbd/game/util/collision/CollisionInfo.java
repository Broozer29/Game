package net.riezebos.bruus.tbd.game.util.collision;

import net.riezebos.bruus.tbd.game.movement.Point;

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