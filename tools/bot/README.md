# Autoplay bot

The bot plays the game by itself, so a performance, memory, crash or visual check needs nobody at the PC.
Nelis, Bruus or Claude steer it with a scenario file in `tools/bot/scenarios/`.
The game opens a full-screen window while it runs: do not use the PC for other full-screen work, and unplug game controllers first (a real controller on seat 1 takes the menus away from the bot).

## Run a scenario

```
bash .claude/scripts/bot-run.sh --scenario smoke
```

Options after the scenario name win over the file for that one run (`--minutes 5`, `--class Carrier`, `--dev playerIsImmune`).
The script compiles the game, backs up your own `savefile.json` and puts it back afterwards, runs the bot, and prints `report.md`.
It exits with 0 when the run finished and every limit was met, and with 6 when a limit was missed.
Other exit codes: 1 the game crashed, 2 an uncaught exception, 3 the bot got stuck, 4 a bot error, 5 the scenario file is wrong.

## Where the results go

`tools/bot/results/<scenario>-<time>/`. Git ignores everything there except `report.md`, so reports can be shared and screenshots stay local.
Open `report.md` first. The folder also holds `bot.log` (what happened and when), `summary.json`, `load-*.csv` (performance per second), `gc.log`, `stdout.log` and `screenshots/`.

## What the bot does

It starts a run, flies and shoots, picks a relic after a boss, flies into the portal, clicks through the score card, leaves the shop without buying, and starts a new run after a death.
It stops when a limit of the scenario is reached, or after 15 minutes with no change (exit code 3).

## Scenario keys

Every key is optional. A mistake (unknown key, class or switch, wrong type) stops the run before the window opens and lists every problem.

| key | values | meaning | default |
| --- | --- | --- | --- |
| `description` | text | what the scenario is for, copied into the report | none |
| `class` | `Captain`, `FireFighter`, `Carrier`, `Mutalisk` | class to play | `Captain` |
| `minutes` | number, 0 = no limit | stop after this many minutes | 10 |
| `levels` | number, 0 = no limit | stop after this many completed levels | 0 |
| `runs` | number, 0 = no limit | stop after this many runs ended in death | 0 |
| `devSwitches` | list of `DevTestSettings` names | switches turned on for the run, for example `["playerIsImmune", "onlyBossLevels"]`; anything not listed stays off | none |
| `dodge` | `auto`, `true`, `false` | run from missiles and enemies; `auto` dodges unless `playerIsImmune` is on | `auto` |
| `target` | `nearest`, `boss` | what the ship closes in on; automatic default is the boss when immune, the nearest enemy otherwise | automatic |
| `special` | `crowded`, `always`, `never` | when the special attack fires; `crowded` means 4 or more enemies, a nearby missile cluster or a boss | `crowded` |
| `fire` | `always`, `never` | whether the primary gun fires | `always` |
| `screenshots.intervalSeconds` | number, 0 = off | a screenshot every this many seconds | 30 |
| `screenshots.width` | number, 0 = full size | screenshots are scaled down to this width | 1280 |
| `screenshots.events` | list of `boss`, `relic`, `score-card`, `dead`, `shop` | which events get a screenshot | boss, relic, score-card, dead |
| `save` | name of a file in `.claude/test-saves` | start from that prepared save instead of a fresh run | fresh run |
| `expect.noCrash` | `true` | fail when the game crashes, throws, or exits early | not checked |
| `expect.maxHeapMb` | number | fail when the highest heap in use is above this | not checked |
| `expect.maxTickMs` | number | fail when the slowest game tick is above this | not checked |
| `expect.maxGapMs` | number | fail when the longest gap between ticks is above this | not checked |
| `expect.maxGcPauseMs` | number | fail when the longest garbage collection pause is above this | not checked |

A limit that could not be measured counts as missed.
Regular levels last as long as their song, so use `onlyBossLevels` for short runs.
The dev switches in `DevTestSettings.java` stay `false` in committed code: the scenario turns them on for the run only.

## Without the script

`-Dgame.bot.scenario=<file>` on the java command line does the same as the script, and `-Dgame.bot=true` runs the bot with defaults.
The other flags are documented at the top of `src/main/java/net/riezebos/bruus/tbd/bot/BotSettings.java`.
