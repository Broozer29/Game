# Where the in-game names live

Java paths are under `src/main/java/net/riezebos/bruus/tbd/`.

## Areas, for splitting the changed files over the agents

| area | folders |
| --- | --- |
| items | `game/items/**`, `guiboards/guicomponents/ShopItem.java`, `guiboards/boardcreators/ShopBoardCreator.java` |
| enemies and bosses | `game/gameobjects/enemies/**`, `game/level/**` |
| player, classes and drones | `game/gameobjects/player/**`, `game/gameobjects/friendlies/**`, `game/gameobjects/missiles/**` |
| controls, menus and settings | `controllerInput/**`, `guiboards/**` (other files), `game/settings/**`, `game/gamestate/**`, `game/playerprofile/**` |
| performance, pictures and sound | `visualsandaudio/**`, `game/util/**`, `game/movement/**`, `game/gameobjects/GameObject.java`, `game/gameobjects/neutral/**`, `Game.java` |
| assets | `src/main/resources/**`, listed by name, not diffed |
| developers | `DevTestSettings.java`, `pom.xml`, `.claude/**`, `.vscode/**`, `docs/**`, `CLAUDE.md`, `README.md`, dot files |

## Where names and values live

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
| dev test switches | `DevTestSettings.java`: every `public static boolean` must be `false` in a release |
| sound and music files | `src/main/resources/audio/**` |
