# Items & Shop
Items, boons, the shop, rerolls and prices.

## Bugs
- Recycler always drops a recycle part instead of rolling its chance
  - SpawnRecyclePartOnDeath.java:50 compares `random.nextInt(0, 1)`, which is always 0, against `quantity * Recycler.spawnChance`
- Missiles put the same effect object on every enemy they hit instead of a copy
  - `GameObject.dealDamageToGameObject` (GameObject.java:422-423) adds `effectsToApply` directly; explosions and special attacks copy first
  - On piercing missiles, several enemies would share one effect's stacks and animations (not confirmed in play)
- Dorment Explosion's copy uses the burning damage as its damage and drops the burn duration and sound
  - `DormentExplosion.copy()`; not confirmed in play

## Features
- Equipment slots: give a player a new active ability that is not decided by their class
  - Teleport/dash ability that you charge up for a while: the longer the charge, the faster the teleport
- Shop freeze: the available items stay frozen in their slot, every other slot is refreshed
- More items for drones
- Electro shock items for the Captain
- Guardian Drones relic (Firefighter): turn the drones into little suns and rework the relic

## Balance
- Calm in Chaos needs a look

## Ideas
- Items for build A that weaken build B, so player choices matter more
- Blue Flame relic: a new item
