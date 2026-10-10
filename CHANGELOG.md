# Changelog

All notable changes on the `Nelis` branch.

## 2026-10-11

### Added
- docs/claude-changelog.md: one changelog of everything present today since the `Nelis` branch was created, with changes that were later undone left out, and Bruus's merged changes in their own section.

### Changed
- docs/claude-changelog.md is rewritten for players: grouped by topic (controls, performance, items, classes, enemies and bosses, bug fixes), without technical terms, with a short section for developers.

## 2026-10-10

### Added
- The startup log (startup_log.txt) now shows how long starting the controller library takes and lists the controllers found. If the controller library cannot be started, the log also says why.

### Changed
- Every level now starts with one ship per connected controller, and there is no limit of 4 players any more. The end-of-level and game-over screens say "Press A or any key". A player whose controller drops out mid-level can reconnect and take their ship back. A controller that joins mid-level plays from the next level. The game pauses when no living player has a controller, also while flying to the portal, and Menu or P resumes it. Only the main controller confirms the relic choice, the score card and the game-over screen. The keyboard now steers player 1 properly next to a controller.
- Controllers can now be plugged in and out while the game runs. A new controller joins when any button is pressed or a stick or trigger is pushed, the menus follow the lowest-numbered seat that has a controller, and a short notice shows when a controller connects or disconnects. The main menu no longer shows the controller count.
- Dev tooling: the test saves use the item names from Bruus's balance update (Upgrade: Overload, Electro Shredding); with the old names CONTINUE RUN quietly started an empty run. The launch configurations and CLAUDE.md no longer pass `-Djava.library.path`.
- Controllers now go through Jamepad (SDL2) instead of JInput. Pause on a controller works on the Menu button, the special attack is on B only, pausing no longer hands menu control to that controller, and the d-pad moves the cursor in menus. The game no longer needs the native libraries folder or `-Djava.library.path`, and start-up skips the half-second controller wait. More controller brands are recognised, because the game ships SDL's community controller list. Pause on the pad and on P now reacts at once. The controls text in the main menu now names A for attack, B for special attack and P or the Menu button for pause.
- Planboard: the controller disconnect entry drops the note about button numbers, and the build setup entries drop the JInput and native-plugin notes.
- Planboard: the controller disconnect entry is done and removed.
- Planboard: the request to Nelis's brother now also asks for his startup_log.txt, which lists the controllers the game found.
- Planboard: new entries for auto attack sometimes stopping for a level, a controls menu for rebinding buttons, the outdated controls picture, choosing hardware acceleration in the game, and one shared startup logger; the relic-selection entry adds the shop skipping two slots and the agreed fix; a question for Bruus about the removed pause wait.
- Planboard: the Jamepad switch is done, so its entry is removed; the question for Bruus about the pause wait moves to a Balance entry.

## 2026-10-09

### Changed
- The build no longer pulls in the old JInput 2.0.5 native-library package next to JInput 2.0.10. It contained no files; the controller libraries the game uses come from its own libraries folder, so controllers work as before.
- Dev tooling: "Run Game (recording memory)" resets the save to the captain-items test save before every run, so test runs no longer continue from a grown save. A new "Run Game (Direct3D only, recording)" configuration tests drawing without OpenGL, for the second-monitor lag.
- Planboard: Bruus's answers on the sound engine and the outcome (no engine change, small fixes done), the second-monitor lag with its cause and a question for Bruus about Direct3D, enemies spawning inside the screen from above, slow relic selection with the stick, and boss image preparation and memory notes.
- Merged Bruus's production updates (balance updates part 1 and 2, build deploy fixes) into the Nelis work. Guillotine combines both changes: each copy adds 10% to the execute threshold, and the shop stops offering it once you own 4 copies. The test saves use the new name Advanced Optics for Precision Amplifier.

## 2026-10-08

### Fixed
- Enemy formations moving left no longer appear half on screen at the right edge. Formation enemies are placed by their center, so the first column now starts one enemy width past the edge, the same margin formations moving right get.
- Boss attacks with a boost sound (final boss mine charge, Striker bombing run, Twin boss manoeuvres) no longer crash the game when no copy of that sound is free. A sound that is still on its cooldown is no longer kept in the active sound list, game resets only rewind sounds that actually played, and the silent sound that looped at startup is removed.

### Changed
- Dev tooling: the stress-run watcher now also logs the game process's private memory and thread count at every reading, so memory outside Java's own tracking can be measured.
- Collision checks are cheaper in busy levels (branch `collision-performance`). Two objects are first tested for overlapping boxes, which most pairs fail, before the distance check, and the distance check no longer takes a square root; the same hits land as before. Enemy missiles that can only be shot down no longer check every player missile themselves; the missile or reflective block that acts on them still finds them.
- Resizing and rotating sprites no longer share one working image between calls, so two threads preparing sprites at the same time can no longer get each other's picture. Hits no longer work out a font size for damage numbers that are not drawn anymore, and the unused text constructor for them is removed.
- Planboard: the collision entries record the stress-run measurements and Bruus's replies of 2026-10-08. The agreed fixes (check order, missile interaction split) move under Bugs; explosions, the pixel check and stopping after a hit stay as they are.

## 2026-10-07

### Fixed
- Busy levels no longer slow down into slow motion: damage numbers are no longer drawn on screen. With many hits landing, over 100,000 of them piled up and were all drawn every frame.
- Drones orbiting the player keep their even spacing: each new orbit continues from the exact angle where the previous one ended, instead of restarting from the drone's rounded position.
- Wondrous Wisdomball's "Copy Inventory" roll no longer also copies a legendary item.
- Continuing a saved run starts the level with the right clock: the song progress bar starts empty and enemies spawn at the normal pace, instead of the level counting as almost finished.
- Unplugging a controller no longer leaves a fire button or direction stuck, and the console says "Controller disconnected." once instead of every tick. If the controller libraries fail to load, the game starts without controllers instead of failing.
- The game-over screen can now show every game-over picture, including the last one, and no longer crashes when there is only one.
- Precision Amplifier stops being offered once 8 copies reach 100% crit chance, so a 9th copy can no longer push it to 112%.
- Guillotine now works as its text says: each copy raises the execute threshold by 10%, up to 80% at the 8-copy limit.
- Recycler now rolls its drop chance (10% per copy) instead of dropping a part from every kill.
- The Royal Guard Shieldbearer limit (10 at a time) now counts Shieldbearers instead of Barricades.
- Shop texts now match what the game does: rerolls cost 15% of the minerals you entered the shop with, VIP Ticket gives one free refresh per copy, and Treasure Hunter raises the chance for Legendary items.
- Item stack limits now work in the shop and for the Wondrous Wisdomball: Barbed Missiles stops being offered at 5 copies and Recycler at 10.
- Every unlocked mini boss now has a fair chance to appear; the mini boss list no longer gains duplicates of early mini bosses every level and run.
- Corrosive Oil lowers an enemy's armor once per burn (and once per extra ignite stack), as intended, instead of on every burn tick.
- Enemy formations moving left now spawn just off-screen like the ones moving right, instead of a full screen width away, so they arrive on time.

### Changed
- The game limits how much memory it uses for rotated and resized sprite copies (2.5 GB). When the limit is reached, the copies used longest ago are dropped first, animations are stored once instead of twice, and laser pictures are no longer kept forever. Copies from earlier levels stay until the space is needed, so the portal and shop open without rebuilding their pictures. All mini bosses are now prepared at startup, so the first mini boss of a run no longer freezes the game for a moment. A dev switch (`disableImageCacheBudget` in DevTestSettings) turns the limit off for comparing.
- Claude setup for test runs: "Run Game (recording memory)" now passes `-Dgame.cacheStatsDir=target/perf`, so the game appends the image cache counters (entries, megabytes, hits, misses, evictions) every 30 seconds to `target/perf/cache-<start time>.log`, and the recording watcher prints the last line at each reading.
- Planboard: the image cache experiment ("Probeerseltje") records the first test results, why the cache is no longer emptied at level change, and the possible later steps.
- Planboard: the damage-number entry records Nelis's decision (stop drawing them, keep recording hits) and the stress-run evidence; new entry for an end-of-level damage overview; the sound-player thread entry has new evidence and a question for Bruus; the image cache budget experiment and its decisions are written down for Bruus under "Probeerseltje".
- VS Code launch configurations: the software-renderer configuration is removed; "Run Game" and "Run Game (recording memory)" remain.
- Claude setup for test runs: the recording watcher saves a class histogram and a native memory summary at every reading, and checks whether the game still runs with `tasklist` instead of starting `jcmd` every few seconds. The "Run Game (recording memory)" configuration adds a 50 ms GC pause target and native memory tracking. savefile.json is in .gitignore.
- Damage numbers and other on-screen texts reuse their fonts and fade settings instead of creating new ones for every text on every frame, which cuts work on the drawing thread when many hits land at once.
- Planboard: removed entries for bugs that are already fixed, added the twin boss timer findings and questions for Bruus (twin boss reset, the stage-count checks of Nepotism and four relics, the Royal Guard Captain delay).
- Planboard: new feature entry to remove the on-screen damage numbers, with a question for Bruus.

### Added
- Developer test switch `DevTestSettings.stressTestSpawns`: when on, enemy directors get 5 times the spawn credits, for memory and performance test runs. Off by default.
- Claude setup for test runs: ready-made test saves (.claude/test-saves) with an install script, a recording watcher and a startup-time report (.claude/scripts), a "Run Game (recording memory)" launch configuration, and startup_log.txt in .gitignore. The VS Code run buttons no longer compile first.

## 2026-10-06

### Added
- Planboard entries from a performance and memory audit (memory leaks, lag spikes, image cache misuse, rendering, collision, effects, weapons, spawning and disk saving), Firefighter and item bugs found along the way, a boss laser bug, music and Spotify research, and an ImageDatabase simplification idea.
- Planboard entries from a bug audit: controller disconnects, crash risks, saving and run resets, co-op, bosses, items and the shop, level directors, and the build and packaging setup.

### Changed
- Faster startup (about 36 s to 19 s on the measuring machine): images start loading as soon as the game launches, alongside the rest of the startup, and are decoded in memory instead of through temp files.
- Image resize and rotate cache lookups go straight to the stored key instead of checking every key, so they no longer slow down as the cache grows.
- Image cropping reads transparency a row at a time instead of pixel by pixel.
- Claude setup: work is now also tracked on a `game` board in Nelis's Plan, with project bindings in .claude/bindings (design-review as the reviewer, `mvn -q compile` as the build, a check that all dev test switches are off). CLAUDE.md says to translate Bruus's Dutch text to English before reasoning on it.
- Planboard: performance entries now record the 2026-10-06 discussion with Bruus on the laser preload, tracking lasers and the rotation crop flag.
- Planboard reading script: free text and unknown headings print as notes where they stand instead of as entries, and `--unanswered` lists only entries without a reply from Bruus. The README describes Bruus's reply format.
- Drones and orbiting missiles carry a 2-orbit route instead of 50, so moving the player no longer shifts thousands of route points per drone every tick. In the stress test the orbit code went from 53% to under 2% of CPU time.
- Rotated images are cached per whole degree (the game only draws whole degrees), flipped rotations for left-facing angles are cached too, and the laser preload walks 360 whole degrees instead of 1,800 steps of 0.2 degrees. In the stress test: about 470 MB less memory after startup, laser preload 2.3-2.7 s down to 1.0 s.

### Fixed
- The "only boss levels" dev test switch was left on in committed code; all dev test switches are off again.
- Rotated images are now cached separately for cropped and uncropped requests, so a caller always gets the version it asked for.
- The background music player is stopped and released on every music change (dying, quitting, boss levels, skipping a song), not only when a level ends normally, so local music files no longer leak a player each time.
- A special-attack hit on an enemy that already has the effect no longer copies the effect and its animation only to throw the copy away.
- Objects no longer keep finished attacks in their follower list: invisible followers are removed before followers are moved.
- The song progress bar is no longer resized every frame when its size has not changed. The unused health, shield and overload bar drawing is marked deprecated and kept.
- Tracking laser beams that fire from a moving object now follow that object. No boss uses this today, so nothing changes in play yet; it removes a hidden bug for future lasers.

- Burn and Scorch effects no longer overwrite the game's shared animation frames when they crop them, and cropped frames are reused, so their resized images come from the cache instead of being rebuilt for every burn stack. In the stress test cropping allocated 69% less memory and used 75% less CPU.

## 2026-10-05

### Added
- Rocket Launcher item (Rare, Captain only): drone shots have a 10% chance to fire an exploding rocket at the nearest enemy, dealing 100% drone damage per stack.
- CLAUDE.md with project facts, build/run commands, a code map and working rules (including keeping this changelog), plus Claude Code project settings.
- docs/planboard, a list of bugs, features, balance tweaks and ideas per game area, with a reading script (.claude/scripts/planboard.sh).
- VS Code "Run Game" launch configurations (hardware and software renderer) and a default compile build task.

### Changed
- VS Code no longer formats Java files on save in this repo, so the original formatting is kept.
- Shell scripts are checked out with Unix line endings (.gitattributes).

### Fixed
- The game no longer crashes when a reflective block reflects a missile that has no flight path.
