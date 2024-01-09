package controllerInput;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import java.util.HashMap;
import java.util.Map;

public class ConnectedControllers {
    private static ConnectedControllers instance = new ConnectedControllers();
    private ControllerManager controllers = new ControllerManager();
    private int firstControllerIndex = -1;
    private Map<Integer, ControllerInputReader> controllerInputReaders = new HashMap<>();

    private ConnectedControllers() {
        controllers.initSDLGamepad();
    }

    public static ConnectedControllers getInstance() {
        return instance;
    }

    public void initController() {
        controllers.update();
        int numControllers = controllers.getNumControllers();

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