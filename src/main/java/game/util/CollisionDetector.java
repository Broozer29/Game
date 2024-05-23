package game.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game.objects.GameObject;

public class CollisionDetector {

    private static CollisionDetector instance = new CollisionDetector();
    private int threshold = 600;
    private int boardBlockThreshold = 4;

    private CollisionDetector () {

    }

    public static CollisionDetector getInstance () {
        return instance;
    }


    public boolean detectCollision (GameObject gameObject1, GameObject gameObject2) {

        //Objects should not collide with their owners, that's just silly and not good design so we skip it entirely
        if(gameObject1.getOwnerOrCreator() != null){
            if(gameObject1.getOwnerOrCreator().equals(gameObject2)){
                return false;
            }
        }

        if(gameObject2.getOwnerOrCreator() != null){
            if(gameObject2.getOwnerOrCreator().equals(gameObject1)){
                return false;
            }
        }



        if (isNearby(gameObject1, gameObject2, threshold)) {

            //This doesn't play well with animations, only regular sprites. Needs fixing, width/height are 0
            //This isn't even used by the other managers lmao what the actual fuck
            Rectangle r1 = BoundsCalculator.getGameObjectBounds(gameObject1);
            Rectangle r2 = BoundsCalculator.getGameObjectBounds(gameObject2);
            if (r1.getHeight() == 0 || r1.getWidth() == 0) {
                System.out.println("R1 has width/height of: " + r1.getWidth() + " / " + r1.getHeight() + " objectType: " + gameObject1.getObjectType());
            }

            if (r2.getHeight() == 0 || r2.getWidth() == 0) {
                System.out.println("R2 has width/height of: " + r2.getWidth() + " / " + r2.getHeight() + " objectType: " + gameObject2.getObjectType());
            }

            if (r1.intersects(r2)) {
                if (gameObject1.isBoxCollision() || gameObject2.isBoxCollision()) {
                    return true;
                } else return checkPixelCollision(gameObject1, gameObject2);
            }
        }

        return false;
    }


    private boolean isWithinBoardBlockThreshold (GameObject gameObject1, GameObject gameObject2) {
        gameObject1.updateBoardBlock();
        gameObject2.updateBoardBlock();

        int blockDifference = Math.abs(gameObject1.getCurrentBoardBlock() - gameObject2.getCurrentBoardBlock());
        return blockDifference <= boardBlockThreshold;
    }

    public boolean isNearby(GameObject gameObject1, GameObject gameObject2, int rangeThreshold) {
        if (!isWithinBoardBlockThreshold(gameObject1, gameObject2)) {
            return false;
        }

        int x1 = gameObject1.getAnimation() != null ? gameObject1
                .getAnimation().getXCoordinate() : gameObject1.getXCoordinate();
        int y1 = gameObject1.getAnimation() != null ? gameObject1.getAnimation().getYCoordinate() : gameObject1.getYCoordinate();

        int x2 = gameObject2.getAnimation() != null ? gameObject2.getAnimation().getXCoordinate() : gameObject2.getXCoordinate();
        int y2 = gameObject2.getAnimation() != null ? gameObject2.getAnimation().getYCoordinate() : gameObject2.getYCoordinate();

        double distance = Math.hypot(x1 - x2, y1 - y2);
        return distance < rangeThreshold;
    }


    private boolean checkPixelCollision(GameObject gameObject1, GameObject gameObject2) {
        BufferedImage img1 = null;
        BufferedImage img2 = null;
        int alphaThreshold = 100;

        // Use animation coordinates and dimensions if available
        int x1, y1, x2, y2, width1, height1, width2, height2;

        if (gameObject1.getAnimation() != null) {
            img1 = gameObject1.getAnimation().getCurrentFrameImage(false);
            x1 = gameObject1.getAnimation().getXCoordinate();
            y1 = gameObject1.getAnimation().getYCoordinate();
            width1 = img1.getWidth();
            height1 = img1.getHeight();
        } else {
            img1 = gameObject1.getImage();
            x1 = gameObject1.getXCoordinate();
            y1 = gameObject1.getYCoordinate();
            width1 = img1.getWidth();
            height1 = img1.getHeight();
        }

        if (gameObject2.getAnimation() != null) {
            img2 = gameObject2.getAnimation().getCurrentFrameImage(false);
            x2 = gameObject2.getAnimation().getXCoordinate();
            y2 = gameObject2.getAnimation().getYCoordinate();
            width2 = img2.getWidth();
            height2 = img2.getHeight();
        } else {
            img2 = gameObject2.getImage();
            x2 = gameObject2.getXCoordinate();
            y2 = gameObject2.getYCoordinate();
            width2 = img2.getWidth();
            height2 = img2.getHeight();
        }

        if (img1 != null && img2 != null) {
            int xStart = Math.max(x1, x2);
            int yStart = Math.max(y1, y2);
            int xEnd = Math.min(x1 + width1, x2 + width2);
            int yEnd = Math.min(y1 + height1, y2 + height2);

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
            return true; // Assuming collision if images are not available
        }
    }


}
