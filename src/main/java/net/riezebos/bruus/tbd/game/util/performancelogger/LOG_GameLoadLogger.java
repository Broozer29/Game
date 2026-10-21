package net.riezebos.bruus.tbd.game.util.performancelogger;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageOverTime;
import net.riezebos.bruus.tbd.game.level.directors.GodRunDetector;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// Dev tool: writes one CSV line per second with tick, draw and gap times and how much is on screen,
// to match a stutter to its cause. Only active when started with -Dgame.loadStatsDir=<folder>
public class LOG_GameLoadLogger {

    private static final long INTERVAL_NANOS = 1_000_000_000L;
    private static final DateTimeFormatter FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final DateTimeFormatter LINE_TIMESTAMP = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static LOG_GameLoadLogger instance = new LOG_GameLoadLogger();

    private BufferedWriter writer = null;
    private long intervalStart = 0;
    private volatile long lastTickStart = 0;
    private int ticks = 0;
    private double tickTotalMs = 0;
    private double tickMaxMs = 0;
    private double gapMaxMs = 0;
    private int draws = 0;
    private double drawTotalMs = 0;
    private double drawMaxMs = 0;

    private LOG_GameLoadLogger() {
        String statsDir = System.getProperty("game.loadStatsDir");
        if (statsDir == null || statsDir.isBlank()) {
            return;
        }
        try {
            Path dir = Paths.get(statsDir);
            Files.createDirectories(dir);
            Path file = dir.resolve("load-" + LocalDateTime.now().format(FILE_TIMESTAMP) + ".csv");
            writer = Files.newBufferedWriter(file);
            writer.write("time,gameSeconds,stage,ticks,tickAvgMs,tickMaxMs,gapMaxMs,draws,drawAvgMs,drawMaxMs,"
                    + "enemies,enemyEffects,dotEffects,igniteEffects,igniteStacks,maxEffectsOnOneEnemy,"
                    + "missiles,specialAttacks,upperAnimations,lowerAnimations,usedHeapMb,"
                    + "godRunScore,godRunSpawned,godRunKilled,godRunKillRatio,godRunBoardBlockTotal,godRunHighRatioSince");
            writer.newLine();
            writer.flush();
        } catch (IOException | RuntimeException exception) {
            System.out.println("Game load stats disabled: " + exception);
            writer = null;
        }
    }

    public static LOG_GameLoadLogger getInstance() {
        return instance;
    }

    public boolean isEnabled() {
        return writer != null;
    }

    // Time spent outside the game board (shop, menus) is not a gap between ticks
    public void LOG_resetGap() {
        lastTickStart = 0;
    }

    // Called at the start of every game tick; returns the start time to pass to endTick
    public long startTick() {
        long now = System.nanoTime();
        if (lastTickStart != 0) {
            gapMaxMs = Math.max(gapMaxMs, (now - lastTickStart) / 1_000_000.0);
        }
        lastTickStart = now;
        if (intervalStart == 0) {
            intervalStart = now;
        }
        return now;
    }

    public void endTick(long tickStart) {
        long now = System.nanoTime();
        double tickMs = (now - tickStart) / 1_000_000.0;
        ticks++;
        tickTotalMs += tickMs;
        tickMaxMs = Math.max(tickMaxMs, tickMs);

        if (now - intervalStart >= INTERVAL_NANOS) {
            writeLine();
            intervalStart = now;
        }
    }

    public void logDraw(long drawStart) {
        double drawMs = (System.nanoTime() - drawStart) / 1_000_000.0;
        draws++;
        drawTotalMs += drawMs;
        drawMaxMs = Math.max(drawMaxMs, drawMs);
    }

    private void writeLine() {
        int enemies = 0;
        int enemyEffects = 0;
        int dotEffects = 0;
        int igniteEffects = 0;
        int igniteStacks = 0;
        int maxEffectsOnOneEnemy = 0;
        for (GameObject enemy : EnemyManager.getInstance().getEnemies()) {
            enemies++;
            int effectCount = enemy.getEffects().size();
            enemyEffects += effectCount;
            maxEffectsOnOneEnemy = Math.max(maxEffectsOnOneEnemy, effectCount);
            for (EffectInterface effect : enemy.getEffects()) {
                if (effect instanceof DamageOverTime dot) {
                    dotEffects++;
                    if (EffectIdentifiers.Ignite.equals(dot.getEffectIdentifier())) {
                        igniteEffects++;
                        igniteStacks += dot.getDotStacks();
                    }
                }
            }
        }
        GodRunDetector godRunDetector = GodRunDetector.getInstance();
        Runtime runtime = Runtime.getRuntime();
        long usedHeapMb = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);

        try {
            writer.write(String.join(",",
                    LocalDateTime.now().format(LINE_TIMESTAMP),
                    String.format(Locale.ROOT, "%.1f", GameState.getInstance().getGameSeconds()),
                    String.valueOf(GameState.getInstance().getStagesCompleted()),
                    String.valueOf(ticks),
                    String.format(Locale.ROOT, "%.2f", ticks > 0 ? tickTotalMs / ticks : 0),
                    String.format(Locale.ROOT, "%.2f", tickMaxMs),
                    String.format(Locale.ROOT, "%.2f", gapMaxMs),
                    String.valueOf(draws),
                    String.format(Locale.ROOT, "%.2f", draws > 0 ? drawTotalMs / draws : 0),
                    String.format(Locale.ROOT, "%.2f", drawMaxMs),
                    String.valueOf(enemies),
                    String.valueOf(enemyEffects),
                    String.valueOf(dotEffects),
                    String.valueOf(igniteEffects),
                    String.valueOf(igniteStacks),
                    String.valueOf(maxEffectsOnOneEnemy),
                    String.valueOf(MissileManager.getInstance().getMissiles().size()),
                    String.valueOf(MissileManager.getInstance().getSpecialAttacks().size()),
                    String.valueOf(AnimationManager.getInstance().getUpperAnimations().size()),
                    String.valueOf(AnimationManager.getInstance().getLowerAnimations().size()),
                    String.valueOf(usedHeapMb),
                    String.valueOf(godRunDetector.getGodRunScore()),
                    String.valueOf(godRunDetector.getEnemiesSpawned()),
                    String.valueOf(godRunDetector.getEnemiesKilled()),
                    String.format(Locale.ROOT, "%.3f", godRunDetector.getEnemyKillRatio()),
                    String.valueOf(godRunDetector.getBoardBlockTotal()),
                    String.format(Locale.ROOT, "%.1f", godRunDetector.getGameSecondsWithHighKillRatio())));
            writer.newLine();
            writer.flush();
        } catch (IOException exception) {
            System.out.println("Game load stats stopped: " + exception);
            writer = null;
        }

        ticks = 0;
        tickTotalMs = 0;
        tickMaxMs = 0;
        gapMaxMs = 0;
        draws = 0;
        drawTotalMs = 0;
        drawMaxMs = 0;
    }
}
