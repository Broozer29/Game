package net.riezebos.bruus.tbd.controllerInput;

// What a menu screen acts on; MenuInput turns the main controller and the keyboard into these
public enum MenuAction {
    LEFT,
    RIGHT,
    UP,
    DOWN,
    CONFIRM,
    BACK,
    ANY_BUTTON,
    PREVIOUS_TAB,
    NEXT_TAB,
    CYCLE_TAB, // the next tab, and from the last tab back to the first
}
