package net.riezebos.bruus.tbd.bot;

import net.riezebos.bruus.tbd.controllerInput.BotInputReader;
import net.riezebos.bruus.tbd.controllerInput.ControllerManager;
import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager.ScreenType;

import javax.swing.Timer;

// Plays the game by itself so a run can be left unattended: it starts a run, flies and shoots, picks a relic, walks through the
// score card and the shop, starts again after a death, and stops after the limits set in BotSettings. Everything it does goes
// through a virtual controller on seat 1 and the same screen-opening calls the menu buttons make.
// Only active with -Dgame.bot=true. See BotSettings for the flags and BotRunRecorder for what a run leaves behind.
public class AutoPlayBot {
    private static final int TICK_MILLIS = 15;
    private static final long SETTLE_MILLIS = 1500; // on a menu screen the bot waits this long before acting, so the screen can be seen and photographed
    private static final long SCREENSHOT_AFTER_EVENT_MILLIS = 400; // gives the screen time to draw the new state first
    private static final long BOSS_SCREENSHOT_DELAY_MILLIS = 3000;
    private static final long PAUSED_BEFORE_RESUME_MILLIS = 2000;

    private final BotInputReader reader = new BotInputReader();
    private final BotBrain brain = new BotBrain();
    private final BotRunRecorder recorder;
    private final Timer timer = new Timer(TICK_MILLIS, e -> tick());

    private String position = "";
    private long positionSinceMillis;
    private long readyAtMillis;
    private long lastScreenshotMillis;
    private long pendingScreenshotAtMillis = 0;
    private String pendingScreenshotLabel = "";
    private int relicTaps = 0;
    private boolean bossWasAlive = false;
    private boolean finished = false;
    private double lastGameSeconds = -1;
    private long lastProgressMillis;

    private AutoPlayBot() {
        recorder = new BotRunRecorder(BotSettings.getOutputDirectory());
    }

    // Called once the main menu is up. Does nothing unless the game was started with -Dgame.bot=true.
    public static void startIfEnabled() {
        if (BotSettings.isEnabled()) {
            new AutoPlayBot().start();
        }
    }

    private void start() {
        Thread.setDefaultUncaughtExceptionHandler((thread, error) -> {
            error.printStackTrace();
            recorder.writeCrash(thread, error);
            recorder.finish("crash: uncaught " + error.getClass().getSimpleName() + " on thread " + thread.getName(), 2);
            Runtime.getRuntime().halt(2);
        });
        // The game ends itself with System.exit(1) when its loop throws; this still leaves a summary behind
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                recorder.finish("the game exited before the bot was done (see error_log.txt in this folder when it exists)", -1), "BotShutdown"));

        ControllerManager.getInstance().installVirtualController(reader);
        long now = System.currentTimeMillis();
        positionSinceMillis = now;
        lastProgressMillis = now;
        lastScreenshotMillis = now;
        readyAtMillis = now + SETTLE_MILLIS;
        recorder.event("bot started: class " + BotSettings.getPlayerClass() + ", " + BotSettings.getMaxMinutes() + " min, levels "
                + BotSettings.getMaxLevels() + ", runs " + BotSettings.getMaxDeaths() + ", screenshots every " + BotSettings.getScreenshotSeconds()
                + " s, folder " + BotSettings.getOutputDirectory());
        timer.start();
    }

    private void tick() {
        if (finished) {
            return;
        }
        try {
            step();
        } catch (RuntimeException error) {
            error.printStackTrace();
            recorder.writeCrash(Thread.currentThread(), error);
            end("bot error: " + error, 4);
        }
    }

    private void step() {
        reader.advanceTap();
        long now = System.currentTimeMillis();
        BoardManager boardManager = BoardManager.getInstance();
        ScreenType screen = boardManager.getCurrentScreen();
        if (screen == null) {
            return; // the window is still loading
        }
        GameStatusEnums state = GameState.getInstance().getGameState();
        String newPosition = screen == ScreenType.GAME ? "GAME/" + state : screen.name();
        if (!newPosition.equals(position)) {
            onPositionChanged(position, newPosition, now);
        }
        double gameSeconds = GameState.getInstance().getGameSeconds();
        if (gameSeconds != lastGameSeconds) {
            lastGameSeconds = gameSeconds;
            lastProgressMillis = now; // the game clock moving counts as progress, so a long level is not "stuck"
        }
        if (newPosition.equals(position) && !screen.equals(ScreenType.GAME)) {
            lastProgressMillis = Math.max(lastProgressMillis, positionSinceMillis);
        }
        recorder.sampleHeap();
        recorder.setLastPosition(position);
        takeScreenshotsThatAreDue(now);
        if (checkEnd(now)) {
            return;
        }

        switch (screen) {
            case MAIN_MENU:
                if (now >= readyAtMillis) {
                    recorder.event("main menu: opening class selection");
                    boardManager.menuToClassSelection();
                }
                break;
            case CLASS_SELECTION:
                if (now >= readyAtMillis) {
                    recorder.event("class selection: choosing " + BotSettings.getPlayerClass());
                    PlayerStats.getInstance().setPlayerClass(BotSettings.getPlayerClass());
                    boardManager.openDifficultyScreen();
                }
                break;
            case DIFFICULTY_SELECTION:
                if (now >= readyAtMillis) {
                    recorder.event("difficulty selection: starting the run");
                    PlayerInventory.getInstance().resetInventory();
                    boardManager.initGame();
                }
                break;
            case SHOP:
                if (now >= readyAtMillis) {
                    recorder.event("shop: leaving without buying");
                    boardManager.initGame();
                }
                break;
            case GAME:
                playGame(state, now);
                break;
            default:
                // The boon screen is not part of a run the bot starts; leave it
                if (now >= readyAtMillis) {
                    boardManager.initMainMenu();
                }
                break;
        }
    }

    private void onPositionChanged(String from, String to, long now) {
        recorder.event(from.isEmpty() ? "now at " + to : from + " -> " + to);
        position = to;
        positionSinceMillis = now;
        lastProgressMillis = now;
        readyAtMillis = now + SETTLE_MILLIS;
        relicTaps = 0;
        reader.releaseGameplayInput();

        if (from.equals(ScreenType.DIFFICULTY_SELECTION.name()) && to.startsWith("GAME/")) {
            recorder.runStarted();
        }
        if (to.equals("GAME/" + GameStatusEnums.Show_Level_Score_Card)) {
            recorder.levelCompleted();
            scheduleScreenshot(now + SCREENSHOT_AFTER_EVENT_MILLIS, "score-card");
        }
        if (to.equals("GAME/" + GameStatusEnums.Dead)) {
            recorder.died();
            scheduleScreenshot(now + SCREENSHOT_AFTER_EVENT_MILLIS, "dead");
        }
        if (to.equals(ScreenType.SHOP.name())) {
            scheduleScreenshot(now + SETTLE_MILLIS - SCREENSHOT_AFTER_EVENT_MILLIS, "shop");
        }
        if (to.equals("GAME/" + GameStatusEnums.SelectingRelic)) {
            scheduleScreenshot(now + SCREENSHOT_AFTER_EVENT_MILLIS, "relic");
        }
    }

    private void playGame(GameStatusEnums state, long now) {
        SpaceShip ship = PlayerManager.getInstance().getSpaceship();
        switch (state) {
            case Playing:
                brain.play(reader, ship);
                watchForBoss(now);
                break;
            case Level_Finished:
                brain.flyToPortal(reader, ship);
                break;
            case SelectingRelic:
                reader.releaseGameplayInput();
                if (now >= readyAtMillis) {
                    // The first left or right press only puts the cursor on the first relic, then confirm takes it
                    // (a few confirms later it moves on, in case the first relic could not be taken)
                    if (reader.tap(relicTaps % 4 == 0 ? MenuAction.RIGHT : MenuAction.CONFIRM)) {
                        relicTaps++;
                    }
                }
                break;
            case Show_Level_Score_Card:
            case Dead:
                reader.releaseGameplayInput();
                if (now >= readyAtMillis) {
                    reader.tap(MenuAction.CONFIRM); // any button continues
                }
                break;
            case Paused:
                reader.releaseGameplayInput();
                if (now - positionSinceMillis > PAUSED_BEFORE_RESUME_MILLIS) {
                    recorder.event("the game is paused, resuming");
                    reader.tap(MenuAction.BACK);
                }
                break;
            default:
                reader.releaseGameplayInput(); // dying, zooming, loading: nothing to do
                break;
        }
    }

    private void watchForBoss(long now) {
        boolean bossAlive = EnemyManager.getInstance().getEnemies().stream().anyMatch(BotBrain::isBoss);
        if (bossAlive && !bossWasAlive) {
            recorder.bossSeen();
            recorder.event("a boss is on screen");
            scheduleScreenshot(now + BOSS_SCREENSHOT_DELAY_MILLIS, "boss");
        }
        bossWasAlive = bossAlive;
    }

    // Only for the events the scenario asks for (the default is boss, relic, score card and death)
    private void scheduleScreenshot(long atMillis, String label) {
        if (!BotSettings.takesEventScreenshot(label)) {
            return;
        }
        pendingScreenshotAtMillis = atMillis;
        pendingScreenshotLabel = label;
    }

    private void takeScreenshotsThatAreDue(long now) {
        if (pendingScreenshotAtMillis != 0 && now >= pendingScreenshotAtMillis) {
            pendingScreenshotAtMillis = 0;
            recorder.screenshot(pendingScreenshotLabel);
        }
        int interval = BotSettings.getScreenshotSeconds();
        if (interval > 0 && now - lastScreenshotMillis >= interval * 1000L) {
            lastScreenshotMillis = now;
            recorder.screenshot(position);
        }
    }

    // True when the run is over and the game is shutting down
    private boolean checkEnd(long now) {
        double minutes = BotSettings.getMaxMinutes();
        if (minutes > 0 && recorder.elapsedSeconds() >= minutes * 60) {
            end("time limit reached", 0);
            return true;
        }
        if (BotSettings.getMaxLevels() > 0 && recorder.getLevelsCompleted() >= BotSettings.getMaxLevels()) {
            end("level limit reached", 0);
            return true;
        }
        if (BotSettings.getMaxDeaths() > 0 && recorder.getDeaths() >= BotSettings.getMaxDeaths()) {
            end("death limit reached", 0);
            return true;
        }
        if (now - lastProgressMillis > BotSettings.getStuckSeconds() * 1000L) {
            recorder.screenshot("stuck");
            recorder.setNote("nothing changed for " + BotSettings.getStuckSeconds() + " s at " + position);
            end("stuck at " + position, 3);
            return true;
        }
        return false;
    }

    private void end(String reason, int exitCode) {
        finished = true;
        timer.stop();
        reader.resetInputStates();
        System.exit(recorder.finish(reason, exitCode));
    }
}
