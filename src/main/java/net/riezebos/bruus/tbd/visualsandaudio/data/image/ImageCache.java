package net.riezebos.bruus.tbd.visualsandaudio.data.image;

import net.riezebos.bruus.tbd.DevTestSettings;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

// One shared cache for rotated and resized images (single images and frame lists) with one memory budget.
// Least recently used entries are dropped first when the pixel data of all cached images exceeds the budget.
public class ImageCache {

    private static final long MEMORY_BUDGET_BYTES = 2_560L * 1024 * 1024;
    private static final long STATS_INTERVAL_MILLIS = 30_000;
    private static final DateTimeFormatter FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final DateTimeFormatter LINE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Must stay below the other static fields, the constructor reads them
    private static ImageCache instance = new ImageCache();

    // Access order: a lookup moves the entry to the end, so the first entry is the least recently used
    private final LinkedHashMap<String, CacheEntry> entries = new LinkedHashMap<>(1024, 0.75f, true);
    // How many cached images point to each pixel buffer (getSubimage views share the buffer of their parent)
    private final Map<DataBuffer, Integer> bufferReferences = new IdentityHashMap<>();
    private long countedBytes = 0;
    private long hits = 0;
    private long misses = 0;
    private long evictions = 0;
    private Path statsFile = null;

    private static class CacheEntry {
        private final List<BufferedImage> images;
        private final ArrayList<BufferedImage> frames;

        private CacheEntry(BufferedImage image) {
            this.images = List.of(image);
            this.frames = null;
        }

        private CacheEntry(ArrayList<BufferedImage> frames) {
            this.images = frames;
            this.frames = frames;
        }
    }

    private ImageCache() {
        String statsDir = System.getProperty("game.cacheStatsDir");
        if (statsDir != null && !statsDir.isBlank()) {
            try {
                Path dir = Paths.get(statsDir);
                Files.createDirectories(dir);
                statsFile = dir.resolve("cache-" + LocalDateTime.now().format(FILE_TIMESTAMP) + ".log");
                Timer statsTimer = new Timer("image-cache-stats", true);
                statsTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        writeStatsLine();
                    }
                }, STATS_INTERVAL_MILLIS, STATS_INTERVAL_MILLIS);
            } catch (IOException | RuntimeException exception) {
                System.out.println("Image cache stats disabled: " + exception);
                statsFile = null;
            }
        }
    }

    public static ImageCache getInstance() {
        return instance;
    }

    public synchronized BufferedImage getImage(String key) {
        CacheEntry entry = entries.get(key);
        if (entry == null || entry.frames != null) {
            misses++;
            return null;
        }
        hits++;
        return entry.images.get(0);
    }

    public synchronized ArrayList<BufferedImage> getFrames(String key) {
        CacheEntry entry = entries.get(key);
        if (entry == null || entry.frames == null) {
            misses++;
            return null;
        }
        hits++;
        return entry.frames;
    }

    public synchronized void putImage(String key, BufferedImage image) {
        add(key, new CacheEntry(image));
    }

    public synchronized void putFrames(String key, ArrayList<BufferedImage> frames) {
        add(key, new CacheEntry(frames));
    }

    public synchronized String getStatsLine() {
        return "entries=" + entries.size()
                + " mb=" + String.format("%.1f", countedBytes / (1024.0 * 1024.0))
                + " hits=" + hits
                + " misses=" + misses
                + " evictions=" + evictions;
    }

    private void add(String key, CacheEntry entry) {
        CacheEntry replaced = entries.remove(key);
        if (replaced != null) {
            release(replaced);
        }
        entries.put(key, entry);
        for (BufferedImage image : entry.images) {
            DataBuffer buffer = image.getRaster().getDataBuffer();
            Integer count = bufferReferences.get(buffer);
            if (count == null) {
                countedBytes += bytesOf(buffer);
                bufferReferences.put(buffer, 1);
            } else {
                bufferReferences.put(buffer, count + 1);
            }
        }

        if (DevTestSettings.disableImageCacheBudget) {
            return;
        }
        // The entry just added is the most recently used one, so it is only reached when it is the last one left
        Iterator<CacheEntry> eldest = entries.values().iterator();
        while (countedBytes > MEMORY_BUDGET_BYTES && entries.size() > 1 && eldest.hasNext()) {
            CacheEntry evicted = eldest.next();
            eldest.remove();
            release(evicted);
            evictions++;
        }
    }

    // Gives back the memory of the entry's images and flushes them. The entry itself must already be out of the map.
    private void release(CacheEntry entry) {
        for (BufferedImage image : entry.images) {
            DataBuffer buffer = image.getRaster().getDataBuffer();
            Integer count = bufferReferences.get(buffer);
            if (count != null) {
                if (count <= 1) {
                    bufferReferences.remove(buffer);
                    countedBytes -= bytesOf(buffer);
                } else {
                    bufferReferences.put(buffer, count - 1);
                }
            }
            image.flush();
        }
    }

    private long bytesOf(DataBuffer buffer) {
        return (long) buffer.getSize() * buffer.getNumBanks() * (DataBuffer.getDataTypeSize(buffer.getDataType()) / 8);
    }

    private void writeStatsLine() {
        Path file = statsFile;
        if (file == null) {
            return;
        }
        String line = LocalDateTime.now().format(LINE_TIMESTAMP) + " " + getStatsLine() + System.lineSeparator();
        try {
            Files.writeString(file, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException exception) {
            // Statistics are optional, never disturb the game
        }
    }
}
