package net.riezebos.bruus.tbd.game.level;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyTribes;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageCache;

/**
 * Builds, once at startup, the images that take long to build and are needed in every run: both laser beams through
 * 360 angles and the cash carrier (pirate and Overlord). They go into the image cache's permanent section, which is
 * never trimmed or cleared. Runs on the startup path, before the main menu is usable.
 */
public class PermanentImageBuilder {

    private PermanentImageBuilder() {
    }

    public static void build() {
        ImageCache imageCache = ImageCache.getInstance();
        LevelManager levelManager = LevelManager.getInstance();
        EnemyTribes originalTribe = levelManager.getCurrentEnemyTribe();
        long start = System.nanoTime();

        imageCache.beginPermanentBuild();
        try {
            preloadLaserbeams();
            buildCashCarrier();
            levelManager.setCurrentEnemyTribeForPreload(EnemyTribes.Zerg); //CashCarrier turns into an Overlord on a Zerg level
            buildCashCarrier();
        } catch (Throwable e) { //also an Error, so the loading thread always goes on to the main menu
            System.out.println("Permanent image build failed, the game starts without all of it: " + e);
            e.printStackTrace();
        } finally {
            levelManager.setCurrentEnemyTribeForPreload(originalTribe);
            imageCache.endPermanentBuild();
            imageCache.resetLevelBuilds(); //startup builds must not show up in a level's build count
        }

        long millis = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Permanent images: " + imageCache.getPermanentEntryCount() + " entries, "
                + String.format("%.1f", imageCache.getPermanentMegabytes()) + " MB, built in " + millis + " ms");
    }

    // Director.getSpawnDirection can return LEFT or RIGHT for a cash carrier (it is not in the up-or-down list), and
    // Director.spawnEnemy uses the default scale, times 0.7 when the resolution factor is below 1 (Director.java:451-456).
    // Game sets the real window size in DataClass before this runs, so the factor is the one play uses.
    private static void buildCashCarrier() {
        EnemyEnums cashCarrierEnum = EnemyEnums.CashCarrier;
        float scale = cashCarrierEnum.getDefaultScale();
        if (DataClass.getInstance().getResolutionFactor() < 1) {
            scale *= 0.7f;
        }
        for (Direction direction : new Direction[]{Direction.LEFT, Direction.RIGHT}) {
            Enemy cashCarrier = EnemyCreator.createEnemy(cashCarrierEnum, 0, 0, direction, scale, cashCarrierEnum.getMovementSpeed());
            cashCarrier.deleteObject();
        }
    }

    private static void preloadLaserbeams() {
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
}
