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
    private static volatile boolean guiInitialized = false;
    private static volatile boolean assetsLoaded = false;

    public static void main(String[] args) {
        // Start watchdog thread to detect GUI initialization failures
        startInitializationWatchdog();

        try {
            logDiagnostic("=== Application Starting ===");
            logDiagnostic("Java Version: " + System.getProperty("java.version"));
            logDiagnostic("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));

            // Detect and log OpenGL/hardware acceleration availability
            detectHardwareAcceleration();

            logDiagnostic("OpenGL enabled: " + System.getProperty("sun.java2d.opengl"));
            logDiagnostic("Graphics Environment: " + GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getIDstring());

            ControllerManager.getInstance().initControllers();
            logDiagnostic("Controllers initialized");

            Platform.startup(() -> {
                // This initializes the JavaFX application thread, which is needed for MediaPlayer
            });
            logDiagnostic("JavaFX Platform started");

            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        logDiagnostic("EventQueue started");
                        // Create and show the window with loading screen first
                        BoardManager ex = BoardManager.getInstance();
                        logDiagnostic("BoardManager instance created");

                        ex.setVisible(true);
                        ex.validate();
                        ex.repaint();
                        logDiagnostic("Window set to visible");

                        guiInitialized = true;

                        // Force the loading screen to be painted before continuing
                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Load assets on a background thread to not block EDT
                        new Thread(() -> {
                            System.out.println("Loading assets...");
                            logDiagnostic("Loading assets...");
                            AudioDatabase loadingAudioInstance = AudioDatabase.getInstance();
                            ImageDatabase loadingImageInstance = ImageDatabase.getInstance();

                            System.out.println("Preloading assets...");
                            logDiagnostic("Preloading assets...");
                            preloadThings();

                            assetsLoaded = true;

                            // Finish initialization back on EDT
                            EventQueue.invokeLater(() -> {
                                System.out.println("Finishing initialization...");
                                logDiagnostic("Finishing initialization...");
                                ex.finishInitialization();
                                ex.initMainMenu();
                                ex.getMainMenuBoard().requestFocus();
                                SpawningCoordinator.getInstance().recalcDimensions();
                                logDiagnostic("=== Application fully initialized ===");
                            });
                        }).start();
                    } catch (Exception e) {
                        logError("Error in EventQueue initialization", e);
                        throw e;
                    }
                }
            });
        } catch (Exception ex) {
            logError("Fatal error during startup", ex);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private static void startInitializationWatchdog() {
        Thread watchdog = new Thread(() -> {
            try {
                // Wait 30 seconds for GUI to initialize
                Thread.sleep(60000);

                if (!guiInitialized) {
                    logError("CRITICAL: GUI failed to initialize within 60 seconds", null);
                    logDiagnostic("GUI initialization timeout - window may not be visible");
                    logDiagnostic("Possible causes: OpenGL incompatibility, graphics driver issue, AWT/JavaFX conflict");
                } else if (!assetsLoaded) {
                    logDiagnostic("WARNING: Assets not loaded within 30 seconds (this may be normal for slow systems)");
                }
            } catch (InterruptedException e) {
                // Normal shutdown
            }
        });
        watchdog.setDaemon(true);
        watchdog.setName("InitializationWatchdog");
        watchdog.start();
    }

    private static void logDiagnostic(String message) {
        System.out.println(message);
        try {
            java.io.FileWriter fw = new java.io.FileWriter("error_log.txt", true);
            java.io.PrintWriter pw = new java.io.PrintWriter(fw);
            pw.println("[" + java.time.LocalDateTime.now() + "] " + message);
            pw.close();
        } catch (java.io.IOException e) {
            // Silently fail if can't write to log
        }
    }

    private static void logError(String message, Exception ex) {
        try {
            java.io.FileWriter fw = new java.io.FileWriter("error_log.txt", true);
            java.io.PrintWriter pw = new java.io.PrintWriter(fw);
            pw.println("=== Error at " + java.time.LocalDateTime.now() + " ===");
            pw.println(message);
            if (ex != null) {
                ex.printStackTrace(pw);
            }
            pw.println();
            pw.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private static void detectHardwareAcceleration() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();

            // Check if hardware acceleration is available
            logDiagnostic("Graphics Device: " + gd.getIDstring());
            logDiagnostic("Available Accelerated Memory: " + gd.getAvailableAcceleratedMemory() + " bytes");

            // Check what's actually being used by Java2D
            String openglEnabled = System.getProperty("sun.java2d.opengl");
            String d3dEnabled = System.getProperty("sun.java2d.d3d");
            String noddrawEnabled = System.getProperty("sun.java2d.noddraw");

            logDiagnostic("sun.java2d.opengl property: " + (openglEnabled != null ? openglEnabled : "not set"));
            logDiagnostic("sun.java2d.d3d property: " + (d3dEnabled != null ? d3dEnabled : "not set"));
            logDiagnostic("sun.java2d.noddraw property: " + (noddrawEnabled != null ? noddrawEnabled : "not set"));

            // Try to detect if OpenGL pipeline is actually loaded
            String pipelineClass = System.getProperty("sun.java2d.opengl.fbobject");
            if (openglEnabled != null && openglEnabled.equals("true")) {
                if (gd.getAvailableAcceleratedMemory() == 0) {
                    logDiagnostic("WARNING: OpenGL enabled but no accelerated memory available - may cause issues");
                    logDiagnostic("RECOMMENDATION: Consider disabling OpenGL acceleration for this system");
                } else {
                    logDiagnostic("OpenGL appears to be available and properly configured");
                }
            }

            // Check if we're in a headless environment
            if (GraphicsEnvironment.isHeadless()) {
                logDiagnostic("ERROR: Running in headless environment - GUI will not work");
            }

            // Log graphics card capabilities
            logDiagnostic("Graphics Configuration: " + gc.toString());
            logDiagnostic("Color Model: " + gc.getColorModel().toString());

        } catch (Exception e) {
            logError("Error detecting hardware acceleration capabilities", e);
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
