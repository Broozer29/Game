package net.riezebos.bruus.tbd;

import javafx.application.Platform;
import net.riezebos.bruus.tbd.controllerInput.ControllerManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.level.SpawningCoordinator;
import net.riezebos.bruus.tbd.game.movement.Direction;
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

//		simulateAttackAngles(true);
//		simulateAttackAngles(false);

        runtime = Runtime.getRuntime();
        usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("After preload memory usage: %.3f GB%n", usedMemory / (1024.0 * 1024.0 * 1024.0));

    }

//	private static void simulateAttackAngles(boolean inwards) {
//		// Use the centralized static values from CrossingLaserbeamsAttack
//		int lowerLaserbeamLowestAngle = RedBossCrossingLaserbeamsAttack.lowerLaserbeamLowestAngle;
//		int lowerLaserbeamHighestAngle = RedBossCrossingLaserbeamsAttack.lowerLaserbeamHighestAngle;
//		int upperLaserbeamLowestAngle = RedBossCrossingLaserbeamsAttack.upperLaserbeamLowestAngle;
//		int upperLaserbeamHighestAngle = RedBossCrossingLaserbeamsAttack.upperLaserbeamHighestAngle;
//		float angleStepSize = RedBossCrossingLaserbeamsAttack.angleStepSize;
//
//		// Adjust angles like the CrossingLaserbeamsAttack settings
//		if (inwards) {
//			lowerLaserbeamHighestAngle = 185;
//			upperLaserbeamLowestAngle = 175;
//		} else {
//			lowerLaserbeamLowestAngle = 160;
//			lowerLaserbeamHighestAngle = 190;
//			upperLaserbeamLowestAngle = 170;
//			upperLaserbeamHighestAngle = 200;
//			angleStepSize = 0.25f;
//		}
//
//		LaserbeamConfiguration lowerConfig = new LaserbeamConfiguration(false, 0);
//		lowerConfig.setAmountOfLaserbeamSegments(20);
//		lowerConfig.setOriginPoint(new Point(0, 0));
//		Laserbeam lowerLaserbeam = new AngledLaserBeam(lowerConfig);
//
//		LaserbeamConfiguration upperConfig = new LaserbeamConfiguration(false, 0);
//		upperConfig.setAmountOfLaserbeamSegments(20);
//		upperConfig.setOriginPoint(new Point(0, 0));
//		Laserbeam upperLaserbeam = new AngledLaserBeam(upperConfig);
//
//		// Simulate lower laserbeam movement
//		for (float angle = lowerLaserbeamLowestAngle; angle <= lowerLaserbeamHighestAngle; angle += angleStepSize) {
//			lowerLaserbeam.setAngleDegrees(angle);
//			lowerLaserbeam.update(); // Caching automatically happens here
//		}
//
//		// Simulate upper laserbeam movement
//		for (float angle = upperLaserbeamLowestAngle; angle <= upperLaserbeamHighestAngle; angle += angleStepSize) {
//			upperLaserbeam.setAngleDegrees(angle);
//			upperLaserbeam.update(); // Caching automatically happens here
//		}
//	}

}
