package net.riezebos.bruus.tbd.visualsandaudio.data.image;

import net.riezebos.bruus.tbd.DevTestSettings;

import java.awt.image.BufferedImage;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

// Every rotating sprite kind except the lasers keeps its unrotated picture and is drawn rotated (see RotationGeometry), plus the check against the real rotated copy
public final class RotateWhileDrawing {

    // Kinds taken back out: these still get a real rotated copy. Each entry says why
    private static final Set<ImageEnums> EXCLUDED = EnumSet.noneOf(ImageEnums.class);

    // Lasers call rotateAnimation directly and are never drawn rotated
    private static final Set<ImageEnums> LASERS = EnumSet.of(
            ImageEnums.LaserbeamBody, ImageEnums.LaserbeamStart, ImageEnums.LaserbeamEnd, ImageEnums.LaserbeamCharging,
            ImageEnums.PinkLaserbeamBody, ImageEnums.PinkLaserbeamStart, ImageEnums.PinkLaserbeamEnd, ImageEnums.PinkLaserbeamCharging);

    private static final AtomicInteger checked = new AtomicInteger();
    private static final AtomicInteger mismatches = new AtomicInteger();
    private static final AtomicInteger edgeDifferences = new AtomicInteger();

    private RotateWhileDrawing () {
    }

    public static boolean isEnabled (ImageEnums imageEnum) {
        if (imageEnum == null || ImageRotator.getInstance().isBlockedFromRotating(imageEnum)) {
            return false;
        }
        return !LASERS.contains(imageEnum) && !EXCLUDED.contains(imageEnum);
    }

    // Checks one rotated single image against the real copy
    public static void LOG_verifySingle (ImageEnums kind, BufferedImage base, double angle, boolean crop, RotationGeometry.Geometry geometry) {
        if (!DevTestSettings.verifyRotateWhileDrawing) {
            return;
        }
        BufferedImage real = ImageRotator.getInstance().rotateOrFlip(base, angle, crop);
        int[] offset = crop ? LOG_contentOffset(base, angle) : null;
        LOG_compare(kind, angle, real, offset == null ? new int[] {0, 0} : offset, geometry);
    }

    // Checks the frames of one rotation against the real copies. perFrame: every frame is cropped to its own content, otherwise one crop box covers all frames
    public static void LOG_verifyFrames (ImageEnums kind, List<BufferedImage> base, List<BufferedImage> real, double angle, boolean perFrame, boolean crop, RotationGeometry.Geometry[] geometries) {
        if (!DevTestSettings.verifyRotateWhileDrawing) {
            return;
        }
        if (real.size() != geometries.length) {
            LOG_report(kind, angle, "frames " + real.size() + "/" + geometries.length);
            return;
        }
        int[] shared = null;
        if (!perFrame) {
            for (BufferedImage frame : base) {
                int[] offset = LOG_contentOffset(frame, angle);
                if (offset == null) {
                    continue;
                }
                shared = shared == null ? offset : new int[] {Math.min(shared[0], offset[0]), Math.min(shared[1], offset[1])};
            }
            if (shared == null) {
                shared = new int[] {0, 0};
            }
        }
        for (int i = 0; i < geometries.length; i++) {
            int[] expected = shared;
            if (perFrame) {
                int[] own = crop ? LOG_contentOffset(base.get(i), angle) : null;
                expected = own == null ? new int[] {0, 0} : own;
            }
            LOG_compare(kind, angle, real.get(i), expected, geometries[i]);
        }
    }

    // Top-left of the alpha > 0 content of the uncropped copy, or null when the copy has no content
    private static int[] LOG_contentOffset (BufferedImage base, double angle) {
        BufferedImage uncropped = ImageRotator.getInstance().rotateOrFlip(base, angle, false);
        int width = uncropped.getWidth();
        int[] row = new int[width];
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        for (int y = 0; y < uncropped.getHeight(); y++) {
            ImageCropper.getInstance().readAlphaRow(uncropped, y, row);
            for (int x = 0; x < width; x++) {
                if (row[x] > 0) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                }
            }
        }
        return minX == Integer.MAX_VALUE ? null : new int[] {minX, minY};
    }

    private static void LOG_compare (ImageEnums kind, double angle, BufferedImage real, int[] offset, RotationGeometry.Geometry geometry) {
        int total = checked.incrementAndGet();
        LOG_check(kind, angle, "width", real.getWidth(), geometry.width);
        LOG_check(kind, angle, "height", real.getHeight(), geometry.height);
        LOG_check(kind, angle, "offset x", offset[0], geometry.cropX);
        LOG_check(kind, angle, "offset y", offset[1], geometry.cropY);
        if (total % 1000 == 0) {
            System.out.println("rotate-verify total checked=" + total + " mismatches=" + mismatches.get() + " edge=" + edgeDifferences.get());
        }
    }

    // Accepted by Nelis on 2026-10-18: at exactly 45, 135, 225 and 315 degrees a value may differ by 1 pixel (a rounding tie at the diagonals)
    private static void LOG_check (ImageEnums kind, double angle, String what, int real, int computed) {
        if (real == computed) {
            return;
        }
        int wholeDegree = (int) ((Math.round(angle) % 360 + 360) % 360);
        boolean diagonal = wholeDegree % 90 == 45;
        if (diagonal && Math.abs(real - computed) <= 1) {
            edgeDifferences.incrementAndGet();
            return;
        }
        int totalMismatches = mismatches.incrementAndGet();
        System.out.println("rotate-verify MISMATCH " + kind + " " + angle + " " + what + " " + real + "/" + computed + " (total " + totalMismatches + ")");
    }

    private static void LOG_report (ImageEnums kind, double angle, String what) {
        int total = mismatches.incrementAndGet();
        System.out.println("rotate-verify MISMATCH " + kind + " " + angle + " " + what + " (total " + total + ")");
    }

    public static String LOG_getSummaryLine () {
        return "rotate-verify level end: checked=" + checked.get() + " mismatches=" + mismatches.get() + " edge=" + edgeDifferences.get();
    }

    public static int LOG_getChecked () {
        return checked.get();
    }

    public static int LOG_getMismatches () {
        return mismatches.get();
    }
}
