package net.riezebos.bruus.tbd.controllerInput;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import java.util.HashMap;
import java.util.Map;

public class ConnectedControllersManager {
    private static ConnectedControllersManager instance = new ConnectedControllersManager();
    private int firstControllerIndex = -1;
    private Map<Integer, ControllerInputReader> controllerInputReaders = new HashMap<>();
    private Controller firstController;

    private ConnectedControllersManager() {
        initController();
    }

    public static ConnectedControllersManager getInstance() {
        return instance;
    }

    public void initController() {
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
                break;
            }
            index++;
        }

        if (firstControllerIndex == -1) {
            System.out.println("No controllers found.");
        }
    }

    public void setControllerSensitifties(boolean sensitive) {
        for (ControllerInputReader inputReader : controllerInputReaders.values()) {
            inputReader.setSensitiveInput(sensitive);
        }
    }

    public ControllerInputReader getFirstController() {
        return controllerInputReaders.get(firstControllerIndex);
    }
}
