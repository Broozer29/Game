package controllerInput;

import com.studiohartman.jamepad.*;

import java.util.HashMap;
import java.util.Map;

public class ControllerInputReader {
	private ControllerManager controllerManager;
	private int controllerIndex;
	private Map<ControllerInput, Boolean> inputState = new HashMap<>();

	public ControllerInputReader(ControllerManager controllerManager, int controllerIndex) {
		this.controllerManager = controllerManager;
		this.controllerIndex = controllerIndex;
	}

	public void pollController() {
		controllerManager.update(); // Make sure to update the state of the controllers
		ControllerIndex currController = controllerManager.getControllerIndex(controllerIndex);

		try {
			// Example for mapping left stick horizontal axis
			float xAxisValue = currController.getAxisState(ControllerAxis.LEFTX);
			inputState.put(ControllerInput.MOVE_LEFT_SLOW, xAxisValue > -0.5 && xAxisValue <= -0.2);
			inputState.put(ControllerInput.MOVE_LEFT_QUICK, xAxisValue <= -0.5);
			inputState.put(ControllerInput.MOVE_RIGHT_SLOW, xAxisValue >= 0.2 && xAxisValue < 0.5);
			inputState.put(ControllerInput.MOVE_RIGHT_QUICK, xAxisValue >= 0.5);

			float yAxisValue = currController.getAxisState(ControllerAxis.LEFTY);
			inputState.put(ControllerInput.MOVE_DOWN_SLOW, yAxisValue > -0.5 && yAxisValue <= -0.2);
			inputState.put(ControllerInput.MOVE_DOWN_QUICK, yAxisValue <= -0.5);
			inputState.put(ControllerInput.MOVE_UP_SLOW, yAxisValue >= 0.2 && yAxisValue < 0.5);
			inputState.put(ControllerInput.MOVE_UP_QUICK, yAxisValue >= 0.5);


			// Similar mappings for other inputs based on the enum
			// Example for buttons
			inputState.put(ControllerInput.FIRE, currController.isButtonPressed(ControllerButton.A)); // Assuming 'A' button maps to FIRE
			inputState.put(ControllerInput.SPECIAL_ATTACK, currController.isButtonPressed(ControllerButton.B)); // Assuming 'B' button maps to SPECIAL_ATTACK

			// Additional mappings as per your ControllerInput enum
		} catch (ControllerUnpluggedException e) {
			// Handle disconnected controller
			System.out.println("Controller disconnected");
		}
	}

	public boolean isInputActive(ControllerInput input) {
		return inputState.getOrDefault(input, false);
	}

}