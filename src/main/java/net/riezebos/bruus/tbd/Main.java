package net.riezebos.bruus.tbd;

import javafx.application.Platform;
import net.riezebos.bruus.tbd.controllerInput.ControllerManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.level.SpawningCoordinator;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageDatabase;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {


            ControllerManager.getInstance().initControllers();

            Platform.startup(() -> {
                // This initializes the JavaFX application thread, which is needed for MediaPlayer
            });

            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    // Create and show the window with loading screen first
                    BoardManager ex = BoardManager.getInstance();
                    ex.setVisible(true);
                    ex.validate();
                    ex.repaint();

                    // Force the loading screen to be painted before continuing
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Load assets on a background thread to not block EDT
                    new Thread(() -> {
                        System.out.println("Loading assets...");
                        AudioDatabase loadingAudioInstance = AudioDatabase.getInstance();
                        ImageDatabase loadingImageInstance = ImageDatabase.getInstance();

                        System.out.println("Preloading assets...");
                        preloadThings();

                        // Finish initialization back on EDT
                        EventQueue.invokeLater(() -> {
                            System.out.println("Finishing initialization...");
                            ex.finishInitialization();
                            ex.initMainMenu();
                            ex.getMainMenuBoard().requestFocus();
                            SpawningCoordinator.getInstance().recalcDimensions();
                        });
                    }).start();
                }
            });
        } catch (Exception ex) {
            //log the exception to a .txt file
            try {
                java.io.FileWriter fw = new java.io.FileWriter("error_log.txt", true);
                java.io.PrintWriter pw = new java.io.PrintWriter(fw);
                pw.println("=== Error at " + java.time.LocalDateTime.now() + " ===");
                ex.printStackTrace(pw);
                pw.println();
                pw.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private static void preloadThings() {
        // Load LARGE animations so it doesn't lag upon creation of them


        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("Before preload memory usage: %.3f GB%n", usedMemory / (1024.0 * 1024.0 * 1024.0));

        EnemyEnums enemyEnum = EnemyEnums.Shuriken;
        Enemy shuriken = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
                enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed());
        shuriken.deleteObject();

        enemyEnum = EnemyEnums.SpaceStationBoss;
        Enemy spaceStation = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
                enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed());
        spaceStation.deleteObject();

        enemyEnum = EnemyEnums.ShurikenMiniBoss;
        Enemy shurikenMiniBoss = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
                enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed());
        shurikenMiniBoss.deleteObject();

        enemyEnum = EnemyEnums.CashCarrier;
        Enemy cashCarrier = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
                enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed());
        cashCarrier.deleteObject();


        enemyEnum = EnemyEnums.RedBoss;
        Enemy redBoss = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
                enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed());
        redBoss.deleteObject();

        enemyEnum = EnemyEnums.FourDirectionalDrone;
        Enemy fDrone = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
                enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed());
        fDrone.deleteObject();

        enemyEnum = EnemyEnums.YellowBoss;
        Enemy yellowBoss = EnemyCreator.createEnemy(enemyEnum, 0, 0, Direction.LEFT,
                enemyEnum.getDefaultScale(), enemyEnum.getMovementSpeed());
        yellowBoss.deleteObject();

        runtime = Runtime.getRuntime();
        usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("Before preloading laserbeams memory usage: %.3f GB%n", usedMemory / (1024.0 * 1024.0 * 1024.0));

        simulateAttackAngles();

        runtime = Runtime.getRuntime();
        usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("After complete preload memory usage: %.3f GB%n", usedMemory / (1024.0 * 1024.0 * 1024.0));

    }

	private static void simulateAttackAngles() {
		LaserbeamConfiguration pinkLaserbeamConfig = new LaserbeamConfiguration(false, 0);
        pinkLaserbeamConfig.setAmountOfLaserbeamSegments(50);
        pinkLaserbeamConfig.setOriginPoint(new Point(0, 0));
		Laserbeam pinkLaserBeam = new AngledLaserBeam(pinkLaserbeamConfig);

        for(float i = 0; i < 360; i += Laserbeam.defaultMaxRotationPerUpdate){
            pinkLaserBeam.setAngleDegrees(i);
            pinkLaserBeam.update();
        }


        LaserbeamConfiguration blueLaserbeamConfig = new LaserbeamConfiguration(true, 0);
        blueLaserbeamConfig.setAmountOfLaserbeamSegments(50);
        blueLaserbeamConfig.setOriginPoint(new Point(0, 0));
        Laserbeam blueLaserBeam = new AngledLaserBeam(blueLaserbeamConfig);


        for(float i = 0; i < 360; i += Laserbeam.defaultMaxRotationPerUpdate){
            blueLaserBeam.setAngleDegrees(i);
            blueLaserBeam.update();
        }
    }

}
