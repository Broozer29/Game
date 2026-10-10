package net.riezebos.bruus.tbd.controllerInput;

// One place to play. It remembers its controller by Jamepad's device instance id, so shifting Jamepad slots do not matter.
public class Seat {
    private final int number;
    private int deviceInstanceId = -1;
    private ControllerInputReader reader;
    private boolean inRun = false; //Set at every level start: true when the seat has a ship in the current level
    private boolean keyboardInUse = false; //Only used by seat 1: a key press marks it as played by the keyboard

    public Seat(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    // A real controller is connected to this seat
    public boolean hasRealController() {
        return reader != null;
    }

    // The seat is played right now: by its controller, or (seat 1 only) by the keyboard
    public boolean hasController() {
        return reader != null || (number == 1 && keyboardInUse);
    }

    public boolean isInRun() {
        return inRun;
    }

    public void setInRun(boolean inRun) {
        this.inRun = inRun;
    }

    public void setKeyboardInUse(boolean keyboardInUse) {
        this.keyboardInUse = keyboardInUse;
    }

    public int getDeviceInstanceId() {
        return deviceInstanceId;
    }

    // Null when the seat has no real controller
    public ControllerInputReader getReader() {
        return reader;
    }

    void setController(int deviceInstanceId, ControllerInputReader reader) {
        this.deviceInstanceId = deviceInstanceId;
        this.reader = reader;
    }

    void clearController() {
        this.deviceInstanceId = -1;
        this.reader = null;
    }
}
