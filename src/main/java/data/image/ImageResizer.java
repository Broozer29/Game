package data.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImageResizer {

	private static ImageResizer instance = new ImageResizer();
	private BufferedImage bufferedImage = null;
	private AffineTransform transform = new AffineTransform();
	private AffineTransformOp transformop = null;

	private Map<String, BufferedImage> bufferedImageCache = new HashMap<>();
	private Map<String, ArrayList<BufferedImage>> bufferedImageListCache = new HashMap<>();

	private ImageResizer() {
	}

	public static ImageResizer getInstance() {
		return instance;
	}

	public BufferedImage getScaledImage(BufferedImage image, float scale) {
		String cacheKey = image.hashCode() + "_" + scale;

		if (bufferedImageCache.containsKey(cacheKey)) {
			return bufferedImageCache.get(cacheKey);
		}

		transform.setToIdentity();
		transform.scale(scale, scale);
		transformop = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

		bufferedImage = transformop.filter(image, null); // null as the second parameter
		bufferedImageCache.put(cacheKey, bufferedImage);

		return bufferedImage;
	}

	public ArrayList<BufferedImage> getScaledFrames(List<BufferedImage> frames, float scale) {
	    String cacheKey = frames.stream()
	        .map(image -> Integer.toString(image.hashCode()))
	        .collect(Collectors.joining("_")) + "_" + scale;

	    if (bufferedImageListCache.containsKey(cacheKey)) {
	        return bufferedImageListCache.get(cacheKey);
	    }

	    ArrayList<BufferedImage> newFrames = new ArrayList<>();
	    for (int i = 0; i < frames.size(); i++) {
	        bufferedImage = frames.get(i);
	        bufferedImage = getScaledImage(bufferedImage, scale);
	        newFrames.add(bufferedImage);
	    }

	    bufferedImageListCache.put(cacheKey, newFrames);

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
