package net.riezebos.bruus.tbd.game.util.collision;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CollisionDetector {

    private static CollisionDetector instance = new CollisionDetector();
    private int threshold = 400;
    private int boardBlockThreshold = 3;

    private CollisionDetector () {

    }

    public static CollisionDetector getInstance () {
        return instance;
    }

    // Public method for GameObject vs. GameObject collision
    public CollisionInfo detectCollision (GameObject gameObject1, GameObject gameObject2) {
        // Objects should not collide with their owners
        if (isOwnerOrCreator(gameObject1, gameObject2) || isOwnerOrCreator(gameObject2, gameObject1)) {
            return null;
        }

        if (isNearby(gameObject1, gameObject2, threshold)) {
            Rectangle r1 = gameObject1.getBounds();
            Rectangle r2 = gameObject2.getBounds();

            if (r1.intersects(r2)) {
                Point collisionPoint = checkPixelCollision(gameObject1, gameObject2);
                if (collisionPoint != null) {
                    return new CollisionInfo(true, collisionPoint);
                }
            }
        }


        return null;
    }


    // Public method for GameObject vs. Laserbeam collision
    public CollisionInfo detectCollision (GameObject gameObject, Laserbeam laserbeam) {
        if (laserbeam.getOwner() != null && laserbeam.getOwner().equals(gameObject)) {
            return new CollisionInfo(false, null);
        }

        // Check collision with laser bodies
        for (SpriteAnimation laserSegment : laserbeam.getLaserBodies()) {
            if (!isNearby(gameObject, laserSegment, 50)) {
                continue; // Skip this segment
            }

            CollisionInfo collisionInfo = detectCollision(gameObject, laserSegment);
            if (collisionInfo.isCollided()) {
                return collisionInfo;
            }
        }

        return new CollisionInfo(false, null);
    }


    // Helper method to detect collision between a GameObject and a SpriteAnimation
    private CollisionInfo detectCollision (GameObject gameObject, SpriteAnimation spriteAnimation) {
        // First, check bounding boxes
        Rectangle gameObjectBounds = gameObject.getBounds();
        Rectangle spriteBounds = getSpriteBounds(spriteAnimation);

        if (gameObjectBounds.intersects(spriteBounds)) {
            // Perform pixel-perfect collision detection
            Point collisionPoint = checkPixelCollision(gameObject, spriteAnimation);
            if (collisionPoint != null) {
                return new CollisionInfo(true, collisionPoint);
            }
        }

        return new CollisionInfo(false, null);
    }

    // Private method to check if one GameObject is the owner or creator of another
    private boolean isOwnerOrCreator (GameObject gameObject1, GameObject gameObject2) {
        return gameObject1.getOwnerOrCreator() != null && gameObject1.getOwnerOrCreator().equals(gameObject2);
    }

    // Reusable method for pixel-perfect collision detection
    private Point checkPixelCollision (BufferedImage img1, int x1, int y1, BufferedImage img2, int x2, int y2) {
        int alphaThreshold = 100;

        if (img1 != null && img2 != null) {
            // Calculate overlap rectangle
            int xStart = Math.max(x1, x2);
            int yStart = Math.max(y1, y2);
            int xEnd = Math.min(x1 + img1.getWidth(), x2 + img2.getWidth());
            int yEnd = Math.min(y1 + img1.getHeight(), y2 + img2.getHeight());

            for (int y = yStart; y < yEnd; y++) {
                for (int x = xStart; x < xEnd; x++) {
                    int pixel1 = img1.getRGB(x - x1, y - y1);
                    int alpha1 = (pixel1 >> 24) & 0xff;

                    int pixel2 = img2.getRGB(x - x2, y - y2);
                    int alpha2 = (pixel2 >> 24) & 0xff;
                    if (alpha1 > alphaThreshold && alpha2 > alphaThreshold) {
                        // Collision detected at (x, y)
                        return new Point(x, y);
                    }
                }
            }
            return null; // No collision detected
        } else {
            return null; // One of the images is missing; assume no collision
        }
    }

    // Refactored pixel-perfect collision between two GameObjects
    private Point checkPixelCollision (GameObject gameObject1, GameObject gameObject2) {
        BufferedImage img1 = getGameObjectImage(gameObject1);
        BufferedImage img2 = getGameObjectImage(gameObject2);

        int x1 = getGameObjectXCoordinate(gameObject1);
        int y1 = getGameObjectYCoordinate(gameObject1);

        int x2 = getGameObjectXCoordinate(gameObject2);
        int y2 = getGameObjectYCoordinate(gameObject2);

        return checkPixelCollision(img1, x1, y1, img2, x2, y2);
    }

    // Refactored pixel-perfect collision between a GameObject and a SpriteAnimation
    private Point checkPixelCollision (GameObject gameObject, SpriteAnimation spriteAnimation) {
        BufferedImage gameObjectImage = getGameObjectImage(gameObject);
        BufferedImage spriteImage = spriteAnimation.getCurrentFrameImage(false); // Get rotated image

        int x1 = getGameObjectXCoordinate(gameObject);
        int y1 = getGameObjectYCoordinate(gameObject);

        int x2 = spriteAnimation.getXCoordinate();
        int y2 = spriteAnimation.getYCoordinate();

        return checkPixelCollision(gameObjectImage, x1, y1, spriteImage, x2, y2);
    }

    // Helper methods to get GameObject image and coordinates
    private BufferedImage getGameObjectImage (GameObject gameObject) {
        if (gameObject.getAnimation() != null) {
            return gameObject.getAnimation().getCurrentFrameImage(false);
        } else {
            return gameObject.getImage();
        }
    }

    private int getGameObjectXCoordinate (GameObject gameObject) {
        return gameObject.getAnimation() != null ? gameObject.getAnimation().getXCoordinate() : gameObject.getXCoordinate();
    }

    private int getGameObjectYCoordinate (GameObject gameObject) {
        return gameObject.getAnimation() != null ? gameObject.getAnimation().getYCoordinate() : gameObject.getYCoordinate();
    }

    // Helper method to get the bounds of a SpriteAnimation
    private Rectangle getSpriteBounds (SpriteAnimation spriteAnimation) {
        int x = spriteAnimation.getXCoordinate();
        int y = spriteAnimation.getYCoordinate();
        BufferedImage image = spriteAnimation.getCurrentFrameImage(false); // Get rotated image
        int width = image.getWidth();
        int height = image.getHeight();
        return new Rectangle(x, y, width, height);
    }

    // Helper method to check if two GameObjects are nearby
    public boolean isNearby (GameObject gameObject1, GameObject gameObject2, int rangeThreshold) {
        if (!isWithinBoardBlockThreshold(gameObject1, gameObject2)) {
            return false;
        }


        if(gameObject1.getWidth() > rangeThreshold || gameObject1.getHeight() > rangeThreshold){
            rangeThreshold = Math.max(gameObject1.getWidth(), gameObject1.getHeight());
        }

        if(gameObject2.getWidth() > rangeThreshold || gameObject2.getHeight() > rangeThreshold){
            rangeThreshold = Math.max(gameObject2.getWidth(), gameObject2.getHeight());
        }

        int x1 = getGameObjectXCoordinate(gameObject1);
        int y1 = getGameObjectYCoordinate(gameObject1);

        int x2 = getGameObjectXCoordinate(gameObject2);
        int y2 = getGameObjectYCoordinate(gameObject2);

        double distance = Math.hypot(x1 - x2, y1 - y2);
        return distance < rangeThreshold;
    }

    // Helper method to check if a GameObject and SpriteAnimation are nearby
    private boolean isNearby (GameObject gameObject, SpriteAnimation spriteAnimation, int rangeThreshold) {
        int x1 = getGameObjectXCoordinate(gameObject);
        int y1 = getGameObjectYCoordinate(gameObject);

        int x2 = spriteAnimation.getXCoordinate();
        int y2 = spriteAnimation.getYCoordinate();

        double distance = Math.hypot(x1 - x2, y1 - y2);
        return distance < rangeThreshold;
    }

    // Helper method to check if two GameObjects are within board block threshold
    private boolean isWithinBoardBlockThreshold (GameObject gameObject1, GameObject gameObject2) {
        gameObject1.updateBoardBlock();
        gameObject2.updateBoardBlock();

        int blockDifference = Math.abs(gameObject1.getCurrentBoardBlock() - gameObject2.getCurrentBoardBlock());
        return blockDifference <= boardBlockThreshold;
    }
}
