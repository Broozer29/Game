package data.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;

public class ImageRotator {

	private static ImageRotator instance = new ImageRotator();
	private BufferedImage bufferedImage = null;
	private AffineTransform transform = new AffineTransform();
	private AffineTransformOp transformop = null;

	private ImageRotator() {

	}

	public static ImageRotator getInstance() {
		return instance;
	}

	public ArrayList<BufferedImage> getRotatedFrames(List<BufferedImage> frames, Direction rotation) {
		ArrayList<BufferedImage> newFrames = new ArrayList<BufferedImage>();
		for (int i = 0; i < frames.size(); i++) {
			bufferedImage = frames.get(i);
			bufferedImage = rotate(bufferedImage, rotation);
			newFrames.add(bufferedImage);
		}
		return newFrames;
	}

	// In ImageRotator class
	public BufferedImage rotate(BufferedImage image, Direction direction) {
		double angle = direction.toAngle();
		return rotate(image, angle);
	}

	
	public BufferedImage rotate(BufferedImage image, double angle) {
	    // Convert the angle to radians
	    double rad = Math.toRadians(angle);

	    // Calculate the diagonal length of the image
	    double diagonal = Math.sqrt(Math.pow(image.getWidth(), 2) + Math.pow(image.getHeight(), 2));

	    // Create a new image that is a square with side length equal to the diagonal of the
	    // original image
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
	    bufferedImage = cropTransparentPixels(bufferedImage);
	    
	    bufferedImage = ImageCropper.getInstance().cropToContent(bufferedImage);
	    return bufferedImage;
	}
	
	
	//Old, set back if it doesnt work
//	public BufferedImage rotate(BufferedImage image, double angle) {
//		// Convert the angle to radians
//		transform.setToIdentity();
//		double rad = Math.toRadians(angle);
//
//		// Calculate the diagonal length of the image
//		double diagonal = Math.sqrt(Math.pow(image.getWidth(), 2) + Math.pow(image.getHeight(), 2));
//
//		// Create a new image that is a square with side length equal to the diagonal of the
//		// original image
//		bufferedImage = new BufferedImage((int) diagonal, (int) diagonal, BufferedImage.TYPE_INT_ARGB);
//
//		// Create a graphics object to draw the original image onto the square image
//		Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
//
//		// Draw the original image centered onto the square image
//		int x = (int) ((diagonal - image.getWidth()) / 2);
//		int y = (int) ((diagonal - image.getHeight()) / 2);
//		g.drawImage(image, x, y, null);
//		g.dispose();
//
//		// Create an affine transform to rotate the square image
//		transform.rotate(rad, bufferedImage.getWidth() / 2.0, bufferedImage.getHeight() / 2.0);
//
//		// Create an affine transform operation
//		transformop = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
//
//		// Apply the operation and return the result
//		bufferedImage = cropTransparentPixels(bufferedImage);
//		return transformop.filter(bufferedImage, null);
//	}

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
