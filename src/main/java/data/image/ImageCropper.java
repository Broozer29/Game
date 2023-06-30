package data.image;

import java.awt.image.BufferedImage;

public class ImageCropper {
	private static ImageCropper instance = new ImageCropper();

	private ImageCropper() {

	}

	public static ImageCropper getInstance() {
		return instance;
	}

	public BufferedImage cropImage(BufferedImage img, float widthPercentage) {
		// Calculate the width to crop to
		int newWidth = (int) Math.ceil(img.getWidth() * widthPercentage / 100);

		// Crop the image
		BufferedImage cropped = img.getSubimage(0, 0, newWidth, img.getHeight());

		return cropped;
	}
}
