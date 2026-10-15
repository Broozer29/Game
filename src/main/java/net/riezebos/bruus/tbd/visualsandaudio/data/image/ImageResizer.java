package net.riezebos.bruus.tbd.visualsandaudio.data.image;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImageResizer {

    private static ImageResizer instance = new ImageResizer();

    private final ImageCache cache = ImageCache.getInstance();

    private ImageResizer() {
    }

    public static ImageResizer getInstance() {
        return instance;
    }


    public BufferedImage getScaledImage(BufferedImage image, float scale) {
        if (Math.abs(scale - 1) <= 0.01 || scale == 0) {
            return image;
        }

        String keyString = "scale_img:" + image.hashCode() + "_" + scale;
        BufferedImage cachedImage = cache.getImage(keyString);
        if (cachedImage != null) {
            return cachedImage;
        }

        long buildStart = System.nanoTime();
        BufferedImage bufferedImage = scaleWithoutCaching(image, scale);
        cache.recordBuild(keyString, System.nanoTime() - buildStart);
        cache.putImage(keyString, bufferedImage);

        return bufferedImage;
    }

    private BufferedImage scaleWithoutCaching(BufferedImage image, float scale) {
        AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp transformop = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

        return transformop.filter(image, null);
    }

    public List<BufferedImage> getScaledFrames(List<BufferedImage> frames, float scale) {
        if (Math.abs(scale - 1) <= 0.01) {
            return frames;
        }

        String keyString = "scale_frames:" + frames.stream()
                .map(image -> Integer.toString(image.hashCode()))
                .collect(Collectors.joining("_")) + "_" + scale;

        ArrayList<BufferedImage> cachedFrames = cache.getFrames(keyString);
        if (cachedFrames != null) {
            return cachedFrames;
        }

        // The frames are cached as one list, not one by one
        // The frames are built on all processor cores at once; collect() keeps the frame order
        long buildStart = System.nanoTime();
        ArrayList<BufferedImage> newFrames = frames.parallelStream()
                .map(frame -> scale == 0 ? frame : scaleWithoutCaching(frame, scale))
                .collect(Collectors.toCollection(ArrayList::new));
        cache.recordBuild(keyString, System.nanoTime() - buildStart);

        cache.putFrames(keyString, newFrames);

        return newFrames;
    }

    public BufferedImage resizeImageToDimensions(BufferedImage image, int width, int height) {
        if (width >= 2147483647) {
            System.out.println("Width dimension too large, probably tried to divide or multiply by 0");
            return image;
        }
        String keyString = "scale_img:" + image.hashCode() + "_" + width + "x" + height;

        BufferedImage cachedImage = cache.getImage(keyString);
        if (cachedImage != null) {
            return cachedImage;
        }

        long buildStart = System.nanoTime();
        double scaleX = (double) width / image.getWidth();
        double scaleY = (double) height / image.getHeight();
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BICUBIC);

        BufferedImage bufferedImage = bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
        cache.recordBuild(keyString, System.nanoTime() - buildStart);
        cache.putImage(keyString, bufferedImage);

        return bufferedImage;
    }

}
