package net.riezebos.bruus.tbd.controllerInput;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.settings.BindableAction;
import net.riezebos.bruus.tbd.game.settings.InputBindings;
import net.riezebos.bruus.tbd.game.settings.SettingsManager;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ControllerInputReader {
    private Map<ControllerInputEnums, Boolean> inputState = new HashMap<>();

    private float xAxisValue;
    private float yAxisValue;
    private static final float INPUT_STRENGTH_REQUIRED = 0.1f;
    private static final float MENU_INPUT_STRENGTH_REQUIRED = 0.5f;
    // The stick turns a menu direction on past 60% and off again only below 35%, so a stick resting near the edge does not flicker
    private static final float MENU_PRESS = 0.6f;
    private static final float MENU_RELEASE = 0.35f;
    private final Set<MenuAction> stickDirections = EnumSet.noneOf(MenuAction.class);
    private final Map<MenuAction, Boolean> menuState = new EnumMap<>(MenuAction.class);
    private double lastGameSecondsTogglePressed = 0;
    private double toggleDelay = 1;
    private boolean holdFireButtonWasPressed = false;
    private boolean pauseButtonWasPressed = false;
    private boolean pausePressedSinceLastCheck = false;
    private boolean waitingForRelease = false;
    private static final int MAX_QUEUED_BUTTONS = 16;
    private final Set<ControllerButton> buttonsDown = EnumSet.noneOf(ControllerButton.class); // as seen by the last tick
    // Buttons that went down, for the CONTROLS tab: filled by the controller clock, drained by the settings page (both on the Swing thread); a full queue drops new presses
    private final BlockingQueue<ControllerButton> pressedButtons = new ArrayBlockingQueue<>(MAX_QUEUED_BUTTONS);

    // After joining a seat, the input that joined is ignored until everything is let go
    public void ignoreInputUntilReleased() {
        waitingForRelease = true;
    }

    // Called by the controller clock every tick with the slot that holds this reader's controller
    public void readController(ControllerIndex controller) {
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

            // Menus: the stick (only its stronger axis counts) or the d-pad
            boolean horizontal = Math.abs(xAxisValue) >= Math.abs(yAxisValue);
            boolean stickLeft = stickDirectionOn(MenuAction.LEFT, horizontal && xAxisValue <= 0 ? -xAxisValue : 0);
            boolean stickRight = stickDirectionOn(MenuAction.RIGHT, horizontal && xAxisValue >= 0 ? xAxisValue : 0);
            boolean stickUp = stickDirectionOn(MenuAction.UP, !horizontal && yAxisValue <= 0 ? -yAxisValue : 0);
            boolean stickDown = stickDirectionOn(MenuAction.DOWN, !horizontal && yAxisValue >= 0 ? yAxisValue : 0);
            menuState.put(MenuAction.LEFT, stickLeft || controller.isButtonPressed(ControllerButton.DPAD_LEFT));
            menuState.put(MenuAction.RIGHT, stickRight || controller.isButtonPressed(ControllerButton.DPAD_RIGHT));
            menuState.put(MenuAction.UP, stickUp || controller.isButtonPressed(ControllerButton.DPAD_UP));
            menuState.put(MenuAction.DOWN, stickDown || controller.isButtonPressed(ControllerButton.DPAD_DOWN));
            menuState.put(MenuAction.CONFIRM, controller.isButtonPressed(ControllerButton.A));
            menuState.put(MenuAction.BACK, controller.isButtonPressed(ControllerButton.B));
            menuState.put(MenuAction.ANY_BUTTON, isAnyMenuButtonPressed(controller));
            // Menus keep fixed buttons, whatever the action bindings are: the bumpers switch tabs
            menuState.put(MenuAction.PREVIOUS_TAB, controller.isButtonPressed(ControllerButton.LEFTBUMPER));
            menuState.put(MenuAction.NEXT_TAB, controller.isButtonPressed(ControllerButton.RIGHTBUMPER));

            queueButtonsThatWentDown(controller);

            InputBindings bindings = SettingsManager.getInstance().getSettings().getInputBindings();
            inputState.put(ControllerInputEnums.FIRE, isBoundButtonPressed(controller, bindings.getButton(BindableAction.FIRE))); // The bound fire button
            inputState.put(ControllerInputEnums.SPECIAL_ATTACK, isBoundButtonPressed(controller, bindings.getButton(BindableAction.SPECIAL_ATTACK))); // The bound special attack button
            boolean pauseButtonPressed = controller.isButtonPressed(ControllerButton.START); // The Menu button; pause can not be rebound
            inputState.put(ControllerInputEnums.PAUSE, pauseButtonPressed);
            // Remember the moment the button goes down, so holding it does not pause and unpause every frame
            if (pauseButtonPressed && !pauseButtonWasPressed) {
                pausePressedSinceLastCheck = true;
            }
            pauseButtonWasPressed = pauseButtonPressed;

            // Toggle only when the bound hold fire button goes down, so holding it does not toggle again every second
            boolean holdFireButtonPressed = isBoundButtonPressed(controller, bindings.getButton(BindableAction.HOLD_FIRE));
            // Not while paused: LB switches tabs in the paused settings page, so that press must not flip auto attack for after resume
            boolean paused = GameState.getInstance().getGameState() == GameStatusEnums.Paused;
            if (holdFireButtonPressed && !holdFireButtonWasPressed && !paused && GameState.getInstance().getGameSeconds() - lastGameSecondsTogglePressed > toggleDelay) {
                lastGameSecondsTogglePressed = GameState.getInstance().getGameSeconds();
                toggleHoldFire();
            }
            holdFireButtonWasPressed = holdFireButtonPressed;
        } catch (ControllerUnpluggedException e) {
            resetInputStates();
            System.out.println(e.getMessage() + " Controller disconnected.");
        }
    }

    // An action with nothing bound (null) is never pressed
    private boolean isBoundButtonPressed(ControllerIndex controller, ControllerButton button) throws ControllerUnpluggedException {
        return button != null && controller.isButtonPressed(button);
    }

    // Every button that went down since the last tick goes into the queue, except the d-pad which only steers menus; a full queue drops the newest
    private void queueButtonsThatWentDown(ControllerIndex controller) throws ControllerUnpluggedException {
        for (ControllerButton button : ControllerButton.values()) {
            if (controller.isButtonPressed(button)) {
                if (buttonsDown.add(button) && !isDpad(button)) {
                    pressedButtons.offer(button);
                }
            } else {
                buttonsDown.remove(button);
            }
        }
    }

    private boolean isDpad(ControllerButton button) {
        return button == ControllerButton.DPAD_UP || button == ControllerButton.DPAD_DOWN
                || button == ControllerButton.DPAD_LEFT || button == ControllerButton.DPAD_RIGHT;
    }

    // The buttons that went down since the last call, in order; asking clears them
    List<ControllerButton> pollPressedButtons() {
        List<ControllerButton> buttons = new ArrayList<>();
        pressedButtons.drainTo(buttons);
        return buttons;
    }

    // Remembers per direction whether the stick holds it: on past MENU_PRESS, off only below MENU_RELEASE
    private boolean stickDirectionOn(MenuAction direction, float strength) {
        boolean on = strength >= (stickDirections.contains(direction) ? MENU_RELEASE : MENU_PRESS);
        if (on) {
            stickDirections.add(direction);
        } else {
            stickDirections.remove(direction);
        }
        return on;
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

    // What the menu sees right now: LEFT..DOWN from the stick or d-pad, CONFIRM = A, BACK = B, ANY_BUTTON = any other button, PREVIOUS_TAB = LB, NEXT_TAB = RB
    public boolean isMenuInputActive(MenuAction action) {
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
        inputState.put(ControllerInputEnums.HOLD_FIRE, false); // The bound hold fire button
        inputState.put(ControllerInputEnums.FIRE, false); // The bound fire button
        inputState.put(ControllerInputEnums.SPECIAL_ATTACK, false); // The bound special attack button
        inputState.put(ControllerInputEnums.PAUSE, false); // The Menu button
        pausePressedSinceLastCheck = false;
        buttonsDown.clear();
        pressedButtons.clear();
        menuState.clear();
        stickDirections.clear();
    }
}
