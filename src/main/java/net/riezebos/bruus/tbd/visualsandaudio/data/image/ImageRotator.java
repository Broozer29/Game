package net.riezebos.bruus.tbd.visualsandaudio.data.image;

import net.riezebos.bruus.tbd.game.movement.Direction;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImageRotator {

    private static ImageRotator instance = new ImageRotator();

    private final ImageCache cache = ImageCache.getInstance();
    private List<ImageEnums> blockedFromRotating = new ArrayList<>();

    private ImageRotator () {
        blockedFromRotating.add(ImageEnums.ShurikenEnemy);
        blockedFromRotating.add(ImageEnums.SpaceStationBoss);
        blockedFromRotating.add(ImageEnums.ShurikenMiniBoss);
        blockedFromRotating.add(ImageEnums.DefenderMiniBoss);
        blockedFromRotating.add(ImageEnums.MotherShipDroneMissile);
    }

    public static ImageRotator getInstance () {
        return instance;
    }

    public List<BufferedImage> getRotatedFrames (List<BufferedImage> frames, Direction rotation, boolean crop) {
        String keyString = "rot_frames:" + frames.stream()
                .map(image -> Integer.toString(image.hashCode()))
                .collect(Collectors.joining("_")) + "_" + rotation + "_" + crop;

        ArrayList<BufferedImage> cachedFrames = cache.getFrames(keyString);
        if (cachedFrames != null) {
            return cachedFrames;
        }

        // The frames are cached as one list, not one by one
        ArrayList<BufferedImage> newFrames = new ArrayList<>();
        for (BufferedImage frame : frames) {
            newFrames.add(rotateOrFlipWithoutCaching(frame, rotation.toAngle(), crop));
        }
        cache.putFrames(keyString, newFrames);
        return newFrames;
    }

    public List<BufferedImage> getRotatedFrames (List<BufferedImage> frames, double angleInDegrees) {
        int roundedDegrees = toWholeDegree(angleInDegrees);

        String keyString = "rot_frames:" + frames.stream()
                .map(image -> Integer.toString(image.hashCode()))
                .collect(Collectors.joining("_")) + "_" + roundedDegrees;

        ArrayList<BufferedImage> cachedFrames = cache.getFrames(keyString);
        if (cachedFrames != null) {
            return cachedFrames;
        }

        // Prepare a list to store the adjusted frames, whether rotated or flipped
        ArrayList<BufferedImage> adjustedFrames = new ArrayList<>();

        // Process each frame, without caching it separately
        for (BufferedImage frame : frames) {
            BufferedImage adjustedFrame = rotateOrFlipWithoutCaching(frame, roundedDegrees, false);
            adjustedFrames.add(adjustedFrame);
        }


        ImageCropper.getInstance().cropFramesToUniformContent(adjustedFrames);
        cache.putFrames(keyString, adjustedFrames);
        // Return the list of adjusted frames
        return adjustedFrames;
    }

    public BufferedImage rotate (BufferedImage image, Direction direction, boolean crop) {
        double angle = direction.toAngle();
        return rotateOrFlip(image, angle, crop);
    }


    private BufferedImage rotate (BufferedImage image, double angle, boolean crop) {
        int roundedDegrees = toWholeDegree(angle);
        String keyString = "rot_img:" + image.hashCode() + "_" + roundedDegrees + "_" + crop;
        BufferedImage cachedImage = cache.getImage(keyString);
        if (cachedImage != null) {
            return cachedImage;
        }

        BufferedImage rotatedImage = rotateWithoutCaching(image, roundedDegrees, crop);
        cache.putImage(keyString, rotatedImage);
        return rotatedImage;
    }

    // Rounds an angle to a whole degree and wraps it into 0-359, so equal-looking angles share one cache key
    private int toWholeDegree (double angle) {
        int wholeDegree = (int) (Math.round(angle) % 360);
        return (wholeDegree + 360) % 360;
    }

    private BufferedImage rotateWithoutCaching (BufferedImage image, int roundedDegrees, boolean crop) {
        // Convert the angle to radians
        double rad = Math.toRadians(roundedDegrees);

        // Calculate the diagonal length of the image
        double diagonal = Math.sqrt(Math.pow(image.getWidth(), 2) + Math.pow(image.getHeight(), 2));

        // Create a new image that is a square with side length equal to the diagonal of the original image
        BufferedImage bufferedImage = new BufferedImage((int) diagonal, (int) diagonal, BufferedImage.TYPE_INT_ARGB);

        // Create a graphics object to draw the original image onto the square image
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();

        // Calculate the center of the image
        int centerX = image.getWidth() / 2;
        int centerY = image.getHeight() / 2;

        // Calculate how much to translate the image so that it is centered
        int translateX = (bufferedImage.getWidth() - image.getWidth()) / 2;
        int translateY = (bufferedImage.getHeight() - image.getHeight()) / 2;

        // Move the image to the center of the square image
        AffineTransform tx = AffineTransform.getTranslateInstance(translateX, translateY);

        // Rotate the image around its center
        tx.rotate(rad, centerX, centerY);

        // Draw the image with the transform applied
        g.drawImage(image, tx, null);
        g.dispose();

        // Crop the image to remove any unnecessary transparent space
        if(crop) {
            bufferedImage = cropTransparentPixels(bufferedImage);
        }

        return bufferedImage;
    }


    private BufferedImage cropTransparentPixels(BufferedImage image) {
        int minX = image.getWidth();
        int minY = image.getHeight();
        int maxX = 0;
        int maxY = 0;

        // Traverse the image to find the bounding box of non-transparent pixels
        int[] alphaRow = new int[image.getWidth()];
        for (int y = 0; y < image.getHeight(); y++) {
            ImageCropper.getInstance().readAlphaRow(image, y, alphaRow);
            for (int x = 0; x < image.getWidth(); x++) {
                int alpha = alphaRow[x];
                if (alpha > 0) { // Pixel is not fully transparent
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }


        // Check if the bounding box is valid
        if (maxX < minX || maxY < minY) {
            System.out.println("Invalid bounds detected:");
            System.out.println("minX: " + minX);
            System.out.println("minY: " + minY);
            System.out.println("maxX: " + maxX);
            System.out.println("maxY: " + maxY);
            System.out.println("Returning the original image.");
            return image; // No valid non-transparent area found
        }


        // Calculate width and height for the subimage
        int subImageWidth = maxX - minX + 1;
        int subImageHeight = maxY - minY + 1;

        // Ensure subimage dimensions are within the image bounds
        if (minX < 0 || minY < 0 || minX + subImageWidth > image.getWidth() || minY + subImageHeight > image.getHeight()) {
            System.out.println("Invalid subimage dimensions calculated:");
            System.out.println("minX: " + minX);
            System.out.println("minY: " + minY);
            System.out.println("maxX: " + maxX);
            System.out.println("maxY: " + maxY);
            System.out.println("subImageWidth: " + subImageWidth);
            System.out.println("subImageHeight: " + subImageHeight);
            System.out.println("Image Width: " + image.getWidth());
            System.out.println("Image Height: " + image.getHeight());

            return image;
        }
        try {
            return image.getSubimage(minX, minY, subImageWidth, subImageHeight);
        } catch (RasterFormatException exception) {
            System.out.println(exception);
            return image;
        }
    }


    public double calculateAngle(int sourceX, int sourceY, int targetX, int targetY) {
        // Calculate the angle in radians from the horizontal axis to the line connecting the two points
        double angleRadians = Math.atan2(targetY - sourceY, targetX - sourceX);

        // Convert radians to degrees, range (-180, 180]
        double angleDegrees = Math.toDegrees(angleRadians);

        // Normalize the angle to the range [0, 360)
        if (angleDegrees < 0) {
            angleDegrees += 360;
        }

        // Round to 2 decimal places
        angleDegrees = Math.round(angleDegrees);

        return angleDegrees;
    }


    public BufferedImage rotateOrFlip(BufferedImage image, double angleDegrees, boolean crop) {
        // Round to a whole degree first, so the flip decision is the same for every angle that shares a cache key.
        int wholeDegree = toWholeDegree(angleDegrees);

        // Determine if the image is facing the left half of the circle and needs to be flipped vertically.
        boolean isLeftHalf = wholeDegree > 90 && wholeDegree < 270;

        if (!isLeftHalf) {
            return rotate(image, wholeDegree, crop);
        }

        String keyString = "rot_img:" + image.hashCode() + "_" + wholeDegree + "_" + crop + "_flip";
        BufferedImage cachedImage = cache.getImage(keyString);
        if (cachedImage != null) {
            return cachedImage;
        }

        // Mirror the angle for rotation, then apply the vertical flip after rotation. Only the flipped result is cached.
        BufferedImage flippedImage = flipVertically(rotateWithoutCaching(image, 360 - wholeDegree, crop));
        cache.putImage(keyString, flippedImage);
        return flippedImage;
    }

    // Same transformation as rotateOrFlip, but the result is not stored in the cache (for frames that are cached as a list)
    private BufferedImage rotateOrFlipWithoutCaching (BufferedImage image, double angleDegrees, boolean crop) {
        int wholeDegree = toWholeDegree(angleDegrees);
        boolean isLeftHalf = wholeDegree > 90 && wholeDegree < 270;
        if (!isLeftHalf) {
            return rotateWithoutCaching(image, wholeDegree, crop);
        }
        return flipVertically(rotateWithoutCaching(image, 360 - wholeDegree, crop));
    }

    public BufferedImage flipHorizontally (BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    public BufferedImage flipVertically (BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    public boolean isBlockedFromRotating (ImageEnums imageEnums){
        return blockedFromRotating.contains(imageEnums);
    }
}
