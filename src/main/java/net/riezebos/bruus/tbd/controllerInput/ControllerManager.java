package net.riezebos.bruus.tbd.controllerInput;

import com.studiohartman.jamepad.Configuration;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerManager {
    private static ControllerManager instance = new ControllerManager();
    private Map<Integer, ControllerInputReader> controllerInputReaders = new HashMap<>();
    private com.studiohartman.jamepad.ControllerManager sdlManager;
    private ControllerInputReader primaryReader; //Multiplayer update: deze is nog nodig om te bepalen welke controller mag sturen in shop/menu en andere schermen. De "primaire" gebruiker.

    private ControllerManager() {
    }

    public static ControllerManager getInstance() {
        return instance;
    }

    public void initControllers() {
        controllerInputReaders.clear();
        primaryReader = null;
        sdlManager = null;
        long startTime = System.currentTimeMillis();
        Configuration configuration = new Configuration();
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
            for (int slot = 0; slot < configuration.maxNumControllers; slot++) {
                ControllerIndex controllerIndex = sdlManager.getControllerIndex(slot);
                if (!controllerIndex.isConnected()) {
                    continue;
                }
                String name;
                try {
                    name = controllerIndex.getName();
                } catch (ControllerUnpluggedException e) {
                    continue;
                }
                logDiagnostic("Controllers:   pad in slot " + slot + ": " + name);
                ControllerInputReader reader = new ControllerInputReader(controllerIndex);
                controllerInputReaders.put(slot, reader);
                if (primaryReader == null) {
                    primaryReader = reader; // Only the first detected controller becomes primary
                    System.out.println("First controller detected: " + name);
                } else {
                    System.out.println("Additional controller detected: " + name);
                }
            }
        }

        if (primaryReader == null) {
            System.out.println("No controllers found.");
        } else {
            System.out.println("ControllerManager initialized with " + controllerInputReaders.size() + " controllers.");
        }
        logDiagnostic("Controllers: done in " + (System.currentTimeMillis() - startTime) + " ms, " + controllerInputReaders.size() + " controller(s) in use");
    }

    // Lets Jamepad read the pads' current state; the readers call this before they read their slot
    void updateSdl() {
        if (sdlManager != null) {
            sdlManager.update();
        }
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
        boolean pressed = false;
        for(ControllerInputReader controllerInputReader : controllerInputReaders.values()){
            if(controllerInputReader.consumePausePress()){
                pressed = true; //true if 1 of them pressed it; every reader is asked so no old press is left behind
            }
        }
        return pressed;
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

    public void resetInputStates(){
        for(ControllerInputReader controllerInputReader : controllerInputReaders.values()){
            controllerInputReader.resetInputStates();
        }
    }
}
