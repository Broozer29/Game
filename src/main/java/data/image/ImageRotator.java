package data.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import data.movement.Direction;


public class ImageRotator {

	private static ImageRotator instance = new ImageRotator();

	private ImageRotator() {

	}

	public static ImageRotator getInstance() {
		return instance;
	}

	private BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		BufferedImage buff = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = buff.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return buff;
	}

	public ArrayList<Image> getRotatedFrames(List<Image> frames, Direction rotation) {
		ArrayList<Image> newFrames = new ArrayList<Image>();
		for (int i = 0; i < frames.size(); i++) {
			Image temp = frames.get(i);
			temp = rotate(temp, rotation);
			newFrames.add(temp);
		}
		return newFrames;
	}

	private int getNumquadrants(Direction rotation) {
		int numquadrants = 1;
		switch (rotation) {
		case UP:
			numquadrants = 1;
			break;
		case DOWN:
			numquadrants = 3;
			break;
		case LEFT:
			numquadrants = 0;
			break;
		case LEFT_UP:
			numquadrants = 0;
			break;
		case LEFT_DOWN:
			numquadrants = 0;
			break;
		case RIGHT:
			numquadrants = 2;
			break;
		case RIGHT_UP:
			numquadrants = 2;
			break;
		case RIGHT_DOWN:
			numquadrants = 2;
			break;
		}

		return numquadrants;
	}

	public BufferedImage rotate(Image image, Direction rotation) {
		BufferedImage bImage = toBufferedImage(image);
		int numquadrants = getNumquadrants(rotation);

		if (numquadrants == 2) {
			return flipImage(bImage);
		}
		int w0 = bImage.getWidth();
		int h0 = bImage.getHeight();
		int w1 = w0;
		int h1 = h0;

		int centerX = w0 / 2;
		int centerY = h0 / 2;

		if (numquadrants % 2 == 1) {
			w1 = h0;
			h1 = w0;
		}

		if (numquadrants % 4 == 1) {
			if (w0 > h0) {
				centerX = h0 / 2;
				centerY = h0 / 2;
			} else if (h0 > w0) {
				centerX = w0 / 2;
				centerY = w0 / 2;
			}
			// if h0 == w0, then use default
		} else if (numquadrants % 4 == 3) {
			if (w0 > h0) {
				centerX = w0 / 2;
				centerY = w0 / 2;
			} else if (h0 > w0) {
				centerX = h0 / 2;
				centerY = h0 / 2;
			}
			// if h0 == w0, then use default
		}

		AffineTransform affineTransform = new AffineTransform();
		affineTransform.setToQuadrantRotation(numquadrants, centerX, centerY);

		AffineTransformOp opRotated = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BICUBIC);

		BufferedImage transformedImage = new BufferedImage(w1, h1, BufferedImage.TYPE_INT_ARGB);

		transformedImage = opRotated.filter(bImage, transformedImage);
		return transformedImage;
	}

	private BufferedImage flipImage(BufferedImage BImage) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-BImage.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		BufferedImage newimage = op.filter(BImage, null);
		return newimage;
	}

}
