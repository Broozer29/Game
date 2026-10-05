package net.riezebos.bruus.tbd.game.util.collision;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/*
 * Cached per-image bitmask of pixels whose alpha exceeds the collision threshold
 * Tl;dr we create and store the pixels that we want to check for collision ONCE so that we can re-use them when actually checking collision
 * Saving a lot of computation because we are only calling getRGB() once, instead of EVERY collision check and thus, improving performance drastically (in theory)
 */
final class AlphaMask {

    private static final int ALPHA_THRESHOLD = 50;
    // This data structure is new to me, requires further study for proper understanding, as of now it might be AI slop and I wouldn't even know it
    private static final Map<BufferedImage, AlphaMask> cache = Collections.synchronizedMap(new WeakHashMap<>());

    private final int wordsPerRow;
    private final long[] bits;

    private AlphaMask(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        this.wordsPerRow = (width + 63) >>> 6;
        this.bits = new long[wordsPerRow * height];

        int[] row = new int[width];
        for (int y = 0; y < height; y++) {
            image.getRGB(0, y, width, 1, row, 0, width);
            int base = y * wordsPerRow;
            for (int x = 0; x < width; x++) {
                if (((row[x] >> 24) & 0xff) > ALPHA_THRESHOLD) {
                    bits[base + (x >>> 6)] |= 1L << (x & 63);
                }
            }
        }
    }

    static AlphaMask of(BufferedImage image) {
        return cache.computeIfAbsent(image, AlphaMask::new);
    }

    boolean isOpaque(int x, int y) {
        return (bits[y * wordsPerRow + (x >>> 6)] & (1L << (x & 63))) != 0;
    }
}