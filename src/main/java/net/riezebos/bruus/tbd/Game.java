package net.riezebos.bruus.tbd;

import javafx.application.Platform;
import net.riezebos.bruus.tbd.controllerInput.ControllerManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.items.ItemDescriptionRetriever;
import net.riezebos.bruus.tbd.game.level.SpawningCoordinator;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageResizer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Game {
    private static volatile boolean guiInitialized = false;
    private static volatile boolean assetsLoaded = false;

    public static void main(String[] args) {
        // Start watchdog thread to detect GUI initialization failures
        startInitializationWatchdog();

        try {
            logDiagnostic("=== Application Starting ===");
            // Detect and log OpenGL/hardware acceleration availability
            detectHardwareAcceleration();

            logDiagnostic("OpenGL enabled: " + System.getProperty("sun.java2d.opengl"));
            ControllerManager.getInstance().initControllers();
            Platform.startup(() -> {
                // This initializes the JavaFX application thread, which is needed for MediaPlayer
            });
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        // Create and show the window with loading screen first
                        BoardManager ex = BoardManager.getInstance();
//                        DiscordConnector.getInstance().connect();
                        ex.setVisible(true);
                        ex.validate();
                        ex.repaint();

//                        Runtime.getRuntime().addShutdownHook(
//                                new Thread(DiscordConnector.getInstance()::close, "Discord-Shutdown")
//                        );

                        guiInitialized = true;

                        // Force the loading screen to be painted before continuing
                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Load assets on a background thread to not block EDT
                        new Thread(() -> {
                            logDiagnostic("Loading assets...");
                            AudioDatabase loadingAudioInstance = AudioDatabase.getInstance();
                            ImageDatabase loadingImageInstance = ImageDatabase.getInstance();

                            logDiagnostic("Preloading assets...");
                            preloadThings();

                            exportItemDescriptions();
                            assetsLoaded = true;

                            // Finish initialization back on EDT
                            EventQueue.invokeLater(() -> {
                                logDiagnostic("Finishing initialization...");
                                logDiagnostic("Calling finishInitialization()...");
                                ex.finishInitialization();
                                logDiagnostic("finishInitialization() completed");
                                logDiagnostic("Calling initMainMenu()...");
                                ex.initMainMenu();
                                logDiagnostic("initMainMenu() completed");
                                logDiagnostic("Requesting focus...");
                                ex.getMainMenuBoard().requestFocus();
                                logDiagnostic("Calling recalcDimensions()...");
                                SpawningCoordinator.getInstance().recalcDimensions();
                                logDiagnostic("=== Application fully initialized ===");
                            });
                        }).start();
                    } catch (Exception e) {
                        logDiagnostic("Error in EventQueue initialization");
                        throw e;
                    }
                }
            });
        } catch (Exception ex) {
            logDiagnostic("Fatal error during startup");
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
                    logDiagnostic("CRITICAL: GUI failed to initialize within 60 seconds");
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
            java.io.FileWriter fw = new java.io.FileWriter("startup_log.txt", true);
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

            // Check what's actually being used by Java2D
            String openglEnabled = System.getProperty("sun.java2d.opengl");

            // Check if OpenGL was enabled but is not actually working
            if (openglEnabled != null && openglEnabled.equals("true")) {
                if (gd.getAvailableAcceleratedMemory() == 0) {
                    logDiagnostic("WARNING: OpenGL enabled but no accelerated memory available");
                    logDiagnostic("This may cause rendering issues or crashes");
                    logDiagnostic("Hardware acceleration will be used anyway - if you experience issues,");
                    logDiagnostic("you can disable it by editing the launch configuration");
                } else {
                    logDiagnostic("OpenGL hardware acceleration is active and functioning");
                    logDiagnostic("This should improve performance, especially on 4K displays");
                }
            } else {
                logDiagnostic("Hardware acceleration is NOT enabled (using software rendering)");
                logDiagnostic("Performance may be reduced, especially at higher resolutions");
            }

            // Check if we're in a headless environment
            if (GraphicsEnvironment.isHeadless()) {
                logDiagnostic("ERROR: Running in headless environment - GUI will not work");
            }

            // Log display information for 4K troubleshooting
            DisplayMode dm = gd.getDisplayMode();
            logDiagnostic("Display Resolution: " + dm.getWidth() + "x" + dm.getHeight() +
                         " @" + dm.getRefreshRate() + "Hz " + dm.getBitDepth() + "bit");
        } catch (Exception e) {
            logDiagnostic("Error detecting hardware acceleration capabilities");
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


    /*
       The ideal version of this method does the following:
       Exports a .csv file with the item descriptions using the following setup

       <ItemEnums.name>
       <ItemEnums.itemIcon>
       <ItemEnums.itemRarity>
       <Item Cost>
       <ItemEnums.itemDescription>
       <break line>

       <ItemEnums.name>
       <ItemEnums.itemIcon>
       <ItemEnums.itemRarity>
       <Item Cost>
       <ItemEnums.itemDescription>
       <break line>


       How to retrieve this info:
       Iterate over all entries in ItemEnums and filter out the ones that are not enabled (ItemEnums.enabled)
       Iterate over the remaining entries and retrieve the string description using ItemDescriptionRetriever.getItemDescription(itemEnums)
       Iterate over the remaining entries and retrieve the image using ImageDatabase.getInstance().getImage(ItemEnum.getItemIcon());
       Iterate over the remaining entries and retrieve the rarity using ItemEnums.getItemRarity()
       Iterate over the remaining entries and retrieve the cost using ItemEnums.getItemRarity().getCost
       Iterate over the remaining entries and retrieve the name using ItemEnums.getItemName()
     */
    private static void exportItemDescriptions(){
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter("item_descriptions.html"))) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<meta charset=\"UTF-8\">");
            writer.println("<title>Item Descriptions</title>");
            writer.println("<style>");
            writer.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #1a1a1a; color: #ffffff; }");
            writer.println(".item { margin-bottom: 20px; padding: 10px; border: 1px solid #444; background-color: #2a2a2a; }");
            writer.println(".item img { width: 140px; height: 140px; }");
            writer.println(".item-name { font-weight: bold; font-size: 1.75em; margin-bottom: 5px; }");
            writer.println("</style>");
            writer.println("</head>");
            writer.println("<body>");

            for (net.riezebos.bruus.tbd.game.items.ItemEnums item : net.riezebos.bruus.tbd.game.items.ItemEnums.values()) {
                if (!item.isEnabled()) {
                    continue;
                }

                String name = item.getItemName();
                BufferedImage iconImage = ImageDatabase.getInstance().getImage(item.getItemIcon());
                iconImage = ImageResizer.getInstance().resizeImageToDimensions(iconImage, 200, 200);
                String rarity = item.getItemRarity().toString();
                Color rarityColor = item.getItemRarity().getColor();
                String colorHex = String.format("#%02x%02x%02x", rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue());
                int cost = Math.round(item.getItemRarity().getItemCost());
                String description = ItemDescriptionRetriever.getDescriptionOfItem(item);

                writer.println("<div class=\"item\">");
                writer.println("<div class=\"item-name\">" + name + "</div>");

                // Embed image as base64
                if (iconImage != null) {
                    try {
                        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                        javax.imageio.ImageIO.write(iconImage, "png", baos);
                        String base64Image = java.util.Base64.getEncoder().encodeToString(baos.toByteArray());
                        writer.println("<div><img src=\"data:image/png;base64," + base64Image + "\" alt=\"" + name + "\"></div>");
                    } catch (Exception e) {
                        writer.println("<div>No image</div>");
                    }
                }

                writer.println("<div style=\"color: " + colorHex + ";\">" + rarity + "</div>");
                writer.println("<div>$" + cost + "</div>");
                writer.println("<div>" + description + "</div>");
                writer.println("</div>");
                writer.println();
            }

            writer.println("</body>");
            writer.println("</html>");

            System.out.println("Item descriptions exported to item_descriptions.html");
        } catch (java.io.IOException e) {
            logError("Failed to export item descriptions", e);
        }
    }

}
