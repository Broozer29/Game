package net.riezebos.bruus.tbd.bot;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

// Everything the autoplay bot is told at launch. The bot is on with -Dgame.bot=true or -Dgame.bot.scenario=<file>.
//
// A scenario file (see BotScenario and tools/bot/README.md) holds the settings. These system properties set the same things for one
// run and win over the file:
//   -Dgame.bot.class=Captain        Captain, FireFighter, Carrier or Mutalisk
//   -Dgame.bot.minutes=10           stop after this many minutes of play (0 = no limit)
//   -Dgame.bot.levels=0             stop after this many completed levels (0 = no limit)
//   -Dgame.bot.runs=0               stop after this many runs have ended in death (0 = no limit)
//   -Dgame.bot.screenshotSeconds=30 a screenshot every this many seconds (0 = off)
//   -Dgame.bot.shotWidth=1280       screenshots are scaled down to this width (0 = full size)
//   -Dgame.bot.stuckSeconds=900     stop with exit code 3 when the screen, the game state and the game clock have not changed for this long
//   -Dgame.bot.dir=<folder>         where logs, screenshots and the report go
//   -Dgame.dev.<name>=true          sets the DevTestSettings switch of that name, for example -Dgame.dev.devTestMuteMode=true
// A scenario that is wrong stops the game before its window opens, with exit code 5.
public class BotSettings {
    private static final Set<String> DEFAULT_EVENTS = Set.of("boss", "relic", "score-card", "dead");

    private static boolean enabled;
    private static BotScenario scenario = new BotScenario();
    private static String scenarioName = "manual";
    private static PlayerClass playerClass = PlayerClass.Captain;
    private static double maxMinutes = 10;
    private static int maxLevels = 0;
    private static int maxDeaths = 0;
    private static int screenshotSeconds = 30;
    private static int screenshotWidth = 1280;
    private static int stuckSeconds = 900;
    private static Path outputDirectory;

    private BotSettings() {
    }

    // Called first thing in main, before anything reads DevTestSettings or the logger properties
    public static void apply() {
        String scenarioPath = System.getProperty("game.bot.scenario");
        enabled = Boolean.getBoolean("game.bot") || scenarioPath != null;
        if (scenarioPath != null) {
            try {
                scenario = BotScenario.load(Paths.get(scenarioPath).toAbsolutePath());
            } catch (IllegalArgumentException e) {
                System.err.println("Bot scenario " + scenarioPath + " is not valid:\n  - " + e.getMessage());
                System.exit(5);
            }
            scenarioName = scenario.name;
        }
        applyDevTestSettings();
        if (!enabled) {
            return;
        }
        try {
            playerClass = scenario.playerClass != null ? scenario.playerClass : PlayerClass.Captain;
            if (System.getProperty("game.bot.class") != null) {
                playerClass = PlayerClass.valueOf(System.getProperty("game.bot.class"));
            }
            maxMinutes = Double.parseDouble(System.getProperty("game.bot.minutes", String.valueOf(scenario.minutes != null ? scenario.minutes : 10)));
            maxLevels = Integer.getInteger("game.bot.levels", scenario.levels != null ? scenario.levels : 0);
            maxDeaths = Integer.getInteger("game.bot.runs", scenario.runs != null ? scenario.runs : 0);
            screenshotSeconds = Integer.getInteger("game.bot.screenshotSeconds", scenario.screenshotIntervalSeconds != null ? scenario.screenshotIntervalSeconds : 30);
            screenshotWidth = Integer.getInteger("game.bot.shotWidth", scenario.screenshotWidth != null ? scenario.screenshotWidth : 1280);
            stuckSeconds = Integer.getInteger("game.bot.stuckSeconds", 900);
        } catch (RuntimeException e) {
            System.err.println("A -Dgame.bot.* flag is not valid: " + e);
            System.exit(5);
        }

        String dir = System.getProperty("game.bot.dir");
        if (dir == null || dir.isBlank()) {
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            dir = scenarioPath != null ? "tools/bot/results/" + scenarioName + "-" + stamp : "target/perf/bot-" + stamp;
        }
        outputDirectory = Paths.get(dir).toAbsolutePath();

        // The per-interval performance recorder reads its folder when it starts, so it has to be told here
        if (System.getProperty("game.loadStatsDir") == null) {
            System.setProperty("game.loadStatsDir", outputDirectory.toString());
        }
    }

    // The scenario's devSwitches, then every -Dgame.dev.<name>=true|false, flip the DevTestSettings field of that name
    private static void applyDevTestSettings() {
        for (String name : scenario.devSwitches) {
            setDevSwitch(name, true, "scenario " + scenarioName);
        }
        for (String property : System.getProperties().stringPropertyNames()) {
            if (property.startsWith("game.dev.") && !isDevSwitch(property.substring("game.dev.".length()))) {
                System.err.println("-D" + property + " is not a DevTestSettings switch");
                System.exit(5);
            }
        }
        for (Field field : DevTestSettings.class.getDeclaredFields()) {
            if (field.getType() != boolean.class || !Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            String value = System.getProperty("game.dev." + field.getName());
            if (value != null) {
                setDevSwitch(field.getName(), Boolean.parseBoolean(value), "-Dgame.dev." + field.getName());
            }
        }
    }

    private static boolean isDevSwitch(String name) {
        try {
            Field field = DevTestSettings.class.getDeclaredField(name);
            return field.getType() == boolean.class && Modifier.isStatic(field.getModifiers());
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private static void setDevSwitch(String name, boolean value, String source) {
        try {
            Field field = DevTestSettings.class.getDeclaredField(name);
            field.setBoolean(null, value);
            System.out.println("DevTestSettings." + name + " = " + value + " (from " + source + ")");
        } catch (ReflectiveOperationException e) {
            System.err.println("Could not set DevTestSettings." + name + ": " + e);
            System.exit(5);
        }
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static BotScenario getScenario() {
        return scenario;
    }

    public static String getScenarioName() {
        return scenarioName;
    }

    public static PlayerClass getPlayerClass() {
        return playerClass;
    }

    public static double getMaxMinutes() {
        return maxMinutes;
    }

    public static int getMaxLevels() {
        return maxLevels;
    }

    public static int getMaxDeaths() {
        return maxDeaths;
    }

    public static int getScreenshotSeconds() {
        return screenshotSeconds;
    }

    public static int getScreenshotWidth() {
        return screenshotWidth;
    }

    public static int getStuckSeconds() {
        return stuckSeconds;
    }

    public static Path getOutputDirectory() {
        return outputDirectory;
    }

    // Dodging is automatic: the bot does not run from danger while the ship is immune. A scenario can force it either way.
    public static boolean isDodging() {
        switch (scenario.dodge) {
            case ON:
                return true;
            case OFF:
                return false;
            default:
                return !DevTestSettings.playerIsImmune;
        }
    }

    // Automatic: the boss when the ship is immune (so the level can finish), the nearest enemy otherwise
    public static boolean targetsBoss() {
        switch (scenario.target) {
            case BOSS:
                return true;
            case NEAREST:
                return false;
            default:
                return DevTestSettings.playerIsImmune;
        }
    }

    public static BotScenario.SpecialMode getSpecialMode() {
        return scenario.special;
    }

    public static BotScenario.FireMode getFireMode() {
        return scenario.fire;
    }

    public static BotScenario.ShopMode getShopMode() {
        return scenario.shop;
    }

    public static boolean takesEventScreenshot(String event) {
        return (scenario.screenshotEvents != null ? scenario.screenshotEvents : DEFAULT_EVENTS).contains(event);
    }
}
