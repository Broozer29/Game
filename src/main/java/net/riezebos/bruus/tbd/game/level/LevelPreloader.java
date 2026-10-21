package net.riezebos.bruus.tbd.game.level;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.TwinBossManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.level.directors.Director;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageCache;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.RotateWhileDrawing;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Builds the images of the next level's boss or mini bosses just before the level, at the scale they spawn with,
 * into the image cache's protected level section, which is deleted at level end. Their summons are not built here:
 * a summon builds its resized copy when it first spawns, which takes a few milliseconds. The cash carrier and the laser
 * beams live in the permanent section, built once at startup by PermanentImageBuilder.
 */
public class LevelPreloader {

    private static final LevelPreloader instance = new LevelPreloader();

    private final ImageCache imageCache = ImageCache.getInstance();
    private final LevelManager levelManager = LevelManager.getInstance();

    private LevelPreloader() {
    }

    public static LevelPreloader getInstance() {
        return instance;
    }

    public void preloadNextLevel() {
        try {
            preloadLevel();
        } finally {
            imageCache.LOG_resetLevelBuilds(); //builds of the preload itself are not counted for the level
        }
    }

    private void preloadLevel() {
        long start = System.nanoTime();
        int built = 0;
        boolean failed = false;
        List<EnemyEnums> planned = new ArrayList<>();

        imageCache.clearLevelEntries(); //leftovers of a level that did not end normally
        try {
            levelManager.planNextLevel();

            EnemyEnums boss = levelManager.getSelectedBoss();
            if (boss != null) {
                planned.add(boss);
            } else {
                planned.addAll(new LinkedHashSet<>(levelManager.getPlannedMiniBosses()));
            }

            for (EnemyEnums enemy : planned) {
                float scale = boss != null ? Director.getBossSpawnScale(enemy) : enemy.getDefaultScale(); //the scales Director spawns them with
                imageCache.beginLevelPreload(); //bosses and mini bosses are Generic, so they go into the level section
                try {
                    buildEnemy(enemy, scale);
                } finally {
                    imageCache.endLevelPreload();
                }
                built++;
            }
        } catch (Exception e) {
            failed = true;
            System.out.println("Level preload failed, the level starts without it: " + e);
            e.printStackTrace();
        } finally {
            if (planned.contains(EnemyEnums.TwinBoss)) {
                TwinBossManager.getInstance().resetTwinBossManager(); //the fight starts with only the real twins
            }
        }

        LOG_printPreload(start, failed, planned, built);
    }

    private void LOG_printPreload(long startNanos, boolean failed, List<EnemyEnums> planned, int built) {
        long millis = (System.nanoTime() - startNanos) / 1_000_000;
        System.out.println("Level preload" + (failed ? " FAILED" : "") + ": " + planned + " built " + built
                + " enemies in " + millis + " ms, " + imageCache.LOG_getStatsLine());
    }

    private void buildEnemy(EnemyEnums enemyType, float scale) {
        Enemy enemy = EnemyCreator.createEnemy(enemyType, 0, 0, Direction.LEFT, scale, enemyType.getMovementSpeed());
        enemy.deleteObject();
    }

    public void clearLevel() {
        LOG_printLevelEnd();
        imageCache.LOG_resetLevelBuilds();
        imageCache.clearLevelEntries();
        long flushStart = System.nanoTime();
        ImageDatabase.getInstance().flushOriginals(); //graphics card copies of the level's pictures; the next level uploads what it draws
        LOG_printFlush(flushStart);
    }

    private void LOG_printLevelEnd() {
        System.out.println("Level end: " + imageCache.LOG_getLevelBuildSummary() + ", " + imageCache.LOG_getStatsLine());
        System.out.println("Level end top: " + imageCache.LOG_getTopNamesLine());
        if (DevTestSettings.verifyRotateWhileDrawing) {
            System.out.println(RotateWhileDrawing.LOG_getSummaryLine());
        }
    }

    private void LOG_printFlush(long startNanos) {
        System.out.println("Level end: graphics card copies of the loaded pictures freed in " + (System.nanoTime() - startNanos) / 1_000_000 + " ms");
    }
}
