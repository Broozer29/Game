package data.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageCropper {
	private static ImageCropper instance = new ImageCropper();

	private ImageCropper() {

	}

	public static ImageCropper getInstance() {
		return instance;
	}

	public BufferedImage cropImage(Image img, float widthPercentage) {
		// Convert the Image to a BufferedImage
		BufferedImage bimg = toBufferedImage(img);

		// Calculate the width to crop to
		int newWidth = (int) Math.ceil(bimg.getWidth() * widthPercentage / 100);

		// Crop the image
		BufferedImage cropped = bimg.getSubimage(0, 0, newWidth, bimg.getHeight());

		return cropped;
	}

	public BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bimage;
	}
}
