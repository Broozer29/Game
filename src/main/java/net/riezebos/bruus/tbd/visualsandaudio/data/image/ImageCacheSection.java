package net.riezebos.bruus.tbd.visualsandaudio.data.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// The entries of one part of the ImageCache, with their own byte count. ImageCache decides which part an image goes into, this class stores and counts.
class ImageCacheSection {
    final LinkedHashMap<String, CacheEntry> map;
    // How many cached images point to each pixel buffer (getSubimage views share the buffer of their parent)
    private final Map<DataBuffer, Integer> bufferReferences = new IdentityHashMap<>();
    long countedBytes = 0;

    ImageCacheSection(boolean accessOrder) {
        this.map = new LinkedHashMap<>(accessOrder ? 1024 : 16, 0.75f, accessOrder);
    }

    void put(String key, CacheEntry entry) {
        map.put(key, entry);
        count(entry);
    }

    // Takes the entry out and gives its bytes back, without flushing its images
    CacheEntry remove(String key) {
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

    void uncount(CacheEntry entry) {
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

    void clear() {
        map.clear();
        bufferReferences.clear();
        countedBytes = 0;
    }

    private static long bytesOf(DataBuffer buffer) {
        return (long) buffer.getSize() * buffer.getNumBanks() * (DataBuffer.getDataTypeSize(buffer.getDataType()) / 8);
    }

    static class CacheEntry {
        final List<BufferedImage> images;
        final ArrayList<BufferedImage> frames;

        CacheEntry(BufferedImage image) {
            this.images = List.of(image);
            this.frames = null;
        }

        CacheEntry(ArrayList<BufferedImage> frames) {
            this.images = frames;
            this.frames = frames;
        }
    }
}
