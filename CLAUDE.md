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
| run the jar | `java -Djava.library.path=src/main/resources/libraries -Xms4g -Xmx8g -XX:+UseG1GC -jar target/Game-0.0.1-SNAPSHOT.jar` |

Any run configuration needs those three JVM settings: `java.library.path` points at the native
controller libraries, `-Xms4g -Xmx8g` raises memory, and `-XX:+UseG1GC` picks the garbage collector.
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
- Work happens on the `Nelis` branch. Commit only there.
- Work in this repo is not tracked in the Plan, and there are no project bindings; skills fall
  back to their stated defaults here.

## Planboard

In this repo, "planboard" means `docs/planboard/`: one Markdown file per game area listing bugs,
features, balance tweaks and ideas. Its rules are in `docs/planboard/README.md`; follow them when
adding or removing entries. Read it with
`bash .claude/scripts/planboard.sh <bugs|features|balance|ideas|all> [area ...]`.
