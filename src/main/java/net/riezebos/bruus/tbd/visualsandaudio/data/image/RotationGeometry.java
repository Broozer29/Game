package net.riezebos.bruus.tbd.visualsandaudio.data.image;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;

// Works out what ImageRotator gives for a rotated copy (size, crop offset, pixel mapping) without building the copy
public class RotationGeometry {

    private static final int ALPHA_THRESHOLD = 50; // Same threshold as the collision mask

    // Content boxes per base image and whole degree, as a few ints only: {canvas side, minX, minY, maxX, maxY} in the final (flipped) orientation
    // AtomicReferenceArray, so a box written on one thread is seen complete on every other thread
    private static final Map<BufferedImage, AtomicReferenceArray<int[]>> contentBoxCache = Collections.synchronizedMap(new WeakHashMap<>());

    // width/height: size of the copy. cropX/cropY: offset of the copy inside the uncropped canvas, in the copy's final orientation
    // (so for a flipped copy the offset is measured after the vertical flip). transform: base pixels to copy pixels.
    public static final class Geometry {
        public final int width;
        public final int height;
        public final int cropX;
        public final int cropY;
        public final boolean flipped;
        public final AffineTransform transform;
        public final AffineTransform inverse;

        private Geometry (int width, int height, int cropX, int cropY, boolean flipped, AffineTransform transform) {
            this.width = width;
            this.height = height;
            this.cropX = cropX;
            this.cropY = cropY;
            this.flipped = flipped;
            this.transform = transform;
            try {
                this.inverse = transform.createInverse();
            } catch (NoninvertibleTransformException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private RotationGeometry () {
    }

    // Same as ImageRotator.toWholeDegree
    private static int toWholeDegree (double angle) {
        int wholeDegree = (int) (Math.round(angle) % 360);
        return (wholeDegree + 360) % 360;
    }

    // Same as ImageRotator.rotateOrFlip: single image, optionally cropped to its own content
    public static Geometry geometry (BufferedImage base, double angleDegrees, boolean crop) {
        int degree = toWholeDegree(angleDegrees);
        int[] box = contentBox(base, degree);
        int side = box[0];
        int minX = box[1];
        int minY = box[2];
        int maxX = box[3];
        int maxY = box[4];
        // An image without content is not cropped, like ImageRotator
        if (!crop || maxX < minX || maxY < minY) {
            return build(base, degree, side, 0, 0, side, side);
        }
        return build(base, degree, side, minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    // Same as ImageRotator.getRotatedFrames(frames, angle): one crop box over all frames, with the width and height clamp
    public static Geometry[] frameListGeometry (List<BufferedImage> frames, double angleDegrees) {
        int degree = toWholeDegree(angleDegrees);
        int globalMinX = Integer.MAX_VALUE;
        int globalMinY = Integer.MAX_VALUE;
        int globalMaxX = 0;
        int globalMaxY = 0;
        int[][] boxes = new int[frames.size()][];
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = contentBox(frames.get(i), degree);
            globalMinX = Math.min(globalMinX, boxes[i][1]);
            globalMinY = Math.min(globalMinY, boxes[i][2]);
            globalMaxX = Math.max(globalMaxX, boxes[i][3]);
            globalMaxY = Math.max(globalMaxY, boxes[i][4]);
        }
        Geometry[] result = new Geometry[boxes.length];
        for (int i = 0; i < boxes.length; i++) {
            int side = boxes[i][0];
            int width = Math.min(globalMaxX - globalMinX + 1, side - globalMinX);
            int height = Math.min(globalMaxY - globalMinY + 1, side - globalMinY);
            if (width > 0 && height > 0) {
                result[i] = build(frames.get(i), degree, side, globalMinX, globalMinY, width, height);
            } else {
                result[i] = build(frames.get(i), degree, side, 0, 0, side, side);
            }
        }
        return result;
    }

    // Whether the base image is more opaque than the collision threshold at the spot that copy pixel (copyX, copyY) shows
    public static boolean isSolid (BufferedImage base, Geometry geometry, int copyX, int copyY) {
        if (copyX < 0 || copyY < 0 || copyX >= geometry.width || copyY >= geometry.height) {
            return false;
        }
        double[] point = {copyX + 0.5, copyY + 0.5};
        geometry.inverse.transform(point, 0, point, 0, 1);
        int sourceX = (int) Math.floor(point[0]);
        int sourceY = (int) Math.floor(point[1]);
        if (sourceX < 0 || sourceY < 0 || sourceX >= base.getWidth() || sourceY >= base.getHeight()) {
            return false;
        }
        return ((base.getRGB(sourceX, sourceY) >> 24) & 0xff) > ALPHA_THRESHOLD;
    }

    // The rotation exactly as ImageRotator.rotateWithoutCaching builds it: integer translate and centre, canvas side (int) of the diagonal
    private static AffineTransform canvasTransform (BufferedImage base, int rotationDegrees, int side) {
        int centerX = base.getWidth() / 2;
        int centerY = base.getHeight() / 2;
        int translateX = (side - base.getWidth()) / 2;
        int translateY = (side - base.getHeight()) / 2;
        AffineTransform tx = AffineTransform.getTranslateInstance(translateX, translateY);
        tx.rotate(Math.toRadians(rotationDegrees), centerX, centerY);
        return tx;
    }

    private static int canvasSide (BufferedImage base) {
        return (int) Math.sqrt(Math.pow(base.getWidth(), 2) + Math.pow(base.getHeight(), 2));
    }

    private static Geometry build (BufferedImage base, int degree, int side, int cropX, int cropY, int width, int height) {
        boolean flipped = degree > 90 && degree < 270;
        AffineTransform tx = canvasTransform(base, flipped ? 360 - degree : degree, side);
        if (flipped) {
            // Vertical flip of the whole canvas, like ImageRotator.flipVertically
            tx.preConcatenate(new AffineTransform(1, 0, 0, -1, 0, side));
        }
        tx.preConcatenate(AffineTransform.getTranslateInstance(-cropX, -cropY));
        return new Geometry(width, height, cropX, cropY, flipped, tx);
    }

    // Bounding box of alpha > 0 pixels of the uncropped copy, found by sampling every canvas pixel centre through the inverse rotation
    private static int[] contentBox (BufferedImage base, int degree) {
        AtomicReferenceArray<int[]> perDegree = contentBoxCache.computeIfAbsent(base, key -> new AtomicReferenceArray<>(360));
        int[] cached = perDegree.get(degree);
        if (cached != null) {
            return cached;
        }
        int side = canvasSide(base);
        boolean flipped = degree > 90 && degree < 270;
        AffineTransform tx = canvasTransform(base, flipped ? 360 - degree : degree, side);
        AffineTransform inverse;
        try {
            inverse = tx.createInverse();
        } catch (NoninvertibleTransformException e) {
            throw new IllegalStateException(e);
        }
        double[] m = new double[6];
        inverse.getMatrix(m);
        int width = base.getWidth();
        int height = base.getHeight();
        int[] alpha = new int[width * height];
        int[] alphaRow = new int[width];
        for (int y = 0; y < height; y++) {
            ImageCropper.getInstance().readAlphaRow(base, y, alphaRow);
            System.arraycopy(alphaRow, 0, alpha, y * width, width);
        }
        // Same start values as ImageRotator.cropTransparentPixels, so an empty copy gives the same invalid box
        int minX = side;
        int minY = side;
        int maxX = 0;
        int maxY = 0;
        for (int y = 0; y < side; y++) {
            double py = y + 0.5;
            for (int x = 0; x < side; x++) {
                double px = x + 0.5;
                int sourceX = (int) Math.floor(m[0] * px + m[2] * py + m[4]);
                int sourceY = (int) Math.floor(m[1] * px + m[3] * py + m[5]);
                if (sourceX < 0 || sourceY < 0 || sourceX >= width || sourceY >= height) {
                    continue;
                }
                if (alpha[sourceY * width + sourceX] > 0) {
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }
        if (flipped && maxX >= minX && maxY >= minY) {
            int flippedMinY = side - 1 - maxY;
            maxY = side - 1 - minY;
            minY = flippedMinY;
        }
        int[] box = {side, minX, minY, maxX, maxY};
        perDegree.set(degree, box);
        return box;
    }
}
