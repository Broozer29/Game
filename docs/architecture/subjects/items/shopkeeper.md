# Shopkeeper

SHOPKEEPER is a page on the main menu that lists every item with its details. It works outside a run,
so it cannot ask an item which classes may get it. Every `ItemEnums` entry therefore carries a class
group and its item types as compiled-in data, and the page reads those. This doc covers that data, the
overlay and the base-value descriptions.

## Item data

Each `ItemEnums` entry holds a rarity, an icon, a display name, an enabled flag, a class group and at
least one item type.

| member | meaning |
| --- | --- |
| `ItemTypeEnums` | The kind of item: Offensive, Defensive, Economy or Utility |
| `ItemClassAvailability` | The class group that can get the item |
| `ItemClassAvailability.getPlayerClasses` | The `PlayerClass` values of a class group |
| `ItemEnums.getItemTypes` | The types of an item, in the order of its entry |
| `ItemEnums.getClassAvailability` | The class group of an item |

An item without a class group or without a type does not compile: the `ItemEnums` constructor takes
`ItemClassAvailability classAvailability, ItemTypeEnums firstType, ItemTypeEnums... otherTypes`.

The class group is a convention, not a guarantee. It is a second copy of the class conditions in each
item's `isAvailable()`, which stays the rule during a run. Nothing keeps the two in step, so a change to
one is made in the other by hand. The class group All covers Captain, FireFighter and Carrier.

## The overlay

`ShopkeeperWindow` is a singleton overlay on the main menu, modelled on `SettingsWindow`. The main menu
sends all input to it while `isOpen()` is true, draws `getComponents()` and then calls `draw`, and closes
it with `closeSilently()` when the window is recreated.

The page lists every item for which `ShopkeeperWindow.isShownInShopkeeper` is true. That method is the
single place that decides which items appear.

| part | behaviour |
| --- | --- |
| Order | Rarity from Common to Relic, then display name A to Z |
| Grid | Icons fill the card width and scroll by whole rows with the cursor (`firstVisibleRow`) |
| Details | Icon, name in its rarity colour, rarity, class group, types, full description and price of the item under the cursor |
| Confirm | Does nothing |
| Back | Closes the page and runs the `onClosed` callback of the screen that opened it |

The SHOPKEEPER row sits above SETTINGS in the main menu card. Its button carries the
`MenuFunctionEnums.Open_Shopkeeper` function, which `MenuButton.activateComponent` sends to
`BoardManager.openShopkeeper()`, and that opens the page through `MainMenuBoard.openShopkeeper()`.

## Base-value descriptions

The page never depends on a run. It reads `ItemDescriptionRetriever.getDescriptionOfItem(ItemEnums, boolean baseValues)`
with `baseValues` true. The one-argument `getDescriptionOfItem(ItemEnums)` keeps the run-based text used by the
shop and the relic cards.

Two items read run state in the run-based text. With `baseValues` true, Placeholder counts no drone
upgrades (`FriendlyStation.getStationDamage(null)`), and Gasoline uses the ignite values a run starts with
(`PlayerStats.baseIgniteDamageMultiplier` and `PlayerStats.baseIgniteDuration`).

<!-- Unbuilt / deferred -->
## Designed but not yet built

- Filters on the class group and the types.
- A SHOPKEEPER entry from the shop.
- Showing only discovered items, through `isShownInShopkeeper`.

## Cross-references

- [Music](../audio/music.md): the music that plays on the main menu behind the page
