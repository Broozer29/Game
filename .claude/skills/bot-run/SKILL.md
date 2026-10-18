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
| One class, one switch or one length different from a scenario | that scenario with a flag override for this run only |
| A setup that will be repeated, or that Bruus will run | a new scenario file, with a description and measured limits |

## Before a run

**When** you are about to start a bot run, **run this checklist** — check every item, top to bottom, skipping none:
- [ ] The game compiles from the working tree that will be played (the run uses the compiled classes, not the jar).
- [ ] No dev test switch is on in committed code: the scenario turns switches on for the run only.
- [ ] No other bot run or game window is open: two games cannot share the screen and the save file.
- [ ] The user is not playing or using the PC full-screen: the game takes the screen for the whole run. Say how long it will take before starting.
- [ ] Nothing in the run would clean the performance folder: never use `mvn clean` in this repo, because the only copies of recordings and reports live there.

**If ANY item is unchecked**, you MUST fix it or tell the user and wait. **Zero exceptions.**

## Running it

- Start the run script as a background command and wait for its completion notice. Never poll, and never sleep in the foreground.
- One run at a time. A run is long, so do other work that does not touch the game window or the save file while it runs.
- Do not stop a run early unless the user asks. The script puts the player's own save back when the run ends. After a run that was killed hard, check that the save in the repo root matches the newest backup the script made.

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
- [ ] Every limit comes from a measured run of the current game, with room above the measured value. A guessed limit is never written.
- [ ] Dev switches are listed in the scenario and not changed in the code.
- [ ] It runs through the game's validation without problems.
- [ ] The README key table still matches, if a key was added or its meaning changed.

**If ANY item is unchecked**, you MUST finish it before the scenario is used or committed. **Zero exceptions.**

## Changing the bot itself

The bot is game code. A change to what it does or what a scenario can say follows the repo's normal working rules, including the changelog line and a separate commit for setup files. A new behavior that no scenario needs is not added.
