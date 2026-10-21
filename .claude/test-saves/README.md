# Test saves

Ready-made runs for play-testing and for memory and performance measurement. Each file is a normal
`savefile.json` (same format the game writes). All start in the shop after 5 completed stages, player
level 8, Medium difficulty (except `captain-hard` and `firefighter-easy`, whose names say their difficulty), difficulty coefficient 7, Double Trouble mode (twice as many enemies at
half health). The next level is a normal level and the one after it is a boss level (boss levels come
every 2nd stage from stage 6).

| profile | use |
| --- | --- |
| `captain-lasers` | stress run: lasers and drone attacks (3000 minerals) |
| `firefighter-scorch` | stress run: burns and Scorch (3000 minerals) |
| `firefighter-distributed-heat` | memory recording with the heaviest Firefighter effect: `firefighter-scorch` plus Distributed Heat 1 and 99,999 minerals, so the build deals enough damage at difficulty 7 |
| `captain-items` | play-test of item and shop fixes: `captain-lasers` plus Precision Amplifier 7, Guillotine 2, Wondrous Wisdomball 1 and 99,999 minerals |
| `captain-focusfire` | Focus Fire load test: `captain-items` plus Upgrade: Focus Fire (all 8 drones fire one shot whenever a laser hits), which fills the screen with bullets |
| `captain-hard` | measurement against the Royal Guard: `captain-items` with Hard difficulty (the next normal levels are Royal Guard with 2 mini bosses) and without Guillotine, which can crash the game at a level end |
| `firefighter-easy` | measurement against the Pirates: `firefighter-distributed-heat` with Easy difficulty (Pirates, no mini bosses) |
| `carrier-medium` | measurement of the Carrier against the Zerg (Medium): hangar bay ships (Scout 4, Arbiter 2, Shuttle 3, Corsair 3, Hangar Bay Upgrade 2), Rally the Fleet 2, Arbiter Multi Targeting, Aim Assist, Construction Kit, Emergency Repairs 2, Plasma Bullets 2, plus Overclock 3, Advanced Optics 3, Upgrade: Overload 2, Sticky Dynamite 2, Battery 3 and the survival items of `captain-items`; 99,999 minerals |
| `captain-limit` | limit test of performance and memory: the Captain build the bot grew from `captain-hard` over 12 levels with buy-all (39 kinds of items, 399 stacks, Focus Fire, Plasma Launcher 12, Guardian Drone 8, Sticky Dynamite 27), Hard, stage 17, 7 bosses defeated, 99,999 minerals. Nelis: if this runs smoothly, anything a normal player can reach runs smoothly |

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
profile is consumed. Re-run the script to start again. Captain, Firefighter and Carrier are already unlocked in `playerprofile.json`.
Test saves do not change the list of seen items in `playerprofile.json`, so ARMORY keeps showing the items you have not bought or picked as locked.

## Use in a bot run

Order: make or pick the profile here first, then name it in a scenario's `save` key, then run the scenario. The run script installs the profile, the bot continues that run (bot.log: `continuing the saved run`), and the script puts your own save back afterwards. A new profile gets a row in the table above before any scenario uses it.

## captain-lasers (Captain)

Tests laser beam rotation and pile-ups of drone Electro Shred special attacks.

| item | stacks | why |
| --- | --- | --- |
| PiercingMissiles | 3 | lasers pierce; required for bouncing |
| BouncingLasers | 1 | lasers bounce between enemies, so they rotate a lot |
| ExplosiveLaserbeams | 1 | an explosion on every laser hit |
| SideCannons | 1 | two extra lasers per shot |
| Overclock | 4 | +120% attack speed, many more lasers |
| AdvancedOptics | 4 | crits (50% chance) |
| UpgradeOverload | 2 | crit damage |
| FocusCrystal, PhotonPiercer | 3, 2 | laser damage, so enemies die and are replaced faster |
| StickyDynamite | 2 | extra explosions on hit |
| GuardianDrone | 8 | maximum drones (8) |
| ModuleElectrify | 1 | every drone copies the Electro Shred (the follower-list code in `Drone.java`) |
| ElectroShredding | 1 | Electro Shred permanently removes armor |
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
| Battery, AdvancedOptics | 3, 3 | more shield charges, crits |
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
