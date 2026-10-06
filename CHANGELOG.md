# Changelog

All notable changes on the `Nelis` branch.

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
