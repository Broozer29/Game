# Music

The game plays one music track at a time. Which track plays depends on the screen or level the
player is on. Every track is an MP3 file under `src/main/resources/audio/music/`, and every track is
one value of the enum `MusicTrack`, which holds its resource path, title, artist, `MusicPlace` and,
for boss songs, the boss and phase. Every track is played through
`AudioManager.playDefaultBackgroundMusicForALevel`. This doc lists where each track plays and which
code decides it.

## How a track is chosen

| situation | code that starts the music | loops |
| --- | --- | --- |
| Main menu | `BoardManager.playMenuMusic`, when the `MAIN_MENU` screen opens | yes |
| Shop between levels | `BoardManager.playShopMenuMusic`, when the `SHOP` screen opens | yes |
| Game over | `GameBoard.playDeathMusic`, when the player has not survived | yes |
| Regular level | `LevelManager.activateMusic`, with the track from `MusicTrack.getRandomLevelTrack` | no |
| Boss level | `LevelManager.activateMusic`, with the track from `MusicTrack.getBossTheme` | yes |
| Final boss phase change | `FinalBoss.changePhaseToNextPhase`, with the track from `MusicTrack.getFinalBossPhase` | yes |
| Special level | `LevelManager.activateMusic` starts no music | not applicable |

Opening the `GAME` screen stops the menu or shop music before a level starts.

## Regular levels

A regular level picks a random track among the `MusicTrack` values whose place is `MusicPlace.LEVEL`.
`MusicTrack.getRandomLevelTrack` never picks one of the last 10 picks again.
A level track is played from a clip created for that one play, and `CustomAudioClip.dispose` frees
its player afterwards. The level ends when its track ends, so the track does not loop.

A level cannot hang on its track. `AudioManager.isLevelMusicFinished` also ends the level when the
track has not reported its end some time after its predicted end, or when the track failed to load
and a fallback level length has passed. `CustomAudioClip.hasFailedToLoad` decides whether a track
failed to load.

This applies when the music source is `MusicMediaPlayer.LocalFiles`. The other music sources play
from the player's own media app.

The level tracks:

| track | track |
| --- | --- |
| 5 seconds | Kiss of the Creature |
| Afterglow | Memory-Uploaded |
| Astral | New Arcades - Solace |
| Cosmos | Nova |
| Depth | Painting the skies |
| Downtown Binary - 8MB | Radium |
| Downtown Binary - Fantasia | Recompile |
| Downtown Binary - Light Cycles | Rose |
| Downtown Binary - Lost | Severed |
| Downtown Binary - Oort | Turismo |
| Eclipse | VIQ - Girl from Nowhere |
| Flux | runner two |
| Forhill-Iris | viq alison |

## Menus and game over

| screen | track |
| --- | --- |
| Main menu | mainmenu |
| Shop | lemmino Firecracker |
| Game over | Vendla - Sonrisa (Royalty Free Music) |

## Boss levels

Each boss has its own theme, mapped in `MusicTrack.getBossTheme`.

| boss | track |
| --- | --- |
| `SpaceStationBoss` | Waveshaper wisdom of rage |
| `RedBoss` | RM Chase |
| `CarrierBoss` | nomad |
| `YellowBoss` | Filth |
| `StrikerBoss` | Arisen |
| `BlueBoss` | Mausoleum mash |
| `TwinBoss` | enraged |
| `FinalBoss` | phase1 |

The final boss switches track at each phase change:

| phase | track |
| --- | --- |
| Phase 1, start of the fight | phase1 |
| Phase 2 | phase2 |
| Phase 3 | phase3 |

## Fallback tracks

`MusicTrack.getRandomFallback` picks a random track among the values whose `isInFallbackPool` is
true. It plays for a boss level whose boss has no theme in `MusicTrack.getBossTheme`.

Every boss that `LevelManager` can pick for a boss level has its own theme, so this case does not
occur in normal play. `FinalBossLaserbeamClone` is in the `Boss`
category without a theme, but its `getBossKillCountRequiredBeforeAllowedToSpawn` requirement keeps
it from being picked.

| track | also plays as |
| --- | --- |
| Waveshaper wisdom of rage | `SpaceStationBoss` theme |
| nomad | `CarrierBoss` theme |
| Blood_On_The_Dancefloor | nothing else |
| Waveshaper - Monster | nothing else |

## Preview from the PLAYLIST page

The PLAYLIST page plays any track as a preview through `AudioManager.startPreview`. The preview is its own clip and never replaces `backGroundMusic`: the menu music is paused in place and resumes where it was when the page closes. The page and the preview are described in [Playlist](playlist.md).

## Files no code plays

These files are in `audio/music/` but no code refers to them:

- keygen
- Meat Grinder

## Cross-references

- `src/main/java/net/riezebos/bruus/tbd/visualsandaudio/data/audio/enums/MusicTrack.java`: every
  track, the boss themes, the fallback tracks and the level pick.
- [Playlist](playlist.md): the page that lists the tracks and previews them.
- `src/main/java/net/riezebos/bruus/tbd/visualsandaudio/data/audio/AudioLoader.java`: builds a
  player from a track's path.
- `src/main/java/net/riezebos/bruus/tbd/visualsandaudio/data/audio/AudioDatabase.java`: keeps one
  pooled clip per track that is not a level track, handed out by `getMusicClip`.
