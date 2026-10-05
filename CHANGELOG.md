# Changelog

All notable changes on the `Nelis` branch.

## 2026-10-06

### Changed
- Faster startup (about 36 s to 19 s on the measuring machine): images start loading as soon as the game launches, alongside the rest of the startup, and are decoded in memory instead of through temp files.
- Image resize and rotate cache lookups go straight to the stored key instead of checking every key, so they no longer slow down as the cache grows.
- Image cropping reads transparency a row at a time instead of pixel by pixel.

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
