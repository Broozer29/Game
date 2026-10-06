# Changelog

All notable changes on the `Nelis` branch.

## 2026-10-07

### Fixed
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
- Damage numbers and other on-screen texts reuse their fonts and fade settings instead of creating new ones for every text on every frame, which cuts work on the drawing thread when many hits land at once.

### Added
- Developer test switch `DevTestSettings.stressTestSpawns`: when on, enemy directors get 5 times the spawn credits, for memory and performance test runs. Off by default.

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
