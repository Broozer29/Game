package controllerInput;

import com.studiohartman.jamepad.*;

import java.util.HashMap;
import java.util.Map;

public class ControllerInputReader {
    private ControllerManager controllerManager;
    private int controllerIndex;
    private Map<ControllerInputEnums, Boolean> inputState = new HashMap<>();

    private float xAxisValue;
    private float yAxisValue;

    public ControllerInputReader (ControllerManager controllerManager, int controllerIndex) {
        this.controllerManager = controllerManager;
        this.controllerIndex = controllerIndex;
    }

    public void pollController () {
        controllerManager.update(); // Make sure to update the state of the controllers
        ControllerIndex currController = controllerManager.getControllerIndex(controllerIndex);
//		testControllerInputs();

        try {
            // Example for mapping left stick horizontal axis
            xAxisValue = currController.getAxisState(ControllerAxis.LEFTX);
//            inputState.put(ControllerInputEnums.MOVE_LEFT_SLOW, xAxisValue > -0.5 && xAxisValue <= -0.2);
            inputState.put(ControllerInputEnums.MOVE_LEFT_QUICK, xAxisValue <= -0.2);
//            inputState.put(ControllerInputEnums.MOVE_RIGHT_SLOW, xAxisValue >= 0.2 && xAxisValue < 0.5);
            inputState.put(ControllerInputEnums.MOVE_RIGHT_QUICK, xAxisValue >= 0.2);



            yAxisValue = currController.getAxisState(ControllerAxis.LEFTY);
//            inputState.put(ControllerInputEnums.MOVE_DOWN_SLOW, yAxisValue > -0.5 && yAxisValue <= -0.2);
            inputState.put(ControllerInputEnums.MOVE_DOWN_QUICK, yAxisValue <= -0.2);
//            inputState.put(ControllerInputEnums.MOVE_UP_SLOW, yAxisValue >= 0.2 && yAxisValue < 0.5);
            inputState.put(ControllerInputEnums.MOVE_UP_QUICK, yAxisValue >= 0.2);

            // Similar mappings for other inputs based on the enum
            // Example for buttons
            inputState.put(ControllerInputEnums.FIRE, currController.isButtonPressed(ControllerButton.A)); // Assuming 'A' button maps to FIRE
            inputState.put(ControllerInputEnums.SPECIAL_ATTACK, currController.isButtonPressed(ControllerButton.B)); // Assuming 'B' button maps to SPECIAL_ATTACK

            // Additional mappings as per your ControllerInput enum
        } catch (ControllerUnpluggedException e) {
            // Handle disconnected controller
            System.out.println("Controller disconnected");
        }
    }

    public boolean isInputActive (ControllerInputEnums input) {
        return inputState.getOrDefault(input, false);
    }

    private void testControllerInputs () {
        controllerManager.update();
        ControllerIndex currController = controllerManager.getControllerIndex(controllerIndex);

        try {
            // Check all buttons to see if they are pressed
            for (ControllerButton button : ControllerButton.values()) {
                if (currController.isButtonPressed(button)) {
                    System.out.println(button + " pressed");
                }
            }

            // Check all axes for their current state
            for (ControllerAxis axis : ControllerAxis.values()) {
                float axisValue = currController.getAxisState(axis);
                if (axisValue != 0) { // You might want to check a deadzone instead of zero
                    System.out.println(axis + " axis: " + axisValue);
                }
            }

            System.out.println("");
        } catch (ControllerUnpluggedException e) {
            System.out.println("Controller disconnected");
        }
    }

    public float getxAxisValue () {
        return xAxisValue;
    }

    public float getyAxisValue () {
        return yAxisValue;
    }
}