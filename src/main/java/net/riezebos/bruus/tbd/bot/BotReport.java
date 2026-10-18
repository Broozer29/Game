package net.riezebos.bruus.tbd.bot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// Writes report.md at the end of a run: the scenario, a PASS or FAIL for every limit it set, the key numbers and the screenshots.
// Plain text on purpose, so anyone can read it without Claude and paste it into a planboard entry.
class BotReport {
    static final int EXIT_CODE_LIMIT_FAILED = 6;

    private record Check(String limit, String bound, String actual, boolean passed) {
    }

    private BotReport() {
    }

    // Returns true when every limit the scenario set was met (also true when it set none)
    static boolean write(Path directory, Map<String, Object> summary, String reason, int exitCode) {
        BotScenario scenario = BotSettings.getScenario();
        double[] csv = readLoadCsv(directory); // max tickMs, max gapMs, max heap, intervals
        double maxGcPause = readLongestGcPauseMs(directory);
        boolean crashed = exitCode == 1 || exitCode == 2 || exitCode == 3 || exitCode == 4 || exitCode == -1 // 3 = stuck, which is no pass either
                || reason.startsWith("crash") || Files.exists(directory.resolve("crash.txt")) || Boolean.TRUE.equals(summary.get("gameErrorLog"));
        double maxHeap = Math.max(csv[2], ((Number) summary.get("maxHeapUsedMb")).doubleValue());

        List<Check> checks = new ArrayList<>();
        if (scenario.noCrash) {
            checks.add(new Check("noCrash", "no crash", crashed ? "crashed: " + reason : "no crash", !crashed));
        }
        addMaxCheck(checks, "maxHeapMb", scenario.maxHeapMb, maxHeap, true);
        addMaxCheck(checks, "maxTickMs", scenario.maxTickMs, csv[0], csv[3] > 0);
        addMaxCheck(checks, "maxGapMs", scenario.maxGapMs, csv[1], csv[3] > 0);
        addMaxCheck(checks, "maxGcPauseMs", scenario.maxGcPauseMs, maxGcPause, maxGcPause >= 0);
        boolean allPassed = checks.stream().allMatch(Check::passed);

        StringBuilder text = new StringBuilder();
        text.append("# Bot run: ").append(BotSettings.getScenarioName()).append("\n\n");
        text.append("**Verdict: ").append(checks.isEmpty() ? "NO LIMITS SET" : allPassed ? "PASS" : "FAIL").append("**\n\n");
        if (!scenario.description.isBlank()) {
            text.append(scenario.description).append("\n\n");
        }
        text.append("The run ended because: ").append(reason).append(" (exit code ").append(exitCode).append(").\n\n");

        if (!checks.isEmpty()) {
            text.append("## Limits\n\n| limit | allowed | measured | result |\n| --- | --- | --- | --- |\n");
            for (Check check : checks) {
                text.append("| ").append(check.limit).append(" | ").append(check.bound).append(" | ").append(check.actual)
                        .append(" | ").append(check.passed ? "PASS" : "FAIL").append(" |\n");
            }
            text.append("\n");
        }

        text.append("## Run\n\n| what | value |\n| --- | --- |\n");
        text.append("| class | ").append(summary.get("playerClass")).append(" |\n");
        text.append("| minutes played | ").append(String.format("%.1f", ((Number) summary.get("elapsedSeconds")).doubleValue() / 60)).append(" |\n");
        text.append("| runs started | ").append(summary.get("runsStarted")).append(" |\n");
        text.append("| levels completed | ").append(summary.get("levelsCompleted")).append(" |\n");
        text.append("| deaths | ").append(summary.get("deaths")).append(" |\n");
        text.append("| bosses seen | ").append(summary.get("bossesSeen")).append(" |\n");
        text.append("| dev switches on | ").append(summary.get("devTestSwitchesOn")).append(" |\n");
        text.append("| game wrote an error log | ").append(summary.get("gameErrorLog")).append(" |\n\n");

        text.append("## Performance\n\n| what | value |\n| --- | --- |\n");
        text.append("| highest heap in use | ").append(Math.round(maxHeap)).append(" MB of ").append(summary.get("heapLimitMb")).append(" MB |\n");
        text.append("| slowest game tick | ").append(csv[3] > 0 ? String.format("%.1f ms", csv[0]) : "not recorded").append(" |\n");
        text.append("| longest gap between ticks | ").append(csv[3] > 0 ? String.format("%.0f ms", csv[1]) : "not recorded").append(" |\n");
        text.append("| longest garbage collection pause | ").append(maxGcPause >= 0 ? String.format("%.1f ms", maxGcPause) : "not recorded").append(" |\n\n");

        text.append("## Files in this folder\n\n");
        text.append("- `bot.log` what happened and when, `summary.json` the same numbers for tools, `load-*.csv` per-second performance, `gc.log` garbage collection.\n");
        List<String> shots = listScreenshots(directory);
        text.append("- Screenshots (").append(shots.size()).append("): ").append(shots.isEmpty() ? "none" : "`screenshots/" + String.join("`, `screenshots/", shots) + "`").append("\n");
        if (Files.exists(directory.resolve("crash.txt"))) {
            text.append("- `crash.txt` the uncaught exception.\n");
        }
        if (Files.exists(directory.resolve("error_log.txt"))) {
            text.append("- `error_log.txt` the game's own error log from this run.\n");
        }

        try {
            Files.writeString(directory.resolve("report.md"), text.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Bot: could not write report.md: " + e);
        }
        return allPassed;
    }

    private static void addMaxCheck(List<Check> checks, String name, Double bound, double actual, boolean measured) {
        if (bound == null) {
            return;
        }
        if (!measured) {
            checks.add(new Check(name, "at most " + trim(bound), "not recorded", false)); // a limit that cannot be measured is not met
            return;
        }
        checks.add(new Check(name, "at most " + trim(bound), trim(actual), actual <= bound));
    }

    private static String trim(double value) {
        return value == Math.rint(value) ? String.valueOf((long) value) : String.format("%.1f", value);
    }

    // {max tickMaxMs, max gapMaxMs, max usedHeapMb, number of rows}; the CSV is flushed after every row
    private static double[] readLoadCsv(Path directory) {
        double[] result = new double[4];
        try (Stream<Path> files = Files.list(directory)) {
            Path csv = files.filter(path -> path.getFileName().toString().startsWith("load-") && path.getFileName().toString().endsWith(".csv")).findFirst().orElse(null);
            if (csv == null) {
                return result;
            }
            List<String> lines = Files.readAllLines(csv, StandardCharsets.UTF_8);
            if (lines.size() < 2) {
                return result;
            }
            List<String> header = Arrays.asList(lines.get(0).split(","));
            int tick = header.indexOf("tickMaxMs");
            int gap = header.indexOf("gapMaxMs");
            int heap = header.indexOf("usedHeapMb");
            for (String line : lines.subList(1, lines.size())) {
                String[] cells = line.split(",");
                if (cells.length < header.size() - 6) {
                    continue; // a row cut off by the exit
                }
                result[0] = Math.max(result[0], number(cells, tick));
                result[1] = Math.max(result[1], number(cells, gap));
                result[2] = Math.max(result[2], number(cells, heap));
                result[3]++;
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Bot: could not read the load csv: " + e);
        }
        return result;
    }

    private static double number(String[] cells, int index) {
        if (index < 0 || index >= cells.length) {
            return 0;
        }
        try {
            return Double.parseDouble(cells[index].trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // -1 when there is no gc.log or no pause in it
    private static double readLongestGcPauseMs(Path directory) {
        Path log = directory.resolve("gc.log");
        double longest = -1;
        try {
            if (!Files.exists(log)) {
                return -1;
            }
            for (String line : Files.readAllLines(log, StandardCharsets.UTF_8)) {
                if (line.contains("Pause") && line.endsWith("ms")) {
                    int start = Math.max(line.lastIndexOf(' '), line.lastIndexOf(')')) + 1;
                    longest = Math.max(longest, Double.parseDouble(line.substring(start, line.length() - 2)));
                }
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Bot: could not read gc.log: " + e);
        }
        return longest;
    }

    private static List<String> listScreenshots(Path directory) {
        try (Stream<Path> files = Files.list(directory.resolve("screenshots"))) {
            return files.map(path -> path.getFileName().toString()).sorted().toList();
        } catch (IOException e) {
            return List.of();
        }
    }
}
