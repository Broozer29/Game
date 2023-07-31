package controllerInput;

import java.util.HashMap;
import java.util.Map;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class ConnectedControllers {
    private static ConnectedControllers instance = new ConnectedControllers();
    private Controller firstController = null;
    private Map<Controller, ControllerInputReader> controllerInputReaders = new HashMap<>();
    

    private ConnectedControllers() {

    }

    public static ConnectedControllers getInstance() {
        return instance;
    }

    public void initController() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for (int i = 0; i < controllers.length && firstController == null; i++) {
            if (controllers[i].getType() == Controller.Type.GAMEPAD
                    || controllers[i].getType() == Controller.Type.STICK) {
                // Found a controller
                firstController = controllers[i];
                // Create a new ControllerInputReader and add it to the map
                controllerInputReaders.put(firstController, new ControllerInputReader(firstController));
            }
        }
        if (firstController == null) {
            // Couldn't find a controller
            System.out.println("Found no controller");
            // Optionally: System.exit(0);
        } else {

            System.out.println("First controller is: " + firstController.getName());
            Component[] components = firstController.getComponents();
            for (Component component : components) {
                System.out.println("Component: " + component.getName() + ", Value: " + component.getPollData());
            }
        }
    }

    public Controller getFirstController() {
        return firstController;
    }

    public void setFirstController(Controller firstController) {
        this.firstController = firstController;
    }

    // Returns the ControllerInputReader associated with the given Controller
    public ControllerInputReader getControllerInputReader(Controller controller) {
        return controllerInputReaders.get(controller);
    }
}
