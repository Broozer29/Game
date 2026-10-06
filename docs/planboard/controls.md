# Controls
Keyboard and controller input.

## Bugs
- Investigate controller disconnect issues
  - Controllers are detected only once, at startup (`ControllerManager.initControllers()`, called from Game.java:43); nothing ever checks again, and a reconnected controller is not picked up until a restart
  - pom.xml mixes JInput versions: `jinput` 2.0.10 with `jinput-platform` 2.0.5. The version of the native libraries in `src/main/resources/libraries` is unknown. Mismatched Java and native versions behave differently per OS, so line them all up on 2.0.10
  - Button numbers (`Button._0`, `_4`, `_11`, …) differ per OS and driver (DirectInput, Linux evdev, macOS IOKit), so on another OS buttons can seem dead while the controller is still connected
- Nelis' brother reports frequent crashes when the game keeps checking for a controller (2026-10-06); earlier proposed fixes didn't help
  - The current code doesn't re-check for controllers, so the cause is still unknown
  - Needed from him: the `hs_err_pid*.log` file from the game folder, `java -version` output, OS, controller model and connection (USB, Bluetooth, dongle), whether it crashes with no controller plugged in, when it crashes (startup, menu, mid-game, after unplugging or sleep), how he launches the game, and his error_log.txt
