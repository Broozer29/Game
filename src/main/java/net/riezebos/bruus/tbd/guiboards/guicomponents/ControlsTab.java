package net.riezebos.bruus.tbd.guiboards.guicomponents;

import com.studiohartman.jamepad.ControllerButton;
import net.riezebos.bruus.tbd.controllerInput.InputDevice;
import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.controllerInput.MenuInput;
import net.riezebos.bruus.tbd.game.settings.BindableAction;
import net.riezebos.bruus.tbd.game.settings.InputBindings;
import net.riezebos.bruus.tbd.game.settings.SettingsManager;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

//The content of the CONTROLS tab inside the settings card: a header, one row per action with the binding of the device used last,
//the waiting state of a rebind, the message about an action that lost its binding, and the RESET text
//SettingsWindow keeps the page, the cursor and the SAVE CHANGES flow; it hands the rebind input over while an action waits
//All sizes below are in unscaled pixels and get multiplied by the resolution factor
public class ControlsTab {

    private static final int HEADER_Y = 105; //From the top of the card to the centre of the row
    private static final int FIRE_Y = 155;
    private static final int SPECIAL_ATTACK_Y = 210;
    private static final int HOLD_FIRE_Y = 265;
    private static final int MESSAGE_Y = 320;
    private static final int BOTTOM_Y = 395; //The same row as SAVE
    private static final int LABEL_X = -215; //From the centre of the card to the start of a label
    private static final int BINDING_X = 150; //From the centre of the card to the centre of the binding column
    private static final int RESET_X = -120; //From the centre of the card to the centre of RESET
    private static final int MAX_KEY_NAME_LENGTH = 16; //Keeps a key name inside the binding column

    private static final BindableAction[] ACTIONS = {BindableAction.FIRE, BindableAction.SPECIAL_ATTACK, BindableAction.HOLD_FIRE};

    private final SettingsManager settingsManager = SettingsManager.getInstance();
    private final int centerX;
    private final int cardTop;
    private final float resolutionFactor;
    private final float textScale;
    private final Map<BindableAction, GUITextCollection> labels = new EnumMap<>(BindableAction.class);
    private final Map<BindableAction, GUITextCollection> cells = new EnumMap<>(BindableAction.class);
    private final Map<BindableAction, String> cellTexts = new EnumMap<>(BindableAction.class);
    private final Map<BindableAction, Integer> rowYs = new EnumMap<>(BindableAction.class);
    private final GUITextCollection actionHeader;
    private final GUITextCollection resetLabel;
    private GUITextCollection deviceHeader;
    private String deviceHeaderText;
    private GUITextCollection messageText;
    private String messageTextShown; //The text messageText was built from
    private String message; //Shown until the next rebind, a tab switch, RESET or closing the page; null when there is none
    private BindableAction waitingAction; //The action that waits for its next input; null when none does

    public ControlsTab (int centerX, int cardTop, float resolutionFactor, float textScale) {
        this.centerX = centerX;
        this.cardTop = cardTop;
        this.resolutionFactor = resolutionFactor;
        this.textScale = textScale;
        rowYs.put(BindableAction.FIRE, FIRE_Y);
        rowYs.put(BindableAction.SPECIAL_ATTACK, SPECIAL_ATTACK_Y);
        rowYs.put(BindableAction.HOLD_FIRE, HOLD_FIRE_Y);
        actionHeader = MenuText.createText("ACTION", centerX + scaled(LABEL_X), cardTop + scaled(HEADER_Y), textScale, MenuText.ALIGN_LEFT);
        for (BindableAction action : ACTIONS) {
            labels.put(action, MenuText.createText(actionName(action), centerX + scaled(LABEL_X), cardTop + scaled(rowYs.get(action)), textScale, MenuText.ALIGN_LEFT));
        }
        resetLabel = MenuText.createText("RESET", centerX + scaled(RESET_X), cardTop + scaled(BOTTOM_Y), textScale, MenuText.ALIGN_CENTER);
    }

    private int scaled (int size) {
        return Math.round(size * resolutionFactor);
    }

    //The action names as the page shows them
    public static String actionName (BindableAction action) {
        switch (action) {
            case SPECIAL_ATTACK:
                return "SPECIAL ATTACK";
            case HOLD_FIRE:
                return "AUTO FIRE";
            default:
                return "FIRE";
        }
    }

    //The part of an action row the cursor stands beside
    public GUITextCollection getLabel (BindableAction action) {
        return labels.get(action);
    }

    public GUITextCollection getResetLabel () {
        return resetLabel;
    }

    public boolean isWaiting () {
        return waitingAction != null;
    }

    //The next input binds to this action
    public void startWaiting (BindableAction action) {
        waitingAction = action;
    }

    //Ends any waiting and clears the message; the bindings already made stay
    public void reset () {
        waitingAction = null;
        message = null;
    }

    //Puts both devices back to the default layout
    public void resetBindings () {
        settingsManager.replaceInputBindings(InputBindings.defaults());
        reset();
    }

    //While an action waits: a bindable key or button binds to it, Escape cancels, and any other press while the cursor is not on the waiting row cancels.
    //Moving the cursor never cancels. One press arrives as several inputs (Enter is a key, CONFIRM and ANY_BUTTON), so when the frame binds or cancels,
    //only the directions go on to the page, and a CONFIRM that cancelled from another row goes on to start waiting there.
    //cursorAction is the action row the cursor is on, null on the bottom row. Returns the actions the page still handles.
    public List<MenuAction> handleWhileWaiting (List<MenuAction> actions, List<Integer> keys, List<ControllerButton> buttons, BindableAction cursorAction) {
        boolean onWaitingRow = cursorAction == waitingAction;
        boolean cancelled = false;
        boolean bound = false;
        for (int key : keys) {
            if (key == KeyEvent.VK_ESCAPE) {
                cancelled = true;
            } else if (isMovementKey(key)) {
                continue;
            } else if (!onWaitingRow) {
                cancelled = true;
            } else if (InputBindings.isBindableKey(key)) {
                bindKey(key);
                bound = true;
            } else {
                continue; //P is the pause key: it never binds, and in the menus it does nothing
            }
            break;
        }
        if (!cancelled && !bound) {
            for (ControllerButton button : buttons) {
                if (!onWaitingRow) {
                    cancelled = true;
                } else if (InputBindings.isBindableButton(button)) {
                    bindButton(button);
                    bound = true;
                } else {
                    continue; //START and GUIDE never bind
                }
                break;
            }
        }
        if (!cancelled && !bound && !onWaitingRow && (actions.contains(MenuAction.CONFIRM) || actions.contains(MenuAction.BACK))) {
            cancelled = true; //The press has no raw input, but still counts as a press away from the waiting row
        }
        if (!cancelled && !bound) {
            return actions;
        }
        if (cancelled) {
            waitingAction = null;
        }
        List<MenuAction> remaining = new ArrayList<>();
        for (MenuAction action : actions) {
            boolean direction = action == MenuAction.UP || action == MenuAction.DOWN || action == MenuAction.LEFT || action == MenuAction.RIGHT;
            boolean confirmToNextRow = cancelled && !onWaitingRow && cursorAction != null && action == MenuAction.CONFIRM;
            if (direction || confirmToNextRow) {
                remaining.add(action);
            }
        }
        return remaining;
    }

    private boolean isMovementKey (int key) {
        switch (key) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_A:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                return true;
            default:
                return false;
        }
    }

    private void bindKey (int key) {
        InputBindings bindings = settingsManager.getSettings().getInputBindings();
        BindableAction previousOwner = bindings.actionUsingKey(key);
        settingsManager.replaceInputBindings(bindings.withKey(waitingAction, key));
        finishRebind(previousOwner);
    }

    private void bindButton (ControllerButton button) {
        InputBindings bindings = settingsManager.getSettings().getInputBindings();
        BindableAction previousOwner = bindings.actionUsingButton(button);
        settingsManager.replaceInputBindings(bindings.withButton(waitingAction, button));
        finishRebind(previousOwner);
    }

    //The waiting ends; if another action lost its binding, the message says so, otherwise an older message goes away
    private void finishRebind (BindableAction previousOwner) {
        message = previousOwner != null && previousOwner != waitingAction ? actionName(previousOwner) + " WAS UNBOUND" : null;
        waitingAction = null;
    }

    //The binding as it shows on the page: a key name, a button name, or NONE
    private String bindingName (BindableAction action, InputDevice device) {
        InputBindings bindings = settingsManager.getSettings().getInputBindings();
        return device == InputDevice.KEYBOARD ? keyName(bindings.getKey(action)) : buttonName(bindings.getButton(action));
    }

    //Menu letters only have a-z A-Z 0-9 ( ) : = ; > < . % , and space, and any other character crashes the game, so everything else is left out
    public static String keyName (int keyCode) {
        if (keyCode == KeyEvent.VK_UNDEFINED) {
            return "NONE";
        }
        String text = KeyEvent.getKeyText(keyCode).toUpperCase();
        StringBuilder name = new StringBuilder();
        for (char c : text.toCharArray()) {
            boolean allowed = (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || "():=;><.%,".indexOf(c) >= 0;
            if (allowed) {
                name.append(c);
            } else if (c == ' ' && name.length() > 0 && name.charAt(name.length() - 1) != ' ') {
                name.append(' ');
            }
        }
        String result = name.toString().trim();
        if (text.startsWith("UNKNOWN KEYCODE") || result.isEmpty()) {
            return "KEY " + keyCode;
        }
        return result.length() > MAX_KEY_NAME_LENGTH ? result.substring(0, MAX_KEY_NAME_LENGTH).trim() : result;
    }

    public static String buttonName (ControllerButton button) {
        if (button == null) {
            return "NONE";
        }
        switch (button) {
            case A:
                return "A";
            case B:
                return "B";
            case X:
                return "X";
            case Y:
                return "Y";
            case LEFTBUMPER:
                return "LB";
            case RIGHTBUMPER:
                return "RB";
            case LEFTSTICK:
                return "LS";
            case RIGHTSTICK:
                return "RS";
            case BACK:
                return "VIEW";
            case START:
                return "MENU";
            case BUTTON_MISC1:
                return "MISC";
            case BUTTON_PADDLE1:
                return "P1";
            case BUTTON_PADDLE2:
                return "P2";
            case BUTTON_PADDLE3:
                return "P3";
            case BUTTON_PADDLE4:
                return "P4";
            case BUTTON_TOUCHPAD:
                return "PAD";
            default:
                return "BUTTON"; //Not bindable anyway (d-pad, GUIDE)
        }
    }

    //Builds again the letters whose text changed since the last time
    private void refresh () {
        InputDevice device = MenuInput.getInstance().getLastDevice();
        String headerText = device == InputDevice.KEYBOARD ? "KEYBOARD" : "CONTROLLER";
        if (!headerText.equals(deviceHeaderText)) {
            deviceHeaderText = headerText;
            deviceHeader = MenuText.createText(headerText, centerX + scaled(BINDING_X), cardTop + scaled(HEADER_Y), textScale, MenuText.ALIGN_CENTER);
        }
        for (BindableAction action : ACTIONS) {
            String text = action == waitingAction
                    ? (device == InputDevice.KEYBOARD ? "PRESS A KEY" : "PRESS A BUTTON")
                    : bindingName(action, device);
            if (!text.equals(cellTexts.get(action))) {
                cellTexts.put(action, text);
                cells.put(action, MenuText.createText(text, centerX + scaled(BINDING_X), cardTop + scaled(rowYs.get(action)), textScale, MenuText.ALIGN_CENTER));
            }
        }
        if (message == null) {
            messageText = null;
            messageTextShown = null;
        } else if (!message.equals(messageTextShown)) {
            messageTextShown = message;
            messageText = MenuText.createText(message, centerX, cardTop + scaled(MESSAGE_Y), textScale, MenuText.ALIGN_CENTER);
        }
    }

    //The header, the binding column and the message, in the order they are drawn; the labels and RESET are drawn by the page
    public List<GUIComponent> getComponents () {
        refresh();
        List<GUIComponent> components = new ArrayList<>();
        components.addAll(actionHeader.getComponents());
        components.addAll(deviceHeader.getComponents());
        for (BindableAction action : ACTIONS) {
            components.addAll(cells.get(action).getComponents());
        }
        if (messageText != null) {
            components.addAll(messageText.getComponents());
        }
        return components;
    }
}
