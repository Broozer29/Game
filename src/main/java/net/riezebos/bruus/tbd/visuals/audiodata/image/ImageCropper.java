package net.riezebos.bruus.tbd.visuals.audiodata.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageCropper {
    private static ImageCropper instance = new ImageCropper();
    private static final int SKIP_PIXELS = 2;  // Check every 2nd pixel

    private ImageCropper() {

    }

    public static ImageCropper getInstance() {
        return instance;
    }

    public BufferedImage cropImage(BufferedImage img, float widthPercentage) {
        int newWidth = (int) Math.ceil(img.getWidth() * widthPercentage / 100);

        // Create a new BufferedImage to avoid shared references
        BufferedImage cropped = new BufferedImage(newWidth, img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = cropped.createGraphics();

        // Draw the desired portion of the original image into the new image
        g.drawImage(img, 0, 0, newWidth, img.getHeight(), 0, 0, newWidth, img.getHeight(), null);
        g.dispose();

        return cropped;
    }
    
    public BufferedImage cropToContent(BufferedImage img) {
        int minX = img.getWidth();
        int minY = img.getHeight();
        int maxX = 0;
        int maxY = 0;

        for (int y = 0; y < img.getHeight() - SKIP_PIXELS; y += SKIP_PIXELS) {
            for (int x = 0; x < img.getWidth() - SKIP_PIXELS; x += SKIP_PIXELS) {
                int alpha = (img.getRGB(x, y) >> 24) & 255;
                if (alpha > 0) {
                    if (x < minX)
                        minX = x;
                    if (y < minY)
                        minY = y;
                    if (x > maxX)
                        maxX = x;
                    if (y > maxY)
                        maxY = y;
                }
            }
        }

        // Adjust the borders to account for skipped pixels
        minX = Math.max(0, minX - SKIP_PIXELS);
        minY = Math.max(0, minY - SKIP_PIXELS);
        maxX = Math.min(img.getWidth() - 1, maxX + SKIP_PIXELS);
        maxY = Math.min(img.getHeight() - 1, maxY + SKIP_PIXELS);

        if (maxX <= minX || maxY <= minY) {
            // Either fully transparent image or some error occurred
            // Return the original image or handle the situation in a different way (e.g., throw an exception)
            return img;
        }

        BufferedImage cropped = img.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
        return cropped;
    }

    public void cropFramesToUniformContent(List<BufferedImage> frames) {
        int globalMinX = Integer.MAX_VALUE;
        int globalMinY = Integer.MAX_VALUE;
        int globalMaxX = 0;
        int globalMaxY = 0;

        // First pass: find the global cropping bounds
        for (BufferedImage img : frames) {
            int minX = img.getWidth();
            int minY = img.getHeight();
            int maxX = 0;
            int maxY = 0;

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int alpha = (img.getRGB(x, y) >> 24) & 255;
                    if (alpha > 0) {
                        minX = Math.min(minX, x);
                        minY = Math.min(minY, y);
                        maxX = Math.max(maxX, x);
                        maxY = Math.max(maxY, y);
                    }
                }
            }

            globalMinX = Math.min(globalMinX, minX);
            globalMinY = Math.min(globalMinY, minY);
            globalMaxX = Math.max(globalMaxX, maxX);
            globalMaxY = Math.max(globalMaxY, maxY);
        }

        // Second pass: crop all frames to these global bounds
        for (int i = 0; i < frames.size(); i++) {
            BufferedImage img = frames.get(i);
            int width = Math.min(globalMaxX - globalMinX + 1, img.getWidth() - globalMinX);
            int height = Math.min(globalMaxY - globalMinY + 1, img.getHeight() - globalMinY);
            if (width > 0 && height > 0) {
                frames.set(i, img.getSubimage(globalMinX, globalMinY, width, height));
            }
        }
    }

}