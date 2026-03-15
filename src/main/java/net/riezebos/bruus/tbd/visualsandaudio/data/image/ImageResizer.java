package net.riezebos.bruus.tbd.visualsandaudio.data.image;

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

    private Map<ImageCacheKey, BufferedImage> bufferedImageCache = new HashMap<>();
    private Map<ImageCacheKey, ArrayList<BufferedImage>> bufferedImageListCache = new HashMap<>();

    private ImageResizer() {
    }

    public static ImageResizer getInstance() {
        return instance;
    }

    public BufferedImage getScaledImage(BufferedImage image, float scale) {
        if (Math.abs(scale - 1) <= 0.01 || scale == 0) {
            return image;
        }

        String keyString = image.hashCode() + "_" + scale;
        ImageCacheKey imageCacheKey = findOrCreateCacheKey(bufferedImageCache, keyString);

        if (imageCacheKey != null && bufferedImageCache.containsKey(imageCacheKey)) {
            imageCacheKey.updateAccessTime();
            return bufferedImageCache.get(imageCacheKey);
        }

        imageCacheKey = new ImageCacheKey(keyString);
        transform.setToIdentity();
        transform.scale(scale, scale);
        transformop = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

        bufferedImage = transformop.filter(image, null);
        bufferedImageCache.put(imageCacheKey, bufferedImage);

        return bufferedImage;
    }

    public List<BufferedImage> getScaledFrames(List<BufferedImage> frames, float scale) {
        if (Math.abs(scale - 1) <= 0.01) {
            return frames;
        }

        String keyString = frames.stream()
                .map(image -> Integer.toString(image.hashCode()))
                .collect(Collectors.joining("_")) + "_" + scale;

        ImageCacheKey imageCacheKey = findOrCreateCacheKey(bufferedImageListCache, keyString);
        if (imageCacheKey != null && bufferedImageListCache.containsKey(imageCacheKey)) {
            imageCacheKey.updateAccessTime();
            return bufferedImageListCache.get(imageCacheKey);
        }

        imageCacheKey = new ImageCacheKey(keyString);
        ArrayList<BufferedImage> newFrames = new ArrayList<>();
        for (int i = 0; i < frames.size(); i++) {
            bufferedImage = getScaledImage(frames.get(i), scale);
            newFrames.add(bufferedImage);
        }

        bufferedImageListCache.put(imageCacheKey, newFrames);

        return newFrames;
    }

    public BufferedImage resizeImageToDimensions(BufferedImage image, int width, int height) {
        if (width >= 2147483647) {
            System.out.println("Width dimension too large, probably tried to divide or multiply by 0");
            return bufferedImage;
        }
        String keyString = image.hashCode() + "_" + width + "x" + height;

        ImageCacheKey imageCacheKey = findOrCreateCacheKey(bufferedImageCache, keyString);
        if (imageCacheKey != null && bufferedImageCache.containsKey(imageCacheKey)) {
            imageCacheKey.updateAccessTime();
            return bufferedImageCache.get(imageCacheKey);
        }

        imageCacheKey = new ImageCacheKey(keyString);
        double scaleX = (double) width / image.getWidth();
        double scaleY = (double) height / image.getHeight();
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BICUBIC);

        bufferedImage = bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
        bufferedImageCache.put(imageCacheKey, bufferedImage);

        return bufferedImage;
    }

    private <T> ImageCacheKey findOrCreateCacheKey(Map<ImageCacheKey, T> cache, String keyString) {
        for (ImageCacheKey key : cache.keySet()) {
            if (key.getKey().equals(keyString)) {
                return key;
            }
        }
        return null;
    }

    /**
     * Removes all cache entries that have not been accessed in more than 5 minutes (300000 milliseconds).
     * This method should be called periodically to prevent memory buildup.
     */
    public void cleanupOldCacheEntries() {
        long maxAge = 300000; // 5 minutes in milliseconds

        bufferedImageCache.entrySet().removeIf(entry ->
            entry.getKey().getTimeSinceLastAccess() > maxAge
        );

        bufferedImageListCache.entrySet().removeIf(entry ->
            entry.getKey().getTimeSinceLastAccess() > maxAge
        );
    }

}
