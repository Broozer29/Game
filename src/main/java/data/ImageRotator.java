package data;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class ImageRotator {

	private static ImageRotator instance = new ImageRotator();

	private ImageRotator() {

	}

	public static ImageRotator getInstance() {
		return instance;
	}

	public Image rotate(Image image, double angle) {
		BufferedImage bufferedImage = toBufferedImage(image);
		AffineTransform tx = new AffineTransform();
		tx.rotate(angle, bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);

		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		bufferedImage = op.filter(bufferedImage, null);
		return bufferedImage;
	}

	private BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		BufferedImage buff = new BufferedImage(image.getWidth(null) + 5, image.getHeight(null) + 5,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = buff.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return buff;
	}
}
