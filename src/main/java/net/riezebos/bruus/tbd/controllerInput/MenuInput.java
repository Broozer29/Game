package net.riezebos.bruus.tbd.controllerInput;

import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// The one place that turns the main controller and the keyboard into menu actions, with one set of release and repeat rules.
// Everything runs on the Swing thread: the key dispatcher, and the screens that poll from their Swing timers.
public class MenuInput {
    private static final MenuInput instance = new MenuInput();
    private static final int MAX_QUEUED_KEY_ACTIONS = 16;
    private static final MenuAction[] CONTROLLER_ACTIONS = MenuAction.values();

    private final Deque<MenuAction> keyActions = new ArrayDeque<>();
    private final Set<Integer> heldKeys = new HashSet<>();
    private final Set<Integer> ignoredKeys = new HashSet<>(); // held when the screen opened, ignored until released once

    private ControllerInputReader lastReader;
    private final Set<MenuAction> ignoredControllerInputs = EnumSet.noneOf(MenuAction.class); // same, for the main controller
    private final Set<MenuAction> controllerInputsDown = EnumSet.noneOf(MenuAction.class); // as seen by the last poll
    private final Map<MenuAction, Long> nextRepeatTime = new EnumMap<>(MenuAction.class);

    private MenuInput() {
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.addKeyEventDispatcher(keyDispatcher());
        // Key releases are not seen when the game window loses focus, so forget the held keys then.
        // Not on focus changes inside the window: a screen switch moves focus, and held keys must stay known there
        focusManager.addPropertyChangeListener("activeWindow", e -> {
            if (e.getNewValue() == null) {
                forgetHeldKeys();
            }
        });
    }

    public static MenuInput getInstance() {
        return instance;
    }

    // Called whenever a screen opens. Drops what came in while nobody polled, and ignores everything that is held until it is released once.
    public void startScreen() {
        keyActions.clear();
        ignoredKeys.clear();
        ignoredKeys.addAll(heldKeys);

        nextRepeatTime.clear();
        controllerInputsDown.clear();
        lastReader = ControllerManager.getInstance().getPrimaryController();
        markHeldControllerInputs(lastReader);
    }

    // The actions since the last call, in order: keys first, then the main controller.
    // Confirm, back and any button come at most once per call.
    public List<MenuAction> poll() {
        List<MenuAction> actions = new ArrayList<>(keyActions);
        keyActions.clear();
        pollMainController(actions);

        Set<MenuAction> seenOnce = EnumSet.noneOf(MenuAction.class);
        List<MenuAction> result = new ArrayList<>();
        for (MenuAction action : actions) {
            boolean oncePerPoll = action == MenuAction.CONFIRM || action == MenuAction.BACK || action == MenuAction.ANY_BUTTON;
            if (oncePerPoll && !seenOnce.add(action)) {
                continue;
            }
            result.add(action);
        }
        return result;
    }

    // Only the main controller steers. A press shorter than one poll can be missed, as the screens poll once per paint.
    private void pollMainController(List<MenuAction> actions) {
        ControllerInputReader reader = ControllerManager.getInstance().getPrimaryController();
        if (reader == null) {
            lastReader = null; // so the next controller is fully marked
            controllerInputsDown.clear();
            nextRepeatTime.clear();
            ignoredControllerInputs.clear();
            return;
        }
        if (reader != lastReader) {
            // A different main controller: whatever it holds is ignored until released
            lastReader = reader;
            controllerInputsDown.clear();
            nextRepeatTime.clear();
            markHeldControllerInputs(reader);
        }

        long now = System.currentTimeMillis();
        for (MenuAction action : CONTROLLER_ACTIONS) {
            boolean active = reader.isMenuInputActive(action);
            if (ignoredControllerInputs.contains(action)) {
                if (!active) {
                    ignoredControllerInputs.remove(action);
                }
                continue;
            }
            if (!active) {
                controllerInputsDown.remove(action);
                continue;
            }
            boolean repeats = isDirection(action);
            if (controllerInputsDown.add(action)) {
                actions.add(action);
                if (repeats) {
                    nextRepeatTime.put(action, now + DataClass.MENU_REPEAT_FIRST_DELAY);
                }
            } else if (repeats && now >= nextRepeatTime.getOrDefault(action, now)) {
                actions.add(action);
                nextRepeatTime.put(action, now + DataClass.MENU_REPEAT_INTERVAL);
            }
        }
    }

    private void markHeldControllerInputs(ControllerInputReader reader) {
        ignoredControllerInputs.clear();
        if (reader == null) {
            return;
        }
        for (MenuAction action : CONTROLLER_ACTIONS) {
            if (reader.isMenuInputActive(action)) {
                ignoredControllerInputs.add(action);
            }
        }
    }

    private KeyEventDispatcher keyDispatcher() {
        return event -> {
            if (event.getID() == KeyEvent.KEY_PRESSED) {
                onKeyPressed(event.getKeyCode());
            } else if (event.getID() == KeyEvent.KEY_RELEASED) {
                heldKeys.remove(event.getKeyCode());
                ignoredKeys.remove(event.getKeyCode());
            }
            return false; // never consumes, so GameBoard and the ships still get their keys
        };
    }

    private void onKeyPressed(int keyCode) {
        boolean firstPress = heldKeys.add(keyCode); // a repeat from the operating system is not a first press
        if (ignoredKeys.contains(keyCode)) {
            return;
        }
        MenuAction direction = directionOf(keyCode);
        if (direction != null) {
            queueKeyAction(direction); // the operating system's key repeat is the repeat
        } else if (firstPress && (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE)) {
            queueKeyAction(MenuAction.CONFIRM);
        } else if (firstPress && keyCode == KeyEvent.VK_ESCAPE) {
            queueKeyAction(MenuAction.BACK);
        }
        if (firstPress) {
            queueKeyAction(MenuAction.ANY_BUTTON);
        }
    }

    private MenuAction directionOf(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                return MenuAction.LEFT;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                return MenuAction.RIGHT;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                return MenuAction.UP;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                return MenuAction.DOWN;
            default:
                return null;
        }
    }

    private boolean isDirection(MenuAction action) {
        return action == MenuAction.LEFT || action == MenuAction.RIGHT || action == MenuAction.UP || action == MenuAction.DOWN;
    }

    private void queueKeyAction(MenuAction action) {
        if (keyActions.size() >= MAX_QUEUED_KEY_ACTIONS) {
            keyActions.pollFirst(); // the oldest goes first
        }
        keyActions.addLast(action);
    }

    private void forgetHeldKeys() {
        heldKeys.clear();
        ignoredKeys.clear();
    }
}
