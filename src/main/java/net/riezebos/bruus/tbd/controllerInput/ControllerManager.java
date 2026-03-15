package net.riezebos.bruus.tbd.controllerInput;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerManager {
    private static ControllerManager instance = new ControllerManager();
    private int firstControllerIndex = -1;
    private Map<Integer, ControllerInputReader> controllerInputReaders = new HashMap<>();
    private Controller firstController; //Multiplayer update: deze is nog nodig om te bepalen welke controller mag sturen in shop/menu en andere schermen. De "primaire" gebruiker.

    private ControllerManager() {
    }

    public static ControllerManager getInstance() {
        return instance;
    }

    public void initControllers() {
        controllerInputReaders.clear();
        firstController = null;
        firstControllerIndex = -1;
        try {
            Thread.sleep(500); // Allow time for initialization
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        int index = 0;

        for (Controller controller : controllers) {
            if (controller.getType() == Controller.Type.GAMEPAD || controller.getType() == Controller.Type.STICK) {
                firstController = controller;
                firstControllerIndex = index;
                controllerInputReaders.put(firstControllerIndex, new ControllerInputReader(firstController));
                System.out.println("First controller detected: " + controller.getName());
            }
            index++;
        }

        if (firstControllerIndex == -1) {
            System.out.println("No controllers found.");
        } else {
            System.out.println("ControllerManager initialized with " + controllerInputReaders.size() + " controllers.");
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

    public ControllerInputReader getFirstController() {
        return controllerInputReaders.get(firstControllerIndex);
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

    //Required because controllerInput is not read after the spaceship dies, thus if all players are dead and game over screen is shown, this method is needed to continue
    public void pollControllers(){
        for(ControllerInputReader controllerInputReader : controllerInputReaders.values()){
            controllerInputReader.pollController();
        }
    }
}
