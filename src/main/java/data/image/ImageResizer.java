package data.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageResizer {

	private static ImageResizer instance = new ImageResizer();
	private BufferedImage bufferedImage = null;
	private AffineTransform transform = new AffineTransform();
	private AffineTransformOp transformop = null;
	private ImageResizer() {

	}

	public static ImageResizer getInstance() {
		return instance;
	}

	public BufferedImage getScaledImage(BufferedImage image, float scale) {
		transform.setToIdentity();
		transform.scale(scale, scale);
		transformop = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		
		bufferedImage = transformop.filter(image, null); // null as the second parameter
		return bufferedImage;
	}

	public ArrayList<BufferedImage> getScaledFrames(List<BufferedImage> frames, float scale) {
		ArrayList<BufferedImage> newFrames = new ArrayList<BufferedImage>();
		for (int i = 0; i < frames.size(); i++) {
			bufferedImage = frames.get(i);
			bufferedImage = getScaledImage(bufferedImage, scale);
			newFrames.add(bufferedImage);
		}

		return newFrames;
	}

	public BufferedImage resizeImageToDimensions(BufferedImage image, int width, int height) {
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bufferedImage.createGraphics();

		// Draw the resized image onto the bufferedVersion
		g.drawImage(image.getScaledInstance(width, height, BufferedImage.SCALE_DEFAULT), 0, 0, null);
		g.dispose();

		return bufferedImage;
	}
}
