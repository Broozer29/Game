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
        // Assuming spaceship.getCenterX() and spaceship.getCenterY() give you the center of the spaceship
        double centerX = gameObject.getCenterXCoordinate();
        double centerY = gameObject.getCenterYCoordinate();

        // Calculate the original offset position of the exhaust
        // The 0.95 and 0.5 are static values that need to be changed depending on the image. Part of image enum?
        double exhaustOffsetX = centerX + (gameObject.getWidth() * 0.95);
        double exhaustOffsetY = centerY - (gameObject.getHeight() * 0.5);

        // Rotate the exhaust offset point by the spaceship's rotation angle
        return rotatePoint(centerX, centerY, exhaustOffsetX, exhaustOffsetY, angleDegrees);
    }

}
