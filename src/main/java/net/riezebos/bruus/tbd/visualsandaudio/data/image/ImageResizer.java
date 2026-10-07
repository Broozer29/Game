package net.riezebos.bruus.tbd.visualsandaudio.data.image;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImageResizer {

    private static ImageResizer instance = new ImageResizer();
    private BufferedImage bufferedImage = null;
    private AffineTransform transform = new AffineTransform();
    private AffineTransformOp transformop = null;

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

        bufferedImage = scaleWithoutCaching(image, scale);
        cache.putImage(keyString, bufferedImage);

        return bufferedImage;
    }

    private BufferedImage scaleWithoutCaching(BufferedImage image, float scale) {
        transform.setToIdentity();
        transform.scale(scale, scale);
        transformop = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

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
        ArrayList<BufferedImage> newFrames = new ArrayList<>();
        for (int i = 0; i < frames.size(); i++) {
            if (scale == 0) {
                bufferedImage = frames.get(i);
            } else {
                bufferedImage = scaleWithoutCaching(frames.get(i), scale);
            }
            newFrames.add(bufferedImage);
        }

        cache.putFrames(keyString, newFrames);

        return newFrames;
    }

    public BufferedImage resizeImageToDimensions(BufferedImage image, int width, int height) {
        if (width >= 2147483647) {
            System.out.println("Width dimension too large, probably tried to divide or multiply by 0");
            return bufferedImage;
        }
        String keyString = "scale_img:" + image.hashCode() + "_" + width + "x" + height;

        BufferedImage cachedImage = cache.getImage(keyString);
        if (cachedImage != null) {
            return cachedImage;
        }

        double scaleX = (double) width / image.getWidth();
        double scaleY = (double) height / image.getHeight();
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BICUBIC);

        bufferedImage = bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
        cache.putImage(keyString, bufferedImage);

        return bufferedImage;
    }

}
