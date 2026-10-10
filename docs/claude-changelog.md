# Changelog since the Nelis branch

All changes present in the code today compared with commit `af04fe6d`, where the `Nelis` branch was
created from `production` on 2026-10-05. The end point is commit `c8ad31e0` (2026-10-10).

Changes that were made and later undone are left out. A change made in several steps is described
once, by its end result. Bruus's changes that reached this branch through merges from `production`
are listed separately in their own section.

## Nelis's changes

### Controllers

#### Added
- Controllers can be plugged in and out while the game runs. A new controller joins by pressing any
  button or pushing a stick or trigger, and takes the lowest free player slot. The press that joined
  does not also confirm a menu or fire.
- A short notice shows on every screen when a controller connects or disconnects.
- The game pauses on its own when no living ship has a controller, also while flying to the portal.
  P or the Menu button resumes it. A paused portal flight resumes as a portal flight.
- On the keyboard, E toggles auto attack for the Captain, like LB on a controller.

#### Changed
- Controllers work through Jamepad (SDL2). More controller brands are recognised, because the game
  ships SDL's community controller list. The buttons are the same on every pad: A fires, B is the
  special attack, LB toggles auto attack, the Menu button pauses, and the left stick or d-pad moves
  the cursor in menus.
- Every level starts with one ship per connected controller, up to 8, or one keyboard ship when
  there are none.
- A ship whose controller drops out keeps its place, and the player takes it back by reconnecting. A
  controller that joins mid-level plays from the next level.
- The keyboard flies player 1 next to the controllers, from the first key press of a level. Releasing
  the keys always stops the ship.
- Only the main controller confirms the relic choice, the level score card and the game-over screen.
  The menus follow the main controller. Ships stop moving on those screens.
- Pausing on P or the Menu button reacts at once and works at any moment of normal play. Holding the
  Menu button no longer flips pause on and off.
- The main menu's controls text names A for attack, B for special attack, and P or the Menu button
  for pause. The end-of-level and game-over screens say "Press A or any key".
- The main menu no longer shows how many controllers were found.
- The game no longer needs a native libraries folder or the `-Djava.library.path` setting.

#### Fixed
- The pause key keeps working when player 1 has no ship, because they died or joined mid-level.

### Performance

#### Changed
- Damage numbers are no longer drawn. In busy levels more than a hundred thousand could pile up and
  made the game run in slow motion.
- Resized and rotated pictures share one cache with a memory limit. When the limit is reached, the
  pictures used longest ago are dropped first, so memory use stays bounded.
- Rotated pictures are cached per whole degree, including flipped pictures for ships facing left.
  Lasers are preloaded for all 360 degrees.
- Cropping, rotating and resizing pictures is faster, and cropped animation frames are reused.
- Collision checks cost less in busy levels. A cheap box test runs before the distance check, and the
  same hits land as before.
- Enemy missiles that can only be shot down no longer check every player missile themselves.
- The game starts faster. Pictures load in the background while the window, audio and controllers
  start up.
- The four mini bosses with a large destruction picture (Mirage, Mother Ship, Defender, Laserbeam)
  are preloaded at start, so the first kill of each no longer freezes the game.
- On-screen texts reuse their fonts and fade settings instead of creating new ones every frame.

#### Fixed
- Two threads preparing pictures at the same time can no longer get each other's picture.
- Burn and Scorch effects no longer overwrite the game's shared animation frames.
- A special-attack hit on an enemy that already has the effect no longer copies the effect only to
  throw it away.
- Objects drop finished attacks from the list of things following them.

### Gameplay, items and enemies

#### Added
- Rocket Launcher, a Rare Captain item. Drone shots have a 10% chance to fire an exploding rocket at
  the nearest enemy, dealing 100% of drone damage per stack.

#### Changed
- Advanced Optics stops being offered once your copies reach 100% critical chance.
- Item stack limits in the shop and in the Wondrous Wisdomball count the copies you own, so a capped
  item stops being offered at its limit.
- Corrosive Oil lowers armor once per burn instead of on every burn tick. Its description says the
  enemy permanently takes more damage.
- Shop texts match the game. The refresh button says 15% of your minerals, VIP Ticket says 1 (+1)
  free refresh, Treasure Hunter says Legendary items, and the Explosive Laserbeams and Guardian Drone
  descriptions are corrected.
- Guillotine's execute threshold grows by 10% per copy, and the shop stops offering it at 4 copies.
  Bruus has since disabled the item, so it does not appear in play.

#### Fixed
- Recycler rolls its drop chance. Before, every kill dropped a part.
- The Wondrous Wisdomball's "Copy Inventory" roll no longer also copies a legendary item.
- Boss attacks that play a boost sound no longer crash the game when no copy of that sound is free.
  This covers the final boss mine charge, the Striker bombing run and the Twin boss manoeuvres.
- Sounds are handled more safely. A sound still on its cooldown is not kept in the active list, a
  reset only rewinds sounds that played, and background music is stopped and released on every music
  change. Pausing or resuming with no music loaded no longer causes an error.
- The silent sound that looped from startup is removed.
- Enemy formations moving left spawn just past the right edge instead of a full screen width
  further out, so they arrive sooner.
- The Shieldbearer spawn limit counts Shieldbearers instead of Barricades.
- Mini bosses no longer pile up as duplicates in the spawn list.
- Drones orbiting the player keep even spacing when their orbit route runs out.
- A reflective block no longer crashes the game when it reflects a missile that has no flight path.
- The last game-over picture can now be picked.
- Continuing a saved run restores the level clock from the save.
- Tracking laser beams that fire from a moving object follow that object. No boss uses this yet.

## Bruus's changes

### Final boss

- Phase two has a new attack, used every 30s. The boss pulls the players to the centre of the
  screen, surrounds them with a ring of clones, and the clones fire laser beams at the centre one
  after another.
- Phase one plays again, and the boss taunts again.
- Phase two missile attacks are faster. The orbit-missile cooldown goes from 10s to 9s. The
  spread-missile cooldown goes from 20s to 15s, with 3 missiles per board block instead of 2. The two
  attacks swapped priority.

### New and reworked items

- Upgrade: Speed, a Rare Captain item. Drones gain 15% attack speed per stack.
- Upgrade: Precision, a Legendary Captain item. Drones gain a 20% chance per stack to crit for double
  damage, and Placeholder drones twice that.
- Precision Overloader is now Upgrade: Overload and is Captain only. It gives drones 15% damage per
  stack instead of 50% critical damage.
- Module: Focus Fire is now Upgrade: Focus Fire. Drones keep firing on their own, and they all fire
  one shot at the target when your laserbeam hits it. It can now be combined with Module: Accuracy
  and Module: Synergize.
- Electro Shedding is now Electro Shredding. It no longer strips enemy armor. Instead it raises
  Electroshred damage per stack, scaled by how much health you have left.
- Guillotine is disabled and no longer appears in the shop.

### Balance

- Placeholder supports up to 8 drones instead of 4. Drone upgrades are twice as effective on its
  drones, and its description shows damage per second.
- Drone attack speed and station attack speed follow the ship's drone speed bonus. The default drone
  attack speed goes from 0.5 to 0.75.
- The space station's damage uses its own ship's drone damage bonus.
- Wondrous Wisdomball's base chance goes from 20% to 30%, and its bonus per failed roll from 17.5% to
  20%. The chance is no longer reset on every roll.
- Recycler's drop chance goes from 10% to 7.5% per copy.
- Anion Inverter's Electroshred cooldown reduction goes from 60% to 70%.
- Module: Electric Razor's cooldown goes from 1.5s to 0.5s.
- Plasma Bullets burn for 2% damage over 2s, instead of 1.8% over 1.5s.
- Plasma Launcher's damage multiplier goes from 2 to 2.5.
- Adrenaline refreshes both its attack speed and health regeneration when you take damage again. Its
  description says "hitpoints per second".
- Firefighter's attack speed stat goes from 0.28 to 0.22. Its ignite damage goes from 0.011 to 0.013,
  and its ignite duration from 1.65s to 2.35s.
- The Firefighter flamethrower's ignite damage scales with the ship's ignite damage bonus. This fixes
  Scorching Fury.
- The end-of-level time boost adds 4% of elapsed game time instead of 10%.
- Enemy difficulty follows the difficulty coefficient, capped at 7, instead of bosses defeated,
  capped at 5.
- Enemy health growth per level changes. Bosses go from 1.35 to 1.2, and normal enemies from 1.175 to
  1.185.
- Enemy directors earn spawn credits faster, 0.06 per difficulty point instead of 0.05.
- Red Boss health goes from 4500 to 4400. It summons 3 drones at a time instead of 2, up to 7
  instead of 6. Its summon cooldown is 22s plus 2s per living drone, and the faster cooldown below
  25% health is removed. Its shuriken interval goes from 10s to 9s.
- Yellow Boss health goes from 5000 to 4500 and its damage from 9 to 7. It appears from stage 2
  instead of stage 1.
- Mini bosses speed up faster with difficulty: Defender, Laserbeam, Mother Ship and Shuriken.
- The Mother Ship keeps up to 4 drones plus one per difficulty level. Mirage always gets at least one
  extra clone.
- The Shuriken mini boss drops a stationary mine every 2.5 seconds.
- Enemy stats that need whole numbers round the difficulty modifier. This covers the Bulldozer bomb
  ring, Needler and Scourge knockback, the Shieldbearer detection range and the Queen spawn cap.

### Planboard

- A refactors list of design problems to fix, and replies and context on the performance list.

## Dev tooling

These changes do not affect play.

- Planboard. `docs/planboard/` has one list of bugs, features, balance tweaks and ideas per game area,
  and `.claude/scripts/planboard.sh` prints it by type and area.
- CLAUDE.md and the project bindings in `.claude/` describe the build, a code map and the working
  rules for Claude Code.
- Test saves in `.claude/test-saves/` with a script that installs one as `savefile.json`.
- Recording tools. A watcher script logs memory, threads and startup times during a recording run,
  and a script prints startup phase times from `startup_log.txt`.
- The startup log lists how long the controller library takes to start and which controllers it
  found.
- VS Code launch configurations: "Run Game", "Run Game (recording memory)" and "Run Game (Direct3D
  only, recording)". There is a compile task, and Java files are not reformatted on save.
- Two dev test switches, both off: `stressTestSpawns` gives enemy directors 5 times the spawn
  credits, and `disableImageCacheBudget` turns off the picture cache's memory limit.
- Shell scripts use Unix line endings. Local settings, `startup_log.txt` and `savefile.json` are not
  tracked by git.
- README credits Jamepad, SDL2 and the SDL controller list.
