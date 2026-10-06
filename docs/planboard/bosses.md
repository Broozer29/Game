# Bosses
Bosses and the final boss: attacks, phases, difficulty and rewards.

## Bugs
- The white battlecruiser boss sometimes crashes the game
- The white battlecruiser crash is probably the reflective-block crash fixed in commit a7ccbe1b
  - error_log.txt (2026-10-05) shows `ReflectiveBlocks.reflectMissile` reading the path of a missile that had none; the Yellow Boss and the final boss both use reflective blocks. Re-test to confirm
  - The same unguarded `getCurrentPath().getWaypoints()` read remains elsewhere: Enemy.java:254, GameObject.java:617 and 634, DestinationPathFinder.java:113, FloatingPathFinder.java:125, SpaceStationSpinningAttack.java:102, and the `allowedToFire` checks in Scout, Seeker, Queen and others
  - Fix: a null-safe helper such as `hasWaypoints()`
- Reflective blocks are only cleaned up off screen by a check that can never be true
  - ReflectiveBlocks.java:59 tests `this.ownerOrCreator.getOwnerOrCreator() instanceof YellowBoss`; the boss has no owner, so it was probably meant to be `ownerOrCreator instanceof YellowBoss`. Lines 37 and 59 also crash if a block has no owner
- A reflected Mutalisk missile would crash or heal the boss
  - Reflected missiles get the block's owner (ReflectiveBlocks.java:137); MutaliskMissile.java:56 and 140 cast the owner to `SpaceShip`, and line 88 heals the owner (not confirmed in play)
- Twin boss attack timers carry over into the next run
  - The twin boss behaviour classes keep static timers such as `TwinBossLeftRightManouvre.lastAttackTime`; `resetBehaviour()` only runs in the `TwinBossManager` constructor, and `GameBoard.resetGame` doesn't reset the twin boss manager
  - In a new run the twin boss can't attack until the previous run's game time is reached again
  - Found on 2026-10-06 while preparing a fix: the problem is bigger than one timer. 11 timers in 5 classes keep the previous run's game time:
    - `TwinBossManager.finishedAttackTime` (and `lastUsedBossActionable`)
    - `lastAttackTime` in TwinBossLaserbeamCentreManouvre, TwinBossLeftRightManouvre, TwinBossProjectileBombManouvre and TwinBossQuickMissileAttack; each is set once when the class loads, from the game time at that moment
    - `gameSecondsLaserbeamStartedFiring` and `lastSecondsMissilesFired` (TwinBossLaserbeamCentreManouvre)
    - `lastBombDroppedTime` and `timeStartedCharging` (TwinBossLeftRightManouvre and TwinBossProjectileBombManouvre)
    - `lastMissileFiredTime` (TwinBossQuickMissileAttack)
  - Proposed fix: every attack's `resetBehaviour()` sets all its own timers back (the `lastAttackTime` ones to the current game time with the same offset as now, the others to 0), and the manager calls them, plus its own reset, when the first twin of a boss level is added. About 15 lines in 5 files
  - Question for Bruus: is this the intended way to reset the twin boss, or would you rather turn the static timers into normal fields per boss instance? Nelis wants your view before we build it
- `TwinBossManager.resetTwinBossManager` adds its four behaviours again every level without clearing the list
  - TwinBossManager.java:60-80; the list keeps growing and is filtered every tick
- Yellow Boss heal orbs heal any target they hit, including the player (YellowBossOrb.java:127; check whether intended)
