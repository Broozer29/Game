package net.riezebos.bruus.tbd.bot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// One scenario file from tools/bot/scenarios: what the bot plays and what counts as a pass. Every key is optional.
// load() checks the whole file and collects every mistake, so one run of a bad file lists all of them.
// See tools/bot/README.md for the keys.
class BotScenario {
    enum Dodge { AUTO, ON, OFF }

    enum Target { AUTO, NEAREST, BOSS }

    enum SpecialMode { CROWDED, ALWAYS, NEVER }

    enum FireMode { ALWAYS, NEVER }

    enum ShopMode { SKIP, BUY_ALL }

    static final Set<String> EVENTS = Set.of("boss", "relic", "score-card", "dead", "shop");
    private static final Set<String> KEYS = Set.of("description", "class", "minutes", "levels", "runs", "devSwitches", "dodge",
            "target", "special", "fire", "shop", "screenshots", "save", "expect");
    private static final Set<String> SCREENSHOT_KEYS = Set.of("intervalSeconds", "width", "events");
    private static final Set<String> EXPECT_KEYS = Set.of("maxHeapMb", "maxTickMs", "maxGapMs", "maxGcPauseMs", "noCrash");

    String name;
    String description = "";
    PlayerClass playerClass;
    Double minutes;
    Integer levels;
    Integer runs;
    List<String> devSwitches = new ArrayList<>();
    Dodge dodge = Dodge.AUTO;
    Target target = Target.AUTO;
    SpecialMode special = SpecialMode.CROWDED;
    FireMode fire = FireMode.ALWAYS;
    ShopMode shop = ShopMode.SKIP;
    Integer screenshotIntervalSeconds;
    Integer screenshotWidth;
    Set<String> screenshotEvents; // null = the default events
    String save;
    Double maxHeapMb;
    Double maxTickMs;
    Double maxGapMs;
    Double maxGcPauseMs;
    boolean noCrash = false;

    // Returns the scenario, or throws IllegalArgumentException whose message lists every problem found
    static BotScenario load(Path file) {
        List<String> problems = new ArrayList<>();
        BotScenario scenario = new BotScenario();
        String fileName = file.getFileName().toString();
        scenario.name = fileName.endsWith(".json") ? fileName.substring(0, fileName.length() - 5) : fileName;

        JsonObject root;
        try {
            JsonElement parsed = JsonParser.parseString(Files.readString(file, StandardCharsets.UTF_8));
            if (!parsed.isJsonObject()) {
                throw new IllegalArgumentException("the file must hold one JSON object ({ ... })");
            }
            root = parsed.getAsJsonObject();
        } catch (IOException e) {
            throw new IllegalArgumentException("cannot read " + file + ": " + e.getMessage());
        } catch (RuntimeException e) {
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new IllegalArgumentException("not valid JSON: " + e.getMessage());
        }

        checkKeys(root, KEYS, "", problems);
        scenario.description = text(root, "description", problems, "");
        String className = text(root, "class", problems, null);
        if (className != null) {
            try {
                scenario.playerClass = PlayerClass.valueOf(className);
            } catch (IllegalArgumentException e) {
                problems.add("class: \"" + className + "\" is not one of " + Arrays.toString(PlayerClass.values()));
            }
        }
        scenario.minutes = number(root, "minutes", problems, 0, 100000);
        Double levels = number(root, "levels", problems, 0, 100000);
        scenario.levels = levels == null ? null : levels.intValue();
        Double runs = number(root, "runs", problems, 0, 100000);
        scenario.runs = runs == null ? null : runs.intValue();
        scenario.devSwitches = devSwitches(root, problems);
        scenario.dodge = choice(root, "dodge", Dodge.class, problems, Dodge.AUTO, true);
        scenario.target = choice(root, "target", Target.class, problems, Target.AUTO, false);
        scenario.special = choice(root, "special", SpecialMode.class, problems, SpecialMode.CROWDED, false);
        scenario.fire = choice(root, "fire", FireMode.class, problems, FireMode.ALWAYS, false);
        scenario.shop = choice(root, "shop", ShopMode.class, problems, ShopMode.SKIP, false);
        scenario.save = text(root, "save", problems, null);

        if (root.has("screenshots")) {
            if (!root.get("screenshots").isJsonObject()) {
                problems.add("screenshots: must be an object");
            } else {
                JsonObject shots = root.getAsJsonObject("screenshots");
                checkKeys(shots, SCREENSHOT_KEYS, "screenshots.", problems);
                Double interval = number(shots, "intervalSeconds", problems, 0, 100000);
                scenario.screenshotIntervalSeconds = interval == null ? null : interval.intValue();
                Double width = number(shots, "width", problems, 0, 20000);
                scenario.screenshotWidth = width == null ? null : width.intValue();
                scenario.screenshotEvents = events(shots, problems);
            }
        }
        if (root.has("expect")) {
            if (!root.get("expect").isJsonObject()) {
                problems.add("expect: must be an object");
            } else {
                JsonObject expect = root.getAsJsonObject("expect");
                checkKeys(expect, EXPECT_KEYS, "expect.", problems);
                scenario.maxHeapMb = number(expect, "maxHeapMb", problems, 1, 1000000);
                scenario.maxTickMs = number(expect, "maxTickMs", problems, 1, 1000000);
                scenario.maxGapMs = number(expect, "maxGapMs", problems, 1, 1000000);
                scenario.maxGcPauseMs = number(expect, "maxGcPauseMs", problems, 1, 1000000);
                if (expect.has("noCrash")) {
                    if (expect.get("noCrash").isJsonPrimitive() && expect.getAsJsonPrimitive("noCrash").isBoolean()) {
                        scenario.noCrash = expect.get("noCrash").getAsBoolean();
                    } else {
                        problems.add("expect.noCrash: must be true or false");
                    }
                }
            }
        }

        if (!problems.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n  - ", problems));
        }
        return scenario;
    }

    private static void checkKeys(JsonObject object, Set<String> allowed, String prefix, List<String> problems) {
        for (String key : object.keySet()) {
            if (!allowed.contains(key)) {
                problems.add(prefix + key + ": unknown key (known: " + String.join(", ", new LinkedHashSet<>(allowed.stream().sorted().toList())) + ")");
            }
        }
    }

    private static String text(JsonObject object, String key, List<String> problems, String fallback) {
        if (!object.has(key)) {
            return fallback;
        }
        JsonElement element = object.get(key);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            problems.add(key + ": must be text");
            return fallback;
        }
        return element.getAsString();
    }

    private static Double number(JsonObject object, String key, List<String> problems, double min, double max) {
        if (!object.has(key)) {
            return null;
        }
        JsonElement element = object.get(key);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber()) {
            problems.add(key + ": must be a number");
            return null;
        }
        double value = element.getAsDouble();
        if (value < min || value > max) {
            problems.add(key + ": " + value + " is outside " + min + " to " + max);
            return null;
        }
        return value;
    }

    private static <E extends Enum<E>> E choice(JsonObject object, String key, Class<E> type, List<String> problems, E fallback, boolean allowBoolean) {
        if (!object.has(key)) {
            return fallback;
        }
        JsonElement element = object.get(key);
        String value;
        if (allowBoolean && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            value = element.getAsBoolean() ? "ON" : "OFF";
        } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            value = element.getAsString().toUpperCase();
        } else {
            problems.add(key + ": must be one of " + options(type, allowBoolean));
            return fallback;
        }
        try {
            return Enum.valueOf(type, value.replace('-', '_'));
        } catch (IllegalArgumentException e) {
            problems.add(key + ": \"" + element.getAsString() + "\" is not one of " + options(type, allowBoolean));
            return fallback;
        }
    }

    private static <E extends Enum<E>> String options(Class<E> type, boolean allowBoolean) {
        List<String> names = new ArrayList<>();
        for (E constant : type.getEnumConstants()) {
            names.add(constant.name().toLowerCase().replace('_', '-'));
        }
        return String.join(", ", names) + (allowBoolean ? " (true and false also work)" : "");
    }

    private static List<String> devSwitches(JsonObject root, List<String> problems) {
        List<String> names = new ArrayList<>();
        if (!root.has("devSwitches")) {
            return names;
        }
        if (!root.get("devSwitches").isJsonArray()) {
            problems.add("devSwitches: must be a list of switch names, like [\"playerIsImmune\"]");
            return names;
        }
        Set<String> known = new LinkedHashSet<>();
        for (Field field : DevTestSettings.class.getDeclaredFields()) {
            if (field.getType() == boolean.class && Modifier.isStatic(field.getModifiers())) {
                known.add(field.getName());
            }
        }
        for (JsonElement element : root.getAsJsonArray("devSwitches")) {
            String name = element.isJsonPrimitive() && element.getAsJsonPrimitive().isString() ? element.getAsString() : null;
            if (name == null) {
                problems.add("devSwitches: every entry must be a switch name in quotes");
            } else if (!known.contains(name)) {
                problems.add("devSwitches: \"" + name + "\" is not a DevTestSettings switch (known: " + String.join(", ", known) + ")");
            } else {
                names.add(name);
            }
        }
        return names;
    }

    private static Set<String> events(JsonObject shots, List<String> problems) {
        if (!shots.has("events")) {
            return null;
        }
        if (!shots.get("events").isJsonArray()) {
            problems.add("screenshots.events: must be a list, like [\"boss\", \"dead\"]");
            return null;
        }
        Set<String> events = new LinkedHashSet<>();
        JsonArray array = shots.getAsJsonArray("events");
        for (JsonElement element : array) {
            String event = element.isJsonPrimitive() && element.getAsJsonPrimitive().isString() ? element.getAsString() : null;
            if (event == null || !EVENTS.contains(event)) {
                problems.add("screenshots.events: " + element + " is not one of " + String.join(", ", EVENTS.stream().sorted().toList()));
            } else {
                events.add(event);
            }
        }
        return events;
    }
}
