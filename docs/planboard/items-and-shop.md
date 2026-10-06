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
- Nepotism never pays out, and four Captain relics can appear in the very first shop
  - Nepotism.java:31, BouncingLasers.java:46, ModuleElectrify.java:48, ModuleFocusFire.java:67 and ModuleAccuracy.java:53 check `getStagesCompleted() == 0`, but `GameState` starts the count at 1
- The first Protoss Arbiter never gets its healing bonus, and extra Arbiters add no ships
  - ProtossArbiterItem.java:13 starts `shouldApply` as false, so the first purchase skips `modifyArbiterHealingMultiplier`; `setArbiterCount(1)` is fixed
- Relic selection can offer duplicates or a wrong card
  - UI/GameBoardCreator.java:113-119 stops retrying at 10 but only falls back at `attempts >= 150`; `getRandomItemByRarity` also rolls disabled relics, and `ShopItem.java:74` falls back to Overclock for every class
- Escalating Flames probably adds its max-stack bonus twice per extra copy
  - `increaseQuantityOfItem` applies the effect, and `PlayerInventory.activateUponPurchaseItemEffects` applies it again for UponAcquiring items
- Explosive Laserbeams' explosion uses the enemy's damage, not the player's
  - ExplosiveLaserbeams.java:42 `target.getDamage() * quantity`; the static `damageModifier` is never read
- Sticky Dynamite's explosion deals about 1.75 flat damage instead of 175%
  - StickyDynamite.java:37 passes `explosionDamage * quantity` as raw damage, without the player's damage
- Electro Shedding shreds armor on every hit, and twice on Electro Shred hits
  - The one-argument `applyEffectToObject` (ElectroShedding.java:26) has no Electro Shred check, and GameObject.java:392-396 calls all three overloads per hit
- Guillotine ignores extra copies
  - Guillotine.java:32 uses `hitpointsThreshold` without `quantity`, although the description says +10% per stack
- Adrenaline's regeneration ends after 2 seconds while its attack-speed bonus keeps refreshing
  - Adrenaline.java:31 only refreshes the attack-speed effect; `PassiveHealthRegeneration` expires 2 seconds after it was created
- Bounty Hunter's bonus and the mineral penalties skip level-1 enemies
  - Enemy.java:136-146 puts them inside `if (level > 1)`
- Smaller item and shop issues (from the item audit, not each checked)
  - Treasure Hunter's text promises more relics, but ItemRarityEnums.java:36-50 only shifts Common into Rare and Legendary
  - Reroll text says 25% (ShopBoardCreator.java:242), the code uses 15% (ShopManager.java:97), and the cost is computed before the end-of-level increase
  - VIP Ticket's text says 1 free refresh, the code gives one per stack; Contract's text says "enemies" even when it counts mini bosses
  - `ItemEnums.isRelicAvailable` counts disabled relics, so Wisdom Ball's "Add a Relic" can fall back to Overclock
  - Bonus Kaart adds the item twice (ShopItem.java:105), which can push capped items past their limit
  - FragmentationSacs and MutaliskHealingBonus pass the wrong `ItemEnums` (both disabled)
  - Anion Inverter and Electric Destabilizer, and Inverse Retrieval and Arbiter Damage, only exclude each other one way

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
