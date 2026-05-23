package net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams;

import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

public class LaserbeamIndicator {

    private int startingXCoordinate;
    private int startingYCoordinate;

    private int endingXCoordinate;
    private int endingYCoordinate;

    private boolean active = true;

    private float maxAngleDegreeChange;
    private double currentAngleDegrees = 0;

    public LaserbeamIndicator(int startingXCoordinate, int startingYCoordinate, int endingXCoordinate, int endingYCoordinate, float maxAngleDegreeChange) {
        this.startingXCoordinate = startingXCoordinate;
        this.startingYCoordinate = startingYCoordinate;
        this.endingXCoordinate = endingXCoordinate;
        this.endingYCoordinate = endingYCoordinate;
        this.maxAngleDegreeChange = maxAngleDegreeChange;

        // Calculate initial angle based on starting and ending coordinates
        this.currentAngleDegrees = Math.toDegrees(Math.atan2(
                endingYCoordinate - startingYCoordinate,
                endingXCoordinate - startingXCoordinate
        ));
    }

    public void targetTowardsCoordinates(int targetXCoordinate, int targetYCoordinate) {
        // Calculate the desired angle to the target
        double desiredDegrees = Math.toDegrees(Math.atan2(
                targetYCoordinate - startingYCoordinate,
                targetXCoordinate - startingXCoordinate
        ));

        // Calculate the difference between the current angle and the desired angle
        double angleDifference = desiredDegrees - currentAngleDegrees;

        // Normalize the angle difference to the range [-180, 180] to ensure the shortest rotation path
        if (angleDifference > 180) {
            angleDifference -= 360;
        } else if (angleDifference < -180) {
            angleDifference += 360;
        }

        // Clamp the angle change to the maximum allowed rotation per update
        double clampedAngleChange = angleDifference;
        if (maxAngleDegreeChange > 0) {
            clampedAngleChange = Math.min(Math.abs(angleDifference), maxAngleDegreeChange);
            if (angleDifference < 0) {
                clampedAngleChange = -clampedAngleChange;
            }
        }

        // Apply the clamped angle change to the current angle
        currentAngleDegrees = (currentAngleDegrees + clampedAngleChange + 360) % 360;

        // Calculate ending coordinates based on the new angle
        // Use a fixed distance (e.g., screen width/height or a large value to ensure the line extends far enough)
        double distance = DataClass.getInstance().getWindowWidth(); // You can adjust this value based on your game's dimensions
        double angleRadians = Math.toRadians(currentAngleDegrees);

        endingXCoordinate = (int) (startingXCoordinate + Math.cos(angleRadians) * distance);
        endingYCoordinate = (int) (startingYCoordinate + Math.sin(angleRadians) * distance);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getStartingXCoordinate() {
        return startingXCoordinate;
    }

    public void setStartingXCoordinate(int startingXCoordinate) {
        this.startingXCoordinate = startingXCoordinate;
    }

    public int getStartingYCoordinate() {
        return startingYCoordinate;
    }

    public void setStartingYCoordinate(int startingYCoordinate) {
        this.startingYCoordinate = startingYCoordinate;
    }

    public int getEndingXCoordinate() {
        return endingXCoordinate;
    }

    public void setEndingXCoordinate(int endingXCoordinate) {
        this.endingXCoordinate = endingXCoordinate;
    }

    public int getEndingYCoordinate() {
        return endingYCoordinate;
    }

    public void setEndingYCoordinate(int endingYCoordinate) {
        this.endingYCoordinate = endingYCoordinate;
    }

    public double getCurrentAngleDegrees() {
        return currentAngleDegrees;
    }

    public void setCurrentAngleDegrees(double currentAngleDegrees) {
        this.currentAngleDegrees = currentAngleDegrees;
    }

    public float getMaxAngleDegreeChange() {
        return maxAngleDegreeChange;
    }

    public void setMaxAngleDegreeChange(float maxAngleDegreeChange) {
        this.maxAngleDegreeChange = maxAngleDegreeChange;
    }
}
