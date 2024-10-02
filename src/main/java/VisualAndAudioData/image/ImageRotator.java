package VisualAndAudioData.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import VisualAndAudioData.DataClass;
import game.managers.OnScreenTextManager;
import game.movement.Direction;
import game.movement.Point;

public class ImageRotator {

    private static ImageRotator instance = new ImageRotator();
    private BufferedImage bufferedImage = null;

    private Map<String, BufferedImage> rotatedImageCache = new HashMap<>();
    private Map<String, ArrayList<BufferedImage>> rotatedFramesCache = new HashMap<>();

    private ImageRotator () {

    }

    public static ImageRotator getInstance () {
        return instance;
    }

    public ArrayList<BufferedImage> getRotatedFrames (List<BufferedImage> frames, Direction rotation, boolean crop) {
        String cacheKey = frames.stream()
                .map(image -> Integer.toString(image.hashCode()))
                .collect(Collectors.joining("_")) + "_" + rotation;

        if (rotatedFramesCache.containsKey(cacheKey)) {
            return rotatedFramesCache.get(cacheKey);
        }


        ArrayList<BufferedImage> newFrames = new ArrayList<>();
        for (BufferedImage frame : frames) {
            newFrames.add(rotate(frame, rotation, crop));
        }
        rotatedFramesCache.put(cacheKey, newFrames);
        return newFrames;
    }

    public ArrayList<BufferedImage> getRotatedFrames (List<BufferedImage> frames, double angleInDegrees, boolean crop) {
        String cacheKey = frames.stream()
                .map(image -> Integer.toString(image.hashCode()))
                .collect(Collectors.joining("_")) + "_" + angleInDegrees;

        if (rotatedFramesCache.containsKey(cacheKey)) {
            return rotatedFramesCache.get(cacheKey);
        }

        // Prepare a list to store the adjusted frames, whether rotated or flipped
        ArrayList<BufferedImage> adjustedFrames = new ArrayList<>();


        // Process each frame using the rotateOrFlip method
        for (BufferedImage frame : frames) {
            BufferedImage adjustedFrame = rotateOrFlip(frame, angleInDegrees, false);
            adjustedFrames.add(adjustedFrame);
        }

        ImageCropper.getInstance().cropFramesToUniformContent(adjustedFrames);
        rotatedFramesCache.put(cacheKey, adjustedFrames);
        // Return the list of adjusted frames
        return adjustedFrames;
    }

    // In ImageRotator class
    public BufferedImage rotate (BufferedImage image, Direction direction, boolean crop) {
        double angle = direction.toAngle();
        return rotateOrFlip(image, angle, crop);
    }


    public BufferedImage rotate (BufferedImage image, double angle, boolean crop) {
        String cacheKey = image.hashCode() + "_" + angle;
        if (rotatedImageCache.containsKey(cacheKey)) {
            return rotatedImageCache.get(cacheKey);
        }

        // Convert the angle to radians
        double rad = Math.toRadians(angle);

        // Calculate the diagonal length of the image
        double diagonal = Math.sqrt(Math.pow(image.getWidth(), 2) + Math.pow(image.getHeight(), 2));

        // Create a new image that is a square with side length equal to the diagonal of the original image
        bufferedImage = new BufferedImage((int) diagonal, (int) diagonal, BufferedImage.TYPE_INT_ARGB);

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
//            bufferedImage = ImageCropper.getInstance().cropToContent(bufferedImage); Not needed as it's already cropped
        }

        rotatedImageCache.put(cacheKey, bufferedImage);
        return bufferedImage;
    }


    private BufferedImage cropTransparentPixels(BufferedImage image) {
        int minX = image.getWidth();
        int minY = image.getHeight();
        int maxX = 0;
        int maxY = 0;

        // Traverse the image to find the bounding box of non-transparent pixels
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int alpha = (image.getRGB(x, y) >> 24) & 255;
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
            // Crop the image using the calculated bounding box
            BufferedImage croppedImage = image.getSubimage(minX, minY, subImageWidth, subImageHeight);
            return croppedImage;
        } catch (RasterFormatException exception) {
            System.out.println("RasterFormatException occurred:");
            System.out.println(exception);
            System.out.println("Returning the original image.");
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
        // Normalize the angle to be within the range of 0 to 360.
        angleDegrees = (angleDegrees + 360) % 360;

        // Determine if the image is facing the left half of the circle and needs to be flipped vertically.
        boolean isLeftHalf = angleDegrees > 90 && angleDegrees < 270;

        // If the image is on the left half of the circle, mirror the angle for rotation.
        if (isLeftHalf) {
            angleDegrees = 360 - angleDegrees;
        }

        angleDegrees = Math.round(angleDegrees);
        // Rotate the image to the adjusted angle.
        BufferedImage processedImage = rotate(image, angleDegrees, crop);

        // If the image was on the left half of the circle, apply the vertical flip after rotation.
        if (isLeftHalf) {
            processedImage = flipVertically(processedImage);
        }

        return processedImage;
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

    public Point calculateFrontPosition(int centerX, int centerY, double angleDegrees, double distanceToFront) {
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the new position using trigonometry
        int newX = centerX + (int) (Math.cos(angleRadians) * distanceToFront);
        int newY = centerY + (int) (Math.sin(angleRadians) * distanceToFront);

        return new Point(newX, newY);
    }
}
