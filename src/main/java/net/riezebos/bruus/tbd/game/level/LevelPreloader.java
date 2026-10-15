package net.riezebos.bruus.tbd.game.level;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.BossSummons;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemySpawnSettings;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.TwinBossManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyTribes;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageCache;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Builds the images of the next level's boss (with all its summons) or mini bosses just before the level.
 * Generic enemies go into the image cache's protected level section, which is deleted at level end. Everything shared
 * (the non-Generic summons, the cash carrier and the laser beams) is built into the normal cache.
 * The plan decides what, BossSummons says how, the cache stores it.
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
                        if (settings.enemy().getEnemyTribe() == EnemyTribes.Generic) {
                            imageCache.beginLevelPreload(); //only a Generic enemy is stored in the level section
                            try {
                                buildEnemy(settings, parentScale, direction);
                            } finally {
                                imageCache.endLevelPreload();
                            }
                        } else {
                            buildEnemy(settings, parentScale, direction); //shared, so into the normal cache
                        }
                        built++;
                    }
                }
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

        try {
            EnemyEnums cashCarrierEnum = EnemyEnums.CashCarrier;
            Enemy cashCarrier = EnemyCreator.createEnemy(cashCarrierEnum, 0, 0, Direction.LEFT,
                    cashCarrierEnum.getDefaultScale(), cashCarrierEnum.getMovementSpeed());
            cashCarrier.deleteObject();
            preloadLaserbeams();
        } catch (Exception e) {
            failed = true;
            System.out.println("Cash carrier and laserbeam preload failed: " + e);
            e.printStackTrace();
        }

        long millis = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Level preload" + (failed ? " FAILED" : "") + ": " + planned + " built " + built
                + " enemies in " + millis + " ms, " + imageCache.getStatsLine());
    }

    private void buildEnemy(EnemySpawnSettings settings, float parentScale, Direction direction) {
        Enemy enemy = EnemyCreator.createEnemy(settings.enemy(), 0, 0, direction,
                settings.scale(parentScale), settings.enemy().getMovementSpeed());
        enemy.deleteObject();
    }

    private void preloadLaserbeams() {
        LaserbeamConfiguration pinkLaserbeamConfig = new LaserbeamConfiguration(false, 0);
        pinkLaserbeamConfig.setAmountOfLaserbeamSegments(50);
        pinkLaserbeamConfig.setOriginPoint(new Point(0, 0));
        Laserbeam pinkLaserBeam = new AngledLaserBeam(pinkLaserbeamConfig);

        for (int degree = 0; degree < 360; degree++) {
            pinkLaserBeam.setAngleDegrees(degree);
            pinkLaserBeam.update();
        }

        LaserbeamConfiguration blueLaserbeamConfig = new LaserbeamConfiguration(true, 0);
        blueLaserbeamConfig.setAmountOfLaserbeamSegments(50);
        blueLaserbeamConfig.setOriginPoint(new Point(0, 0));
        Laserbeam blueLaserBeam = new AngledLaserBeam(blueLaserbeamConfig);

        for (int degree = 0; degree < 360; degree++) {
            blueLaserBeam.setAngleDegrees(degree);
            blueLaserBeam.update();
        }
    }

    public void clearLevel() {
        imageCache.clearLevelEntries();
    }
}
