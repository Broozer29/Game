package VisualAndAudioData.image;

import java.awt.image.BufferedImage;

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
        BufferedImage cropped = img.getSubimage(0, 0, newWidth, img.getHeight());
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
}