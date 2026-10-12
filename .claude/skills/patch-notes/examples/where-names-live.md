# Where the in-game names live

Paths are under `src/main/java/net/riezebos/bruus/tbd/`.

| what | where |
| --- | --- |
| item display name, rarity, enabled or disabled | `game/items/ItemEnums.java` (constructor: rarity, image, display name, enabled) |
| item description text with its numbers | `game/items/ItemDescriptionRetriever.java` |
| item price per rarity | `game/items/enums/ItemRarityEnums.java` |
| item limited to a class | the item's `isAvailable()` comparing `PlayerClass`, in `game/items/items/**` |
| class names | `game/gameobjects/player/PlayerClass.java` |
| class base stats | `game/gameobjects/player/PlayerStats.java` |
| enemy and boss base stats | `game/gameobjects/enemies/enums/EnemyEnums.java` (constructor: hitpoints, damage, ..., minimum stage) |
| music tracks | `visualsandaudio/data/audio/enums/AudioEnums.java` |
| dev test switches | `DevTestSettings.java` |
