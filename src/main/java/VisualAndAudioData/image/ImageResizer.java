package VisualAndAudioData.image;

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
        if (scale != 0) {
            String cacheKey = image.hashCode() + "_" + scale;

            if (bufferedImageCache.containsKey(cacheKey)) {
                return bufferedImageCache.get(cacheKey);
            }

            transform.setToIdentity();
            transform.scale(scale, scale);
            transformop = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

            bufferedImage = transformop.filter(image, null);
            bufferedImageCache.put(cacheKey, bufferedImage);

            return bufferedImage;
        }
        return image;
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
            bufferedImage = getScaledImage(frames.get(i), scale);
            newFrames.add(bufferedImage);
        }

        bufferedImageListCache.put(cacheKey, newFrames);

        return newFrames;
    }

    public BufferedImage resizeImageToDimensions(BufferedImage image, int width, int height) {
        String cacheKey = image.hashCode() + "_" + width + "x" + height;

        if (bufferedImageCache.containsKey(cacheKey)) {
            return bufferedImageCache.get(cacheKey);
        }

        double scaleX = (double) width / image.getWidth();
        double scaleY = (double) height / image.getHeight();
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BICUBIC);

        bufferedImage = bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
        bufferedImageCache.put(cacheKey, bufferedImage);

        return bufferedImage;
    }


}
