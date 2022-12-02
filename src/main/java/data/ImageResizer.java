package data;

import java.awt.Image;
import java.awt.MediaTracker;
import java.util.List;

import javax.swing.ImageIcon;
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
	
	public List<ImageIcon> getScaledFrames(List<ImageIcon> frames){
		
		return null;
	}
}
