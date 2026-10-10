# CLAUDE.md — Game

A 2D shooter built on a custom Java engine (Swing rendering, JavaFX for audio, Jamepad (SDL2)
for controllers). Written almost entirely by Bruus Riezebos; Nelis works on it from the `Nelis` branch.
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
| run the jar | `java -Xms4g -Xmx8g -XX:+UseG1GC -jar target/Game-0.0.1-SNAPSHOT.jar` |

Any run configuration needs those two JVM settings: `-Xms4g -Xmx8g` raises memory, and
`-XX:+UseG1GC` picks the garbage collector.
The packaged jar is about 2 GB because all assets go into it, so prefer `mvn -q compile` to check a
change.

## Where to start looking

Searching the whole codebase is expensive. Start from these files for a subject (paths under
`src/main/java/net/riezebos/bruus/tbd/game/`):

| subject | start here |
| --- | --- |
| Items | `items/items/Overclock.java` (example item), `items/ItemDescriptionRetriever.java`, `items/PlayerInventory.java` |
| Enemies | `gameobjects/enemies/enemytypes/pirates/Needler.java` (example enemy), `gameobjects/GameObject.java`, `gameobjects/enemies/EnemyManager.java` |
| Effects (ignite, self-repairing steel, …) | `items/effects/effectimplementations/DamageOverTime.java`, `gameobjects/GameObject.java` |
| Missiles | `gameobjects/enemies/enemytypes/pirates/Seeker.java` (`shootMissile()`), `gameobjects/GameObject.java`, `gameobjects/missiles/MissileManager.java` |
| Special attacks | `gameobjects/missiles/specialAttacks/SpecialAttack.java`, `gameobjects/missiles/MissileManager.java` |
| Explosions | `gameobjects/neutral/Explosion.java`, `gameobjects/missiles/MissileManager.java` |
| Player | `gameobjects/player/spaceship/SpaceShip.java`, `PrimaryPlayerGun.java` and `SecondaryPlayerGun.java` (same folder), `gameobjects/player/PlayerManager.java` |

## Working rules for this repo

- Match the existing code style of the surrounding file. Do not restyle, rename, or refactor code
  outside the task at hand.
- Bruus writes notes, code comments and commit messages in a mix of Dutch and English. Translate
  any of his text to English first, and reason only on the English version.
- Work happens on the `Nelis` branch. Commit only there.
- Every change we commit gets a line in `CHANGELOG.md` under today's date (Added / Changed / Fixed),
  in the same commit.
- Project bindings live in `.claude/bindings/`.

## Planboard and the Plan

Work is tracked twice, and both copies are kept in step:

- In this repo, "planboard" means `docs/planboard/`: one Markdown file per game area listing bugs,
  features, balance tweaks and ideas. It is how Nelis and Bruus talk about the work. Its rules are
  in `docs/planboard/README.md`; follow them when adding or removing entries. Read it with
  `bash .claude/scripts/planboard.sh <bugs|features|balance|ideas|all> [area ...]`.
- The Plan's `game` board holds Nelis's own items, under `plan-workflow`. Items are captured when
  work on a planboard entry starts, never imported in bulk.
- A Plan item names the planboard file and entry it belongs to. When either copy changes, update
  the other in the same session. Closing the Plan item deletes the planboard entry.
