package game.gameobjects.missiles.specialAttacks;

import VisualAndAudioData.DataClass;
import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.GameObject;
import game.movement.Direction;
import game.movement.Point;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;

public class Laserbeam {

    private ArrayList<SpriteAnimation> laserBodies = new ArrayList<>(); // List of laser body animations
    private SpriteAnimation laserOriginAnimation;

    private Point originPoint;
    private GameObject originObject;
    private Direction direction;
    private static final float scale = 1; //This shit breaks when it's anything but 1 so lets just not bother okay? okay
    private static int screenWidth = DataClass.getInstance().getWindowWidth();
    private static int screenHeight = DataClass.getInstance().getWindowHeight();
    private static int padding = 50; //Used to ensure that the laserbeam goes beyond the screens windows
    private int bodyWidth = 64;

    private int xOffset;
    private int yOffset;
    private GameObject owner;


    public Laserbeam (LaserbeamConfiguration laserbeamConfiguration) {
        this.bodyWidth = Math.round(this.bodyWidth * scale);
        this.xOffset = laserbeamConfiguration.getxOffset();
        this.yOffset = laserbeamConfiguration.getyOffset();

        if(laserbeamConfiguration.getOriginObject() != null) {
            this.originObject = laserbeamConfiguration.getOriginObject();
            this.originPoint = new Point(originObject.getCenterXCoordinate(), originObject.getCenterYCoordinate());
        }

        if(laserbeamConfiguration.getOriginPoint() != null){
            this.originPoint = laserbeamConfiguration.getOriginPoint();
        }
        this.direction = laserbeamConfiguration.getDirection();

        if(laserbeamConfiguration.getOwner() != null){
            this.owner = laserbeamConfiguration.getOwner();
        }

        createLaserOriginAnimation();
        adjustOriginPointForDirection();
        initAnimations();
    }

    private void initAnimations () {
        laserBodies.clear(); // Clear any existing segments
        createLaserBodies();
    }

    private void createLaserOriginAnimation () {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(this.originPoint.getX());
        spriteConfiguration.setyCoordinate(this.originPoint.getY());
        spriteConfiguration.setScale(scale * 1.5f);
        spriteConfiguration.setImageType(ImageEnums.LaserbeamHead);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, true);
        this.laserOriginAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        laserOriginAnimation.setCenterCoordinates(this.originPoint.getX(), this.originPoint.getY());
    }

    private void createLaserBodies () {
        // Determine end point based on the direction
        Point endPoint = calculateEndPoint();

        int x0 = originPoint.getX();
        int y0 = originPoint.getY();
        int x1 = endPoint.getX();
        int y1 = endPoint.getY();

        int dxTotal = x1 - x0;
        int dyTotal = y1 - y0;

        double totalDistance = Math.sqrt(dxTotal * dxTotal + dyTotal * dyTotal);

        int numSegments = (int) (totalDistance / bodyWidth) + 1;

        int[] deltaPerSegment = getDeltaPerSegment(direction);
        int deltaX_per_segment = deltaPerSegment[0];
        int deltaY_per_segment = deltaPerSegment[1];

        int x = x0;
        int y = y0;

        for (int i = 0; i < numSegments; i++) {
            SpriteAnimation laserBodyPart = createLaserBodySegment(x, y, direction.toAngle());
            laserBodies.add(laserBodyPart);

            x += deltaX_per_segment;
            y += deltaY_per_segment;
        }
    }


    // Helper method to create a laser body segment at a given position
    private SpriteAnimation createLaserBodySegment (double posX, double posY, double angleDegrees) {
        SpriteConfiguration laserBodyConfig = new SpriteConfiguration();
        laserBodyConfig.setxCoordinate((int) posX);
        laserBodyConfig.setyCoordinate((int) posY);
        laserBodyConfig.setScale(scale);
        laserBodyConfig.setImageType(ImageEnums.LaserbeamBody);

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


    // Method to update the laser beam's segments based on changing origin point
    public void update () {
        // Update origin point if originObject is not null

        int currentFrame = laserBodies.get(0).getCurrentFrame();
        for(SpriteAnimation spriteAnimation : laserBodies){
            spriteAnimation.setCurrentFrame(currentFrame);
        }

        if (originObject != null) {
            originPoint.setX(originObject.getCenterXCoordinate());
            originPoint.setY(originObject.getCenterYCoordinate());
            adjustOriginPointForDirection();
        }

        // Determine end point based on the direction
        Point endPoint = calculateEndPoint();

        int x0 = originPoint.getX();
        int y0 = originPoint.getY();
        int x1 = endPoint.getX();
        int y1 = endPoint.getY();

        int dxTotal = x1 - x0;
        int dyTotal = y1 - y0;

        double totalDistance = Math.sqrt(dxTotal * dxTotal + dyTotal * dyTotal);

        int numSegmentsNeeded = (int) (totalDistance / bodyWidth) + 1;

        int currentNumSegments = laserBodies.size();

        int[] deltaPerSegment = getDeltaPerSegment(direction);
        int deltaX_per_segment = deltaPerSegment[0];
        int deltaY_per_segment = deltaPerSegment[1];

        if (numSegmentsNeeded > currentNumSegments) {
            // Add new segments
            int x = originPoint.getX() + currentNumSegments * deltaX_per_segment;
            int y = originPoint.getY() + currentNumSegments * deltaY_per_segment;

            for (int i = currentNumSegments; i < numSegmentsNeeded; i++) {
                SpriteAnimation laserBodyPart = createLaserBodySegment(x, y, direction.toAngle());
                laserBodies.add(laserBodyPart);

                x += deltaX_per_segment;
                y += deltaY_per_segment;
            }
        } else if (numSegmentsNeeded < currentNumSegments) {
            // Remove excess segments
            for (int i = currentNumSegments - 1; i >= numSegmentsNeeded; i--) {
                SpriteAnimation segmentToRemove = laserBodies.remove(i);
                // Optionally, set the segment to invisible or perform any cleanup
                segmentToRemove.setVisible(false);
            }
        }

        // Update positions of all segments
        int x = originPoint.getX();
        int y = originPoint.getY();

        for (SpriteAnimation segment : laserBodies) {
            segment.setCenterCoordinates(x, y);
            x += deltaX_per_segment;
            y += deltaY_per_segment;
        }
    }

    // Adjust the origin point based on the direction to avoid visual bugs
    private void adjustOriginPointForDirection () {
        switch (direction) {
            case LEFT:
                originPoint.setX(originPoint.getX() - bodyWidth);
                laserOriginAnimation.setCenterCoordinates(
                        originPoint.getX() + (bodyWidth / 2),
                        originPoint.getY()
                );
                break;
            case RIGHT:
                originPoint.setX(originPoint.getX() + bodyWidth);
                laserOriginAnimation.setCenterCoordinates(
                        originPoint.getX() - (bodyWidth / 2),
                        originPoint.getY()
                );
                break;
            case LEFT_UP:
                originPoint.setX(originPoint.getX() - bodyWidth);
                originPoint.setY(originPoint.getY() - (bodyWidth / 2));
                laserOriginAnimation.setCenterCoordinates(
                        originPoint.getX() + (bodyWidth / 4),
                        originPoint.getY() + (bodyWidth / 4)
                );
                break;
            case LEFT_DOWN:
                originPoint.setX(originPoint.getX() - bodyWidth);
                originPoint.setY(originPoint.getY() + (bodyWidth / 2));
                laserOriginAnimation.setCenterCoordinates(
                        originPoint.getX() + (bodyWidth / 4),
                        originPoint.getY() - (bodyWidth / 4)
                );
                break;
            case RIGHT_UP:
                originPoint.setX(originPoint.getX() + bodyWidth);
                originPoint.setY(originPoint.getY() - (bodyWidth / 2));
                laserOriginAnimation.setCenterCoordinates(
                        originPoint.getX() - (bodyWidth / 4),
                        originPoint.getY() + (bodyWidth / 4)
                );
                break;
            case RIGHT_DOWN:
                originPoint.setX(originPoint.getX() + bodyWidth);
                originPoint.setY(originPoint.getY() + (bodyWidth / 2));
                laserOriginAnimation.setCenterCoordinates(
                        originPoint.getX() - (bodyWidth / 4),
                        originPoint.getY() - (bodyWidth / 4)
                );
                break;
            case UP:
                originPoint.setY(originPoint.getY() - bodyWidth / 2);
                laserOriginAnimation.setCenterCoordinates(
                        originPoint.getX(),
                        originPoint.getY() + (bodyWidth / 4)
                );
                break;
            case DOWN:
                originPoint.setY(originPoint.getY() + bodyWidth / 2);
                laserOriginAnimation.setCenterCoordinates(
                        originPoint.getX(),
                        originPoint.getY() - (bodyWidth / 4)
                );
                break;
        }
    }

    private int[] getDeltaPerSegment (Direction direction) {
        int deltaX = 0;
        int deltaY = 0;
        int diagonalStep = (int) Math.round(bodyWidth / Math.sqrt(2));

        switch (direction) {
            case RIGHT:
                deltaX = bodyWidth;
                deltaY = 0;
                break;
            case LEFT:
                deltaX = -bodyWidth;
                deltaY = 0;
                break;
            case UP:
                deltaX = 0;
                deltaY = -bodyWidth;
                break;
            case DOWN:
                deltaX = 0;
                deltaY = bodyWidth;
                break;
            case RIGHT_UP:
                deltaX = diagonalStep;
                deltaY = -diagonalStep;
                break;
            case RIGHT_DOWN:
                deltaX = diagonalStep;
                deltaY = diagonalStep;
                break;
            case LEFT_UP:
                deltaX = -diagonalStep;
                deltaY = -diagonalStep;
                break;
            case LEFT_DOWN:
                deltaX = -diagonalStep;
                deltaY = diagonalStep;
                break;
            default:
                break;
        }
        return new int[]{deltaX, deltaY};
    }

    // Method to calculate the end point based on the direction
    private Point calculateEndPoint () {
        int endX = originPoint.getX();
        int endY = originPoint.getY();

        switch (direction) {
            case RIGHT:
            case RIGHT_UP:
            case RIGHT_DOWN:
                endX = screenWidth + padding;
                break;
            case LEFT:
            case LEFT_UP:
            case LEFT_DOWN:
                endX = -padding;
                break;
            default:
                // For UP and DOWN directions, endX remains the same
                break;
        }

        switch (direction) {
            case DOWN:
            case RIGHT_DOWN:
            case LEFT_DOWN:
                endY = screenHeight + padding;
                break;
            case UP:
            case RIGHT_UP:
            case LEFT_UP:
                endY = -padding;
                break;
            default:
                // For LEFT and RIGHT directions, endY remains the same
                break;
        }

        return new Point(endX, endY);
    }

    public ArrayList<SpriteAnimation> getLaserBodies () {
        return laserBodies;
    }

    public void setVisible (boolean visible) {
        if (!visible) {
            for (SpriteAnimation animation : laserBodies) {
                animation.setVisible(false);
            }
            this.laserBodies.clear();
            this.laserOriginAnimation.setVisible(false);
            this.laserOriginAnimation = null;
        }
    }

    public SpriteAnimation getLaserOriginAnimation () {
        return laserOriginAnimation;
    }

    public void setLaserOriginAnimation (SpriteAnimation laserOriginAnimation) {
        this.laserOriginAnimation = laserOriginAnimation;
    }

    public int getxOffset () {
        return xOffset;
    }

    public void setxOffset (int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset () {
        return yOffset;
    }

    public void setyOffset (int yOffset) {
        this.yOffset = yOffset;
    }

    public void setOriginPoint(Point point){
        this.originPoint = point;
        this.originPoint.setX(originPoint.getX() + xOffset);
        this.originPoint.setY(originPoint.getY() + yOffset);
    }

    public GameObject getOwner () {
        return owner;
    }

    public void setOwner (GameObject owner) {
        this.owner = owner;
    }
}
