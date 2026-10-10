package net.riezebos.bruus.tbd.controllerInput;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;
import net.riezebos.bruus.tbd.game.gamestate.GameState;

import java.util.HashMap;
import java.util.Map;

public class ControllerInputReader {
    private ControllerIndex controller;
    private Map<ControllerInputEnums, Boolean> inputState = new HashMap<>();

    private float xAxisValue;
    private float yAxisValue;
    private float inputStrengthRequired;
    private boolean sensitiveInput;
    private double lastGameSecondsTogglePressed = 0;
    private double toggleDelay = 1;
    private boolean holdFireButtonWasPressed = false;
    private boolean pauseButtonWasPressed = false;
    private boolean pausePressedSinceLastCheck = false;
    private boolean disconnected = false;

    public ControllerInputReader(ControllerIndex controller) {
        this.controller = controller;
        this.setSensitiveInput(false);
    }

    public void pollController() {
        if (disconnected) {
            return;
        }

        ControllerManager.getInstance().updateSdl();
        if (!controller.isConnected()) {
            resetInputStates();
            disconnected = true;
            System.out.println("Controller disconnected.");
            return;
        }

        try {
            // Left stick. SDL reports up as negative, same as the screen, so no flip is needed
            xAxisValue = controller.getAxisState(ControllerAxis.LEFTX);
            yAxisValue = controller.getAxisState(ControllerAxis.LEFTY);
            boolean left = xAxisValue <= -inputStrengthRequired;
            boolean right = xAxisValue >= inputStrengthRequired;
            boolean up = yAxisValue <= -inputStrengthRequired;
            boolean down = yAxisValue >= inputStrengthRequired;

            // The d-pad only moves the cursor in menus, not while flying
            if (!sensitiveInput) {
                left |= controller.isButtonPressed(ControllerButton.DPAD_LEFT);
                right |= controller.isButtonPressed(ControllerButton.DPAD_RIGHT);
                up |= controller.isButtonPressed(ControllerButton.DPAD_UP);
                down |= controller.isButtonPressed(ControllerButton.DPAD_DOWN);
            }
            inputState.put(ControllerInputEnums.MOVE_LEFT, left);
            inputState.put(ControllerInputEnums.MOVE_RIGHT, right);
            inputState.put(ControllerInputEnums.MOVE_UP, up);
            inputState.put(ControllerInputEnums.MOVE_DOWN, down);

            inputState.put(ControllerInputEnums.FIRE, controller.isButtonPressed(ControllerButton.A)); // Button A
            inputState.put(ControllerInputEnums.SPECIAL_ATTACK, controller.isButtonPressed(ControllerButton.B)); // Button B
            boolean pauseButtonPressed = controller.isButtonPressed(ControllerButton.START); // Menu button
            inputState.put(ControllerInputEnums.PAUSE, pauseButtonPressed);
            // Remember the moment the button goes down, so holding it does not pause and unpause every frame
            if (pauseButtonPressed && !pauseButtonWasPressed) {
                pausePressedSinceLastCheck = true;
            }
            pauseButtonWasPressed = pauseButtonPressed;

            // Toggle only when LB goes down, so holding it does not toggle again every second
            boolean holdFireButtonPressed = controller.isButtonPressed(ControllerButton.LEFTBUMPER);
            if (holdFireButtonPressed && !holdFireButtonWasPressed && GameState.getInstance().getGameSeconds() - lastGameSecondsTogglePressed > toggleDelay) {
                lastGameSecondsTogglePressed = GameState.getInstance().getGameSeconds();
                toggleHoldFire();
            }
            holdFireButtonWasPressed = holdFireButtonPressed;
        } catch (ControllerUnpluggedException e) {
            resetInputStates();
            disconnected = true;
            System.out.println(e.getMessage() + " Controller disconnected.");
        }
    }

    private void toggleHoldFire() {
        inputState.put(ControllerInputEnums.HOLD_FIRE, !inputState.getOrDefault(ControllerInputEnums.HOLD_FIRE, false));
    }

    // True once per press of the pause button; asking clears it
    public boolean consumePausePress() {
        boolean pressed = pausePressedSinceLastCheck;
        pausePressedSinceLastCheck = false;
        return pressed;
    }

    public boolean isInputActive(ControllerInputEnums input) {
        return inputState.getOrDefault(input, false);
    }

    public void setSensitiveInput(boolean sensitiveInput) {
        this.sensitiveInput = sensitiveInput;
        adjustSensitivity();
    }

    private void adjustSensitivity() {
        if (this.sensitiveInput) {
            this.inputStrengthRequired = 0.1f;
        } else {
            this.inputStrengthRequired = 0.5f;
        }
    }

    public float getxAxisValue() {
        return xAxisValue;
    }

    public float getyAxisValue() {
        return yAxisValue;
    }

    public void resetInputStates() {
        // Handle axis movement (Left Stick)
        inputState.put(ControllerInputEnums.MOVE_LEFT, false);
        inputState.put(ControllerInputEnums.MOVE_RIGHT, false);
        inputState.put(ControllerInputEnums.MOVE_UP, false);
        inputState.put(ControllerInputEnums.MOVE_DOWN, false);

        // Handle button presses
        inputState.put(ControllerInputEnums.HOLD_FIRE, false); // LB
        inputState.put(ControllerInputEnums.FIRE, false); // Button A
        inputState.put(ControllerInputEnums.SPECIAL_ATTACK, false); // Button B
        inputState.put(ControllerInputEnums.PAUSE, false); // Menu button
        pausePressedSinceLastCheck = false;
    }
}
