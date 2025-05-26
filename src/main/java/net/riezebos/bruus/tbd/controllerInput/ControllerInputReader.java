package net.riezebos.bruus.tbd.controllerInput;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

import java.util.HashMap;
import java.util.Map;

public class ControllerInputReader {
    private Controller controller;
    private Map<ControllerInputEnums, Boolean> inputState = new HashMap<>();

    private float xAxisValue;
    private float yAxisValue;
    private float inputStrengthRequired;
    private boolean sensitiveInput;

    public ControllerInputReader(Controller controller) {
        this.controller = controller;
        this.setSensitiveInput(false);
    }

    public void pollController() {
        if (!controller.poll()) {
            System.out.println("Controller disconnected.");
            return;
        }
//        printPressedButton();

        EventQueue queue = controller.getEventQueue();
        Event event = new Event();

        while (queue.getNextEvent(event)) {
            Component comp = event.getComponent();
            float value = event.getValue();

            // Handle axis movement (Left Stick)
            if (comp.getIdentifier() == Component.Identifier.Axis.X) {
                xAxisValue = value;
                inputState.put(ControllerInputEnums.MOVE_LEFT, xAxisValue <= -inputStrengthRequired);
                inputState.put(ControllerInputEnums.MOVE_RIGHT, xAxisValue >= inputStrengthRequired);
            }
            if (comp.getIdentifier() == Component.Identifier.Axis.Y) {
                yAxisValue = value;
                inputState.put(ControllerInputEnums.MOVE_UP, yAxisValue <= -inputStrengthRequired);
                inputState.put(ControllerInputEnums.MOVE_DOWN, yAxisValue >= inputStrengthRequired);
            }

            // Handle button presses
            if (comp.getIdentifier() == Component.Identifier.Button._0) {
                inputState.put(ControllerInputEnums.FIRE, value == 1.0f); // Button A
            }
            if (comp.getIdentifier() == Component.Identifier.Button._1 || comp.getIdentifier() == Component.Identifier.Button._6 || comp.getIdentifier() == Component.Identifier.Button._7) {
                inputState.put(ControllerInputEnums.SPECIAL_ATTACK, value == 1.0f); // Button B
            }
            if (comp.getIdentifier() == Component.Identifier.Button._11) {
                inputState.put(ControllerInputEnums.PAUSE, value == 1.0f); // D-Pad Up
            }
        }
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

    public void printPressedButton() {
        if (!controller.poll()) {
            System.out.println("Controller disconnected.");
            return;
        }

        EventQueue queue = controller.getEventQueue();
        Event event = new Event();

        while (queue.getNextEvent(event)) {
            Component component = event.getComponent();
            float value = event.getValue();

            // Check if the component's value corresponds to a fully pressed state
            if (value == 1.0f) {
                System.out.println("Pressed: " + component.getIdentifier());
            }
        }
    }

    public float getxAxisValue() {
        return xAxisValue;
    }

    public float getyAxisValue() {
        return yAxisValue;
    }
}
