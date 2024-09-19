package game.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game.gameobjects.GameObject;
import game.gameobjects.missiles.specialAttacks.Laserbeam;
import visualobjects.SpriteAnimation;

public class CollisionDetector {

    private static CollisionDetector instance = new CollisionDetector();
    private int threshold = 400;
    private int boardBlockThreshold = 3;

    private CollisionDetector() {

    }

    public static CollisionDetector getInstance() {
        return instance;
    }

    // Public method for GameObject vs. GameObject collision
    public boolean detectCollision(GameObject gameObject1, GameObject gameObject2) {
        // Objects should not collide with their owners
        if (isOwnerOrCreator(gameObject1, gameObject2) || isOwnerOrCreator(gameObject2, gameObject1)) {
            return false;
        }

        if (isNearby(gameObject1, gameObject2, threshold)) {
            Rectangle r1 = BoundsCalculator.getGameObjectBounds(gameObject1);
            Rectangle r2 = BoundsCalculator.getGameObjectBounds(gameObject2);

            if (r1.intersects(r2)) {
                if (gameObject1.isBoxCollision() || gameObject2.isBoxCollision()) {
                    return true;
                } else {
                    return checkPixelCollision(gameObject1, gameObject2);
                }
            }
        }

        return false;
    }

    // Public method for GameObject vs. Laserbeam collision
    public boolean detectCollision(GameObject gameObject, Laserbeam laserbeam) {
        if(laserbeam.getOwner() != null){
            if(laserbeam.getOwner().equals(gameObject)){
                return false;
            }
        }


        int playerBoardBlock = gameObject.getCurrentBoardBlock();

        // Optionally, check collision with laser origin animation
        SpriteAnimation laserOrigin = laserbeam.getLaserOriginAnimation();
        if (laserOrigin != null) {
            int laserOriginBlock = BoardBlockUpdater.getBoardBlock(laserOrigin.getXCoordinate());

            if (Math.abs(playerBoardBlock - laserOriginBlock) <= 2) {
                if (isNearby(gameObject, laserOrigin, 50)) {
                    if (detectCollision(gameObject, laserOrigin)) {
                        return true;
                    }
                }
            }
        }

        // Check collision with laser bodies
        for (SpriteAnimation laserSegment : laserbeam.getLaserBodies()) {
            int laserSegmentBlock = BoardBlockUpdater.getBoardBlock(laserSegment.getXCoordinate());

            // Check if the player's board block is within 2 of the laser segment's block
            if (Math.abs(playerBoardBlock - laserSegmentBlock) > 2) {
                continue; // Skip this segment
            }

            // Check if the player is within 50 pixels of the laser segment
            if (!isNearby(gameObject, laserSegment, 50)) {
                continue; // Skip this segment
            }

            // Perform collision detection
            if (detectCollision(gameObject, laserSegment)) {
                return true;
            }
        }

        return false;
    }

    // Helper method to detect collision between a GameObject and a SpriteAnimation
    private boolean detectCollision(GameObject gameObject, SpriteAnimation spriteAnimation) {
        // First, check bounding boxes
        Rectangle gameObjectBounds = BoundsCalculator.getGameObjectBounds(gameObject);
        Rectangle spriteBounds = getSpriteBounds(spriteAnimation);

        if (gameObjectBounds.intersects(spriteBounds)) {
            // Perform pixel-perfect collision detection
            return checkPixelCollision(gameObject, spriteAnimation);
        }

        return false;
    }

    // Private method to check if one GameObject is the owner or creator of another
    private boolean isOwnerOrCreator(GameObject gameObject1, GameObject gameObject2) {
        return gameObject1.getOwnerOrCreator() != null && gameObject1.getOwnerOrCreator().equals(gameObject2);
    }

    // Reusable method for pixel-perfect collision detection
    private boolean checkPixelCollision(BufferedImage img1, int x1, int y1, BufferedImage img2, int x2, int y2) {
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
                        return true; // Collision detected
                    }
                }
            }
            return false; // No collision detected
        } else {
            return false; // One of the images is missing; assume no collision
        }
    }

    // Refactored pixel-perfect collision between two GameObjects
    private boolean checkPixelCollision(GameObject gameObject1, GameObject gameObject2) {
        BufferedImage img1 = getGameObjectImage(gameObject1);
        BufferedImage img2 = getGameObjectImage(gameObject2);

        int x1 = getGameObjectXCoordinate(gameObject1);
        int y1 = getGameObjectYCoordinate(gameObject1);

        int x2 = getGameObjectXCoordinate(gameObject2);
        int y2 = getGameObjectYCoordinate(gameObject2);

        return checkPixelCollision(img1, x1, y1, img2, x2, y2);
    }

    // Refactored pixel-perfect collision between a GameObject and a SpriteAnimation
    private boolean checkPixelCollision(GameObject gameObject, SpriteAnimation spriteAnimation) {
        BufferedImage gameObjectImage = getGameObjectImage(gameObject);
        BufferedImage spriteImage = spriteAnimation.getCurrentFrameImage(false); // Get rotated image

        int x1 = getGameObjectXCoordinate(gameObject);
        int y1 = getGameObjectYCoordinate(gameObject);

        int x2 = spriteAnimation.getXCoordinate();
        int y2 = spriteAnimation.getYCoordinate();

        return checkPixelCollision(gameObjectImage, x1, y1, spriteImage, x2, y2);
    }

    // Helper methods to get GameObject image and coordinates
    private BufferedImage getGameObjectImage(GameObject gameObject) {
        if (gameObject.getAnimation() != null) {
            return gameObject.getAnimation().getCurrentFrameImage(false);
        } else {
            return gameObject.getImage();
        }
    }

    private int getGameObjectXCoordinate(GameObject gameObject) {
        return gameObject.getAnimation() != null ? gameObject.getAnimation().getXCoordinate() : gameObject.getXCoordinate();
    }

    private int getGameObjectYCoordinate(GameObject gameObject) {
        return gameObject.getAnimation() != null ? gameObject.getAnimation().getYCoordinate() : gameObject.getYCoordinate();
    }

    // Helper method to get the bounds of a SpriteAnimation
    private Rectangle getSpriteBounds(SpriteAnimation spriteAnimation) {
        int x = spriteAnimation.getXCoordinate();
        int y = spriteAnimation.getYCoordinate();
        BufferedImage image = spriteAnimation.getCurrentFrameImage(true); // Get rotated image
        int width = image.getWidth();
        int height = image.getHeight();
        return new Rectangle(x, y, width, height);
    }

    // Helper method to check if two GameObjects are nearby
    public boolean isNearby(GameObject gameObject1, GameObject gameObject2, int rangeThreshold) {
        if (!isWithinBoardBlockThreshold(gameObject1, gameObject2)) {
            return false;
        }

        int x1 = getGameObjectXCoordinate(gameObject1);
        int y1 = getGameObjectYCoordinate(gameObject1);

        int x2 = getGameObjectXCoordinate(gameObject2);
        int y2 = getGameObjectYCoordinate(gameObject2);

        double distance = Math.hypot(x1 - x2, y1 - y2);
        return distance < rangeThreshold;
    }

    // Helper method to check if a GameObject and SpriteAnimation are nearby
    private boolean isNearby(GameObject gameObject, SpriteAnimation spriteAnimation, int rangeThreshold) {
        int x1 = getGameObjectXCoordinate(gameObject);
        int y1 = getGameObjectYCoordinate(gameObject);

        int x2 = spriteAnimation.getXCoordinate();
        int y2 = spriteAnimation.getYCoordinate();

        double distance = Math.hypot(x1 - x2, y1 - y2);
        return distance < rangeThreshold;
    }

    // Helper method to check if two GameObjects are within board block threshold
    private boolean isWithinBoardBlockThreshold(GameObject gameObject1, GameObject gameObject2) {
        gameObject1.updateBoardBlock();
        gameObject2.updateBoardBlock();

        int blockDifference = Math.abs(gameObject1.getCurrentBoardBlock() - gameObject2.getCurrentBoardBlock());
        return blockDifference <= boardBlockThreshold;
    }
}
