package net.riezebos.bruus.tbd.bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.guiboards.BoardManager;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Everything a bot run writes to its folder: bot.log (what happened when), screenshots, the crash trace, and summary.json at the end.
class BotRunRecorder {
    private static final long MEGABYTE = 1024L * 1024L;

    private final Path directory;
    private final Path screenshotDirectory;
    private final long startMillis = System.currentTimeMillis();
    private final ExecutorService screenshotWriter = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "BotScreenshots");
        thread.setDaemon(true);
        return thread;
    });
    private PrintWriter log;
    private Robot robot;
    private volatile boolean screenshotsFailed = false;
    private boolean summaryWritten = false;
    private int finalExitCode = 0;

    private int runsStarted = 0;
    private int levelsCompleted = 0;
    private int deaths = 0;
    private int screenshotCount = 0;
    private int bossesSeen = 0;
    private long maxHeapUsedMb = 0;
    private long lastHeapUsedMb = 0;
    private String lastPosition = "";
    private String note = "";

    BotRunRecorder(Path directory) {
        this.directory = directory;
        this.screenshotDirectory = directory.resolve("screenshots");
        try {
            Files.createDirectories(screenshotDirectory);
            log = new PrintWriter(Files.newBufferedWriter(directory.resolve("bot.log"), StandardCharsets.UTF_8), true);
        } catch (IOException e) {
            System.out.println("Bot: could not create " + directory + ": " + e);
        }
    }

    double elapsedSeconds() {
        return (System.currentTimeMillis() - startMillis) / 1000.0;
    }

    void event(String message) {
        String line = String.format("[+%7.1fs] %s", elapsedSeconds(), message);
        System.out.println("Bot: " + line);
        if (log != null) {
            log.println(line);
        }
    }

    void runStarted() {
        runsStarted++;
    }

    void levelCompleted() {
        levelsCompleted++;
    }

    void died() {
        deaths++;
    }

    void bossSeen() {
        bossesSeen++;
    }

    int getLevelsCompleted() {
        return levelsCompleted;
    }

    int getDeaths() {
        return deaths;
    }

    void setLastPosition(String position) {
        this.lastPosition = position;
    }

    void setNote(String note) {
        this.note = note;
    }

    void sampleHeap() {
        Runtime runtime = Runtime.getRuntime();
        lastHeapUsedMb = (runtime.totalMemory() - runtime.freeMemory()) / MEGABYTE;
        maxHeapUsedMb = Math.max(maxHeapUsedMb, lastHeapUsedMb);
    }

    // The screenshot is taken and written on a background thread, so the game does not hitch while it is encoded
    void screenshot(String label) {
        if (screenshotsFailed) {
            return;
        }
        BoardManager frame = BoardManager.getInstance();
        if (!frame.isShowing()) {
            return;
        }
        Rectangle bounds = new Rectangle(frame.getLocationOnScreen(), frame.getSize());
        long seconds = Math.round(elapsedSeconds());
        int width = BotSettings.getScreenshotWidth();
        Path file = screenshotDirectory.resolve(String.format("%05ds-%s.png", seconds, label.replaceAll("[^A-Za-z0-9_-]", "_")));
        screenshotCount++;
        screenshotWriter.submit(() -> {
            try {
                if (robot == null) {
                    robot = new Robot();
                }
                BufferedImage image = robot.createScreenCapture(bounds);
                if (width > 0 && image.getWidth() > width) {
                    image = scale(image, width);
                }
                ImageIO.write(image, "png", file.toFile());
            } catch (Throwable e) {
                screenshotsFailed = true;
                event("screenshots stopped: " + e);
            }
        });
    }

    private BufferedImage scale(BufferedImage image, int width) {
        int height = Math.round(image.getHeight() * (width / (float) image.getWidth()));
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return scaled;
    }

    void writeCrash(Thread thread, Throwable error) {
        try {
            StringWriter text = new StringWriter();
            error.printStackTrace(new PrintWriter(text));
            Files.writeString(directory.resolve("crash.txt"), "Uncaught exception on thread " + thread.getName() + ":\n" + text, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Bot: could not write crash.txt: " + e);
        }
    }

    // Writes summary.json and report.md once. The reason says why the run ended; the exit code is what the process would exit with.
    // Returns the exit code to use: a clean run that missed a limit of its scenario exits with 6.
    synchronized int finish(String reason, int exitCode) {
        if (summaryWritten) {
            return finalExitCode;
        }
        summaryWritten = true;
        sampleHeap();
        screenshotWriter.shutdown();
        try {
            screenshotWriter.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean gameErrorLog = copyGameErrorLog();
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("scenario", BotSettings.getScenarioName());
        summary.put("reason", reason);
        summary.put("exitCode", exitCode);
        summary.put("elapsedSeconds", Math.round(elapsedSeconds()));
        summary.put("playerClass", BotSettings.getPlayerClass().name());
        summary.put("runsStarted", runsStarted);
        summary.put("levelsCompleted", levelsCompleted);
        summary.put("deaths", deaths);
        summary.put("bossesSeen", bossesSeen);
        summary.put("screenshots", screenshotCount);
        summary.put("lastHeapUsedMb", lastHeapUsedMb);
        summary.put("maxHeapUsedMb", maxHeapUsedMb);
        summary.put("heapLimitMb", Runtime.getRuntime().maxMemory() / MEGABYTE);
        summary.put("lastPosition", lastPosition);
        summary.put("gameErrorLog", gameErrorLog);
        summary.put("crashFile", Files.exists(directory.resolve("crash.txt")));
        summary.put("devTestSwitchesOn", switchesThatAreOn());
        summary.put("note", note);
        summary.put("outputDirectory", directory.toString());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Files.writeString(directory.resolve("summary.json"), gson.toJson(summary), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Bot: could not write summary.json: " + e);
        }
        boolean limitsMet = BotReport.write(directory, summary, reason, exitCode);
        finalExitCode = exitCode == 0 && !limitsMet ? BotReport.EXIT_CODE_LIMIT_FAILED : exitCode;
        event("run finished: " + reason + " (exit code " + finalExitCode + (limitsMet ? "" : ", a limit of the scenario was missed: see report.md") + ")");
        if (log != null) {
            log.close();
        }
        return finalExitCode;
    }

    // The game writes error_log.txt in its working directory when its loop throws; keep it when it was written during this run
    private boolean copyGameErrorLog() {
        Path errorLog = Path.of("error_log.txt");
        try {
            if (Files.exists(errorLog) && Files.getLastModifiedTime(errorLog).toMillis() >= startMillis) {
                Files.copy(errorLog, directory.resolve("error_log.txt"), StandardCopyOption.REPLACE_EXISTING);
                return true;
            }
        } catch (IOException e) {
            System.out.println("Bot: could not copy error_log.txt: " + e);
        }
        return false;
    }

    private List<String> switchesThatAreOn() {
        List<String> on = new ArrayList<>();
        for (Field field : DevTestSettings.class.getDeclaredFields()) {
            if (field.getType() == boolean.class && Modifier.isStatic(field.getModifiers())) {
                try {
                    if (field.getBoolean(null)) {
                        on.add(field.getName());
                    }
                } catch (IllegalAccessException e) {
                    // not readable, so not listed
                }
            }
        }
        return on;
    }
}
