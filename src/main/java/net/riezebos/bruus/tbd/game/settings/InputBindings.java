package net.riezebos.bruus.tbd.game.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studiohartman.jamepad.ControllerButton;

import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

//The keyboard key and the controller button for each bindable action, as stored in settings.json.
//Keys are stored as Java key codes and buttons by their Jamepad name, both under the action name.
//The two maps hold whatever the file says; repair() turns them into valid bindings.
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputBindings {

    private static final Map<BindableAction, Integer> DEFAULT_KEYS = new EnumMap<>(BindableAction.class);
    private static final Map<BindableAction, ControllerButton> DEFAULT_BUTTONS = new EnumMap<>(BindableAction.class);

    static {
        DEFAULT_KEYS.put(BindableAction.FIRE, KeyEvent.VK_SPACE);
        DEFAULT_KEYS.put(BindableAction.SPECIAL_ATTACK, KeyEvent.VK_Q);
        DEFAULT_KEYS.put(BindableAction.HOLD_FIRE, KeyEvent.VK_E);
        DEFAULT_KEYS.put(BindableAction.PAUSE, KeyEvent.VK_P);
        DEFAULT_BUTTONS.put(BindableAction.FIRE, ControllerButton.A);
        DEFAULT_BUTTONS.put(BindableAction.SPECIAL_ATTACK, ControllerButton.B);
        DEFAULT_BUTTONS.put(BindableAction.HOLD_FIRE, ControllerButton.LEFTBUMPER);
        DEFAULT_BUTTONS.put(BindableAction.PAUSE, ControllerButton.START);
    }

    private Map<String, Object> keyboard = new LinkedHashMap<>();
    private Map<String, Object> controller = new LinkedHashMap<>();

    //What the game reads, filled by repair()
    @JsonIgnore
    private final Map<BindableAction, Integer> keys = new EnumMap<>(BindableAction.class);
    @JsonIgnore
    private final Map<BindableAction, ControllerButton> buttons = new EnumMap<>(BindableAction.class);

    public InputBindings() {
        keys.putAll(DEFAULT_KEYS);
        buttons.putAll(DEFAULT_BUTTONS);
        writeMaps();
    }

    public Map<String, Object> getKeyboard() {
        return keyboard;
    }

    //Anything that is not a map is kept as an empty map, so a bad value cannot make the whole file fail
    public void setKeyboard(Object keyboard) {
        this.keyboard = asMap(keyboard);
    }

    public Map<String, Object> getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = asMap(controller);
    }

    public int getKey(BindableAction action) {
        return keys.get(action);
    }

    public ControllerButton getButton(BindableAction action) {
        return buttons.get(action);
    }

    //Keys that stay fixed for moving and for the menus
    public static boolean isBindableKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_A:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_UNDEFINED:
                return false;
            default:
                return true;
        }
    }

    //The d-pad stays fixed for the menus, and GUIDE opens the Windows Game Bar
    public static boolean isBindableButton(ControllerButton button) {
        switch (button) {
            case DPAD_UP:
            case DPAD_DOWN:
            case DPAD_LEFT:
            case DPAD_RIGHT:
            case GUIDE:
                return false;
            default:
                return true;
        }
    }

    //A value that is missing, unknown or not bindable becomes the default of that action.
    //If two actions of one device then share an input, that device goes back to its default layout.
    public void repair() {
        Map<BindableAction, Integer> repairedKeys = new EnumMap<>(BindableAction.class);
        Map<BindableAction, ControllerButton> repairedButtons = new EnumMap<>(BindableAction.class);
        for (BindableAction action : BindableAction.values()) {
            Object keyValue = keyboard.get(action.name());
            if (keyValue instanceof Number && isBindableKey(((Number) keyValue).intValue())) {
                repairedKeys.put(action, ((Number) keyValue).intValue());
            } else {
                System.out.println("settings.json: keyboard binding for " + action + " (" + keyValue + ") is not valid, using the default");
                repairedKeys.put(action, DEFAULT_KEYS.get(action));
            }
            Object buttonValue = controller.get(action.name());
            ControllerButton button = toButton(buttonValue);
            if (button != null) {
                repairedButtons.put(action, button);
            } else {
                System.out.println("settings.json: controller binding for " + action + " (" + buttonValue + ") is not valid, using the default");
                repairedButtons.put(action, DEFAULT_BUTTONS.get(action));
            }
        }
        if (new HashSet<>(repairedKeys.values()).size() < repairedKeys.size()) {
            System.out.println("settings.json: two keyboard actions share one input, using the default keyboard layout");
            repairedKeys = new EnumMap<>(DEFAULT_KEYS);
        }
        if (new HashSet<>(repairedButtons.values()).size() < repairedButtons.size()) {
            System.out.println("settings.json: two controller actions share one input, using the default controller layout");
            repairedButtons = new EnumMap<>(DEFAULT_BUTTONS);
        }
        keys.clear();
        keys.putAll(repairedKeys);
        buttons.clear();
        buttons.putAll(repairedButtons);
        writeMaps();
    }

    //A separate, already repaired object with its own maps
    public InputBindings copyOf() {
        InputBindings copy = new InputBindings();
        copy.keys.putAll(this.keys);
        copy.buttons.putAll(this.buttons);
        copy.writeMaps();
        return copy;
    }

    public boolean hasSameValuesAs(InputBindings other) {
        return other != null
                && this.keys.equals(other.keys)
                && this.buttons.equals(other.buttons);
    }

    private void writeMaps() {
        keyboard = new LinkedHashMap<>();
        controller = new LinkedHashMap<>();
        for (BindableAction action : BindableAction.values()) {
            keyboard.put(action.name(), keys.get(action));
            controller.put(action.name(), buttons.get(action).name());
        }
    }

    private static ControllerButton toButton(Object value) {
        if (!(value instanceof String)) {
            return null;
        }
        try {
            ControllerButton button = ControllerButton.valueOf((String) value);
            return isBindableButton(button) ? button : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object value) {
        return value instanceof Map ? new LinkedHashMap<>((Map<String, Object>) value) : new LinkedHashMap<>();
    }
}
