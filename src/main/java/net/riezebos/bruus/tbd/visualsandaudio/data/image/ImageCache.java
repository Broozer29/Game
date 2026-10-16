package net.riezebos.bruus.tbd.visualsandaudio.data.image;

import net.riezebos.bruus.tbd.DevTestSettings;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// One shared cache for rotated and resized images (single images and frame lists).
// The normal section has a memory budget for its Java pixels plus an estimated graphics card copy of each image (GRAPHICS_COPY_FACTOR): least recently used entries are dropped first when that total exceeds it.
// The permanent section sits outside the budget too: it holds what is built once at startup (laser beams, cash carrier) and is never evicted or cleared.
// While the startup build runs (beginPermanentBuild until endPermanentBuild), every put goes to the permanent section.
// The level section sits outside that budget and is never evicted. While the preloader builds a Generic enemy
// (beginLevelPreload until endLevelPreload), every put goes to the level section, from any thread. A lookup that hits
// the normal section leaves the entry there. clearLevelEntries() deletes the whole level section; nothing moves between the sections.
public class ImageCache {

    private static final long MEMORY_BUDGET_BYTES = 2_560L * 1024 * 1024;
    // The graphics driver keeps its own copy of every drawn image, measured on 2026-10-16 at about 1.3 times the image's pixel bytes
    private static final double GRAPHICS_COPY_FACTOR = 1.3;
    private static final long STATS_INTERVAL_MILLIS = 30_000;
    private static final DateTimeFormatter FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final DateTimeFormatter LINE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Must stay below the other static fields, the constructor reads them
    private static ImageCache instance = new ImageCache();

    // Normal section, access order: a lookup moves the entry to the end, so the first entry is the least recently used
    private final ImageCacheSection entries = new ImageCacheSection(true);
    // Level section: outside the budget and never evicted, until clearLevelEntries() deletes it
    private final ImageCacheSection levelEntries = new ImageCacheSection(false);
    // Permanent section: outside the budget, never evicted and never cleared; filled by the startup build between beginPermanentBuild() and endPermanentBuild()
    private final ImageCacheSection permanentEntries = new ImageCacheSection(false);
    private boolean levelPreload = false;
    private boolean permanentBuild = false;
    private long hits = 0;
    private long misses = 0;
    private long evictions = 0;
    private Path statsFile = null;
    // Builds on a cache miss since the last resetLevelBuilds(), for the level end log line
    private long levelBuilds = 0;
    private long levelBuildNanos = 0;
    private long levelSlowestNanos = 0;
    private String levelSlowestKey = null;

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
        ImageCacheSection.CacheEntry entry = lookup(key, false);
        return entry == null ? null : entry.images.get(0);
    }

    public synchronized ArrayList<BufferedImage> getFrames(String key) {
        ImageCacheSection.CacheEntry entry = lookup(key, true);
        return entry == null ? null : entry.frames;
    }

    public synchronized void putImage(String key, BufferedImage image) {
        add(key, new ImageCacheSection.CacheEntry(image));
    }

    public synchronized void putFrames(String key, ArrayList<BufferedImage> frames) {
        add(key, new ImageCacheSection.CacheEntry(frames));
    }

    public synchronized void beginLevelPreload() {
        levelPreload = true;
    }

    public synchronized void endLevelPreload() {
        levelPreload = false;
    }

    public synchronized void beginPermanentBuild() {
        permanentBuild = true;
    }

    public synchronized void endPermanentBuild() {
        permanentBuild = false;
    }

    public synchronized int getPermanentEntryCount() {
        return permanentEntries.map.size();
    }

    public synchronized double getPermanentMegabytes() {
        return permanentEntries.countedBytes / (1024.0 * 1024.0);
    }

    // Deletes every level entry, the same way an eviction releases it. Nothing is added to the normal section.
    public synchronized void clearLevelEntries() {
        for (ImageCacheSection.CacheEntry levelEntry : levelEntries.map.values()) {
            flush(levelEntry);
        }
        levelEntries.clear();
    }

    // Called by the resizer and rotator after they built an image or frame list on a cache miss
    public synchronized void recordBuild(String key, long nanos) {
        levelBuilds++;
        levelBuildNanos += nanos;
        if (nanos > levelSlowestNanos) {
            levelSlowestNanos = nanos;
            levelSlowestKey = key;
        }
    }

    public synchronized void resetLevelBuilds() {
        levelBuilds = 0;
        levelBuildNanos = 0;
        levelSlowestNanos = 0;
        levelSlowestKey = null;
    }

    public synchronized String getLevelBuildSummary() {
        String key = levelSlowestKey == null ? "-" : levelSlowestKey;
        if (key.length() > 80) {
            key = key.substring(0, 80) + "...";
        }
        return levelBuilds + " cache misses built in " + String.format("%.1f", levelBuildNanos / 1_000_000.0)
                + " ms during the level, slowest " + String.format("%.1f", levelSlowestNanos / 1_000_000.0)
                + " ms (" + key + ")";
    }

    public synchronized String getStatsLine() {
        return "entries=" + entries.map.size()
                + " mb=" + String.format("%.1f", entries.countedBytes / (1024.0 * 1024.0))
                + " withGraphicsMb=" + String.format("%.1f", withGraphicsCopy(entries.countedBytes) / (1024.0 * 1024.0))
                + " hits=" + hits
                + " misses=" + misses
                + " evictions=" + evictions
                + " levelEntries=" + levelEntries.map.size()
                + " levelMb=" + String.format("%.1f", levelEntries.countedBytes / (1024.0 * 1024.0))
                + " permanentEntries=" + permanentEntries.map.size()
                + " permanentMb=" + String.format("%.1f", permanentEntries.countedBytes / (1024.0 * 1024.0));
    }

    // Finds the entry in any of the three sections, and leaves it in the section it was found in
    private ImageCacheSection.CacheEntry lookup(String key, boolean wantFrames) {
        ImageCacheSection.CacheEntry entry = permanentEntries.map.get(key);
        if (entry == null && !levelEntries.map.isEmpty()) {
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

    private void add(String key, ImageCacheSection.CacheEntry entry) {
        // A key already in the permanent or level section stays there, so it keeps its protection
        ImageCacheSection target;
        if (permanentBuild || permanentEntries.map.containsKey(key)) {
            target = permanentEntries;
        } else if (levelPreload || levelEntries.map.containsKey(key)) {
            target = levelEntries;
        } else {
            target = entries;
        }
        // A key lives in one section only, so an older copy in another section is deleted
        for (ImageCacheSection section : new ImageCacheSection[]{permanentEntries, levelEntries, entries}) {
            if (section != target) {
                flush(section.remove(key));
            }
        }
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
        Iterator<ImageCacheSection.CacheEntry> eldest = entries.map.values().iterator();
        while (withGraphicsCopy(entries.countedBytes) > MEMORY_BUDGET_BYTES && entries.map.size() > 1 && eldest.hasNext()) {
            ImageCacheSection.CacheEntry evicted = eldest.next();
            eldest.remove();
            entries.uncount(evicted);
            flush(evicted);
            evictions++;
        }
    }

    // Java pixel bytes plus the estimated graphics card copy, which is what the budget counts
    private static long withGraphicsCopy(long pixelBytes) {
        return (long) (pixelBytes * (1 + GRAPHICS_COPY_FACTOR));
    }

    // Flushes the images so their graphics card copies are freed too. The entry must already be out of its section.
    private void flush(ImageCacheSection.CacheEntry entry) {
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
