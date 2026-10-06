# Test saves

Ready-made runs for play-testing and for memory and performance measurement. Each file is a normal
`savefile.json` (same format the game writes). All start in the shop after 5 completed stages, player
level 8, Medium difficulty, difficulty coefficient 7, Double Trouble mode (twice as many enemies at
half health). The next level is a normal level and the one after it is a boss level (boss levels come
every 2nd stage from stage 6).

| profile | use |
| --- | --- |
| `captain-lasers` | stress run: lasers and drone attacks (3000 minerals) |
| `firefighter-scorch` | stress run: burns and Scorch (3000 minerals) |
| `captain-items` | play-test of item and shop fixes: `captain-lasers` plus Precision Amplifier 7, Guillotine 2, Wondrous Wisdomball 1 and 99,999 minerals |

## Stress runs and recording

1. Set `DevTestSettings.stressTestSpawns` (5 times the spawn credits) and, if wanted, `playerIsImmune` to `true`. Both must be `false` in every commit.
2. Install a profile (below) and start `bash .claude/scripts/watch-recording.sh` in the background.
3. Start **Run Game (recording memory)** in VS Code. It writes a Flight Recorder file and a GC log to `target/perf/`. The watcher reports the startup phase times, the live heap after a forced full GC every 3 minutes (also written to `target/perf/heap-<time>.txt`) and the saved recording.
4. `bash .claude/scripts/startup-times.sh [N]` prints the startup phases of the last N runs from `startup_log.txt`.

`mvn clean` deletes `target/`, and with it every recording.

## Install and continue

1. `bash .claude/scripts/use-test-save.sh` lists the profiles.
2. `bash .claude/scripts/use-test-save.sh captain-lasers` (or `firefighter-scorch`) backs up the current
   `savefile.json` to `target/perf/savefile-backup-<timestamp>.json` and installs the profile.
3. Start the game and click **CONTINUE RUN** on the main menu. It opens the shop; leave the shop to start the level.

The game overwrites `savefile.json` while you play (shop purchases, level end), so the installed
profile is consumed. Re-run the script to start again. Both classes are already unlocked in `playerprofile.json`.

## captain-lasers (Captain)

Tests laser beam rotation and pile-ups of drone Electro Shred special attacks.

| item | stacks | why |
| --- | --- | --- |
| PiercingMissiles | 3 | lasers pierce; required for bouncing |
| BouncingLasers | 1 | lasers bounce between enemies, so they rotate a lot |
| ExplosiveLaserbeams | 1 | an explosion on every laser hit |
| SideCannons | 1 | two extra lasers per shot |
| Overclock | 4 | +120% attack speed, many more lasers |
| PrecisionAmplifier | 4 | crits (50% chance) |
| CriticalOverloadCapacitor | 2 | crit damage |
| FocusCrystal, PhotonPiercer | 3, 2 | laser damage, so enemies die and are replaced faster |
| StickyDynamite | 2 | extra explosions on hit |
| GuardianDrone | 8 | maximum drones (8) |
| ModuleElectrify | 1 | every drone copies the Electro Shred (the follower-list code in `Drone.java`) |
| ElectroShedding | 1 | Electro Shred permanently removes armor |
| ElectricSupercharger | 2 | bigger Electro Shred area and damage |
| AnionInverter | 1 | -60% Electro Shred cooldown, so shreds pile up (cannot be combined with Electric Destabilizer in the shop) |
| Battery | 3 | more special attack charges |
| Recycler | 5 | recycle parts refill special charges |
| RocketLauncher | 2 | drones fire rockets |
| RegenerativeSteel | 6 | steady health regeneration (heal animations) |
| Adrenaline | 2 | regeneration burst whenever hit |
| EmergencyRepairBot | 3 | emergency heal at 25% health |
| PlatinumSponge | 4 | flat damage reduction (armor) |
| CalmInChaos | 2 | damage bonus while not hit |

## firefighter-scorch (FireFighter)

Tests burn (ignite) stacking, fire effects and drone fireballs.

| item | stacks | why |
| --- | --- | --- |
| ModuleScorch | 1 | drones become fireballs that apply ignite |
| GuardianDrone | 8 | maximum drones |
| BeckoningFlames | 1 | auto missiles at ignited targets |
| RingOfFire | 1 | fire shield fires ignite missiles in all directions |
| FieryImplosion | 1 | explosion at maximum ignite stacks |
| FireWithoutGasIsAss | 1 | more ignite damage |
| ScorchingFury | 4 | ignite damage |
| EscalatingFlames | 4 | +4 maximum ignite stacks |
| StickyOil | 3 | longer ignite |
| InfernalPreIgniter | 3 | flamethrower damage ramps up |
| FlameDetonation | 1 | ignited enemies explode and leave flames |
| EternaFlame | 1 | less fuel use, so the flamethrower runs longer |
| EphemeralBlaze, FuelCannister | 2, 3 | flamethrower damage and fuel |
| CorrosiveOil | 2 | ignite reduces armor |
| CannisterOfGasoline | 3 | enemies explode on death and ignite others |
| Overclock | 3 | attack speed |
| Battery, PrecisionAmplifier | 3, 3 | more shield charges, crits |
| RegenerativeSteel | 6 | steady health regeneration |
| Adrenaline | 2 | regeneration burst whenever hit |
| EmergencyRepairBot | 3 | emergency heal at 25% health |
| PlatinumSponge | 4 | flat damage reduction (armor) |

## Left out on purpose

- LeechingLasers, HighVelocityLasers, Thornweaver, BarbedAegis, BarbedMissiles, ThickHide: switched off in
  `ItemEnums` (enabled = false), so they do nothing.
- PlasmaCoatedBullets and ElectricDestabilizer: Captain-only (PlasmaCoatedBullets also Carrier), not usable by FireFighter.
  Destabilizer is also excluded from the Captain profile because it cannot coexist with AnionInverter.
- EmergencyRepairs: Carrier-only.
- ModuleCommand (12 drones) was not added; drones stay at the normal maximum of 8.
