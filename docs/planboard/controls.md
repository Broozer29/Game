# Controls
Keyboard and controller input.

## Bugs
- Investigate controller disconnect issues
  - Controllers are detected only once, at startup (`ControllerManager.initControllers()`, called from Game.java:43); nothing ever checks again, and a reconnected controller is not picked up until a restart
  - After a disconnect, `ControllerInputReader.pollController()` (line 29) prints "Controller disconnected." and returns, every tick, forever. The reader stays in the list and keeps polling the dead device, and its last input state is never reset, so a held direction or fire button can stay stuck on
  - Polling runs on the Swing thread every tick; an error thrown by JInput after a disconnect or a wake from sleep is caught by the game tick and written to error_log.txt, so that tick is lost
  - `initControllers()` has no catch of its own; if the JInput native libraries fail to load (only 64-bit Windows DLLs are bundled), startup can fail instead of falling back to no controllers
  - Fix direction: catch errors around `initControllers()` and `poll()`; on a failed poll, mark the reader disconnected, reset its input, log once and stop polling it. Don't rescan periodically: JInput has no hot-plug support, and the usual workaround (re-creating the controller environment) leaks native handles and is a known crash cause. If reconnecting is wanted, add a manual "rescan controllers" button in the menu, or switch to an SDL2-based library such as Jamepad
- Nelis' brother reports frequent crashes when the game keeps checking for a controller (2026-10-06); earlier proposed fixes didn't help
  - The current code doesn't re-check for controllers, so the cause is still unknown
  - Needed from him: the `hs_err_pid*.log` file from the game folder, `java -version` output, OS, controller model and connection (USB, Bluetooth, dongle), whether it crashes with no controller plugged in, when it crashes (startup, menu, mid-game, after unplugging or sleep), how he launches the game, and his error_log.txt
