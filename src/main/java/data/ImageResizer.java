package data;

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

		BufferedImage buff = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = buff.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return buff;
	}

	public BufferedImage getScaledImage(Image image, int scale) {
		// resize, internally chains as operation after loading
		int newWidth = image.getWidth(null) * scale;
		int newHeight = image.getHeight(null) * scale;

		BufferedImage before = toBufferedImage(image);
		BufferedImage after = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(before, after);
		return after;
//
//		if (scale >= 1) {
//			image = image.getScaledInstance(width * scale, height * scale, Image.SCALE_SMOOTH);
//		} else if (scale < 1) {
//			image = image.getScaledInstance(width * scale, height * scale, Image.SCALE_AREA_AVERAGING);
//		}
//
//		// wait for image to be ready
//		MediaTracker tracker = new MediaTracker(new java.awt.Container());
//		tracker.addImage(image, 0);
//		try {
//			tracker.waitForAll();
//		} catch (InterruptedException ex) {
//			throw new RuntimeException("Image loading interrupted", ex);
//		}
//		return image;
	}

	// DIT WERKT NIET, ZEER WAARSCHIJNLIJK OMDAT HET NIET GEMAAKT IS OF GIFS TE
	// LEZEN EN TE TRANSFORMEREN
	// COLOUR PALETTE MATCHED GEWOON NIET
	public ArrayList<Image> getScaledFrames(List<Image> frames, int scale) {
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
}
