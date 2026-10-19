# Shopkeeper

SHOPKEEPER is a page on the main menu and in the shop that lists every item with its details. It works outside a run,
so it cannot ask an item which classes may get it. Every `ItemEnums` entry therefore carries a class
group and its item types as compiled-in data, and the page reads those. This doc covers that data, the
overlay, its filters and the base-value descriptions.

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

`ShopkeeperWindow` is a singleton overlay on the main menu and the shop, modelled on `SettingsWindow`. The main menu
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

The shop hosts the same overlay. Its inventory card has a SHOPKEEPER button right of INVENTORY in the
card's bottom row, drawn only while the inventory card is open. `BoardManager.openShopkeeper()` sends it to
`ShopBoard.openShopkeeper()` when the shop is the current board, and `ShopBoard.executeMenuInput` gives all
input to the overlay while it is open. The shop draws the overlay after its item description and
animations. BACK returns to the open inventory card with the cursor on SHOPKEEPER, and the shop's slots are
unchanged.

## Filters

X on a controller or F on the keyboard (`MenuAction.OPEN_FILTERS`) opens a filter pane over the item
details. It has three groups: class (Captain, Firefighter, Carrier), rarity (Common, Rare, Legendary,
Relic) and type (Offensive, Defensive, Economy, Utility), then a Clear all filters row. Up and down move
through the toggles and CONFIRM switches one. X, F, B or Escape close the pane; B or Escape close the page
only when the pane is closed. Y or C (`MenuAction.CLEAR_FILTERS`) clears every filter.

Toggles in one group add up, and the groups narrow each other (`ShopkeeperWindow.matchesFilters`). A class
toggle matches every item whose class group contains that class, so Captain also shows the All,
CaptainFireFighter and CaptainCarrier items. Mutalisk-only items match no class toggle. Each toggle shows
how many items would show if only it changed, and the bar above the grid lists the active filters and
"N of M items" (M is every item the page lists). When nothing matches, the grid shows "No items found".

The filters live in one `ShopkeeperFilterState` held by `ShopkeeperWindow`. They are stored in `settings.json` as `ShopkeeperFilters`: three lists of enum names for class, rarity and type. `ShopkeeperWindow.open` loads them from the saved settings. Every toggle change and every clear calls `SettingsManager.saveShopkeeperFilters`, which writes the filters at once and never writes other unsaved settings. A name that is no longer a valid enum value is dropped by `ShopkeeperFilters.repair` when the file is read, and the other settings stay as they are. The filters are not part of `Settings.hasSameValuesAs`, so changing them never makes the settings page ask to save.

## Base-value descriptions

The page never depends on a run. It reads `ItemDescriptionRetriever.getDescriptionOfItem(ItemEnums, boolean baseValues)`
with `baseValues` true. The one-argument `getDescriptionOfItem(ItemEnums)` keeps the run-based text used by the
shop and the relic cards.

Two items read run state in the run-based text. With `baseValues` true, Placeholder counts no drone
upgrades (`FriendlyStation.getStationDamage(null)`), and Gasoline uses the ignite values a run starts with
(`PlayerStats.baseIgniteDamageMultiplier` and `PlayerStats.baseIgniteDuration`).

<!-- Unbuilt / deferred -->
## Designed but not yet built

- Showing only discovered items, through `isShownInShopkeeper`.

## Cross-references

- [Music](../audio/music.md): the music that plays on the main menu behind the page
