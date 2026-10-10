package net.riezebos.bruus.tbd.controllerInput;

// One of the 4 places to play. It remembers its controller by Jamepad's device instance id, so shifting Jamepad slots do not matter.
public class Seat {
    private final int number;
    private int deviceInstanceId = -1;
    private ControllerInputReader reader;
    private boolean inRun = false; //Nothing sets this yet; it will be set when a seat joins a run

    public Seat(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public boolean hasController() {
        return reader != null;
    }

    public boolean isInRun() {
        return inRun;
    }

    public int getDeviceInstanceId() {
        return deviceInstanceId;
    }

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
