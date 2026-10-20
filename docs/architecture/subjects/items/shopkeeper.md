# Armory

ARMORY is a page on the main menu (it opens from there only, not from the shop) that lists every enabled item. An item shows its details once the player has bought it in the shop or picked it as a relic, and shows locked before that. It works outside a run,
so it cannot ask an item which classes may get it. Every `ItemEnums` entry therefore carries a class
group and its item types as compiled-in data, and the page reads those. This doc covers that data, the
overlay, the discovery of items, its filters and the base-value descriptions.
The code keeps the older name: the classes and functions are `ShopkeeperWindow` and `Open_Shopkeeper`, and this file is still called shopkeeper.md.
Item and class descriptions in the shop, the relic choice and the class screen are drawn with antialiasing on, through `DescriptionTextQuality`; its constant `CRISP_DESCRIPTION_TEXT` switches that off for all of them, the ARMORY page included.

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
single place that decides which items appear. `ShopkeeperWindow.isDiscovered` decides how an item is drawn.

| part | behaviour |
| --- | --- |
| Order | Rarity from Common to Relic, then display name A to Z |
| Layout | A bar under the top of the card holds three wide cards in the style of the shop's MAIN MENU button (`Wide_Card` with menu letters at the shop's scale, centred on the card): the item counter on the left ("76 ITEMS", or "12 OF 76" with the real total while filtering), "F FILTERS" or "X FILTERS" and "C CLEAR" or "Y CLEAR" on the right. The letters follow the device used last (`MenuInput.getLastDevice`), with CLOSE instead of FILTERS while the pane is open, and are built again only when their text changes. The cards and letters are returned by `getComponents()` with the page card. The active filters are chips drawn between the counter card and the filters card, in the order class, type, rarity; chips that do not fit are cut. The card is drawn with a padding of 88 at the sides and top and 96 at the bottom so the boxes and the bar stay inside the frame of the card image. Under the bar, a grid box on the left and a details box on the right. The grid box is drawn with `ImageEnums.Card_X5` (the wide card with waves) and the details box, with the filter pane in its place, with `ImageEnums.Card_X1` (the tall card with the light streak). Both are `ResizableCard` nine-slice images, so the border keeps its thin source thickness at any box size. The grid card uses the default constructor, and the details card passes a reference width that gives it the same scale as the grid card, so the two borders are equally thin and the boxes do not look like copies of each other |
| Grid | Six columns of icons with a gap and an inner padding wider than the frost reaches, with no rarity bar under them; it scrolls by whole rows with the cursor (`firstVisibleRow`). The selected icon gets the frost of a frozen shop item (`ShopFreezeEffect.drawFrozenFrame`), drawn in the rarity colour of the selected item instead of ice blue, and clipped to the inner edge of the grid card's border (`CARD_BORDER_SOURCE` scaled like the card) so it never crosses it. An item that is not discovered shows `ImageEnums.LockedIcon` |
| Details | In the details box: a header with a large icon, the name in its rarity colour and a filled rarity badge, then the rows CLASS and TYPE (small chips) and PRICE (the price next to the shop's mineral icon, `ImageEnums.TopazGem7`), then the full description in an inset box (an opaque dark-teal box with a border, drawn on top of the details card) that wraps inside it and shrinks its font until it fits. An item that is not discovered shows only the lock icon, the name "???" and the rarity badge |
| Confirm | Does nothing |
| Back | Closes the page and runs the `onClosed` callback of the screen that opened it |

The ARMORY row sits above PLAYLIST and SETTINGS in the main menu card. Its button carries the
`MenuFunctionEnums.Open_Shopkeeper` function, which `MenuButton.activateComponent` sends to
`BoardManager.openShopkeeper()`, and that opens the page through `MainMenuBoard.openShopkeeper()`.

## Discovery

An item is discovered once the player has bought it in the shop or picked it as a relic. Every other item shows locked.

| part | behaviour |
| --- | --- |
| Storage | `PlayerProfile.discoveredItems` holds the `ItemEnums` names, in `playerprofile.json`. A profile file without the field loads with an empty set. A name that is not an `ItemEnums` value matches nothing |
| Recording | `PlayerInventory.discoverItem` adds the name, writes the profile at once and returns true only when the name was new. With no loaded profile (`PlayerProfileManager.getLoadedProfile` returns null) it does nothing and returns false |
| Callers | Only `ShopItem.purchaseItemInShop` calls it, once per purchase or pick and after `addItem`. Continue Run, test saves, the `infiniteMoney` grant, the Bargain Bucket items and contract rewards record nothing, so an item got that way stays locked until it is bought or picked |
| Dev switch | `DevTestSettings.showFullArmory` makes `isDiscovered` true for every item. It changes what is shown, not what is recorded |

Discovery is silent during play: no popup and no mark on shop or relic cards tell the player an item was recorded, so ARMORY stays out of the way of the game. The caller `ShopItem.purchaseItemInShop` ignores the return value. The quest mark asset (`images/Icons/QuestMark.png`, `ImageEnums.QuestMarkIcon`) is kept unused for a later feature; nothing draws it now.

## Filters

X on a controller or F on the keyboard (`MenuAction.OPEN_FILTERS`) opens a filter pane over the item
details. It has three columns in this order: class (Captain, Firefighter, Carrier), type (Offensive, Defensive, Economy, Utility) and rarity (Common, Rare, Legendary, Relic), then a Clear all filters row. Left and right move between the columns (keeping the row, clamped to the length of the column), up and down move within a column, and down from the last toggle of a column goes to the Clear all filters row; up from that row returns to the last toggle of the class column. Nothing wraps. CONFIRM switches a toggle. X, F, B or Escape close the pane; B or Escape close the page
only when the pane is closed. Y or C (`MenuAction.CLEAR_FILTERS`) clears every filter.

Toggles in one group add up, and the groups narrow each other (`ShopkeeperWindow.matchesFilters`). Locked items match on their real class, type and rarity. A class
toggle matches every item whose class group contains that class, so Captain also shows the All,
CaptainFireFighter and CaptainCarrier items. Mutalisk-only items match no class toggle. The counter in the bar above the boxes shows the number of items that pass, with the total (M is every item the page lists) while filters are active.
When nothing matches, the grid shows "No items found".

The filters live in one `ShopkeeperFilterState` held by `ShopkeeperWindow`. They are stored in `settings.json` as `ShopkeeperFilters`: three lists of enum names for class, rarity and type. `ShopkeeperWindow.open` loads them from the saved settings. Every toggle change and every clear calls `SettingsManager.saveShopkeeperFilters`, which writes the filters at once and never writes other unsaved settings. A name that is no longer a valid enum value is dropped by `ShopkeeperFilters.repair` when the file is read, and the other settings stay as they are. The filters are not part of `Settings.hasSameValuesAs`, so changing them never makes the settings page ask to save.

## Base-value descriptions

The page never depends on a run. It reads `ItemDescriptionRetriever.getDescriptionOfItem(ItemEnums, boolean baseValues)`
with `baseValues` true. The one-argument `getDescriptionOfItem(ItemEnums)` keeps the run-based text used by the
shop and the relic cards.

Two items read run state in the run-based text. With `baseValues` true, Placeholder counts no drone
upgrades (`FriendlyStation.getStationDamage(null)`), and Gasoline uses the ignite values a run starts with
(`PlayerStats.baseIgniteDamageMultiplier` and `PlayerStats.baseIgniteDuration`).

## Cross-references

- [Music](../audio/music.md): the music that plays on the main menu behind the page
