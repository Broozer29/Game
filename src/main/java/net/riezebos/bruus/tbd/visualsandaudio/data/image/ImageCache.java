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
import java.util.*;

// One shared cache for rotated and resized images (single images and frame lists).
// The normal section has a memory budget: least recently used entries are dropped first when its pixel data exceeds it.
// The level section sits outside that budget and is never evicted. While the preloader builds a Generic enemy
// (beginLevelPreload until endLevelPreload), every put goes to the level section, from any thread. A lookup that hits
// the normal section leaves the entry there. clearLevelEntries() deletes the whole level section; nothing moves between the sections.
public class ImageCache {

    private static final long MEMORY_BUDGET_BYTES = 2_560L * 1024 * 1024;
    private static final long STATS_INTERVAL_MILLIS = 30_000;
    private static final DateTimeFormatter FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final DateTimeFormatter LINE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Must stay below the other static fields, the constructor reads them
    private static ImageCache instance = new ImageCache();

    // Normal section, access order: a lookup moves the entry to the end, so the first entry is the least recently used
    private final Section entries = new Section(true);
    // Level section: outside the budget and never evicted, until clearLevelEntries() deletes it
    private final Section levelEntries = new Section(false);
    private boolean levelPreload = false;
    private long hits = 0;
    private long misses = 0;
    private long evictions = 0;
    private Path statsFile = null;

    // The entries of one section, with their own byte count
    private static class Section {
        private final LinkedHashMap<String, CacheEntry> map;
        // How many cached images point to each pixel buffer (getSubimage views share the buffer of their parent)
        private final Map<DataBuffer, Integer> bufferReferences = new IdentityHashMap<>();
        private long countedBytes = 0;

        private Section(boolean accessOrder) {
            this.map = new LinkedHashMap<>(accessOrder ? 1024 : 16, 0.75f, accessOrder);
        }

        private void put(String key, CacheEntry entry) {
            map.put(key, entry);
            count(entry);
        }

        // Takes the entry out and gives its bytes back, without flushing its images
        private CacheEntry remove(String key) {
            CacheEntry entry = map.remove(key);
            if (entry != null) {
                uncount(entry);
            }
            return entry;
        }

        private void count(CacheEntry entry) {
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
        }

        private void uncount(CacheEntry entry) {
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
            }
        }

        private void clear() {
            map.clear();
            bufferReferences.clear();
            countedBytes = 0;
        }

        private static long bytesOf(DataBuffer buffer) {
            return (long) buffer.getSize() * buffer.getNumBanks() * (DataBuffer.getDataTypeSize(buffer.getDataType()) / 8);
        }
    }

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
        if (DevTestSettings.exportCachePerformance && statsDir != null && !statsDir.isBlank()) {
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
        CacheEntry entry = lookup(key, false);
        return entry == null ? null : entry.images.get(0);
    }

    public synchronized ArrayList<BufferedImage> getFrames(String key) {
        CacheEntry entry = lookup(key, true);
        return entry == null ? null : entry.frames;
    }

    public synchronized void putImage(String key, BufferedImage image) {
        add(key, new CacheEntry(image));
    }

    public synchronized void putFrames(String key, ArrayList<BufferedImage> frames) {
        add(key, new CacheEntry(frames));
    }

    public synchronized void beginLevelPreload() {
        levelPreload = true;
    }

    public synchronized void endLevelPreload() {
        levelPreload = false;
    }

    // Deletes every level entry, the same way an eviction releases it. Nothing is added to the normal section.
    public synchronized void clearLevelEntries() {
        for (CacheEntry levelEntry : levelEntries.map.values()) {
            flush(levelEntry);
        }
        levelEntries.clear();
    }

    public synchronized String getStatsLine() {
        return "entries=" + entries.map.size()
                + " mb=" + String.format("%.1f", entries.countedBytes / (1024.0 * 1024.0))
                + " hits=" + hits
                + " misses=" + misses
                + " evictions=" + evictions
                + " levelEntries=" + levelEntries.map.size()
                + " levelMb=" + String.format("%.1f", levelEntries.countedBytes / (1024.0 * 1024.0));
    }

    // Finds the entry in either section, and leaves it in the section it was found in
    private CacheEntry lookup(String key, boolean wantFrames) {
        CacheEntry entry = null;
        if (!levelEntries.map.isEmpty()) {
            entry = levelEntries.map.get(key);
        }
        if (entry == null) {
            entry = entries.map.get(key);
        }
        if (entry == null || (entry.frames != null) != wantFrames) {
            misses++;
            return null;
        }
        hits++;
        return entry;
    }

    private void add(String key, CacheEntry entry) {
        // A key already in the level section stays there, so it keeps its protection
        Section target = levelPreload || levelEntries.map.containsKey(key) ? levelEntries : entries;
        Section other = target == entries ? levelEntries : entries;
        flush(other.remove(key));
        flush(target.remove(key));
        target.put(key, entry);
        if (target == entries) {
            evictOverBudget();
        }
    }

    // Only the normal section is evicted, the level section never is
    private void evictOverBudget() {
        if (DevTestSettings.disableImageCacheBudget) {
            return;
        }
        // After a put the new entry is the most recently used one, so the loop only reaches it when nothing else is left
        Iterator<CacheEntry> eldest = entries.map.values().iterator();
        while (entries.countedBytes > MEMORY_BUDGET_BYTES && entries.map.size() > 1 && eldest.hasNext()) {
            CacheEntry evicted = eldest.next();
            eldest.remove();
            entries.uncount(evicted);
            flush(evicted);
            evictions++;
        }
    }

    // Flushes the images so their graphics card copies are freed too. The entry must already be out of its section.
    private void flush(CacheEntry entry) {
        if (entry == null) {
            return;
        }
        for (BufferedImage image : entry.images) {
            image.flush();
        }
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
