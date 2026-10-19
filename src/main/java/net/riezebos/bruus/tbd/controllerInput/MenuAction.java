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
    OPEN_FILTERS, // controller X, keyboard F: opens and closes the filter pane of the SHOPKEEPER page
    CLEAR_FILTERS, // controller Y, keyboard C: switches every filter off
}
