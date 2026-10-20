# Playlist

PLAYLIST is a page on the main menu (it opens from there only, not from the shop or the pause window) that lists every song of the game by where it plays and lets the player listen to one. A song plays whole from the start as a preview. When the page closes, the menu music continues from the moment the preview paused it. This doc covers the page, the preview and how the preview shares the music with the menu.

## The page

`PlaylistWindow` is a singleton overlay on the main menu, built like `ShopkeeperWindow` and modelled on `SettingsWindow`. The main menu sends all input to it while `isOpen()` is true, draws `getComponents()` and then calls `draw`, and closes it with `closeSilently()` when the window is recreated. The row PLAYLIST sits between ARMORY and SETTINGS in the main menu card. Its button carries `MenuFunctionEnums.Open_Playlist`, which `MenuButton.activateComponent` sends to `BoardManager.openPlaylist()`, and that opens the page through `MainMenuBoard.openPlaylist()`.

The page is one `Square_Card` with a tab row in menu letters and a white underline under the selected tab. The song list is on the left and the song that plays on the right, split by a thin line. Hint cards on `Wide_Card` art sit under the card and follow the device used last (`MenuInput.getLastDevice`). The only moving part is the progress bar.

| tab | songs |
| --- | --- |
| LEVELS | The `MusicTrack` values with `MusicPlace.LEVEL` |
| BOSSES | The values with `MusicPlace.BOSS`, then the two with `MusicPlace.FALLBACK`, which show "fallback" as the place |
| FINAL BOSS | The three phases, `MusicPlace.FINAL_BOSS` |
| MENUS | Main menu, shop and game over |

Each row shows the title, the artist or "Artist unknown", and where the song plays: the boss name, the phase, the screen, or "random regular level".

| action | effect |
| --- | --- |
| Previous or next tab, cycle tab | Switches the tab and selects its first song |
| Up, down | Moves the cursor; nothing plays |
| Confirm | Plays the selected song as a preview |
| Back | Closes the page and runs the `onClosed` callback of the main menu, which puts the cursor on PLAYLIST |

The right side shows the song of the last preview, or the selected song before any preview. The progress bar is the game's own (`ImageEnums.ProgressBar` and `ImageEnums.ProgressBarFilling`), with the time played and the length under it. The bar is empty until the song starts. The played time reads "--:--" until the song starts, and the length reads "--:--" while it is unknown. A preview that ends by itself leaves the bar full.

## The preview

The audio side is `AudioManager.startPreview(MusicTrack)` `AudioManager.stopPreviewAndResume()` and `AudioManager.discardPreview()`. Opening the page does not touch the menu music.

- `startPreview` stops and disposes a preview that is still playing and loads the new clip first. If the song fails to load it returns `false` and the menu music is not touched. Only after a successful load does it pause `backGroundMusic` in place, when that clip exists and is playing, and remember that it did. The page shows NOW PLAYING only when it returns `true`.
- The preview is always a fresh `CustomAudioClip` that owns its player, created with loop off. A pooled clip is never used, because a pool holds one clip per track and a paused menu clip would be handed out again.
- `stopPreviewAndResume` disposes the preview and resumes `backGroundMusic` only when `startPreview` paused it. In mute mode the muted menu music is paused and resumed like normal. `backGroundMusic` is never released.
- `discardPreview` disposes a running preview and clears the paused-by-preview flag without resuming. `BoardManager.playMenuMusic` and `playShopMenuMusic` call it before they restart the music themselves, so a screen switch never leaves a stale preview or flag.
- A preview that reaches its end is noticed by `AudioManager.pollPreviewEnded`, which the page calls from its input loop. The clip is disposed then, and the menu music stays paused until the page closes.

Closing the page, and recreating the main menu window, both end in `PlaylistWindow.closeSilently`, which calls `stopPreviewAndResume`.

## Cross-references

- [Music](music.md): which track plays on each screen and level, and the `MusicTrack` model the page lists.
- [Armory](../items/shopkeeper.md): the overlay the page is built like.
