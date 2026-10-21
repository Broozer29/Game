# Review: branch memory-reduction

Status: 2026-10-18. Branch `memory-reduction`, to be squash-merged into `Nelis`. Reader: Bruus.

Measured numbers come from the recordings and bot runs named per row (recordings under `target/perf/` on Nelis's PC, bot reports under `tools/bot/results/`). Estimates are marked as estimates.

## 1. Summary

- The branch lowers the memory of the game process at minute 30 of play from 11.95 GB to about 6.9 GB.
- The Java heap limit goes from 8 GB to 3 GB (start size from 4 GB to 2 GB). With every non-laser kind drawn rotated, the fully grown Captain limit build needs at most about 2.6 GB of it, with 4,000 missiles on screen, and the whole process stays under 5 GB (section 3b, step 2).
- The biggest single saving is the image cache counting the graphics card copy of each image (10.84 GB to 8.50 GB). The heap limit changes save most of the rest.
- Three more changes are gameplay-neutral: mini bosses die in four small explosions, the unused level-up animation is not loaded, and rotated pictures are stored at their visible size.
- "Rotate while drawing" is on the branch: every rotating sprite kind except the lasers is drawn rotated instead of stored as rotated copies. A fully grown Captain build then needs about 2.2 GB of heap instead of 3.1 GB, and the image cache holds no rotated copies outside the lasers (section 3b). The steps are separate commits, so each can be rolled back on its own.
- Tooling changes (autoplay bot, JVM option check, cache log names) do not affect players.
- No automated tests exist. Every result below comes from compile checks, bot runs, recordings and scratch programs.
- Still open: the Firefighter's first levels (missile scales that follow the difficulty, a look decision for Nelis, section 3b), decoding boss images per level, one 1.4 s freeze and four 0.3 to 0.56 s driver stalls.

The commit list against `origin/Nelis` also shows older boss-preload commits. The file diff against `Nelis` does not include them, so their content is already in `Nelis`. Review only the commits and files named here.

One unrelated file is in the diff: `playerprofile.json` (commit `60f53919`, "Update playerprofile.json"). It is not part of the memory work.

## 2. Memory before and after

### 2.1 Process memory at minute 30

Before: run 20261016-1001 (`vmmap-run`, 8 GB heap, VMMap snapshots). Now: Firefighter run of 2026-10-17 at minute 40 (`firefighter-run`, 4 GB heap), as listed in the status file.

| part | before | now |
| --- | --- | --- |
| Java heap (committed; Java keeps its limit once grown to it) | 8.2 GB | 4.0 GB |
| Graphics card copies of drawn pictures | 2.8 GB | 2.15 GB |
| Windows | 0.57 GB | 0.5 GB |
| Everything else | about 0.4 GB (derived: total minus the three parts above) | 0.2 GB |
| Total | 11.95 GB | about 6.9 GB |

The "now" column is minute 40, the "before" column minute 30. The status file gives both totals as the minute-30 comparison.

### 2.2 Memory really in use inside the heap

| part | before | now |
| --- | --- | --- |
| Live heap after forced full cleanups | 4.1 to 4.3 GB (run 20261016-1001) | without the pilot: 2.25 to 3.16 GB (Captain bot run, start to end), 2.61 to 2.74 GB (Firefighter bot run); with the pilot: 2,602 MB at the end of the Captain bot run |
| Decoded PNG originals, kept all game | 1.25 GB (measured 2026-10-16, includes the 48 MB level-up animation) | 1.25 GB per the status file (not re-measured after the level-up change) |
| Image cache, normal section | not itemised in the sources | without the pilot 1,113 MB, of which 939 MB rotated pictures; with the pilot 542 MB, of which 377 MB rotated (end of the Captain bot run) |
| Image cache, permanent section (laser beams, cash carrier, Explosion2) | 302 MB (heap6 run) | 0.26 GB |
| Rest of the game | not itemised | about 0.5 GB |

Without the pilot, GuardianMissile held 290 to 430 MB of the rotated pictures and PlasmaLauncherMissile 250 MB (Captain only, from a bought weapon). With the pilot neither is cached.

### 2.3 Step by step

Totals are process memory at minute 30.

| # | change | measured effect | source run |
| --- | --- | --- | --- |
| 1 | Heap limit 8 GB to 6 GB, misspelled flag removed | 11.95 to 10.84 GB | heap6-run, 20261016-1154 |
| 2 | Mini bosses die in four explosions | 558 MB less in mini-boss levels; level section 644 to 85 MB | cache-budget-run, 20261016-1739 |
| 3 | Cache counts the graphics card copy | 10.84 to 8.50 GB; live heap 2.9 GB | cache-budget-run, 20261016-1739 |
| 4 | Level-up animation not loaded | 48 MB | measured by a scratch program |
| 5 | Heap limit 6 GB to 4 GB, start 2 GB | 8.50 to about 6.9 GB; Captain run at minute 16: 6.18 GB, live heap 3.1 GB, 2 emergency collections (about 0.18 s) | heap4-run 20261016-2350, firefighter-run 20261017-1503 |
| 6 | Rotated pictures stored at visible size | no memory change at the kept limit; cache drops pictures about 4 times less often (2,645 evictions in 31 minutes against 11,671 in 43) | run 20261018-1116 |
| 7 to 9 | Cache log names, RotationGeometry, bot changes | none | not applicable |
| 10 | Pilot: GuardianMissile and PlasmaLauncherMissile drawn rotated | private memory about -0.7 GB at minute 10, heap about -0.5 GB at the end (8-level Captain bot run) | memory-captain-20261018-172114 and -190356 |

## 3. The changes

### 3a. Game changes that affect memory or play

#### Heap limits (commits `b1a7ad3f`, `d2e630e7`; run commands `349e7b61`, `7ecad707`)

- Files: `pom.xml` (jpackage `javaOptions`), `.vscode/launch.json`, `CLAUDE.md`.
- Change: `-Xms4g -Xmx8g` becomes `-Xms2g -Xmx4g`. The option `-Dsun.java2d.accthresh=0` is removed.
- Why: the removed option does nothing, because JDK 21 only reads `sun.java2d.accthreshold`. The lower limit makes the game fit on a 16 GB PC.
- Effect: 11.95 to 10.84 GB at 6 GB, then 8.50 to about 6.9 GB at 4 GB.
- Review: a build above about 3.2 GB live heap would hit emergency cleanups. Without the pilot the 4 GB limit leaves about 0.8 GB of room in the grown-build runs; with it the Captain run ends at about 2.5 GB. A limit of 3 GB is not decided.

#### Mini-boss death explosions (commits `cc8fa1c9`, `f8a00ba6`)

- Files: `Enemy.initBossDeathExplosions`, `GameObject` (new list `bossDeathExplosions`, played in the destruction code next to `destructionAnimation`, getter `getBossDeathExplosions`), and the five mini bosses `Defender`, `Laserbeam`, `Mirage`, `MotherShip`, `Shuriken`.
- Change: the old code set the destruction animation to scale 3 (Defender: `scale / 1.5`). That built a 3 times enlarged copy of Explosion2. The new code plays four explosions at scale 1, spread over the boss. The first stays at the centre. The other three sit at offsets of a third of the boss width and height, with frame delays 1, 1 and 2.
- Why: the enlarged copy was a blurry picture that cost 558 MB. Bruus asked on the planboard for a list of smaller explosions.
- Effect: 558 MB less during mini-boss levels; the level section drops from 644 to 85 MB. Not measured separately from the cache budget change in a recording of its own.
- Review: the visual result of four overlapping explosions (play-test a mini-boss death). `MirageMiniBoss` clears the list on its clones (`clonedEnemy.getBossDeathExplosions().clear()`), so clones keep a small smoke death.

#### Image cache counts the graphics card copy (commit `4a181d76`)

- File: `ImageCache` (`withGraphicsCopy`, constant `GRAPHICS_COPY_FACTOR = 1.3`, eviction loop of the normal section).
- Change: the eviction loop compares `pixelBytes * (1 + 1.3)` with the 2,560 MB budget instead of `pixelBytes`.
- Why: the graphics driver keeps its own copy of every drawn picture, measured at 1.1 to 2 times the pixel bytes (1.3 used). The old budget did not count it.
- Effect: the normal section holds about 1.1 GB of pictures instead of 2.5 GB. Process memory goes from 10.84 to 8.50 GB. Slowest accepted build time: 54 ms.
- Review: the factor 1.3 is a measured average, not exact. The cache log now prints `withGraphicsMb` next to the counted size.

#### Level-up animation not loaded (commit `a4f5f6c7`)

- Files: `ImageDatabase` (loading loop commented out), `AnimationManager` (the level-up method returns early when the frame list is empty).
- Change: the 35 level-up frames are not loaded while leveling is switched off. The method is marked `@Deprecated`.
- Why: `PlayerStats.addXP` returns at once, so the frames are never drawn. Loading a save still replays the levels, so the early return prevents a crash.
- Effect: 48 MB. A level 8 save loads without a crash.
- Review: re-enabling leveling needs the loop restored. A comment in both places says so.

#### Rotated copies trimmed to their visible size (commits `4d991915`, `8c1d1836`)

- Files: `ImageCropper.copyToOwnImage` (new), `ImageCropper` (multi-frame crop now copies), `ImageRotator` (single-image crop now copies).
- Change: a cropped picture used to be `getSubimage`, a view that keeps the whole rotation square alive. The copy holds only the visible part.
- Why: the cache budget counted the visible size, but the heap held the whole square.
- Effect: at the same 2,560 MB limit the cache holds about twice as many rotated pictures and drops them about 4 times less often. Memory is unchanged. The first commit title says "about 600 MB less memory". That came from a run with the cache limit lowered to 1,152 MB, which was reverted (section 4). The second commit restores the limit.
- Review: the copy draws with `AlphaComposite.Src` into a new ARGB image. Check that pixels are identical (a scratch test `TightCropTest` showed identical pixels and own buffers).

#### Cache log names (commits `c60f1777`, `a38a9488`)

- Files: new `LOG_ImageNames` (weak map from image to sprite name), `ImageDatabase.LOG_registerImageNames`, `ImageCache` (`LOG_getTopNamesLine`, `LOG_megabytesWithPrefix`, name stored in each entry), name inheritance calls in `ImageResizer`, `ImageRotator`, `ImageCropper`, `ImageCacheSection.CacheEntry.name`, `LevelPreloader` (prints the top names at level end).
- Change: the log shows `rotMb`, `scaleMb`, `permanentRotMb`, `permanentScaleMb`, the top 10 names of the normal section, the top 5 of the permanent section and the name of the slowest rebuild.
- Why: to find which sprites hold the memory.
- Effect: logging only. The map has weak keys, so it does not keep pictures alive.
- Review: `LOG_registerImageNames` loops over all `ImageEnums` at startup and calls `getAnimation` and `getImage` inside a try/catch. Startup cost is not measured in the sources.

#### Boss health bar crash (commit `game-rotate-measure-matrix`)

- File: `BossHealthBar.updateHealthbarLength`.
- Why: a boss left with a sliver of health above 0 rounded its red bar to 0 pixels, and `ImageResizer.resizeImageToDimensions` cannot scale to width 0, so the game crashed. The bug exists on `Nelis` too; it ended two measurement runs.
- Change: the bar keeps at least 1 pixel.
- Memory effect: none.

#### Crash on an enemy removed during an attack (commit `game-rotate-draw-all` crash fix)

- File: `Enemy.triggerOnDeathActions`.
- Why: a special attack that kills an enemy also removes the enemies following it, which clears their movement setting. When the same attack then hits one of those, its death code read the cleared setting and the game crashed. It ended runs with Guillotine, with `instaKill` and with the grown `captain-limit` build. The bug exists on `Nelis` too.
- Change: the god-run board block is skipped for an enemy whose movement setting is already cleared; the kill is still counted as before.
- Memory effect: none.

### 3b. Rotate while drawing

Goal: a sprite keeps its unrotated picture and is drawn through a rotation transform. No rotated copy is built, so both the heap copy and the graphics card copy disappear. Size, position and hit pixels must stay exactly what the copy would have given.

#### RotationGeometry (committed, commit `dff4555d`)

- File: new `RotationGeometry` (199 lines): `geometry`, `frameListGeometry`, `isSolid`, with a per-image cache of content boxes (weak keys).
- Does: computes width, height, crop offset, the draw transform and the inverse mapping that `ImageRotator` would give, without building the picture.
- Exactness: identical size and crop offset for all 360 angles. The hit mask is within 1 pixel at the edge on the exact diagonals (45, 135, 225, 315 degrees), a rounding tie that Nelis accepted.
- Cost: under 0.2 ms per angle for normal sprites, 1 to 5 ms for the largest bosses and 512 px explosions.
- Used by `RotateWhileDrawing`, `Sprite`, `SpriteAnimation`, `GameObject`, `GameBoard` and `CollisionDetector` for every sprite kind drawn rotated.

#### RotateWhileDrawing and the mechanism (commit `game-rotate-draw-mechanism`)

Reviewed by two reviewers; three findings (the `setScale` order, the level-end verify totals, restoring the drawing state) are fixed and re-reviewed.

| file | change |
| --- | --- |
| `RotateWhileDrawing` (new, 139 lines) | `isEnabled(ImageEnums)`: true for every kind except the lasers, the kinds `ImageRotator` blocks and the kinds on `EXCLUDED` (empty). Verify helpers `LOG_verifySingle`, `LOG_verifyFrames`, `LOG_getSummaryLine`. Lasers are listed and never enabled. |
| `DevTestSettings` | New switch `verifyRotateWhileDrawing = false`. When true, each rotation of a kind drawn rotated is also compared with the real copy. A difference logs `rotate-verify MISMATCH`. |
| `Sprite` | Fields `drawGeometry`, `drawAngle`, `drawCrop`. `pictureWidth()` and `pictureHeight()` return the geometry size when set. `setDrawRotation`, `clearRotation`, `getDrawGeometry`. |
| `SpriteAnimation` | Per-frame `frameGeometries`. Both `rotateAnimation` overloads keep the unrotated frames for an enabled kind. `getCurrentGeometry`, `getDrawnGeometry`. Width, height, bounds and centring read the geometry size. `clearRotation` is also called in scale, clone and size-change paths. |
| `GameObject` | `rotateGameObjectTowards` and `rotateObjectTowardsAngle` keep `originalImage` for an enabled kind. `setScale` falls back to the real rotated copy first (finding F-1). |
| `GameBoard` | `drawRotated` draws the base picture through the geometry transform with nearest-neighbour interpolation, then restores transform and hints in a `finally`. Used in the sprite and animation draw methods. |
| `CollisionDetector` | `checkPixelCollision` takes an optional geometry per side. With a geometry, size comes from it and solid pixels from `RotationGeometry.isSolid`. |
| `LevelPreloader` | Prints the verify totals at level end when the verify switch is on. |

For the lasers and any kind on `EXCLUDED`, and with the verify switch false as shipped, the code paths take the old branches.

Measured so far:

| check | result |
| --- | --- |
| Verify run `verify-rotation-20261018-162722` (verify on, 3 levels) | 1,984,000 rotations checked, 0 mismatches, 0 edge differences, no exception |
| Scratch test, before the diagonal tolerance | 11,016 checked, 11 mismatches, all 1 pixel at 45, 225, 315 degrees. After the tolerance: 0 |
| Sizes and bounds with the switch off against on | 954 comparisons, 0 differences |
| `setScale` fallback (F-1) | 936 comparisons, 0 differences |
| Hit tests, 7 kinds | 6,647,248 `detectCollision` calls, 0 disagreements except 18 at 45, 135, 225, 315 degrees (the accepted tie). With one geometry shifted 3 px: 265,834 disagreements, so the test can detect errors |
| Baseline run `rotation-baseline-20261018-164003` (list empty, verify off, 4 GB settings, 3 levels) | no exception, no evictions, max tick 67 ms |
| Load run `focusfire-load-20261018-165440` (switch set empty, before the pilot, verify off) | at most 1,709 missiles, 63 ticks per second throughout |

Cost that is known: the drawn-rotated pixel test takes about 1,186 ns per call for GuardianMissile against 163 ns for the mask, about 7 times slower. Rotating while drawing is about 5 percent slower than drawing ready-made copies (earlier measurement in the recap).

Estimate made before the pilot: the two aimed missiles hold 0.5 to 0.7 GB of heap in the grown-build runs, plus their graphics card copies.

The pilot section below gives the measured saving, the draw cost and the cost with many missiles.

Open design concern from the gate: the laser-only collision overload passes no geometry, so a non-laser animation enabled through it would be hit-tested unrotated. Verify-on runs are no performance evidence, because they cache extra copies.

Review: the sprite state resets (`clearRotation` call sites), `GameObject.setScale`, `CollisionDetector.checkPixelCollision`, `GameBoard.drawRotated`.

#### Pilot: two missiles drawn rotated (commit `game-rotate-draw-pilot`)

- File: `RotateWhileDrawing` (in this commit the switch set held `GuardianMissile` and `PlasmaLauncherMissile`; the step "All non-laser kinds drawn rotated" below replaces the set).
- Why: these two aimed missiles held about two thirds of the rotated memory in the grown-build Captain runs.
- Measured with two `memory-captain` bot runs (8 levels, buy-all, the game's own settings, the game visible on screen), before and after, plus a process-memory and VMMap sampler. Cache and heap are taken at the end of the run. VMMap and the tick and draw averages are taken from the part of the before run with a proven live display (up to 840 s; VMMap at minute 10), and from the same span of the after run.
- Units: MB and GB as the tools print them, 1 MB = 1,048,576 bytes and 1 GB = 1,024 MB (the cache log and gc.log count in MB, VMMap in KB of 1,024 bytes).

| after 8 levels | before | after | difference |
| --- | --- | --- | --- |
| image cache, end of run | 1,113 MB (939 MB rotated), full | 542 MB (377 MB rotated), not full | about -571 MB |
| heap in use after mixed collections, end of run | about 3,128 MB | about 2,602 MB | about -526 MB |
| graphics-driver blocks (VMMap, minute 10) | 1.44 GB | 0.91 GB | about -0.53 GB |
| private memory (VMMap, minute 10) | 6.13 GB | 5.44 GB | about -0.68 GB |
| ticks per second (whole run) | 63.2 | 63.2 | none |
| average tick / draw (first 821 seconds) | 1.88 / 2.38 ms | 2.36 / 2.54 ms | game work about +0.5 ms, drawing about +0.16 ms |
| gaps between ticks above 200 ms | 6, all mid-level: 0.21 to 0.33 s, one 0.95 s game-logic stall, one 0.21 s right after the start | 3, all mid-level: 0.28 to 0.40 s | the same kind in both runs |

- Focus Fire load run with the pilot on: up to 1,884 missiles; above 1,000 missiles 63.3 ticks per second (lowest 58), tick 3.4 ms, draw 4.0 ms; without the pilot 63.2 ticks per second (lowest 58), tick 2.8 ms, draw 5.0 ms. Heap after mixed collections 2,346 MB against 2,793 MB. One gap of 0.29 s, two seconds after a level change; the Focus Fire run without the pilot had none above 0.08 s, while both Captain runs show mid-level gaps of 0.2 to 0.4 s with and without the pilot.
- Runs: `memory-captain-20261018-172114` (before), `memory-captain-20261018-190356` (after), `focusfire-load-20261018-165440` (before) and `focusfire-load-20261018-192430` (after); sampler files under `target/perf/pilot-run/`. Earlier after runs (174050, 175442, 181448, 183515) ran while the display was off or covered, so their drawing and graphics-driver numbers are not used.
- Review: the game work per tick rises by about 0.5 ms, most likely the slower pixel hit test of a drawn-rotated animation; at 63 ticks per second a tick may take about 16 ms.

#### Measurement matrix: which sprite kinds hold the remaining rotated memory (commit `game-rotate-measure-matrix`)

- Why: the pilot showed the two missiles only for the Captain against the Zerg. These runs rank the remaining kinds over the other factions, all bosses, the mini bosses and the classes, so the next kinds to draw rotated are chosen by size.
- Runs, all with the pilot on, an immune ship, infinite money, buy-all and the game's own Java settings:

| run | class and faction | save | result |
| --- | --- | --- | --- |
| A `matrix-captain-royalguard-20261018-211532` | Captain against the Royal Guard (Hard) | `captain-hard` | 14 levels, all 7 bosses, Shuriken, Mirage and MotherShip mini bosses, 41 minutes |
| B `matrix-firefighter-pirates-20261018-200613` | Firefighter against the Pirates (Easy) | `firefighter-easy` | 14 levels, all 7 bosses, no mini bosses, 31 minutes |
| C `matrix-carrier-zerg-20261018-203816` | Carrier against the Zerg (Medium) | `carrier-medium` | 1 normal level (Mirage mini boss), then crashed at the Striker boss after 27 minutes on it (bot.log: boss on screen at 260.9 s, crash at 1870.4 s) |
| D `matrix-final-boss-20261018-211036` | Captain against the Final boss | `captain-hard` | 2 Final boss levels, 4 minutes |

- Numbers: the highest rotated MB of each kind at any level end, from the `Level end top` lines of each run's `stdout.log` (MB = 1,048,576 bytes), counted by a script over those lines. Only kinds at 20 MB or more in some run are listed. A dash means the kind was never among the 10 kinds with the most rotated plus resized memory at a level end in that run; it may still be present below that cut.

| sprite kind | A | B | C | D |
| --- | --- | --- | --- | --- |
| StrikerBoss | 107.3 | 35.0 | - | - |
| ProtossShuttleMissileExplosion | 103.0 | - | - | 22.7 |
| MirageMiniBoss | 99.1 | - | 40.5 | - |
| Bomba_Missile | - | 63.5 | - | - |
| BarrierProjectile | - | 56.0 | - | - |
| MothershipMiniboss | 52.1 | - | - | - |
| LaserBulletDestruction | 46.1 | - | 13.2 | - |
| Impact_Explosion_One | 43.1 | 9.8 | 13.0 | 3.9 |
| RotatingBoxes | - | - | - | 37.3 |
| GuardianIdle | - | - | 29.6 | - |
| RoyalGuardBarricadeMinion | 28.5 | - | - | - |
| LaserBullet | 24.8 | - | - | 6.1 |
| StrikerCornerDrone | 24.5 | - | - | - |
| FinalBossAnim | - | - | - | 23.4 |
| ProtossArbiter | - | - | 22.3 | - |
| RoyalGuardGuardsmen | 20.0 | - | - | - |

- Most of the remaining rotated memory belongs to enemy sprites, which depend on the faction and the boss, not on the class. The Carrier's own ships were measured over one level only; Nelis chose not to run the Carrier further, because the Carrier needs a crash fix (`game-guillotine-null-movement`) to finish a run with `instaKill`.
- Two existing crashes ended runs. A boss left with a sliver of health crashed the health bar resize; it is fixed (section 3a). A special attack hitting an enemy already removed in the same attack crashed `Enemy.triggerOnDeathActions`; it is fixed too (section 3a).

#### Step 1: all non-laser kinds drawn rotated, with a fast hit test (commit `game-rotate-draw-all` step 1)

- Files: `RotateWhileDrawing` (`isEnabled` is true for every kind except the lasers, the kinds `ImageRotator` blocks and an `EXCLUDED` set, which is empty), `CollisionDetector` (new private `RotatedMask`), `DevTestSettings` (comment of the verify switch).
- Why: the measurement matrix showed most of the remaining rotated memory spread over enemy sprites. Nelis chose one rule for all kinds over choosing kinds by size.
- First try without `RotatedMask`: the frame rate fell when Focus Fire filled the screen (seen by Nelis). The hit test of a drawn-rotated sprite read every tested pixel with `getRGB` and allocated an array per pixel.
- `RotatedMask`: the same inverse rotation as `RotationGeometry.isSolid`, read once per collision check, on the cached `AlphaMask` of the unrotated picture (same alpha threshold). A scratch program compared both for every pixel, a 1-pixel border and many angles (every multiple of 5 degrees plus the next degree, all 360 for pictures up to 96 px) with and without crop: first 182 pictures (every 25th), 11,507,884,217 pixels, 0 differences; then all 4,537 pictures, 237,969,620,092 pixels, 0 differences.
- Runs (the game's own Java settings, 4 GB limit): `matrix-captain-royalguard-20261018-211532` (pilot only), `-221655` (all kinds, old hit test, stopped after 12 levels), `captain-limit-20261018-225247` (all kinds with `RotatedMask`, grown build, stopped after 6 minutes once above 1,500 missiles).

| measure | pilot only (211532) | all kinds (221655) |
| --- | --- | --- |
| image cache at the last level end | 879 MB, of which 628 MB rotated | 239 MB, of which 0 MB rotated |
| highest heap in use after a mixed collection | 3,131 MB | 2,222 MB |
| full collections | 0 | 0 |
| private memory at 1,503 s | 6,119 MB | 5,640 MB |

| missiles on screen | all kinds, old hit test (221655) | all kinds, `RotatedMask` (limit run) |
| --- | --- | --- |
| 600 to 1,000 | 54.5 ticks/s, game work 11.5 ms per tick | 62.2 ticks/s, 4.8 ms |
| 1,000 to 1,500 | 42.5 ticks/s, 18.9 ms | 60.7 ticks/s, 6.7 ms |
| above 1,500 | 28.1 ticks/s, 35.0 ms | 51.7 ticks/s, 11.1 ms |

- Numbers: ticks and times are per-second rows of the run's `load-*.csv`, grouped by the `missiles` column and averaged; heap from `gc.log` (`Pause Young (Mixed)` lines, heap after the pause); private memory from the sampler (MB = 1,048,576 bytes). The pilot-only run never reached 1,000 missiles, so there is no stored-copies number at that load.
- Java keeps its whole 4 GB heap once it has grown, so most of the heap saving shows in Windows only when the limit drops to 3 GB (next step).
- Worth it: the heap saving is about 0.9 GB for one changed rule and one 35-line class; normal play (under 1,000 missiles) runs at 63 ticks per second.

#### Step 2: heap limit 3 GB (commit `game-rotate-draw-all` step 2)

- Files: `pom.xml` (`-Xmx3g`), `.vscode/launch.json` (all three configurations), `CLAUDE.md` (run command). `check-jvm-options.sh` passes.
- Why: with every kind drawn rotated, the grown Captain build needs at most about 2.6 GB of heap, so Java no longer needs 4 GB. Java keeps the whole limit once it has grown to it, so the limit is what Windows sees.
- Run: `captain-limit-20261018-230658` (grown build, 12 minutes, 4 levels, up to about 4,300 missiles), compared with `captain-limit-20261018-225247` (same build, 4 GB, 6 minutes).

| measure | 4 GB limit (225247) | 3 GB limit (230658) |
| --- | --- | --- |
| highest heap in use after a mixed collection | 2,189 MB | 2,587 MB |
| full collections | 0 | 0 |
| longest pause | 59.6 ms | 62.4 ms |
| time in young pauses per second played | 3.0 ms | 3.4 ms |
| 1,500 to 2,500 missiles | 56.1 ticks/s | 56.1 ticks/s |
| 2,500 to 3,500 missiles | 47.6 ticks/s | 45.0 ticks/s |
| above 3,500 missiles | 33.6 ticks/s | 35.1 ticks/s |

- Process memory of the 3 GB run at minute 10 (VMMap, MB = 1,048,576 bytes, GB = 1,024 MB):

| part | size |
| --- | --- |
| Java heap (the whole limit, committed) | 3.0 GB |
| graphics-driver blocks (committed `Private Data` rows of 32,768 KB) | 1.03 GB |
| other private data (thread stacks, class data, compiled code) | about 0.25 GB |
| Windows heaps | 0.22 GB |
| loaded program files and mapped files | 0.42 GB committed, 0.03 GB private |
| **total committed** | **4.83 GB** (private memory per the sampler: 4,699 MB, 4.59 GB, at 710 s) |

- Inside the heap at the last level end: image cache 155 MB (all resized copies, none rotated, no evictions) plus the permanent section 255 MB (laser beams and `Explosion2`). The rest is the decoded PNG originals and the game's objects.
- Reading: at the same missile count the 3 GB limit runs as fast as 4 GB; garbage collection costs about 0.4 ms more per second. The worst moment leaves about 0.5 GB of the 3 GB free, with 4,000 missiles on screen. The lag above 2,500 missiles is the same with both limits, so it is not memory.
- Worth it: about 1 GB less process memory for a three-line setting change.

#### Step 3: image cache limit 1,024 MB (commit `game-rotate-draw-all` step 3)

- File: `ImageCache` (`MEMORY_BUDGET_BYTES` 2,560 MB to 1,024 MB).
- Why: the limit counts Java pixels plus an estimated graphics card copy (1.3 times the pixels). At 2,560 MB the cache could in principle hold about 1,113 MB of pixels, more than the 3 GB heap has free at its busiest. At 1,024 MB it holds at most about 445 MB. With rotated sprites drawn rotated, the normal section holds only resized copies.
- Run: `matrix-captain-royalguard-20261019-001059` (Captain from `captain-hard` against the Royal Guard, 14 levels, all 7 bosses, 3 GB limit, 32 minutes): PASS, no exception.

| measure | value |
| --- | --- |
| image cache at the last level end | 252 MB of pixels (579 MB counted against the limit), 0 MB rotated |
| copies dropped by the limit (evictions) | 0 |
| slowest rebuild of a copy during play | 1.5 ms |
| highest heap in use after a mixed collection | 2,470 MB |
| full collections | 0 |
| longest pause | 47 ms |
| private memory at 1,503 s | 4,647 MB (pilot only at 4 GB: 6,119 MB; all kinds at 4 GB: 5,640 MB) |
| under 1,000 missiles | 63.4, 57.2 and 62.0 ticks/s for 0-300, 300-600 and 600-1,000 missiles. The 300-600 band has only 39 seconds; 7 of its 9 slow seconds fall in 9 seconds of the last boss fight (level 18, 1 enemy, 489 to 571 missiles at those moments, 17 to 25 ms of game work per tick). That fight peaked at 3,879 missiles; the pilot-only run never had more than 891 in its level 18. Its exact cause is not known. |

- The 1,152 MB limit tried on 2026-10-17 caused hitches because rotated copies filled it and were dropped and rebuilt. Without rotated copies the Captain run's cache stays at about a quarter of the new limit and drops nothing. The Firefighter run fills it (see "Firefighter at 3 GB" below).
- Worth it: one number; it removes the one way the cache could outgrow the 3 GB heap.
- Seen in this run: the graphics-driver blocks keep growing with play (VMMap every 5 minutes: 0.11, 0.56, 0.77, 0.82, 1.02, 1.07, 1.53 GB), so they are now the part of the process that changes most. Next step.

#### Step 4: graphics card copies freed at every level end (commit `game-rotate-draw-all` step 4)

- Files: `ImageDatabase.flushOriginals` (new, calls `flush()` on every loaded picture and animation frame), `LevelPreloader.clearLevel` (calls it and logs the time).
- Why: Java gives every picture that is drawn a copy on the graphics card and keeps it until the picture is flushed or thrown away. The image cache flushes the copies it drops, but the loaded original pictures stay for the whole game, so their copies pile up as more enemies, bosses and effects are seen. With rotated sprites drawn rotated, the originals are what is drawn.
- `flush()` frees only the graphics card copy. The pixels stay in Java, and a picture gets a new copy the next time it is drawn.
- Runs: `matrix-captain-royalguard-20261019-001059` (step 3, without) and `-004516` (with), both Captain from `captain-hard`, 14 levels, all 7 bosses, 3 GB limit.

| graphics-driver blocks (VMMap) | minute 5 | 10 | 15 | 20 | 25 | 30 | 35 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| without (001059) | 0.56 GB | 0.77 | 0.82 | 1.02 | 1.07 | 1.53 | run over |
| with (004516) | 0.50 GB | 0.76 | 0.79 | 0.70 | 0.84 | 0.73 | 0.71 |

| measure | without | with |
| --- | --- | --- |
| private memory at about 1,500 s | 4,647 MB | 4,409 MB |
| private memory at the end of the run | 5,123 MB (1,903 s) | 4,291 MB (2,117 s) |
| time to flush, per level end | none | 7 to 52 ms, during the level-end screen |
| worst gap between ticks in the first 10 seconds of a level | 18 to 63 ms (first level 158) | 18 to 70 ms (first level 128) |
| gaps above 100 ms later in a level | 9 | 7 |

- Reading: the driver memory stops growing at about 0.7 to 0.8 GB instead of climbing past 1.5 GB. The cost is re-uploading pictures when they are first drawn in the next level: the worst early gap of a level rises by up to about 40 ms in some levels (two or three frames), for example from 31 to 70 ms and from 32 to 64 ms. The two runs grew different builds (the shop is random), so their missile counts differ and their tick numbers above 1,000 missiles do not compare.
- Worth it: about 0.8 GB less process memory late in a run for 20 lines. To check by hand: a short stutter in the first seconds of a level. If it is noticeable, flushing only the pictures of the level's boss and mini bosses is the smaller variant.

#### Firefighter at 3 GB, and the cache log names the most rebuilt sprites (commit `game-rotate-draw-all` cache log)

- Run: `matrix-firefighter-pirates-20261019-015026` (Firefighter from `firefighter-easy` against the Pirates, 14 levels, all 7 bosses, steps 1 to 4): PASS, 63 ticks per second at every missile count it reached (under 1,000).

| measure | 4 GB, pilot only (`-20261018-200613`) | 3 GB, steps 1 to 4 (`-20261019-015026`) |
| --- | --- | --- |
| image cache at the last level end | 1,112 MB (289 MB rotated), 5,220 evictions | 443 MB (0 MB rotated), 2,266 evictions |
| highest heap in use after a mixed collection | 4,093 MB | 3,068 MB |
| full collections | 1 (153 ms) | 2 (107 and 90 ms), both in the first 5 minutes |
| heap in use after a mixed collection from minute 10 on | not measured | about 2,445 MB, steady |
| private memory at the end | 6,685 MB | 4,343 MB |
| graphics-driver blocks from minute 5 on | not sampled | 0.79 to 0.83 GB |

- The full collections come from the first levels of a run. The cache log, which now lists per level the sprites with the most build time (`ImageCache.LOG_getLevelBuildSummary`: builds, distinct keys, milliseconds), shows why (run `matrix-firefighter-pirates-20261019-023603`, 8 minutes): in the first level 971 cache misses took 2.6 s, led by `EnergyCircle` (61 builds, 61 keys) and `BarrierProjectile` and its destruction animation (227 builds, 227 keys each). In the next level there were 53 misses in 18 ms.
- Cause: the Pirates' Energizer and Flamer scale their missiles with `EnemyManager.getEnemyDifficultyModifier()`, which grows smoothly with the difficulty until it reaches its cap. Until then every missile has a slightly different scale, so every missile builds a new scaled copy of its whole animation. The garbage of those copies fills the heap faster than the collector frees it. Every run goes through this early phase, because the difficulty starts at 1.
- Tried: a larger G1 region (`-XX:G1HeapRegionSize=8m`, run `-20261019-022425`): collections caused by very large allocations fell from 45 to 2 in the first 10 minutes, but the 2 full collections stayed, so it was not kept.
- Open for Nelis: rounding the missile scale (for example to 0.01) would stop the rebuilds. The size change is at most half a percent, but it is a look change, and rounded scales were put off before.

### 3c. Tooling only (no effect on players)

| change | files | what it does | memory effect |
| --- | --- | --- | --- |
| Bot continues a saved run | `AutoPlayBot`, `BotBrain`, `BotScenario`, `MainMenuBoard`, `BoardManager`, `MenuButton` | The bot presses Continue Run for the start save in a scenario instead of starting an empty run. | none |
| Shop buy-all | `ShopBoard`, `BotScenario` (`shop: buy-all`) | The bot buys every shop item, so the build grows during a run. | none |
| Fuel handling | `FireFighterPrimaryGun.getMinimumFuelToFire`, `BotBrain` | The Firefighter bot lets fuel refill instead of holding fire on an empty tank. | none |
| JVM options from `pom.xml` | `.claude/scripts/bot-run.sh`, new `.claude/scripts/check-jvm-options.sh`, `.vscode/launch.json` | The bot starts Java with the game's own options, read from the jpackage options in `pom.xml`. The check script fails when any launcher differs. The 50 ms pause option is removed from recording configurations. | none |
| Gap measurement | `LOG_GameLoadLogger.LOG_resetGap`, `lastTickStart` made `volatile`, `BoardManager` | Time in the shop or menus no longer counts as a gap between ticks. | none |
| Scenarios | `tools/bot/scenarios/memory-captain.json`, `memory-firefighter.json`, `memory-soak.json`, `smoke.json`, `tools/bot/README.md` | New and adjusted run definitions. | none |
| Docs and setup | `.claude/how-to/bot-run.md`, `.claude/skills/bot-run/SKILL.md`, `.claude/test-saves/*`, `docs/planboard/*`, `CHANGELOG.md` | Documentation and the stacked Firefighter test save. | none |

Reason for the JVM options work: the first bot runs of 2026-10-18 used an 8 GB heap and a 50 ms pause option instead of the game's 4 GB. Their heap, pause and smoothness numbers were discarded. Their cache numbers stay valid.

Setup commit: the scenarios `verify-rotation.json`, `rotation-baseline.json`, `focusfire-load.json` and the test save `captain-focusfire.json`.

Setup commit for the measurement matrix: the scenarios `matrix-captain-royalguard.json`, `matrix-firefighter-pirates.json`, `matrix-carrier-zerg.json`, `matrix-final-boss.json` and the test saves `captain-hard.json`, `firefighter-easy.json`, `carrier-medium.json`.

### 3d. Changes from Bruus's review

#### Spawn settings back in their own classes (commit `game-spawn-config-in-classes`)

- Files: `BossSummons` and `EnemySpawnSettings` removed; the 16 boss behaviour classes restored to their version before `b05e8745`; `Director`, `MirageMiniBoss` and `MotherShipMiniBoss` set their own directions and scales again; `Director.getBossSpawnScale` holds the boss scale rule (default scale, times 0.7 below resolution factor 1) for the boss spawn and the preload; `LevelPreloader` builds only the level's boss or mini bosses.
- Why: Bruus wants every spawn class to hold its own configuration, with the `EnemyEnums` defaults overridden per class. The shared list existed to preload every direction of every summon; since rotated sprites are drawn rotated, directions cost nothing to prepare.
- Measured before the change (bot runs with the summon preload switched off): no summon was among the slowest builds of any level; the slowest summon build was under 4 ms; worst tick gaps 19 to 50 ms, except one 112 ms caused by an `Explosion2` rebuild of 61 ms. Fights: Red 87 s, Space Station 69 s with 31 enemies, Striker 294 s with 36 enemies, Carrier 234 s, Blue, Twin, the MotherShip and Laserbeam mini bosses.
- Memory effect: none.

#### LOG_ prefix for analysis code (commit `game-log-prefix`)

- Renamed classes: `ImageNames` is now `LOG_ImageNames` and `GameLoadLogger` is now `LOG_GameLoadLogger`.
- Prefixed methods in `ImageCache`: `LOG_recordBuild`, `LOG_resetLevelBuilds`, `LOG_getLevelBuildSummary`, `LOG_getTopNamesLine`, `LOG_getStatsLine`, `LOG_writeStatsLine`, `LOG_mostBuilt`, `LOG_topNames`, `LOG_megabytesWithPrefix`, `LOG_getPermanentEntryCount` and `LOG_getPermanentMegabytes`.
- Prefixed methods elsewhere: `RotateWhileDrawing.LOG_verifySingle`, `LOG_verifyFrames`, `LOG_contentOffset`, `LOG_compare`, `LOG_check`, `LOG_report`, `LOG_getSummaryLine`, `LOG_getChecked` and `LOG_getMismatches` (`isEnabled` stays, it is drawing logic), `ImageDatabase.LOG_registerImageNames` and `LOG_GameLoadLogger.LOG_resetGap`.
- Extracted methods that hold the log lines which sat inside gameplay methods: `ImageCache.LOG_recordAdd` and `LOG_startStatsTimer`, `LevelPreloader.LOG_printPreload`, `LOG_printLevelEnd` and `LOG_printFlush`, `PermanentImageBuilder.LOG_printBuilt`, `AudioManager.LOG_printMusicOverdue`.
- Not prefixed: fields, the autoplay bot, and `PerformanceLogger`, `PerformanceLoggerManager` and `PerformanceMetric`.
- Why: Bruus wants analysis code to be recognisable at the call site, so it is not mistaken for gameplay logic.
- Memory effect: none.

## 4. Tried and reverted or dropped

| item | outcome | reason |
| --- | --- | --- |
| Cache limit 1,152 MB | reverted (`8c1d1836`) | Java used 0.6 GB less (about 3.1 to 2.45 GB), but the cache rebuilt pictures about twice as often and hitches were felt. |
| Laser angles per level | dropped | Adds 1.5 to 2 seconds to every level start, which is why they are built at startup. |
| Rule against early drops | dropped | After the visible-size change the cache drops pictures about 4 times less often, and no hitches were felt. |
| GC pause target (50 ms) | dropped | Pauses are 15 to 20 ms without it. |
| Coarser angle steps, smaller PNG files | not now | They change the look. |
| Rounded scales | undecided | They change sprite sizes by at most 2.5 percent. |
| Hit scan limited to each drawn-rotated picture's content box | tried 2026-10-19, not kept | Exact (a scratch check over every third picture folder found solid pixels at most 1 pixel outside the box, so a 1-pixel margin was added), but the limit-build profile showed no gain: the pixel hit test stayed at about 36 percent of the samples. |
| Shorter orbit routes for drones and orbiting missiles | not tried | Shifting the stored routes costs about 13 percent of the samples in the limit build, but Bruus reverted the 2-orbit change before (commit `2d7812a2`). |

## 5. What to check when reviewing

| spot | what to look at |
| --- | --- |
| `Enemy.initBossDeathExplosions` and the five mini boss constructors | Offsets and delays of the extra explosions, and the Mirage clone clearing. Play-test one mini-boss death. |
| `ImageCache.withGraphicsCopy` and the eviction loop | The 1.3 factor and the 1,024 MB limit. A wrong factor shifts how often the cache drops pictures. |
| `ImageCropper.copyToOwnImage` and its two call sites | Copy is exact and releases the parent image. |
| `AnimationManager` level-up method and `ImageDatabase` loading | The early return when frames are missing. Needed for loading saves. |
| `ImageDatabase.LOG_registerImageNames` | Startup cost, swallowed `RuntimeException`. |
| `pom.xml` `javaOptions` against `CLAUDE.md` and `.vscode/launch.json` | All three agree (`check-jvm-options.sh`). |
| `Sprite.clearRotation` call sites in `Sprite`, `SpriteAnimation`, `GameObject` | A missed reset leaves a stale geometry. |
| `CollisionDetector.checkPixelCollision` | Geometry path against mask path, and the laser-only overload without geometry. |
| `GameBoard.drawRotated` | Restores transform and hints on every path. |
| `playerprofile.json` in the diff | Unrelated file, probably should not be merged. |

## 6. Open work

| item | state |
| --- | --- |
| Rotate while drawing | Every rotating kind except the lasers is drawn rotated (section 3b, step 1). Lasers stay on copies. |
| Firefighter's first levels at 3 GB | 2 full collections of about 100 ms in the first 5 minutes, from missile scales that follow the difficulty. Rounding those scales would stop it; a look decision for Nelis. |
| Lag above 2,500 missiles | Only the `captain-limit` build reaches it. The costs are the explosions' pixel hit tests and the orbit routes (section 4). |
| Decode boss images per level | Parked (`game-boss-decode-per-level`). About 426 MB of boss-only originals (388 MB sprites, 38 MB projectiles). Decoding the big five takes 1.3 to 1.4 s on one core and 0.2 s on 16 cores. Needs a measurement first. |
| Freeze of 1.4 s | Once, mid-level, in the Captain run. The tick was slow while drawing took 12 ms, so the cause is game logic and unknown. That run had no Flight Recorder. |
| Driver stalls of 0.3 to 0.56 s | 4 in 18 minutes in a Firefighter run. The Flight Recorder shows the game waiting for the graphics driver in `OGLRenderQueue.flushBuffer`. Not garbage collection, locks, disk or game logic. A bot run on Direct3D would show whether they belong to the OpenGL path (`game-render-software-fallback`). |
| Native memory (`game-native-memory`) | Answered as not a leak. The part outside the heap is driver copies of drawn pictures, which grow with the cache and stop at 1.1 to 2 times the cache budget. |
| Unused `cashExplosion` (64 MB) | Loaded but never used. No decision. |
