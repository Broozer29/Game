package net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams;

import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
public class AngledLaserBeam extends Laserbeam {

    private double lastAngleDegrees;

    public AngledLaserBeam(LaserbeamConfiguration laserbeamConfiguration) {
        super(laserbeamConfiguration);
        setAngleDegrees(laserbeamConfiguration.getAngleDegrees());
        setAngleRadians(Math.toRadians(laserbeamConfiguration.getAngleDegrees()));
        this.lastAngleDegrees = getAngleDegrees();
        initAnimations();
    }

    @Override
    public void update() {
        if(this.laserBodies.isEmpty()){
            return;
        }
        int frameAmount = this.laserBodies.get(0).getCurrentFrame();
        super.updateTransparancyconfig();
        for(SpriteAnimation animation : this.laserBodies){
            animation.setCurrentFrame(frameAmount);
        }

        // Update origin point if originObject is not null
        if (originObject != null) {
            int newX = originObject.getCenterXCoordinate() + this.xOffset;
            int newY = originObject.getCenterYCoordinate() + this.yOffset;

            // Only update if the position has changed
            if (originPoint.getX() != newX || originPoint.getY() != newY) {
                originPoint.setX(newX);
                originPoint.setY(newY);
                needsUpdate = true;
            }
        }

        if(getAngleDegrees() != lastAngleDegrees){
            needsUpdate = true;
        }

        if (!needsUpdate) return;

        // Recalculate positions
        double deltaX_per_segment = Math.cos(getAngleRadians()) * bodyWidth;
        double deltaY_per_segment = Math.sin(getAngleRadians()) * bodyWidth;

        double x = originPoint.getX();
        double y = originPoint.getY();

        // Update positions of all segments
        for (int i = 0; i < laserBodies.size(); i++) {
            SpriteAnimation segment = laserBodies.get(i);
            segment.setXCoordinate((int) x);
            segment.setYCoordinate((int) y);

            // Only need to rotate once since angle doesn't change
            if (needsUpdate) {
                segment.rotateAnimation(getAngleDegrees(), false);
            }

            x += deltaX_per_segment;
            y += deltaY_per_segment;
        }

        needsUpdate = false;
    }
}
