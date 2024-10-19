package net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;

public class TrackingLaserBeam extends Laserbeam {

    private GameObject targetObject;
    private int lastOriginXCoordinate;
    private int lastOriginYCoordinate;
    private int lastTargetXCoordinate;
    private int lastTargetYCoordinate;

    private float maxRotationPerUpdate;
    private double desiredDegrees;

    public TrackingLaserBeam (LaserbeamConfiguration laserbeamConfiguration) {
        super(laserbeamConfiguration);
        this.targetObject = laserbeamConfiguration.getTargetToAimAt();
        // Initial angle calculation
        calculateAngle();
        initAnimations();
    }

    @Override
    public void update() {
        int frameAmount = this.laserBodies.get(0).getCurrentFrame();
        for (SpriteAnimation animation : this.laserBodies) {
            animation.setCurrentFrame(frameAmount);
        }

        // Update origin point if originObject is not null
        if (originObject != null) {
            lastOriginXCoordinate = originObject.getCenterXCoordinate() + this.xOffset;
            lastOriginYCoordinate = originObject.getCenterYCoordinate() + this.yOffset;
            int newX = originObject.getCenterXCoordinate() + this.xOffset;
            int newY = originObject.getCenterYCoordinate() + this.yOffset;

            // Only update if the position has changed
            if (lastOriginXCoordinate != newX || lastOriginYCoordinate != newY) {
                originPoint.setX(newX);
                originPoint.setY(newY);
                lastOriginXCoordinate = newX;
                lastOriginYCoordinate = newY;
                needsUpdate = true;
            }
        }

        // Update target point if targetObject is not null
        if (targetObject != null) {
            int targetX = targetObject.getCenterXCoordinate();
            int targetY = targetObject.getCenterYCoordinate();

            // Only recalculate if the target has moved
            if (targetX != lastTargetXCoordinate || targetY != lastTargetYCoordinate) {
                lastTargetXCoordinate = targetX;
                lastTargetYCoordinate = targetY;
                needsUpdate = true;
            }
        }

        // Check if the current angle differs from the target (desired) angle
        if (Math.abs(angleDegrees - this.desiredDegrees) > 0.01) {
            needsUpdate = true;
        }

        // If no update is needed, exit the method early
        if (!needsUpdate) return;
        calculateAngle();

        // Recalculate positions
        double deltaX_per_segment = Math.cos(angleRadians) * bodyWidth;
        double deltaY_per_segment = Math.sin(angleRadians) * bodyWidth;

        double x = originPoint.getX();
        double y = originPoint.getY();

        // Update positions of all segments
        for (int i = 0; i < laserBodies.size(); i++) {
            SpriteAnimation segment = laserBodies.get(i);
            segment.setXCoordinate((int) x);
            segment.setYCoordinate((int) y);

            // Rotate the segment based on the new angle
            segment.rotateAnimation(angleDegrees, false);

            x += deltaX_per_segment;
            y += deltaY_per_segment;
        }

        // If the laser beam has reached the desired angle, stop updating
        if (Math.abs(angleDegrees - this.desiredDegrees) <= 0.01) {
            needsUpdate = false;
        }
    }

    private void calculateAngle() {
        // Store the current angle degrees
        double currentAngleDegrees = angleDegrees;

        // Calculate the desired angle to the target
        int laserBodyWidth = laserBodies.get(1).getWidth();
        int laserBodyHeight = laserBodies.get(1).getHeight();

        // Calculate the angle that the laser should aim towards (desired angle)
        desiredDegrees = (float) Math.toDegrees(Math.atan2(
                (targetObject.getCenterYCoordinate() - (laserBodyHeight / 2)) - originPoint.getY(),
                (targetObject.getCenterXCoordinate() - (laserBodyWidth / 2)) - originPoint.getX()
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
        double clampedAngleChange = angleDifference; // Start with the full angle change
        if (maxRotationPerUpdate > 0) {
            clampedAngleChange = Math.min(Math.abs(angleDifference), maxRotationPerUpdate);
            if (angleDifference < 0) {
                clampedAngleChange = -clampedAngleChange; // Rotate counterclockwise if needed
            }
        }

        // Apply the clamped angle change to the current angle
        angleDegrees = (currentAngleDegrees + clampedAngleChange + 360) % 360;

        // Update angleRadians (if needed in other parts of the code)
        angleRadians = Math.toRadians(angleDegrees);
    }


    public float getMaxRotationPerUpdate () {
        return maxRotationPerUpdate;
    }

    public void setMaxRotationPerUpdate (float maxRotationPerUpdate) {
        this.maxRotationPerUpdate = maxRotationPerUpdate;
    }
}
