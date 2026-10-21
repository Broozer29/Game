package net.riezebos.bruus.tbd.visualsandaudio.data.image;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

// Remembers which ImageEnums name each image came from, so cached copies can be traced back to their sprite.
// Weak keys (BufferedImage compares by identity) let dropped copies be collected.
public class LOG_ImageNames {
    private static final Map<BufferedImage, String> NAMES = Collections.synchronizedMap(new WeakHashMap<>());

    private LOG_ImageNames() {
    }

    // The first name an image gets is kept
    public static void register(BufferedImage image, String name) {
        if (image == null || name == null) {
            return;
        }
        NAMES.putIfAbsent(image, name);
    }

    // The copy gets the name of its source, when the source is known
    public static void inherit(BufferedImage copy, BufferedImage source) {
        if (copy == null || source == null) {
            return;
        }
        String name = NAMES.get(source);
        if (name != null) {
            register(copy, name);
        }
    }

    // Every copy takes the name of the first source
    public static void inheritAll(List<BufferedImage> copies, List<BufferedImage> sources) {
        if (copies == null || sources == null || sources.isEmpty()) {
            return;
        }
        BufferedImage first = sources.get(0);
        for (BufferedImage copy : copies) {
            inherit(copy, first);
        }
    }

    public static String nameOf(BufferedImage image) {
        if (image == null) {
            return "?";
        }
        String name = NAMES.get(image);
        return name == null ? "?" : name;
    }
}
