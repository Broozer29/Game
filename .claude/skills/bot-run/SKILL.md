---
name: bot-run
description: Explains how to set up and use the autoplay bot that plays the game unattended, and how to read what a run leaves behind. Use when the user asks to playtest, run the bot, do a performance, memory, crash or visual check, test a change by playing, run something unattended, or add or edit a bot scenario.
---

# Bot Run

The autoplay bot plays the game by itself, so a performance, memory, crash or visual check needs nobody at the PC.
A scenario file says what the bot plays and what counts as a pass. A run ends with a report.
Commands, flags, exit codes and troubleshooting are in `.claude/how-to/bot-run.md`: read it before the first run of a session.
The scenario keys are in the bot's own README, which the how-to file names.

## Choose the run

Pick the smallest scenario that answers the question. A scenario that does not exist yet is a new file, never a pile of command-line flags.

| The user wants | Run |
| --- | --- |
| A quick check that a change did not break the game | the short scenario for crashes |
| Memory or stutter numbers over a long play | the long scenario with limits |
| One class or one length different from a scenario | that scenario with a flag override for this run only |
| Different switches (levels, spawns) or a different save | a new scenario file: a flag can add a switch but never remove one |
| A setup that will be repeated, or that Bruus will run | a new scenario file, with a description and measured limits |

## Order of work

Every bot run is set up in this order, never out of it:
1. **Save first.** Decide the class and the items the run needs, and choose or make the test save that holds them. The test-saves README in `.claude/test-saves` says how.
2. **Scenario second.** Write or pick the scenario that names that save, the levels mode, the switches and the length.
3. **Run last.** Only then start the run script.

A scenario without a save plays a fresh, empty run of its class. Say so to the user before starting.

## Before a run

**When** you are about to start a bot run, **run this checklist** — check every item, top to bottom, skipping none:
- [ ] The game compiles from the working tree that will be played (the run uses the compiled classes, not the jar).
- [ ] The run uses the game's own memory and collector settings: `check-jvm-options.sh` passes, and the run prints them.
- [ ] No dev test switch is on in committed code: the scenario turns switches on for the run only.
- [ ] No other bot run or game window is open: two games cannot share the screen and the save file.
- [ ] The user is not playing or using the PC full-screen: the game takes the screen for the whole run.
- [ ] Nothing in the run would clean the performance folder: never use `mvn clean` in this repo, because the only copies of recordings and reports live there.
- [ ] The scenario file was read, and its switches fit the question: which levels (boss levels only or normal), spawn rate and class. A run is never described to anyone before its scenario file was read.
- [ ] The scenario names the save the question needs (see "Order of work"). When the user has named, made or asked to keep a build or save since the last run, the run uses that save unless the user said otherwise.
- [ ] The run can last: an immune ship, a start save strong enough to clear every level and boss, and a class the bot can play for the whole length.
- [ ] A launch statement went to the user before starting: class, scenario, save, the build in that save read from the file (difficulty, stage, item kinds and stacks), levels mode, switches, spawn rate and length. Each part was compared with the user's latest instruction about the run and about builds, not an earlier plan.

**If ANY item is unchecked**, you MUST fix it or tell the user and wait. **Zero exceptions.** The launch statement informs the user; it does not ask permission.

## Running it

- Start the run script with the Bash tool's background option and wait for its completion notice. Never end the command with `&` in a foreground shell: the script dies with the shell, the player's save is not put back, and the game stays open. Never poll, and never sleep in the foreground.
- One run at a time. A run is long, so do other work that does not touch the game window or the save file while it runs.
- Do not stop a run early unless the user asks or the first-minute check below fails. The script puts the player's own save back when the run ends. After a run that was killed hard, check that the save in the repo root matches the newest backup the script made.

**When** a run has just started, **run this checklist** within its first minute, reading the start of the bot log (the how-to names the lines) — check every item:
- [ ] The class playing is the one the launch statement named.
- [ ] The run script's `save build` line shows the build the launch statement named.
- [ ] A scenario with a save shows the saved run being continued, not a new run being started.
- [ ] The levels mode matches the launch statement.

**If ANY item is wrong**, you MUST stop the run, check that the player's save was put back, tell the user what was wrong, and fix it before launching again. **Zero exceptions.**

## After a run

Read the run's report first, then act on the outcome:

| Outcome | Do |
| --- | --- |
| PASS | Report the verdict and the key numbers. Look at two or three screenshots and say what they show. |
| FAIL on a limit | Say which limit and by how much. Do not change the limit to make it pass: a limit moves only when the user agrees the old one was wrong. |
| Crash or uncaught exception | Read the crash file and the game's error log from the run folder, find the cause in code, and report it with the file and method. |
| Stuck | Read the bot log for the last screen and state, look at the stuck screenshot, and decide whether the bot or the game is wrong. |
| Scenario refused | The problems are listed before the window opens: fix the file and run again. |
| No report | The game died hard: read the captured game output. |

Screenshots are the visual check: open them with the image reader and look for missing sprites, wrong sizes, overlapping text and broken screens. A clean number table does not replace looking.
Report plainly: the verdict, the numbers that matter, what the screenshots show, and anything odd. Offer the report as a planboard note only when the user asks.

## Writing a scenario

**When** you write or change a scenario, **run this checklist** — check every item, top to bottom, skipping none:
- [ ] It has a description that says what the scenario is for.
- [ ] A scenario that plays a class with items names a test save that has them, and its first run continued that save.
- [ ] Every limit comes from a measured run of the current game, with room above the measured value. A guessed limit is never written.
- [ ] Dev switches are listed in the scenario and not changed in the code.
- [ ] It runs through the game's validation without problems.
- [ ] The README key table still matches, if a key was added or its meaning changed.

**If ANY item is unchecked**, you MUST finish it before the scenario is used or committed. **Zero exceptions.**

## Changing the bot itself

The bot is game code. A change to what it does or what a scenario can say follows the repo's normal working rules, including the changelog line and a separate commit for setup files. A new behavior that no scenario needs is not added.
