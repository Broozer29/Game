# Running the autoplay bot

How to start, steer and read the bot. The skill `bot-run` says when and why; this file holds the commands.
The full key list for scenario files is `tools/bot/README.md`.

## One-time setup

Nothing to install beyond the repo's normal requirements (JDK 21, Maven). The run script does the rest:

- It compiles with `mvn -q compile`, unless told `--no-build`.
- It builds the dependency classpath once into `target/perf/bot-classpath.txt` and rebuilds it when `pom.xml` is newer.
- It runs from the compiled classes, so there is no 560 MB jar to package.
- Unplug game controllers first: a real controller on seat 1 takes the menus away from the bot.
- Run from anywhere: the script moves to the repo root itself. The game writes `startup_log.txt` and `error_log.txt` there.

## Startup settings

The bot reads the `-Xms`, `-Xmx` and `-XX:` options from the jpackage `<javaOptions>` in `pom.xml` and prints them as `JVM options: ...` at the start (also saved as `jvm-options.txt` in the run folder). They are the game's own settings, so heap and pause numbers match the game.
`bash .claude/scripts/check-jvm-options.sh` checks every place that starts the game (pom, VS Code configurations, the `CLAUDE.md` run command, the bot script) and exits 1 on a difference.
Any heap, GC-pause or tick number from a bot run made before 2026-10-18 with `-Xmx8g` is not valid for the game.

## Run a scenario

```
bash .claude/scripts/bot-run.sh --scenario smoke
```

From Claude, start it with the Bash tool's `run_in_background` option and wait for the completion notice. Never add `&` in a foreground shell: the script dies with the shell, the save is not put back and the game keeps running.

| option | meaning |
| --- | --- |
| `--scenario NAME` | run `tools/bot/scenarios/NAME.json` |
| `--minutes N` | length in minutes (0 = until a level or death limit) |
| `--class NAME` | Captain, FireFighter, Carrier or Mutalisk; ignored for the first run when the scenario has a `save`, because the class comes from the save |
| `--levels N` | stop after N completed levels |
| `--runs N` | stop after N runs ended in death |
| `--shots N` | a screenshot every N seconds (0 = off) |
| `--dev a,b` | dev test switches to turn on, added to the scenario's, for example `playerIsImmune,onlyBossLevels` |
| `--sound` | leave the audio on (it is muted by default) |
| `--jfr` | also record a Java Flight Recorder file (slower, for deep memory work) |
| `--keep-save` | play on top of the current save instead of starting fresh |
| `--no-build` | skip the compile step |

Other options given on the command line win over the scenario file for that run. `--dev` only adds switches to the scenario's, so a run without one of a scenario's switches needs another scenario file.
Without `--scenario` the script runs the bot with defaults and keeps its results under `target/perf/bot-<time>/`.

## Scenarios that exist

List them with `ls tools/bot/scenarios`. Today:

| scenario | for |
| --- | --- |
| `smoke` | 3 minutes, boss levels only, immune ship, no save, fails on a crash |
| `memory-soak` | 30 minutes, boss levels only, 5x spawns, immune ship, no save, Captain, limits on tick time, tick gap and GC pause (set under the old 8 GB startup, to be re-measured) |
| `memory-captain` | 8 levels (45-minute ceiling), normal levels, immune ship, infinite money with `shop: buy-all` (buys every shop item each visit), continues the captain-items save, image cache log on, fails only on a crash until a baseline exists |
| `memory-firefighter` | the same (8 levels, buy-all with infinite money) with the firefighter-distributed-heat save; the bot lets the fuel refill |

There is no measured heap limit yet for the 4 GB settings.
Add a scenario by copying one of these and changing it. A start save for the `save` key is a file name from `.claude/test-saves`; the bot continues that saved run, and the class comes from the save.

## Check the first minute

`head -15 tools/bot/results/<scenario>-<time>/bot.log` shows what started:

- `bot started: class X` is only the setting, not proof of what plays.
- For a scenario with a `save`, the line `main menu: continuing the saved run` proves the saved run is played. `main menu: opening class selection` means a fresh, empty run: stop it.
- A boss on screen right at the start of a run meant for normal levels means boss-only levels are on: stop it.
- The run script's own output (not bot.log) prints `save build: class X, difficulty D, stages completed N, K item kinds, S stacks`, read from the installed save. A build other than the one announced: stop it.

## What a run leaves behind

In `tools/bot/results/<scenario>-<time>/`:

| file | holds |
| --- | --- |
| `report.md` | verdict, limits with measured values, key numbers, screenshot list |
| `summary.json` | the same numbers for tools |
| `bot.log` | every screen and state change with the time, and what the bot did |
| `load-*.csv` | performance once per second: tick, gap and draw times, object counts, heap |
| `gc.log` | garbage collection pauses |
| `stdout.log` | everything the game printed, including a refused scenario's problems |
| `screenshots/` | scaled screenshots, named by time and event |
| `crash.txt`, `error_log.txt` | present only after an uncaught exception or a game error |

Git ignores everything in `tools/bot/results/` except each `report.md`.

## Exit codes

| code | meaning |
| --- | --- |
| 0 | finished and every limit met |
| 1 | the game crashed by itself (see `error_log.txt`) |
| 2 | uncaught exception (see `crash.txt`) |
| 3 | stuck: nothing changed for 15 minutes |
| 4 | the bot itself failed |
| 5 | the scenario file or a `--dev` name is wrong, nothing ran |
| 6 | finished, but a limit of the scenario was missed |

## Problems

| symptom | cause and fix |
| --- | --- |
| The bot sits on a menu until the stuck exit | a real controller is connected and holds seat 1: unplug it |
| No `summary.json` and no report | the game died before the bot started: read `stdout.log` |
| `savefile.json` missing after a run | the script backs it up as `target/perf/savefile-backup-bot-<time>.json`; copy the newest one back |
| A limit shows "not recorded" | the measurement was missing (for example no `gc.log` outside the script): run through the script |
| The screenshots are black or show another window | the game window lost the screen: nobody should use the PC during a run |
| Empty inventory in a run that should have items | the save was not continued: bot.log says `opening class selection` instead of `continuing the saved run`; stop the run |
| Dependency errors on the first run | delete `target/perf/bot-classpath.txt` and run again (never run `mvn clean`) |

The bot's flags without the script, and the Java classes, are described at the top of `src/main/java/net/riezebos/bruus/tbd/bot/BotSettings.java`.
