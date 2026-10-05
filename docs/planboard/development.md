# Development
Settings, startup, Discord status, builds, packaging and dev tooling.

## Features
- Log runs to show how much damage each player does, so player power creep is visible
  - Plot it on graphs
- Test run mode: after class selection, open a new screen to customise the run, as a testing environment
  - Options: normal run, difficulty, miniboss, boss
  - Choose which items the player has and which enemies to fight
  - Infinite playtime, with a way to exit

## Ideas
- Simplify ImageDatabase so adding an image takes one edit instead of five (enum, path in ImageLoader, field, load line, getImage case)
  - Put the file path on ImageEnums and keep all images in one EnumMap filled by a loop
  - Replace the ~80 copy-pasted animation loops with a table of folder and frame range, or read whatever frames are in the folder
  - Report a missing image loudly at startup instead of silently drawing the star fallback
  - Agree it with Bruus first, since it changes how every new image is added
