package data.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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

	public BufferedImage getScaledImage(BufferedImage image, float scale) {
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		
		BufferedImage after = scaleOp.filter(image, null); // null as the second parameter
		return after;
	}

	public ArrayList<BufferedImage> getScaledFrames(List<BufferedImage> frames, float scale) {
		ArrayList<BufferedImage> newFrames = new ArrayList<BufferedImage>();
		for (int i = 0; i < frames.size(); i++) {
			BufferedImage temp = frames.get(i);
			BufferedImage tempBuffer = getScaledImage(temp, scale);
			newFrames.add(tempBuffer);
		}

		return newFrames;
	}

	public BufferedImage resizeImageToDimensions(BufferedImage image, int width, int height) {
		BufferedImage bufferedVersion = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bufferedVersion.createGraphics();

		// Draw the resized image onto the bufferedVersion
		g.drawImage(image.getScaledInstance(width, height, BufferedImage.SCALE_DEFAULT), 0, 0, null);
		g.dispose();

		return bufferedVersion;
	}
}
