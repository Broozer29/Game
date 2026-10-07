# Menus & UI
Menus, screens, the HUD and health bars.

## Features
- Remove the on-screen damage numbers entirely; Nelis: nobody uses them
  - In the 2026-10-07 stress run `GameBoard.drawOnScreenText` still caused 66% of all allocated memory after two rounds of fixes (Java's own `drawString` font lookups), so removing them would also cut garbage collection work
  - Check first which other on-screen texts use the same code (minerals, markers, messages)
  - Question for Bruus (2026-10-07): agree to remove them, or keep them behind a setting?
- Give up run button
- Pimp out the loading screen
- Player profile in the shop (show max hitpoints, attack speed, crit chance, health regen etc etc)
