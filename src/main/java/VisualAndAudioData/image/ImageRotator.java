package VisualAndAudioData.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import game.movement.Direction;

public class ImageRotator {

	private static ImageRotator instance = new ImageRotator();
	private BufferedImage bufferedImage = null;

	private Map<String, BufferedImage> rotatedImageCache = new HashMap<>();
	private Map<String, ArrayList<BufferedImage>> rotatedFramesCache = new HashMap<>();

	private ImageRotator() {

	}

	public static ImageRotator getInstance() {
		return instance;
	}

	public ArrayList<BufferedImage> getRotatedFrames(List<BufferedImage> frames, Direction rotation) {
		String cacheKey = frames.stream()
		        .map(image -> Integer.toString(image.hashCode()))
		        .collect(Collectors.joining("_")) + "_" + rotation;

		    if (rotatedFramesCache.containsKey(cacheKey)) {
		        return rotatedFramesCache.get(cacheKey);
		    }
		
		
		    ArrayList<BufferedImage> newFrames = new ArrayList<>();
		    for (BufferedImage frame : frames) {
		        newFrames.add(rotate(frame, rotation));
		    }
		    rotatedFramesCache.put(cacheKey, newFrames);
		    return newFrames;
	}

	// In ImageRotator class
	public BufferedImage rotate(BufferedImage image, Direction direction) {
		double angle = direction.toAngle();
		return rotate(image, angle);
	}

	
	public BufferedImage rotate(BufferedImage image, double angle) {
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

	    // If the angle is 180 degrees or more, flip the image horizontally
	    if (angle == 180 || angle == 225 || angle == 135) {
	        tx.scale(-1, 1);
	        tx.translate(-image.getWidth(), 0);
	        rad = Math.toRadians(angle - 180);
	    }

	    // If the angle is 90 or 270 degrees, flip the image vertically
	    if (angle == 90 || angle == 270) {
	        tx.scale(1, -1);
	        tx.translate(0, -image.getHeight());
	        if (angle == 90) rad = Math.toRadians(270);
	        else rad = Math.toRadians(90);
	    }

	    // Rotate the image around its center
	    tx.rotate(rad, centerX, centerY);

	    // Draw the image with the transform applied
	    g.drawImage(image, tx, null);
	    g.dispose();

	    // Crop the image to remove any unnecessary transparent space
	    bufferedImage = cropTransparentPixels(bufferedImage);

	    bufferedImage = ImageCropper.getInstance().cropToContent(bufferedImage);

	    rotatedImageCache.put(cacheKey, bufferedImage);
	    return bufferedImage;
	}



	
	public BufferedImage cropTransparentPixels(BufferedImage image) {
		int minX = image.getWidth();
		int minY = image.getHeight();
		int maxX = 0;
		int maxY = 0;

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int alpha = (image.getRGB(x, y) >> 24) & 255;
				if (alpha > 0) { // if pixel is not transparent
					if (x < minX)
						minX = x;
					if (y < minY)
						minY = y;
					if (x > maxX)
						maxX = x;
					if (y > maxY)
						maxY = y;
				}
			}
		}

		// Add 1 to maxX/maxY because subimage's second parameter is exclusive
		bufferedImage = image.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
		return bufferedImage;
	}

}
