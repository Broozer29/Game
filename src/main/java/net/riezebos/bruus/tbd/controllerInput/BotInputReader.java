package net.riezebos.bruus.tbd.controllerInput;

import java.util.EnumSet;
import java.util.Set;

// A controller that nobody holds: the autoplay bot sets what is pressed and the ship and the menus read it like a real one.
// Only used when the game starts with -Dgame.bot=true. Everything runs on the Swing thread.
public class BotInputReader extends ControllerInputReader {
    private static final float MOVE_THRESHOLD = 0.1f; // same strength a real stick needs to count as a direction
    private static final int TAP_HOLD_TICKS = 7; // about 100 ms: long enough for a menu that polls once per paint
    private static final int TAP_GAP_TICKS = 7;

    private final Set<ControllerInputEnums> inputs = EnumSet.noneOf(ControllerInputEnums.class);
    private final Set<MenuAction> menuActions = EnumSet.noneOf(MenuAction.class);
    private float xAxis;
    private float yAxis;
    private MenuAction tappedAction;
    private int tapTicksLeft;
    private int gapTicksLeft;

    @Override
    public boolean isVirtual() {
        return true;
    }

    // x and y run from -1 to 1; right and down are positive, like the left stick
    public void setMove(float x, float y) {
        xAxis = clamp(x);
        yAxis = clamp(y);
        set(ControllerInputEnums.MOVE_LEFT, xAxis <= -MOVE_THRESHOLD);
        set(ControllerInputEnums.MOVE_RIGHT, xAxis >= MOVE_THRESHOLD);
        set(ControllerInputEnums.MOVE_UP, yAxis <= -MOVE_THRESHOLD);
        set(ControllerInputEnums.MOVE_DOWN, yAxis >= MOVE_THRESHOLD);
    }

    public void setFire(boolean pressed) {
        set(ControllerInputEnums.FIRE, pressed);
    }

    public void setSpecialAttack(boolean pressed) {
        set(ControllerInputEnums.SPECIAL_ATTACK, pressed);
    }

    // Presses one menu button for a short moment. Ignored while an earlier tap is still held or cooling down,
    // so every tap is a separate press for MenuInput. Confirm also counts as "any button", like the A button does.
    public boolean tap(MenuAction action) {
        if (tappedAction != null || gapTicksLeft > 0) {
            return false;
        }
        tappedAction = action;
        tapTicksLeft = TAP_HOLD_TICKS;
        menuActions.add(action);
        if (action == MenuAction.CONFIRM || action == MenuAction.BACK) {
            menuActions.add(MenuAction.ANY_BUTTON);
        }
        return true;
    }

    // Called by the bot once per tick, before it decides anything new
    public void advanceTap() {
        if (tappedAction != null) {
            tapTicksLeft--;
            if (tapTicksLeft <= 0) {
                menuActions.clear();
                tappedAction = null;
                gapTicksLeft = TAP_GAP_TICKS;
            }
        } else if (gapTicksLeft > 0) {
            gapTicksLeft--;
        }
    }

    public void releaseGameplayInput() {
        setMove(0, 0);
        setFire(false);
        setSpecialAttack(false);
    }

    @Override
    public void resetInputStates() {
        inputs.clear();
        menuActions.clear();
        xAxis = 0;
        yAxis = 0;
        tappedAction = null;
        tapTicksLeft = 0;
        gapTicksLeft = 0;
    }

    @Override
    public void readController(com.studiohartman.jamepad.ControllerIndex controller) {
        // No physical controller to read
    }

    @Override
    public boolean isInputActive(ControllerInputEnums input) {
        return inputs.contains(input);
    }

    @Override
    public boolean isMenuInputActive(MenuAction action) {
        return menuActions.contains(action);
    }

    @Override
    public float getxAxisValue() {
        return xAxis;
    }

    @Override
    public float getyAxisValue() {
        return yAxis;
    }

    private void set(ControllerInputEnums input, boolean pressed) {
        if (pressed) {
            inputs.add(input);
        } else {
            inputs.remove(input);
        }
    }

    private float clamp(float value) {
        return Math.max(-1f, Math.min(1f, value));
    }
}
