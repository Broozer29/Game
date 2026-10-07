# Menus & UI
Menus, screens, the HUD and health bars.

## Features
- Stop drawing damage numbers on screen; Nelis: nobody uses them
  - Decided by Nelis on 2026-10-07: only the drawing goes. The game keeps recording the damage itself, for the damage overview below
  - In the 2026-10-07 stress run `GameBoard.drawOnScreenText` still caused 66% of all allocated memory after two rounds of fixes (Java's own `drawString` font lookups), so removing them would also cut garbage collection work
  - Check first which other on-screen texts use the same code (minerals, markers, messages)
  - New on 2026-10-07: they are the likely cause of the slow motion in stress runs. After a full garbage collection, 88,000 to 153,000 texts were still alive and drawn every frame (histograms in target/perf, 21:07 to 21:38). The count dropped at the level change and the slowdown with it
  - They fade per drawn frame, not per game tick (GameBoard.java `drawOnScreenText`, 0.0175 per frame for damage numbers), and are only removed when almost transparent. When frames slow down, each text lives longer, so more pile up and frames slow down further
  - Confirmed on 2026-10-07: a stress run with damage numbers switched off had no lag at all for the whole level
  - Question for Bruus (2026-10-07): any objection to removing the on-screen numbers?
- A damage overview at the end of a level and of a run: average damage per second, highest hit and lowest hit
- Give up run button
- Pimp out the loading screen
- Player profile in the shop (show max hitpoints, attack speed, crit chance, health regen etc etc)
