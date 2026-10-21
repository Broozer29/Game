# Items & Shop
Items, boons, the shop, rerolls and prices.

## Bugs
- Missiles put the same effect object on every enemy they hit instead of a copy
  - `GameObject.dealDamageToGameObject` (GameObject.java:422-423) adds `effectsToApply` directly; explosions and special attacks copy first
  - On piercing missiles, several enemies would share one effect's stacks and animations (not confirmed in play)
- Dorment Explosion's copy uses the burning damage as its damage and drops the burn duration and sound
  - `DormentExplosion.copy()`; not confirmed in play
- Nepotism never pays out, and four Captain relics can appear in the very first shop
  - Nepotism.java:31, BouncingLasers.java:46, ModuleElectrify.java:48, UpgradeFocusFire.java:61 and ModuleAccuracy.java:53 check `getStagesCompleted() == 0`, but `GameState` starts the count at 1
  - Question for Bruus (2026-10-07): the count starts at 1, so these checks are never true. Should they be `<= 1` (Nepotism pays out in the first shop, the four relics leave the first shop), or is today's behaviour intended?
- The first Protoss Arbiter never gets its healing bonus, and extra Arbiters add no ships
  - ProtossArbiterItem.java:13 starts `shouldApply` as false, so the first purchase skips `modifyArbiterHealingMultiplier`; `setArbiterCount(1)` is fixed
- Relic selection can offer duplicates or a wrong card
  - UI/GameBoardCreator.java:114-123 stops retrying at 10 but only falls back at `attempts >= 150`; `getRandomItemByRarity` also rolls disabled relics, and `ShopRoller.getRandomAvailableItemByRarity` falls back to Overclock for every class
- Escalating Flames probably adds its max-stack bonus twice per extra copy
  - `increaseQuantityOfItem` applies the effect, and `PlayerInventory.activateUponPurchaseItemEffects` applies it again for UponAcquiring items
- Explosive Laserbeams' explosion uses the enemy's damage, not the player's
  - ExplosiveLaserbeams.java:42 `target.getDamage() * quantity`; the static `damageModifier` is never read
- Sticky Dynamite's explosion deals about 1.75 flat damage instead of 175%
  - StickyDynamite.java:37 passes `explosionDamage * quantity` as raw damage, without the player's damage
- Adrenaline's regeneration ends after 2 seconds while its attack-speed bonus keeps refreshing
  - Adrenaline.java:31 only refreshes the attack-speed effect; `PassiveHealthRegeneration` expires 2 seconds after it was created
- Bounty Hunter's bonus and the mineral penalties skip level-1 enemies
  - Enemy.java:136-146 puts them inside `if (level > 1)`
- Smaller item and shop issues (from the item audit, not each checked)
  - Contract's text says "enemies" even when it counts mini bosses
  - `ItemEnums.isRelicAvailable` counts disabled relics, so Wisdom Ball's "Add a Relic" can fall back to Overclock
  - Bonus Kaart adds the item twice (`ShopItem.purchaseItemInShop`), which can push capped items past their limit
  - FragmentationSacs and MutaliskHealingBonus pass the wrong `ItemEnums` (both disabled)
  - Anion Inverter and Electric Destabilizer, and Inverse Retrieval and Arbiter Damage, only exclude each other one way
- Pre-release check (2026-10-14): drone upgrades, Placeholder, Firefighter, shield items and item texts are wrong
  - Upgrade: Speed makes drones fire slower; the space station ignores drone speed entirely
  - Upgrade: Precision never runs; Upgrade: Overload adds 0.15 flat damage instead of +15%
  - Electro Shredding does not boost the drones' Electroshred
  - Placeholder: upgrades should count double (1 + 2 × bonus) for damage and fire rate; its text says 10.0 DPS
  - Firefighter's class settings reset the new 2.35 ignite duration to 1.75
  - Barrier Backfire gives 30% on the first copy instead of 25%; "(+400%)" leaves the text
  - Blizzard Barrier's 1% check must use max shield × the overload modifier, in both places
  - Shop freeze frosts and thaws locked and bought slots
  - Texts: Recycler shows 8% for 7.5%, Bombastic Barrier reads Electric Destabilizer's stun time, Barrier Booster says "Inceases"
  - Bruus answered every question (2026-10-14); Plan unit `game-release-bug-sweep`
- Barrier Booster probably does not raise the real shield cap
  - BarrierBooster.java:19 raises the ship's `maxShieldPoints`, but shield regen (SpaceShip.java:432-434) and `changeShieldHitpoints` (:390) cap at PlayerStats' `maxShieldHitPoints`; not confirmed in play
- Barrier Backfire's thorns ratio still grows by 400% per copy
  - ReflectiveShielding.java:27 and :43 add `quantity * buffAmount` to the thorns ratio; Bruus said the 400% should not grow per copy. Question for Bruus: is the thorns ratio a separate effect that may grow?
- Continue Run cannot read the shop from the save file, so a new shop is rolled
  - Seen on 2026-10-15 on the `boss-preload` branch after `Nelis` was merged in. Jackson throws `InvalidDefinitionException`: `ShopSlot` has no constructor it can use
  - Related crash, seen 2026-10-15: Continue Run from a save that stopped mid-level (`Zoning_In`) goes straight into the level, so no shop is made; picking a relic after the level then crashes because `ShopContents` is null (ShopItem.java:72)

## Features
- Equipment slots: give a player a new active ability that is not decided by their class
  - Teleport/dash ability that you charge up for a while: the longer the charge, the faster the teleport
- More items for drones
- Electro shock items for the Captain
- Guardian Drones relic (Firefighter): turn the drones into little suns and rework the relic
- An item glossary that lists every item with its icon and description
  - Plan theme `game-shopkeeper` (2026-10-19), units `game-shopkeeper-screen`, `game-shopkeeper-from-shop`, `game-shopkeeper-filters`
- Store each item's stack limit and "cannot be combined with" rules as data on the item, instead of as code inside its availability check (Nelis, 2026-10-19)
  - Today these rules exist only as code in each item's `isAvailable()`, which reads the current run's inventory. Example: AnionInverter.java:44 says "not available if you already own Anion Inverter or Electric Destabilizer"
  - Why: the new ARMORY screen lists every item outside a run, so it cannot ask that code. Its first version leaves stack limits and exclusions out for this reason
  - Proposal: give each `ItemEnums` entry a stack limit and a list of items it excludes, and let the availability checks read those values. The shop and the ARMORY then use one source, and a new item sets its rules in one place
  - Same pattern for classes (Nelis, 2026-10-19): ARMORY adds a class group to every `ItemEnums` line (which classes may get the item), next to the class check in each `isAvailable()`. Letting `isAvailable()` read that group would remove the second copy
  - It would also fix the one-way exclusions found in the item audit (Anion Inverter and Electric Destabilizer, Inverse Retrieval and Arbiter Damage), because one list can be checked both ways
  - Question for Bruus: do you agree with moving these rules into the item data? Is there a rule that cannot be written as "at most N copies" or "not together with item X"?
    - Bruus insight:
      - **Mijn voorstel:** Ja, zou eenvoudig moeten kunnen door een private static List<ItemEnums> incompatibleItems op de specifieke Item te zetten, een getter toevoegen in de Item interface. Dan kan elke item een lege lijst terugsturen bij geen incompatible items, of een gevulde lijst van de ItemEnums waarmee hij wel incompatible is. Dan moet deze lijst gepopuleerd worden maar dat kan ook statically gedaan worden, dan is hij compatible met de shop en het menu

## Balance
- Calm in Chaos needs a look

## Ideas
- A set-collection item, like Exodia: collect all 5 pieces of a set in a run to get a mega item (Bruus, 2026-10-20)
  - The quest exclamation mark from the ARMORY work (`images/Icons/QuestMark.png`) could show which pieces you still need; it is kept unused for this
  - Plan item `game-item-collection-set` (2026-10-20)
- Items for build A that weaken build B, so player choices matter more
- Blue Flame relic: a new item
