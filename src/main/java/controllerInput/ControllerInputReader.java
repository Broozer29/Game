package controllerInput;

import java.util.HashMap;
import java.util.Map;

import net.java.games.input.Component;
import net.java.games.input.Controller;

//Translates controller input into the corresponding actions
public class ControllerInputReader {

	private Controller controller;
	private Map<ControllerInput, Boolean> inputState = new HashMap<>();

	public ControllerInputReader(Controller controller) {
		this.controller = controller;
	}

	public void pollController() {
	    controller.poll();
	    Component[] components = controller.getComponents();

	    for (Component component : components) {
	        String componentName = component.getName();
	        float componentValue = component.getPollData();

	        if (componentName.equals("x") || componentName.equals("X")) {
	            // Reset previous states
	            inputState.put(ControllerInput.MOVE_LEFT_SLOW, false);
	            inputState.put(ControllerInput.MOVE_LEFT_QUICK, false);
	            inputState.put(ControllerInput.MOVE_RIGHT_SLOW, false);
	            inputState.put(ControllerInput.MOVE_RIGHT_QUICK, false);

	            if (componentValue > -0.5 && componentValue <= -0.2) {
	                // Slow move left:
	                inputState.put(ControllerInput.MOVE_LEFT_SLOW, true);
	            } else if (componentValue <= -0.5) {
	                // Hard move left
	                inputState.put(ControllerInput.MOVE_LEFT_QUICK, true);
	            } else if (componentValue >= 0.2 && componentValue < 0.5) {
	                // Slow move right:
	                inputState.put(ControllerInput.MOVE_RIGHT_SLOW, true);
	            } else if (componentValue >= 0.5) {
	                // Hard move right
	                inputState.put(ControllerInput.MOVE_RIGHT_QUICK, true);
	            }
	        } else if (componentName.equals("y") || componentName.equals("Y")) {
	            // Reset previous states
	            inputState.put(ControllerInput.MOVE_UP_SLOW, false);
	            inputState.put(ControllerInput.MOVE_UP_QUICK, false);
	            inputState.put(ControllerInput.MOVE_DOWN_SLOW, false);
	            inputState.put(ControllerInput.MOVE_DOWN_QUICK, false);

	            if (componentValue > -0.5 && componentValue <= -0.2) {
	                // Slow move up:
	                inputState.put(ControllerInput.MOVE_UP_SLOW, true);
	            } else if (componentValue <= -0.5) {
	                // Hard move up
	                inputState.put(ControllerInput.MOVE_UP_QUICK, true);
	            } else if (componentValue >= 0.2 && componentValue < 0.5) {
	                // Slow move down:
	                inputState.put(ControllerInput.MOVE_DOWN_SLOW, true);
	            } else if (componentValue >= 0.5) {
	                // Hard move down
	                inputState.put(ControllerInput.MOVE_DOWN_QUICK, true);
	            }
	        } else if (componentName.equals("0")) {
	            inputState.put(ControllerInput.FIRE, componentValue > 0.5);
	        } else if (componentName.equals("1")) {
	            inputState.put(ControllerInput.SPECIAL_ATTACK, componentValue > 0.5);
	        }
	    }
	}


	public boolean isInputActive(ControllerInput input) {
		return inputState.getOrDefault(input, false);
	}
}
