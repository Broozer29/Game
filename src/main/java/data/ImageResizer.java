package data;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
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

	public Image getScaledImage(Image image, int width, int height, int scale) {
		// resize, internally chains as operation after loading
		if (scale >= 1) {
			image = image.getScaledInstance(width * scale, height * scale, Image.SCALE_SMOOTH);
		} else if (scale < 1) {
			image = image.getScaledInstance(width * scale, height * scale, Image.SCALE_AREA_AVERAGING);
		}

		// wait for image to be ready
		MediaTracker tracker = new MediaTracker(new java.awt.Container());
		tracker.addImage(image, 0);
		try {
			tracker.waitForAll();
		} catch (InterruptedException ex) {
			throw new RuntimeException("Image loading interrupted", ex);
		}
		return image;
	}

	
	//DIT WERKT NIET, ZEER WAARSCHIJNLIJK OMDAT HET NIET GEMAAKT IS OF GIFS TE LEZEN EN TE TRANSFORMEREN
	//COLOUR PALETTE MATCHED GEWOON NIET
	public ArrayList<Image> getScaledFrames(List<Image> frames, int scale) {
		ArrayList<Image> newFrames = new ArrayList<Image>();
		for (int i = 0; i < frames.size(); i++) {
			Image temp = frames.get(i);
			
			
			temp = getScaledImage(temp, temp.getWidth(null), temp.getHeight(null), scale);
//			BufferedImage bimage = new BufferedImage(temp.getWidth(null), temp.getHeight(null),
//					9);
//			
//			MediaTracker tracker = new MediaTracker(new java.awt.Container());
//			tracker.addImage(bimage, 0);
//			try {
//				tracker.waitForAll();
//			} catch (InterruptedException ex) {
//				throw new RuntimeException("Image loading interrupted", ex);
//			}
//			
//			
//			
//		    Graphics2D g = bimage.createGraphics();
//		    g.drawImage(temp, null, null);
//
//			newFrames.add(temp);
		}

		return newFrames;
	}
}
