package net.riezebos.bruus.tbd.controllerInput;

import com.studiohartman.jamepad.Configuration;
import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ControllerManager {
    private static ControllerManager instance = new ControllerManager();
    private static final int SEAT_COUNT = 4;
    private static final int CONTROLLER_CLOCK_DELAY = 15;
    private final List<Seat> seats = new ArrayList<>();
    private com.studiohartman.jamepad.ControllerManager sdlManager;
    private Configuration configuration;
    private Timer controllerClock;
    private boolean sensitiveInput = false; // the current screen's setting, so a controller that joins later gets it too

    private ControllerManager() {
        for (int i = 1; i <= SEAT_COUNT; i++) {
            seats.add(new Seat(i));
        }
    }

    public static ControllerManager getInstance() {
        return instance;
    }

    public void initControllers() {
        if (controllerClock != null) {
            controllerClock.stop();
            controllerClock = null;
        }
        for (Seat seat : seats) {
            seat.clearController();
        }
        sdlManager = null;
        long startTime = System.currentTimeMillis();
        configuration = new Configuration();
        try {
            com.studiohartman.jamepad.ControllerManager manager = new com.studiohartman.jamepad.ControllerManager(configuration);
            manager.initSDLGamepad();
            sdlManager = manager;
            logDiagnostic("Controllers: SDL started in " + (System.currentTimeMillis() - startTime) + " ms");
        } catch (Throwable e) {
            System.out.println("Could not start the controller library: " + e.getMessage());
            logDiagnostic("Controllers: could not start SDL after " + (System.currentTimeMillis() - startTime) + " ms: " + e);
        }

        if (sdlManager != null) {
            // Every connected controller gets a seat, in slot order
            for (int slot = 0; slot < configuration.maxNumControllers; slot++) {
                ControllerIndex controllerIndex = sdlManager.getControllerIndex(slot);
                if (!controllerIndex.isConnected()) {
                    continue;
                }
                String name;
                int deviceInstanceId;
                try {
                    name = controllerIndex.getName();
                    deviceInstanceId = controllerIndex.getDeviceInstanceID();
                } catch (ControllerUnpluggedException e) {
                    continue;
                }
                Seat seat = findFreeSeat();
                if (seat == null) {
                    logDiagnostic("Controllers:   controller in slot " + slot + " has no free seat: " + name);
                    continue;
                }
                seat.setController(deviceInstanceId, new ControllerInputReader());
                logDiagnostic("Controllers:   controller in slot " + slot + ", seat " + seat.getNumber() + ": " + name);
            }
        }

        int seated = getControllerInputReaders().size();
        if (seated == 0) {
            System.out.println("No controllers found.");
        } else {
            System.out.println("ControllerManager initialized with " + seated + " controllers.");
        }
        logDiagnostic("Controllers: done in " + (System.currentTimeMillis() - startTime) + " ms, " + seated + " controller(s) in use");

        if (sdlManager != null) {
            controllerClock = new Timer(CONTROLLER_CLOCK_DELAY, e -> controllerClockTick());
            controllerClock.start();
        }
    }

    // The controller clock: the only place that updates Jamepad and reads the controllers. Runs on the Swing thread.
    private void controllerClockTick() {
        sdlManager.update();

        // Controllers connected now, by device instance id
        Map<Integer, ControllerIndex> connected = new LinkedHashMap<>();
        for (int slot = 0; slot < configuration.maxNumControllers; slot++) {
            ControllerIndex controllerIndex = sdlManager.getControllerIndex(slot);
            if (!controllerIndex.isConnected()) {
                continue;
            }
            try {
                connected.put(controllerIndex.getDeviceInstanceID(), controllerIndex);
            } catch (ControllerUnpluggedException e) {
                // Went away between the two calls; seen as not connected
            }
        }

        // A controller that disappeared leaves its seat
        for (Seat seat : seats) {
            if (seat.hasController() && !connected.containsKey(seat.getDeviceInstanceId())) {
                seat.getReader().resetInputStates(); //So no direction or fire stays held on a ship that still holds this reader
                seat.clearController();
                ControllerNotices.getInstance().addNotice("CONTROLLER " + seat.getNumber() + " DISCONNECTED");
                logDiagnostic("Controllers: controller left seat " + seat.getNumber());
            }
        }

        // A free controller takes a seat on any input
        Set<Integer> justJoined = new HashSet<>();
        for (Map.Entry<Integer, ControllerIndex> entry : connected.entrySet()) {
            if (findSeatOfController(entry.getKey()) != null) {
                continue;
            }
            Seat seat = findFreeSeat();
            if (seat == null) {
                break; //No seat available, the free controllers wait
            }
            if (isAnyInputActive(entry.getValue())) {
                ControllerInputReader reader = new ControllerInputReader();
                reader.setSensitiveInput(sensitiveInput);
                reader.resetInputStates();
                reader.ignoreInputUntilReleased(); //So the joining input does not also confirm a menu or fire
                seat.setController(entry.getKey(), reader);
                justJoined.add(seat.getNumber());
                ControllerNotices.getInstance().addNotice("CONTROLLER " + seat.getNumber() + " CONNECTED");
                String name;
                try {
                    name = entry.getValue().getName();
                } catch (ControllerUnpluggedException e) {
                    name = "unknown";
                }
                logDiagnostic("Controllers: controller joined seat " + seat.getNumber() + ": " + name);
            }
        }

        // Every seated reader reads its controller; slots shift, so the reader gets the slot that now holds its id
        for (Seat seat : seats) {
            if (seat.hasController() && !justJoined.contains(seat.getNumber())) {
                seat.getReader().readController(connected.get(seat.getDeviceInstanceId()));
            }
        }
    }

    // The lowest seat that is in the run but has no controller, else the lowest seat without a controller
    private Seat findFreeSeat() {
        for (Seat seat : seats) {
            if (seat.isInRun() && !seat.hasController()) {
                return seat;
            }
        }
        for (Seat seat : seats) {
            if (!seat.hasController()) {
                return seat;
            }
        }
        return null;
    }

    private Seat findSeatOfController(int deviceInstanceId) {
        for (Seat seat : seats) {
            if (seat.hasController() && seat.getDeviceInstanceId() == deviceInstanceId) {
                return seat;
            }
        }
        return null;
    }

    // Any button, or a stick or trigger pushed past halfway, lets a free controller join
    static boolean isAnyInputActive(ControllerIndex controllerIndex) {
        try {
            for (ControllerButton button : ControllerButton.values()) {
                if (controllerIndex.isButtonPressed(button)) {
                    return true;
                }
            }
            for (ControllerAxis axis : ControllerAxis.values()) {
                if (Math.abs(controllerIndex.getAxisState(axis)) > 0.5f) {
                    return true;
                }
            }
        } catch (ControllerUnpluggedException e) {
            // Gone again; the next tick sees it as not connected
        }
        return false;
    }

    private void logDiagnostic(String message) {
        System.out.println(message);
        try {
            java.io.FileWriter fw = new java.io.FileWriter("startup_log.txt", true);
            java.io.PrintWriter pw = new java.io.PrintWriter(fw);
            pw.println("[" + java.time.LocalDateTime.now() + "] " + message);
            pw.close();
        } catch (java.io.IOException e) {
            // Silently fail if can't write to log
        }
    }

    public List<ControllerInputReader> getControllerInputReaders() {
        List<ControllerInputReader> readers = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.hasController()) {
                readers.add(seat.getReader());
            }
        }
        return readers;
    }

    public void setControllerSensitive(boolean sensitive) {
        this.sensitiveInput = sensitive;
        for (ControllerInputReader inputReader : getControllerInputReaders()) {
            inputReader.setSensitiveInput(sensitive);
        }
    }

    // The reader of the main seat: the lowest seat that has a controller. Worked out each time, so the menus follow the main seat when it changes.
    public ControllerInputReader getPrimaryController() {
        for (Seat seat : seats) {
            if (seat.hasController()) {
                return seat.getReader();
            }
        }
        return null;
    }

    public boolean isPausePressed(){
        boolean pressed = false;
        for(ControllerInputReader controllerInputReader : getControllerInputReaders()){
            if(controllerInputReader.consumePausePress()){
                pressed = true; //true if 1 of them pressed it; every reader is asked so no old press is left behind
            }
        }
        return pressed;
    }

    public boolean isFirePressed(){
        for(ControllerInputReader controllerInputReader : getControllerInputReaders()){
            if(controllerInputReader.isInputActive(ControllerInputEnums.FIRE)){
                return true; //return true if 1 of them has it pressed,
            }
        }
        return false;
    }

    public boolean isPrimaryControllerLeftPressed(){
        ControllerInputReader primary = getPrimaryController();
        return primary != null && primary.isInputActive(ControllerInputEnums.MOVE_LEFT);
    }

    public boolean isPrimaryControllerRightPressed(){
        ControllerInputReader primary = getPrimaryController();
        return primary != null && primary.isInputActive(ControllerInputEnums.MOVE_RIGHT);
    }

    //Required because controllerInput is not read after the spaceship dies, thus if all players are dead and game over screen is shown, this method is needed to continue
    public void pollControllers(){
        for(ControllerInputReader controllerInputReader : getControllerInputReaders()){
            controllerInputReader.pollController();
        }
    }

    public void resetInputStates(){
        for(ControllerInputReader controllerInputReader : getControllerInputReaders()){
            controllerInputReader.resetInputStates();
        }
    }
}
