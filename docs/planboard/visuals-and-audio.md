# Visuals & Audio
Sprites, effects, explosions, music and sounds.

## Bugs
- The "local files" music option never plays the player's own files
  - `AudioLoader.getRandomCustomMusicFile` only reads the track list inside the jar (`/audio/music/custom/tracks.txt`); nothing looks in an outside folder, and `isExternalFolder` is always false
- Spotify option is an unfinished stub
  - `SpotifyMediaPlayer` makes no network calls; it has placeholder client ID, client secret, token and device ID, and its playback position is always 0
  - No menu button selects it (MenuButton.java only switches between iTunes and local files)
  - Level length follows the song: a level ends when the predicted song end is reached (`AudioManager.isLevelMusicFinished`), so any outside music source must report the track position and length

- Pausing or resuming can crash when no background music is loaded
  - AudioManager.java:323-324 and 349-350 call `backGroundMusic.pauseClip()` and `resumeClip()` without a null check (also the Spotify branch at 333 and 359); the game state is already Paused before the error
  - `isBackgroundMusicInitializing()` (line 278) has no null check and runs every tick on Special levels, which never start music
  - `CustomAudioClip.startClip()` (line 114) calls `mediaPlayer.play()` without a null check
- A finished-sound callback from a previous play can cut off a restarted sound
  - CustomAudioClip.java:37-45 sets its flags from the JavaFX thread without `volatile`, and `AudioDatabase.resetClips` then stops the clip early

## Features
- Spotify API connection so players can play their own music
  - Researched 2026-10-05. Spotify's Developer Policy (developer.spotify.com/policy, section III) says "Do not create a game, including trivia quizzes." Read literally, a Spotify integration in this game is not allowed. Ask Spotify before building it
  - The same policy forbids synchronising recordings with visual media and overlapping Spotify audio with other audio. Game sounds over the music may count as overlap (uncertain)
  - The game cannot play Spotify audio itself. It can only remote-control the player's own Spotify app, and play, pause and skip only work for Premium users
  - New apps stay in development mode: at most 5 allowlisted users. Opening it to everyone needs a registered company with at least 250,000 monthly active users (since 15 May 2025)
  - If built anyway: sign-in with Authorization Code and PKCE (no client secret in the game), redirect to `http://127.0.0.1:<port>` ("localhost" is not allowed), and poll the currently playing track every few seconds
- Show and control whatever music the player already has playing, without the Spotify API
  - Windows: the system media session (`GlobalSystemMediaTransportControlsSessionManager`) gives title, artist, play state and play/pause/skip for any media app, Spotify included. It is a Windows API, so Java needs a small native bridge or helper program (not verified)
  - macOS: AppleScript `tell application "Spotify"` can read the track, its position and length, and play, pause and skip, the same way `MacOSMediaPlayer` already controls Apple Music. macOS asks the player for permission once (not verified)
  - Track position and length are needed because the level ends when the song ends; whether the Windows session reports them reliably is not verified
- Let players add their own music folder
  - Fixes the "local files" bug above. JavaFX plays WAV, MP3 and AAC (m4a), not OGG
- Store the music as MP3 or AAC instead of WAV
  - All music is WAV: 1.7 GB of the roughly 2 GB jar. MP3 or AAC at normal quality would shrink it to roughly a seventh to a tenth, and JavaFX plays both
