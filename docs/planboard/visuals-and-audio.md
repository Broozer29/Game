# Visuals & Audio
Sprites, effects, explosions, music and sounds.

## Bugs
- The "local files" music option never plays the player's own files
  - Level songs come from the compiled-in `MusicTrack` list (`MusicTrack.getRandomLevelTrack`); nothing looks in an outside folder
- Spotify option is an unfinished stub
  - `SpotifyMediaPlayer` makes no network calls; it has placeholder client ID, client secret, token and device ID, and its playback position is always 0
  - No menu button selects it (MenuButton.java only switches between iTunes and local files)
  - Level length follows the song: a level ends when the predicted song end is reached (`AudioManager.isLevelMusicFinished`), so any outside music source must report the track position and length

- Starting a sound clip without a media player can crash
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
- A music list viewer that shows every track and where in the game it plays
  - The list itself is written up in `docs/architecture/subjects/audio/music.md`
  - Plan item `game-music-glossary` (2026-10-19)
  - Nelis and Bruus will fill in each song's real title and artist, so every song gets a proper model: file, title, artist and where it plays (level pool, which boss, menu, shop, game over) (Nelis, 2026-10-20)
  - Question for Bruus: should that model become the one place that knows every song, so the level pool, boss themes and menu music are picked from it? Or should it only add names beside the current audio code, linked by file name?
    - Bruus insight:
      - **Mening:** Tracks.txt vervangen is dikke plus, maar werkt dat wel? De reden dat tracks.txt nodig is, was omdat de .jar niet de mogelijkheid had om de inhoud van de audio folder te lezen dus de namen van de tracks had ik maar op deze manier vastgelegd zodat Java ze nog wel kon vinden. Als het mogelijk is om tracks.txt te vervangen met een code implementatie dan graag
    - Answer (Claude, 2026-10-20): it works. A song list in code names every file itself, so the game never has to list the folder inside the jar
- Show the song name at the start of a level
  - Plan item `game-song-name-toast` (2026-10-19)

## Ideas
- Shrink the PNGs without changing how they look, to cut the download size further
  - The 4,536 PNGs take 298 MB. A lossless optimiser such as `oxipng` usually saves 10 to 30% (not measured on these files)
  - More could be saved if some GIF frame folders are stored at a much higher resolution than they are drawn on screen (not checked)
  - `images/background/BlueNebula9 copy.png` is a byte-identical 3.1 MB copy of `BlueNebula9.png`, and only `BlueNebula9.png` is used in the code
