package net.riezebos.bruus.tbd.controllerInput;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;
import net.riezebos.bruus.tbd.game.gamestate.GameState;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class ControllerInputReader {
    private Map<ControllerInputEnums, Boolean> inputState = new HashMap<>();

    private float xAxisValue;
    private float yAxisValue;
    private static final float INPUT_STRENGTH_REQUIRED = 0.1f;
    private static final float MENU_INPUT_STRENGTH_REQUIRED = 0.5f;
    private final Map<MenuAction, Boolean> menuState = new EnumMap<>(MenuAction.class);
    private double lastGameSecondsTogglePressed = 0;
    private double toggleDelay = 1;
    private boolean holdFireButtonWasPressed = false;
    private boolean pauseButtonWasPressed = false;
    private boolean pausePressedSinceLastCheck = false;
    private boolean waitingForRelease = false;

    // After joining a seat, the input that joined is ignored until everything is let go
    void ignoreInputUntilReleased() {
        waitingForRelease = true;
    }

    // Called by the controller clock every tick with the slot that holds this reader's controller
    void readController(ControllerIndex controller) {
        if (controller == null || !controller.isConnected()) {
            resetInputStates();
            return;
        }

        if (waitingForRelease) {
            if (ControllerManager.isAnyInputActive(controller)) {
                resetInputStates();
                return;
            }
            waitingForRelease = false;
        }

        try {
            // Left stick. SDL reports up as negative, same as the screen, so no flip is needed
            xAxisValue = controller.getAxisState(ControllerAxis.LEFTX);
            yAxisValue = controller.getAxisState(ControllerAxis.LEFTY);
            // Flying: the stick at 10%, never the d-pad
            inputState.put(ControllerInputEnums.MOVE_LEFT, xAxisValue <= -INPUT_STRENGTH_REQUIRED);
            inputState.put(ControllerInputEnums.MOVE_RIGHT, xAxisValue >= INPUT_STRENGTH_REQUIRED);
            inputState.put(ControllerInputEnums.MOVE_UP, yAxisValue <= -INPUT_STRENGTH_REQUIRED);
            inputState.put(ControllerInputEnums.MOVE_DOWN, yAxisValue >= INPUT_STRENGTH_REQUIRED);

            // Menus: the stick at 50% or the d-pad
            menuState.put(MenuAction.LEFT, xAxisValue <= -MENU_INPUT_STRENGTH_REQUIRED || controller.isButtonPressed(ControllerButton.DPAD_LEFT));
            menuState.put(MenuAction.RIGHT, xAxisValue >= MENU_INPUT_STRENGTH_REQUIRED || controller.isButtonPressed(ControllerButton.DPAD_RIGHT));
            menuState.put(MenuAction.UP, yAxisValue <= -MENU_INPUT_STRENGTH_REQUIRED || controller.isButtonPressed(ControllerButton.DPAD_UP));
            menuState.put(MenuAction.DOWN, yAxisValue >= MENU_INPUT_STRENGTH_REQUIRED || controller.isButtonPressed(ControllerButton.DPAD_DOWN));
            menuState.put(MenuAction.CONFIRM, controller.isButtonPressed(ControllerButton.A));
            menuState.put(MenuAction.BACK, controller.isButtonPressed(ControllerButton.B));
            menuState.put(MenuAction.ANY_BUTTON, isAnyMenuButtonPressed(controller));

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
            System.out.println(e.getMessage() + " Controller disconnected.");
        }
    }

    // Every button except the d-pad, and a trigger past halfway; stick clicks count, stick movement does not
    private boolean isAnyMenuButtonPressed(ControllerIndex controller) throws ControllerUnpluggedException {
        for (ControllerButton button : ControllerButton.values()) {
            if (button == ControllerButton.DPAD_UP || button == ControllerButton.DPAD_DOWN
                    || button == ControllerButton.DPAD_LEFT || button == ControllerButton.DPAD_RIGHT) {
                continue;
            }
            if (controller.isButtonPressed(button)) {
                return true;
            }
        }
        return controller.getAxisState(ControllerAxis.TRIGGERLEFT) > MENU_INPUT_STRENGTH_REQUIRED
                || controller.getAxisState(ControllerAxis.TRIGGERRIGHT) > MENU_INPUT_STRENGTH_REQUIRED;
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

    // What the menu sees right now: LEFT..DOWN from the stick or d-pad, CONFIRM = A, BACK = B, ANY_BUTTON = any other button
    boolean isMenuInputActive(MenuAction action) {
        return menuState.getOrDefault(action, false);
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
        menuState.clear();
    }
}
