# Development
Settings, startup, Discord status, builds, packaging and dev tooling.

## Bugs
- Any error during a game tick closes the whole game
  - `GameBoard.actionPerformed` catches every exception, writes error_log.txt and calls `System.exit(1)` (GameBoard.java:1097-1109), so every null pointer below ends the run
  - Fix: log the error and skip that tick, or go to a game-over screen instead of exiting
- Null pointer risks that would close the game
  - `PlayerManager.getClosestSpaceShip` returns null with no ships; FinalBoss.java:135, Enemy.java:236-238 and MissileManager.java around 495-520 use it unchecked. `getRandomSpaceShip` throws on an empty list (ExplosiveGreed.java:40, FriendlyStation.java:103)
  - `deleteObject()` nulls `movementConfiguration` and `ownerOrCreator`; Enemy.java:246 and MissileManager.java:498 read the owner's movement, and Drone.java:105 and MissileDrone.java:60, 128 and 175 cast the owner to `SpaceShip`
  - MissileDrone.java:176 reads `getItemFromInventoryIfExists(RocketLauncher).getQuantity()` without a null check
  - `SpriteAnimation` reads `frames.get(0)` and `frames.get(currentFrame - 1)` without checking for empty frames; missing images fall back silently to a star or return null
- Shared fields in `ImageLoader.getImage` and the image caches are not thread-safe; only the startup order keeps the loader threads from overlapping
- CI never builds the `Nelis` branch
  - .github/workflows/build.yml:5-8 runs on pushes to `production` and `cleanup-and-refactoring` and on pull requests to `production`; a compile error on Nelis is only caught when a pull request is opened
  - Fix: add `Nelis`, or a single Linux job that runs `mvn -q compile` on every push
- JavaFX versions are mixed: `javafx-swing` 19 and `javafx-media` 21.0.2 in pom.xml
  - Maven keeps one version of the shared JavaFX modules, so media 21 probably runs on base and graphics 19 (not tested). Fix: one version property, 21.0.x
- Stale build setup in pom.xml
  - `maven-nativedependencies-plugin` has no version or executions, so it does nothing
  - The `repositories` block points at the dead `jcenter.bintray.com` under the id `central`; only JitPack is needed (for jDRPC)
  - jinput-platform 2.0.5 sits next to jinput 2.0.10 and osx-plugin 2.0.10
  - jpackage `appVersion` is fixed at 1.0 and there is no icon; `<resourceDir>` points at `src/main/resources`, which may copy all 2 GB of assets into the app image a second time (not verified)
  - Old libraries to bump when convenient: gson 2.8.9, jackson-databind 2.15.0, org.json 20230618 (versions up to this one have a known denial-of-service issue), shade plugin 3.2.4
- The jar only runs on the operating system that built it, because JavaFX natives are picked from the build machine; CI builds one jar per OS, so CI downloads are fine
- JInput natives exist only for 64-bit Windows, 64-bit Linux and Intel macOS; the VS Code configs and the documented `java -jar` command only find them when started from the repo root
- Runtime files are not ignored, and some junk is tracked
  - `playerprofile.json` is tracked and rewritten by the game; `savefile.json`, `error_log.txt` and `startup_log.txt` are not ignored (`.gitignore` has `*.log`, not `.txt`)
  - About 60 `.DS_Store` files, 6 `.idea` files and 2 `.settings` files are tracked
  - Untracking needs `git rm --cached`, which has to be done by hand outside Claude
- The repository is 3.78 GiB because 133 music WAV files (1.7 GB) are stored as plain git objects; every music change adds a full copy. Converting the music (see Visuals & Audio) and deciding on Git LFS would fix it
- CI runs 6 jobs (3 operating systems, 2 render profiles) that each upload a ~2 GB artifact kept 30 days, which uses a lot of GitHub storage
- README.md says the game "only uses JamePad and Java Swing"; it actually uses JavaFX, JInput, Jackson, Gson and jDRPC

## Features
- Log runs to show how much damage each player does, so player power creep is visible
  - Plot it on graphs
- Test run mode: after class selection, open a new screen to customise the run, as a testing environment
  - Options: normal run, difficulty, miniboss, boss
  - Choose which items the player has and which enemies to fight
  - Infinite playtime, with a way to exit

## Ideas
- Simplify ImageDatabase so adding an image takes one edit instead of five (enum, path in ImageLoader, field, load line, getImage case)
  - Put the file path on ImageEnums and keep all images in one EnumMap filled by a loop
  - Replace the ~80 copy-pasted animation loops with a table of folder and frame range, or read whatever frames are in the folder
  - Report a missing image loudly at startup instead of silently drawing the star fallback
  - Agree it with Bruus first, since it changes how every new image is added
