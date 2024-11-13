package net.riezebos.bruus.tbd.controllerInput;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import java.util.HashMap;
import java.util.Map;

public class ConnectedControllersManager {
    private static ConnectedControllersManager instance = new ConnectedControllersManager();
    private ControllerManager controllers = new ControllerManager();
    private int firstControllerIndex = -1;
    private Map<Integer, ControllerInputReader> controllerInputReaders = new HashMap<>();

    private ConnectedControllersManager () {
        controllers.initSDLGamepad();
    }

    public static ConnectedControllersManager getInstance() {
        return instance;
    }

    public void initController() {
        try {
            Thread.sleep(500); // Sleep for half a second to let SDL initialize properly
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        controllers.update();
        int numControllers = controllers.getNumControllers();
        System.out.println("Controllers found: " + numControllers);

        controllers.update();

        for (int i = 0; i < numControllers; i++) {
            ControllerState state = controllers.getState(i);
            if (state.isConnected) {
                firstControllerIndex = i;
                controllerInputReaders.put(firstControllerIndex, new ControllerInputReader(controllers, firstControllerIndex));
                break; // Assuming you only want the first controller
            }
        }

        if (firstControllerIndex == -1) {
            System.out.println("Found no controller");
        } else {
            // Get the name of the controller directly from its ControllerState
            String controllerName = controllers.getState(firstControllerIndex).controllerType;
            System.out.println("First controller is: " + controllerName);
        }
    }

    public void setControllerSensitifties(boolean sensitive){
        for(ControllerInputReader inputReader : controllerInputReaders.values()){
            inputReader.setSensitiveInput(sensitive);
        }
    }

    public ControllerInputReader getFirstController() {
        return controllerInputReaders.get(firstControllerIndex);
    }

    public void setFirstControllerIndex(int index) {
        this.firstControllerIndex = index;
    }

    // Returns the ControllerInputReader associated with the given controller index
    public ControllerInputReader getControllerInputReader(int controllerIndex) {
        return controllerInputReaders.get(controllerIndex);
    }
}