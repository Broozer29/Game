# Controls
Keyboard and controller input.

## Bugs
- Investigate controller disconnect issues
  - Controllers are detected only once, at startup (`ControllerManager.initControllers()`, called from Game.java:43); nothing ever checks again, and a reconnected controller is not picked up until a restart
  - Button numbers (`Button._0`, `_4`, `_11`, …) differ per OS and driver (DirectInput, Linux evdev, macOS IOKit), so on another OS buttons can seem dead while the controller is still connected
- Nelis' brother reports frequent crashes when the game keeps checking for a controller (2026-10-06); earlier proposed fixes didn't help
  - The current code doesn't re-check for controllers, so the cause is still unknown
  - Needed from him: the `hs_err_pid*.log` file from the game folder, `java -version` output, OS, controller model and connection (USB, Bluetooth, dongle), whether it crashes with no controller plugged in, when it crashes (startup, menu, mid-game, after unplugging or sleep), how he launches the game, and his error_log.txt
- Selecting a relic with the stick feels laggy, and the cursor sometimes keeps moving left and right on its own (Nelis, 2026-10-08)
  - Moving on its own: when a level starts the stick is switched to sensitive mode for flying, so 10% of a push counts as left or right (BoardManager.java:100, `setControllerSensitive(true)`; ControllerInputReader.java `adjustSensitivity`). The relic selection appears on the game board itself and keeps that 10% threshold, while menus use 50%. A stick that rests slightly off centre then reads as held
  - Laggy: the input cooldown is 200 (`DataClass.CONTROLLER_INPUT_COOLDOWN`), but GameBoard.java:1095 adds 5 per game tick of 15 ms, so one step takes about 600 ms, and longer when the game runs slow. The comment says a slight extra delay was wanted
  - The same cooldown covers the level score card and the game-over screen
  - Possible fix: use the 50% menu threshold while the relic selection is open, and count the cooldown in real time
  - Question for Bruus: was the delay meant to be about 200 ms?
