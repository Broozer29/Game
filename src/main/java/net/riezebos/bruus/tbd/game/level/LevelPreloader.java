package net.riezebos.bruus.tbd.game.level;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.BossSummons;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemySpawnSettings;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.TwinBossManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageCache;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Builds the images of the next level's boss (with all its summons) or mini bosses just before the level,
 * into the image cache's protected level section. The plan decides what, BossSummons says how, the cache stores it.
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
        long start = System.nanoTime();
        int built = 0;
        boolean failed = false;
        List<EnemyEnums> planned = new ArrayList<>();

        imageCache.clearLevelEntries(); //leftovers of a level that did not end normally
        imageCache.beginLevelPreload();
        try {
            levelManager.planNextLevel();

            EnemyEnums boss = levelManager.getSelectedBoss();
            if (boss != null) {
                planned.add(boss);
            } else {
                planned.addAll(new LinkedHashSet<>(levelManager.getPlannedMiniBosses()));
            }

            for (EnemyEnums topLevelEnemy : planned) {
                List<EnemySpawnSettings> settingsList = BossSummons.forEnemy(topLevelEnemy);
                if (settingsList.isEmpty()) {
                    continue;
                }
                float parentScale = settingsList.get(0).scale(0); //entry 0 is the boss or mini boss itself
                for (EnemySpawnSettings settings : settingsList) {
                    for (Direction direction : settings.directions()) {
                        Enemy enemy = EnemyCreator.createEnemy(settings.enemy(), 0, 0, direction,
                                settings.scale(parentScale), settings.enemy().getMovementSpeed());
                        enemy.deleteObject();
                        built++;
                    }
                }
            }
        } catch (Exception e) {
            failed = true;
            System.out.println("Level preload failed, the level starts without it: " + e);
            e.printStackTrace();
        } finally {
            imageCache.endLevelPreload();
            if (planned.contains(EnemyEnums.TwinBoss)) {
                TwinBossManager.getInstance().resetTwinBossManager(); //the fight starts with only the real twins
            }
        }

        long millis = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Level preload" + (failed ? " FAILED" : "") + ": " + planned + " built " + built
                + " enemies in " + millis + " ms, " + imageCache.getStatsLine());
    }

    public void clearLevel() {
        imageCache.clearLevelEntries();
    }
}
