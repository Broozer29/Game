# CLAUDE.md — Game

A 2D shooter built on a custom Java engine (Swing rendering, JavaFX for audio, JInput for
controllers). Written almost entirely by Bruus Riezebos; Nelis works on it from the `Nelis` branch.
`production` is the main branch and the one CI builds.

## Project facts

- Single Maven module, about 63k lines of Java in 436 files under `src/main/java/net/riezebos/bruus/tbd/`.
- Entry point: `Game.java`. Dev/test switches (no enemies, immunity, infinite money, …) live in
  `DevTestSettings.java` and must all be `false` in committed code.
- Packages: `game` (game objects, game state, items, levels, movement, player profile),
  `guiboards` (menus and screens), `visualsandaudio` (sprites, animations, audio),
  `controllerInput`, `discordconnector`.
- Assets live under `src/main/resources/` (audio, images, GIF frame folders).
- There are no automated tests. A change is checked by compiling it and playing the affected part
  of the game.

## Build and run

Requires **JDK 21** (the compiler targets 21; CI uses Temurin 21).

| purpose | command |
| --- | --- |
| compile | `mvn -q compile` |
| package jar | `mvn package` |
| packaged app (as CI does) | `mvn clean package jpackage:jpackage -Phw-accel` (or `-Pno-hw-accel` for the software renderer) |

## Working rules for this repo

- Match the existing code style of the surrounding file. Do not restyle, rename, or refactor code
  outside the task at hand.
- Work happens on the `Nelis` branch. Commit only there.
- Work in this repo is not tracked in the Plan, and there are no project bindings; skills fall
  back to their stated defaults here.
