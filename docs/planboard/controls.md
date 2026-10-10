# Controls
Keyboard and controller input.

## Bugs
- Nelis' brother reports frequent crashes when the game keeps checking for a controller (2026-10-06); earlier proposed fixes didn't help
  - The cause is still unknown; since 2026-10-10 a 15 ms controller clock checks for controllers through Jamepad
  - Needed from him: the `hs_err_pid*.log` file from the game folder, `java -version` output, OS, controller model and connection (USB, Bluetooth, dongle), whether it crashes with no controller plugged in, when it crashes (startup, menu, mid-game, after unplugging or sleep), how he launches the game, his error_log.txt, and his startup_log.txt (it lists the controllers the game found)
- Auto attack sometimes stops working for a level and works again in the next one (Nelis, 2026-10-10)
  - Captain, at the start of a level, LB did nothing, no Big Iron (Nelis, 2026-10-10)
  - It stopped and came back several times within one run, so the cause is still unknown (a timer kept from an earlier run is ruled out)
  - Switching hold fire off at every screen change stays: it stops a relic being picked by itself

## Features
- The controls picture in the main menu shows the old layout: the d-pad is labelled "Movement" (it only moves the menu cursor after the Jamepad switch), and the labels on Y and B are unreadable (Nelis, 2026-10-10)
  - To do together with a controls menu for rebinding buttons
- A controls menu where players rebind which controller button does which action (fire, special attack, hold fire, pause) (Nelis, 2026-10-10)

## Balance
- Pause has no wait anymore: on the pad and on P it pauses and unpauses at once; the 1 s wait after unpausing (`GameState.isAllowedToPause`) was removed (Nelis, 2026-10-10)
  - Question for Bruus: was the 1 s wait meant to stop players pausing again and again to dodge attacks? `isAllowedToPause` is kept but no longer used, in case it has to come back
