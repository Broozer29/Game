package game.gameobjects.missiles.laserbeams;

import VisualAndAudioData.DataClass;
import VisualAndAudioData.image.ImageDatabase;
import VisualAndAudioData.image.ImageEnums;
import VisualAndAudioData.image.ImageRotator;
import game.gameobjects.GameObject;
import game.movement.Point;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Laserbeam {

    protected ArrayList<SpriteAnimation> laserBodies = new ArrayList<>(); // List of laser body animations

    protected float knockbackStrength;
    protected Point originPoint;
    protected GameObject originObject;

    protected double angleDegrees;       // Angle in degrees for rotation
    protected double angleRadians;      // Angle in radians for calculations

    protected static final float scale = 1; // This should remain at 1 for now
    protected static int screenWidth = DataClass.getInstance().getWindowWidth();
    protected static int screenHeight = DataClass.getInstance().getWindowHeight();
    protected static int padding = 50; // Used to ensure that the laserbeam goes beyond the screen window
    public static int bodyWidth = 64;
    protected boolean visible;
    protected int xOffset;
    protected int yOffset;
    protected GameObject owner;
    protected float damage;

    protected boolean blocksMovement;
    protected int amountOfLaserbeamBodySegments;

    protected boolean needsUpdate = true; // Flag to control when to update

    public Laserbeam(LaserbeamConfiguration laserbeamConfiguration) {
        this.knockbackStrength = 10;
        this.visible = true;
        this.xOffset = laserbeamConfiguration.getxOffset();
        this.yOffset = laserbeamConfiguration.getyOffset();
        this.damage = laserbeamConfiguration.getDamage();
        this.blocksMovement = laserbeamConfiguration.isBlocksMovement();

        if (laserbeamConfiguration.getOriginObject() != null) {
            this.originObject = laserbeamConfiguration.getOriginObject();
            this.originPoint = new Point(originObject.getCenterXCoordinate(), originObject.getCenterYCoordinate());
        }

        if (laserbeamConfiguration.getOriginPoint() != null) {
            this.originPoint = laserbeamConfiguration.getOriginPoint();
        }

        if (laserbeamConfiguration.getOwner() != null) {
            this.owner = laserbeamConfiguration.getOwner();
        }

        if (laserbeamConfiguration.getAmountOfLaserbeamSegments() != 0) {
            this.amountOfLaserbeamBodySegments = laserbeamConfiguration.getAmountOfLaserbeamSegments();
        } else {
            // Default value
            this.amountOfLaserbeamBodySegments = 60;
        }

        initAnimations();
    }

    protected void initAnimations() {
        laserBodies.clear(); // Clear any existing segments
        createLaserBodies();
    }

    protected void createLaserBodies() {
        // Ensure at least two segments (start and end)
        int numSegments = Math.max(2, this.amountOfLaserbeamBodySegments);

        // delta per segment
        double deltaX_per_segment = Math.cos(angleRadians) * bodyWidth;
        double deltaY_per_segment = Math.sin(angleRadians) * bodyWidth;

        double x = originPoint.getX();
        double y = originPoint.getY();

        for (int i = 0; i < numSegments; i++) {
            ImageEnums imageType;
            if (i == 0) {
                imageType = ImageEnums.LaserbeamStart; // Start of the laser beam
            } else if (i == numSegments - 1) {
                imageType = ImageEnums.LaserbeamEnd; // End of the laser beam
            } else {
                imageType = ImageEnums.LaserbeamBody; // Middle segments
            }

            SpriteAnimation laserBodyPart = createLaserBodySegment(x, y, angleDegrees, imageType);
            laserBodies.add(laserBodyPart);

            x += deltaX_per_segment;
            y += deltaY_per_segment;
        }

        needsUpdate = false; // Initial creation done
    }

    // Helper method to create a laser body segment at a given position
    protected SpriteAnimation createLaserBodySegment(double posX, double posY, double angleDegrees, ImageEnums imageType) {
        SpriteConfiguration laserBodyConfig = new SpriteConfiguration();
        laserBodyConfig.setxCoordinate((int) posX);
        laserBodyConfig.setyCoordinate((int) posY);
        laserBodyConfig.setScale(scale);
        laserBodyConfig.setImageType(imageType);

        SpriteAnimationConfiguration bodyAnimConfig = new SpriteAnimationConfiguration(laserBodyConfig, 1, true);
        SpriteAnimation laserBodyPart = new SpriteAnimation(bodyAnimConfig);

        laserBodyPart.rotateAnimation(angleDegrees, false);

        // Synchronize the frame with the first segment if it exists
        if (!laserBodies.isEmpty()) {
            laserBodyPart.setCurrentFrame(laserBodies.get(0).getCurrentFrame());
        }
        laserBodyPart.setCenterCoordinates((int) posX, (int) posY);
        return laserBodyPart;
    }

    // Abstract method to be implemented by subclasses
    public abstract void update();

    // Method to calculate the end point based on the angle
    protected Point calculateEndPoint(double angleRadians) {
        // Extend the laser beam beyond the screen in the direction of angleRadians
        double maxDistance = Math.hypot(screenWidth, screenHeight) + padding;

        double endX = originPoint.getX() + Math.cos(angleRadians) * maxDistance;
        double endY = originPoint.getY() + Math.sin(angleRadians) * maxDistance;

        return new Point((int) endX, (int) endY);
    }

    public ArrayList<SpriteAnimation> getLaserBodies() {
        return laserBodies;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (!visible) {
            for (SpriteAnimation animation : laserBodies) {
                animation.setVisible(false);
            }
            this.laserBodies.clear();
        }
    }

    public int getxOffset() {
        return xOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public void setOriginPoint(Point point) {
        this.originPoint = point;
        this.originPoint.setX(originPoint.getX() + xOffset);
        this.originPoint.setY(originPoint.getY() + yOffset);
    }

    public GameObject getOwner() {
        return owner;
    }

    public void setOwner(GameObject owner) {
        this.owner = owner;
    }

    public float getKnockBackStrength() {
        return this.knockbackStrength;
    }

    public void setKnockbackStrength (float knockbackStrength) {
        this.knockbackStrength = knockbackStrength;
    }

    public boolean isBlocksMovement() {
        return blocksMovement;
    }

    public void setBlocksMovement(boolean blocksMovement) {
        this.blocksMovement = blocksMovement;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public double getAngleDegrees () {
        return angleDegrees;
    }

    public void setAngleDegrees (double angleDegrees) {
        this.angleDegrees = angleDegrees;
        angleRadians = Math.toRadians(angleDegrees);
    }

    public double getAngleRadians () {
        return angleRadians;
    }

    public void setAngleRadians (double angleRadians) {
        this.angleRadians = angleRadians;
    }

    public boolean isVisible () {
        return visible;
    }
}