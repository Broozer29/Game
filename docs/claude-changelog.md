# Changelog

Everything that changed since 5 October 2026.

## Controls

- Plug controllers in and out while playing. Press any button on a new controller to join.
- A short message shows when a controller connects or disconnects.
- Up to 8 players: every level starts with one ship per controller.
- Lost your controller mid-level? Reconnect it to take your ship back. A controller that joins
  mid-level plays from the next level.
- The game pauses when no living player has a controller. Press P or Menu to continue.
- More controller brands work, and every controller uses the same buttons: A attacks, B is the
  special attack, LB toggles auto attack, Menu pauses. The d-pad moves through menus.
- The keyboard controls player 1, also when controllers are connected.
- New: press E on the keyboard to toggle auto attack (Captain).
- Pausing reacts instantly and works at any moment during play.
- Only player 1 confirms the relic choice, the level score and the game-over screen.

## Performance

- Busy levels no longer turn into slow motion. Damage numbers are no longer shown on screen.
- The game starts faster and uses less memory.
- No more freeze the first time you kill a Mirage, Mother Ship, Defender or Laserbeam mini boss.
- Busy levels with many missiles and enemies run smoother.

## Items

### New
- **Rocket Launcher** (Captain, Rare): drone shots have a 10% chance to fire an exploding rocket at
  the nearest enemy.
- **Upgrade: Speed** (Captain, Rare): drones attack 15% faster per stack.
- **Upgrade: Precision** (Captain, Legendary): drones get a 20% chance per stack to deal double
  damage. Placeholder drones get twice that.

### Changed
- **Precision Overloader** is now **Upgrade: Overload** (Captain only): drones deal 15% more damage
  per stack, instead of +50% critical damage.
- **Module: Focus Fire** is now **Upgrade: Focus Fire**: drones keep firing on their own, and all
  fire one extra shot when your laserbeam hits. It now works together with Module: Accuracy and
  Module: Synergize.
- **Electro Shedding** is now **Electro Shredding**: it no longer lowers enemy armor. Instead your
  Electroshred deals more damage the more health you have.
- **Placeholder**: up to 8 drones instead of 4, and drone upgrades are twice as strong on them.
- Drones fire a little slower by default: one shot every 0.75s instead of 0.5s.
- **Wondrous Wisdomball**: starting chance 20% to 30%, bonus per failed roll 17.5% to 20%.
- **Recycler**: drop chance 10% to 7.5% per copy, and it now really rolls the chance. Before, every
  kill dropped a part.
- **Anion Inverter**: Electroshred cooldown reduction 60% to 70%.
- **Module: Electric Razor**: cooldown 1.5s to 0.5s.
- **Plasma Bullets**: burn damage 1.8% to 2%, burn time 1.5s to 2s.
- **Plasma Launcher**: damage x2 to x2.5.
- **Corrosive Oil**: lowers armor once per burn instead of on every burn tick.
- **Adrenaline**: getting hit again refreshes both its attack speed and its health regeneration.
- **Advanced Optics** is no longer offered once you have 100% critical chance.
- **Guillotine** is removed from the shop.
- Items with a stack limit stop showing up in the shop once you reach it.
- Several item descriptions and the shop refresh text are corrected.

### Fixed
- **Scorching Fury**: the Firefighter's flamethrower burn now gets stronger with your burn upgrades.
- **Wondrous Wisdomball**: "Copy Inventory" no longer also copies a legendary item.

## Classes

- **Firefighter**: attacks a bit slower, but burns deal more damage (0.011 to 0.013) and last longer
  (1.65s to 2.35s).

## Enemies and bosses

### Final Boss
- New attack in phase two: you are pulled to the centre, surrounded by a ring of clones, and the
  clones fire lasers at you one after another.
- Phase one is back, and so are the boss's taunts.
- Missile attacks in phase two come more often, with more missiles.

### Bosses and mini bosses
- **Red Boss**: slightly less health, summons more drones at once (up to 7), and throws shurikens
  more often.
- **Yellow Boss**: less health and damage, and it now appears from stage 2.
- **Shuriken mini boss**: drops a mine every 2.5 seconds.
- **Mother Ship**: keeps more drones on higher difficulties.
- **Mirage**: always gets at least one extra clone.
- Mini bosses get faster on higher difficulties.

### Difficulty
- Enemies get stronger as the difficulty rises during a run, not only after each boss.
- Bosses gain less health per level, normal enemies slightly more.
- Enemies spawn a little faster as difficulty rises.
- The time bonus at the end of a level is smaller: 4% instead of 10%.

## Bug fixes

- Some boss attacks (Final Boss mine charge, Striker bombing run, Twin Boss manoeuvres) no longer
  crash the game.
- Reflected missiles no longer crash the game.
- Enemy groups coming from the right no longer wait far off screen before arriving.
- Shieldbearers now respect their spawn limit.
- Mini bosses no longer show up more often than intended.
- Drones circling your ship stay evenly spaced.
- Music and sound effects no longer pile up or break when dying, quitting or skipping songs.
- Continuing a saved run starts the level timer correctly.
- All game-over pictures can now show up.

## For developers

- No native libraries or `-Djava.library.path` needed any more.
- The startup log (`startup_log.txt`) lists the controllers found and how long starting them took.
- New dev test switches in `DevTestSettings`: `stressTestSpawns` (5x enemy spawns) and
  `disableImageCacheBudget`.
- Planboard in `docs/planboard/`, read with `.claude/scripts/planboard.sh`.
- Claude Code setup: `CLAUDE.md`, `.claude/bindings/`, test saves in `.claude/test-saves/`, and
  recording scripts in `.claude/scripts/`.
- VS Code launch configurations and a compile task.
