package data.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageResizer {

	private static ImageResizer instance = new ImageResizer();

	private ImageResizer() {

	}

	public static ImageResizer getInstance() {
		return instance;
	}

	private BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		BufferedImage buff = null;
		try {
			buff = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = buff.createGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
		} catch (IllegalArgumentException | NullPointerException e) {
			// Handle the exception
			System.out.println("An exception occurred: " + e.getMessage());
			System.out.println("Image dimensions given: " + image.getWidth(null) + "," + image.getHeight(null) + " Make sure the image that is being resized is actually loaded the file");
		}

		return buff;
	}

	public BufferedImage getScaledImage(Image image, float scale) {
// Een harde cast, het kan omdat er geen afbeeldingen zijn met komma getallen qua dimensies, maar indien die er zijn gaat dit tot problemen leiden		
		int newWidth = (int) Math.floor((image.getWidth(null) * scale));
		int newHeight = (int) Math.floor((image.getHeight(null) * scale));

		BufferedImage before = toBufferedImage(image);
		BufferedImage after = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(before, after);
		return after;
	}

	public ArrayList<Image> getScaledFrames(List<Image> frames, float scale) {
		ArrayList<Image> newFrames = new ArrayList<Image>();
		for (int i = 0; i < frames.size(); i++) {
			Image temp = frames.get(i);
			BufferedImage tempBuffer = getScaledImage(temp, scale);
			Graphics2D g = tempBuffer.createGraphics();
			g.drawImage(tempBuffer, 0, 0, null);
//
			newFrames.add(tempBuffer);
		}

		return newFrames;
	}

	public BufferedImage resizeImageToDimensions(Image image, int width, int height) {
		// Een harde cast, het kan omdat er geen afbeeldingen zijn met komma getallen qua
		// dimensies, maar indien die er zijn gaat dit tot problemen leiden
		Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		BufferedImage bufferedVersion = toBufferedImage(resizedImage);
		return bufferedVersion;
	}
}
