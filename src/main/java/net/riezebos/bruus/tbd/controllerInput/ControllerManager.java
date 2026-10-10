package net.riezebos.bruus.tbd.controllerInput;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerManager {
    private static ControllerManager instance = new ControllerManager();
    private Map<Integer, ControllerInputReader> controllerInputReaders = new HashMap<>();
    private ControllerInputReader primaryReader; //Multiplayer update: deze is nog nodig om te bepalen welke controller mag sturen in shop/menu en andere schermen. De "primaire" gebruiker.

    private ControllerManager() {
    }

    public static ControllerManager getInstance() {
        return instance;
    }

    public void initControllers() {
        controllerInputReaders.clear();
        primaryReader = null;
        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(500); // Allow time for initialization
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long stepStart = System.currentTimeMillis();
        logDiagnostic("Controllers: sleep took " + (stepStart - startTime) + " ms");


        Controller[] controllers;
        try {
            ControllerEnvironment environment = ControllerEnvironment.getDefaultEnvironment();
            logDiagnostic("Controllers: JInput environment loaded in " + (System.currentTimeMillis() - stepStart) + " ms");
            stepStart = System.currentTimeMillis();
            controllers = environment.getControllers();
            logDiagnostic("Controllers: device list read in " + (System.currentTimeMillis() - stepStart) + " ms, " + controllers.length + " devices");
        } catch (LinkageError e) {
            System.out.println("Could not load the controller library: " + e.getMessage());
            logDiagnostic("Controllers: could not load the controller library after " + (System.currentTimeMillis() - stepStart) + " ms");
            controllers = new Controller[0];
        }
        int index = 0;

        for (Controller controller : controllers) {
            boolean used = controller.getType() == Controller.Type.GAMEPAD || controller.getType() == Controller.Type.STICK;
            logDiagnostic("Controllers:   device \"" + controller.getName() + "\" type " + controller.getType() + ", " + (used ? "used" : "skipped"));
            if (used) {
                ControllerInputReader reader = new ControllerInputReader(controller);
                controllerInputReaders.put(index, reader);
                if (primaryReader == null) {
                    primaryReader = reader; // Only the first detected controller becomes primary
                    System.out.println("First controller detected: " + controller.getName());
                } else {
                    System.out.println("Additional controller detected: " + controller.getName());
                }
            }
            index++;
        }

        if (primaryReader == null) {
            System.out.println("No controllers found.");
        } else {
            System.out.println("ControllerManager initialized with " + controllerInputReaders.size() + " controllers.");
        }
        logDiagnostic("Controllers: done in " + (System.currentTimeMillis() - startTime) + " ms, " + controllerInputReaders.size() + " controller(s) in use");
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
        return new ArrayList<>(controllerInputReaders.values());
    }

    public void setControllerSensitive(boolean sensitive) {
        for (ControllerInputReader inputReader : controllerInputReaders.values()) {
            inputReader.setSensitiveInput(sensitive);
        }
    }

    public ControllerInputReader getPrimaryController() {
        return primaryReader;
    }

    public boolean isPausePressed(){
        for(ControllerInputReader controllerInputReader : controllerInputReaders.values()){
            if(controllerInputReader.isInputActive(ControllerInputEnums.PAUSE)){
                return true; //return true if 1 of them has it pressed,
            }
        }
        return false;
    }

    public boolean isFirePressed(){
        for(ControllerInputReader controllerInputReader : controllerInputReaders.values()){
            if(controllerInputReader.isInputActive(ControllerInputEnums.FIRE)){
                return true; //return true if 1 of them has it pressed,
            }
        }
        return false;
    }

    public boolean isPrimaryControllerLeftPressed(){
        return getPrimaryController().isInputActive(ControllerInputEnums.MOVE_LEFT);
    }

    public boolean isPrimaryControllerRightPressed(){
        return getPrimaryController().isInputActive(ControllerInputEnums.MOVE_RIGHT);
    }

    //Required because controllerInput is not read after the spaceship dies, thus if all players are dead and game over screen is shown, this method is needed to continue
    public void pollControllers(){
        for(ControllerInputReader controllerInputReader : controllerInputReaders.values()){
            controllerInputReader.pollController();
        }
    }

    public void requestControl(ControllerInputReader controllerInputReader) {
        this.primaryReader = controllerInputReader;
    }

    public void resetInputStates(){
        for(ControllerInputReader controllerInputReader : controllerInputReaders.values()){
            controllerInputReader.resetInputStates();
        }
    }
}
