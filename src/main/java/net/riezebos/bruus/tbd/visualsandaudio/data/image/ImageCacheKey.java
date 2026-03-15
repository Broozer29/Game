package net.riezebos.bruus.tbd.visualsandaudio.data.image;

import java.util.Objects;

public class ImageCacheKey {
    private final String key;
    private long lastAccessTime;

    public ImageCacheKey(String key) {
        this.key = key;
        this.lastAccessTime = System.currentTimeMillis();
    }

    public String getKey() {
        return key;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void updateAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    public long getTimeSinceLastAccess() {
        return System.currentTimeMillis() - lastAccessTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageCacheKey imageCacheKey = (ImageCacheKey) o;
        return Objects.equals(key, imageCacheKey.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return key;
    }
}
