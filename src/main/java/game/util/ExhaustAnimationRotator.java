package game.util;

import game.movement.Point;
import game.objects.GameObject;

public class ExhaustAnimationRotator {

    /**
     * Rotate a point around a center point by a given angle.
     * @param centerX The X coordinate of the center point.
     * @param centerY The Y coordinate of the center point.
     * @param pointX The X coordinate of the point to rotate.
     * @param pointY The Y coordinate of the point to rotate.
     * @param angleDegrees The angle in degrees to rotate.
     * @return A Point object representing the new coordinates after rotation.
     */
    private static Point rotatePoint(double centerX, double centerY, double pointX, double pointY, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);

        double cosTheta = Math.cos(angleRadians);
        double sinTheta = Math.sin(angleRadians);

        double dx = pointX - centerX;
        double dy = pointY - centerY;

        double newX = cosTheta * dx - sinTheta * dy + centerX;
        double newY = sinTheta * dx + cosTheta * dy + centerY;

        return new Point((int) newX, (int) newY);
    }

    /**
     * Calculate the new position for the exhaust based on the spaceship's rotation.
     * @param gameObject The spaceship object.
     * @param angleDegrees The angle in degrees the spaceship is rotated.
     * @return The new position for the exhaust animation.
     */
    public static Point calculateExhaustPosition(GameObject gameObject, double angleDegrees) {
        double centerX = gameObject.getCenterXCoordinate();
        double centerY = gameObject.getCenterYCoordinate();

        // Assume the exhaust needs to be positioned a fixed distance from the center at the back of the ship.
        // This distance is based on the ship's dimensions and should be adjusted to fit your game's visuals.
        double fixedDistanceFromCenterToExhaust = gameObject.getHeight() * 0.8;

        // Calculate the angle in radians for use in trigonometric functions
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the dynamic offset based on the rotation angle
        // The offsetX and offsetY determine the position of the exhaust relative to the ship's center.
        double offsetX = -Math.sin(angleRadians) * fixedDistanceFromCenterToExhaust;
        double offsetY = Math.cos(angleRadians) * fixedDistanceFromCenterToExhaust;

        // Adjust the position of the exhaust based on calculated offsets
        double exhaustPositionX = centerX + offsetX;
        double exhaustPositionY = centerY - offsetY; // Subtract offsetY because Y-coordinates increase downwards

        return new Point((int) exhaustPositionX, (int) exhaustPositionY);
    }



}
